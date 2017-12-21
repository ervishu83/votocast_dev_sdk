package db;

/**
 * Created by Anil on 5/30/2016.
 */
public class ActivityClass {
    int nodeId;
    int nodePos;
    String actionId;
    String action;
    String actionContent;
    String date;
    String mainUserId;
    String mainUserName;
    String mainUserPic;
    String otherUserId;
    String otherUsername;
    String otherUserPic;

    public ActivityClass(int nodeId, int nodePos,String actionId, String action,String actionContent, String date, String mainUserId, String mainUserName, String mainUserPic, String otherUserId, String otherUsername, String otherUserPic) {
        this.nodeId = nodeId;
        this.nodePos = nodePos;
        this.actionId = actionId;
        this.action = action;
        this.actionContent = actionContent;
        this.date = date;
        this.mainUserId = mainUserId;
        this.mainUserName = mainUserName;
        this.mainUserPic = mainUserPic;
        this.otherUserId = otherUserId;
        this.otherUsername = otherUsername;
        this.otherUserPic = otherUserPic;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getNodePos() {
        return nodePos;
    }

    public void setNodePos(int nodePos) {
        this.nodePos = nodePos;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMainUserId() {
        return mainUserId;
    }

    public void setMainUserId(String mainUserId) {
        this.mainUserId = mainUserId;
    }

    public String getMainUserName() {
        return mainUserName;
    }

    public void setMainUserName(String mainUserName) {
        this.mainUserName = mainUserName;
    }

    public String getMainUserPic() {
        return mainUserPic;
    }

    public void setMainUserPic(String mainUserPic) {
        this.mainUserPic = mainUserPic;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getOtherUsername() {
        return otherUsername;
    }

    public void setOtherUsername(String otherUsername) {
        this.otherUsername = otherUsername;
    }

    public String getOtherUserPic() {
        return otherUserPic;
    }

    public void setOtherUserPic(String otherUserPic) {
        this.otherUserPic = otherUserPic;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionContent() {
        return actionContent;
    }

    public void setActionContent(String actionContent) {
        this.actionContent = actionContent;
    }
    @Override
    public String toString() {
        return "ActivityClass{" +
                "nodeId=" + nodeId +
                ", nodePos=" + nodePos +
                ", actionId='" + actionId + '\'' +
                ", action='" + action + '\'' +
                ", actionContent='" + actionContent + '\'' +
                ", date='" + date + '\'' +
                ", mainUserId='" + mainUserId + '\'' +
                ", mainUserName='" + mainUserName + '\'' +
                ", mainUserPic='" + mainUserPic + '\'' +
                ", otherUserId='" + otherUserId + '\'' +
                ", otherUsername='" + otherUsername + '\'' +
                ", otherUserPic='" + otherUserPic + '\'' +
                '}';
    }
}
