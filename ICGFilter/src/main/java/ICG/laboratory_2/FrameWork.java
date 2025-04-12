package ICG.laboratory_2;

import ICG.laboratory_2.Settings.MainWindowSettings;
import ICG.laboratory_2.Settings.SelectedSettings;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FrameWork extends JFrame {
	private ImagePanel imagePanel;
	private SelectedSettings selectedSettings;
	private ToolBarMenu toolBarMenu;
	private JScrollPane scrollPane;

	public static void main(String[] args) {
		new FrameWork();
	}

	FrameWork() {
		super("ICGFilter");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int minWindowWidth = 640;
		int minWindowHeight = 480;
		int windowWidth = (int) screenSize.getWidth();
		int windowHeight = (int) screenSize.getHeight();

		setPreferredSize(new Dimension(windowWidth, windowHeight));
		setSize(new Dimension(windowWidth, windowHeight));
		setMinimumSize(new Dimension(minWindowWidth, minWindowHeight));
		setResizable(true);

		MainWindowSettings mainWindowSettings = new MainWindowSettings();
		mainWindowSettings.setSize(new Dimension(windowWidth-30, windowHeight-115));

		setLocation(
				(int)((screenSize.getWidth() - windowWidth) / 2),
				(int)((screenSize.getHeight() - windowHeight) / 2)
		);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		SelectedSettings defaultColor = new SelectedSettings(Color.black);
		this.selectedSettings = defaultColor;
		this.imagePanel = new ImagePanel(defaultColor, mainWindowSettings, scrollPane, this);

		scrollPane.setViewportView(imagePanel);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				imagePanel.updateScrollBars();
			}
		});

		add(scrollPane, BorderLayout.CENTER);

		addMainMenu();


		BoxLayoutUtils blUtils = new BoxLayoutUtils();
		JPanel utilsPanel = blUtils.createHorizontalPanel();
		toolBarMenu = new ToolBarMenu(imagePanel, defaultColor, this);
		utilsPanel.add(toolBarMenu);
		getContentPane().add(utilsPanel, "North");



		pack();
		setVisible(true);
	}

	private void showAboutDialog() {
		String aboutMessage = "ICGFilter - графический редактор для обработки растровых изображений\n" +
				"\n" +
				"Автор: Кулишова Анастасия\n" +
				"\n" +
				"Функциональные возможности:\n" +
				"- Загрузка и сохранение изображений (PNG, JPEG, BMP, GIF)\n" +
				"- Два режима отображения: реальный размер и подгон под экран\n" +
				"- Базовые фильтры: черно-белое, негатив, гамма-коррекция\n" +
				"- Пространственные фильтры: размытие, повышение резкости, тиснение\n" +
				"- Выделение границ (операторы Робертса и Собеля)\n" +
				"- Алгоритмы дизеринга: Флойда-Стейнберга и упорядоченный\n" +
				"- Дополнительные эффекты: акварелизация, поворот изображения, дизеринг без распространения ошибки, мозаика\n" +
				"\n" +
				"Минимальное разрешение экрана: 640×480\n";
		JOptionPane.showMessageDialog(this, aboutMessage, "О программе", JOptionPane.INFORMATION_MESSAGE);
	}

	public void addMainMenu() {
		JMenuBar menuBar = new JMenuBar();

		MainMenuPanel aboutMenu = new MainMenuPanel("Help");
		aboutMenu.addMenuItem("About", this::showAboutDialog);

		MainMenuPanel fileMenu = new MainMenuPanel("File");
		fileMenu.addMenuItem("Open", () -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Open File");
			fileChooser.setCurrentDirectory(new File("images"));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg", "bmp", "gif"));
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				imagePanel.openImage(fileChooser.getSelectedFile().getAbsolutePath());
			}
		});

		fileMenu.addMenuItem("Save", () -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Save As");
			fileChooser.setCurrentDirectory(new File("images"));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				String filePath = fileChooser.getSelectedFile().getAbsolutePath();
				if (!filePath.toLowerCase().endsWith(".png")) filePath += ".png";
				imagePanel.saveAsImage(filePath, "png");
			}
		});

		fileMenu.addSeparator();
		fileMenu.addMenuItem("Exit", () -> System.exit(0));

		MainMenuPanel filtersMenu = new MainMenuPanel("Filters");
		filtersMenu.addMenuItem("1. Convert to BW", () -> {imagePanel.changeViewedImage("BW");});
		filtersMenu.addMenuItem("2. Convert to RGB", () -> {imagePanel.changeViewedImage("RGB");});
		filtersMenu.addMenuItem("3. Convert to inverse", () -> {imagePanel.changeViewedImage("INVERSE");});
		filtersMenu.addMenuItem("4. Convert to gausse blur", () -> {toolBarMenu.openGausseForm();});
		filtersMenu.addMenuItem("5. Convert to sharpness", () -> {toolBarMenu.openSharpForm();});
		filtersMenu.addMenuItem("6. Convert to emboss", () -> {toolBarMenu.openSharpForm();});
		filtersMenu.addMenuItem("7. Convert with gamma", () -> {toolBarMenu.openGammaForm();});
		filtersMenu.addMenuItem("8. Convert by Roberts and Sobel", () -> {toolBarMenu.openEdgeDetectionForm();});
        filtersMenu.addMenuItem("9. Convert to Floyd-Steinberg", () -> {toolBarMenu.openFloydSteinbergForm();});
        filtersMenu.addMenuItem("10. Convert to disering without error format", () -> {toolBarMenu.openConvertToFunnyColorsForm();});
        filtersMenu.addMenuItem("11. Convert to ordered dithering format", () -> {toolBarMenu.openConvertToOrderedDitheringForm();});
        filtersMenu.addMenuItem("12. Convert to acvarel format", () -> {toolBarMenu.convertToAcvarel();});

		MainMenuPanel clearMenu = new MainMenuPanel("Clean");
		clearMenu.addMenuItem("Clean", imagePanel::clear);

		menuBar.add(aboutMenu);
		menuBar.add(fileMenu);
		menuBar.add(filtersMenu);
		menuBar.add(clearMenu);

		setJMenuBar(menuBar);
	}
}