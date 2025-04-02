package ICG.laboratory_2;

import ICG.laboratory_2.Settings.MainWindowSettings;
import ICG.laboratory_2.Settings.SelectedSettings;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImagePanel extends JPanel implements MouseListener {
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
	private boolean filterMode = false;

	public void setFilterMode(boolean mode) {
		this.filterMode = mode;
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
	}

	@Override
	protected void paintComponent(Graphics g) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
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
					sg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
							RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					sg.drawImage(savedLoadedImage, 0, 0, drawWidth, drawHeight, null);
					sg.dispose();

					loadedImage = scaledImage;
				}
				else if (lookOther) {
					BufferedImage scaledImage = new BufferedImage(drawWidth, drawHeight, BufferedImage.TYPE_INT_ARGB);
					Graphics2D sg = scaledImage.createGraphics();
					sg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
							RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					sg.drawImage(savedLoadedImage, 0, 0, drawWidth, drawHeight, null);
					sg.dispose();

					loadedImage = scaledImage;
				}
				else {
					BufferedImage scaledImage = new BufferedImage(drawWidth, drawHeight, BufferedImage.TYPE_INT_ARGB);
					Graphics2D sg = scaledImage.createGraphics();
					sg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
							RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					sg.drawImage(changedImage, 0, 0, drawWidth, drawHeight, null);
					sg.dispose();
					loadedImage = scaledImage;
				}

				int xPos = (availableWidth - drawWidth) / 2 + BORDER_PADDING;
				int yPos = (availableHeight - drawHeight) / 2 + BORDER_PADDING;

				g2d.drawImage(loadedImage, xPos, yPos, this);
				drawDashedBorder(g2d, xPos, yPos, drawWidth, drawHeight);
			} else {
				int xPos = BORDER_PADDING;
				int yPos = BORDER_PADDING;
				if (!changingFlag) {
					loadedImage = savedLoadedImage;
					g2d.drawImage(loadedImage, xPos, yPos, this);
					drawDashedBorder(g2d, xPos, yPos,
							loadedImage.getWidth(), loadedImage.getHeight());
				}
				else if (lookOther) {
					loadedImage = savedLoadedImage;
					g2d.drawImage(loadedImage, xPos, yPos, this);
					drawDashedBorder(g2d, xPos, yPos,
							loadedImage.getWidth(), loadedImage.getHeight());
				}
				else {
					loadedImage = changedImage;
					g2d.drawImage(loadedImage, xPos, yPos,
							savedLoadedImage.getWidth(), savedLoadedImage.getHeight(), this);
					drawDashedBorder(g2d, xPos, yPos,
							savedLoadedImage.getWidth(), savedLoadedImage.getHeight());
				}
			}
		}
		g2d.dispose();
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}



	public void outerCommandResizeToScreen() {
		fitToScreen = true;
		Dimension viewSize = scrollPane.getViewport().getExtentSize();
		setPreferredSize(viewSize);

		scrollPane.getViewport().setViewSize(viewSize);
		scrollPane.getViewport().setViewPosition(new Point(0, 0));

		revalidate();
		repaint();
	}

	public void outerCommandResizeToReal() {
		fitToScreen = false;
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
						savedLoadedImage.getHeight() + 2 * BORDER_PADDING
				);
			}

			boolean needHorizontal = imageSize.width > viewportSize.width;
			boolean needVertical = imageSize.height > viewportSize.height;

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
		sg.drawImage(savedLoadedImage, 0, 0, width, height, null);
		sg.dispose();

		loadedImage = scaledImage;
	}


	void convertPixelToEmboss() {
		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		if (this.filterMode) {
			loadStartImage(height, width);
		}

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
						int pixel = loadedImage.getRGB(x + kx, y + ky);
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
		int width = loadedImage.getWidth();
		int height = loadedImage.getHeight();
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		if (this.filterMode) {
			loadStartImage(height, width);
		}

		// Предварительно вычисляем таблицу преобразования (для оптимизации)
		int[] gammaLUT = new int[256];
		for (int i = 0; i < 256; i++) {
			gammaLUT[i] = (int)(255 * Math.pow(i / 255.0, 1.0 / gamma));
		}

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = loadedImage.getRGB(x, y);

				// Извлекаем каналы с сохранением альфа-канала
				int alpha = (pixel >> 24) & 0xFF;
				int red   = (pixel >> 16) & 0xFF;
				int green = (pixel >> 8)  & 0xFF;
				int blue  =  pixel        & 0xFF;

				// Применяем гамма-коррекцию через LUT
				red   = gammaLUT[red];
				green = gammaLUT[green];
				blue  = gammaLUT[blue];

				// Собираем пиксель обратно
				int newPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
				changedImage.setRGB(x, y, newPixel);
			}
		}

		loadedImage = changedImage;
		repaint();
	}

	void edgeDetectionConvert(ToolBarMenu.EdgeDetectionType type, int threshold){
		changingFlag = true;
		int width = loadedImage.getWidth();
		int height = loadedImage.getHeight();
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		if (this.filterMode) {
			loadStartImage(height, width);
		}

		BufferedImage grayImage = convertToGray(loadedImage);

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

	void convertPixelByGausse3() {
		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		if (this.filterMode) {
			loadStartImage(height, width);
		}

		float[][] kernel = {
				{1/16f, 2/16f, 1/16f},
				{2/16f, 4/16f, 2/16f},
				{1/16f, 2/16f, 1/16f}
		};

		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				float r = 0, g = 0, b = 0;
				for (int ky = -1; ky <= 1; ky++) {
					for (int kx = -1; kx <= 1; kx++) {
						int pixel = loadedImage.getRGB(x + kx, y + ky);
						r += ((pixel >> 16) & 0xFF) * kernel[ky+1][kx+1];
						g += ((pixel >> 8) & 0xFF) * kernel[ky+1][kx+1];
						b += (pixel & 0xFF) * kernel[ky+1][kx+1];
					}
				}
				int newPixel = (255 << 24) | ((int)r << 16) | ((int)g << 8) | (int)b;
				changedImage.setRGB(x, y, newPixel);
			}
		}

		loadedImage = changedImage;
		repaint();
	}

	void convertPixelByGausse(int size) {
		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		if (this.filterMode) {
			loadStartImage(height, width);
		}

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
		else if (size == 7) {
			kernel = new float[][]{
					{0.000789f, 0.003157f, 0.006622f, 0.008866f, 0.006622f, 0.003157f, 0.000789f},
					{0.003157f, 0.012626f, 0.026488f, 0.035465f, 0.026488f, 0.012626f, 0.003157f},
					{0.006622f, 0.026488f, 0.055567f, 0.074408f, 0.055567f, 0.026488f, 0.006622f},
					{0.008866f, 0.035465f, 0.074408f, 0.099654f, 0.074408f, 0.035465f, 0.008866f},
					{0.006622f, 0.026488f, 0.055567f, 0.074408f, 0.055567f, 0.026488f, 0.006622f},
					{0.003157f, 0.012626f, 0.026488f, 0.035465f, 0.026488f, 0.012626f, 0.003157f},
					{0.000789f, 0.003157f, 0.006622f, 0.008866f, 0.006622f, 0.003157f, 0.000789f}
			};
		}
		else if (size == 9) {
			kernel = new float[][]{
					{0.000036f, 0.000238f, 0.000858f, 0.001796f, 0.002352f, 0.001796f, 0.000858f, 0.000238f, 0.000036f},
					{0.000238f, 0.001564f, 0.005648f, 0.011811f, 0.015468f, 0.011811f, 0.005648f, 0.001564f, 0.000238f},
					{0.000858f, 0.005648f, 0.020403f, 0.042671f, 0.055886f, 0.042671f, 0.020403f, 0.005648f, 0.000858f},
					{0.001796f, 0.011811f, 0.042671f, 0.089235f, 0.116883f, 0.089235f, 0.042671f, 0.011811f, 0.001796f},
					{0.002352f, 0.015468f, 0.055886f, 0.116883f, 0.153073f, 0.116883f, 0.055886f, 0.015468f, 0.002352f},
					{0.001796f, 0.011811f, 0.042671f, 0.089235f, 0.116883f, 0.089235f, 0.042671f, 0.011811f, 0.001796f},
					{0.000858f, 0.005648f, 0.020403f, 0.042671f, 0.055886f, 0.042671f, 0.020403f, 0.005648f, 0.000858f},
					{0.000238f, 0.001564f, 0.005648f, 0.011811f, 0.015468f, 0.011811f, 0.005648f, 0.001564f, 0.000238f},
					{0.000036f, 0.000238f, 0.000858f, 0.001796f, 0.002352f, 0.001796f, 0.000858f, 0.000238f, 0.000036f}
			};
		}
		else if (size == 11) {
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
		}

		for (int y = shift; y < height - shift; y++) {
			for (int x = shift; x < width - shift; x++) {
				float r = 0, g = 0, b = 0;
				for (int ky = -shift; ky <= shift; ky++) {
					for (int kx = -shift; kx <= shift; kx++) {
						int pixel = loadedImage.getRGB(x + kx, y + ky);
						float weight = kernel[ky+shift][kx+shift];
						r += ((pixel >> 16) & 0xFF) * weight;
						g += ((pixel >> 8) & 0xFF) * weight;
						b += (pixel & 0xFF) * weight;
					}
				}
				int newPixel = (255 << 24) | ((int)r << 16) | ((int)g << 8) | (int)b;
				changedImage.setRGB(x, y, newPixel);
			}
		}

		loadedImage = changedImage;
		repaint();
	}

	void convertPixelBySharp3(boolean light) {
		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		if (this.filterMode) {
			loadStartImage(height, width);
		}

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
						int pixel = loadedImage.getRGB(x + kx, y + ky);
						float weight = kernel[ky+1][kx+1];
						r += ((pixel >> 16) & 0xFF) * weight;
						g += ((pixel >> 8) & 0xFF) * weight;
						b += (pixel & 0xFF) * weight;
					}
				}
				int alpha = loadedImage.getRGB(x, y) & 0xFF000000;
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
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		if (this.filterMode) {
			loadStartImage(height, width);
		}

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
						int pixel = loadedImage.getRGB(x + kx, y + ky);
						float weight = kernel[ky+2][kx+2];
						r += ((pixel >> 16) & 0xFF) * weight;
						g += ((pixel >> 8) & 0xFF) * weight;
						b += (pixel & 0xFF) * weight;
					}
				}
				int alpha = loadedImage.getRGB(x, y) & 0xFF000000;
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
		repaint();
	}
	@Override public void mouseReleased(MouseEvent e) {
		this.lookOther = false;
		if (lastModifiedImage != null) {
			changedImage = lastModifiedImage;
		}
		repaint();
	}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
}