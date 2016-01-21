package com.ludovicsvetlana.xlair;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "HvLUDxv9TTrmGPcc0Ak9TLOUg";
    private static final String TWITTER_SECRET = "7TQnTAABlQB6wi8ABcvkCkXwI6axIyRCWfW0sjDZouCZvY4GGo";

    public static final String LOG_TAG = "XLAIR";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerListener;
    private ListView listView_navigation;
    private String[] listView_navigation_items;

    MediaPlayer mPlayer;
    Button playButton;
    boolean playerIsPlaying = false;
    public String URLToStream = "http://streaming.ritcs.be:8000/.mp3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        // http://stackoverflow.com/questions/28541459/getting-403-forbidden-when-using-twitter-fabric-to-get-user-timeline
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> result) {


            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText(getApplicationContext(), "Could not retrieve tweets", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });

        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        listView_navigation = (ListView) findViewById(R.id.left_drawer_listview);
        listView_navigation_items = getResources().getStringArray(R.array.menu_items);

        listView_navigation.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listView_navigation_items));
        listView_navigation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.the_toolbar);
        setSupportActionBar(myToolbar);

        drawerListener = new ActionBarDrawerToggle(this, drawerLayout,
                myToolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerListener);

        getSupportActionBar().setHomeButtonEnabled(true);

        findAndSetupPlayer();
        //setupHTTPConnectionAndJsonFetching();

        //http://jsonplaceholder.typicode.com/posts
        new HTTPRequestAndGetJsonTask().execute("http://www.xlair.be/scheme/data");

        /*

        Click on title to open navigation drawer werkt nog niet
        
        myToolbar.setNavigationIcon(R.drawable.ic_drawer);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // dit toont up button icon
        */

    }

    private void setupHTTPConnectionAndJsonFetching() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try{
            URL urlToMomenteleUitzending = new URL("http://www.xlair.be/scheme/data");
            connection = (HttpURLConnection) urlToMomenteleUitzending.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();


            String line = "";
            while((line = reader.readLine()) != null){
                buffer.append(line);
            }

            jsonParsing(line);

        }catch(MalformedURLException e){
            e.printStackTrace();;
        }catch(IOException e){
            e.printStackTrace();
        } finally{
            if(connection != null){
                connection.disconnect();
            }
            try{
                if(reader != null){
                    reader.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }

    private void jsonParsing(String theJson) {
        Log.d(LOG_TAG, "Json content " + theJson);
    }

    private void findAndSetupPlayer(){
        playerIsPlaying = false;

        playButton = (Button) findViewById(R.id.playbtn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!playerIsPlaying){
                    createAndPlayMusicPlayer();
                    playButton.setText(R.string.action_pause);
                    playerIsPlaying = true;
                }else{
                    mPlayer.stop();
                    playButton.setText(R.string.action_play);
                    playerIsPlaying = false;
                }
            }
        });
    }


    protected void createAndPlayMusicPlayer(){
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mPlayer.setDataSource(URLToStream);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepareAsync();
        }catch (IllegalArgumentException e){
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mPlayer.start();
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerListener.syncState();
    }

    private void selectItem(int position) {
        listView_navigation.setItemChecked(position, true);
        changeTitleTo(listView_navigation_items[position]);
        drawerLayout.closeDrawer(listView_navigation);
    }

    private void changeTitleTo(String listView_navigation_item) {
        getSupportActionBar().setTitle(listView_navigation_item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.action_tweet:
                Log.d("Xlair", "Clicked on tweeting");
                createTweet();
                return true;
            case R.id.action_settings:
                Log.d("Xlair", "Clicked on settings");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void createTweet(){
        // Intent is going to open browser if no twitter app is found on phone
        String url = "http://www.twitter.com/intent/tweet?hashtags=XLAIR";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


    public class HTTPRequestAndGetJsonTask extends AsyncTask<String, String, JSONArray>{

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        @Override
        protected JSONArray doInBackground(String... params) {
            try{
                //URL urlToMomenteleUitzending = new URL("http://www.xlair.be/scheme/data");
                URL urlToMomenteleUitzending = new URL(params[0]);
                connection = (HttpURLConnection) urlToMomenteleUitzending.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();


                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                try{
                    JSONArray parentObject = new JSONArray(buffer.toString());
                    return parentObject;
                }catch(JSONException e){
                    e.printStackTrace();
                }

                //return buffer.toString();

                //jsonParsing(line);

            }catch(MalformedURLException e){
                e.printStackTrace();;
            }catch(IOException e){
                e.printStackTrace();
            } finally{
                if(connection != null){
                    connection.disconnect();
                }
                try{
                    if(reader != null){
                        reader.close();
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            return null;
        }



        @Override
        protected void onPostExecute(JSONArray theJson) {
            if(theJson != null){
                super.onPostExecute(theJson);
                Log.d(LOG_TAG, "Result of theJson" + theJson);
                EditText e = (EditText) findViewById(R.id.editText);
                if(theJson.length() == 0){
                    e.setText("Geen uitzending");
                }else{
                    e.setText("Wel uitzending");
                }


            } else {
                Log.e(LOG_TAG, "Parsed json is null");
            }


        }
    }

}

