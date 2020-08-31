package com.inventrax_pepsi.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.inventrax_pepsi.fragments.FragmentEmojiSliderOne;
import com.inventrax_pepsi.fragments.FragmentEmojiSliderThree;
import com.inventrax_pepsi.fragments.FragmentEmojiSliderTwo;
import com.inventrax_pepsi.fragments.FragmentMTNDewSlider;
import com.inventrax_pepsi.fragments.FragmentNimboozSlider;
import com.inventrax_pepsi.fragments.FragmentPepsiSlider;
import com.inventrax_pepsi.fragments.FragmentSliceSlider;

/**
 * Author   : Naresh P.
 * Date		: 11/04/2016 11:03 AM
 * Purpose	: Image View Pager Adapter
 */



public class ImageViewPagerAdapter extends FragmentPagerAdapter {
    public static int totalPage = 7;
    private Context _context;

    public ImageViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        _context = context;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();
        switch (position) {
            case 0:
                f = new FragmentEmojiSliderOne();
                break;
            case 1:
                f = new FragmentEmojiSliderTwo();
                break;
            case 2:
                f = new FragmentEmojiSliderThree();
                break;
            case 3:
                f = new FragmentPepsiSlider();
                break;
            case 4:
                f = new FragmentNimboozSlider();
                break;
            case 5:
                f = new FragmentMTNDewSlider();
                break;
            case 6:
                f = new FragmentSliceSlider();
                break;


        }
        return f;
    }

    @Override
    public int getCount() {
        return totalPage;
    }

}
