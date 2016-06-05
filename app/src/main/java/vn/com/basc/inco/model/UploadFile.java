package vn.com.basc.inco.model;

import android.net.Uri;

/**
 * Created by SONY on 6/5/2016.
 */
public class UploadFile {
    Uri uri;
    String fileName;
    String file_size;
    String info;
    String id;
    int progress;
    int status;
    int index;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getUri() {
        return uri;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFile_size() {
        return file_size;
    }

    public String getInfo() {
        return info;
    }

    public String getId() {
        return id;
    }
}
