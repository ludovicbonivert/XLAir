package be.svtpk.xlairapp.Data;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.Date;

/**
 * Created by Sveta on 07/01/16.
 */
public class Event extends SugarRecord {

    @SerializedName("event_id")
    int eventId;
    String title;
    String description;
    String image;
    String imageFileSrc = "";
    String datum;
    String startuur;
    String locatie;
    String creator;
    @SerializedName("created_at")
    Date createdAt;
    @SerializedName("updated_at")
    Date updatedAt;
    @Ignore
    private static final String BASE_URL = "http://www.xlair.be/public/uploads/events/";


    public Event() {
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImgSrc() {
        return (BASE_URL + this.image.replace(" ", "%20"));
    }

    public String getImageFileSrc() {
        return imageFileSrc;
    }

    public void setImageFileSrc(String imageFileSrc) {
        this.imageFileSrc = imageFileSrc;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getStartuur() {
        return startuur;
    }

    public void setStartuur(String startuur) {
        this.startuur = startuur;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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
}
