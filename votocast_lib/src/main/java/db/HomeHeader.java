package db;

/**
 * Created by Anil on 5/19/2016.
 */
public class HomeHeader {
    int id;
    String image;
    String mainText;
    String subText;

    public HomeHeader(int id, String image, String mainText, String subText) {
        this.id = id;
        this.image = image;
        this.mainText = mainText;
        this.subText = subText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
