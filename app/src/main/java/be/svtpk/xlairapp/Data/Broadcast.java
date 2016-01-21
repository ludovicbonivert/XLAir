package be.svtpk.xlairapp.Data;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by Sveta on 07/01/16.
 */
public class Broadcast extends SugarRecord {

    public String title;
    public String description;
    public String uri;
    public Programme programme;
    @Ignore
    private static final String BASE_URL = "http://www.xlair.be";


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
        return (BASE_URL + uri);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Programme getProgramme() {
        return programme;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }
}
