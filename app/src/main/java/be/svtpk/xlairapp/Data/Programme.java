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

    String imageFileSrc = "";

    @SerializedName("created_at")
    Date createdAt;

    @SerializedName("updated_at")
    Date updatedAt;

    @Ignore
    List<Broadcast> broadcastList = new ArrayList<Broadcast>();


    public Programme() {

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

    public String getImgSrc() {
        return "http://www.xlair.be/public/uploads/programs/" + this.image;
    }

    public String getImageFileSrc() {
        return imageFileSrc;
    }

    public void setImageFileSrc(String imageFileSrc) {
        this.imageFileSrc = imageFileSrc;
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