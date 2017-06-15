package com.example.flickrviewer;

import android.media.Image;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.flickrviewer.utils.FlickrUtil;

/**
 * Created by amber on 6/15/2017.
 */

public class FlickrPhotoPagerAdapter extends FragmentStatePagerAdapter {
    FlickrUtil.FlickrPhoto[] mPics;

    public FlickrPhotoPagerAdapter(FragmentManager fragMan){
        super(fragMan);
    }

    public void updatePics(FlickrUtil.FlickrPhoto[] p){
        mPics = p;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int i){
        Fragment f = new FlickrPhotoFragment();
        Bundle args = new Bundle();
        args.putString(FlickrPhotoFragment.ARGS_PHOTO_URL, mPics[i].url_l);
        f.setArguments(args);
        return f;
    }

    @Override
    public int getCount(){
        if(mPics != null){
            return mPics.length;
        }
        else{
            return 0;
        }
    }

    public static class FlickrPhotoFragment extends Fragment {
        public static final String ARGS_PHOTO_URL = "photoUrl";

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflate, @Nullable ViewGroup contain, @Nullable Bundle savedInstanceState){
            View rView = inflate.inflate(R.layout.photo_pager_item, contain, false);
            Bundle args = getArguments();
            ImageView photoIv = (ImageView)rView.findViewById(R.id.iv_photo);
            String photoUrl = args.getString(ARGS_PHOTO_URL);
            Glide.with(photoIv.getContext())
                    .load(photoUrl)
                    .into(photoIv);
            return rView;
        }
    }

}
