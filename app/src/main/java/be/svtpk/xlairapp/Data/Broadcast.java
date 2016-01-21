package be.svtpk.xlairapp.Data;

import com.orm.SugarRecord;

/**
 * Created by Sveta on 07/01/16.
 */
public class Broadcast extends SugarRecord {

    public String title;
    public String description;
    public String uri;


    public Broadcast() {
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
