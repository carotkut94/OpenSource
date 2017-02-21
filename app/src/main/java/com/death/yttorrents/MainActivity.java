package com.death.yttorrents;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    private static final String endpoint = "https://yts.ag/api/v2/list_movies.json?limit=50";
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    static  int count = 0;
    String query;
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
        fetchImages(endpoint);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.seedr)
        {
            count+=1;
            Toast.makeText(MainActivity.this, "Sorting by minimum rating 8 on page "+count, Toast.LENGTH_SHORT).show();
            String url = "https://yts.ag/api/v2/list_movies.json?minimum_rating=8&limit=50&page="+count;
            fetchImages(url);
        }
        if(item.getItemId()==R.id.search)
        {

            LayoutInflater li = LayoutInflater.from(MainActivity.this);
            View dialogView = li.inflate(R.layout.custom_query, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    MainActivity.this);
            // set title
            alertDialogBuilder.setTitle("Search Movie");
            // set custom dialog icon
            alertDialogBuilder.setIcon(R.drawable.ic_icon);
            // set custom_dialog.xml to alertdialog builder
            alertDialogBuilder.setView(dialogView);
            final EditText userInput = (EditText) dialogView
                    .findViewById(R.id.et_input);
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    query = userInput.getText().toString();
                                    String dataUrl = "https://yts.ag/api/v2/list_movies.json?query_term="+query+"&limit=30";
                                    fetchImages(dataUrl);
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchImages(String url) {
        pDialog.setMessage("Downloading json...");
        pDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        images.clear();
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
                            pDialog.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter.notifyDataSetChanged();
                        pDialog.hide();
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