package huhu.com.chatonwifidirect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.wifidirect.R;

import java.net.InetAddress;

public class ChatActivity extends AppCompatActivity {
    private ServerThread serverThread;
    private ClientThread clientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent i = getIntent();
        Boolean isGroupOwner = i.getExtras().getBoolean("isGroupOwner");
        InetAddress inetAddress = (InetAddress) i.getExtras().get("groupOwnerAddress");
        if (isGroupOwner) {
            ToastBuilder.Build("是组长" + inetAddress, this);
        } else {
            ToastBuilder.Build("不是组长" + inetAddress, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
