package com.pierce.importnew.models;

import java.util.List;

/**
 * Author: pierce
 * Date: 2015/8/22
 */
public class Event {
    public static class PostListEvent{
        List<Post> list;
        int position;

        public int getPosition() {
            return position;
        }

        public PostListEvent(List<Post> list,int position){
            this.list=list;
            this.position=position;

        }

        public List<Post> getList() {
            return list;
        }
    }
}
