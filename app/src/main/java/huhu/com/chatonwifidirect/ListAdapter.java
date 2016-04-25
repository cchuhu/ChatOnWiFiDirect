package huhu.com.chatonwifidirect;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.wifidirect.R;

import java.util.ArrayList;

/**
 * Created by Huhu on 4/25/16.
 * 设备列表的适配器
 */
public class ListAdapter extends BaseAdapter {
    //设备列表
    private ArrayList<WifiP2pDevice> deviceArrayList;
    //上下文对象
    private Context context;
    //handler对象
    private Handler handler;

    public ListAdapter(ArrayList<WifiP2pDevice> deviceArrayList, Context context, Handler handler) {
        this.deviceArrayList = deviceArrayList;
        this.context = context;
        this.handler = handler;

    }

    @Override
    public int getCount() {
        return deviceArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return deviceArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        MyHolder holder = null;
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item, null);
            holder = new MyHolder((TextView) convertView.findViewById(R.id.tv_device_name), (TextView) convertView.findViewById(R.id.tv_connectstatus), (Button) convertView.findViewById(R.id.btn_connect));
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }
        holder.tv_name.setText(deviceArrayList.get(i).deviceName);
        holder.btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 3;
                msg.arg1 = i;
                handler.sendMessage(msg);
            }
        });


        return convertView;
    }


    class MyHolder {
        private TextView tv_name;
        private Button btn_connect;

        public MyHolder(TextView tv_name, TextView tv_connectstatus, Button btn_connect) {
            this.tv_name = tv_name;
            this.btn_connect = btn_connect;


        }
    }
}
