package db;

/**
 * Created by Anil on 5/3/2016.
 */
public class NewestVideo {
    int position;
    String id;
    String imagePath;
    String from;
    String typeId;

    public NewestVideo(int position,String id, String imagePath,String from,String typeId) {
        this.position=position;
        this.id = id;
        this.imagePath = imagePath;
        this.from = from;
        this.typeId = typeId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
