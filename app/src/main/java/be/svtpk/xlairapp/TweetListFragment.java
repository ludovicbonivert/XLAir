package be.svtpk.xlairapp;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

/**
 * Created by ludovicbonivert on 21/01/16.
 */
public class TweetListFragment extends ListFragment{

    private static final String SEARCH_QUERY = "#xlair OR xlair";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_twitter_timeline, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SearchTimeline searchTimeline = new SearchTimeline.Builder().query(SEARCH_QUERY).build();
        // GETCONTEXT REPLACED HERE
        final TweetTimelineListAdapter tweetAdapter = new TweetTimelineListAdapter.Builder(getActivity()).setTimeline(searchTimeline).build();
        setListAdapter(tweetAdapter);

    }

}
