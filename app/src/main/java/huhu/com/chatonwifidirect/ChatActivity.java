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
import android.widget.TextView;

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
    private TextView tv_head;
    private Button btn_back;
    //自己的名字
    private String name;
    //对方的名字
    private String p2pname;
    //聊天记录列表
    private ArrayList<ChatEntity> recordlist;
    //适配器实例
    private RecordListAdapter adapter;
    /**
     * case 1:己方发送数据导致列表项数据改变
     * case 2:接收到消息导致列表项数据改变
     * case 3:对方已经退出聊天
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
                    break;
                case 3:
                    ToastBuilder.Build("对方已退出聊天", ChatActivity.this);
                    ChatActivity.this.finish();
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
        //获取到自己的名字
        name = i.getExtras().get("devicename").toString();
        p2pname = i.getExtras().get("p2pname").toString();
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
        tv_head = (TextView) findViewById(R.id.tv_head);
        btn_back = (Button) findViewById(R.id.btn_back);
        //将对方名字显示在head
        tv_head.setText(p2pname);
        //初始化了聊天记录列表
        recordlist = new ArrayList<>();
        adapter = new RecordListAdapter(recordlist, this, handler);
        lv_chatRecord.setAdapter(adapter);
        //为发送按钮设置监听器
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = editText_message.getText().toString();
                if (word.length() == 0) {
                    ToastBuilder.Build("请输入文字", ChatActivity.this);
                } else {
                    ChatEntity chatEntity = new ChatEntity();
                    chatEntity.setName(name + ":");
                    chatEntity.setWord(editText_message.getText().toString());
                    //将自己发送的信息加入列表
                    recordlist.add(chatEntity);
                    //通知列表更新数据
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    //将输入框清空
                    editText_message.setText("");
                    try {
                        //将数据发送给对方
                        chatThread.write(chatEntity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        //按下返回按钮
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    goBack();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 按下返回按钮，结束线程任务。
     * 发送一个内容为quit的chatentity结束对话
     */
    private void goBack() throws IOException {
        ChatEntity end = new ChatEntity();
        end.setName(name + ":");
        end.setWord(Constants.endSignal);
        chatThread.write(end);
        ChatActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        btn_back.performClick();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatThread.interrupt();
    }
}
