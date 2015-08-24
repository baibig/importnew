package com.pierce.importnew;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.pierce.importnew.models.Post;

import java.util.List;

/**
 * Author: pierce
 * Date: 2015/8/20
 */
public class OtherFragment extends FragmentPostList {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadTask.setUrl("Others");

        mLoadTask.initialLoad();
    }
}
