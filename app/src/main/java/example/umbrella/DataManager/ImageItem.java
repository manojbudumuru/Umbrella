package example.umbrella.DataManager;

import android.graphics.Bitmap;

/**
 * Created by manojbudumuru on 9/12/17.
 */

public class ImageItem {
    private Bitmap image;
    private String time;
    private String value;
    private Boolean ishighest;
    private Boolean isLowest;

    public ImageItem(Bitmap image, String time, String value) {
        super();
        this.image = image;
        this.time = time;
        this.value = value;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
