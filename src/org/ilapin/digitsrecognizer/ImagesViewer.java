package org.ilapin.digitsrecognizer;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ImagesViewer {
	private JPanel rootPanel;
	private JTextField mIndexTextField;
	private JButton mShowButton;
	private ImageView mImageView;
	private JButton mRecognizeButton;

	private ImagesViewer() {
		mShowButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				final int index = Integer.parseInt(mIndexTextField.getText());

				final double[][] matrix;
				try {
					final MatlabProxyFactory factory = new MatlabProxyFactory();
					final MatlabProxy proxy = factory.getProxy();

					proxy.eval(String.format("trainImages(:, :, %d)", (index + 1)));
					final MatlabTypeConverter converter = new MatlabTypeConverter(proxy);
					final MatlabNumericArray array = converter.getNumericArray("ans");

					matrix = array.getRealArray2D();

					proxy.disconnect();
				} catch (MatlabConnectionException | MatlabInvocationException e) {
					throw new RuntimeException(e);
				}

				final BufferedImage bufferedImage = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
				final Graphics2D graphics2D = bufferedImage.createGraphics();
				for (int i = 0; i < matrix.length; i++) {
					for (int j = 0; j < matrix[0].length; j++) {
						final float colorComponent = (float) (1 - matrix[i][j] / 255);
						graphics2D.setColor(new Color(colorComponent, colorComponent, colorComponent, 1));
						graphics2D.drawLine(j, i, j, i);
					}
				}
				graphics2D.dispose();
				mImageView.setImage(bufferedImage);
			}
		});
	}

	public static void main(final String[] args) {
		final JFrame frame = new JFrame("Digits Recognizer");
		frame.setContentPane(new ImagesViewer().rootPanel);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
