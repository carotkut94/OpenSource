package com.death.yttorrents.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.death.yttorrents.model.Movie;
import com.death.yttorrents.R;

import java.util.List;

/**
 * Created by sidhantrajora on 07/07/16.
*/

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private List<Movie> movies;
    private Context mContext;

    /**
     * ViewHolder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView textViewTitle;
        public TextView textViewSummary;
        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    /**
     *
     * @param context
     * @param movies
     */
    public GalleryAdapter(Context context, List<Movie> movies) {
        mContext = context;
        this.movies = movies;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = movies.get(position);

        Glide.with(mContext).load(movie.getMedium())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    /**
     * OnItem touch listener implementation
     */
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private GalleryAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
