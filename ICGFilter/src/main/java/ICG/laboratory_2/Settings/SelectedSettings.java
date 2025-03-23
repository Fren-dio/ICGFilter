package ICG.laboratory_2.Settings;


import java.awt.Color;

public class SelectedSettings {

    private Boolean STRAIGHT_LINE_MODE;
    private Boolean WAVED_LINE_MODE;
    private int LINE_WEIGHT_RANGE;
    private int FIGURE_CORNERS;
    private boolean FIGURE_MODE;
    private Boolean FUN_MODE;

    private Boolean FILL_MODE;

    private Color currentColor;

    private int FIGURE_SIZE;
    private int FIGURE_ROTATE;

    private Boolean STAR_MODE;

    private int STAR_SIZE;
    private int STAR_ROTATE;
    private int STAR_CORNERS;
    private int STAR_RADIUS;

    public SelectedSettings(Color initialColor) {
        this.currentColor = initialColor;
        this.STRAIGHT_LINE_MODE = true;
        this.WAVED_LINE_MODE = false;
        this.FIGURE_CORNERS = 3;
        this.FIGURE_MODE = false;
        this.FUN_MODE = false;
        this.FILL_MODE = false;
        this.STAR_MODE = false;
        this.STAR_SIZE = 100;
        this.STAR_ROTATE = 0;
        this.STAR_CORNERS = 5;
        this.STAR_RADIUS = 50;

        this.LINE_WEIGHT_RANGE = 1;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public int getStarSize() {return this.STAR_SIZE;}
    public int getStarRotate() {return this.STAR_ROTATE;}
    public int getStarCorners() {return this.STAR_CORNERS;}
    public int getStarRadius() {return this.STAR_RADIUS;}

    public void setFigureSize(int value) {this.FIGURE_SIZE = value;}
    public void setFigureRotate(int value) {this.FIGURE_ROTATE = value;}

    public void setStarSize(int value) {this.STAR_SIZE = value;}
    public void setStarRotate(int value) {this.STAR_ROTATE = value;}
    public void setStarCorners(int value) {this.STAR_CORNERS = value;}
    public void setStarRadius(int value) {this.STAR_RADIUS = value;}

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public String getCurrentMode() {
        if (this.WAVED_LINE_MODE)
            return "WAVED_LINE_MODE";
        if (this.FUN_MODE)
            return "FUN_MODE";
        if (this.FIGURE_MODE)
            return "FIGURE_MODE";
        if (this.FILL_MODE)
            return "FILL_MODE";
        if (this.STAR_MODE)
            return "STAR_MODE";
        return "STRAIGHT_LINE_MODE";
    }

    public int getCurrentWeight()
    {
        return this.LINE_WEIGHT_RANGE;
    }

    public void setStarMode() {
        setAllFalse();
        this.STAR_MODE = true;
    }

    public void setStraightLineMode() {
        setAllFalse();
        this.STRAIGHT_LINE_MODE = true;
    }

    public void setWavedLineMode() {
        setAllFalse();
        this.WAVED_LINE_MODE = true;
    }

    public void setFillMode() {
        setAllFalse();
        this.FILL_MODE = true;
    }

    public void setFigurePatternMode(int corners) {
        setAllFalse();
        this.FIGURE_MODE = true;
        this.FIGURE_CORNERS = corners;
    }

    public int getFigureCorners() {
        return this.FIGURE_CORNERS;
    }

    public void setFunPatternMode() {
        setAllFalse();
        this.FUN_MODE = true;
    }

    public void setLightWeight(){
        this.LINE_WEIGHT_RANGE = 1;
    }

    public void setMediumWeight(){
        this.LINE_WEIGHT_RANGE = 3;
    }

    public void setBoldWeight(){
        this.LINE_WEIGHT_RANGE = 6;
    }

    public void setWeight(int weight){
        this.LINE_WEIGHT_RANGE = weight;
    }

    public void setFigureSettings(int size, int rotate){
        this.FIGURE_SIZE = size;
        this.FIGURE_ROTATE = rotate;
    }

    public int getFigureSize() {
        return this.FIGURE_SIZE;
    }

    public void setFigureMode() {
        this.FIGURE_MODE = true;
    }

    public int getFigureRotate() {
        return this.FIGURE_ROTATE;
    }

    private void setAllFalse(){
        this.STRAIGHT_LINE_MODE = false;
        this.WAVED_LINE_MODE = false;
        this.FUN_MODE = false;
        this.FIGURE_MODE = false;
        this.FILL_MODE = false;
        this.STAR_MODE = false;

    }
}