package ICG.laboratory_2.Settings;


public class SavedFigureSettings {

    private boolean isStar;
    private int corners;
    private int size;
    private int rotate;
    private int radius;
    private boolean wasApply;

    public SavedFigureSettings()
    {
        wasApply = false;
        isStar = false;
        corners = 3;
        size = 100;
        rotate = 0;
        radius = 0;
    }

    public void setApply() {
        wasApply = true;
    }

    public boolean isApply() {
        return wasApply;
    }

    public void setIsStar(boolean var) {
        isStar = var;
    }

    public boolean isStar() {
        return  isStar;
    }

    public void setSize(int var) {
        size = var;
    }

    public int getSize() {
        return size;
    }

    public void setRotate(int var) {
        rotate = var;
    }

    public int getRotate() {
        return rotate;
    }

    public void setCorners(int var) {
        corners = var;
    }

    public int getCorners() {
        return corners;
    }

    public void setRadius(int var) {
        radius = var;
    }

    public int getRadius() {
        return radius;
    }

}
