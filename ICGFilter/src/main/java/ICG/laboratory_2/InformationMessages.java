package ICG.laboratory_2;


import javax.swing.*;
import java.awt.*;

public class InformationMessages {

    public void showInformationMessage(ImagePanel imagePanel, String text, String title){
        JOptionPane.showMessageDialog(imagePanel, text, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
