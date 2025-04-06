package ICG.laboratory_2;

import javax.swing.*;

public class ManyVariantJButton extends JToggleButton {

    private int currentTextState;

    public ManyVariantJButton() {
        super();

        currentTextState = 0; //default value
        updateText();
        this.addActionListener(e -> {
            currentTextState = (currentTextState + 1) % 3;
            updateText();
        });

    }

    private void updateText() {
        if (currentTextState == 0) {
            this.setText("Билинейная");
        }
        else if (currentTextState == 1) {
            this.setText("Бикубическая");
        }
        else if (currentTextState == 2) {
            this.setText("Ближайший сосед");
        }
    }

    public String getInterpolationNameForLog() {
        if (currentTextState == 1-1) {
            return "Бикубическая";
        }
        else if (currentTextState == 2-1) {
            return "Ближайший сосед";
        }
        return "Билинейная";
    }

    public int getState() {
        return currentTextState;
    }

}
