package be.svtpk.xlairapp;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProgrammeListFragment.OnFragmentInteractionListener,
        ProgrammeFragment.OnFragmentInteractionListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "HvLUDxv9TTrmGPcc0Ak9TLOUg";
    private static final String TWITTER_SECRET = "7TQnTAABlQB6wi8ABcvkCkXwI6axIyRCWfW0sjDZouCZvY4GGo";

    public static final String LOG_TAG = "XLAIR";

    private ActionBarDrawerToggle toggle;

    private String mTitle;

    MediaPlayer mPlayer;
    Button playButton;
    boolean playerIsPlaying = false;
    public String URLToStream = "http://streaming.ritcs.be:8000/.mp3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupTwitterFabric();
        setupToolbar();
        findAndSetupRadioPlayer();

        //http://jsonplaceholder.typicode.com/posts
        new HTTPRequestAndGetJsonTask().execute("http://www.xlair.be/scheme/data");
    }

    private void findAndSetupRadioPlayer() {

        playerIsPlaying = false;

        playButton = (Button) findViewById(R.id.playbtn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playerIsPlaying) {
                    createAndPlayMusicPlayer();
                    playButton.setText(R.string.action_pause);
                    playerIsPlaying = true;
                } else {
                    mPlayer.stop();
                    playButton.setText(R.string.action_play);
                    playerIsPlaying = false;
                }
            }
        });

    }

    private void createAndPlayMusicPlayer() {

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


    private void setupTwitterFabric() {
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
    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void createTweet(){
        // Intent is going to open browser if no twitter app is found on phone
        String url = "http://www.twitter.com/intent/tweet?hashtags=XLAIR";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = new LiveFragment();

        if (id == R.id.nav_live) {
            fragment = new LiveFragment();

        } else if (id == R.id.nav_programmas) {
            fragment = new ProgrammeListFragment();
        } else if (id == R.id.nav_events) {
            fragment = new EventsFragment();
        } else if (id == R.id.nav_contact) {
            fragment = new ContactFragment();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.menu_item_live);
                break;
            case 2:
                mTitle = getString(R.string.menu_item_programmas);
                break;
            case 3:
                mTitle = getString(R.string.menu_item_events);
                break;
            case 4:
                mTitle = getString(R.string.menu_item_contact);
                break;
        }
        setTitle();
    }

    private void setTitle() {
        getSupportActionBar().setTitle(mTitle);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onProgrammeSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.content_main, new ProgrammeFragment());
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }


    public class HTTPRequestAndGetJsonTask extends AsyncTask<String, String, JSONArray> {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        @Override
        protected JSONArray doInBackground(String... params) {
            try{
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
                EditText e = (EditText) findViewById(R.id.current_broadcast);
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
