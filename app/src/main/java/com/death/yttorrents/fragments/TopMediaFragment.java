package com.death.yttorrents.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.death.yttorrents.R;
import com.death.yttorrents.model.MediaSkeleton;

import java.util.ArrayList;


public class TopMediaFragment extends DialogFragment {
    Button download;
    Boolean setFitCenter;
    String URLString = "https://image.tmdb.org/t/p/w";
    private ArrayList<MediaSkeleton> mediaSkeletons;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate, lbRating, lblOverview;
     /**
     * Viewpager change listener
     */
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private int selectedPosition = 0;

    static TopMediaFragment newInstance() {
        TopMediaFragment f = new TopMediaFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_media_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) v.findViewById(R.id.title);
        lblDate = (TextView) v.findViewById(R.id.date);
        lbRating = (TextView) v.findViewById(R.id.rating);
        download = (Button) v.findViewById(R.id.download);
        lblOverview = (TextView) v.findViewById(R.id.textOverView);

        mediaSkeletons = (ArrayList<MediaSkeleton>) getArguments().getSerializable("media");
        selectedPosition = getArguments().getInt("position");

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setFitCenter = true;
        } else {
            setFitCenter = false;
        }

        return v;
    }
    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }
    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + mediaSkeletons.size());
        MediaSkeleton media = mediaSkeletons.get(position);
        lblTitle.setText(media.getTitle());
        lbRating.setText(media.getVote_average());
        lblOverview.setText(media.getOverview());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        setHasOptionsMenu(true);
    }

    /**
     * Pager Adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.media_full_screen_details, container, false);


            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            final MediaSkeleton media = mediaSkeletons.get(position);

            Log.e("IAMGE LINK CREATED", URLString + "1920" + media.getPoster_Path());
            Glide.with(getActivity()).load(URLString + "1920" + media.getPoster_Path())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);
            Log.e("LAYOUT", "Landscape");
            container.addView(view);

            return view;
        }


        @Override
        public int getCount() {
            return mediaSkeletons.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
