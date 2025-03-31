package ICG.laboratory_2.Settings;


import java.awt.Color;

public class SelectedSettings {

    private Color currentColor;


    public SelectedSettings(Color initialColor) {
        this.currentColor = initialColor;
    }

    public Color getCurrentColor() {
        return currentColor;
    }


    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

}