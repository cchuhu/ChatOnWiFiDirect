package huhu.com.chatonwifidirect.Util;

/**
 * Created by Huhu on 4/27/16.
 * 常量类,存放自定义广播
 */
public class Constants {
    //断开连接的广播
    public static final String DISCONNECT_ACTION = "huhu.com.action.ACTION_DISCONNECT";
    //发送新消息的广播
    public static final String NEW_MESSAGE_ACTION = "huhu.com.action.ACTION_NEW";
    //更新消息的广播
    public static final String UPDATE_LIST_ACTION = "huhu.com.action.ACTION_UPDATE";
    //端口号
    public static int PORT = 8898;
    //对话结束标志
    public static String endSignal = "quit";

}
