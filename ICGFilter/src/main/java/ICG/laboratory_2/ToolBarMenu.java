package ICG.laboratory_2;


import ICG.laboratory_2.Settings.SavedFigureSettings;
import ICG.laboratory_2.Settings.SelectedSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ToolBarMenu extends JToolBar {

    private FrameWork frameWork;
    private SavedFigureSettings star;
    private SavedFigureSettings threeCorners;
    private SavedFigureSettings fourCorners;
    private SavedFigureSettings fiveCorners;
    private SavedFigureSettings sixCorners;
    private SavedFigureSettings polygon;

    public boolean lineBtnFlag;
    public boolean wavedBtnFlag;
    public boolean fillBtnFlag;
    public boolean funBtnFlag;
    public boolean cleanBtnFlag;
    public boolean lightWeightFlag;
    public boolean mediumWeightFlag;
    public boolean hardWeightFlag;
    public boolean otherWeightFlag;
    private final int btnSize = 40;

    private final ImagePanel imagePanel;
    private final SelectedSettings selectedSettings;


    JButton lineBtn;
    JButton wavedLineBtn;
    JButton fillBtn;
    JButton clearBtn;
    JButton funBtn;
    JButton lightWeightLineBtn;
    JButton mediumWeightLineBtn;
    JButton boldWeightLineBtn;
    JButton otherWeightLineBtn;
    JButton currentColorBtn;

    public void setCurrenColor(Color color) {
        currentColorBtn.setBackground(color);
    }

    public void pressFillBtn() {
        selectedSettings.setFillMode();
        setAllModesFlagFalse();
        fillBtnFlag = true;
        if (fillBtnFlag) {
            fillBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("pressed/filling_icon.jpg"))));
            frameWork.setFillModeMenuItemSelected(true);
        }
        else
            fillBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("filling_icon.jpg"))));
    }
    public void pressFunBtn() {
        selectedSettings.setFunPatternMode();
        setAllModesFlagFalse();
        funBtnFlag = true;
        if (funBtnFlag) {
            funBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("pressed/fun_icon.jpg"))));
            frameWork.setFunModeMenuItemSelected(true);
        }
        else
            funBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("fun_icon.jpg"))));
    }
    public void pressLineBtn() {
        selectedSettings.setStraightLineMode();
        setAllModesFlagFalse();
        lineBtnFlag = true;
        if (lineBtnFlag) {
            lineBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("pressed/line_icon.jpg"))));
            frameWork.setStraightLineMenuItemSelected(true);
        }
        else
            lineBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("line_icon.jpg"))));
    }
    public void pressWavedBtn() {
        selectedSettings.setWavedLineMode();
        setAllModesFlagFalse();
        wavedBtnFlag = true;
        if (wavedBtnFlag) {
            wavedLineBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("pressed/waved_line_icon.jpg"))));
            frameWork.setWavedLineMenuItemSelected(true);
        }
        else
            wavedLineBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("waved_line_icon.jpg"))));
    }
    public void pressCleanBtn() {
        imagePanel.clear();
        setAllModesFlagFalse();
        cleanBtnFlag = true;
        if (cleanBtnFlag) {
            clearBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("pressed/clear_icon.jpg"))));
        }
        else
            clearBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("clear_icon.jpg"))));

        clearBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("clear_icon.jpg"))));
    }

    public void setAllModesFlagFalse(){
        //setAllFigureFlagsFalse();
        lineBtnFlag = false;
        lineBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("line_icon.jpg"))));
        wavedBtnFlag = false;
        wavedLineBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("waved_line_icon.jpg"))));
        fillBtnFlag = false;
        fillBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("filling_icon.jpg"))));
        funBtnFlag = false;
        funBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("fun_icon.jpg"))));
        cleanBtnFlag = false;
        clearBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("clear_icon.jpg"))));
    }


    public void presslightBoldBtn() {
        setAllBoldsFlagFalse();
        lightWeightFlag = true;
        selectedSettings.setWeight(1);
        if (lightWeightFlag) {
            frameWork.setLightBoldMenuItemSelected(true);
            lightWeightLineBtn.setBackground(new Color(200, 200, 200));
            lightWeightLineBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("pressed/line_weight_light.jpg"))));
        }
        else
            lightWeightLineBtn.setBackground(new Color(255, 255, 255));
            lightWeightLineBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("line_weight_light.jpg"))));
    }
    public void pressMediumBoldBtn() {
        setAllBoldsFlagFalse();
        mediumWeightFlag = true;
        selectedSettings.setWeight(3);
        if (mediumWeightFlag) {
            frameWork.setMediumBoldMenuItemSelected(true);
            mediumWeightLineBtn.setBackground(new Color(200, 200, 200));
            mediumWeightLineBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("pressed/line_weight_medium.jpg"))));
        }
        else {
            mediumWeightLineBtn.setBackground(new Color(255, 255, 255));
            mediumWeightLineBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("line_weight_medium.jpg"))));
        }
    }
    public void pressHardBoldBtn() {
        hardWeightFlag = true;
        selectedSettings.setWeight(6);
        if (hardWeightFlag) {
            frameWork.setHardBoldMenuItemSelected(true);
            boldWeightLineBtn.setBackground(new Color(200, 200, 200));
            boldWeightLineBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("pressed/line_weight_bold.jpg"))));
        }
        else {
            boldWeightLineBtn.setBackground(new Color(255, 255, 255));
            boldWeightLineBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("line_weight_bold.jpg"))));
        }
    }
    public void pressOtherBoldBtn() {
        setAllBoldsFlagFalse();
        otherWeightFlag = true;
        if (otherWeightFlag) {
            frameWork.setOtherBoldMenuItemSelected(true);
            otherWeightLineBtn.setBackground(new Color(200, 200, 200));
        }
        else{
            otherWeightLineBtn.setBackground(new Color(255, 255, 255));
        }
    }

    public void setAllBoldsFlagFalse(){
        lightWeightFlag = false;
        lightWeightLineBtn.setBackground(new Color(255, 255, 255));
        lightWeightLineBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("line_weight_light.jpg"))));
        mediumWeightFlag = false;
        mediumWeightLineBtn.setBackground(new Color(255, 255, 255));
        mediumWeightLineBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("line_weight_medium.jpg"))));
        hardWeightFlag = false;
        boldWeightLineBtn.setBackground(new Color(255, 255, 255));
        boldWeightLineBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("line_weight_bold.jpg"))));
        otherWeightFlag = false;
        otherWeightLineBtn.setBackground(new Color(255,255,255));
    }

    public ToolBarMenu(ImagePanel imagePanel, SelectedSettings selectedSettings, FrameWork frameWork) {
        this.imagePanel = imagePanel;
        this.frameWork = frameWork;
        this.selectedSettings = selectedSettings;
        this.star = new SavedFigureSettings();
        this.threeCorners = new SavedFigureSettings();
        this.fourCorners = new SavedFigureSettings();
        this.fiveCorners = new SavedFigureSettings();
        this.sixCorners = new SavedFigureSettings();
        this.polygon = new SavedFigureSettings();

        setDefaultSettings();
        lineBtn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("pressed/line_icon.jpg"))));
        wavedLineBtn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("waved_line_icon.jpg"))));
        fillBtn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("filling_icon.jpg"))));
        clearBtn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("clear_icon.jpg"))));
        funBtn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("fun_icon.jpg"))));

        // Кнопка для прямой линии
        setSizeSquareBtn(lineBtn, btnSize);
        lineBtn.setToolTipText("Draw straight line.");
        lineBtn.addActionListener(e -> {
            pressLineBtn();
        });
        this.add(lineBtn);

        // Кнопка для кривой/волнистой линии
        setSizeSquareBtn(wavedLineBtn, btnSize);
        wavedLineBtn.setToolTipText("Draw waved line, with any rotate and length.");
        wavedLineBtn.addActionListener(e -> {
            pressWavedBtn();
        });
        this.add(wavedLineBtn);

        this.addSeparator();

        // Кнопка для заливки
        setSizeSquareBtn(fillBtn, btnSize);
        fillBtn.setToolTipText("Filling area by chosen color");
        fillBtn.addActionListener(ev -> {
            pressFillBtn();
        });
        this.add(fillBtn);

        // Кнопка для очистки
        setSizeSquareBtn(clearBtn, btnSize);
        clearBtn.setToolTipText("Clear all area.");
        clearBtn.addActionListener(ev -> {
            pressCleanBtn();
        });
        this.add(clearBtn);


        setSizeSquareBtn(funBtn, btnSize);
        funBtn.setToolTipText("Just press left mouse button and move your mouse.");
        funBtn.addActionListener(ev -> {
            pressFunBtn();
        });
        this.add(funBtn);


        this.addSeparator();

        // настройки для штампов
        JPanel patternsGrid = new JPanel();
        patternsGrid.setLayout(new BoxLayout(patternsGrid, BoxLayout.X_AXIS));
        createPatternsGrid(patternsGrid);
        this.add(patternsGrid);

        this.addSeparator();


        lightWeightLineBtn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("pressed/line_weight_light.jpg"))));
        mediumWeightLineBtn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("line_weight_medium.jpg"))));
        boldWeightLineBtn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("line_weight_bold.jpg"))));
        otherWeightLineBtn = new JButton("?");
        // panel with weight of line settings
        JPanel weightsGrid = new JPanel();
        weightsGrid.setLayout(new BoxLayout(weightsGrid, BoxLayout.Y_AXIS));
        lightWeightLineBtn.setBackground(new Color(255, 255, 255));
        lightWeightLineBtn.setToolTipText("Line weight = 1. 'Light' weight.");
        Dimension weightBtnSize = new Dimension(btnSize*2, btnSize/3);
        setDimensionBtn(lightWeightLineBtn, weightBtnSize);
        lightWeightLineBtn.addActionListener(e -> presslightBoldBtn());
        weightsGrid.add(lightWeightLineBtn);

        mediumWeightLineBtn.setBackground(new Color(255, 255, 255));
        mediumWeightLineBtn.setToolTipText("Line weight = 3. 'Medium' weight.");
        setDimensionBtn(mediumWeightLineBtn, weightBtnSize);
        mediumWeightLineBtn.addActionListener(e -> pressMediumBoldBtn());
        weightsGrid.add(mediumWeightLineBtn);

        boldWeightLineBtn.setBackground(new Color(255, 255, 255));
        boldWeightLineBtn.setToolTipText("Line weight = 6. 'Bold' weight.");
        setDimensionBtn(boldWeightLineBtn, weightBtnSize);
        boldWeightLineBtn.addActionListener(e -> pressHardBoldBtn());
        weightsGrid.add(boldWeightLineBtn);

        otherWeightLineBtn.setBackground(new Color(255, 255, 255));
        otherWeightLineBtn.setToolTipText("Set your own line weight.");
        setDimensionBtn(otherWeightLineBtn, weightBtnSize);
        otherWeightLineBtn.addActionListener(e -> {
            int weight = showBoldsDialog();
            if (weight >= 1) {
                selectedSettings.setWeight(weight);
            }
            pressOtherBoldBtn();
        });
        weightsGrid.add(otherWeightLineBtn);

        this.add(weightsGrid);
        this.addSeparator();

        currentColorBtn = new JButton("");
        setDimensionBtn(currentColorBtn, new Dimension(40, 40));
        currentColorBtn.setBackground(new Color(0, 0, 0));
        currentColorBtn.setToolTipText("Current color");
        this.add(currentColorBtn);

        // Панель для цветовых кнопок
        JPanel colorsGrid = new JPanel();
        colorsGrid.setLayout(new BoxLayout(colorsGrid, BoxLayout.Y_AXIS));

        // Первый ряд цветов
        JPanel row1 = new JPanel();
        row1.setLayout(new BoxLayout(row1, BoxLayout.X_AXIS));

        // Черный цвет
        JButton blackBtn = createColorButton(new Color(0, 0, 0));
        blackBtn.setToolTipText("Black color. RGB=(0,0,0)");
        blackBtn.addActionListener(e -> frameWork.setBlackMenuItemSelected(true));
        row1.add(blackBtn);

        // Красный цвет
        JButton redBtn = createColorButton(new Color(255, 0, 0));
        redBtn.setToolTipText("Red color. RGB=(255,0,0)");
        redBtn.addActionListener(e -> frameWork.setRedMenuItemSelected(true));
        row1.add(redBtn);

        // Желтый цвет
        JButton redGreenBtn = createColorButton(new Color(255, 255, 0));
        redGreenBtn.setToolTipText("Yellow color. RGB=(255,255,0)");
        redGreenBtn.addActionListener(e -> frameWork.setYellowMenuItemSelected(true));
        row1.add(redGreenBtn);

        // Зеленый цвет
        JButton greenBtn = createColorButton(new Color(0, 255, 0));
        greenBtn.setToolTipText("Green color. RGB=(0,255,0)");
        greenBtn.addActionListener(e -> frameWork.setGreenMenuItemSelected(true));
        row1.add(greenBtn);

        // Второй ряд цветов
        JPanel row2 = new JPanel();
        row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));

        // Голубой цвет
        JButton greenBlueBtn = createColorButton(new Color(0, 255, 255));
        greenBlueBtn.setToolTipText("Azure color. RGB=(0,255,255)");
        greenBlueBtn.addActionListener(e -> frameWork.setLaureateMenuItemSelected(true));
        row2.add(greenBlueBtn);

        // Синий цвет
        JButton blueBtn = createColorButton(new Color(0, 0, 255));
        blueBtn.setToolTipText("Blue color. RGB=(0,0,255)");
        blueBtn.addActionListener(e -> frameWork.setBlueMenuItemSelected(true));
        row2.add(blueBtn);

        // Пурпурный цвет
        JButton redBlueBtn = createColorButton(new Color(255, 0, 255));
        redBlueBtn.setToolTipText("Purple color. RGB=(255,0,255)");
        redBlueBtn.addActionListener(e -> frameWork.setPurpleMenuItemSelected(true));
        row2.add(redBlueBtn);

        // Белый цвет
        JButton whiteBtn = createColorButton(new Color(255, 255, 255));
        whiteBtn.setToolTipText("White color. RGB=(255,255,255)");
        whiteBtn.addActionListener(e -> frameWork.setWhiteMenuItemSelected(true));
        row2.add(whiteBtn);

        // Добавляем ряды в сетку
        colorsGrid.add(row1);
        colorsGrid.add(row2);
        this.add(colorsGrid);

        // Кнопка для выбора произвольного цвета
        JButton colorSelectBtn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("palitra_icon.jpg"))));
        colorSelectBtn.setToolTipText("Select color, what you want.");
        setDimensionBtn(colorSelectBtn, new Dimension(btnSize-20, btnSize-20));
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
                    frameWork.setOtherMenuItemSelected(true);
                    colorSelectBtn.setBackground(selectedColor);
                    currentColorBtn.setBackground(selectedColor);
                    selectedSettings.setCurrentColor(selectedColor);
                }
            }
        });
        this.add(colorSelectBtn);


        // Настройки панели инструментов
        this.setFloatable(false);
        this.setRollover(false);
        this.add(Box.createGlue());
    }

    public void setDefaultSettings() {
        star.setIsStar(true);
        star.setCorners(5);
        star.setSize(100);
        star.setRadius(50);
        star.setRotate(0);

        threeCorners.setIsStar(false);
        threeCorners.setRotate(0);
        threeCorners.setSize(100);
        threeCorners.setCorners(3);

        fourCorners.setIsStar(false);
        fourCorners.setRotate(0);
        fourCorners.setSize(100);
        fourCorners.setCorners(4);

        fiveCorners.setIsStar(false);
        fiveCorners.setRotate(0);
        fiveCorners.setSize(100);
        fiveCorners.setCorners(5);

        sixCorners.setIsStar(false);
        sixCorners.setRotate(0);
        sixCorners.setSize(100);
        sixCorners.setCorners(6);

        polygon.setIsStar(false);
        polygon.setRotate(0);
        polygon.setSize(100);
        polygon.setCorners(6);
    }

    int showParametrDialog(int lowBarrier, int topBarrier) {
        Integer selected = null;

        while (selected == null) {
            String input = JOptionPane.showInputDialog(
                    this,
                    "Введите толщину линии (от 1 до 100):",
                    "Bold of line",
                    JOptionPane.QUESTION_MESSAGE
            );

            // Если пользователь нажал "Отмена" или закрыл окно
            if (input == null) {
                break;
            }
            try {
                selected = Integer.parseInt(input);
                if (selected < lowBarrier || selected > topBarrier) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Please enter a number between 1 and 20.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE
                    );
                    selected = null;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid input. Please enter a valid integer.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        return (selected != null) ? selected : -1;
    }

    int showBoldsDialog() {
        Integer selected = null;

        while (selected == null) {
            String input = JOptionPane.showInputDialog(
                    this,
                    "Введите толщину линии (от 1 до 100):",
                    "Bold of line",
                    JOptionPane.QUESTION_MESSAGE
            );

            // Если пользователь нажал "Отмена" или закрыл окно
            if (input == null) {
                break;
            }
            try {
                selected = Integer.parseInt(input);
                if (selected < 1 || selected > 100) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Please enter a number between 1 and 20.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE
                    );
                    selected = null;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid input. Please enter a valid integer.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        return (selected != null) ? selected : -1;
    }



    public boolean triangleBtnFlag;
    public boolean squareBtnFlag;
    public boolean fiveBtnFlag;
    public boolean sixBtnFlag;
    public boolean starBtnFlag;
    public boolean polygonBtnFlag;

    private JButton triangleBtn;
    private JButton squareBtn;
    private JButton fiveBtn;
    private JButton sixBtn;
    private JButton starBtn;
    private JButton polygonBtn;

    private void setAllFigureFlagsFalse() {
        setAllModesFlagFalse();
        triangleBtnFlag = false;
        triangleBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("triangle_icon.jpg"))));
        squareBtnFlag = false;
        squareBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("square_icon.jpg"))));
        fiveBtnFlag = false;
        fiveBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("five_icon.jpg"))));
        sixBtnFlag = false;
        sixBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("six_icon.jpg"))));
        starBtnFlag = false;
        starBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("star_icon.jpg"))));
        polygonBtnFlag = false;
        polygonBtn.setBackground(new Color(255, 255, 255));
    }

    public void pressTriangleBtn() {
        selectedSettings.setFigureMode();
        setAllFigureFlagsFalse();
        triangleBtnFlag = true;
        if (triangleBtnFlag) {
            triangleBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("pressed/triangle_icon.jpg"))));
            frameWork.setTriangleMenuItemSelected(true);
        }
        else
            triangleBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("triangle_icon.jpg"))));
    }
    public void pressFourBtn() {
        selectedSettings.setFigureMode();
        setAllFigureFlagsFalse();
        squareBtnFlag = true;
        if (squareBtnFlag) {
            squareBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("pressed/square_icon.jpg"))));
            frameWork.setSquareMenuItemSelected(true);
        }
        else
            squareBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("square_icon.jpg"))));
    }
    public void pressFiveBtn() {
        selectedSettings.setFigureMode();
        setAllFigureFlagsFalse();
        fiveBtnFlag = true;
        if (fiveBtnFlag) {
            fiveBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("pressed/five_icon.jpg"))));
            frameWork.setSquareMenuItemSelected(true);
        }
        else
            fiveBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("five_icon.jpg"))));
    }
    public void pressSixBtn() {
        selectedSettings.setFigureMode();
        setAllFigureFlagsFalse();
        sixBtnFlag = true;
        if (sixBtnFlag) {
            sixBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("pressed/six_icon.jpg"))));
            frameWork.setSquareMenuItemSelected(true);
        }
        else
            sixBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("six_icon.jpg"))));
    }
    public void pressStarBtn() {
        selectedSettings.setFigureMode();
        setAllFigureFlagsFalse();
        starBtnFlag = true;
        if (starBtnFlag) {
            starBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("pressed/star_icon.jpg"))));
            frameWork.setSquareMenuItemSelected(true);
        }
        else
            starBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("star_icon.jpg"))));
    }
    public void pressPolygonBtn() {
        selectedSettings.setFigureMode();
        setAllFigureFlagsFalse();
        polygonBtnFlag = true;
        if (polygonBtnFlag) {
            polygonBtn.setBackground(new Color(200, 200, 200));
            frameWork.setSquareMenuItemSelected(true);
        }
        else
            polygonBtn.setBackground(new Color(255, 255, 255));
    }

    private void createPatternsGrid(JPanel patternsGrid) {
        triangleBtn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("triangle_icon.jpg"))));
        triangleBtn.setToolTipText("Draw triangle by chosen color and weight.");
        setSizeSquareBtn(triangleBtn, (int)(btnSize*0.85));
        triangleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Открываем диалог выбора
                int corners = 3;
                createChosenWindow(corners, "Triangle Settings");
                selectedSettings.setFigurePatternMode(corners);
                frameWork.setTriangleMenuItemSelected(true);
                pressTriangleBtn();
            }
        });
        triangleBtn.addActionListener(e -> pressTriangleBtn());
        patternsGrid.add(triangleBtn);

        squareBtn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("square_icon.jpg"))));
        squareBtn.setToolTipText("Draw square by chosen color and weight.");
        setSizeSquareBtn(squareBtn, (int)(btnSize*0.85));
        squareBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int corners = 4;
                createChosenWindow(corners, "Four corners Settings");
                selectedSettings.setFigurePatternMode(corners);
                frameWork.setSquareMenuItemSelected(true);
                pressFourBtn();
            }
        });
        patternsGrid.add(squareBtn);


        fiveBtn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("five_icon.jpg"))));
        fiveBtn.setToolTipText("Draw square by chosen color and weight.");
        setSizeSquareBtn(fiveBtn, (int)(btnSize*0.85));
        fiveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Открываем диалог выбора
                int corners = 5;
                createChosenWindow(corners, "Five corners Settings");
                selectedSettings.setFigurePatternMode(corners);
                frameWork.setFivePolygonMenuItemSelected(true);
                pressFiveBtn();
            }
        });
        patternsGrid.add(fiveBtn);

        sixBtn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("six_icon.jpg"))));
        sixBtn.setToolTipText("Draw square by chosen color and weight.");
        setSizeSquareBtn(sixBtn, (int)(btnSize*0.85));
        sixBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Открываем диалог выбора
                int corners = 6;
                createChosenWindow(corners, "Six corners Settings");
                selectedSettings.setFigurePatternMode(corners);
                frameWork.setSixPolygonMenuItemSelected(true);
                pressSixBtn();
            }
        });
        patternsGrid.add(sixBtn);

        starBtn = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("star_icon.jpg"))));
        starBtn.setToolTipText("Draw square by chosen color and weight.");
        setSizeSquareBtn(starBtn, (int)(btnSize*0.85));
        starBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Открываем диалог выбора
                int corners = 6;
                createChosenWindowForStar(corners, "Star corners Settings");
                selectedSettings.setFigurePatternMode(corners);
                frameWork.setStarMenuItemSelected(true);
            }
        });
        patternsGrid.add(starBtn);

        polygonBtn = new JButton("Polygon");
        polygonBtn.setToolTipText("Draw a polygon with chosen number of corners.");
        setSizeSquareBtn(polygonBtn, (int)(btnSize * 0.85));
        polygonBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imagePanel.starExampleMode = true;
                int corners = showPolygonCornersDialog();
                if (corners >= 3) {
                    createChosenWindow(corners, "Polygon Settings");
                    selectedSettings.setFigurePatternMode(corners);
                    frameWork.setOtherPolygonMenuItemSelected(true);
                }
                imagePanel.starExampleMode = false;
            }
        });
        patternsGrid.add(polygonBtn);
    }


    private JFrame createFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(
                (int) ((screenSize.getWidth() - width) / 2),
                (int) ((screenSize.getHeight() - height) / 2)
        );
        return frame;
    }

    private ImagePanel createFigurePanel(int width, int height, int corners) {
        ImagePanel panel = new ImagePanel(selectedSettings, 100, 0, corners);
        panel.setPreferredSize(new Dimension(width - 200, height));
        panel.setStarExampleValue(true);
        return panel;
    }

    private ImagePanel createFigurePanelForPolygon(int width, int height, int corners) {
        ImagePanel panel = new ImagePanel(selectedSettings, 100, 0, corners);
        panel.setPreferredSize(new Dimension(width - 200, height));
        panel.setPolygonExampleValue(true);
        return panel;
    }

    private JSlider createSlider(int min, int max, int value, int majorTick, int minorTick) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);
        slider.setMajorTickSpacing(majorTick);
        slider.setMinorTickSpacing(minorTick);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }

    private JTextField createTextField(JSlider slider) {
        JTextField textField = new JTextField(3);
        textField.setMaximumSize(new Dimension(50, 20));
        textField.setText(String.valueOf(slider.getValue()));
        return textField;
    }

    private JPanel createSliderPanel(String labelText, JTextField textField, JSlider slider) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(textField);
        panel.add(slider);
        return panel;
    }

    private void configureSliderListener(JSlider slider, JTextField textField, ImagePanel panel, String paramType) {
        slider.addChangeListener(e -> {
            int value = slider.getValue();
            textField.setText(String.valueOf(value));
            switch (paramType) {
                case "size" -> panel.setStarParamsSize(value);
                case "corners" -> panel.setStarParamsCorners(value);
                case "rotate" -> panel.setStarParamsRotate(value);
                case "radius" -> panel.setStarParamsRadius(value);
            }
            panel.repaint();
        });

        textField.addActionListener(e -> {
            try {
                int value = Integer.parseInt(textField.getText());
                if (value >= slider.getMinimum() && value <= slider.getMaximum()) {
                    slider.setValue(value);
                } else {
                    showErrorDialog("Value must be between " + slider.getMinimum() + " and " + slider.getMaximum());
                }
            } catch (NumberFormatException ex) {
                showErrorDialog("Please enter a valid integer.");
            }
        });
    }

    private void configureSliderListenerForPolygon(JSlider slider, JTextField textField, ImagePanel panel, String paramType) {
        slider.addChangeListener(e -> {
            int value = slider.getValue();
            textField.setText(String.valueOf(value));
            switch (paramType) {
                case "size" -> panel.setPolygonSize(value);
                case "rotate" -> panel.setPolygonRotate(value);
            }
            panel.repaint();
        });

        textField.addActionListener(e -> {
            try {
                int value = Integer.parseInt(textField.getText());
                if (value >= slider.getMinimum() && value <= slider.getMaximum()) {
                    slider.setValue(value);
                } else {
                    showErrorDialog("Value must be between " + slider.getMinimum() + " and " + slider.getMaximum());
                }
            } catch (NumberFormatException ex) {
                showErrorDialog("Please enter a valid integer.");
            }
        });
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }

    private void addComponentsToSettingsPanel(JPanel settingsPanel, JPanel sizePanel, JPanel cornersPanel, JPanel rotationPanel, JPanel radiusPanel, JButton applyButton, JButton okButton, JButton cancelButton) {
        settingsPanel.add(new JLabel("Choose size: "));
        settingsPanel.add(sizePanel);
        settingsPanel.add(new JLabel("Choose amount of corners:"));
        settingsPanel.add(cornersPanel);
        settingsPanel.add(new JLabel("Choose rotation:"));
        settingsPanel.add(rotationPanel);
        settingsPanel.add(new JLabel("Choose radius:"));
        settingsPanel.add(radiusPanel);

        settingsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(applyButton);
        btnPanel.add(okButton);
        btnPanel.add(cancelButton);
        settingsPanel.add(btnPanel);
    }

    private void addComponentsToSettingsPanelForPolygon(JPanel settingsPanel, JPanel sizePanel, JPanel rotationPanel, JButton applyButton, JButton okButton, JButton cancelButton) {
        settingsPanel.add(new JLabel("Choose size: "));
        settingsPanel.add(sizePanel);
        settingsPanel.add(new JLabel("Choose rotation:"));
        settingsPanel.add(rotationPanel);

        settingsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(applyButton);
        btnPanel.add(okButton);
        btnPanel.add(cancelButton);
        settingsPanel.add(btnPanel);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
    }

    int showPolygonCornersDialog() {
        Integer[] cornersOptions = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};

        Integer selectedCorners = (Integer) JOptionPane.showInputDialog(
                this,
                "Choose the number of corners:",
                "Polygon Corners",
                JOptionPane.QUESTION_MESSAGE,
                null,
                cornersOptions,
                cornersOptions[0]
        );

        return (selectedCorners != null) ? selectedCorners : -1;
    }

    private JButton createColorButton(Color color) {
        JButton button = new JButton("");
        setDimensionBtn(button, new Dimension(btnSize / 2, btnSize / 2));
        button.setBackground(color);

        button.addActionListener(e -> {
            selectedSettings.setCurrentColor(color);
            currentColorBtn.setBackground(color);
        });
        return button;
    }

    private void setSizeSquareBtn(JButton btn, int size) {
        btn.setPreferredSize(new Dimension(size, size));
        btn.setMinimumSize(new Dimension(size, size));
        btn.setMaximumSize(new Dimension(size, size ));

    }

    private JButton setDimensionBtn(JButton btn, Dimension dim) {
        btn.setPreferredSize(dim);
        btn.setMinimumSize(dim);
        btn.setMaximumSize(dim);

        return btn;
    }

    public void createChosenWindow(int corners, String formName) {
        int formSizeH = 380;
        int formSizeW = 500;

        if (corners == 3){
            if (!threeCorners.isApply()) {
                selectedSettings.setFigureSize(100);
                selectedSettings.setFigureRotate(0);
            }
        }
        if (corners == 4){
            if (!fourCorners.isApply()) {
                selectedSettings.setFigureSize(100);
                selectedSettings.setFigureRotate(0);
            }
        }
        if (corners == 5){
            if (!fiveCorners.isApply()) {
                selectedSettings.setFigureSize(100);
                selectedSettings.setFigureRotate(0);
            }
        }
        if (corners == 6){
            if (!sixCorners.isApply()) {
                selectedSettings.setFigureSize(100);
                selectedSettings.setFigureRotate(0);
            }
        }
        else {
            if (!polygon.isApply()) {
                selectedSettings.setFigureSize(100);
                selectedSettings.setFigureRotate(0);
            }
        }

        JFrame figureSetSettingsFrame = createFrame(formName, formSizeW, formSizeH);

        JPanel mainPanel = new JPanel(new BorderLayout());

        ImagePanel figurePanel = createFigurePanelForPolygon(formSizeW, formSizeH, corners);
        figurePanel.setPolygonExampleValue(true);
        figurePanel.setFigureParameters(100, 0, corners);
        figurePanel.drawExample();
        figurePanel.repaint();
        mainPanel.add(figurePanel, BorderLayout.CENTER);

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));

        JSlider sizeSlider = createSlider(0, 200, 100, 50, 25);
        if (corners == 3)
            if (threeCorners.isApply())
                sizeSlider.setValue(threeCorners.getSize());
        if (corners == 4)
            if (fourCorners.isApply())
                sizeSlider.setValue(fourCorners.getSize());
        if (corners == 5)
            if (fiveCorners.isApply())
                sizeSlider.setValue(fiveCorners.getSize());
        if (corners == 6) {
            if (sixCorners.isApply())
                sizeSlider.setValue(sixCorners.getSize());
        }
        else
            if (polygon.isApply())
                sizeSlider.setValue(polygon.getSize());
        JTextField sizeTextField = createTextField(sizeSlider);
        JPanel sizePanel = createSliderPanel("Choose size: ", sizeTextField, sizeSlider);
        configureSliderListenerForPolygon(sizeSlider, sizeTextField, figurePanel, "size");

        JSlider rotationSlider = createSlider(0, 1440, 0, 360, 180);
        if (corners == 3)
            if (threeCorners.isApply())
                rotationSlider.setValue(threeCorners.getRotate());
        if (corners == 4)
            if (fourCorners.isApply())
                rotationSlider.setValue(fourCorners.getRotate());
        if (corners == 5)
            if (fiveCorners.isApply())
                rotationSlider.setValue(fiveCorners.getRotate());
        if (corners == 6) {
            if (sixCorners.isApply())
                rotationSlider.setValue(sixCorners.getRotate());
        }
        else
            if (polygon.isApply())
                rotationSlider.setValue(polygon.getRotate());
        JTextField rotationTextField = createTextField(rotationSlider);
        JPanel rotationPanel = createSliderPanel("Choose rotation:", rotationTextField, rotationSlider);
        configureSliderListenerForPolygon(rotationSlider, rotationTextField, figurePanel, "rotate");

        JButton applyButton = createButton("Apply", e -> {
            if (corners == 3) {
                threeCorners.setIsStar(false);
                threeCorners.setApply();
                threeCorners.setCorners(corners);
                threeCorners.setSize(sizeSlider.getValue());
                threeCorners.setRotate(rotationSlider.getValue());
            }
            if (corners == 4) {
                fourCorners.setIsStar(false);
                fourCorners.setApply();
                fourCorners.setCorners(corners);
                fourCorners.setSize(sizeSlider.getValue());
                fourCorners.setRotate(rotationSlider.getValue());
            }
            if (corners == 5) {
                fiveCorners.setIsStar(false);
                fiveCorners.setApply();
                fiveCorners.setCorners(corners);
                fiveCorners.setSize(sizeSlider.getValue());
                fiveCorners.setRotate(rotationSlider.getValue());
            }
            if (corners == 6) {
                sixCorners.setIsStar(false);
                sixCorners.setApply();
                sixCorners.setCorners(corners);
                sixCorners.setSize(sizeSlider.getValue());
                sixCorners.setRotate(rotationSlider.getValue());
            }
            else {
                polygon.setIsStar(false);
                polygon.setApply();
                polygon.setCorners(corners);
                polygon.setSize(sizeSlider.getValue());
                polygon.setRotate(rotationSlider.getValue());
            }
            figurePanel.repaint();
        });
        JButton okButton = createButton("Ok", e -> {
            selectedSettings.setFigurePatternMode(corners);
            figureSetSettingsFrame.dispose();
        });
        JButton cancelButton = createButton("Cancel", e -> {
            if (corners == 3){
                if (!threeCorners.isApply()) {
                    selectedSettings.setFigureSize(100);
                    selectedSettings.setFigureRotate(0);
                }
            }
            if (corners == 4){
                if (!fourCorners.isApply()) {
                    selectedSettings.setFigureSize(100);
                    selectedSettings.setFigureRotate(0);
                }
            }
            if (corners == 5){
                if (!fiveCorners.isApply()) {
                    selectedSettings.setFigureSize(100);
                    selectedSettings.setFigureRotate(0);
                }
            }
            if (corners == 6){
                if (!sixCorners.isApply()) {
                    selectedSettings.setFigureSize(100);
                    selectedSettings.setFigureRotate(0);
                }
            }
            else {
                if (!polygon.isApply()) {
                    selectedSettings.setFigureSize(100);
                    selectedSettings.setFigureRotate(0);
                }
            }
            figureSetSettingsFrame.dispose();
        });

        addComponentsToSettingsPanelForPolygon(settingsPanel, sizePanel, rotationPanel, applyButton, okButton, cancelButton);

        mainPanel.add(settingsPanel, BorderLayout.EAST);
        figureSetSettingsFrame.add(mainPanel);
        figureSetSettingsFrame.setVisible(true);
    }



    public void createChosenWindowForStar(int corners, String formName) {
        int formSizeH = 380;
        int formSizeW = 500;

        if (!star.isApply()) {
            selectedSettings.setStarCorners(5);
            selectedSettings.setStarSize(100);
            selectedSettings.setStarRadius(50);
            selectedSettings.setFigureRotate(0);
        }

        JFrame figureSetSettingsFrame = createFrame(formName, formSizeW, formSizeH);

        JPanel mainPanel = new JPanel(new BorderLayout());

        ImagePanel figurePanel = createFigurePanel(formSizeW, formSizeH, corners);
        mainPanel.add(figurePanel, BorderLayout.CENTER);

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));

        JSlider sizeSlider = createSlider(0, 200, 100, 50, 25);
        if (star.isApply())
            sizeSlider.setValue(star.getSize());
        JTextField sizeTextField = createTextField(sizeSlider);
        JPanel sizePanel = createSliderPanel("Choose size: ", sizeTextField, sizeSlider);
        configureSliderListener(sizeSlider, sizeTextField, figurePanel, "size");

        JSlider cornersSlider = createSlider(0, 100, 5, 20, 10);
        if (star.isApply())
            cornersSlider.setValue(star.getCorners());
        JTextField cornersTextField = createTextField(cornersSlider);
        JPanel cornersPanel = createSliderPanel("Choose amount of corners:", cornersTextField, cornersSlider);
        configureSliderListener(cornersSlider, cornersTextField, figurePanel, "corners");

        JSlider rotationSlider = createSlider(0, 1440, 0, 360, 180);
        if (star.isApply())
            rotationSlider.setValue(star.getRotate());
        JTextField rotationTextField = createTextField(rotationSlider);
        JPanel rotationPanel = createSliderPanel("Choose rotation:", rotationTextField, rotationSlider);
        configureSliderListener(rotationSlider, rotationTextField, figurePanel, "rotate");

        JSlider radiusSlider = createSlider(0, 200, 50, 50, 25);
        if (star.isApply())
            radiusSlider.setValue(star.getRadius());
        JTextField radiusTextField = createTextField(radiusSlider);
        JPanel radiusPanel = createSliderPanel("Choose radius:", radiusTextField, radiusSlider);
        configureSliderListener(radiusSlider, radiusTextField, figurePanel, "radius");

        JButton applyButton = createButton("Apply", e -> {
            star.setIsStar(true);
            star.setApply();
            star.setCorners(cornersSlider.getValue());
            star.setSize(sizeSlider.getValue());
            star.setRadius(radiusSlider.getValue());
            star.setRotate(rotationSlider.getValue());
            figurePanel.repaint();
        });
        JButton okButton = createButton("Ok", e -> {
            selectedSettings.setStarMode();
            figureSetSettingsFrame.dispose();
        });
        JButton cancelButton = createButton("Cancel", e -> {
            selectedSettings.setStarCorners(5);
            selectedSettings.setStarSize(100);
            selectedSettings.setStarRadius(50);
            selectedSettings.setFigureRotate(0);
            figureSetSettingsFrame.dispose();
        });

        addComponentsToSettingsPanel(settingsPanel, sizePanel, cornersPanel, rotationPanel, radiusPanel, applyButton, okButton, cancelButton);

        mainPanel.add(settingsPanel, BorderLayout.EAST);
        figureSetSettingsFrame.add(mainPanel);
        figureSetSettingsFrame.setVisible(true);
    }


}