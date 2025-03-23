package ICG.laboratory_2;


import ICG.laboratory_2.Settings.MainWindowSettings;
import ICG.laboratory_2.Settings.SavedFigureSettings;
import ICG.laboratory_2.Settings.SelectedSettings;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImagePanel extends JPanel implements MouseListener {

	private JScrollPane scrollPane;
	private MainWindowSettings mainWindowSettings;
	private boolean draw = true;
	private int x, y, xd = -1, yd = -1;
	private BufferedImage currentImage;

	int DEFAULT_TRIANGLE_SIZE = 200;
	private SelectedSettings selectedSettings;
	private BufferedImage exampleImage;

	public Boolean starExampleMode;
	public Boolean polygonExampleMode;
	public Boolean exampleMode;
	private JLabel imageLabel;

	public ImagePanel() {
		starExampleMode = false;
		exampleMode = false;
		currentImage = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_ARGB);
		exampleImage = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_ARGB);
		imageLabel = new JLabel();
		scrollPane = new JScrollPane(imageLabel);
		clearExampleImage();
	}

	public ImagePanel(SelectedSettings selectedSettings, int figureSize, int figureRotate, int figureCorners) {
		imageLabel = new JLabel();
		scrollPane = new JScrollPane(imageLabel);
		starExampleMode = false;
		polygonExampleMode = false;
		exampleMode = false;
		this.selectedSettings = selectedSettings;
		currentImage = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_ARGB);
	}


	public void clear() {
		if (currentImage != null) {
			Graphics2D g2d = currentImage.createGraphics();
			g2d.setColor(new Color(230, 230, 230));
			g2d.fillRect(0, 0, currentImage.getWidth(), currentImage.getHeight());
			g2d.dispose();
			repaint();
		}
	}

	@Override
	public Dimension getPreferredSize() {
		if (currentImage != null) {
			return new Dimension(currentImage.getWidth(), currentImage.getHeight());
		}
		return super.getPreferredSize();
	}

	public ImagePanel(SelectedSettings selectedSettings, MainWindowSettings mainWindowSettings) {

		this.mainWindowSettings = mainWindowSettings;
		imageLabel = new JLabel();
		scrollPane = new JScrollPane(imageLabel);
		starExampleMode = false;
		polygonExampleMode = false;
		exampleMode = false;
		this.selectedSettings = selectedSettings;

		starExampleMode = false;
		exampleMode = false;
		currentImage = new BufferedImage(mainWindowSettings.getWeight(), mainWindowSettings.getHeight(), BufferedImage.TYPE_INT_ARGB);
		exampleImage = new BufferedImage(mainWindowSettings.getWeight(), mainWindowSettings.getHeight(), BufferedImage.TYPE_INT_ARGB);
		imageLabel = new JLabel();
		JScrollPane scrollPane = new JScrollPane(this);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		clearExampleImage();

		addMouseListener(this);

		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (selectedSettings.getCurrentMode().equals("WAVED_LINE_MODE")) {
					if (xd >= 0) {
						Graphics2D g2d = currentImage.createGraphics();
						g2d.setColor(selectedSettings.getCurrentColor());
						g2d.setStroke(new BasicStroke(selectedSettings.getCurrentWeight()));
						if ((selectedSettings.getCurrentWeight()) > 1)
							g2d.drawLine(xd, yd, e.getX(), e.getY());
						else bresenhemDrawLine(xd, yd, e.getX(), e.getY());
						g2d.dispose();
						repaint();
					}
					xd = e.getX();
					yd = e.getY();
				}
				if (selectedSettings.getCurrentMode().equals("FUN_MODE")) {
					drawFunMode(e);
				}
			}
		});

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				handleResize();
			}
		});
	}

	private void handleResize() {
		int newWidth = getWidth();
		int newHeight = getHeight();

		if (newWidth > currentImage.getWidth() || newHeight > currentImage.getHeight()) {
			BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = newImage.createGraphics();
			g2d.drawImage(currentImage, 0, 0, null);
			g2d.dispose();
			currentImage = newImage;
			setPreferredSize(new Dimension(newWidth, newHeight));
			revalidate();
		}

		// Проверяем, нужно ли показывать скроллбары
		checkScrollBars();
	}

	private void checkScrollBars() {
		if (currentImage == null) return;

		int imageWidth = currentImage.getWidth();
		int imageHeight = currentImage.getHeight();
		int panelWidth = getWidth();
		int panelHeight = getHeight();

		boolean needHorizontalScroll = (imageWidth > panelWidth);
		boolean needVerticalScroll = (imageHeight > panelHeight);

		scrollPane.setHorizontalScrollBarPolicy(needHorizontalScroll ? JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED : JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(needVerticalScroll ? JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED : JScrollPane.VERTICAL_SCROLLBAR_NEVER);
	}

	private void bresenhemDrawLine(int x0, int y0, int x1, int y1) {
		if (currentImage == null) return;

		Graphics2D g2d = currentImage.createGraphics();
		g2d.setColor((selectedSettings.getCurrentColor()));

		int x = x0;
		int y = y0;
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);

		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;

		int err = dx - dy;

		for (int i = 0; i <= Math.max(dx, dy); i++) {
			currentImage.setRGB(x, y, (selectedSettings.getCurrentColor()).getRGB());

			if (x == x1 && y == y1) break;

			int e2 = 2 * err;
			if (e2 > -dy) {
				err -= dy;
				x += sx;
			}
			if (e2 < dx) {
				err += dx;
				y += sy;
			}
		}

		g2d.dispose();
		repaint();
	}

	public void setFigureParameters(int figureSize, int figureRotate, int figureCorners) {
		drawExampleFigure();
	}



	private void clearExampleImage() {
		Graphics2D g2d = exampleImage.createGraphics();
		g2d.setColor(getBackground());
		g2d.fillRect(0, 0, exampleImage.getWidth(), exampleImage.getHeight());
		g2d.dispose();
	}


	public void setStarExampleValue(boolean value){
		this.starExampleMode = value;
	}


	public void setPolygonExampleValue(boolean value){
		this.polygonExampleMode = value;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (currentImage != null) {
			g.drawImage(currentImage, 0, 0, this);
		}
		if (starExampleMode || polygonExampleMode) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			int centerX = getWidth() / 2;
			int centerY = getHeight() / 2;

			if (starExampleMode)
				drawStar(g2d, centerX, centerY, selectedSettings.getStarSize(),
					selectedSettings.getStarRadius(), selectedSettings.getStarCorners(), selectedSettings.getStarRotate());
			if (polygonExampleMode) {
				drawFigure(g2d, centerX, centerY, selectedSettings.getFigureSize(),
						selectedSettings.getFigureCorners(), selectedSettings.getFigureRotate());
			}
		}
		if (exampleMode) {
			super.paintComponent(g);
			drawExampleFigure();
		}
	}

	public void setPolygonSize(int value) {
		selectedSettings.setFigureSize(value);
	}

	public void setPolygonRotate(int value) {
		selectedSettings.setFigureRotate(value);
	}

	public void setStarParamsSize(int value) {
		selectedSettings.setStarSize(value);
	}


	public void setStarParamsRotate(int value) {
		selectedSettings.setStarRotate(value);
	}

	public void setStarParamsRadius(int value) {
		selectedSettings.setStarRadius(value);
	}

	public void setStarParamsCorners(int value) {
		selectedSettings.setStarCorners(value);
	}

	private void drawFigureGraphic(int currentX, int currentY, int size, int rotate, int corners) {

		Graphics2D g2d = this.currentImage.createGraphics();
		g2d.setColor(selectedSettings.getCurrentColor());
		g2d.setStroke(new BasicStroke(selectedSettings.getCurrentWeight()));

		if (currentImage == null) {
			System.out.println("currentImage is null");
			return;
		}

		int[] xPoints = new int[corners];
		int[] yPoints = new int[corners];

		double angleStep = 2 * Math.PI / corners;

		for (int i = 0; i < corners; i++) {
			double angle = i * angleStep;
			xPoints[i] = (int) (currentX + size / 2 * Math.cos(angle));
			yPoints[i] = (int) (currentY + size / 2 * Math.sin(angle));
		}

		int[] xPointsRotated = new int[xPoints.length];
		int[] yPointsRotated = new int[yPoints.length];

		double radians = Math.toRadians(rotate);
		for (int i = 0; i < xPoints.length; i++) {
			int x = xPoints[i] - currentX;
			int y = yPoints[i] - currentY;
			xPointsRotated[i] = (int) (x * Math.cos(radians) - y * Math.sin(radians) + currentX);
			yPointsRotated[i] = (int) (x * Math.sin(radians) + y * Math.cos(radians) + currentY);
		}

		for (int i = 0; i < corners; i++) {
			int nextIndex = (i + 1) % corners;
			myDrawLine(g2d, xPointsRotated[i], yPointsRotated[i],
					xPointsRotated[nextIndex], yPointsRotated[nextIndex], selectedSettings.getCurrentWeight());
		}
		g2d.dispose();
		repaint();
	}

	public static void drawStar(Graphics2D g2d, int centerX, int centerY, int outerRadius, int innerRadius, int n, int rotationAngleDegrees) {
		Path2D.Double star = new Path2D.Double();
		double rotationAngle = Math.toRadians(rotationAngleDegrees);
		double angleStep = Math.PI / n;
		double angle = -Math.PI / 2 + rotationAngle;
		double x = centerX + outerRadius * Math.cos(angle);
		double y = centerY + outerRadius * Math.sin(angle);
		star.moveTo(x, y);

		for (int i = 1; i <= 2 * n; i++) {
			angle += angleStep;
			double radius = (i % 2 == 0) ? outerRadius : innerRadius;
			x = centerX + radius * Math.cos(angle);
			y = centerY + radius * Math.sin(angle);
			star.lineTo(x, y);
		}

		star.closePath();
		g2d.draw(star);
	}



	public void clean() {
		draw = !draw;
		repaint();
	}


	private void drawStarGraphic(int centerX, int centerY, int outerRadius, int innerRadius, int n, int rotationAngleDegrees) {
		Graphics2D g2d = this.currentImage.createGraphics();
		g2d.setColor(selectedSettings.getCurrentColor());
		g2d.setStroke(new BasicStroke(selectedSettings.getCurrentWeight()));
		Path2D.Double star = new Path2D.Double();

		double rotationAngle = Math.toRadians(rotationAngleDegrees);
		double angleStep = Math.PI / n;
		double angle = -Math.PI / 2 + rotationAngle;
		double x = centerX + outerRadius * Math.cos(angle);
		double y = centerY + outerRadius * Math.sin(angle);
		star.moveTo(x, y);

		for (int i = 1; i <= 2 * n; i++) {
			angle += angleStep;
			double radius = (i % 2 == 0) ? outerRadius : innerRadius;
			x = centerX + radius * Math.cos(angle);
			y = centerY + radius * Math.sin(angle);
			star.lineTo(x, y);
		}

		star.closePath();
		g2d.draw(star);
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent ev) {
		if (selectedSettings.getCurrentMode().equals("FILL_MODE")) {
			spanFill(ev.getX(), ev.getY());
		}
	}

	@Override
	public void mouseEntered(MouseEvent ev) {}

	@Override
	public void mouseExited(MouseEvent ev) {}

	@Override
	public void mousePressed(MouseEvent ev) {
		xd = x = ev.getX();
		yd = y = ev.getY();
	}

	@Override
	public void mouseReleased(MouseEvent ev) {
		if (currentImage == null) return;

		if (selectedSettings.getCurrentMode().equals("STRAIGHT_LINE_MODE")) {
			System.out.println(selectedSettings.getCurrentWeight());
			if (selectedSettings.getCurrentWeight() == 1) {
				bresenhemDrawLine(x, y, ev.getX(), ev.getY());
			} else {
				Graphics2D g2d = currentImage.createGraphics();
				myDrawLine(g2d, x, y, ev.getX(), ev.getY(), selectedSettings.getCurrentWeight());
				g2d.dispose();
				repaint();
			}
		}
		if (selectedSettings.getCurrentMode().equals("FUN_MODE")) {
			drawFunMode(ev);
		}

		if (selectedSettings.getCurrentMode().equals("FIGURE_MODE")) {
			drawFigureGraphic(ev.getX(), ev.getY(), selectedSettings.getFigureSize(),
					selectedSettings.getFigureRotate(), selectedSettings.getFigureCorners());
		}
		if (selectedSettings.getCurrentMode().equals("FILL_MODE")) {
			spanFill(ev.getX(), ev.getY());
		}
		if(selectedSettings.getCurrentMode().equals("STAR_MODE")) {
			drawStarGraphic(ev.getX(), ev.getY(), selectedSettings.getStarSize(),
					selectedSettings.getStarRadius(), selectedSettings.getStarCorners(), selectedSettings.getStarRotate());
		}
	}

	private void spanFill(int x, int y) {
		if (currentImage == null) {
			System.out.println("BufferedImage is null");
			return;
		}
		Graphics2D g2d = currentImage.createGraphics();
		g2d.setColor(selectedSettings.getCurrentColor());
		int targetColor = this.selectedSettings.getCurrentColor().getRGB();
		int startColor = currentImage.getRGB(x, y);
		if (startColor == (targetColor)) {
			return;
		}
		Stack<int[]> stack = new Stack<>();
		stack.push(new int[]{x, y});

		while (!stack.isEmpty()) {
			int[] span = stack.pop();
			int startX = span[0];
			int currentY = span[1];

			int leftX = startX;
			while (leftX >= 0 && isSameColor(leftX, currentY, startColor)) {
				leftX--;
			}
			leftX++;

			int rightX = startX;
			while (rightX < currentImage.getWidth() && isSameColor(rightX, currentY, startColor)) {
				rightX++;
			}
			rightX--;

			for (int p=leftX; p<=rightX; p++)
				currentImage.setRGB(p, currentY, (selectedSettings.getCurrentColor()).getRGB());

			if (currentY > 0) {
				checkSpan(leftX, rightX, currentY - 1, startColor, stack);
			}
			if (currentY < currentImage.getHeight() - 1)
				checkSpan(leftX, rightX, currentY + 1, startColor, stack);

		}

		g2d.dispose();
		repaint();
	}

	private boolean isSameColor(int x, int y, int startColor) {
		if (x < 0 || y < 0 || x >= currentImage.getWidth() || y >= currentImage.getHeight()) {
			return false;
		}
		int pixelColor = currentImage.getRGB(x, y);
		return (pixelColor == startColor);
	}

	private void checkSpan(int leftX, int rightX, int y, int startColor, Stack<int[]> stack) {
		for (int i = leftX; i <= rightX; i++) {
			if (isSameColor(i, y, startColor)) {
				int startSpanX = i;
				while (i <= rightX && isSameColor(i, y, startColor)) {
					i++;
				}
				stack.push(new int[]{startSpanX, y});
			}
		}
	}

	private void drawFunMode(MouseEvent ev) {
		if (currentImage == null) return;

		int currentX = ev.getX();
		int currentY = ev.getY();

		Graphics2D g2d = currentImage.createGraphics();
		if (selectedSettings.getCurrentWeight() == 1)
			bresenhemDrawLine(currentX - DEFAULT_TRIANGLE_SIZE * 2 / 3, currentY + DEFAULT_TRIANGLE_SIZE / 3,
					currentX, currentY - DEFAULT_TRIANGLE_SIZE * 2 / 3);
		else
			myDrawLine(g2d,
					currentX - DEFAULT_TRIANGLE_SIZE * 2 / 3, currentY + DEFAULT_TRIANGLE_SIZE / 3,
					currentX, currentY - DEFAULT_TRIANGLE_SIZE * 2 / 3,
					selectedSettings.getCurrentWeight());
		g2d.dispose();
		repaint();
	}

	private void drawExampleFigure() {
		if (currentImage == null) {
			System.out.println("currentImage is null");
			return;
		}

		int currentX = currentImage.getWidth() / 2;
		int currentY = currentImage.getHeight() / 2;
		int corners = this.selectedSettings.getFigureCorners();
		int size = this.selectedSettings.getFigureSize();

		int[] xPoints = new int[corners];
		int[] yPoints = new int[corners];

		double angleStep = 2 * Math.PI / corners;

		for (int i = 0; i < corners; i++) {
			double angle = i * angleStep;
			xPoints[i] = (int) (currentX + size / 2 * Math.cos(angle));
			yPoints[i] = (int) (currentY + size / 2 * Math.sin(angle));
		}

		int[] xPointsRotated = new int[xPoints.length];
		int[] yPointsRotated = new int[yPoints.length];

		double radians = Math.toRadians(this.selectedSettings.getFigureRotate());
		for (int i = 0; i < xPoints.length; i++) {
			int x = xPoints[i] - currentX;
			int y = yPoints[i] - currentY;
			xPointsRotated[i] = (int) (x * Math.cos(radians) - y * Math.sin(radians) + currentX);
			yPointsRotated[i] = (int) (x * Math.sin(radians) + y * Math.cos(radians) + currentY);
		}

		Graphics2D g2d = currentImage.createGraphics();
		for (int i = 0; i < corners; i++) {
			int nextIndex = (i + 1) % corners; // Следующая вершина (с замыканием на первую)
			myDrawLine(g2d, xPointsRotated[i], yPointsRotated[i],
					xPointsRotated[nextIndex], yPointsRotated[nextIndex], selectedSettings.getCurrentWeight());
		}
		g2d.dispose();
		repaint();

		repaint();
	}


	private void drawFigure(Graphics2D g2d, int currentX, int currentY, int size, int corners, int rotationAngle) {
		Path2D.Double polygon = new Path2D.Double();

		// Угловой шаг между вершинами
		double angleStep = 2 * Math.PI / corners;

		// Начинаем с первой вершины
		double angle = -Math.PI / 2 + Math.toRadians(rotationAngle); // Начинаем с верхней вершины и добавляем угол поворота
		double x = currentX + size * Math.cos(angle);
		double y = currentY + size * Math.sin(angle);
		polygon.moveTo(x, y);

		// Добавляем остальные вершины
		for (int i = 1; i < corners; i++) {
			angle += angleStep;
			x = currentX + size * Math.cos(angle);
			y = currentY + size * Math.sin(angle);
			polygon.lineTo(x, y);
		}

		polygon.closePath();

		// Рисуем многоугольник
		g2d.draw(polygon);
	}


	public void drawExample() {
		if (currentImage == null) {
			System.out.println("currentImage is null");
			return;
		}

		int currentX = 100;
		int currentY = 100;
		int corners = this.selectedSettings.getFigureCorners();
		int size = this.selectedSettings.getFigureSize();

		int[] xPoints = new int[corners];
		int[] yPoints = new int[corners];

		double angleStep = 2 * Math.PI / corners;

		for (int i = 0; i < corners; i++) {
			double angle = i * angleStep;
			xPoints[i] = (int) (currentX + size / 2 * Math.cos(angle));
			yPoints[i] = (int) (currentY + size / 2 * Math.sin(angle));
		}

		int[] xPointsRotated = new int[xPoints.length];
		int[] yPointsRotated = new int[yPoints.length];

		double radians = Math.toRadians(this.selectedSettings.getFigureRotate());
		for (int i = 0; i < xPoints.length; i++) {
			int x = xPoints[i] - currentX;
			int y = yPoints[i] - currentY;
			xPointsRotated[i] = (int) (x * Math.cos(radians) - y * Math.sin(radians) + currentX);
			yPointsRotated[i] = (int) (x * Math.sin(radians) + y * Math.cos(radians) + currentY);
		}

		Graphics2D g2d = currentImage.createGraphics();
		for (int i = 0; i < corners; i++) {
			int nextIndex = (i + 1) % corners;
			myDrawLine(g2d, xPointsRotated[i], yPointsRotated[i],
					xPointsRotated[nextIndex], yPointsRotated[nextIndex], selectedSettings.getCurrentWeight());
		}
		g2d.dispose();
		repaint();

		repaint();
	}

	private void myDrawLine(Graphics2D g, int x1, int y1, int x2, int y2, int weight) {
		g.setColor(selectedSettings.getCurrentColor());
		g.setStroke(new BasicStroke(weight));
		g.drawLine(x1, y1, x2, y2);
	}

	public void getMessage(String typeOfMsg, String text, String title) {
		if (typeOfMsg.equals("info")) {
			InformationMessages informationMessages = new InformationMessages();
			informationMessages.showInformationMessage(this, text, title);
		}
	}

	public void saveAsImage(String filePath, String format) {
		try {
			ImageIO.write(currentImage, format, new File(filePath));
			System.out.println("Изображение успешно сохранено в " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Ошибка при сохранении изображения: " + e.getMessage());
		}
	}

	public void openImage(String filePath) {
		try {
			currentImage = ImageIO.read(new File(filePath));
			imageLabel.setIcon(new ImageIcon(currentImage));
			repaint();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Ошибка при открытии изображения: " + e.getMessage());
		}
	}
}