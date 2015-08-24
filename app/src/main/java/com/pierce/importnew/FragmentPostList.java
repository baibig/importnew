package com.pierce.importnew;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pierce.importnew.adapters.PostListRecyclerViewAdapter;
import com.pierce.importnew.models.Event;
import com.pierce.importnew.models.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


public class FragmentPostList extends Fragment implements InterFaceLoad,
SwipeRefreshLayout.OnRefreshListener{


    private final String TAG="fragment_post_list";
    @Bind(R.id.swiprefresh_post_list)
    protected SwipeRefreshLayout mSwipRefreshLayout;
    @Bind(R.id.recyclerview_post_list)
    protected RecyclerView mRecyclerView;
    protected LoadTask mLoadTask;

    protected List<Post> mData=new ArrayList<>();

    RecyclerView.LayoutManager manager;

    PostListRecyclerViewAdapter adapter;

    SQLiteDatabase db;



    public FragmentPostList() {
        // Required empty public constructor

    }

    @Override
    public void onStop() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
        db= Connector.getDatabase();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_post_list, container, false);
        ButterKnife.bind(this, view);

        manager=new LinearLayoutManager(getActivity());
        adapter=new PostListRecyclerViewAdapter(getActivity(),R.layout.item_fragment_post_list,mData);
        adapter.setOnItemClickedListener(new PostListRecyclerViewAdapter.OnRecyclerViewItemClickedListener() {
            @Override
            public void onRecyclerViewItemClicked(int position, Object o) {
                Post post= (Post) o;
                showInfo(post.getTitle());
                Intent intent=new Intent(getActivity(),ArticleActivity.class);
                EventBus.getDefault().postSticky(new Event.PostListEvent(mData, position));
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(manager);
        mSwipRefreshLayout.setOnRefreshListener(this);

        return view;
    }
    public void showInfo(String s){
        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
    }
    public void onEvent(int i){
        Log.i(TAG, i + "");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadTask=new LoadTask();
        mLoadTask.setLoadListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void initLoadDone(Object object) {
        Gson gson=new Gson();
        JSONArray jsonArray= null;
        try {
            jsonArray = ((JSONObject)object).getJSONArray("posts");
            String s=jsonArray.toString();
            Log.i(TAG,s);
            mData=gson.fromJson(s, new TypeToken<ArrayList<Post>>() {
            }.getType());
            DataSupport.saveAll(mData);
            adapter.myNotify(mData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshLoadDone(Object object)  {
        Gson gson=new Gson();
        JSONArray jsonArray= null;
        try {
            jsonArray = ((JSONObject)object).getJSONArray("posts");
            String s=jsonArray.toString();
            List<Post> list=gson.fromJson(s, new TypeToken<ArrayList<Post>>() {
            }.getType());
            mData.addAll(0, list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSwipRefreshLayout.setRefreshing(false);

    }

    @Override
    public void moreLoadDone(Object object)  {
        Gson gson=new Gson();
        JSONArray jsonArray= null;
        try {
            jsonArray = ((JSONObject)object).getJSONArray("posts");
            String s=jsonArray.toString();
            mData=gson.fromJson(s, new TypeToken<ArrayList<Post>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        mLoadTask.refreshLoad(mData.get(0));
    }
}
