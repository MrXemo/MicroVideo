package com.micro.microvideo.main.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by William on 2018/6/2.
 */

public class MicroBean implements Parcelable {
    private String id;
    private String imgurl;
    private String name;

    public MicroBean() {
    }

    public MicroBean(String url, String name) {
        this.imgurl = url;
        this.name = name;
    }

    protected MicroBean(Parcel in) {
        id = in.readString();
        imgurl = in.readString();
        name = in.readString();
    }

    public static final Creator<MicroBean> CREATOR = new Creator<MicroBean>() {
        @Override
        public MicroBean createFromParcel(Parcel in) {
            return new MicroBean(in);
        }

        @Override
        public MicroBean[] newArray(int size) {
            return new MicroBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imgurl);
        dest.writeString(name);
    }
}
