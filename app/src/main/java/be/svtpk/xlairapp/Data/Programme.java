package be.svtpk.xlairapp.Data;

import java.util.List;

/**
 * Created by Sveta on 07/01/16.
 */
public class Programme {
    String title;
    String desc;
    int iconId;
    List<Broadcast> broadcastList;

    public Programme(String title, String desc, int iconId, List<Broadcast> broadcastList) {
        this.title = title;
        this.desc = desc;
        this.iconId = iconId;
        this.broadcastList = broadcastList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public List<Broadcast> getBroadcastList() {
        return broadcastList;
    }

    public void setBroadcastList(List<Broadcast> broadcastList) {
        this.broadcastList = broadcastList;
    }


}