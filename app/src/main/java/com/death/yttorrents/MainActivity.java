package com.death.yttorrents;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private static final String endpoint = "https://yts.ag/api/v2/list_movies.json";
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("YTtorrents");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fetchImages();
    }

    private void fetchImages() {

        pDialog.setMessage("Downloading json...");
        pDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(endpoint, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pDialog.hide();
                        try {
                            JSONObject object = response.getJSONObject("data");
                            JSONArray array = object.getJSONArray("movies");
                            for(int i=0;i<array.length();i++)
                            {
                                JSONObject object2 = array.getJSONObject(i);
                                Image image = new Image();
                                Log.e("ERROR",object2.getString("title"));
                                image.setName(object2.getString("title"));
                                Log.e("ERROR2", object2.getString("date_uploaded"));
                                Log.e("ERROR3", object2.getString("small_cover_image"));
                                Log.e("ERROR4", object2.getString("medium_cover_image"));
                                Log.e("ERROR5", object2.getString("large_cover_image"));
                                Log.e("ERROR6", object2.getString("rating"));
                                image.setSmall(object2.getString("small_cover_image"));
                                image.setMedium(object2.getString("medium_cover_image"));
                                image.setLarge(object2.getString("large_cover_image"));
                                image.setTimestamp(object2.getString("date_uploaded"));
                                image.setRating(object2.getString("rating"));
                                JSONArray array3 = object2.getJSONArray("torrents");
                                JSONObject jsonObject = array3.getJSONObject(0);
                                Log.e("URL:",jsonObject.getString("url"));
                                image.setURL(jsonObject.getString("url"));
                                images.add(image);
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

//        JsonArrayRequest req = new JsonArrayRequest(endpoint,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d(TAG, response.toString());
//                        pDialog.hide();
//
//                        images.clear();
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                Log.e("LENGTH",""+response.length());
//                                JSONObject object = response.getJSONObject(i);
//                                Image image = new Image();
//                                image.setName(object.getString("name"));
//                                JSONObject url = object.getJSONObject("url");
//                                image.setSmall(url.getString("small"));
//                                image.setMedium(url.getString("medium"));
//                                image.setLarge(url.getString("large"));
//                                image.setTimestamp(object.getString("timestamp"));
//
//                                images.add(image);
//
//                            } catch (JSONException e) {
//                                Log.e(TAG, "Json parsing error: " + e.getMessage());
//                            }
//                        }
//
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
//                pDialog.hide();
//            }
//        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request);

    }
}