package com.micro.microvideo.main.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by William on 2018/6/9.
 */

public class VideoBean implements Parcelable {
    private String name;
    private String cname;
    private String quality;
    private String remark;
    private String imgurl;
    private String videourl;

    private List<String> imgs;

    protected VideoBean(Parcel in) {
        name = in.readString();
        cname = in.readString();
        quality = in.readString();
        remark = in.readString();
        imgurl = in.readString();
        videourl = in.readString();
        imgs = in.createStringArrayList();
    }

    public static final Creator<VideoBean> CREATOR = new Creator<VideoBean>() {
        @Override
        public VideoBean createFromParcel(Parcel in) {
            return new VideoBean(in);
        }

        @Override
        public VideoBean[] newArray(int size) {
            return new VideoBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(cname);
        parcel.writeString(quality);
        parcel.writeString(remark);
        parcel.writeString(imgurl);
        parcel.writeString(videourl);
        parcel.writeStringList(imgs);
    }
}
