package huhu.com.chatonwifidirect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.wifidirect.R;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * 聊天记录界面
 */
public class ChatActivity extends AppCompatActivity {
    //服务器和客户端线程
    private ChatThread chatThread;
    //View
    private EditText editText_message;
    private Button btn_send;
    private ListView lv_chatRecord;
    //自己的名字
    private String name;
    //聊天记录列表
    private ArrayList<ChatEntity> recordlist;
    //适配器实例
    private RecordListAdapter adapter;
    /**
     * case 1:己方发送数据导致列表项数据改变
     * case 2:接收到消息导致列表项数据改变
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    ChatEntity chatEntity = (ChatEntity) msg.obj;
                    recordlist.add(chatEntity);
                    adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        judge();
        initView();

    }


    /**
     * 判断该启动哪一种线程
     */
    private void judge() {
        Intent i = getIntent();
        //获取到组长
        Boolean isGroupOwner = i.getExtras().getBoolean("isGroupOwner");
        //获取到组长地址
        InetAddress inetAddress = (InetAddress) i.getExtras().get("groupOwnerAddress");
        //获取到对方的名字
        name = i.getExtras().get("devicename").toString();
        if (isGroupOwner) {
            chatThread = new ServerThread(Constants.PORT, handler);
        } else {
            chatThread = new ClientThread(inetAddress, Constants.PORT, handler);
        }
        chatThread.start();
    }

    /**
     * 初始化界面元素
     */
    private void initView() {
        //初始化View
        editText_message = (EditText) findViewById(R.id.tv_message);
        btn_send = (Button) findViewById(R.id.btn_sendMessage);
        lv_chatRecord = (ListView) findViewById(R.id.lv_chatRecord);
        //初始化了聊天记录列表
        recordlist = new ArrayList<>();
        adapter = new RecordListAdapter(recordlist, this, handler);
        lv_chatRecord.setAdapter(adapter);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = editText_message.getText().toString();
                if (word.equals("")) {
                    ToastBuilder.Build("请输入文字", ChatActivity.this);
                } else {
                    ChatEntity chatEntity = new ChatEntity();
                    chatEntity.setName(name);
                    chatEntity.setWord(editText_message.getText().toString());
                    //将自己发送的信息加入列表
                    recordlist.add(chatEntity);
                    //通知列表更新数据
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    try {
                        chatThread.write(chatEntity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
