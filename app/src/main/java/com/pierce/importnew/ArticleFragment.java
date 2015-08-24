package com.pierce.importnew;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pierce.importnew.models.Event;
import com.pierce.importnew.models.Post;
import com.tt.whorlviewlibrary.WhorlView;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends Fragment implements LoadTask.OnArticleFetchedListener{

    private String TAG="articlefragment";

    @Bind(R.id.appbar_article)
    AppBarLayout mAppBar;
    @Bind(R.id.toolbar_article)
    Toolbar mToolBar;
    @Bind(R.id.img_acticle)
    ImageView mImageView;
    @Bind(R.id.floatbutton_share)
    FloatingActionButton mFAB;
    @Bind(R.id.collapsing_toolbar_article)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.webview)
    WebView mWebView;
    @Bind(R.id.whorl2)
    WhorlView mDialog;

    Post mData;
    int position;
    LoadTask mLoadTask=new LoadTask();



    public ArticleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position=getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_article, container, false);
        ButterKnife.bind(this, v);
        //mDialog=new ProgressDialog(getActivity());
        //mDialog=new WhorlView(getActivity());

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolBar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        WebSettings webSettings=mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        WebViewClient webclient=new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //mDialog.show();
                mDialog.setVisibility(View.VISIBLE);
                mDialog.start();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //mDialog.dismiss();
                mDialog.stop();
                mDialog.setVisibility(View.GONE);
            }
        };
        mWebView.setWebViewClient(webclient);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_TITLE, mData.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, mData.getContent());
                startActivity(Intent.createChooser(intent, "选择分享的应用"));
            }
        });

        Random random=new Random();
        int n=random.nextInt(6);
        try {
            Field f=R.mipmap.class.getField("bg_img_"+n);
            int src=f.getInt(new R.mipmap());
            mImageView.setImageResource(src);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadTask.setOnArticleFetchedListener(this);
        Log.i(TAG,"new fragment : "+position);
    }

    public void onEvent(Event.PostListEvent event){
        mData=event.getList().get(position);
        Post post= DataSupport.find(Post.class,mData.getId());
        if (TextUtils.isEmpty(post.getContent()))
            mLoadTask.fetchArticle(mData);
        else {
            setWebView(post);
            Log.i(TAG, post.getId()+" from database");
        }
    }

    @Override
    public void OnArticleFetched(Object o) {
        Gson gson=new Gson();
        try {
            JSONObject obj=((JSONObject)o).getJSONObject("post");
            mData=gson.fromJson(obj.toString(), Post.class);
            ContentValues values=new ContentValues();
            values.put("content",mData.getContent());
            DataSupport.update(Post.class, values, mData.getId());
            setWebView(mData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setWebView(Post post){
        StringBuilder builder = new StringBuilder("<html>");
        builder.append("<head>");
        builder.append("<link rel=stylesheet href='css/style.css'>");
        builder.append("</head>");
        builder.append(post.getContent());
        builder.append("</html>");
        mCollapsingToolbarLayout.setTitle(post.getTitle());
        mWebView.loadDataWithBaseURL("", builder.toString(), "text/html", "UTF-8", "");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
