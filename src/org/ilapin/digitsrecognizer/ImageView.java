package org.ilapin.digitsrecognizer;

import javax.swing.*;
import java.awt.*;

public class ImageView extends JPanel {

	private Image mImage;

	public void setImage(final Image image) {
		mImage = image;
		repaint();
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);

		if (mImage != null) {
			g.drawImage(mImage, 0, 0, getWidth(), getHeight(), null);
		}
	}
}
