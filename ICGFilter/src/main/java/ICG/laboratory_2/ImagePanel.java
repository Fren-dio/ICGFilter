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

	private MainWindowSettings mainWindowSettings;
	public BufferedImage loadedImage;
	public BufferedImage savedLoadedImage;
	public BufferedImage currentViewedImage;
	public BufferedImage changedImage;
	private SelectedSettings selectedSettings;
	private JScrollPane scrollPane;
	private FrameWork frameWork;
	private boolean changingFlag;

	public ImagePanel(SelectedSettings selectedSettings, MainWindowSettings mainWindowSettings, JScrollPane scrollPane, FrameWork frameWork) {
		this.mainWindowSettings = mainWindowSettings;
		this.selectedSettings = selectedSettings;
		this.scrollPane = scrollPane;
		this.frameWork = frameWork;
		this.changingFlag = false;

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
		currentViewedImage = new BufferedImage(
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
				else {
					loadedImage = changedImage;
				}

				int xPos = (availableWidth - drawWidth) / 2 + BORDER_PADDING;
				int yPos = (availableHeight - drawHeight) / 2 + BORDER_PADDING;

				g2d.drawImage(loadedImage, xPos, yPos, this);
				drawDashedBorder(g2d, xPos, yPos, drawWidth, drawHeight);
			} else {
				// Режим реального размера - используем savedLoadedImage
				int xPos = BORDER_PADDING;
				int yPos = BORDER_PADDING;
				if (!changingFlag) {
					loadedImage = savedLoadedImage;
				}
				else {
					loadedImage = changedImage;
				}
				g2d.drawImage(loadedImage, xPos, yPos, this);
				drawDashedBorder(g2d, xPos, yPos,
						loadedImage.getWidth(), loadedImage.getHeight());
			}
		}
		g2d.dispose();
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
		// Всегда используем сохраненное оригинальное изображение
		loadedImage = savedLoadedImage;
		// Устанавливаем размер с учетом оригинального изображения
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
				// В режиме "под экран" используем текущие размеры панели
				imageSize = getSize();
			} else {
				// В реальном размере - размеры оригинального изображения + отступы
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

	// C = 0.2989 * R + 0.5870 * G + 0.1140 * B
	public void changeViewedImageToBW() {
		changingFlag = true;
		int height = loadedImage.getHeight();
		int width = loadedImage.getWidth();
		changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < height; i++) {
			for (int k = 0; k < width; k++) {
				int rgb = loadedImage.getRGB(k, i);
				int alpha = (rgb >> 24) & 0xFF;
				int red = (rgb >> 16) & 0xFF;
				int green = (rgb >> 8) & 0xFF;
				int blue = rgb & 0xFF;

				int gray = (int)(0.2989 * red + 0.5870 * green + 0.1140 * blue);

				int newPixel = (alpha << 24) | (gray << 16) | (gray << 8) | gray;
				changedImage.setRGB(k, i, newPixel);
			}
		}

		loadedImage = changedImage;
		repaint();
		System.out.println("Image converted to BW. Size: " + width + "x" + height);
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

	@Override public void mouseClicked(MouseEvent e) {}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
}