package db;

/**
 * Created by Admin on 5/26/2017.
 */

public class SearchCampaign {

    private String id;

    private String title;

    private String photo;

    private String photoPath;

    private String isFeatured;

    private String status;

    private String videos;

    private String users;

    private String followers;

    private String didfollowed;

    private String allowUpload;

    public SearchCampaign(String id, String title, String photo, String photoPath, String isFeatured, String status, String videos, String users, String followers, String didfollowed, String allowUpload) {
        this.id = id;
        this.title = title;
        this.photo = photo;
        this.photoPath = photoPath;
        this.isFeatured = isFeatured;
        this.status = status;
        this.videos = videos;
        this.users = users;
        this.followers = followers;
        this.didfollowed = didfollowed;
        this.allowUpload = allowUpload;
    }

    public String getAllowUpload() {
        return allowUpload;
    }

    public void setAllowUpload(String allowUpload) {
        this.allowUpload = allowUpload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(String isFeatured) {
        this.isFeatured = isFeatured;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getDidfollowed() {
        return didfollowed;
    }

    public void setDidfollowed(String didfollowed) {
        this.didfollowed = didfollowed;
    }

    @Override
    public String toString() {
        return "SearchCampaign{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", photo='" + photo + '\'' +
                ", photoPath='" + photoPath + '\'' +
                ", isFeatured='" + isFeatured + '\'' +
                ", status='" + status + '\'' +
                ", videos='" + videos + '\'' +
                ", users='" + users + '\'' +
                ", followers='" + followers + '\'' +
                ", didfollowed='" + didfollowed + '\'' +
                ", allowUpload='" + allowUpload + '\'' +
                '}';
    }
}
