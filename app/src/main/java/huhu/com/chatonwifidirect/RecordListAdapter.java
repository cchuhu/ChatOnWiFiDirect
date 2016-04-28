package huhu.com.chatonwifidirect;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wifidirect.R;

import java.util.ArrayList;

/**
 * Created by Huhu on 4/28/16.
 * 聊天记录列表的适配器
 */
public class RecordListAdapter extends BaseAdapter {
    //设备列表
    private ArrayList<ChatEntity> recordlist;
    //上下文对象
    private Context context;
    //handler对象
    private Handler handler;

    public RecordListAdapter(ArrayList<ChatEntity> recordlist, Context context, Handler handler) {
        this.recordlist = recordlist;
        this.context = context;
        this.handler = handler;

    }

    @Override
    public int getCount() {
        return recordlist.size();
    }

    @Override
    public Object getItem(int i) {
        return recordlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        MyHolder holder = null;
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.chatentity, null);
            holder = new MyHolder((TextView) convertView.findViewById(R.id.tv_recordname), (TextView) convertView.findViewById(R.id.tv_recordcontent));
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }
        holder.tv_recordname.setText(recordlist.get(i).getName());
        holder.tv_recordcontent.setText(recordlist.get(i).getWord());

        return convertView;
    }


    class MyHolder {
        private TextView tv_recordname;
        private TextView tv_recordcontent;

        public MyHolder(TextView tv_recordname, TextView tv_recordcontent) {
            this.tv_recordname = tv_recordname;
            this.tv_recordcontent = tv_recordcontent;


        }
    }
}
