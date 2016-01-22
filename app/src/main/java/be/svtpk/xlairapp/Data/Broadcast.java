package be.svtpk.xlairapp.Data;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Sveta on 07/01/16.
 */
public class Broadcast extends SugarRecord {

    @SerializedName("naam")
    public String title;
    @SerializedName("locatie")
    public String uri;
    public Programme programme;
    @Ignore
    private static final String BASE_URL = "http://www.xlair.be/public/uploads/audio/";


    public Broadcast() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return (BASE_URL + this.programme.getTitle().replace(" ", "%20") + "/" + this.uri.replace(" ", "%20"));
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
