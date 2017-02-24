package com.death.yttorrents;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Media_Info extends AppCompatActivity {

    // URL FOR IMAGE https://image.tmdb.org/t/p/w320/cGOPbv9wA5gEejkUN892JrveARt.jpg
    // My API KEY 56196b3d62369c56461e48dcf3652bf0
    // TOP RATED URL https://api.themoviedb.org/3/movie/top_rated?api_key=56196b3d62369c56461e48dcf3652bf0&language=en-US&page=1

    private static final String endpoint = "https://api.themoviedb.org/3/movie/top_rated?api_key=56196b3d62369c56461e48dcf3652bf0&language=en-US&page=1";
    RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MediaSkeleton> mediaSkeletons;
    private ProgressDialog pDialog;
    private MediaAdapter mAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media__info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("YTtorrents");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        pDialog = new ProgressDialog(this);
        mediaSkeletons  = new ArrayList<>();
        mAdapter = new MediaAdapter(getApplicationContext(), mediaSkeletons);
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("media", mediaSkeletons);
                bundle.putInt("position", position);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                TopMediaFragment newFragment = TopMediaFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "topMedia");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        fetchMedia(endpoint);
    }

    private void fetchMedia(String url) {
        JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mediaSkeletons.clear();
                        pDialog.hide();
                        try {
                            /*
                            "release_date": "2016-08-26",
                            "genre_ids": [
                              16,
                              18,
                              14,
                              10749
                            ],
                            "id": 372058,
                            "original_title": "君の名は。",
                            "original_language": "ja",
                            "title": "Your Name.",
                            "backdrop_path": "/6vkhRvsRvWpmaRVyCXaxTkIEb7j.jpg",
                            "popularity": 7.996468,
                            "vote_count": 227,
                            "video": false,
                            "vote_average": 8.4

                             */
                            JSONArray array = response.getJSONArray("results");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object2 = array.getJSONObject(i);
                                MediaSkeleton mediaSkeleton = new MediaSkeleton();
                                mediaSkeleton.setPoster_Path(object2.getString("poster_path"));
                                mediaSkeleton.setAdult(object2.getString("adult"));
                                mediaSkeleton.setOverview(object2.getString("overview"));
                                mediaSkeleton.setRelease_date(object2.getString("release_date"));
                                mediaSkeleton.setId(object2.getString("id"));
                                mediaSkeleton.setTitle(object2.getString("title"));
                                mediaSkeleton.setBackdrop_path(object2.getString("backdrop_path"));
                                mediaSkeleton.setPopularity(object2.getString("popularity"));
                                mediaSkeleton.setVote_count(object2.getString("vote_count"));
                                mediaSkeleton.setVote_average(object2.getString("vote_average"));
                                mediaSkeletons.add(mediaSkeleton);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                pDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }
}
