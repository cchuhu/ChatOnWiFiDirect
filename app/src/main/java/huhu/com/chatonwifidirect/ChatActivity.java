package huhu.com.chatonwifidirect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.wifidirect.R;

import java.io.IOException;
import java.net.InetAddress;

public class ChatActivity extends AppCompatActivity {
    //服务器和客户端线程
    private ChatThread chatThread;
    //实例化View
    private EditText editText_message;
    private Button btn_send;
    private ListView lv_chatRecord;

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
        Boolean isGroupOwner = i.getExtras().getBoolean("isGroupOwner");
        InetAddress inetAddress = (InetAddress) i.getExtras().get("groupOwnerAddress");
        if (isGroupOwner) {
            chatThread = new ServerThread(Constants.PORT, this);
        } else {
            chatThread = new ClientThread(inetAddress, Constants.PORT, this);
        }
        chatThread.start();
    }

    /**
     * 初始化界面元素
     */
    private void initView() {
        editText_message = (EditText) findViewById(R.id.tv_message);
        btn_send = (Button) findViewById(R.id.btn_sendMessage);
        lv_chatRecord = (ListView) findViewById(R.id.lv_chatRecord);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = editText_message.getText().toString();
                if (word.equals("")) {
                    ToastBuilder.Build("请输入文字", ChatActivity.this);
                } else {
                    ChatEntity chatEntity = new ChatEntity();
                    chatEntity.setName("hh");
                    chatEntity.setWord(editText_message.getText().toString());

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
