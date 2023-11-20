package net.free.supertool.useful.scancode.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class CreateBean implements Serializable {
    private byte[] bitmap;
    private String content;

    public CreateBean(byte[] bitmap, String content) {
        this.bitmap = bitmap;
        this.content = content;
    }

    public byte[] getBitmap() {
        return bitmap;
    }

    public void setBitmap(byte[] bitmap) {
        this.bitmap = bitmap;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
