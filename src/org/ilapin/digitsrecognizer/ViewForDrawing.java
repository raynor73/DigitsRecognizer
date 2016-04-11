package org.ilapin.digitsrecognizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class ViewForDrawing extends JPanel {

	private final List<List<Point>> mSegments = new ArrayList<>();

	private final MouseMotionListener mMouseMotionListener = new MouseMotionListener() {

		@Override
		public void mouseDragged(final MouseEvent e) {
			System.out.println(String.format("%d; %d", e.getX(), e.getY()));
		}

		@Override
		public void mouseMoved(final MouseEvent e) {
			// do nothing
		}
	};

	public ViewForDrawing() {
		addMouseMotionListener(mMouseMotionListener);
	}

	@Override
	protected void paintComponent(final Graphics g) {

	}

	private enum State {
		IDLE, DRAWING
	}
}
