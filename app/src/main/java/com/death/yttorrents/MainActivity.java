package com.death.yttorrents;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

    private static final String endpoint = "https://yts.ag/api/v2/list_movies.json?limit=50";
    RecyclerView.LayoutManager mLayoutManager;
    int count = 0;
    String query;
    private ArrayList<Movie> movies;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private TextView errorView;
    private RecyclerView recyclerView;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        haveStoragePermission();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("YTtorrents");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        errorView = (TextView) findViewById(R.id.error);
        pDialog = new ProgressDialog(this);
        movies = new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), movies);

        mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        /**
         * Click event handler on recycler view.
         */
        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("movies", movies);
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
        fetchMovies(endpoint);
    }

    /**
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
            recyclerView.setLayoutManager(mLayoutManager);
        } else {
            mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Check for write permissions
     *
     * @return
     */
    public boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error", "You have permission");
                return true;
            } else {
                Log.e("Permission error", "You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error", "You already have the permission");
            return true;
        }
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.seedr) {
            count += 1;
            Toast.makeText(MainActivity.this, "Sorting by minimum rating 8 on page " + count, Toast.LENGTH_SHORT).show();
            String url = "https://yts.ag/api/v2/list_movies.json?minimum_rating=8&limit=50&page=" + count;
            fetchMovies(url);
        }
        if (item.getItemId() == R.id.search) {
            count = 0;
            LayoutInflater li = LayoutInflater.from(MainActivity.this);
            View dialogView = li.inflate(R.layout.custom_query, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    MainActivity.this, R.style.MyDialogTheme);
            alertDialogBuilder.setTitle(Html.fromHtml("<font color='#ffffff'>Search Movie</font>"));
            alertDialogBuilder.setIcon(R.drawable.ic_icon);
            alertDialogBuilder.setView(dialogView);
            final EditText userInput = (EditText) dialogView
                    .findViewById(R.id.et_input);
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Search",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    query = userInput.getText().toString();
                                    String dataUrl = "https://yts.ag/api/v2/list_movies.json?query_term=" + query + "&limit=30";
                                    fetchMovies(dataUrl);
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        if(item.getItemId()==R.id.mwiki)
        {
            startActivity(new Intent(MainActivity.this, MediaContainer.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method for gathering the json data from API
     *
     * @param url
     */
    private void fetchMovies(String url) {
        pDialog.setMessage("Downloading movie list...");
        pDialog.show();
        errorView.setVisibility(View.INVISIBLE);
        JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        movies.clear();
                        pDialog.hide();
                        errorView.setVisibility(View.INVISIBLE);
                        try {
                            JSONObject object = response.getJSONObject("data");
                            JSONArray array = object.getJSONArray("movies");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object2 = array.getJSONObject(i);
                                Movie movie = new Movie();
                                movie.setName(object2.getString("title"));
                                movie.setSmall(object2.getString("small_cover_image"));
                                movie.setMedium(object2.getString("medium_cover_image"));
                                movie.setLarge(object2.getString("large_cover_image"));
                                movie.setTimestamp(object2.getString("date_uploaded"));
                                movie.setRating(object2.getString("rating"));
                                movie.setSumary(object2.getString("synopsis"));
                                JSONArray array3 = object2.getJSONArray("torrents");
                                JSONObject jsonObject = array3.getJSONObject(0);
                                movie.setURL(jsonObject.getString("url"));
                                movies.add(movie);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            errorView.setVisibility(View.VISIBLE);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                errorView.setVisibility(View.VISIBLE);
                pDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }
}