package freestar.friends.util.sort_rich_editor.view.editor;

import android.graphics.Bitmap;

/**
 * Created by hitomi on 2016/6/3.
 */
public class SEditorData {

    private String inputStr;

    private String imagePath;

    private Bitmap bitmap;

    public String getInputStr() {
        return inputStr;
    }

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "SEditorData{" +
                "inputStr='" + inputStr + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", bitmap=" + bitmap +
                '}';
    }
}
