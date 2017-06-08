package com.example.flickrviewer;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.flickrviewer.utils.FlickrUtil;

/**
 * Created by amber on 6/7/2017.
 */

/*
The recycler view implementation essentially - shows how pictures should look in a grid basically
 */

public class FlickrPhotoGridAdapater extends RecyclerView.Adapter<FlickrPhotoGridAdapater.FlickrPhotoViewHolder> {
    private FlickrUtil.FlickrPhoto[] mPics;
    private OnPhotoItemClickListener mPicClickListener;

    public FlickrPhotoGridAdapater(OnPhotoItemClickListener photoItemClickListener){
        mPicClickListener = photoItemClickListener;
    }

    public void updatePhotos(FlickrUtil.FlickrPhoto[] photos){
        mPics = photos;
        notifyDataSetChanged();
    }

    @Override
    public FlickrPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.photo_grid_item, parent, false);
        return new FlickrPhotoViewHolder(item);
    }

    @Override
    public void onBindViewHolder(FlickrPhotoViewHolder hold, int position){
        hold.bind(mPics[position]);
    }

    @Override
    public int getItemCount(){
        if(mPics != null){
            return mPics.length;
        }
        else{
            return 0;
        }
    }

    public interface OnPhotoItemClickListener{
        void onPhotoItemClick(int photoId);
    }

    class FlickrPhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mPhoto;

        public FlickrPhotoViewHolder(View item){
            super(item);
            mPhoto = (ImageView)item.findViewById(R.id.iv_pic);
            item.setOnClickListener(this);
        }

        public void bind(FlickrUtil.FlickrPhoto p){
            Glide.with(mPhoto.getContext()).load(p.url_m)
                    .apply(RequestOptions.placeholderOf(new SizedColorDrawable(Color.WHITE, p.width_m, p.height_m)))
                    .into(mPhoto);
        }

        @Override
        public void onClick(View v){
            mPicClickListener.onPhotoItemClick(getAdapterPosition());
        }

    }

    class SizedColorDrawable extends ColorDrawable {
        int mW = -1;
        int mH = -1;

        public SizedColorDrawable(int color, int width, int height){
            super(color);
            mW = width;
            mH = height;
        }

        @Override
        public int getIntrinsicWidth(){
            return mW;
        }

        public int getIntrinsicHeight(){
            return mH;
        }
    }
}
