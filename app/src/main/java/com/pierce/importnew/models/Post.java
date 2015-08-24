package com.pierce.importnew.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: pierce
 * Date: 2015/8/20
 */
public class Post extends DataSupport implements Parcelable {
    static final DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    private  int id;
    private String url;
    private String title;
    private String description;
    private String cover;
    private String user;
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    private Date date;
    private String content;
    private String create_at;

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public Post(int id, String url, String title, String cover, String user, String description) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.cover = cover;
        this.user = user;
        this.description = description;
    }

    public Post(int id, String url, String title, String cover, String user, String date, String description, String content) {
        this(id, url, title, cover, user, description);
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            this.date = new Date();
            e.printStackTrace();
        }
        this.content = content;
    }

    public Post(int id, String url, String title, String cover, String user, Date date, String description) {
        this(id, url, title, cover, user, description);
        this.date = date;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public String getCover() {
        return cover;
    }

    public String getUser() {
        return user;
    }

    public Date getDate() {
        return this.date;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFormatDate() {
        return dateFormat.format(date);
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='"+id+'\''+
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", cover='" + cover + '\'' +
                ", user='" + user + '\'' +
                ", date=" + date +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.cover);
        dest.writeString(this.user);
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeString(this.content);
    }

    protected Post(Parcel in) {
        this.id = in.readInt();
        this.url = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.cover = in.readString();
        this.user = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.content = in.readString();
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
