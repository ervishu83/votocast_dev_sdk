package utils;

import android.net.Uri;

public class ThumbnailConfig {
    private Uri uri;
    private int width;
    private int height;

    public ThumbnailConfig(Uri uri, int width, int height) {
        this.uri = uri;
        this.width = width;
        this.height = height;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
