package com.ludovicsvetlana.xlair;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by ludovicbonivert on 18/12/15.
 */
public class RadioFragment extends Fragment implements MediaController.MediaPlayerControl{

    private RadioService radioService;
    private Intent playIntent;
    private boolean musicBound = false;
    private RadioController radioController;
    MediaPlayer mPlayer;
    Button playButton;
    boolean playerIsPlaying = false;
    public String URLToStream = "http://streaming.ritcs.be:8000/.mp3";


    private void setRadioController(){
            radioController = new RadioController(getContext());
            radioController.setPrevNextListeners(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //playNext();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //playPrev();
                }
            });

        radioController.setMediaPlayer(this);
        radioController.setAnchorView(getView().findViewById(R.id.playbtn));
        radioController.setEnabled(true);

    }

    // What does the fragment look like
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio, container, false);

        playerIsPlaying = false;

        playButton = (Button) view.findViewById(R.id.playbtn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do we need to play or pause the player ?
                if (playerIsPlaying == false) {
                    createAndPlayMusicPlayer();
                    playButton.setText("Pause");
                    playerIsPlaying = true;
                } else {
                    mPlayer.stop();
                    playButton.setText("Play");
                    playerIsPlaying = false;
                }
            }
        });
        setRadioController();
        return view;
    }


    // Connect to the service
    private ServiceConnection radioConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RadioService.RadioBinder binder = (RadioService.RadioBinder) service;
            radioService = binder.getService();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    protected void createAndPlayMusicPlayer(){
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mPlayer.setDataSource(URLToStream);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getActivity().getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getActivity().getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getActivity().getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepareAsync();
        }catch (IllegalArgumentException e){
            Toast.makeText(getActivity().getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        catch (IllegalStateException e) {
            Toast.makeText(getActivity().getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mPlayer.start();

        if(playIntent == null){
            playIntent = new Intent(this, RadioService.class);
            bindService()
        }


    }

}
