package ICG.laboratory_2;


import ICG.laboratory_2.Settings.SelectedSettings;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Hashtable;
import java.util.Objects;

public class ToolBarMenu extends JToolBar {

    private FrameWork frameWork;
    private final int btnSize = 50;

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

    void addSharpBtn() {
        JButton GausseBtn = new JButton("Sharp");
        setDimensionBtn(GausseBtn, new Dimension(btnSize, btnSize));
        GausseBtn.setToolTipText("Convert loaded image to more sharpness format");
        GausseBtn.addActionListener(e -> openSharpForm());
        GausseBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(GausseBtn);
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
        JButton btn = new JButton("EdgeDetection");
        setDimensionBtn(btn, new Dimension(btnSize, btnSize));
        btn.setToolTipText("Convert loaded image to edge detection format");
        btn.addActionListener(e -> openEdgeDetectionForm());
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(btn);
    }

    void addChoseBordersBtn() {
        JButton btn = new JButton("Gamma");
        setDimensionBtn(btn, new Dimension(btnSize, btnSize));
        btn.setToolTipText("Convert loaded image to gamma corrected format");
        btn.addActionListener(e -> openGammaForm());
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(btn);
    }

    void addFloydSteinberg() {
        JButton btn = new JButton("Floyd-Steinberg");
        setDimensionBtn(btn, new Dimension(btnSize, btnSize));
        btn.setToolTipText("Convert loaded image to \"Floyd-Steinberg format");
        btn.addActionListener(e -> openFloydSteinbergForm());
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(btn);
    }


    void addFunnyColors() {
        JButton btn = new JButton("Disering without error");
        setDimensionBtn(btn, new Dimension(btnSize, btnSize));
        btn.setToolTipText("Convert loaded image to Floyd-Steinberg format without error's distribute");
        btn.addActionListener(e -> convertToFunnyColors());
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(btn);
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
    void convertToFunnyColors(){imagePanel.convertToFunnyColors();}

    public void openFloydSteinbergForm() {
        JFrame frame = new JFrame("Гамма-коррекция");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 250); // Увеличили размер для удобства
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JPanel paramPanel = new JPanel();
        paramPanel.setLayout(new BoxLayout(paramPanel, BoxLayout.Y_AXIS));
        paramPanel.setBorder(BorderFactory.createTitledBorder("Выберите параметр:"));

        ButtonGroup paramGroup = new ButtonGroup();
        JRadioButton param1Button = new JRadioButton("Дизеринг по всему цвету пикселя", true);
        JRadioButton param2Button = new JRadioButton("Дизеринг по каждому компоненту отдельно (красному, синему и зеленому)");

        paramGroup.add(param1Button);
        paramGroup.add(param2Button);

        paramPanel.add(param1Button);
        paramPanel.add(param2Button);

        JButton applyButton = new JButton("Применить");
        applyButton.addActionListener(e -> {
            boolean byFullPixel = param1Button.isSelected() ? true : false;
            imagePanel.convertToFloydSteinberg(byFullPixel);
            frame.dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            System.out.println("Выбор отменён (кнопка Cancel)");
            frame.dispose();
        });

        frame.add(paramPanel);
        frame.add(Box.createVerticalStrut(10));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        frame.add(buttonPanel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void openGammaForm() {
        JFrame frame = new JFrame("Гамма-коррекция");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 250); // Увеличили размер для удобства
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JSlider gammaSlider = new JSlider(1, 100, 10); // 10 = 1.0 по умолчанию
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = 1; i <= 100; i += 10) { // Метки каждые 1.0
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

        JTextField gammaField = new JTextField("1.0");
        gammaField.setHorizontalAlignment(JTextField.CENTER);
        gammaField.setMaximumSize(new Dimension(100, 25));

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

        JButton applyButton = new JButton("Применить");
        applyButton.addActionListener(e -> {
            double gamma = gammaSlider.getValue() / 10.0;
            imagePanel.convertToGamma(gamma);
            frame.dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            System.out.println("Выбор отменён (кнопка Cancel)");
            frame.dispose();
        });

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(new JLabel("Выберите параметр γ (0.1 - 10.0):"));
        controlPanel.add(gammaSlider);
        controlPanel.add(valueLabel);
        controlPanel.add(new JLabel("Или введите вручную:"));
        controlPanel.add(gammaField);

        frame.add(controlPanel);
        frame.add(Box.createVerticalStrut(10));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        frame.add(buttonPanel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    enum EdgeDetectionType {
        ROBERTS, SOBEL
    }

    public void openEdgeDetectionForm() {
        JFrame frame = new JFrame("Выделение границ");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300); // Увеличили высоту для нового элемента
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        ButtonGroup typeGroup = new ButtonGroup();
        JRadioButton robertsButton = new JRadioButton("Оператор Робертса", true);
        JRadioButton sobelButton = new JRadioButton("Оператор Собеля");
        typeGroup.add(robertsButton);
        typeGroup.add(sobelButton);

        JSlider thresholdSlider = new JSlider(0, 255, 50);
        thresholdSlider.setMajorTickSpacing(50);
        thresholdSlider.setPaintTicks(true);
        thresholdSlider.setPaintLabels(true);
        JLabel thresholdLabel = new JLabel("Порог: 50");

        JTextField thresholdField = new JTextField("50");
        thresholdField.setHorizontalAlignment(JTextField.CENTER);
        thresholdField.setMaximumSize(new Dimension(100, 25));

        thresholdSlider.addChangeListener(e -> {
            int value = thresholdSlider.getValue();
            thresholdLabel.setText("Порог: " + value);
            thresholdField.setText(String.valueOf(value));
        });

        thresholdField.addActionListener(e -> {
            try {
                int value = Integer.parseInt(thresholdField.getText());
                value = Math.max(0, Math.min(255, value)); // Ограничение 0-255
                thresholdSlider.setValue(value);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Введите целое число от 0 до 255",
                        "Ошибка ввода",
                        JOptionPane.ERROR_MESSAGE);
                thresholdField.setText(String.valueOf(thresholdSlider.getValue()));
            }
        });

        JButton applyButton = new JButton("Применить");
        applyButton.addActionListener(e -> {
            EdgeDetectionType type = robertsButton.isSelected() ?
                    EdgeDetectionType.ROBERTS : EdgeDetectionType.SOBEL;

            imagePanel.edgeDetectionConvert(type, thresholdSlider.getValue());
            frame.dispose();
        });

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> {
            System.out.println("Выбор отменён (кнопка Cancel)");
            frame.dispose();
        });

        // Компоновка
        frame.add(new JLabel("Выберите оператор:"));
        frame.add(robertsButton);
        frame.add(sobelButton);
        frame.add(Box.createVerticalStrut(10));

        frame.add(new JLabel("Порог бинаризации (0-255):"));
        frame.add(thresholdSlider);
        frame.add(thresholdLabel);

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.add(new JLabel("Ручной ввод:"));
        textFieldPanel.add(thresholdField);
        frame.add(textFieldPanel);

        frame.add(Box.createVerticalStrut(10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        frame.add(buttonPanel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void openGausseForm() {
        JFrame frame = new JFrame("Настройки Гауссова размытия");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 250);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Выбор отменён (закрытие окна)");
            }
        });

        ButtonGroup matrixGroup = new ButtonGroup();
        JRadioButton matrix3x3 = new JRadioButton("Матрица 3×3", true);
        JRadioButton matrix5x5 = new JRadioButton("Матрица 5×5");
        JRadioButton matrix7x7 = new JRadioButton("Матрица 7×7");
        JRadioButton matrix9x9 = new JRadioButton("Матрица 9×9");
        JRadioButton matrix11x11 = new JRadioButton("Матрица 11×11");
        matrixGroup.add(matrix3x3);
        matrixGroup.add(matrix5x5);
        matrixGroup.add(matrix7x7);
        matrixGroup.add(matrix9x9);
        matrixGroup.add(matrix11x11);

        JButton applyButton = new JButton("Применить");
        applyButton.addActionListener(e -> {
            int size = 3;
            if (matrix5x5.isSelected())
                size = 5;
            if (matrix7x7.isSelected())
                size = 7;
            if (matrix9x9.isSelected())
                size = 9;
            if (matrix11x11.isSelected())
                size = 11;
            JOptionPane.showMessageDialog(frame,
                    "Выбрана матрица " + size + "×" + size,
                    "Настройки применены",
                    JOptionPane.INFORMATION_MESSAGE);

            imagePanel.convertPixelByGausse(size);
            frame.dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            System.out.println("Выбор отменён (кнопка Cancel)");
            frame.dispose();
        });

        frame.add(new JLabel("Выберите размер матрицы:"));
        frame.add(matrix3x3);
        frame.add(matrix5x5);
        frame.add(matrix7x7);
        frame.add(matrix9x9);
        frame.add(matrix11x11);
        frame.add(applyButton);
        frame.add(cancelButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void openSharpForm() {
        JFrame frame = new JFrame("Настройки резкости");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // Группа для выбора размера матрицы
        ButtonGroup matrixGroup = new ButtonGroup();
        JRadioButton matrix3x3 = new JRadioButton("Матрица 3×3", true);
        JRadioButton matrix5x5 = new JRadioButton("Матрица 5×5");
        matrixGroup.add(matrix3x3);
        matrixGroup.add(matrix5x5);

        // Чекбокс для мягкого эффекта
        JCheckBox softEffectCheckBox = new JCheckBox("Мягкий эффект");

        // Кнопки действия
        JButton applyButton = new JButton("Применить");
        applyButton.addActionListener(e -> {
            int size = matrix3x3.isSelected() ? 3 : 5;
            boolean softEffect = softEffectCheckBox.isSelected();

            if (size == 3) {
                imagePanel.convertPixelBySharp3(softEffect);
            } else {
                imagePanel.convertPixelBySharp5(softEffect);
            }
            frame.dispose();
        });

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> frame.dispose());

        // Добавляем компоненты
        frame.add(new JLabel("Выберите размер матрицы:"));
        frame.add(matrix3x3);
        frame.add(matrix5x5);
        frame.add(Box.createVerticalStrut(10)); // Отступ
        frame.add(softEffectCheckBox);
        frame.add(Box.createVerticalStrut(10)); // Отступ
        frame.add(applyButton);
        frame.add(cancelButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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