package db;

/**
 * Created by Anil on 5/14/2016.
 */
public class Search {

    String id;
    int nodeId;
    int nodePos;
    String mainId;
    String mainImage;
    String mainText;
    String subText;
    String subImage;
    String didFollow;
    String totalFollow;

    public Search(String id, int nodeId, int nodePos, String mainId,String mainImage, String mainText, String subText, String subImage, String didFollow, String totalFollow) {
        this.id = id;
        this.nodeId = nodeId;
        this.nodePos = nodePos;
        this.mainId = mainId;
        this.mainImage = mainImage;
        this.mainText = mainText;
        this.subText = subText;
        this.subImage = subImage;
        this.didFollow = didFollow;
        this.totalFollow = totalFollow;
    }

//    public Search(String id, int nodeId, int nodePos, String mainImage, String mainText, String subText, String subImage) {
//        this.id = id;
//        this.nodeId = nodeId;
//        this.nodePos = nodePos;
//        this.mainImage = mainImage;
//        this.mainText = mainText;
//        this.subText = subText;
//        this.subImage = subImage;
//    }
//
//    public Search(String id, String mainImage, String mainText, String subText, String subImage) {
//        this.id = id;
//        this.mainImage = mainImage;
//        this.mainText = mainText;
//        this.subText = subText;
//        this.subImage = subImage;
//    }
//
//    public Search(String id, int nodeId, String mainImage, String mainText, String subText, String subImage) {
//        this.id = id;
//        this.nodeId = nodeId;
//        this.mainImage = mainImage;
//        this.mainText = mainText;
//        this.subText = subText;
//        this.subImage = subImage;
//    }


    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public String getDidFollow() {
        return didFollow;
    }

    public void setDidFollow(String didFollow) {
        this.didFollow = didFollow;
    }

    public String getTotalFollow() {
        return totalFollow;
    }

    public void setTotalFollow(String totalFollow) {
        this.totalFollow = totalFollow;
    }

    public int getNodePos() {
        return nodePos;
    }

    public void setNodePos(int nodePos) {
        this.nodePos = nodePos;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public String getSubImage() {
        return subImage;
    }

    public void setSubImage(String subImage) {
        this.subImage = subImage;
    }

    @Override
    public String toString() {
        return "Search{" +
                "id='" + id + '\'' +
                ", nodeId=" + nodeId +
                ", nodePos=" + nodePos +
                ", mainImage='" + mainImage + '\'' +
                ", mainText='" + mainText + '\'' +
                ", subText='" + subText + '\'' +
                ", subImage='" + subImage + '\'' +
                '}';
    }
}
