package be.svtpk.xlairapp.Adapters;


/**
 * Created by Sveta on 18/01/15. Based on Android Design & Development Werkcollege 7: Visit Brussels v2.
 */

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import be.svtpk.xlairapp.Data.Programme;

/**
 * An {@link android.app.IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class FileDownloader extends IntentService {

    public static String DOWNLOAD_ACTION = "FILE_DOWNLOADER";

    public FileDownloader() {
        super("FileDownloader");
    }

    public Bitmap downloadImage(String path)throws MalformedURLException,IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        Bitmap myBitmap = null;
        try {
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize = 4; //decrease decoded image
            myBitmap = BitmapFactory.decodeStream(input, null, options);
        }
        catch (Error e) {
            myBitmap = null;
        }

        return myBitmap;
    }

    public String saveImage(String path,Bitmap myBitmap) throws IOException {
        File f = new File(getFilesDir(), path);

        FileOutputStream stream = new FileOutputStream(f);

        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outstream);
        byte[] byteArray = outstream.toByteArray();

        stream.write(byteArray);
        stream.close();

        return f.getPath();
    }

    public BufferedInputStream downloadAudio(String path)throws MalformedURLException,IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        BufferedInputStream myAudio = new BufferedInputStream(input);

        return myAudio;
    }

    public String saveAudio(String path, BufferedInputStream myAudio) throws IOException {
        File f = new File(getFilesDir(), path);

        FileOutputStream stream = new FileOutputStream(f);

        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        int bytesRead;
        byte[] buffer = new byte[1024 * 50];
        while ((bytesRead = myAudio.read(buffer)) != -1) {
            outstream.write(buffer, 0, bytesRead);
        }
        byte[] byteArray = outstream.toByteArray();

        stream.write(byteArray);
        stream.close();

        return f.getPath();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        for(int i = 0; i < Programme.listAll(Programme.class).size(); i ++){

            Programme item = Programme.listAll(Programme.class).get(i);

            try{
                Thread.sleep(1000);
            }
            catch(Exception e){

            }

            try {

                File file = new File(item.getImageFileSrc());

                if(!file.exists()) {
                    Bitmap image = downloadImage(item.getImgSrc());
                    if (image != null) {
                        item.setImageFileSrc(saveImage(item.getImage(), image));

                        //update in database
                        item.save();

                        Intent brIntent = new Intent();
                        brIntent.setAction(DOWNLOAD_ACTION);
                        brIntent.putExtra("index",i);

                        sendBroadcast(brIntent);
                    }
                }


                //BufferedInputStream audio = downloadAudio(item.getAudio());
                //item.setAudioFileSrc(saveAudio(item.getAudioName(), audio));


            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
