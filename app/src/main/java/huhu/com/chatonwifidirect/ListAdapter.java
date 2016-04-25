package huhu.com.chatonwifidirect;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

    public ListAdapter(ArrayList<WifiP2pDevice> deviceArrayList, Context context) {
        this.deviceArrayList = deviceArrayList;
        this.context = context;

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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MyHolder holder = null;
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item, null);
            holder = new MyHolder((TextView) convertView.findViewById(R.id.tv_device_name));
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }
        holder.tv_name.setText(deviceArrayList.get(i).deviceName);


        return convertView;
    }

    class MyHolder {
        private TextView tv_name;

        public MyHolder(TextView tv_name) {
            this.tv_name = tv_name;

        }
    }
}
