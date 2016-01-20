package be.svtpk.xlairapp.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by Sveta on 07/01/16.
 */
public class Programme extends SugarRecord {

    @SerializedName("programma_id")
    long programmeId;

    @SerializedName("name")
    String title;

    @SerializedName("description")
    String desc;

    String image;

    int iconId;

    @SerializedName("created_at")
    Date createdAt;

    @SerializedName("updated_at")
    Date updatedAt;

    @Ignore
    List<Broadcast> broadcastList = new ArrayList<Broadcast>();

    public Programme() {

    }

    public Programme(String title, String desc, int iconId, List<Broadcast> broadcastList) {
        this.title = title;
        this.desc = desc;
        this.iconId = iconId;
        this.broadcastList = broadcastList;
    }

    public Programme(long programmeId, String title, String desc, int iconId, Date createdAt, Date updatedAt, List<Broadcast> broadcastList) {
        this.programmeId = programmeId;
        this.title = title;
        this.desc = desc;
        this.iconId = iconId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.broadcastList = broadcastList;
    }

    public long getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(long programmeId) {
        this.programmeId = programmeId;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Broadcast> getBroadcastList() {
        return broadcastList;
    }

    public void setBroadcastList(List<Broadcast> broadcastList) {
        this.broadcastList = broadcastList;
    }


}