package db;

/**
 * Created by Anil on 5/2/2016.
 */
public class UploadVideo {

    Integer imageId;
    String filename;
    String duration;

    public UploadVideo(Integer imageId, String filename, String duration) {
        this.imageId = imageId;
        this.filename = filename;
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
