package db;

import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;

/**
 * Created by Anil on 5/23/2016.
 */
public class Comment {

    private String commentId;
    private String vidId;
    private String userId;
    private String comment;
    private String username;
    private String userImage;
    private String date;
    private VideoPlayerManager<MetaData> mVideoPlayerManager;
    private String self_commented;

    public Comment(String commentId, String vidId, String userId, String comment, String username, String userImage, String date,String self_commented) {
        this.commentId = commentId;
        this.vidId = vidId;
        this.userId = userId;
        this.comment = comment;
        this.username = username;
        this.userImage = userImage;
        this.date = date;
        this.self_commented = self_commented;
    }

    public Comment(String commentId, String vidId, String userId, String comment, String username, String userImage, String date, VideoPlayerManager<MetaData> mVideoPlayerManager) {
        this.commentId = commentId;
        this.vidId = vidId;
        this.userId = userId;
        this.comment = comment;
        this.username = username;
        this.userImage = userImage;
        this.date = date;
        this.mVideoPlayerManager = mVideoPlayerManager;
    }

    public VideoPlayerManager<MetaData> getmVideoPlayerManager() {
        return mVideoPlayerManager;
    }

    public void setmVideoPlayerManager(VideoPlayerManager<MetaData> mVideoPlayerManager) {
        this.mVideoPlayerManager = mVideoPlayerManager;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVidId() {
        return vidId;
    }

    public void setVidId(String vidId) {
        this.vidId = vidId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSelf_commented() {
        return self_commented;
    }

    public void setSelf_commented(String self_commented) {
        this.self_commented = self_commented;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", vidId='" + vidId + '\'' +
                ", userId='" + userId + '\'' +
                ", comment='" + comment + '\'' +
                ", username='" + username + '\'' +
                ", userImage='" + userImage + '\'' +
                ", date='" + date + '\'' +
                ", self_commented='" + self_commented + '\'' +
                '}';
    }
}
