package com.pierce.importnew.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pierce.importnew.FragmentPostList;
import com.pierce.importnew.JavaFragment;
import com.pierce.importnew.OtherFragment;
import com.pierce.importnew.PythonFragment;
import com.pierce.importnew.models.Post;

import java.util.List;

/**
 * Author: pierce
 * Date: 2015/8/20
 */
public class FragmentPostListAdapter extends FragmentPagerAdapter {
    //private List<String> titles;
    private String[] titles={"Java","Python","Others"};

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public FragmentPostListAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new JavaFragment();
            case 1:
                return new PythonFragment();
            case 2:
                return new OtherFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
