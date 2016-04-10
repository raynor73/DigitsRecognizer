package org.ilapin.digitsrecognizer;

import javax.swing.*;
import java.awt.*;

public class ImageView extends JPanel {

	private Image mImage;

	public void setmImage(final Image image) {
		mImage = image;
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);

		if (mImage != null) {
			g.drawImage(mImage, 0, 0, getWidth(), getHeight(), null);
		}
	}
}
