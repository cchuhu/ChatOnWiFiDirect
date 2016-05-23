package huhu.com.chatonwifidirect.Entity;

import java.io.Serializable;

/**
 * Created by Huhu on 4/27/16.
 * 聊天信息基类,包括姓名，内容和时间戳
 */
public class ChatEntity implements Serializable{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }



    private String name;
    private String word;

}
