package net.free.supertool.useful.scancode.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class QRCodeBean implements Serializable {
    private byte[] code_image;
    private byte[] code_icon;
    private String code_content;
    private String code_format;
    private String code_time;
    private String code_type;
    private int id;

    public QRCodeBean(byte[] code_image, byte[] code_icon, String code_content, String code_format, String code_time, String code_type, int id) {
        this.code_image = code_image;
        this.code_icon = code_icon;
        this.code_content = code_content;
        this.code_format = code_format;
        this.code_time = code_time;
        this.code_type = code_type;
        this.id = id;
    }

    public QRCodeBean(byte[] code_image, byte[] code_icon, String code_content, String code_format, String code_time, String code_type) {
        this.code_image = code_image;
        this.code_icon = code_icon;
        this.code_content = code_content;
        this.code_format = code_format;
        this.code_time = code_time;
        this.code_type = code_type;
    }

    public byte[] getCode_image() {
        return code_image;
    }

    public void setCode_image(byte[] code_image) {
        this.code_image = code_image;
    }

    public byte[] getCode_icon() {
        return code_icon;
    }

    public void setCode_icon(byte[] code_icon) {
        this.code_icon = code_icon;
    }

    public String getCode_content() {
        return code_content;
    }

    public void setCode_content(String code_content) {
        this.code_content = code_content;
    }

    public String getCode_format() {
        return code_format;
    }

    public void setCode_format(String code_format) {
        this.code_format = code_format;
    }

    public String getCode_time() {
        return code_time;
    }

    public void setCode_time(String code_time) {
        this.code_time = code_time;
    }

    public String getCode_type() {
        return code_type;
    }

    public void setCode_type(String code_type) {
        this.code_type = code_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public QRCodeBean() {
    }

   /* @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code_image);
    }*/
}
