package com.pierce.importnew;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pierce.importnew.app.AppController;
import com.pierce.importnew.models.Post;

import org.json.JSONObject;

/**
 * Author: pierce
 * Date: 2015/8/20
 */
public class LoadTask {
    private final String TAG="loadtask";

    public enum Action {INITIAL,REFRESH,MORE};

    private String url;
    private String action;
    private InterFaceLoad loadDone;
    private OnArticleFetchedListener articleFetchedListener;
    public LoadTask(){

    }

    public void setUrl(String action){
        this.action=action;
        switch (action){
            case "Python":
                url=Config.PythonURL;
                break;
            case "Java":
                url=Config.JavaURL;
                break;
            case "Others":
                url=Config.OtherURL;
                break;
            default:
                url=Config.ITEMS_URL;
        }
    }
    public void initialLoad(){
        query(url,Action.INITIAL);
    }
    public void moreLoad(int currentPage){
        String url = this.url + "?l=" + ((currentPage - 1) * 20);
        query(url,Action.MORE);
    }

    public void refreshLoad(Post post){
        if (post!=null){
            String url = this.url+ "?f_id=" + post.getId();
            query(url, Action.REFRESH);
        }
    }

    public void query(String url, final Action action){
        JsonObjectRequest request=new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        switch (action){
                            case INITIAL:
                                loadDone.initLoadDone(response);
                                break;
                            case REFRESH:
                                loadDone.refreshLoadDone(response);
                                break;
                            case MORE:
                                loadDone.moreLoadDone(response);
                                break;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        AppController.getInstance().addToRequest(request);
    }
    public void fetchArticle(Post post){
        String s=Config.ITEMS_URL+"/"+post.getId();
        JsonObjectRequest request=new JsonObjectRequest(s, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        articleFetchedListener.OnArticleFetched(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        AppController.getInstance().addToRequest(request);
    }
    public void setLoadListener(InterFaceLoad loadDone){
        this.loadDone= loadDone;
    }
    public interface OnArticleFetchedListener{
        public void OnArticleFetched(Object o);
    }
    public void setOnArticleFetchedListener(OnArticleFetchedListener articleFetched){
        this.articleFetchedListener=articleFetched;
    }
}
