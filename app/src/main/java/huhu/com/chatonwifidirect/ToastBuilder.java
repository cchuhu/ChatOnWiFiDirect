package huhu.com.chatonwifidirect;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Huhu on 4/24/16.
 * 显示Toast的工具类
 */
public class ToastBuilder {
    /**
     * @param word    要显示的文字
     * @param context 上下文对象
     */
    public static void Build(String word, Context context) {
        Toast.makeText(context, word, Toast.LENGTH_SHORT).show();
    }
}
