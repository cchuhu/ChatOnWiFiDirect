package huhu.com.chatonwifidirect;

import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;

/**
 * Created by Huhu on 4/25/16.
 */
public class ConnectPeers implements WifiP2pManager.ConnectionInfoListener {
    private Handler handler;


    public ConnectPeers(Handler handler) {
        this.handler = handler;

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {

    }
}
