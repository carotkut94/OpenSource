package com.death.yttorrents.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.death.yttorrents.model.Movie;
import com.death.yttorrents.R;

import java.io.File;
import java.util.ArrayList;


public class SlideshowDialogFragment extends DialogFragment {
    Button download;
    Boolean setFitCenter;
    private ArrayList<Movie> movies;

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate, lbRating;
    //	page change listener
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

    /**
     * For making a new instance of fragment
     * @return
     */
    static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }


    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) v.findViewById(R.id.title);
        lblDate = (TextView) v.findViewById(R.id.date);
        lbRating = (TextView) v.findViewById(R.id.rating);
        download = (Button) v.findViewById(R.id.download);

        movies = (ArrayList<Movie>) getArguments().getSerializable("movies");
        selectedPosition = getArguments().getInt("position");

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setFitCenter = true;
        }
        else {
            setFitCenter = false;
        }

        return v;
    }

    /**
     * displaying current item from movie array
     * @param position
     */
    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    /**
     * Dispay the movie data on fragment
     * @param position
     */
    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + movies.size());
        Movie movie = movies.get(position);
        lblTitle.setText(movie.getName());
        final String url2 = movie.getURL();
        lblDate.setText(movie.getTimestamp());
        lbRating.setText(movie.getRating());
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = url2;
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDescription("Downloading torrent");
                request.setTitle(lblTitle.getText().toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                Uri downloadLocation = Uri.fromFile(new File(dir, lblTitle.getText().toString() + ".torrent"));
                //request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory(), "name-of-the-file.ext");
                request.setDestinationUri(downloadLocation);
                DownloadManager manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                Toast.makeText(getContext(), "Torrent Downloading", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    //	adapter

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
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            final Movie movie = movies.get(position);


            if(setFitCenter)
            {
                imageViewPreview.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(getActivity()).load(movie.getLarge())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageViewPreview);
                Log.e("LAYOUT","Landscape");

            }else
            {
                imageViewPreview.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(getActivity()).load(movie.getLarge())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageViewPreview);
            }

            imageViewPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
                    alertDialogBuilder.setTitle(Html.fromHtml("<font color='#ffffff'>"+movie.getName()+"</font>"));
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setMessage(Html.fromHtml("<font color='#ffffff'>"+movie.getSumary()+"</font>"));
                    alertDialogBuilder.setIcon(R.drawable.ic_icon);
                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCancelable(true);
                    alertDialog.show();
                }
            });

            container.addView(view);

            return view;
        }



        @Override
        public int getCount() {
            return movies.size();
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
