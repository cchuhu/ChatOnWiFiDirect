package huhu.com.chatonwifidirect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wifidirect.R;

import java.net.InetAddress;
import java.util.ArrayList;

public class MainActivity extends Activity {

    //自定义的广播接收器对象
    private MyReceiver myReceiver;
    //wifi管理类对象
    private WifiP2pManager wifiP2pManager;
    //获取channel对象
    private WifiP2pManager.Channel channel;
    //设备信息文本框
    private TextView tv_myname, tv_mystatus;
    //搜索按钮
    private Button btn_search;
    //列表实例
    private ListView lv_peers;
    //适配器实例
    private ListAdapter adapter;
    //设备列表
    private ArrayList<WifiP2pDevice> mPeerslist;
    //判断谁是组长
    private boolean isGroupOwner;
    //组长的地址
    private InetAddress groupOwnerAddress;
    //设备在当前列表中的位置
    private int position;
    //自己的设备信息
    private WifiP2pDevice device;
    /**
     * case 1:获取到数据
     * case 2:自己的设备发生变化
     * case 3:选中一个设备并连接
     * case 4:判断是否是组长
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mPeerslist = FindPeers.getDeviceList();
                    presentData(mPeerslist);
                    break;
                case 2:
                    device = ((WifiP2pDevice) msg.obj);
                    updateDevice(device);
                    break;
                case 3:
                    position = msg.arg1;
                    connectDevice(mPeerslist.get(position));
                    break;
                case 4:
                    groupOwnerAddress = (InetAddress) msg.obj;
                    if (msg.arg1 == 1) {
                        isGroupOwner = true;
                    } else {
                        isGroupOwner = false;
                    }
                    ToastBuilder.Build("搭讪成功(^.^) ", MainActivity.this);
                    jumpToChat(isGroupOwner, groupOwnerAddress, device.deviceName);


            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //建立过滤器
        IntentFilter filter = getIntentFilter(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION, WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION, WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION, WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        //注册广播接收器
        registerReceiver(myReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }


    /**
     * 初始化资源的方法
     */
    private void init() {
        //实例化视图
        tv_myname = (TextView) findViewById(R.id.tv_myname);
        tv_mystatus = (TextView) findViewById(R.id.tv_mystatus);
        btn_search = (Button) findViewById(R.id.btn_search);
        lv_peers = (ListView) findViewById(R.id.listview_peers);
        //实例化数组
        mPeerslist = new ArrayList();
        //获取wifi管理类
        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        //获取channel对象
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);
        //实例化receiver
        myReceiver = new MyReceiver(wifiP2pManager, channel, this, handler);
        //为按钮设置监听
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discover();
            }
        });

    }

    /**
     * 扫描对等体的方法
     */
    private void discover() {
        //开启了扫描过程并立即返回，仅仅表示初始化过程是否正确
        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                ToastBuilder.Build("扫描成功", MainActivity.this);
            }

            @Override
            public void onFailure(int i) {
                ToastBuilder.Build("扫描失败", MainActivity.this);
            }
        });
    }

    /**
     * 呈现列表数据
     *
     * @param mPeerslist 获取到的设备列表
     */
    private void presentData(ArrayList<WifiP2pDevice> mPeerslist) {
        adapter = new ListAdapter(mPeerslist, MainActivity.this, handler);
        lv_peers.setAdapter(adapter);

    }

    /**
     * 更新设备状态的方法
     *
     * @param device
     */
    private void updateDevice(WifiP2pDevice device) {
        tv_myname.setText(device.deviceName);
        String str_status;

        switch (device.status) {
            case WifiP2pDevice.AVAILABLE:
                str_status = "空闲中";
                break;
            case WifiP2pDevice.CONNECTED:
                str_status = "连接中";
                break;
            case WifiP2pDevice.FAILED:
                str_status = "连接失败";
                break;
            case WifiP2pDevice.INVITED:
                str_status = "收到邀请";
                break;
            case WifiP2pDevice.UNAVAILABLE:
                str_status = "设备不可用";
                break;
            default:
                str_status = "未知状况";
        }
        tv_mystatus.setText(str_status);
    }

    /**
     * 与指定设备连接
     *
     * @param device 要连接的设备
     */
    private void connectDevice(WifiP2pDevice device) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        //该接口仅仅通知连接是否成功
        wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                ToastBuilder.Build("正在搭讪中...(0.0) ", MainActivity.this);
            }

            @Override
            public void onFailure(int i) {
                ToastBuilder.Build("搭讪失败，请重试(>_<)", MainActivity.this);
            }
        });
    }

    /**
     *  设置过滤器的方法
     *
     * @param kvs 需要过滤的动作
     * @return 返回设置好的过滤器
     */
    private IntentFilter getIntentFilter(String... kvs) {
        IntentFilter filter = new IntentFilter();
        for (int i = 0; i < kvs.length; i++) {
            filter.addAction(kvs[i]);
        }
        return filter;
    }

    /**
     * 跳转到聊天界面
     *
     * @param isGroupOwner      是否是组长
     * @param groupOwnerAddress 组长的ip地址
     * @name 自己的名字
     */
    private void jumpToChat(boolean isGroupOwner, InetAddress groupOwnerAddress, String name) {
        Intent i = new Intent();
        i.putExtra("isGroupOwner", isGroupOwner);
        i.putExtra("groupOwnerAddress", groupOwnerAddress);
        i.putExtra("devicename", name);
        i.putExtra("p2pname", mPeerslist.get(position).deviceName);
        i.setClass(MainActivity.this, ChatActivity.class);
        startActivity(i);

    }
}
