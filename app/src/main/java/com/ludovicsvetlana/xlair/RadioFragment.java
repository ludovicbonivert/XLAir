package com.ludovicsvetlana.xlair;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by ludovicbonivert on 18/12/15.
 */
public class RadioFragment extends Fragment{

    MediaPlayer mPlayer;
    Button playButton;
    boolean playerIsPlaying = false;
    public String URLToStream = "http://streaming.ritcs.be:8000/.mp3";

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
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
    }

}
