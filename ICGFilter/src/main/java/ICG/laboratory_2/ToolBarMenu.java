package ICG.laboratory_2;


import ICG.laboratory_2.Settings.SelectedSettings;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Objects;

public class ToolBarMenu extends JToolBar {

    private FrameWork frameWork;
    private final int btnSize = 40;

    private final ImagePanel imagePanel;
    private final SelectedSettings selectedSettings;



    public ToolBarMenu(ImagePanel imagePanel, SelectedSettings selectedSettings, FrameWork frameWork) {
        this.imagePanel = imagePanel;
        this.frameWork = frameWork;
        this.selectedSettings = selectedSettings;

        this.setFloatable(false);
        this.setRollover(false);

        addOpenSaveBtns();
        addResizeBtns();


        this.addSeparator();
        addOpenDefineImage();
        addBtnChoseColor();
        this.addSeparator();

        this.add(new JSeparator(SwingConstants.VERTICAL));

        addInfoPanel();


    }

    void addOpenSaveBtns() {
        JButton openBtn = new JButton("Open");
        openBtn.addActionListener(e -> openImage());
        this.add(openBtn);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> saveImage());
        this.add(saveBtn);
    }

    void saveImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        fileChooser.setCurrentDirectory(new File("images"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".png")) filePath += ".png";
            imagePanel.saveAsImage(filePath, "png");
        }
    }

    void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open File");
        fileChooser.setCurrentDirectory(new File("images"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg", "bmp", "gif"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            imagePanel.openImage(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    void addResizeBtns() {
        JButton toRealSizeBtn = new JButton("To real size");
        toRealSizeBtn.addActionListener(e -> resizeToRealSize());
        this.add(toRealSizeBtn);

        JButton toWindowSizeBtn = new JButton("To screen size");
        toWindowSizeBtn.addActionListener(e -> resizeToScreenSize());
        this.add(toWindowSizeBtn);
    }

    private void resizeToScreenSize() {
        imagePanel.outerCommandResizeToScreen();
    }

    private void resizeToRealSize() {
        imagePanel.outerCommandResizeToReal();
    }

    void addInfoPanel() {

        JPanel colorsGrid = new JPanel();
        colorsGrid.setLayout(new BoxLayout(colorsGrid, BoxLayout.Y_AXIS));

        JPanel row1 = new JPanel();
        row1.setLayout(new BoxLayout(row1, BoxLayout.X_AXIS));

        JLabel sizeLabelFrameWork = new JLabel("Размер окна: ");
        JLabel widthLabelFrameWork = new JLabel(String.valueOf(frameWork.getWidth()));
        JLabel sizeLabel2FrameWork = new JLabel(" x ");
        JLabel heightLabelFrameWork = new JLabel(String.valueOf(frameWork.getHeight()));
        row1.add(sizeLabelFrameWork);
        row1.add(widthLabelFrameWork);
        row1.add(sizeLabel2FrameWork);
        row1.add(heightLabelFrameWork);

        Runnable updateSizeLabelsFrameWork = () -> {
            int width = frameWork.getWidth();
            int height = frameWork.getHeight();
            widthLabelFrameWork.setText(String.valueOf(width));
            heightLabelFrameWork.setText(String.valueOf(height));
        };
        updateSizeLabelsFrameWork.run();

        frameWork.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateSizeLabelsFrameWork.run();
            }
        });


        JPanel row2 = new JPanel();
        row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));

        JLabel sizeLabel = new JLabel("Размер изображения: ");
        JLabel widthLabel = new JLabel(String.valueOf(imagePanel.getPreferredSize().getWidth()));
        JLabel sizeLabel2 = new JLabel(" x ");
        JLabel heightLabel = new JLabel(String.valueOf(imagePanel.getPreferredSize().getHeight()));
        row2.add(sizeLabel);
        row2.add(widthLabel);
        row2.add(sizeLabel2);
        row2.add(heightLabel);

        Runnable updateSizeLabels = () -> {
            int width = imagePanel.getWidth();
            int height = imagePanel.getHeight();
            widthLabel.setText(String.valueOf(width));
            heightLabel.setText(String.valueOf(height));
        };
        updateSizeLabels.run();

        imagePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateSizeLabels.run();
            }
        });

        colorsGrid.add(row1);
        colorsGrid.add(row2);
        this.add(colorsGrid);
    }

    void addOpenDefineImage() {
        JButton defineImageBtn = new JButton("define");
        defineImageBtn.setToolTipText("Open define image to look example.");
        setDimensionBtn(defineImageBtn, new Dimension(2*btnSize, btnSize-10));

        defineImageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imagePanel.clear();
                imagePanel.openImage("_test_image.jpg");
            }
        });

        this.add(defineImageBtn);
    }


    void addBtnChoseColor() {
        JButton colorSelectBtn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("/palitra_icon.jpg"))));
        colorSelectBtn.setToolTipText("Select color, what you want.");
        setDimensionBtn(colorSelectBtn, new Dimension(btnSize-10, btnSize-10));
        colorSelectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Открываем диалог выбора цвета
                Color selectedColor = JColorChooser.showDialog(
                        ToolBarMenu.this,
                        "Choose a Color",
                        colorSelectBtn.getBackground()
                );

                if (selectedColor != null) {
                    colorSelectBtn.setBackground(selectedColor);
                    selectedSettings.setCurrentColor(selectedColor);
                }
            }
        });
        this.add(colorSelectBtn);
    }


    private JButton setDimensionBtn(JButton btn, Dimension dim) {
        btn.setPreferredSize(dim);
        btn.setMinimumSize(dim);
        btn.setMaximumSize(dim);

        return btn;
    }



}