package com.death.yttorrents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SearchActivity extends AppCompatActivity {

    String hitUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activty);
        String category = getIntent().getStringExtra("Category");
        String query = getIntent().getStringExtra("Query");

        if(category == "TV")
        {
            hitUrl = "https://api.themoviedb.org/3/search/tv?api_key=56196b3d62369c56461e48dcf3652bf0&language=en-US&query="+query+"&page=1";
        }else{

            hitUrl = "https://api.themoviedb.org/3/search/movie?api_key=56196b3d62369c56461e48dcf3652bf0&language=en-US&query="+query+"&page=1&include_adult=true";
        }

    }
}
