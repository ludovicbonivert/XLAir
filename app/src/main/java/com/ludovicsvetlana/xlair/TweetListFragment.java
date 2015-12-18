package com.ludovicsvetlana.xlair;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

/**
 * Created by ludovicbonivert on 18/12/15.
 */
public class TweetListFragment extends ListFragment {

    private static final String SEARCH_QUERY = "#xlair OR xlair";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timeline, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        SearchTimeline searchTimeline = new SearchTimeline.Builder().query(SEARCH_QUERY).build();
        final TweetTimelineListAdapter tweetAdapter = new TweetTimelineListAdapter.Builder(getContext())
                .setTimeline(searchTimeline).build();
        setListAdapter(tweetAdapter);

    }

}
