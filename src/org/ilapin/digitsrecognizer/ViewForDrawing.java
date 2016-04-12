package org.ilapin.digitsrecognizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class ViewForDrawing extends JPanel {

	private final static int END_DRAWING_TIMEOUT = 500; //millis
	private final static int IMAGE_ROWS = 28;
	private final static int IMAGE_COLUMNS = 28;

	private final Color mBackgroundColor = Color.WHITE;
	private final Color mDrawColor = Color.BLACK;

	private final List<List<Point>> mSegments = new ArrayList<>();

	private Listener mListener;

	private State mState = State.IDLE;

	private final Timer mTimer = new Timer(END_DRAWING_TIMEOUT, new ActionListener() {

		@Override
		public void actionPerformed(final ActionEvent e) {
			mState = State.IDLE;
			if (mListener != null) {
				final double[][] image = new double[IMAGE_ROWS][IMAGE_COLUMNS];
				mListener.onDrawingEnded(image);
			}
		}
	});

	@SuppressWarnings("FieldCanBeLocal")
	private final MouseMotionListener mMouseMotionListener = new MouseMotionListener() {

		private boolean mIsFirstDragEvent = true;

		@Override
		public void mouseDragged(final MouseEvent e) {
			switch (mState) {
				case IDLE:
					mState = State.DRAWING;
					final Point point = new Point(e.getX(), e.getY());
					if (mIsFirstDragEvent) {
						mIsFirstDragEvent = false;
						final ArrayList<Point> segment = new ArrayList<>();
						segment.add(point);
						mSegments.add(segment);
					} else {
						final List<Point> currentSegment = mSegments.get(mSegments.size() - 1);
						currentSegment.add(point);
					}

				case DRAWING:
				case AWAITING_FOR_DRAWING:
			}
			repaint();
		}

		@Override
		public void mouseMoved(final MouseEvent e) {
			switch (mState) {
				case DRAWING:
					mState = State.AWAITING_FOR_DRAWING;
					mTimer.start();

				case AWAITING_FOR_DRAWING:
			}
		}
	};

	public ViewForDrawing() {
		addMouseMotionListener(mMouseMotionListener);
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);

		g.setColor(mBackgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(mDrawColor);

		for (final List<Point> segmentPoints : mSegments) {
			for (int i = 1; i < segmentPoints.size(); i++) {
				final Point prevPoint = segmentPoints.get(i - 1);
				final Point point = segmentPoints.get(i);
				g.drawLine(prevPoint.x, prevPoint.y, point.x, point.y);
			}
		}
	}

	public void setListener(final Listener listener) {
		mListener = listener;
	}

	public interface Listener {

		void onDrawingEnded(double[][] image);
	}

	private enum State {
		IDLE, DRAWING, AWAITING_FOR_DRAWING
	}
}
