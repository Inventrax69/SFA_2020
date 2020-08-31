package com.inventrax_pepsi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inventrax_pepsi.R;

/**
 * Created by android on 4/11/2016.
 */
public class FragmentEmojiSliderTwo extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_emoji_two_slider,container,false);

      /*  Picasso.with(getContext())
                .load(ServiceURLConstants.DASHBOARD_SLIDER_IMAGE_URL + "2.png")
                .into((ImageView)rootView.findViewById(R.id.imgSlider));*/

        return rootView;
    }
}
