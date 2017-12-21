package db;

/**
 * Created by Chirag on 4/26/2016.
 */
public class LeaderBoard {

    int position;
    int id;
    String campId;
    String vidId;
    String userId;
    String imageid;
    String name;
    String folloers;
    String Desc;
    String like;
    String play;
    String share;
    String rank;
    String winner;
    String from;

    public LeaderBoard(int position,int id, String campId,String vidId,String userId,String imageid, String name, String folloers, String desc, String like, String play, String share,String rank,String winner,String from) {
        this.position = position;
        this.id = id;
        this.campId=campId;
        this.vidId = vidId;
        this.userId = userId;
        this.imageid = imageid;
        this.name = name;
        this.folloers = folloers;
        Desc = desc;
        this.like = like;
        this.play = play;
        this.share = share;
        this.rank = rank;
        this.winner = winner;
        this.from = from;
    }

    public LeaderBoard(String imageid, String name, String folloers, String desc, String like, String play, String share) {
        this.imageid = imageid;
        this.name = name;
        this.folloers = folloers;
        Desc = desc;
        this.like = like;
        this.play = play;
        this.share = share;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolloers() {
        return folloers;
    }

    public void setFolloers(String folloers) {
        this.folloers = folloers;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
