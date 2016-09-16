package a7;

import java.awt.BorderLayout;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;



public class PixelInspectorWidget extends JPanel implements MouseListener {
	
	private PictureView pictureView;
	private JLabel labelX;
	private JLabel labelY;
	private JLabel labelRed;
	private JLabel labelGreen;
	private JLabel labelBlue;
	private JLabel labelBrightness;
	
	public PixelInspectorWidget(Picture picture, String title) {
		setLayout(new BorderLayout());
		
		pictureView = new PictureView(picture.createObservable());
		pictureView.addMouseListener(this);
		add(pictureView, BorderLayout.CENTER);
		
		JLabel titleLabel = new JLabel(title);
		add(titleLabel, BorderLayout.WEST);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(6,1));
		add(infoPanel, BorderLayout.WEST);
		
		labelX = new JLabel("X:");
		infoPanel.add(labelX);
		labelY = new JLabel("Y:");
		infoPanel.add(labelY);
		labelRed = new JLabel("Red:");
		infoPanel.add(labelRed);
		labelGreen = new JLabel("Green:");
		infoPanel.add(labelGreen);
		labelBlue = new JLabel("Blue:");
		infoPanel.add(labelBlue);
		labelBrightness = new JLabel("Brightness:");
		infoPanel.add(labelBrightness);										
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Pixel pixel = pictureView.getPicture().getPixel(e.getX(), e.getY());
		
		labelX.setText("X:" + e.getX());
		labelY.setText("Y:" + e.getY());
		labelRed.setText("Red:" + (  (double) Math.round((pixel.getRed())*100))/100  ); //truncating to 2 decimals
		labelGreen.setText("Green:" + (  (double) Math.round((pixel.getGreen())*100))/100  );
		labelBlue.setText("Blue:" + (  (double) Math.round((pixel.getBlue())*100))/100  );
		labelBrightness.setText("Brightness:" + (  (double) Math.round((pixel.getIntensity())*100))/100  );
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
