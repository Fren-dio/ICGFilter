package ICG.laboratory_2;

import ICG.laboratory_2.Settings.MainWindowSettings;
import ICG.laboratory_2.Settings.SelectedSettings;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final int BORDER_PADDING = 4;
	private static final int SCROLLBAR_EXTRA_PADDING = 6;
	private boolean fitToScreen = false;
	private boolean lookOther;

	private MainWindowSettings mainWindowSettings;
	public BufferedImage loadedImage;
	public BufferedImage savedLoadedImage;
	public BufferedImage changedImage;
	private BufferedImage lastModifiedImage;
	private SelectedSettings selectedSettings;
	private JScrollPane scrollPane;
	private FrameWork frameWork;
	private boolean changingFlag;
	private boolean processFlag = false;
	private boolean filterMode = true;
	private int interpolationMode = 0;


	private Point dragStartPoint;
	private Point imagePosition = new Point(0, 0);
	private boolean isDragging = false;


	public void setFilterMode(boolean mode) {
		this.filterMode = mode;
	}

	public void setInterpolationMode(int mode) {
		interpolationMode = mode;
	}

	public ImagePanel(SelectedSettings selectedSettings, MainWindowSettings mainWindowSettings, JScrollPane scrollPane, FrameWork frameWork) {
		this.mainWindowSettings = mainWindowSettings;
		this.selectedSettings = selectedSettings;
		this.scrollPane = scrollPane;
		this.frameWork = frameWork;
		this.changingFlag = false;
		this.lookOther = false;

		loadedImage = new BufferedImage(
				mainWindowSettings.getWeight(),
				mainWindowSettings.getHeight(),
				BufferedImage.TYPE_INT_ARGB
		);
		savedLoadedImage = new BufferedImage(
				mainWindowSettings.getWeight(),
				mainWindowSettings.getHeight(),
				BufferedImage.TYPE_INT_ARGB
		);
		changedImage = new BufferedImage(
				mainWindowSettings.getWeight(),
				mainWindowSettings.getHeight(),
				BufferedImage.TYPE_INT_ARGB
		);

		clear();
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();

		if (loadedImage != null) {
			if (fitToScreen) {
				Dimension viewSize = frameWork.getSize();
				int availableWidth = viewSize.width - 2 * BORDER_PADDING;
				int availableHeight = viewSize.height - 2 * BORDER_PADDING - 110;

				double imageRatio = (double) loadedImage.getWidth() / loadedImage.getHeight();
				double panelRatio = (double) availableWidth / availableHeight;

				int drawWidth, drawHeight;

				if (panelRatio > imageRatio) {
					drawHeight = availableHeight;
					drawWidth = (int) (drawHeight * imageRatio);
				} else {
					drawWidth = availableWidth;
					drawHeight = (int) (drawWidth / imageRatio);
				}

				if (!changingFlag) {
					BufferedImage scaledImage = new BufferedImage(drawWidth, drawHeight, BufferedImage.TYPE_INT_ARGB);
					Graphics2D sg = scaledImage.createGraphics();
					setInterpolation(sg);
					sg.drawImage(savedLoadedImage, 0, 0, drawWidth, drawHeight, null);
					sg.dispose();

					loadedImage = scaledImage;
				}
				else if (lookOther) {
					BufferedImage scaledImage = new BufferedImage(drawWidth, drawHeight, BufferedImage.TYPE_INT_ARGB);
					Graphics2D sg = scaledImage.createGraphics();
					setInterpolation(sg);
					sg.drawImage(savedLoadedImage, 0, 0, drawWidth, drawHeight, null);
					sg.dispose();

					loadedImage = scaledImage;
				}
				else {
					BufferedImage scaledImage = new BufferedImage(drawWidth, drawHeight, BufferedImage.TYPE_INT_ARGB);
					Graphics2D sg = scaledImage.createGraphics();
					setInterpolation(sg);
					sg.drawImage(changedImage, 0, 0, drawWidth, drawHeight, null);
					sg.dispose();
					loadedImage = scaledImage;
				}

				int xPos = (availableWidth - drawWidth) / 2 + BORDER_PADDING;
				int yPos = (availableHeight - drawHeight) / 2 + BORDER_PADDING;

				g2d.drawImage(loadedImage, xPos + imagePosition.x, yPos + imagePosition.y, this);
				drawDashedBorder(g2d, xPos, yPos, drawWidth, drawHeight);
			} else {
				int xPos = BORDER_PADDING;
				int yPos = BORDER_PADDING;
				if (!changingFlag) {
					loadedImage = savedLoadedImage;
					g2d.drawImage(loadedImage, xPos + imagePosition.x, yPos + imagePosition.y, this);
					drawDashedBorder(g2d, xPos, yPos,
							loadedImage.getWidth(), loadedImage.getHeight());
				}
				else if (lookOther) {
					loadedImage = savedLoadedImage;
					g2d.drawImage(loadedImage, xPos + imagePosition.x, yPos + imagePosition.y, this);
					drawDashedBorder(g2d, xPos, yPos,
							loadedImage.getWidth(), loadedImage.getHeight());
				}
				else {
					loadedImage = changedImage;
					g2d.drawImage(loadedImage, xPos + imagePosition.x, yPos + imagePosition.y,
							savedLoadedImage.getWidth(), savedLoadedImage.getHeight(), this);
					drawDashedBorder(g2d, xPos, yPos,
							savedLoadedImage.getWidth(), savedLoadedImage.getHeight());
				}
			}
		}
		g2d.dispose();
	}

	public void setInterpolation(Graphics2D sg) {
		if (interpolationMode == 1) {
			sg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			System.out.println("Подгонка под размер экрана. Интерполяция: бикубическая");
		}
		else if (interpolationMode == 2) {
			sg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			System.out.println("Подгонка под размер экрана. Интерполяция: метод ближайшего соседа");
		}
		else {
			sg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			System.out.println("Подгонка под размер экрана. Интерполяция: билинейная");
		}
	}

	public void outerCommandResizeToScreen() {
		fitToScreen = true;
		imagePosition.setLocation(0, 0);
		Dimension viewSize = scrollPane.getViewport().getExtentSize();
		setPreferredSize(viewSize);

		scrollPane.getViewport().setViewSize(viewSize);
		scrollPane.getViewport().setViewPosition(new Point(0, 0));

		revalidate();
		repaint();
	}

	public void outerCommandResizeToReal() {
		fitToScreen = false;
		imagePosition.setLocation(0, 0);
		loadedImage = savedLoadedImage;
		setPreferredSize(new Dimension(
				savedLoadedImage.getWidth() + 2 * BORDER_PADDING,
				savedLoadedImage.getHeight() + 2 * BORDER_PADDING
		));
		revalidate();
		repaint();
		updateScrollBars();
	}


	private void drawDashedBorder(Graphics2D g2d, int x, int y, int width, int height) {
		g2d.setColor(Color.darkGray);
		float[] dashPattern = {10, 5};
		g2d.setStroke(new BasicStroke(
				2,                      // Толщина
				BasicStroke.CAP_BUTT,   // Окончание линии
				BasicStroke.JOIN_MITER, // Соединение линий
				10,                     // Предел скругления
				dashPattern,            // Пунктирный шаблон
				0                       // Фаза пунктира
		));
		g2d.drawRect(x, y, width, height);
	}


	@Override
	public Dimension getPreferredSize() {
		if (loadedImage != null) {
			boolean hasHorizontalScroll = scrollPane != null &&
					scrollPane.getHorizontalScrollBarPolicy() != JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;

			boolean hasVerticalScroll = scrollPane != null &&
					scrollPane.getVerticalScrollBarPolicy() != JScrollPane.VERTICAL_SCROLLBAR_NEVER;

			int extraWidth = hasVerticalScroll ? SCROLLBAR_EXTRA_PADDING : 0;
			int extraHeight = hasHorizontalScroll ? SCROLLBAR_EXTRA_PADDING : 0;

			return new Dimension(
					loadedImage.getWidth() + 2 * BORDER_PADDING + extraWidth,
					loadedImage.getHeight() + 2 * BORDER_PADDING + extraHeight
			);
		}
		return new Dimension(
				mainWindowSettings.getWeight() + 2 * BORDER_PADDING,
				mainWindowSettings.getHeight() + 2 * BORDER_PADDING
		);
	}

	public void clear() {
		if (loadedImage != null) {
			Graphics2D g2d = loadedImage.createGraphics();
			g2d.setColor(getBackground());
			g2d.fillRect(0, 0, loadedImage.getWidth(), loadedImage.getHeight());
			g2d.dispose();
			repaint();
		}
	}

	public void updateScrollBars() {
		if (scrollPane == null) return;

		SwingUtilities.invokeLater(() -> {
			Dimension viewportSize = scrollPane.getViewport().getExtentSize();
			Dimension imageSize;

			if (fitToScreen) {
				imageSize = getSize();
			} else {
				imageSize = new Dimension(
						savedLoadedImage.getWidth() + 2 * BORDER_PADDING,
						savedLoadedImage.getHeight() + 4 * BORDER_PADDING
				);
			}

			boolean needHorizontal = imageSize.width > viewportSize.width;
			boolean needVertical = (imageSize.height - 20) > viewportSize.height;

			scrollPane.setHorizontalScrollBarPolicy(
					needHorizontal ? JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
							: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setVerticalScrollBarPolicy(
					needVertical ? JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
							: JScrollPane.VERTICAL_SCROLLBAR_NEVER);

			scrollPane.revalidate();
			scrollPane.repaint();
		});
	}

	public void changeViewedImage(String changeFlag) {

		if (changeFlag.equals("RGB")) {
			changingFlag = false;
			int height = savedLoadedImage.getHeight();
			int width = savedLoadedImage.getWidth();
			changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			changedImage = savedLoadedImage;
		}
		else {
			changingFlag = true;
			int height = loadedImage.getHeight();
			int width = loadedImage.getWidth();
			if (this.filterMode) {
				loadStartImage(height, width);
			}
			changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			for (int i = 0; i < height; i++) {
				for (int k = 0; k < width; k++) {
					if (changeFlag.equals("INVERSE"))
						changedImage.setRGB(k, i, convertPixelToInverse(loadedImage.getRGB(k, i)));
					else if (changeFlag.equals("BW"))
						changedImage.setRGB(k, i, convertPixelToBW(loadedImage.getRGB(k, i)));
				}
			}
		}

		loadedImage = changedImage;
		repaint();
	}

	void loadStartImage(int height, int width) {
		BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D sg = scaledImage.createGraphics();
		sg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		if (this.filterMode)
			sg.drawImage(savedLoadedImage, 0, 0, width, height, null);
		else
			sg.drawImage(loadedImage, 0, 0, width, height, null);
		sg.dispose();

		loadedImage = scaledImage;
	}


	void convertPixelToEmboss() {
		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		if (this.filterMode) {
			height = savedLoadedImage.getHeight();
			width = savedLoadedImage.getWidth();
		}
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		loadStartImage(height, width);

		// Ядро (свет сверху слева)
		float[][] kernel = {
				{-1, -1,  0},
				{-1,  0,  1},
				{ 0,  1,  1}
		};

		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				float r = 128, g = 128, b = 128;

				for (int ky = -1; ky <= 1; ky++) {
					for (int kx = -1; kx <= 1; kx++) {
						int pixel = savedLoadedImage.getRGB(x+kx, y+ky);
						if (!this.filterMode)
							pixel = loadedImage.getRGB(x+kx, y+ky);
						float weight = kernel[ky+1][kx+1];

						int gray = (int)(0.299 * ((pixel >> 16) & 0xFF) +
								0.587 * ((pixel >> 8) & 0xFF) +
								0.114 * (pixel & 0xFF));

						r += gray * weight;
						g += gray * weight;
						b += gray * weight;
					}
				}

				int newPixel = (255 << 24) |
						(clamp((int)r) << 16) |
						(clamp((int)g) << 8) |
						clamp((int)b);

				changedImage.setRGB(x, y, newPixel);
			}
		}

		loadedImage = changedImage;
		repaint();
	}

	public void convertToGamma(double gamma) {
		// Ограничиваем значение гаммы
		gamma = Math.max(0.1, Math.min(10.0, gamma));

		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		if (this.filterMode) {
			height = savedLoadedImage.getHeight();
			width = savedLoadedImage.getWidth();
		}
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		loadStartImage(height, width);

		int[] gammaLUT = new int[256];
		for (int i = 0; i < 256; i++) {
			gammaLUT[i] = (int)(255 * Math.pow(i / 255.0, 1.0 / gamma));
		}

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = savedLoadedImage.getRGB(x, y);
				if (!this.filterMode)
					pixel = loadedImage.getRGB(x, y);

				int alpha = (pixel >> 24) & 0xFF;
				int red   = (pixel >> 16) & 0xFF;
				int green = (pixel >> 8)  & 0xFF;
				int blue  =  pixel        & 0xFF;

				red   = gammaLUT[red];
				green = gammaLUT[green];
				blue  = gammaLUT[blue];

				int newPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
				changedImage.setRGB(x, y, newPixel);
			}
		}

		loadedImage = changedImage;
		repaint();
	}


	void convertToFloydSteinberg(boolean byFullPixel, int colorsFullPixel, int cntRED, int cntGREEN, int cntBLUE) {
		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		if (this.filterMode) {
			height = savedLoadedImage.getHeight();
			width = savedLoadedImage.getWidth();
		}
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		loadStartImage(height, width);

		int[][] pixels = new int[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[y][x] = savedLoadedImage.getRGB(x, y);
				if (!this.filterMode)
					pixels[y][x] = loadedImage.getRGB(x, y);
			}
		}

		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				int oldPixel = pixels[y][x];
				int newPixel;

				if (!byFullPixel) {
					// Обработка по компонентам
					int a = (oldPixel >> 24) & 0xFF;
					int r = (oldPixel >> 16) & 0xFF;
					int g = (oldPixel >> 8) & 0xFF;
					int b = oldPixel & 0xFF;

					int pixel_alpha = (oldPixel >> 24) & 0xFF;
					int pixel_r = convertToClosest((oldPixel >> 16) & 0xFF, cntRED);
					int pixel_g = convertToClosest((oldPixel >> 8) & 0xFF, cntGREEN);
					int pixel_b = convertToClosest((oldPixel) & 0xFF, cntBLUE);

					newPixel = (pixel_alpha << 24) | (pixel_r << 16) | (pixel_g << 8) | pixel_b;

					int errR = r - pixel_r;
					int errG = g - pixel_g;
					int errB = b - pixel_b;

					distributeError(pixels, x, y, errR, errG, errB);
				} else {
					// Обработка целого пикселя (усредненное значение)
					int gray = (int)(0.299 * ((oldPixel >> 16) & 0xFF) +
							0.587 * ((oldPixel >> 8) & 0xFF) +
							0.114 * (oldPixel & 0xFF));

					int newGray = convertToClosest(gray, colorsFullPixel);
					newPixel = (255 << 24) | (newGray << 16) | (newGray << 8) | newGray;

					// Распространение ошибки
					int error = gray - newGray;
					distributeError(pixels, x, y, error, error, error);
				}
				changedImage.setRGB(x, y, newPixel);
			}
		}

		loadedImage = changedImage;
		System.out.println("convertToFloydSteinberg\n");
		repaint();
	}


	void convertToFunnyColors(int value) {
		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		if (this.filterMode) {
			height = savedLoadedImage.getHeight();
			width = savedLoadedImage.getWidth();
		}
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		loadStartImage(height, width);

		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				int oldPixel = savedLoadedImage.getRGB(x, y);
				if (!this.filterMode)
					oldPixel = loadedImage.getRGB(x, y);

				int pixel_alpha = (oldPixel >> 24) & 0xFF;
				int pixel_r = convertToClosestThree((oldPixel >> 16) & 0xFF) + value;
				int pixel_g = convertToClosestThree((oldPixel >> 8) & 0xFF) + value;
				int pixel_b = convertToClosestThree((oldPixel) & 0xFF) + value;

				int newPixel = (pixel_alpha << 24) | (pixel_r << 16) | (pixel_g << 8) | pixel_b;

				changedImage.setRGB(x, y, newPixel);
			}
		}

		loadedImage = changedImage;
		System.out.println("convertToFunnyColors\n");
		repaint();
	}

	private int convertToClosest(int pixel, int colors) {
		double step = (double) (256.0 / colors);
		double colorShift = (double) (256.0 / (colors-1));
		for (int i = 1; i < (256/step); i++) {
			if (pixel < i*step)
				return (int) (colorShift*(i-1));
		}

		return 255;
	}

	private int convertToClosestThree(int pixel) {
		if (pixel < 64)
			return 0;
		if (pixel < 192)
			return 128;
		return 255;
	}

	private void distributeError(int[][] pixels, int x, int y, int errR, int errG, int errB) {
		// Право
		if (x + 1 < pixels[0].length) {
			addError(pixels, x+1, y, errR, errG, errB, 7/16.0);
		}
		// Лево-низ
		if (x > 0 && y + 1 < pixels.length) {
			addError(pixels, x-1, y+1, errR, errG, errB, 3/16.0);
		}
		// Низ
		if (y + 1 < pixels.length) {
			addError(pixels, x, y+1, errR, errG, errB, 5/16.0);
		}
		// Право-низ
		if (x + 1 < pixels[0].length && y + 1 < pixels.length) {
			addError(pixels, x+1, y+1, errR, errG, errB, 1/16.0);
		}
	}

	private void addError(int[][] pixels, int x, int y, int errR, int errG, int errB, double factor) {
		int pixel = pixels[y][x];
		int r = ((pixel >> 16) & 0xFF) + (int)(errR * factor);
		int g = ((pixel >> 8) & 0xFF) + (int)(errG * factor);
		int b = (pixel & 0xFF) + (int)(errB * factor);
		pixels[y][x] = (0xFF << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b);
	}


	void orderedDithering(int redLevels, int greenLevels, int blueLevels) {
		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		if (this.filterMode) {
			height = savedLoadedImage.getHeight();
			width = savedLoadedImage.getWidth();
		}
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		loadStartImage(height, width);

		// Матрица Байера
		int maxLevels = Math.max(redLevels, Math.max(greenLevels, blueLevels));
		int matrixSize = calculateBayerSize(maxLevels);
		int[][] bayerMatrix = generateBayerMatrix(matrixSize);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = savedLoadedImage.getRGB(x, y);
				if (!this.filterMode) pixel = loadedImage.getRGB(x, y);

				int r = (pixel >> 16) & 0xFF;
				int g = (pixel >> 8) & 0xFF;
				int b = pixel & 0xFF;

				r = applyChannelDither(r, y, x, redLevels, bayerMatrix, matrixSize);
				g = applyChannelDither(g, y, x, greenLevels, bayerMatrix, matrixSize);
				b = applyChannelDither(b, y, x, blueLevels, bayerMatrix, matrixSize);

				int newPixel = (r << 16) | (g << 8) | b;
				changedImage.setRGB(x, y, newPixel);
			}
		}

		loadedImage = changedImage;
		repaint();
	}

	private int calculateBayerSize(int levels) {
		int size = 2;
		while (size * size < levels) {
			size *= 2;
		}
		return size;
	}

	private int[][] generateBayerMatrix(int size) {
		int[][] matrix = new int[size][size];
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				int value = 0;
				int mask = size >> 1;
				int shift = 0;

				while (mask > 0) {
					value |= ((y & mask) != 0 ? 1 : 0) << (shift * 2);
					value |= ((x & mask) != 0 ? 1 : 0) << (shift * 2 + 1);
					mask >>= 1;
					shift++;
				}

				matrix[y][x] = value;
			}
		}
		return matrix;
	}

	private int applyChannelDither(int value, int y, int x, int levels, int[][] matrix, int matrixSize) {
		if (levels < 2) levels = 2;
		if (levels > 256) levels = 256; // Исправлено максимальное значение

		// Нормализация значения матрицы к [0, 1]
		float threshold = matrix[y % matrixSize][x % matrixSize] / (float)(matrixSize * matrixSize);

		// Диапазон квантования
		float step = 255f / (levels - 1);

		// Добавляем шум Байера
		float noisyValue = value + (threshold - 0.5f) * step;

		// Квантование с округлением
		int quantized = (int) (Math.round(noisyValue / step) * step);

		return clamp(quantized);
	}

	void mainConvertStart() {
		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		if (this.filterMode) {
			height = savedLoadedImage.getHeight();
			width = savedLoadedImage.getWidth();
		}
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		loadStartImage(height, width);
	}

	void mainConvertEnd() {
		loadedImage = changedImage;
		repaint();
	}




	void  convertToAcvarel() {

		applyMedianFilter();

		addLight(0.15f);

		addTexture();

		addPaperTextureEffect(0.05f);

		applyWatercolorEdges();

		//enhanceLightAreas();

		loadedImage = changedImage;
		repaint();
	}


	void applyMedianFilter() {
		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		if (this.filterMode) {
			height = savedLoadedImage.getHeight();
			width = savedLoadedImage.getWidth();
		}
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		loadStartImage(height, width);

		int radius = 2; // Для матрицы 5x5 радиус = 2
		int windowSize = 5;
		int pixelCount = windowSize * windowSize;

		// Временные массивы для хранения компонентов
		int[] reds = new int[pixelCount];
		int[] greens = new int[pixelCount];
		int[] blues = new int[pixelCount];

		for (int y = radius; y < height - radius; y++) {
			for (int x = radius; x < width - radius; x++) {
				int index = 0;

				// Собираем пиксели из окрестности 5x5
				for (int ky = -radius; ky <= radius; ky++) {
					for (int kx = -radius; kx <= radius; kx++) {
						int pixel;
						if (this.filterMode) {
							pixel = savedLoadedImage.getRGB(x + kx, y + ky);
						} else {
							pixel = loadedImage.getRGB(x + kx, y + ky);
						}

						reds[index] = (pixel >> 16) & 0xFF;
						greens[index] = (pixel >> 8) & 0xFF;
						blues[index] = pixel & 0xFF;
						index++;
					}
				}

				// Сортируем и находим медиану
				Arrays.sort(reds);
				Arrays.sort(greens);
				Arrays.sort(blues);

				int medianRed = reds[pixelCount / 2];
				int medianGreen = greens[pixelCount / 2];
				int medianBlue = blues[pixelCount / 2];

				changedImage.setRGB(x, y, (medianRed << 16) | (medianGreen << 8) | medianBlue);
			}
		}

		// Обработка граничных пикселей (простое копирование)
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (y < radius || y >= height - radius || x < radius || x >= width - radius) {
					int pixel;
					if (this.filterMode) {
						pixel = savedLoadedImage.getRGB(x, y);
					} else {
						pixel = loadedImage.getRGB(x, y);
					}
					changedImage.setRGB(x, y, pixel);
				}
			}
		}

		loadedImage = changedImage;
		repaint();
	}

	void acvarelBlur() {
		int height = changedImage.getHeight();
		int width = changedImage.getWidth();

		// Параметры фильтра
		int radius = 3; // Радиус размытия
		double sigmaColor = 30; // Влияние разницы цветов
		double sigmaSpace = 15; // Влияние пространственного расстояния

		for (int y = radius; y < height - radius; y++) {
			for (int x = radius; x < width - radius; x++) {
				double r = 0, g = 0, b = 0;
				double totalWeight = 0;

				// Центральный пиксель (для вычисления цветового веса)
				int centerPixel;
				if (this.filterMode) {
					centerPixel = savedLoadedImage.getRGB(x, y);
				} else {
					centerPixel = loadedImage.getRGB(x, y);
				}
				int centerR = (centerPixel >> 16) & 0xFF;
				int centerG = (centerPixel >> 8) & 0xFF;
				int centerB = centerPixel & 0xFF;

				// Проход по окрестности
				for (int ky = -radius; ky <= radius; ky++) {
					for (int kx = -radius; kx <= radius; kx++) {
						int pixel;
						if (this.filterMode) {
							pixel = savedLoadedImage.getRGB(x + kx, y + ky);
						} else {
							pixel = loadedImage.getRGB(x + kx, y + ky);
						}
						int currentR = (pixel >> 16) & 0xFF;
						int currentG = (pixel >> 8) & 0xFF;
						int currentB = pixel & 0xFF;

						// Вычисляем веса
						double colorDiff = Math.sqrt(
								Math.pow(currentR - centerR, 2) +
										Math.pow(currentG - centerG, 2) +
										Math.pow(currentB - centerB, 2)
						);
						double colorWeight = Math.exp(-(colorDiff * colorDiff) / (2 * sigmaColor * sigmaColor));

						double spaceWeight = Math.exp(-(kx * kx + ky * ky) / (2 * sigmaSpace * sigmaSpace));

						double weight = colorWeight * spaceWeight;

						r += currentR * weight;
						g += currentG * weight;
						b += currentB * weight;
						totalWeight += weight;
					}
				}

				// Нормализация и установка нового значения пикселя
				int newR = clamp((int)(r / totalWeight));
				int newG = clamp((int)(g / totalWeight));
				int newB = clamp((int)(b / totalWeight));

				changedImage.setRGB(x, y, (newR << 16) | (newG << 8) | newB);
			}
		}

		// Обработка граничных пикселей (просто копируем без размытия)
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (y < radius || y >= height - radius || x < radius || x >= width - radius) {
					int pixel;
					if (this.filterMode) {
						pixel = savedLoadedImage.getRGB(x, y);
					} else {
						pixel = loadedImage.getRGB(x, y);
					}
					changedImage.setRGB(x, y, pixel);
				}
			}
		}
	}

	void addLight(float factor) {
		for (int y = 0; y < changedImage.getHeight(); y++) {
			for (int x = 0; x < changedImage.getWidth(); x++) {
				Color color = new Color(changedImage.getRGB(x, y), true);
				float[] hsb = Color.RGBtoHSB(
						color.getRed(),
						color.getGreen(),
						color.getBlue(),
						null
				);

				// Адаптивное увеличение насыщенности
				float saturationBoost = factor * (1 - hsb[1]);
				hsb[1] = Math.min(1.0f, hsb[1] + saturationBoost);

				// Сохраняем прозрачность
				int alpha = color.getAlpha();
				int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
				rgb = (alpha << 24) | (rgb & 0x00FFFFFF);

				changedImage.setRGB(x, y, rgb);
			}
		}
	}

	void addTexture(){
		BufferedImage texture = new BufferedImage(
				changedImage.getWidth(),
				changedImage.getHeight(),
				BufferedImage.TYPE_INT_ARGB
		);

		Graphics2D g2d = texture.createGraphics();
		g2d.setComposite(AlphaComposite.SrcOver);

		Random random = new Random();
		int brushSize = 15 + random.nextInt(10);
		int step = brushSize / 2;

		BufferedImage grayImage = convertToGray(changedImage);

		for (int y = step; y < changedImage.getHeight() - step; y += step) {
			for (int x = step; x < changedImage.getWidth() - step; x += step) {
				// Анализ градиентов
				double gx = (grayImage.getRGB(x+1, y) & 0xFF) - (grayImage.getRGB(x-1, y) & 0xFF);
				double gy = (grayImage.getRGB(x, y+1) & 0xFF) - (grayImage.getRGB(x, y-1) & 0xFF);
				double angle = Math.atan2(gy, gx);

				// Доминирующий цвет области
				Color areaColor = getDominantColor(x, y, brushSize/2);

				// Создаем мазок
				g2d.setColor(new Color(
						clamp(areaColor.getRed() + random.nextInt(15) - 7),
						clamp(areaColor.getGreen() + random.nextInt(15) - 7),
						clamp(areaColor.getBlue() + random.nextInt(15) - 7),
						30 + random.nextInt(70)
				));

				// Рисуем мазок вдоль направления градиента
				g2d.rotate(angle, x, y);
				int width_cur = brushSize + random.nextInt(brushSize/2);
				int height_cur = brushSize/3 + random.nextInt(brushSize/3);
				g2d.fillOval(x - width_cur/2, y - height_cur/2, width_cur, height_cur);
				g2d.rotate(-angle, x, y);
			}
		}
		g2d.dispose();

		// Наложение текстуры
		Graphics2D g = changedImage.createGraphics();
		g.drawImage(texture, 0, 0, null);
		g.dispose();
	}

	private void enhanceLightAreas() {
		for (int y = 0; y < changedImage.getHeight(); y++) {
			for (int x = 0; x < changedImage.getWidth(); x++) {
				Color color = new Color(changedImage.getRGB(x, y));
				float[] hsb = Color.RGBtoHSB(
						color.getRed(),
						color.getGreen(),
						color.getBlue(),
						null
				);

				// Усиливаем яркость светлых участков
				if (hsb[2] > 0.7f) {
					hsb[2] = Math.min(1.0f, hsb[2] * 1.1f);
				}

				changedImage.setRGB(x, y, Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
			}
		}
	}

	private void addPaperTextureEffect(float opacity) {
		BufferedImage paperTexture = new BufferedImage(
				changedImage.getWidth(),
				changedImage.getHeight(),
				BufferedImage.TYPE_INT_ARGB
		);

		// Создаем текстуру бумаги
		Graphics2D g2d = paperTexture.createGraphics();
		Random random = new Random();

		// Заливаем фон светло-бежевым (цвет бумаги)
		g2d.setColor(new Color(240, 230, 210));
		g2d.fillRect(0, 0, paperTexture.getWidth(), paperTexture.getHeight());

		// Добавляем шум и волокна
		for (int i = 0; i < paperTexture.getWidth() * paperTexture.getHeight() / 50; i++) {
			int x = random.nextInt(paperTexture.getWidth());
			int y = random.nextInt(paperTexture.getHeight());
			int length = 10 + random.nextInt(30);
			int angle = random.nextInt(180);

			g2d.setColor(new Color(
					220 + random.nextInt(30),
					210 + random.nextInt(30),
					190 + random.nextInt(30),
					30 + random.nextInt(40)
			));

			g2d.rotate(Math.toRadians(angle), x, y);
			g2d.drawLine(x, y, x + length, y);
			g2d.rotate(-Math.toRadians(angle), x, y);
		}
		g2d.dispose();

		// Наложение текстуры
		Graphics2D g = changedImage.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g.drawImage(paperTexture, 0, 0, null);
		g.dispose();
	}

	private void applyWatercolorEdges() {
		int radius = 2;
		for (int y = radius; y < changedImage.getHeight() - radius; y++) {
			for (int x = radius; x < changedImage.getWidth() - radius; x++) {
				if (Math.random() > 0.85) { // 5% пикселей
					int dx = (int)(Math.random() * radius * 2) - radius;
					int dy = (int)(Math.random() * radius * 2) - radius;

					if (x + dx >= 0 && x + dx < changedImage.getWidth() &&
							y + dy >= 0 && y + dy < changedImage.getHeight()) {

						// Смешиваем цвета для эффекта растекания
						Color src = new Color(changedImage.getRGB(x, y), true);
						Color dst = new Color(changedImage.getRGB(x + dx, y + dy), true);

						int r = (src.getRed() + dst.getRed()) / 2;
						int g = (src.getGreen() + dst.getGreen()) / 2;
						int b = (src.getBlue() + dst.getBlue()) / 2;

						changedImage.setRGB(x + dx, y + dy, new Color(r, g, b).getRGB());
					}
				}
			}
		}
	}


	private Color getDominantColor(int centerX, int centerY, int radius) {
		int r = 0, g = 0, b = 0;
		int count = 0;

		int startX = Math.max(0, centerX - radius);
		int endX = Math.min(changedImage.getWidth() - 1, centerX + radius);
		int startY = Math.max(0, centerY - radius);
		int endY = Math.min(changedImage.getHeight() - 1, centerY + radius);

		for (int y = startY; y <= endY; y++) {
			for (int x = startX; x <= endX; x++) {
				if (Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)) <= radius) {
					Color color = new Color(changedImage.getRGB(x, y));
					r += color.getRed();
					g += color.getGreen();
					b += color.getBlue();
					count++;
				}
			}
		}

		if (count == 0) return new Color(changedImage.getRGB(centerX, centerY));

		return new Color(r/count, g/count, b/count);
	}


	void edgeDetectionConvert(ToolBarMenu.EdgeDetectionType type, int threshold){
		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		if (this.filterMode) {
			height = savedLoadedImage.getHeight();
			width = savedLoadedImage.getWidth();
		}
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		loadStartImage(height, width);

		BufferedImage grayImage = convertToGray(savedLoadedImage);
		if (!this.filterMode)
			grayImage = convertToGray(loadedImage);

		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				int magnitude = 0;

				switch(type) {
					case ROBERTS:
						magnitude = calculateRoberts(grayImage, x, y);
						break;
					case SOBEL:
						magnitude = calculateSobel(grayImage, x, y);
						break;
				}

				// Бинаризация по порогу
				int pixelValue = (magnitude >= threshold) ? 255 : 0;
				int newPixel = (255 << 24) | (pixelValue << 16) | (pixelValue << 8) | pixelValue;
				changedImage.setRGB(x, y, newPixel);
			}
		}

		loadedImage = changedImage;
		repaint();
	}


	void rotateImage(int angleDegrees) {
		changingFlag = true;
		int originalWidth = loadedImage.getWidth();
		int originalHeight = loadedImage.getHeight();
		if (this.filterMode) {
			originalWidth = savedLoadedImage.getWidth();
			originalHeight = savedLoadedImage.getHeight();
		}

		angleDegrees = Math.max(-180, Math.min(180, angleDegrees));
		double angleRadians = Math.toRadians(angleDegrees);

		double sin = Math.abs(Math.sin(angleRadians));
		double cos = Math.abs(Math.cos(angleRadians));
		int newWidth = (int) Math.round(originalWidth * cos + originalHeight * sin);
		int newHeight = (int) Math.round(originalWidth * sin + originalHeight * cos);

		changedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = changedImage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, newWidth, newHeight);
		g2d.dispose();

		double originalCenterX = originalWidth / 2.0;
		double originalCenterY = originalHeight / 2.0;
		double newCenterX = newWidth / 2.0;
		double newCenterY = newHeight / 2.0;

		for (int y = 0; y < newHeight; y++) {
			for (int x = 0; x < newWidth; x++) {
				double dx = x - newCenterX;
				double dy = y - newCenterY;
				double rotatedX = dx * Math.cos(-angleRadians) - dy * Math.sin(-angleRadians);
				double rotatedY = dx * Math.sin(-angleRadians) + dy * Math.cos(-angleRadians);
				int srcX = (int) Math.round(rotatedX + originalCenterX);
				int srcY = (int) Math.round(rotatedY + originalCenterY);
				if (srcX >= 0 && srcX < originalWidth && srcY >= 0 && srcY < originalHeight) {
					int pixel;
					if (this.filterMode) {
						pixel = savedLoadedImage.getRGB(srcX, srcY);
					} else {
						pixel = loadedImage.getRGB(srcX, srcY);
					}
					changedImage.setRGB(x, y, pixel);
				}

			}
		}

		loadedImage = changedImage;
		repaint();
	}



	private BufferedImage convertToGray(BufferedImage src) {
		BufferedImage gray = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = gray.getGraphics();
		g.drawImage(src, 0, 0, null);
		g.dispose();
		return gray;
	}

	private int calculateRoberts(BufferedImage image, int x, int y) {
		int p00 = image.getRGB(x, y) & 0xFF;
		int p01 = image.getRGB(x, y+1) & 0xFF;
		int p10 = image.getRGB(x+1, y) & 0xFF;
		int p11 = image.getRGB(x+1, y+1) & 0xFF;

		int gx = p00 - p11;
		int gy = p01 - p10;

		return (int) Math.sqrt(gx*gx + gy*gy);
	}

	private int calculateSobel(BufferedImage image, int x, int y) {
		int[][] pixels = {
				{image.getRGB(x-1, y-1) & 0xFF, image.getRGB(x, y-1) & 0xFF, image.getRGB(x+1, y-1) & 0xFF},
				{image.getRGB(x-1, y)   & 0xFF, image.getRGB(x, y)   & 0xFF, image.getRGB(x+1, y)   & 0xFF},
				{image.getRGB(x-1, y+1) & 0xFF, image.getRGB(x, y+1) & 0xFF, image.getRGB(x+1, y+1) & 0xFF}
		};

		int gx = (pixels[0][0]*-1 + pixels[0][2]*1 +
				pixels[1][0]*-2 + pixels[1][2]*2 +
				pixels[2][0]*-1 + pixels[2][2]*1);

		int gy = (pixels[0][0]*-1 + pixels[0][1]*-2 + pixels[0][2]*-1 +
				pixels[2][0]*1  + pixels[2][1]*2  + pixels[2][2]*1);

		return (int) Math.sqrt(gx*gx + gy*gy);
	}


	void convertToCrystal (int shift) {

		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		if (this.filterMode) {
			height = savedLoadedImage.getHeight();
			width = savedLoadedImage.getWidth();
		}
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		loadStartImage(height, width);

		for (int y = shift; y < height - shift; y+=2*shift) {
			for (int x = shift; x < width - shift; x+=2*shift) {
				float r = 0, g = 0, b = 0;
				int pixel = savedLoadedImage.getRGB(x, y);
				if (!this.filterMode)
					pixel = loadedImage.getRGB(x, y);
				r = ((pixel >> 16) & 0xFF);
				g = ((pixel >> 8) & 0xFF);
				b = (pixel & 0xFF);
				for (int ky = -shift; ky <= shift; ky++) {
					for (int kx = -shift; kx <= shift; kx++) {
						int newPixel = (255 << 24) | ((int) (r) << 16) | ((int) g << 8) | (int) b;
						changedImage.setRGB(x+kx, y+ky, newPixel);
					}
				}

				}
			}


		loadedImage = changedImage;
		repaint();
	}


	void convertPixelByGausse(int size) {
		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		if (this.filterMode) {
			height = savedLoadedImage.getHeight();
			width = savedLoadedImage.getWidth();
		}
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		loadStartImage(height, width);

		float[][] kernel = {
				{1/16f, 2/16f, 1/16f},
				{2/16f, 4/16f, 2/16f},
				{1/16f, 2/16f, 1/16f}
		};
		int shift = (size - 1)/2;
		if (size == 5) {
			kernel = new float[][]{
					{1 / 256f, 4 / 256f, 6 / 256f, 4 / 256f, 1 / 256f},
					{4 / 256f, 16 / 256f, 24 / 256f, 16 / 256f, 4 / 256f},
					{6 / 256f, 24 / 256f, 36 / 256f, 24 / 256f, 6 / 256f},
					{4 / 256f, 16 / 256f, 24 / 256f, 16 / 256f, 4 / 256f},
					{1 / 256f, 4 / 256f, 6 / 256f, 4 / 256f, 1 / 256f}
			};
		}

			/*
			kernel = new float[][]{
					{0.000001f, 0.000012f, 0.000085f, 0.000382f, 0.000724f, 0.000382f, 0.000085f, 0.000012f, 0.000001f, 0.000000f, 0.000000f},
					{0.000012f, 0.000209f, 0.001446f, 0.006622f, 0.012626f, 0.006622f, 0.001446f, 0.000209f, 0.000012f, 0.000001f, 0.000000f},
					{0.000085f, 0.001446f, 0.010021f, 0.045881f, 0.087528f, 0.045881f, 0.010021f, 0.001446f, 0.000085f, 0.000012f, 0.000001f},
					{0.000382f, 0.006622f, 0.045881f, 0.210051f, 0.400818f, 0.210051f, 0.045881f, 0.006622f, 0.000382f, 0.000085f, 0.000012f},
					{0.000724f, 0.012626f, 0.087528f, 0.400818f, 0.764986f, 0.400818f, 0.087528f, 0.012626f, 0.000724f, 0.000382f, 0.000085f},
					{0.000382f, 0.006622f, 0.045881f, 0.210051f, 0.400818f, 0.210051f, 0.045881f, 0.006622f, 0.000382f, 0.000085f, 0.000012f},
					{0.000085f, 0.001446f, 0.010021f, 0.045881f, 0.087528f, 0.045881f, 0.010021f, 0.001446f, 0.000085f, 0.000012f, 0.000001f},
					{0.000012f, 0.000209f, 0.001446f, 0.006622f, 0.012626f, 0.006622f, 0.001446f, 0.000209f, 0.000012f, 0.000001f, 0.000000f},
					{0.000001f, 0.000012f, 0.000085f, 0.000382f, 0.000724f, 0.000382f, 0.000085f, 0.000012f, 0.000001f, 0.000000f, 0.000000f},
					{0.000000f, 0.000001f, 0.000012f, 0.000085f, 0.000382f, 0.000085f, 0.000012f, 0.000001f, 0.000000f, 0.000000f, 0.000000f},
					{0.000000f, 0.000000f, 0.000001f, 0.000012f, 0.000085f, 0.000012f, 0.000001f, 0.000000f, 0.000000f, 0.000000f, 0.000000f}
			};
			*/

		if (size < 7) {
			for (int y = shift; y < height - shift; y++) {
				for (int x = shift; x < width - shift; x++) {
					float r = 0, g = 0, b = 0;
					for (int ky = -shift; ky <= shift; ky++) {
						for (int kx = -shift; kx <= shift; kx++) {
							int pixel = savedLoadedImage.getRGB(x + kx, y + ky);
							if (!this.filterMode)
								pixel = loadedImage.getRGB(x+kx, y+ky);
							float weight = kernel[ky + shift][kx + shift];
							r += ((pixel >> 16) & 0xFF) * weight;
							g += ((pixel >> 8) & 0xFF) * weight;
							b += (pixel & 0xFF) * weight;
						}
					}
					int newPixel = (255 << 24) | ((int) r << 16) | ((int) g << 8) | (int) b;
					changedImage.setRGB(x, y, newPixel);
				}
			}
		}
		else {
				for (int y = shift; y < height - shift; y++) {
					for (int x = shift; x < width - shift; x++) {
						float r = 0, g = 0, b = 0;
						for (int ky = -shift; ky <= shift; ky++) {
							for (int kx = -shift; kx <= shift; kx++) {
								int pixel = savedLoadedImage.getRGB(x + kx, y+ky);
								if (!this.filterMode)
									pixel = loadedImage.getRGB(x+kx, y+ky);
								r += ((pixel >> 16) & 0xFF);
								g += ((pixel >> 8) & 0xFF);
								b += (pixel & 0xFF);
							}
						}
						int newPixel = (255 << 24) | ((int) (r/(size*size)) << 16) | ((int) (g/(size*size)) << 8) | (int) (b/(size*size));
						changedImage.setRGB(x, y, newPixel);
					}
				}
		}

		loadedImage = changedImage;
		repaint();
	}

	void convertPixelBySharp3(boolean light) {
		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		if (this.filterMode) {
			height = savedLoadedImage.getHeight();
			width = savedLoadedImage.getWidth();
		}
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		loadStartImage(height, width);

		float[][] kernel = {
				{-1/9f, -1/9f,  -1/9f},
				{-1/9f, 17/9f, -1/9f},
				{-1/9f, -1/9f, -1/9f}
		};
		if (light) {
			kernel = new float[][]{
					{ 0,    -0.25f,  0    },
					{-0.25f,  2.0f, -0.25f},
					{ 0,    -0.25f,  0    }
			};
		}


		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				float r = 0, g = 0, b = 0;
				for (int ky = -1; ky <= 1; ky++) {
					for (int kx = -1; kx <= 1; kx++) {
						int pixel = savedLoadedImage.getRGB(x + kx, y + ky);
						if (!this.filterMode)
							pixel = loadedImage.getRGB(x+kx, y+ky);
						float weight = kernel[ky+1][kx+1];
						r += ((pixel >> 16) & 0xFF) * weight;
						g += ((pixel >> 8) & 0xFF) * weight;
						b += (pixel & 0xFF) * weight;
					}
				}
				int alpha = savedLoadedImage.getRGB(x, y) & 0xFF000000;
				if (!this.filterMode)
					alpha = loadedImage.getRGB(x, y) & 0xFF;
				int newPixel = alpha |
						(clamp((int)r) << 16) |
						(clamp((int)g) << 8)  |
						clamp((int)b);

				changedImage.setRGB(x, y, newPixel);
			}
		}

		loadedImage = changedImage;
		repaint();
	}

	int clamp(int value) {
		return Math.max(0, Math.min(255, value));
	}

	void convertPixelBySharp5(boolean light) {
		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		if (this.filterMode) {
			height = savedLoadedImage.getHeight();
			width = savedLoadedImage.getWidth();
		}
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		loadStartImage(height, width);

		float[][] kernel = {
				{-1/256f, -4/256f,  -6/256f, -4/256f, -1/256f},
				{-4/256f, -16/256f, -24/256f,-16/256f, -4/256f},
				{-6/256f, -24/256f, 476/256f,-24/256f, -6/256f},
				{-4/256f, -16/256f, -24/256f,-16/256f, -4/256f},
				{-1/256f, -4/256f,  -6/256f, -4/256f, -1/256f}
		};
		if (light) {
			kernel = new float[][]{
					{ 0,     -1/256f,  -2/256f,  -1/256f,  0     },
					{-1/256f, -4/256f,  -8/256f,  -4/256f, -1/256f},
					{-2/256f, -8/256f, 532/256f, -8/256f, -2/256f},
					{-1/256f, -4/256f,  -8/256f,  -4/256f, -1/256f},
					{ 0,     -1/256f,  -2/256f,  -1/256f,  0     }
			};
		}

		for (int y = 2; y < height - 2; y++) {
			for (int x = 2; x < width - 2; x++) {
				float r = 0, g = 0, b = 0;
				for (int ky = -2; ky <= 2; ky++) {
					for (int kx = -2; kx <= 2; kx++) {
						int pixel = savedLoadedImage.getRGB(x + kx, y + ky);
						if (!this.filterMode)
							pixel = loadedImage.getRGB(x+kx, y+ky);
						float weight = kernel[ky+2][kx+2];
						r += ((pixel >> 16) & 0xFF) * weight;
						g += ((pixel >> 8) & 0xFF) * weight;
						b += (pixel & 0xFF) * weight;
					}
				}
				int alpha = savedLoadedImage.getRGB(x, y) & 0xFF000000;
				if (!this.filterMode)
					alpha = loadedImage.getRGB(x, y) & 0xFF;
				int newPixel = alpha |
						(clamp((int)r) << 16) |
						(clamp((int)g) << 8)  |
						clamp((int)b);
				changedImage.setRGB(x, y, newPixel);
			}
		}

		loadedImage = changedImage;
		repaint();
	}

	int convertPixelToInverse(int rgb) {
		int alpha = (rgb >> 24) & 0xFF;
		int red = (rgb >> 16) & 0xFF;
		int green = (rgb >> 8) & 0xFF;
		int blue = rgb & 0xFF;

		return (clamp(alpha) << 24) | (clamp(256-red) << 16) | (clamp(256-green) << 8) | (clamp(256-blue));
	}
	int convertPixelToBW(int rgb) {
		int alpha = (rgb >> 24) & 0xFF;
		int red = (rgb >> 16) & 0xFF;
		int green = (rgb >> 8) & 0xFF;
		int blue = rgb & 0xFF;
		int gray = (int)(0.2989 * red + 0.5870 * green + 0.1140 * blue);

		return (alpha << 24) | (gray << 16) | (gray << 8) | gray;
	}

	public void saveAsImage(String filePath, String format) {
		try {
			ImageIO.write(loadedImage, format, new File(filePath));
			System.out.println("Изображение успешно сохранено в " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Ошибка при сохранении изображения: " + e.getMessage());
		}
	}

	public void openImage(String filePath) {
		try {
			fitToScreen = false;
			changingFlag = false;
			loadedImage = ImageIO.read(new File(filePath));
			savedLoadedImage = ImageIO.read(new File(filePath));
			setPreferredSize(new Dimension(
					loadedImage.getWidth() + 2 * BORDER_PADDING,
					loadedImage.getHeight() + 2 * BORDER_PADDING
			));
			revalidate();
			repaint();
			updateScrollBars();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Ошибка при открытии изображения: " + e.getMessage());
		}
	}

	@Override public void mouseClicked(MouseEvent e) {
	}
	@Override public void mousePressed(MouseEvent e) {
		this.lookOther = true;
		this.lastModifiedImage = changedImage;

		dragStartPoint = e.getPoint();
		isDragging = true;

		System.out.println("Начало перемещения");

		repaint();
	}
	@Override public void mouseReleased(MouseEvent e) {
		this.lookOther = false;
		if (lastModifiedImage != null) {
			changedImage = lastModifiedImage;
		}

		isDragging = false;
		System.out.println("Конец перемещения");

		repaint();
	}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (isDragging) {
			Point currentPoint = e.getPoint();
			int dx = currentPoint.x - dragStartPoint.x;
			int dy = currentPoint.y - dragStartPoint.y;

			if (currentPoint.x < 0)
				dx = 0;
			if (currentPoint.y < 0)
				dy = 0;

			imagePosition.translate(dx, dy);

			dragStartPoint = currentPoint;


			/*System.out.println("dragStartPoint.x = " + dragStartPoint.x + ";  dragStartPoint.y = " + dragStartPoint.y);
			System.out.println("dx = " + dx + ";  dy = " + dy);

			System.out.println("imagePosition.x = " + imagePosition.x + ";  imagePosition.y = " + imagePosition.y);
			System.out.println("currentPoint.x = " + currentPoint.x + ";  currentPoint.y = " + currentPoint.y);*/
			repaint();
		}
		System.out.println("mouseDragged");

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}


	public int getImageWidth() {
		if (filterMode) {
			return savedLoadedImage != null ? savedLoadedImage.getWidth() : 0;
		} else {
			return loadedImage != null ? loadedImage.getWidth() : 0;
		}
	}

	public int getImageHeight() {
		if (filterMode) {
			return savedLoadedImage != null ? savedLoadedImage.getHeight() : 0;
		} else {
			return loadedImage != null ? loadedImage.getHeight() : 0;
		}
	}
}