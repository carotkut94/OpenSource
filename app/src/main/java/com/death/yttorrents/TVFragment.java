package com.death.yttorrents;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rajora_sd on 2/24/2017.
 */

public class TVFragment extends Fragment {

    private static final String endpoint = "https://api.themoviedb.org/3/tv/top_rated?api_key=56196b3d62369c56461e48dcf3652bf0&language=en-US&page=1";
    RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<TVSkeleton> tvSkeletons;
    private ProgressDialog pDialog;
    private TVAdapter mAdapter;
    RecyclerView recyclerView;
    public TVFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("IN", "TV VIEW");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        pDialog = new ProgressDialog(getActivity());
        tvSkeletons  = new ArrayList<>();
        mAdapter = new TVAdapter(getActivity(),tvSkeletons);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getActivity(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("tv", tvSkeletons);
                bundle.putInt("position", position);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                TVMediaFragment newFragment = TVMediaFragment.newInstance();
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
                        tvSkeletons.clear();
                        pDialog.hide();
                        try {
                            JSONArray array = response.getJSONArray("results");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object2 = array.getJSONObject(i);
                                TVSkeleton tvS = new TVSkeleton();
                                tvS.setPoster_Path(object2.getString("poster_path"));
                                tvS.setOverview(object2.getString("overview"));
                                tvS.setId(object2.getString("id"));
                                tvS.setFirst_air_date(object2.getString("first_air_date"));
                                tvS.setTitle(object2.getString("name"));
                                tvS.setPopularity(object2.getString("popularity"));
                                tvS.setVote_average(object2.getString("vote_average"));
                                tvSkeletons.add(tvS);
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
