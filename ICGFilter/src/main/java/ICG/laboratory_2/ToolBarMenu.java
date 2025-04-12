package ICG.laboratory_2;


import ICG.laboratory_2.Settings.SelectedSettings;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Hashtable;
import java.util.Objects;

public class ToolBarMenu extends JToolBar {

    private int lastFunnyColorsValue = 0;
    private int lastRotateValue = 0;
    private int lastFSDitheringFullValue = 3;
    private int lastFSDitheringRED = 3;
    private int lastFSDitheringGREEN = 3;
    private int lastFSDitheringBLUE = 3;
    private int lastOrderedDitheringRED = 2;
    private int lastOrderedDitheringGREEN = 2;
    private int lastOrderedDitheringBLUE = 2;
    private boolean lastFloydSteinbergByFullPixel = true;
    private double lastGammaValue = 1.0;
    private EdgeDetectionType lastEdgeDetectionType = EdgeDetectionType.ROBERTS;
    private int lastEdgeDetectionThreshold = 50;
    private int lastGaussianSize = 3;
    private int lastSharpSize = 3;
    private boolean lastSharpSoftEffect = false;


    private FrameWork frameWork;
    private final int btnSize = 45;

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
        addRotateBtn();

        this.addSeparator();
        addOpenDefineImage();
        this.addSeparator();
        this.add(new JSeparator(SwingConstants.VERTICAL));

        addRGBToBWBtn();
        addBWToRGBBtn();
        addInverseBtn();
        addGausseBtn();
        addSharpBtn();
        addEmbossBtn();
        addGammaBtn();
        addChoseBordersBtn();
        addFloydSteinberg();
        addFunnyColors();
        addOrderedDithering();
        addAcvarel();
        addCrystalBtn();

        this.add(new JSeparator(SwingConstants.VERTICAL));

        JPanel setsGrid = new JPanel();
        setsGrid.setLayout(new BoxLayout(setsGrid, BoxLayout.Y_AXIS));

        JPanel row1 = new JPanel();
        row1.setLayout(new BoxLayout(row1, BoxLayout.X_AXIS));

        JLabel interpolLabel = new JLabel("Интерполяция: ", SwingConstants.CENTER);
        interpolLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        ManyVariantJButton interpolationBtn = new ManyVariantJButton();
        interpolationBtn.setToolTipText("Выбор способа интерполяции для изменения размеров изображения при сохранении пропорций");
        interpolationBtn.setSelected(false);
        interpolationBtn.addActionListener(e -> {
            imagePanel.setInterpolationMode((interpolationBtn.getState()+1)%3);
            System.out.println("Изменено состояние кнопки. Метод интерполяции изменен: " + interpolationBtn.getInterpolationNameForLog() + "\n");
        });
        row1.add(interpolLabel);
        row1.add(interpolationBtn);

        JPanel row2 = new JPanel();
        row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));

        JLabel label = new JLabel("Работа с исходным изображением", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        JToggleButton toggleButton = new JToggleButton("ДА");
        toggleButton.setToolTipText("ДА - фильтры применяются к изначальному изображению\nНЕТ - фильтры применяются последовательно");
        toggleButton.setSelected(true);
        row2.add(label);
        row2.add(toggleButton);

        toggleButton.addActionListener(e -> {
            if (toggleButton.isSelected()) {
                System.out.println("Состояние: Последовательное применение фильтров");
                toggleButton.setText("НЕТ");
                imagePanel.setFilterMode(false);
            } else {
                System.out.println("Состояние: Работа с исходным изображением");
                toggleButton.setText("ДА");
                imagePanel.setFilterMode(true);
            }
        });
        setsGrid.add(row1);
        setsGrid.add(row2);
        add(setsGrid);
        add(toggleButton);
        addInfoPanel();


    }

    void addOrderedDithering() {
        JButton btn = new JButton("");
        setDimensionBtn(btn, new Dimension(btnSize, btnSize));
        btn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/FS_dithering_icon.jpg"))));
        btn.setToolTipText("Look loaded image in ordered dithering format");
        btn.addActionListener(e -> openConvertToOrderedDitheringForm());
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(btn);
    }

    void openConvertToOrderedDitheringForm() {
        JFrame frame = new JFrame("Упорядоченный дизеринг");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(450, 300);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel paramPanel = new JPanel();
        paramPanel.setLayout(new BoxLayout(paramPanel, BoxLayout.Y_AXIS));
        paramPanel.setBorder(BorderFactory.createTitledBorder("Выберите параметр:"));
        paramPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel sliderPanelRED = new JPanel(new BorderLayout(5, 5));
        sliderPanelRED.setBorder(BorderFactory.createTitledBorder("Число квантования для красного:"));
        sliderPanelRED.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider sliderDithColorsRED = new JSlider(2, 128, lastOrderedDitheringRED);
        JSlider sliderDithColorsGREEN = new JSlider(2, 128, lastOrderedDitheringGREEN);
        JSlider sliderDithColorsBLUE = new JSlider(2, 128, lastOrderedDitheringBLUE);
        sliderDithColorsRED.setPreferredSize(new Dimension(450, 40));
        Hashtable<Integer, JLabel> labelTableDithColorsRED = new Hashtable<>();
        for (int i = 10; i <= 120; i += 10) {
            labelTableDithColorsRED.put(i, new JLabel(String.format("%d", i)));
        }

        sliderDithColorsRED.setLabelTable(labelTableDithColorsRED);
        sliderDithColorsRED.setMajorTickSpacing(10);
        sliderDithColorsRED.setMinorTickSpacing(1);
        sliderDithColorsRED.setPaintTicks(true);
        sliderDithColorsRED.setPaintLabels(true);

        JLabel valueLabelDithColorsRED = new JLabel("Выбрано число квантования для красного: 3", SwingConstants.CENTER);
        valueLabelDithColorsRED.setFont(new Font("Arial", Font.BOLD, 12));
        valueLabelDithColorsRED.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        sliderPanelRED.add(sliderDithColorsRED, BorderLayout.CENTER);
        sliderPanelRED.add(valueLabelDithColorsRED, BorderLayout.SOUTH);

        JPanel textFieldPanelDithColorsRED = new JPanel();
        textFieldPanelDithColorsRED.setLayout(new BoxLayout(textFieldPanelDithColorsRED, BoxLayout.X_AXIS));
        textFieldPanelDithColorsRED.setBorder(BorderFactory.createTitledBorder(""));
        textFieldPanelDithColorsRED.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField ditheringFieldRED = new JTextField(String.valueOf(lastOrderedDitheringRED));
        ditheringFieldRED.setHorizontalAlignment(JTextField.CENTER);
        ditheringFieldRED.setMaximumSize(new Dimension(100, 20));
        ditheringFieldRED.setPreferredSize(new Dimension(100, 20));

        textFieldPanelDithColorsRED.add(Box.createHorizontalGlue());
        textFieldPanelDithColorsRED.add(ditheringFieldRED);
        textFieldPanelDithColorsRED.add(Box.createHorizontalGlue());

        sliderDithColorsRED.addChangeListener(e -> {
            int gammaValue = sliderDithColorsRED.getValue();
            valueLabelDithColorsRED.setText(String.format("Текущее значение: %d", gammaValue));
            ditheringFieldRED.setText(String.format("%d", gammaValue));
        });

        ditheringFieldRED.addActionListener(e -> {
            try {
                double value = Integer.parseInt(ditheringFieldRED.getText());
                value = Math.max(2, Math.min(128, value));
                sliderDithColorsRED.setValue((int)(value));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Укажите целое значение от 2 до 128",
                        "Ошибка ввода",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        paramPanel.add(sliderPanelRED);
        paramPanel.add(Box.createVerticalStrut(10));
        paramPanel.add(textFieldPanelDithColorsRED);
        paramPanel.add(Box.createVerticalStrut(10));

        // for green
        JPanel sliderPanelGREEN = new JPanel(new BorderLayout(5, 5));
        sliderPanelGREEN.setBorder(BorderFactory.createTitledBorder("Число квантования для зеленого:"));
        sliderPanelGREEN.setAlignmentX(Component.CENTER_ALIGNMENT);

        sliderDithColorsGREEN.setPreferredSize(new Dimension(450, 40));
        Hashtable<Integer, JLabel> labelTableDithColorsGREEN = new Hashtable<>();
        for (int i = 10; i <= 120; i += 10) {
            labelTableDithColorsGREEN.put(i, new JLabel(String.format("%d", i)));
        }

        sliderDithColorsGREEN.setLabelTable(labelTableDithColorsGREEN);
        sliderDithColorsGREEN.setMajorTickSpacing(10);
        sliderDithColorsGREEN.setMinorTickSpacing(1);
        sliderDithColorsGREEN.setPaintTicks(true);
        sliderDithColorsGREEN.setPaintLabels(true);

        JLabel valueLabelDithColorsGREEN = new JLabel("Выбрано число квантования для зеленого: 3", SwingConstants.CENTER);
        valueLabelDithColorsGREEN.setFont(new Font("Arial", Font.BOLD, 12));
        valueLabelDithColorsGREEN.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        sliderPanelGREEN.add(sliderDithColorsGREEN, BorderLayout.CENTER);
        sliderPanelGREEN.add(valueLabelDithColorsGREEN, BorderLayout.SOUTH);

        JPanel textFieldPanelDithColorsGREEN = new JPanel();
        textFieldPanelDithColorsGREEN.setLayout(new BoxLayout(textFieldPanelDithColorsGREEN, BoxLayout.X_AXIS));
        textFieldPanelDithColorsGREEN.setBorder(BorderFactory.createTitledBorder(""));
        textFieldPanelDithColorsGREEN.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField ditheringFieldGREEN = new JTextField(String.valueOf(lastOrderedDitheringGREEN));
        ditheringFieldGREEN.setHorizontalAlignment(JTextField.CENTER);
        ditheringFieldGREEN.setMaximumSize(new Dimension(100, 20));
        ditheringFieldGREEN.setPreferredSize(new Dimension(100, 20));

        textFieldPanelDithColorsGREEN.add(Box.createHorizontalGlue());
        textFieldPanelDithColorsGREEN.add(ditheringFieldGREEN);
        textFieldPanelDithColorsGREEN.add(Box.createHorizontalGlue());

        sliderDithColorsGREEN.addChangeListener(e -> {
            int gammaValue = sliderDithColorsGREEN.getValue();
            valueLabelDithColorsGREEN.setText(String.format("Текущее значение: %d", gammaValue));
            ditheringFieldGREEN.setText(String.format("%d", gammaValue));
        });

        ditheringFieldGREEN.addActionListener(e -> {
            try {
                double value = Integer.parseInt(ditheringFieldGREEN.getText());
                value = Math.max(2, Math.min(128, value));
                sliderDithColorsGREEN.setValue((int)(value));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Укажите целое значение от 2 до 128",
                        "Ошибка ввода",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        paramPanel.add(sliderPanelGREEN);
        paramPanel.add(Box.createVerticalStrut(10));
        paramPanel.add(textFieldPanelDithColorsGREEN);
        paramPanel.add(Box.createVerticalStrut(10));

        // for blue
        JPanel sliderPanelBLUE = new JPanel(new BorderLayout(5, 5));
        sliderPanelBLUE.setBorder(BorderFactory.createTitledBorder("Число квантования для синего:"));
        sliderPanelBLUE.setAlignmentX(Component.CENTER_ALIGNMENT);

        sliderDithColorsBLUE.setPreferredSize(new Dimension(450, 40));
        Hashtable<Integer, JLabel> labelTableDithColorsBLUE = new Hashtable<>();
        for (int i = 10; i <= 120; i += 10) {
            labelTableDithColorsBLUE.put(i, new JLabel(String.format("%d", i)));
        }

        sliderDithColorsBLUE.setLabelTable(labelTableDithColorsBLUE);
        sliderDithColorsBLUE.setMajorTickSpacing(10);
        sliderDithColorsBLUE.setMinorTickSpacing(1);
        sliderDithColorsBLUE.setPaintTicks(true);
        sliderDithColorsBLUE.setPaintLabels(true);

        JLabel valueLabelDithColorsBLUE = new JLabel("Выбрано число квантования для синего: 3", SwingConstants.CENTER);
        valueLabelDithColorsBLUE.setFont(new Font("Arial", Font.BOLD, 12));
        valueLabelDithColorsBLUE.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        sliderPanelBLUE.add(sliderDithColorsBLUE, BorderLayout.CENTER);
        sliderPanelBLUE.add(valueLabelDithColorsBLUE, BorderLayout.SOUTH);

        JPanel textFieldPanelDithColorsBLUE = new JPanel();
        textFieldPanelDithColorsBLUE.setLayout(new BoxLayout(textFieldPanelDithColorsBLUE, BoxLayout.X_AXIS));
        textFieldPanelDithColorsBLUE.setBorder(BorderFactory.createTitledBorder(""));
        textFieldPanelDithColorsBLUE.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField ditheringFieldBLUE = new JTextField(String.valueOf(lastOrderedDitheringBLUE));
        ditheringFieldBLUE.setHorizontalAlignment(JTextField.CENTER);
        ditheringFieldBLUE.setMaximumSize(new Dimension(100, 20));
        ditheringFieldBLUE.setPreferredSize(new Dimension(100, 20));

        textFieldPanelDithColorsBLUE.add(Box.createHorizontalGlue());
        textFieldPanelDithColorsBLUE.add(ditheringFieldBLUE);
        textFieldPanelDithColorsBLUE.add(Box.createHorizontalGlue());

        sliderDithColorsBLUE.addChangeListener(e -> {
            int gammaValue = sliderDithColorsBLUE.getValue();
            valueLabelDithColorsBLUE.setText(String.format("Текущее значение: %d", gammaValue));
            ditheringFieldBLUE.setText(String.format("%d", gammaValue));
        });

        ditheringFieldBLUE.addActionListener(e -> {
            try {
                double value = Integer.parseInt(ditheringFieldBLUE.getText());
                value = Math.max(2, Math.min(128, value));
                sliderDithColorsBLUE.setValue((int)(value));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Укажите целое значение от 2 до 128",
                        "Ошибка ввода",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        paramPanel.add(sliderPanelBLUE);
        paramPanel.add(Box.createVerticalStrut(10));
        paramPanel.add(textFieldPanelDithColorsBLUE);
        paramPanel.add(Box.createVerticalStrut(10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton applyButton = new JButton("Применить");
        JButton cancelButton = new JButton("Отмена");

        Insets buttonMargin = new Insets(5, 15, 5, 15);
        cancelButton.setMargin(buttonMargin);
        applyButton.setMargin(buttonMargin);

        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);

        mainPanel.add(paramPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);

        frame.add(mainPanel, BorderLayout.CENTER);

        applyButton.addActionListener(e -> {
            lastOrderedDitheringRED = sliderDithColorsRED.getValue();
            lastOrderedDitheringGREEN = sliderDithColorsGREEN.getValue();
            lastOrderedDitheringBLUE = sliderDithColorsBLUE.getValue();
            convertToOrderedDithering(lastOrderedDitheringRED, lastOrderedDitheringGREEN, lastOrderedDitheringBLUE);
            frame.dispose();
        });

        cancelButton.addActionListener(e -> frame.dispose());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    void convertToOrderedDithering(int lastFSDitheringRED, int lastFSDitheringGREEN, int lastFSDitheringBLUE) {
        imagePanel.orderedDithering(lastOrderedDitheringRED, lastOrderedDitheringGREEN, lastOrderedDitheringBLUE);
    }

    void addAcvarel() {
        JButton btn = new JButton("");
        setDimensionBtn(btn, new Dimension(btnSize, btnSize));
        btn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/acvarel_icon.jpg"))));
        btn.setToolTipText("Look loaded image in acvarel format");
        btn.addActionListener(e -> convertToAcvarel());
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(btn);
    }

    void convertToAcvarel() {
        imagePanel.convertToAcvarel();
    }

    void addBWToRGBBtn() {
        JButton BWToRGBBtn = new JButton("");
        setDimensionBtn(BWToRGBBtn, new Dimension(btnSize, btnSize));
        BWToRGBBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/RGB_icon.jpg"))));
        BWToRGBBtn.setToolTipText("Look loaded image in rgb format");
        BWToRGBBtn.addActionListener(e -> convertImageFromBwToRGB());
        BWToRGBBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(BWToRGBBtn);
    }

    void addRGBToBWBtn() {
        JButton RGBToBWBtn = new JButton("");
        setDimensionBtn(RGBToBWBtn, new Dimension(btnSize, btnSize));
        RGBToBWBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/BW_icon.jpg"))));
        RGBToBWBtn.setToolTipText("Convert loaded image to black/white format");
        RGBToBWBtn.addActionListener(e -> convertImageFromRGBToBw());
        RGBToBWBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(RGBToBWBtn);
    }

    void addInverseBtn() {
        JButton RGBToBWBtn = new JButton("");
        setDimensionBtn(RGBToBWBtn, new Dimension(btnSize, btnSize));
        RGBToBWBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Inverse_icon.jpg"))));
        RGBToBWBtn.setToolTipText("Convert loaded image to inverse format");
        RGBToBWBtn.addActionListener(e -> convertImageToInverse());
        RGBToBWBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(RGBToBWBtn);
    }


    void addGausseBtn() {
        JButton GausseBtn = new JButton("");
        setDimensionBtn(GausseBtn, new Dimension(btnSize, btnSize));
        GausseBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Gausse_icon.jpg"))));
        GausseBtn.setToolTipText("Convert loaded image to gaussian blured format");
        GausseBtn.addActionListener(e -> openGausseForm());
        GausseBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(GausseBtn);
    }

    void addCrystalBtn() {
        JButton GausseBtn = new JButton("");
        setDimensionBtn(GausseBtn, new Dimension(btnSize, btnSize));
        GausseBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/crystal_icon.jpg"))));
        GausseBtn.setToolTipText("Convert loaded image to crystallic format");
        GausseBtn.addActionListener(e -> imagePanel.convertToCrystal(3));
        GausseBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(GausseBtn);
    }

    void addSharpBtn() {
        JButton btn = new JButton("");
        setDimensionBtn(btn, new Dimension(btnSize, btnSize));
        btn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/sharp_icon.jpg"))));
        btn.setToolTipText("Convert loaded image to more sharpness format");
        btn.addActionListener(e -> openSharpForm());
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(btn);
    }

    void addEmbossBtn() {
        JButton btn = new JButton("");
        setDimensionBtn(btn, new Dimension(btnSize, btnSize));
        btn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Embess_icon.jpg"))));
        btn.setToolTipText("Convert loaded image to emboss format");
        btn.addActionListener(e -> convertImageToEmboss());
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(btn);
    }

    void addGammaBtn() {
        JButton btn = new JButton("");
        setDimensionBtn(btn, new Dimension(btnSize, btnSize));
        btn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/edge_detection_icon.jpg"))));
        btn.setToolTipText("Convert loaded image to edge detection format");
        btn.addActionListener(e -> openEdgeDetectionForm());
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(btn);
    }

    void addChoseBordersBtn() {
        JButton btn = new JButton("");
        setDimensionBtn(btn, new Dimension(btnSize, btnSize));
        btn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/gamma_icon.jpg"))));
        btn.setToolTipText("Convert loaded image to gamma corrected format");
        btn.addActionListener(e -> openGammaForm());
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(btn);
    }

    void addFloydSteinberg() {
        JButton btn = new JButton("");
        setDimensionBtn(btn, new Dimension(btnSize, btnSize));
        btn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/dithering_icon.jpg"))));
        btn.setToolTipText("Convert loaded image to \"Floyd-Steinberg format");
        btn.addActionListener(e -> openFloydSteinbergForm());
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(btn);
    }


    void addFunnyColors() {
        JButton btn = new JButton("");
        setDimensionBtn(btn, new Dimension(btnSize, btnSize));
        btn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/my_dithering_icon.jpg"))));
        btn.setToolTipText("Convert loaded image to Floyd-Steinberg format without error's distribute");
        btn.addActionListener(e -> openConvertToFunnyColorsForm());
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(btn);
    }




    void openConvertToFunnyColorsForm() {
        JFrame frame = new JFrame("Дизеринг без распространения ошибки");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(450, 300);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel sliderPanel = new JPanel(new BorderLayout(5, 5));
        sliderPanel.setBorder(BorderFactory.createTitledBorder("Параметр value"));
        sliderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider slider = new JSlider(-255, 255, lastFunnyColorsValue);
        slider.setPreferredSize(new Dimension(400, 60));
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = -250; i <= 250; i += 50) {
            labelTable.put(i, new JLabel(String.format("%d", i)));
        }

        slider.setLabelTable(labelTable);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        JLabel valueLabel = new JLabel("Текущее значение: 0", SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        valueLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        sliderPanel.add(slider, BorderLayout.CENTER);
        sliderPanel.add(valueLabel, BorderLayout.SOUTH);

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.X_AXIS));
        textFieldPanel.setBorder(BorderFactory.createTitledBorder("Ручной ввод"));
        textFieldPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Изменили на CENTER

        JTextField gammaField = new JTextField(String.valueOf(lastFunnyColorsValue));
        gammaField.setHorizontalAlignment(JTextField.CENTER);
        gammaField.setMaximumSize(new Dimension(100, 25));
        gammaField.setPreferredSize(new Dimension(100, 25));

        textFieldPanel.add(Box.createHorizontalGlue());
        textFieldPanel.add(gammaField);
        textFieldPanel.add(Box.createHorizontalGlue());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton cancelButton = new JButton("Отмена");
        JButton applyButton = new JButton("Применить");

        Insets buttonMargin = new Insets(5, 15, 5, 15);
        cancelButton.setMargin(buttonMargin);
        applyButton.setMargin(buttonMargin);

        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);

        mainPanel.add(sliderPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(textFieldPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buttonPanel);

        frame.add(mainPanel, BorderLayout.CENTER);

        slider.addChangeListener(e -> {
            int gammaValue = slider.getValue();
            valueLabel.setText(String.format("Текущее значение: %d", gammaValue));
            gammaField.setText(String.format("%d", gammaValue));
        });

        gammaField.addActionListener(e -> {
            try {
                double value = Integer.parseInt(gammaField.getText());
                value = Math.max(-255, Math.min(255, value));
                slider.setValue((int)(value));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Укажите целое значение от -255 до 255",
                        "Ошибка ввода",
                        JOptionPane.ERROR_MESSAGE);
            }
        });


        applyButton.addActionListener(e -> {
            int value = slider.getValue();
            lastFunnyColorsValue = value;
            convertToFunnyColors(value);
            frame.dispose();
        });

        cancelButton.addActionListener(e -> {
            frame.dispose();
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    void openRotateForm() {
        JFrame frame = new JFrame("Выбор угла поворота");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(450, 300);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel sliderPanel = new JPanel(new BorderLayout(5, 5));
        sliderPanel.setBorder(BorderFactory.createTitledBorder("Угол поворота"));
        sliderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider slider = new JSlider(-180, 180, lastRotateValue);
        slider.setPreferredSize(new Dimension(400, 60));
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = -250; i <= 250; i += 50) {
            labelTable.put(i, new JLabel(String.format("%d", i)));
        }

        slider.setLabelTable(labelTable);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        JLabel valueLabel = new JLabel("Выбранный угол: 0", SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        valueLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        sliderPanel.add(slider, BorderLayout.CENTER);
        sliderPanel.add(valueLabel, BorderLayout.SOUTH);

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.X_AXIS));
        textFieldPanel.setBorder(BorderFactory.createTitledBorder("Ручной ввод"));
        textFieldPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField gammaField = new JTextField(String.valueOf(lastRotateValue));
        gammaField.setHorizontalAlignment(JTextField.CENTER);
        gammaField.setMaximumSize(new Dimension(100, 20));
        gammaField.setPreferredSize(new Dimension(100, 20));

        textFieldPanel.add(Box.createHorizontalGlue());
        textFieldPanel.add(gammaField);
        textFieldPanel.add(Box.createHorizontalGlue());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton cancelButton = new JButton("Отмена");
        JButton applyButton = new JButton("Применить");

        Insets buttonMargin = new Insets(5, 15, 5, 15);
        cancelButton.setMargin(buttonMargin);
        applyButton.setMargin(buttonMargin);

        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);

        mainPanel.add(sliderPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(textFieldPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buttonPanel);

        frame.add(mainPanel, BorderLayout.CENTER);

        slider.addChangeListener(e -> {
            int gammaValue = slider.getValue();
            valueLabel.setText(String.format("Текущее значение: %d", gammaValue));
            gammaField.setText(String.format("%d", gammaValue));
        });

        gammaField.addActionListener(e -> {
            try {
                double value = Integer.parseInt(gammaField.getText());
                value = Math.max(-180, Math.min(180, value));
                slider.setValue((int)(value));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Укажите целое значение от -180 до 180",
                        "Ошибка ввода",
                        JOptionPane.ERROR_MESSAGE);
            }
        });


        applyButton.addActionListener(e -> {
            int value = slider.getValue();
            lastRotateValue = value;
            imagePanel.rotateImage(value);
            frame.dispose();
        });

        cancelButton.addActionListener(e -> {
            frame.dispose();
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    void convertImageFromRGBToBw() {
        imagePanel.changeViewedImage("BW");
    }
    void convertImageFromBwToRGB() {
        imagePanel.changeViewedImage("RGB");
    }
    void convertImageToInverse() {
        imagePanel.changeViewedImage("INVERSE");
    }
    void convertImageToEmboss() {
        imagePanel.convertPixelToEmboss();
    }
    void convertToFunnyColors(int value){imagePanel.convertToFunnyColors(value);}


    public void openFloydSteinbergForm() {
        JFrame frame = new JFrame("Floyd-Steinberg дизеринг");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(450, 300);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel paramPanel = new JPanel();
        paramPanel.setLayout(new BoxLayout(paramPanel, BoxLayout.Y_AXIS));
        paramPanel.setBorder(BorderFactory.createTitledBorder("Выберите параметр:"));
        paramPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ButtonGroup paramGroup = new ButtonGroup();
        JRadioButton param1Button = new JRadioButton("Дизеринг по всему цвету пикселя", lastFloydSteinbergByFullPixel);
        param1Button.setFont(new Font("Arial", Font.BOLD, 16));
        JRadioButton param2Button = new JRadioButton("Дизеринг по каждому компоненту отдельно", !lastFloydSteinbergByFullPixel);
        param2Button.setFont(new Font("Arial", Font.BOLD, 16));

        paramGroup.add(param1Button);
        paramGroup.add(param2Button);

        paramPanel.add(param1Button);

        JPanel sliderPanelFullPixel = new JPanel(new BorderLayout(5, 5));
        sliderPanelFullPixel.setBorder(BorderFactory.createTitledBorder("Число квантования:"));
        sliderPanelFullPixel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider sliderDithColorsFullPixel = new JSlider(2, 128, lastFSDitheringFullValue);
        sliderDithColorsFullPixel.setPreferredSize(new Dimension(200, 40));
        Hashtable<Integer, JLabel> labelTableDithColorsFullPixel = new Hashtable<>();
        for (int i = 10; i <= 120; i += 10) {
            labelTableDithColorsFullPixel.put(i, new JLabel(String.format("%d", i)));
        }

        sliderDithColorsFullPixel.setLabelTable(labelTableDithColorsFullPixel);
        sliderDithColorsFullPixel.setMajorTickSpacing(10);
        sliderDithColorsFullPixel.setMinorTickSpacing(1);
        sliderDithColorsFullPixel.setPaintTicks(true);
        sliderDithColorsFullPixel.setPaintLabels(true);

        JLabel valueLabelDithColorsFullPixel = new JLabel("Выбрано число квантования: 0", SwingConstants.CENTER);
        valueLabelDithColorsFullPixel.setFont(new Font("Arial", Font.BOLD, 12));
        valueLabelDithColorsFullPixel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        sliderPanelFullPixel.add(sliderDithColorsFullPixel, BorderLayout.CENTER);
        sliderPanelFullPixel.add(valueLabelDithColorsFullPixel, BorderLayout.SOUTH);

        JPanel textFieldPanelDithColorsFullPixel = new JPanel();
        textFieldPanelDithColorsFullPixel.setLayout(new BoxLayout(textFieldPanelDithColorsFullPixel, BoxLayout.X_AXIS));
        textFieldPanelDithColorsFullPixel.setBorder(BorderFactory.createTitledBorder(""));
        textFieldPanelDithColorsFullPixel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField ditheringFieldFullPixel = new JTextField(String.valueOf(lastFSDitheringFullValue));
        ditheringFieldFullPixel.setHorizontalAlignment(JTextField.CENTER);
        ditheringFieldFullPixel.setMaximumSize(new Dimension(100, 20));
        ditheringFieldFullPixel.setPreferredSize(new Dimension(100, 20));

        textFieldPanelDithColorsFullPixel.add(Box.createHorizontalGlue());
        textFieldPanelDithColorsFullPixel.add(ditheringFieldFullPixel);
        textFieldPanelDithColorsFullPixel.add(Box.createHorizontalGlue());

        sliderDithColorsFullPixel.addChangeListener(e -> {
            int gammaValue = sliderDithColorsFullPixel.getValue();
            valueLabelDithColorsFullPixel.setText(String.format("Текущее значение: %d", gammaValue));
            ditheringFieldFullPixel.setText(String.format("%d", gammaValue));
        });

        ditheringFieldFullPixel.addActionListener(e -> {
            try {
                double value = Integer.parseInt(ditheringFieldFullPixel.getText());
                value = Math.max(2, Math.min(128, value));
                sliderDithColorsFullPixel.setValue((int)(value));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Укажите целое значение от 2 до 128",
                        "Ошибка ввода",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        paramPanel.add(sliderPanelFullPixel);
        paramPanel.add(Box.createVerticalStrut(10));
        paramPanel.add(textFieldPanelDithColorsFullPixel);
        paramPanel.add(Box.createVerticalStrut(10));

        paramPanel.add(param2Button);

        // for red
        JPanel sliderPanelRED = new JPanel(new BorderLayout(5, 5));
        sliderPanelRED.setBorder(BorderFactory.createTitledBorder("Число квантования для красного:"));
        sliderPanelRED.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider sliderDithColorsRED = new JSlider(2, 128, lastFSDitheringRED);
        sliderDithColorsRED.setPreferredSize(new Dimension(200, 40));
        Hashtable<Integer, JLabel> labelTableDithColorsRED = new Hashtable<>();
        for (int i = 10; i <= 120; i += 10) {
            labelTableDithColorsRED.put(i, new JLabel(String.format("%d", i)));
        }

        sliderDithColorsRED.setLabelTable(labelTableDithColorsRED);
        sliderDithColorsRED.setMajorTickSpacing(10);
        sliderDithColorsRED.setMinorTickSpacing(1);
        sliderDithColorsRED.setPaintTicks(true);
        sliderDithColorsRED.setPaintLabels(true);

        JLabel valueLabelDithColorsRED = new JLabel("Выбрано число квантования для красного: 3", SwingConstants.CENTER);
        valueLabelDithColorsRED.setFont(new Font("Arial", Font.BOLD, 12));
        valueLabelDithColorsRED.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        sliderPanelRED.add(sliderDithColorsRED, BorderLayout.CENTER);
        sliderPanelRED.add(valueLabelDithColorsRED, BorderLayout.SOUTH);

        JPanel textFieldPanelDithColorsRED = new JPanel();
        textFieldPanelDithColorsRED.setLayout(new BoxLayout(textFieldPanelDithColorsRED, BoxLayout.X_AXIS));
        textFieldPanelDithColorsRED.setBorder(BorderFactory.createTitledBorder(""));
        textFieldPanelDithColorsRED.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField ditheringFieldRED = new JTextField(String.valueOf(lastFSDitheringRED));
        ditheringFieldRED.setHorizontalAlignment(JTextField.CENTER);
        ditheringFieldRED.setMaximumSize(new Dimension(100, 20));
        ditheringFieldRED.setPreferredSize(new Dimension(100, 20));

        textFieldPanelDithColorsRED.add(Box.createHorizontalGlue());
        textFieldPanelDithColorsRED.add(ditheringFieldRED);
        textFieldPanelDithColorsRED.add(Box.createHorizontalGlue());

        sliderDithColorsRED.addChangeListener(e -> {
            int gammaValue = sliderDithColorsRED.getValue();
            valueLabelDithColorsRED.setText(String.format("Текущее значение: %d", gammaValue));
            ditheringFieldRED.setText(String.format("%d", gammaValue));
        });

        ditheringFieldRED.addActionListener(e -> {
            try {
                double value = Integer.parseInt(ditheringFieldRED.getText());
                value = Math.max(2, Math.min(128, value));
                sliderDithColorsRED.setValue((int)(value));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Укажите целое значение от 2 до 128",
                        "Ошибка ввода",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        paramPanel.add(sliderPanelRED);
        paramPanel.add(Box.createVerticalStrut(10));
        paramPanel.add(textFieldPanelDithColorsRED);
        paramPanel.add(Box.createVerticalStrut(10));

        // for green
        JPanel sliderPanelGREEN = new JPanel(new BorderLayout(5, 5));
        sliderPanelGREEN.setBorder(BorderFactory.createTitledBorder("Число квантования для зеленого:"));
        sliderPanelGREEN.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider sliderDithColorsGREEN = new JSlider(2, 128, lastFSDitheringGREEN);
        sliderDithColorsGREEN.setPreferredSize(new Dimension(200, 40));
        Hashtable<Integer, JLabel> labelTableDithColorsGREEN = new Hashtable<>();
        for (int i = 10; i <= 120; i += 10) {
            labelTableDithColorsGREEN.put(i, new JLabel(String.format("%d", i)));
        }

        sliderDithColorsGREEN.setLabelTable(labelTableDithColorsGREEN);
        sliderDithColorsGREEN.setMajorTickSpacing(10);
        sliderDithColorsGREEN.setMinorTickSpacing(1);
        sliderDithColorsGREEN.setPaintTicks(true);
        sliderDithColorsGREEN.setPaintLabels(true);

        JLabel valueLabelDithColorsGREEN = new JLabel("Выбрано число квантования для зеленого: 3", SwingConstants.CENTER);
        valueLabelDithColorsGREEN.setFont(new Font("Arial", Font.BOLD, 12));
        valueLabelDithColorsGREEN.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        sliderPanelGREEN.add(sliderDithColorsGREEN, BorderLayout.CENTER);
        sliderPanelGREEN.add(valueLabelDithColorsGREEN, BorderLayout.SOUTH);

        JPanel textFieldPanelDithColorsGREEN = new JPanel();
        textFieldPanelDithColorsGREEN.setLayout(new BoxLayout(textFieldPanelDithColorsGREEN, BoxLayout.X_AXIS));
        textFieldPanelDithColorsGREEN.setBorder(BorderFactory.createTitledBorder(""));
        textFieldPanelDithColorsGREEN.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField ditheringFieldGREEN = new JTextField(String.valueOf(lastFSDitheringGREEN));
        ditheringFieldGREEN.setHorizontalAlignment(JTextField.CENTER);
        ditheringFieldGREEN.setMaximumSize(new Dimension(100, 20));
        ditheringFieldGREEN.setPreferredSize(new Dimension(100, 20));

        textFieldPanelDithColorsGREEN.add(Box.createHorizontalGlue());
        textFieldPanelDithColorsGREEN.add(ditheringFieldGREEN);
        textFieldPanelDithColorsGREEN.add(Box.createHorizontalGlue());

        sliderDithColorsGREEN.addChangeListener(e -> {
            int gammaValue = sliderDithColorsGREEN.getValue();
            valueLabelDithColorsGREEN.setText(String.format("Текущее значение: %d", gammaValue));
            ditheringFieldGREEN.setText(String.format("%d", gammaValue));
        });

        ditheringFieldGREEN.addActionListener(e -> {
            try {
                double value = Integer.parseInt(ditheringFieldGREEN.getText());
                value = Math.max(2, Math.min(128, value));
                sliderDithColorsGREEN.setValue((int)(value));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Укажите целое значение от 2 до 128",
                        "Ошибка ввода",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        paramPanel.add(sliderPanelGREEN);
        paramPanel.add(Box.createVerticalStrut(10));
        paramPanel.add(textFieldPanelDithColorsGREEN);
        paramPanel.add(Box.createVerticalStrut(10));

        // for blue
        JPanel sliderPanelBLUE = new JPanel(new BorderLayout(5, 5));
        sliderPanelBLUE.setBorder(BorderFactory.createTitledBorder("Число квантования для синего:"));
        sliderPanelBLUE.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider sliderDithColorsBLUE = new JSlider(2, 128, lastFSDitheringBLUE);
        sliderDithColorsBLUE.setPreferredSize(new Dimension(200, 40));
        Hashtable<Integer, JLabel> labelTableDithColorsBLUE = new Hashtable<>();
        for (int i = 10; i <= 120; i += 10) {
            labelTableDithColorsBLUE.put(i, new JLabel(String.format("%d", i)));
        }

        sliderDithColorsBLUE.setLabelTable(labelTableDithColorsBLUE);
        sliderDithColorsBLUE.setMajorTickSpacing(10);
        sliderDithColorsBLUE.setMinorTickSpacing(1);
        sliderDithColorsBLUE.setPaintTicks(true);
        sliderDithColorsBLUE.setPaintLabels(true);

        JLabel valueLabelDithColorsBLUE = new JLabel("Выбрано число квантования для синего: 3", SwingConstants.CENTER);
        valueLabelDithColorsBLUE.setFont(new Font("Arial", Font.BOLD, 12));
        valueLabelDithColorsBLUE.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        sliderPanelBLUE.add(sliderDithColorsBLUE, BorderLayout.CENTER);
        sliderPanelBLUE.add(valueLabelDithColorsBLUE, BorderLayout.SOUTH);

        JPanel textFieldPanelDithColorsBLUE = new JPanel();
        textFieldPanelDithColorsBLUE.setLayout(new BoxLayout(textFieldPanelDithColorsBLUE, BoxLayout.X_AXIS));
        textFieldPanelDithColorsBLUE.setBorder(BorderFactory.createTitledBorder(""));
        textFieldPanelDithColorsBLUE.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField ditheringFieldBLUE = new JTextField(String.valueOf(lastFSDitheringBLUE));
        ditheringFieldBLUE.setHorizontalAlignment(JTextField.CENTER);
        ditheringFieldBLUE.setMaximumSize(new Dimension(100, 20));
        ditheringFieldBLUE.setPreferredSize(new Dimension(100, 20));

        textFieldPanelDithColorsBLUE.add(Box.createHorizontalGlue());
        textFieldPanelDithColorsBLUE.add(ditheringFieldBLUE);
        textFieldPanelDithColorsBLUE.add(Box.createHorizontalGlue());

        sliderDithColorsBLUE.addChangeListener(e -> {
            int gammaValue = sliderDithColorsBLUE.getValue();
            valueLabelDithColorsBLUE.setText(String.format("Текущее значение: %d", gammaValue));
            ditheringFieldBLUE.setText(String.format("%d", gammaValue));
        });

        ditheringFieldBLUE.addActionListener(e -> {
            try {
                double value = Integer.parseInt(ditheringFieldBLUE.getText());
                value = Math.max(2, Math.min(128, value));
                sliderDithColorsBLUE.setValue((int)(value));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Укажите целое значение от 2 до 128",
                        "Ошибка ввода",
                        JOptionPane.ERROR_MESSAGE);
            }
        });


        paramPanel.add(sliderPanelBLUE);
        paramPanel.add(Box.createVerticalStrut(10));
        paramPanel.add(textFieldPanelDithColorsBLUE);
        paramPanel.add(Box.createVerticalStrut(10));



        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton applyButton = new JButton("Применить");
        JButton cancelButton = new JButton("Отмена");

        Insets buttonMargin = new Insets(5, 15, 5, 15);
        cancelButton.setMargin(buttonMargin);
        applyButton.setMargin(buttonMargin);

        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);

        mainPanel.add(paramPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);

        frame.add(mainPanel, BorderLayout.CENTER);

        applyButton.addActionListener(e -> {
            boolean byFullPixel = param1Button.isSelected();
            lastFloydSteinbergByFullPixel = byFullPixel;
            lastFSDitheringFullValue = sliderDithColorsFullPixel.getValue();
            lastFSDitheringRED = sliderDithColorsRED.getValue();
            lastFSDitheringGREEN = sliderDithColorsGREEN.getValue();
            lastFSDitheringBLUE = sliderDithColorsBLUE.getValue();
            imagePanel.convertToFloydSteinberg(byFullPixel, lastFSDitheringFullValue, lastFSDitheringRED, lastFSDitheringGREEN, lastFSDitheringBLUE);
            frame.dispose();
        });

        cancelButton.addActionListener(e -> frame.dispose());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void openGammaForm() {
        JFrame frame = new JFrame("Гамма-коррекция");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(450, 300);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel sliderPanel = new JPanel(new BorderLayout(5, 5));
        sliderPanel.setBorder(BorderFactory.createTitledBorder("Параметр γ"));
        sliderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider gammaSlider = new JSlider(1, 100, (int)(lastGammaValue * 10));
        gammaSlider.setPreferredSize(new Dimension(400, 60));

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = 1; i <= 100; i += 10) {
            float value = i / 10.0f;
            labelTable.put(i, new JLabel(String.format("%.1f", value)));
        }

        gammaSlider.setLabelTable(labelTable);
        gammaSlider.setMajorTickSpacing(10);
        gammaSlider.setMinorTickSpacing(1);
        gammaSlider.setPaintTicks(true);
        gammaSlider.setPaintLabels(true);

        JLabel valueLabel = new JLabel("γ = 1.0", SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        valueLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        sliderPanel.add(gammaSlider, BorderLayout.CENTER);
        sliderPanel.add(valueLabel, BorderLayout.SOUTH);

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.X_AXIS));
        textFieldPanel.setBorder(BorderFactory.createTitledBorder("Ручной ввод"));
        textFieldPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField gammaField = new JTextField(String.format("%.1f", lastGammaValue));
        gammaField.setHorizontalAlignment(JTextField.CENTER);
        gammaField.setMaximumSize(new Dimension(100, 25));
        gammaField.setPreferredSize(new Dimension(100, 25));

        textFieldPanel.add(Box.createHorizontalGlue());
        textFieldPanel.add(gammaField);
        textFieldPanel.add(Box.createHorizontalGlue());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton applyButton = new JButton("Применить");
        JButton cancelButton = new JButton("Отмена");

        Insets buttonMargin = new Insets(5, 15, 5, 15);
        cancelButton.setMargin(buttonMargin);
        applyButton.setMargin(buttonMargin);

        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);

        mainPanel.add(sliderPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(textFieldPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buttonPanel);

        frame.add(mainPanel, BorderLayout.CENTER);

        gammaSlider.addChangeListener(e -> {
            double gammaValue = gammaSlider.getValue() / 10.0;
            valueLabel.setText(String.format("γ = %.1f", gammaValue));
            gammaField.setText(String.format("%.1f", gammaValue));
        });

        gammaField.addActionListener(e -> {
            try {
                double value = Double.parseDouble(gammaField.getText());
                value = Math.max(0.1, Math.min(10.0, value));
                gammaSlider.setValue((int)(value * 10));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Введите число от 0.1 до 10.0", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        applyButton.addActionListener(e -> {
            double gamma = gammaSlider.getValue() / 10.0;
            lastGammaValue = gamma;
            imagePanel.convertToGamma(gamma);
            frame.dispose();
        });

        cancelButton.addActionListener(e -> frame.dispose());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void openEdgeDetectionForm() {
        JFrame frame = new JFrame("Выделение границ");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(450, 350);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Панель выбора оператора
        JPanel operatorPanel = new JPanel();
        operatorPanel.setLayout(new BoxLayout(operatorPanel, BoxLayout.Y_AXIS));
        operatorPanel.setBorder(BorderFactory.createTitledBorder("Выберите оператор"));
        operatorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ButtonGroup typeGroup = new ButtonGroup();
        JRadioButton robertsButton = new JRadioButton("Оператор Робертса", lastEdgeDetectionType == EdgeDetectionType.ROBERTS);
        JRadioButton sobelButton = new JRadioButton("Оператор Собеля", lastEdgeDetectionType == EdgeDetectionType.SOBEL);
        typeGroup.add(robertsButton);
        typeGroup.add(sobelButton);

        operatorPanel.add(robertsButton);
        operatorPanel.add(sobelButton);

        // Панель слайдера порога
        JPanel thresholdPanel = new JPanel(new BorderLayout(5, 5));
        thresholdPanel.setBorder(BorderFactory.createTitledBorder("Порог бинаризации"));
        thresholdPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider thresholdSlider = new JSlider(0, 255, lastEdgeDetectionThreshold);
        thresholdSlider.setPreferredSize(new Dimension(400, 60));
        thresholdSlider.setMajorTickSpacing(50);
        thresholdSlider.setMinorTickSpacing(10);
        thresholdSlider.setPaintTicks(true);
        thresholdSlider.setPaintLabels(true);

        JLabel thresholdLabel = new JLabel("Порог: 50", SwingConstants.CENTER);
        thresholdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        thresholdLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        thresholdPanel.add(thresholdSlider, BorderLayout.CENTER);
        thresholdPanel.add(thresholdLabel, BorderLayout.SOUTH);

        // Панель текстового поля
        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.X_AXIS));
        textFieldPanel.setBorder(BorderFactory.createTitledBorder("Ручной ввод"));
        textFieldPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField thresholdField = new JTextField(String.valueOf(lastEdgeDetectionThreshold));
        thresholdField.setHorizontalAlignment(JTextField.CENTER);
        thresholdField.setMaximumSize(new Dimension(100, 25));
        thresholdField.setPreferredSize(new Dimension(100, 25));

        textFieldPanel.add(Box.createHorizontalGlue());
        textFieldPanel.add(thresholdField);
        textFieldPanel.add(Box.createHorizontalGlue());

        // Панель кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton applyButton = new JButton("Применить");
        JButton cancelButton = new JButton("Отмена");

        Insets buttonMargin = new Insets(5, 15, 5, 15);
        cancelButton.setMargin(buttonMargin);
        applyButton.setMargin(buttonMargin);

        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);

        // Добавление компонентов
        mainPanel.add(operatorPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(thresholdPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(textFieldPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buttonPanel);

        frame.add(mainPanel, BorderLayout.CENTER);

        // Обработчики событий
        thresholdSlider.addChangeListener(e -> {
            int value = thresholdSlider.getValue();
            thresholdLabel.setText("Порог: " + value);
            thresholdField.setText(String.valueOf(value));
        });

        thresholdField.addActionListener(e -> {
            try {
                int value = Integer.parseInt(thresholdField.getText());
                value = Math.max(0, Math.min(255, value));
                thresholdSlider.setValue(value);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Введите число от 0 до 255", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        applyButton.addActionListener(e -> {
            EdgeDetectionType type = robertsButton.isSelected() ?
                    EdgeDetectionType.ROBERTS : EdgeDetectionType.SOBEL;
            lastEdgeDetectionType = robertsButton.isSelected() ?
                    EdgeDetectionType.ROBERTS : EdgeDetectionType.SOBEL;
            lastEdgeDetectionThreshold = thresholdSlider.getValue();
            imagePanel.edgeDetectionConvert(type, thresholdSlider.getValue());
            frame.dispose();
        });

        cancelButton.addActionListener(e -> frame.dispose());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void openGausseForm() {
        JFrame frame = new JFrame("Гауссово размытие");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(450, 300);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel matrixPanel = new JPanel();
        matrixPanel.setLayout(new BoxLayout(matrixPanel, BoxLayout.Y_AXIS));
        matrixPanel.setBorder(BorderFactory.createTitledBorder("Выберите размер матрицы"));
        matrixPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ButtonGroup matrixGroup = new ButtonGroup();
        JRadioButton matrix3x3 = new JRadioButton("Матрица 3×3", lastGaussianSize == 3);
        JRadioButton matrix5x5 = new JRadioButton("Матрица 5×5", lastGaussianSize == 5);
        JRadioButton matrix7x7 = new JRadioButton("Матрица 7×7", lastGaussianSize == 7);
        JRadioButton matrix9x9 = new JRadioButton("Матрица 9×9", lastGaussianSize == 9);
        JRadioButton matrix11x11 = new JRadioButton("Матрица 11×11", lastGaussianSize == 11);

        matrixGroup.add(matrix3x3);
        matrixGroup.add(matrix5x5);
        matrixGroup.add(matrix7x7);
        matrixGroup.add(matrix9x9);
        matrixGroup.add(matrix11x11);

        matrixPanel.add(matrix3x3);
        matrixPanel.add(matrix5x5);
        matrixPanel.add(matrix7x7);
        matrixPanel.add(matrix9x9);
        matrixPanel.add(matrix11x11);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton applyButton = new JButton("Применить");
        JButton cancelButton = new JButton("Отмена");

        Insets buttonMargin = new Insets(5, 15, 5, 15);
        cancelButton.setMargin(buttonMargin);
        applyButton.setMargin(buttonMargin);

        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);

        mainPanel.add(matrixPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);

        frame.add(mainPanel, BorderLayout.CENTER);

        applyButton.addActionListener(e -> {
            int size = 3;
            if (matrix5x5.isSelected()) size = 5;
            else if (matrix7x7.isSelected()) size = 7;
            else if (matrix9x9.isSelected()) size = 9;
            else if (matrix11x11.isSelected()) size = 11;

            if (matrix5x5.isSelected()) lastGaussianSize = 5;
            else if (matrix7x7.isSelected()) lastGaussianSize = 7;
            else if (matrix9x9.isSelected()) lastGaussianSize = 9;
            else if (matrix11x11.isSelected()) lastGaussianSize = 11;
            else lastGaussianSize = 3;

            imagePanel.convertPixelByGausse(size);
            frame.dispose();
        });

        cancelButton.addActionListener(e -> frame.dispose());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void openSharpForm() {
        JFrame frame = new JFrame("Увеличение резкости");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(450, 300);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel matrixPanel = new JPanel();
        matrixPanel.setLayout(new BoxLayout(matrixPanel, BoxLayout.Y_AXIS));
        matrixPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ButtonGroup matrixGroup = new ButtonGroup();
        JRadioButton matrix3x3 = new JRadioButton("Матрица 3×3", lastSharpSize == 3);
        JRadioButton matrix5x5 = new JRadioButton("Матрица 5×5", lastSharpSize == 5);
        matrixGroup.add(matrix3x3);
        matrixGroup.add(matrix5x5);

        matrixPanel.add(matrix3x3);
        matrixPanel.add(matrix5x5);

        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        checkboxPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JCheckBox softEffectCheckBox = new JCheckBox("Мягкий эффект", lastSharpSoftEffect);
        checkboxPanel.add(softEffectCheckBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton applyButton = new JButton("Применить");
        JButton cancelButton = new JButton("Отмена");

        Insets buttonMargin = new Insets(5, 15, 5, 15);
        cancelButton.setMargin(buttonMargin);
        applyButton.setMargin(buttonMargin);

        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);

        mainPanel.setBorder(BorderFactory.createTitledBorder("Выберите размер матрицы"));
        mainPanel.add(matrixPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(checkboxPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buttonPanel);

        frame.add(mainPanel, BorderLayout.CENTER);

        applyButton.addActionListener(e -> {
            int size = matrix3x3.isSelected() ? 3 : 5;
            boolean softEffect = softEffectCheckBox.isSelected();

            lastSharpSize = matrix3x3.isSelected() ? 3 : 5;
            lastSharpSoftEffect = softEffectCheckBox.isSelected();

            if (size == 3) {
                imagePanel.convertPixelBySharp3(softEffect);
            } else {
                imagePanel.convertPixelBySharp5(softEffect);
            }
            frame.dispose();
        });

        cancelButton.addActionListener(e -> frame.dispose());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    enum EdgeDetectionType {
        ROBERTS, SOBEL
    }



    void addOpenSaveBtns() {
        JButton openBtn = new JButton("");
        setDimensionBtn(openBtn, new Dimension(45, 45));
        openBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/open_icon.jpg"))));
        openBtn.setToolTipText("Open image");
        openBtn.addActionListener(e -> openImage());
        this.add(openBtn);

        JButton saveBtn = new JButton("");
        setDimensionBtn(saveBtn, new Dimension(45, 45));
        saveBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/save_icon.jpg"))));
        saveBtn.setToolTipText("Save current image");
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
        JButton toRealSizeBtn = new JButton("");
        setDimensionBtn(toRealSizeBtn, new Dimension(45, 45));
        toRealSizeBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/full_size.jpg"))));
        toRealSizeBtn.setToolTipText("Convert image to real size (pixel to pixel)");
        toRealSizeBtn.addActionListener(e -> resizeToRealSize());
        this.add(toRealSizeBtn);

        JButton toWindowSizeBtn = new JButton("");
        setDimensionBtn(toWindowSizeBtn, new Dimension(45, 45));
        toWindowSizeBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/screen_size.jpg"))));
        toWindowSizeBtn.setToolTipText("Convert to current image size. Resize loaded image.");
        toWindowSizeBtn.addActionListener(e -> resizeToScreenSize());
        this.add(toWindowSizeBtn);
    }


    void addRotateBtn() {
        JButton btn = new JButton("");
        setDimensionBtn(btn, new Dimension(45, 45));
        btn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/rotate_icon.jpg"))));
        btn.setToolTipText("Rotate image");
        btn.addActionListener(e -> openRotateForm());
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(btn);
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
        JLabel widthLabel = new JLabel(String.valueOf(imagePanel.getImageWidth()));
        JLabel sizeLabel2 = new JLabel(" x ");
        JLabel heightLabel = new JLabel(String.valueOf(imagePanel.getImageHeight()));
        row2.add(sizeLabel);
        row2.add(widthLabel);
        row2.add(sizeLabel2);
        row2.add(heightLabel);

        Runnable updateSizeLabels = () -> {
            int width = imagePanel.getImageWidth();
            int height = imagePanel.getImageHeight();
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
        JButton defineImageBtn = new JButton("");
        setDimensionBtn(defineImageBtn, new Dimension(45, 45));
        defineImageBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Define_icon.jpg"))));
        defineImageBtn.setToolTipText("Open define image to look example.");
        setDimensionBtn(defineImageBtn, new Dimension(btnSize, btnSize));

        defineImageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imagePanel.clear();
                imagePanel.openImage("_test_image.jpg");
            }
        });

        this.add(defineImageBtn);
    }


    private JButton setDimensionBtn(JButton btn, Dimension dim) {
        btn.setPreferredSize(dim);
        btn.setMinimumSize(dim);
        btn.setMaximumSize(dim);

        return btn;
    }



}