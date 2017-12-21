package db;

import android.app.Activity;

import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;

import java.util.ArrayList;

/**
 * Created by Anil on 5/26/2016.
 */
public class Home {

    Activity mActivity;
    int position;
    int vidId;
    String campId;
    String userId;
    String shortCode;
    String headerImage;
    String headerMainText;
    String headerSubText;
    String videoThumbImage;
    String videoUrl;
    String like;
    String view;
    String share;
    String desc;
    String didfollowed;
    String didliked;
    int rank;
    //    String is_winner;
    String cnt_comments;
    String created_on;

    ArrayList<Reports> reportList;

    int isShowBanner;
    int isSimpleDesc;
    int isMoreDesc;

    String self_video;

    public Home(Activity mActivity, int position, int vidId, String campId, String userId, String shortCode, String headerImage, String headerMainText, String headerSubText, String videoThumbImage, String videoUrl, String like, String view, String share, String desc, int isSimpleDesc,int isMoreDesc,String didfollowed, String didliked, int rank, int isShowBanner, String cnt_comments, String created_on, ArrayList<Reports> reportList,String self_video) {

        this.mActivity = mActivity;
        this.position = position;
        this.vidId = vidId;
        this.campId = campId;
        this.userId = userId;
        this.shortCode = shortCode;
        this.headerImage = headerImage;
        this.headerMainText = headerMainText;
        this.headerSubText = headerSubText;
        this.videoThumbImage = videoThumbImage;
        this.videoUrl = videoUrl;
        this.like = like;
        this.view = view;
        this.share = share;
        this.desc = desc;
        this.didfollowed = didfollowed;
        this.didliked = didliked;
        this.rank = rank;
        this.isShowBanner = isShowBanner;
        this.cnt_comments = cnt_comments;
        this.created_on = created_on;
        this.reportList = reportList;
        this.isSimpleDesc = isSimpleDesc;
        this.isMoreDesc = isMoreDesc;
        this.self_video=self_video;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Activity getmActivity() {
        return mActivity;
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public int getVidId() {
        return vidId;
    }

    public void setVidId(int vidId) {
        this.vidId = vidId;
    }

    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public String getHeaderMainText() {
        return headerMainText;
    }

    public void setHeaderMainText(String headerMainText) {
        this.headerMainText = headerMainText;
    }

    public String getHeaderSubText() {
        return headerSubText;
    }

    public void setHeaderSubText(String headerSubText) {
        this.headerSubText = headerSubText;
    }

    public String getVideoThumbImage() {
        return videoThumbImage;
    }

    public void setVideoThumbImage(String videoThumbImage) {
        this.videoThumbImage = videoThumbImage;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDidfollowed() {
        return didfollowed;
    }

    public void setDidfollowed(String didfollowed) {
        this.didfollowed = didfollowed;
    }

    public String getDidliked() {
        return didliked;
    }

    public void setDidliked(String didliked) {
        this.didliked = didliked;
    }

    public ArrayList<Reports> getReportList() {
        return reportList;
    }

    public void setReportList(ArrayList<Reports> reportList) {
        this.reportList = reportList;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getCnt_comments() {
        return cnt_comments;
    }

    public void setCnt_comments(String cnt_comments) {
        this.cnt_comments = cnt_comments;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public int getIsShowBanner() {
        return isShowBanner;
    }

    public void setIsShowBanner(int isShowBanner) {
        this.isShowBanner = isShowBanner;
    }

    public int getIsSimpleDesc() {
        return isSimpleDesc;
    }

    public void setIsSimpleDesc(int isSimpleDesc) {
        this.isSimpleDesc = isSimpleDesc;
    }

    public int getIsMoreDesc() {
        return isMoreDesc;
    }

    public void setIsMoreDesc(int isMoreDesc) {
        this.isMoreDesc = isMoreDesc;
    }

    public String getSelf_video() {
        return self_video;
    }

    public void setSelf_video(String self_video) {
        this.self_video = self_video;
    }
}
