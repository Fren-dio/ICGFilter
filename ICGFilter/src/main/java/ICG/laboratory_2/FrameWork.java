package ICG.laboratory_2;

import ICG.laboratory_2.Settings.MainWindowSettings;
import ICG.laboratory_2.Settings.SelectedSettings;

import java.awt.*;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FrameWork extends JFrame
{

	private ImagePanel imagePanel;
	private SelectedSettings selectedSettings;
	private ToolBarMenu toolBarMenu;
	private JRadioButtonMenuItem straightLineMenuItem;
	private JRadioButtonMenuItem wavedLineMenuItem ;
	private JRadioButtonMenuItem funModeMenuItem;
	private JRadioButtonMenuItem fillModeMenuItem;

	private JRadioButtonMenuItem triangleMenuItem;
	private JRadioButtonMenuItem squareMenuItem;
	private JRadioButtonMenuItem fivePolygonMenuItem;
	private JRadioButtonMenuItem sixPolygonMenuItem;
	private JRadioButtonMenuItem otherPolygonMenuItem;
	private JRadioButtonMenuItem starMenuItem;
	private JRadioButtonMenuItem redMenuItem;
	private JRadioButtonMenuItem greenMenuItem;
	private JRadioButtonMenuItem blueMenuItem;
	private JRadioButtonMenuItem yellowMenuItem;
	private JRadioButtonMenuItem purpleMenuItem;
	private JRadioButtonMenuItem laureateMenuItem;
	private JRadioButtonMenuItem blackMenuItem;
	private JRadioButtonMenuItem whiteMenuItem;
	private JRadioButtonMenuItem otherMenuItem;
	private JRadioButtonMenuItem lightBoldMenuItem;
	private JRadioButtonMenuItem mediumBoldMenuItem;
	private JRadioButtonMenuItem hardBoldMenuItem;
	private JRadioButtonMenuItem otherBoldMenuItem;

	public static void main(String[] args) {
		new FrameWork();
	}
	
	FrameWork()
	{
		super("ICG test");

		//set window's size
		int minWindowWidth = 640;
		int minWindowHeight = 480;
		int windowWidth = 640+300;
		int windowHeight = 480+200;

		this.setPreferredSize(new Dimension(windowWidth, windowHeight));
		this.setSize(new Dimension(windowWidth, windowHeight));
		this.setMinimumSize(new Dimension(minWindowWidth, minWindowHeight));
		this.setResizable(true);

		//set window's position
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		MainWindowSettings mainWindowSettings = new MainWindowSettings();

		mainWindowSettings.setSize(new Dimension(windowWidth, windowHeight-115));

		this.setLocation((int)((screenSize.getWidth()- windowWidth)/2), (int)((screenSize.getHeight()- windowHeight)/2));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		//set image panel
		SelectedSettings defaultColor = new SelectedSettings(Color.black);
		this.selectedSettings = defaultColor;
		ImagePanel p = new ImagePanel(defaultColor, mainWindowSettings);
		this.imagePanel = p;
		add(p);

		// Обертывание ImagePanel в JScrollPane
		JScrollPane scrollPane = new JScrollPane(imagePanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		// Добавление JScrollPane в окно
		add(scrollPane, BorderLayout.CENTER);

		//set inform menuJScrollPane
		//addMainMenu();

		// set tool bar menu
		BoxLayoutUtils blUtils = new BoxLayoutUtils();
		JPanel utilsPanel = blUtils.createHorizontalPanel();
		//toolBarMenu = new ToolBarMenu(p, defaultColor, this);
		//utilsPanel.add(toolBarMenu);
		getContentPane().add(utilsPanel, "North");
		
		pack();
		setVisible(true);
	}

	private void showAboutDialog() {
		String aboutMessage =
				"Программа: Графический редактор\n" +
						"Версия: 1.0\n" +
						"Автор: Кулишова Анастасия\n\n" +
						"Основные особенности:\n" +
						"- Редактор позволяет рисовать линии, фигуры и закрашивать области.\n" +
						"- Редактор позволяет изменять цвет и толщину линий.\n" +
						"- Редактор позволяет сохранять и открывать изображений.\n\n" +
						"Подсказки:\n" +
						"- Сохраняйте свои работы и открывайте изображения с помощью меню 'File'\n" +
						"- Выбирайте режим рисования (линия/заливка/т.д.) с помощью 'Modes'\n" +
						"- Выбирайте цвет для рисования с помощью 'Colors'\n" +
						"- Выбирайте толщину линий с помощью 'Bolds'\n" +
						"- Выбирайте шаблон для рисования фигуры с помощью 'Patterns'\n" +
						"- Очищайте изображение с помощью 'Clean'.";

		JOptionPane.showMessageDialog(
				this, // Родительское окно
				aboutMessage, // Сообщение
				"О программе", // Заголовок окна
				JOptionPane.INFORMATION_MESSAGE // Тип сообщения
		);
	}

	public void addMainMenu() {
		JMenuBar menuBar = new JMenuBar();

		MainMenuPanel aboutMenu = new MainMenuPanel("About");
		aboutMenu.addMenuItem("About", () -> showAboutDialog());

		MainMenuPanel fileMenu = new MainMenuPanel("File");
		fileMenu.addMenuItem("Open", () -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Open File");
			fileChooser.setCurrentDirectory(new File("C:/Users/kulma/IdeaProjects/ICG_Paint/images"));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg", "bmp", "gif"));
			int userSelection = fileChooser.showOpenDialog(this);
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File fileToOpen = fileChooser.getSelectedFile();
				imagePanel.openImage(fileToOpen.getAbsolutePath());
			}
		});

		fileMenu.setToolTipText("Actions and settings with file");
		fileMenu.addMenuItem("Save", () -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Save As");
			fileChooser.setCurrentDirectory(new File("C:/Users/kulma/IdeaProjects/ICG_Paint/images"));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
			int userSelection = fileChooser.showSaveDialog(this);
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File fileToSave = fileChooser.getSelectedFile();
				String filePath = fileToSave.getAbsolutePath();
				if (!filePath.toLowerCase().endsWith(".png")) {
					filePath += ".png";
				}
				imagePanel.saveAsImage(filePath, "png");
			}
		});

		fileMenu.addSeparator();
		fileMenu.addMenuItem("Exit", () -> System.exit(0));

		JMenu modesMenu = new JMenu("Modes");
		ButtonGroup modesGroup = new ButtonGroup();
		straightLineMenuItem = new JRadioButtonMenuItem("Straight Line");
		modesGroup.add(straightLineMenuItem);
		wavedLineMenuItem = new JRadioButtonMenuItem("Waved Line");
		modesGroup.add(wavedLineMenuItem);
		funModeMenuItem = new JRadioButtonMenuItem("Fun Mode");
		modesGroup.add(funModeMenuItem);
		fillModeMenuItem = new JRadioButtonMenuItem("Fill Mode");
		modesGroup.add(fillModeMenuItem);
		straightLineMenuItem.addActionListener(e -> {
			selectedSettings.setStraightLineMode();
			toolBarMenu.pressLineBtn();
			if (toolBarMenu.lineBtnFlag)
				straightLineMenuItem.setSelected(true);
		});
		wavedLineMenuItem.addActionListener(e -> {
			selectedSettings.setWavedLineMode();
			toolBarMenu.pressWavedBtn();
			if (toolBarMenu.wavedBtnFlag)
				wavedLineMenuItem.setSelected(true);
		});
		funModeMenuItem.addActionListener(e -> {
			selectedSettings.setFunPatternMode();
			toolBarMenu.pressFunBtn();
			if (toolBarMenu.funBtnFlag)
				funModeMenuItem.setSelected(true);
		});
		fillModeMenuItem.addActionListener(e -> {
			selectedSettings.setFillMode();
			toolBarMenu.pressFillBtn();
			if (toolBarMenu.fillBtnFlag)
				fillModeMenuItem.setSelected(true);
		});
		straightLineMenuItem.setSelected(true);

		modesMenu.add(straightLineMenuItem);
		modesMenu.add(wavedLineMenuItem);
		modesMenu.add(funModeMenuItem);
		modesMenu.add(fillModeMenuItem);

		JMenu colorsMenu = new JMenu("Colors");
		ButtonGroup colorsGroup = new ButtonGroup();
		redMenuItem = new JRadioButtonMenuItem("Red");
		colorsGroup.add(redMenuItem);
		greenMenuItem = new JRadioButtonMenuItem("Green");
		colorsGroup.add(greenMenuItem);
		blueMenuItem = new JRadioButtonMenuItem("Blue");
		colorsGroup.add(blueMenuItem);
		yellowMenuItem = new JRadioButtonMenuItem("Yellow");
		colorsGroup.add(yellowMenuItem);
		purpleMenuItem = new JRadioButtonMenuItem("Purple");
		colorsGroup.add(purpleMenuItem);
		laureateMenuItem = new JRadioButtonMenuItem("Laureate");
		colorsGroup.add(laureateMenuItem);
		blackMenuItem = new JRadioButtonMenuItem("Black");
		colorsGroup.add(blackMenuItem);
		whiteMenuItem = new JRadioButtonMenuItem("White");
		colorsGroup.add(whiteMenuItem);
		otherMenuItem = new JRadioButtonMenuItem("Other");
		colorsGroup.add(otherMenuItem);

		redMenuItem.addActionListener(e -> {
			selectedSettings.setCurrentColor(new Color(255, 0, 0));
			toolBarMenu.setCurrenColor(new Color(255, 0, 0));
		});
		greenMenuItem.addActionListener(e -> {
			selectedSettings.setCurrentColor(new Color(0, 255, 0));
			toolBarMenu.setCurrenColor(new Color(0, 255, 0));
		});
		blueMenuItem.addActionListener(e -> {
			selectedSettings.setCurrentColor(new Color(0, 0, 255));
			toolBarMenu.setCurrenColor(new Color(0, 0, 255));
		});
		yellowMenuItem.addActionListener(e -> {
			selectedSettings.setCurrentColor(new Color(255, 255, 0));
			toolBarMenu.setCurrenColor(new Color(255, 255, 0));
		});
		purpleMenuItem.addActionListener(e -> {
			selectedSettings.setCurrentColor(new Color(255, 0, 255));
			toolBarMenu.setCurrenColor(new Color(255, 0, 255));
		});
		laureateMenuItem.addActionListener(e -> {
			selectedSettings.setCurrentColor(new Color(0, 255, 255));
			toolBarMenu.setCurrenColor(new Color(0, 255, 255));
		});
		blackMenuItem.addActionListener(e -> {
			selectedSettings.setCurrentColor(new Color(0, 0, 0));
			toolBarMenu.setCurrenColor(new Color(0, 0, 0));
		});
		whiteMenuItem.addActionListener(e -> {
			selectedSettings.setCurrentColor(new Color(255, 255, 255));
			toolBarMenu.setCurrenColor(new Color(255, 255, 255));
		});
		otherMenuItem.addActionListener(e -> {
			Color selectedColor = JColorChooser.showDialog(this, "Choose a Color", null);
			if (selectedColor != null) {
				selectedSettings.setCurrentColor(selectedColor);
				toolBarMenu.setCurrenColor(selectedColor);
			}
		});

		colorsMenu.add(redMenuItem);
		colorsMenu.add(greenMenuItem);
		colorsMenu.add(blueMenuItem);
		colorsMenu.add(yellowMenuItem);
		colorsMenu.add(purpleMenuItem);
		colorsMenu.add(laureateMenuItem);
		colorsMenu.add(blackMenuItem);
		colorsMenu.add(whiteMenuItem);
		colorsMenu.add(otherMenuItem);

		JMenu boldsMenu = new JMenu("Bolds");
		ButtonGroup boldsGroup = new ButtonGroup();
		lightBoldMenuItem = new JRadioButtonMenuItem("Light");
		boldsGroup.add(lightBoldMenuItem);
		mediumBoldMenuItem = new JRadioButtonMenuItem("Medium");
		boldsGroup.add(mediumBoldMenuItem);
		hardBoldMenuItem = new JRadioButtonMenuItem("Bold");
		boldsGroup.add(hardBoldMenuItem);
		otherBoldMenuItem = new JRadioButtonMenuItem("Other");
		boldsGroup.add(otherBoldMenuItem);
		lightBoldMenuItem.addActionListener(e -> {
			selectedSettings.setLightWeight();
			toolBarMenu.presslightBoldBtn();
		});
		mediumBoldMenuItem.addActionListener(e -> {
			selectedSettings.setMediumWeight();
			toolBarMenu.pressMediumBoldBtn();
		});
		hardBoldMenuItem.addActionListener(e -> {
			selectedSettings.setBoldWeight();
			toolBarMenu.pressHardBoldBtn();
		});
		otherBoldMenuItem.addActionListener(e -> {
			int weight = toolBarMenu.showBoldsDialog();
			if (weight >= 1) {
				selectedSettings.setWeight(weight);
			}
			toolBarMenu.pressOtherBoldBtn();
		});
		lightBoldMenuItem.setSelected(true);

		boldsMenu.add(lightBoldMenuItem);
		boldsMenu.add(mediumBoldMenuItem);
		boldsMenu.add(hardBoldMenuItem);
		boldsMenu.add(otherBoldMenuItem);

		JMenu patternsMenu = new JMenu("Patterns");
		ButtonGroup patternsGroup = new ButtonGroup();
		triangleMenuItem = new JRadioButtonMenuItem("Triangle");
		patternsGroup.add(triangleMenuItem);
		squareMenuItem = new JRadioButtonMenuItem("Square");
		patternsGroup.add(squareMenuItem);
		fivePolygonMenuItem = new JRadioButtonMenuItem("Five polygon");
		patternsGroup.add(fivePolygonMenuItem);
		sixPolygonMenuItem = new JRadioButtonMenuItem("Six polygon");
		patternsGroup.add(sixPolygonMenuItem);
		otherPolygonMenuItem = new JRadioButtonMenuItem("Other polygon");
		patternsGroup.add(otherPolygonMenuItem);
		starMenuItem = new JRadioButtonMenuItem("Star");
		patternsGroup.add(starMenuItem);

		triangleMenuItem.addActionListener(e -> {
			int corners = 3;
			ToolBarMenu toolBarMenu = new ToolBarMenu(this.imagePanel, this.selectedSettings, this);
			toolBarMenu.createChosenWindow(corners, "Triangle Settings");
			selectedSettings.setFigurePatternMode(corners);
			toolBarMenu.pressTriangleBtn();
		});
		squareMenuItem.addActionListener(e -> {
			int corners = 4;
			ToolBarMenu toolBarMenu = new ToolBarMenu(this.imagePanel, this.selectedSettings, this);
			toolBarMenu.createChosenWindow(corners, "Square Settings");
			selectedSettings.setFigurePatternMode(corners);
			toolBarMenu.pressFourBtn();
		});
		fivePolygonMenuItem.addActionListener(e -> {
			int corners = 5;
			ToolBarMenu toolBarMenu = new ToolBarMenu(this.imagePanel, this.selectedSettings, this);
			toolBarMenu.createChosenWindow(corners, "Five polygon Settings");
			selectedSettings.setFigurePatternMode(corners);
			toolBarMenu.pressFiveBtn();
		});
		sixPolygonMenuItem.addActionListener(e -> {
			int corners = 6;
			ToolBarMenu toolBarMenu = new ToolBarMenu(this.imagePanel, this.selectedSettings, this);
			toolBarMenu.createChosenWindow(corners, "Six polygon Settings");
			selectedSettings.setFigurePatternMode(corners);
			toolBarMenu.pressSixBtn();
		});
		otherPolygonMenuItem.addActionListener(e -> {
			ToolBarMenu toolBarMenu = new ToolBarMenu(this.imagePanel, this.selectedSettings, this);
			int corners = toolBarMenu.showPolygonCornersDialog();
			if (corners != -1) {
				toolBarMenu.createChosenWindow(corners, "Polygon Settings");
				selectedSettings.setFigurePatternMode(corners);
			}
		});
		starMenuItem.addActionListener(e -> {
			ToolBarMenu toolBarMenu = new ToolBarMenu(this.imagePanel, this.selectedSettings, this);
			toolBarMenu.createChosenWindowForStar(5, "Star's Settings");
		});

		patternsMenu.add(triangleMenuItem);
		patternsMenu.add(squareMenuItem);
		patternsMenu.add(fivePolygonMenuItem);
		patternsMenu.add(sixPolygonMenuItem);
		patternsMenu.add(otherPolygonMenuItem);
		patternsMenu.add(starMenuItem);

		MainMenuPanel clearMenu = new MainMenuPanel("Clean");
		clearMenu.addMenuItem("Clean", () -> {
			imagePanel.clear();
			toolBarMenu.pressCleanBtn();
		});

		menuBar.add(aboutMenu);
		menuBar.add(fileMenu);
		menuBar.add(modesMenu);
		menuBar.add(colorsMenu);
		menuBar.add(boldsMenu);
		menuBar.add(patternsMenu);
		menuBar.add(clearMenu);

		setJMenuBar(menuBar);
	}

	public void setStraightLineMenuItemSelected(boolean selected) {
		straightLineMenuItem.setSelected(selected);
	}

	public void setWavedLineMenuItemSelected(boolean selected) {
		wavedLineMenuItem.setSelected(selected);
	}

	public void setFunModeMenuItemSelected(boolean selected) {
		funModeMenuItem.setSelected(selected);
	}

	public void setFillModeMenuItemSelected(boolean selected) {
		fillModeMenuItem.setSelected(selected);
	}
	public void setRedMenuItemSelected(boolean selected) {
		redMenuItem.setSelected(selected);
	}

	public void setGreenMenuItemSelected(boolean selected) {
		greenMenuItem.setSelected(selected);
	}

	public void setBlueMenuItemSelected(boolean selected) {
		blueMenuItem.setSelected(selected);
	}

	public void setYellowMenuItemSelected(boolean selected) {
		yellowMenuItem.setSelected(selected);
	}

	public void setPurpleMenuItemSelected(boolean selected) {
		purpleMenuItem.setSelected(selected);
	}

	public void setLaureateMenuItemSelected(boolean selected) {
		laureateMenuItem.setSelected(selected);
	}

	public void setBlackMenuItemSelected(boolean selected) {
		blackMenuItem.setSelected(selected);
	}

	public void setWhiteMenuItemSelected(boolean selected) {
		whiteMenuItem.setSelected(selected);
	}

	public void setOtherMenuItemSelected(boolean selected) {
		otherMenuItem.setSelected(selected);
	}

	public void setLightBoldMenuItemSelected(boolean selected) {
		lightBoldMenuItem.setSelected(selected);
	}

	public void setMediumBoldMenuItemSelected(boolean selected) {
		mediumBoldMenuItem.setSelected(selected);
	}

	public void setHardBoldMenuItemSelected(boolean selected) {
		hardBoldMenuItem.setSelected(selected);
	}

	public void setOtherBoldMenuItemSelected(boolean selected) {
		otherBoldMenuItem.setSelected(selected);
	}

	public void setTriangleMenuItemSelected(boolean selected) {
		triangleMenuItem.setSelected(selected);
	}

	public void setSquareMenuItemSelected(boolean selected) {
		squareMenuItem.setSelected(selected);
	}

	public void setFivePolygonMenuItemSelected(boolean selected) {
		fivePolygonMenuItem.setSelected(selected);
	}

	public void setSixPolygonMenuItemSelected(boolean selected) {
		sixPolygonMenuItem.setSelected(selected);
	}

	public void setOtherPolygonMenuItemSelected(boolean selected) {
		otherPolygonMenuItem.setSelected(selected);
	}

	public void setStarMenuItemSelected(boolean selected) {
		starMenuItem.setSelected(selected);
	}

}
