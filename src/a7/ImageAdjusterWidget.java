package a7;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ImageAdjusterWidget extends JPanel implements ChangeListener {

	private Picture picture;
	private PictureView pictureView;
	private JLabel labelBlur;
	private JLabel labelSaturation;
	private JLabel labelBrightness;
	private JSlider blurSlider;
	private JSlider saturationSlider;
	private JSlider brightnessSlider;
	List<ChangeListener> change_listeners;

	public ImageAdjusterWidget(Picture picture, String title) {
		setLayout(new BorderLayout());

		this.picture = picture;
		pictureView = new PictureView(picture.createObservable());
		add(pictureView, BorderLayout.CENTER);

		JLabel titleLabel = new JLabel(title);
		add(titleLabel, BorderLayout.SOUTH);

		JPanel sliderPanel = new JPanel();
//		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
		sliderPanel.setLayout(new GridLayout(3,1));
		
		labelBlur = new JLabel("Blur:");
		labelSaturation = new JLabel("Saturation:");
		labelBrightness = new JLabel("Brightness:");

		blurSlider = new JSlider(0, 5, 0); // ( minimum, max, starting point)
		saturationSlider = new JSlider(-100, 100, 0);
		brightnessSlider = new JSlider(-100, 100, 0);
		blurSlider.setBorder(BorderFactory.createTitledBorder("Blur: "));
		saturationSlider.setBorder(BorderFactory.createTitledBorder("Saturation: "));
		brightnessSlider.setBorder(BorderFactory.createTitledBorder("Brightness: "));

		blurSlider.addChangeListener(this);
		saturationSlider.addChangeListener(this);
		brightnessSlider.addChangeListener(this);

		blurSlider.setMajorTickSpacing(1);
		blurSlider.setPaintTicks(true);
		blurSlider.setPaintLabels(true);
		blurSlider.setSnapToTicks(true);

		saturationSlider.setMajorTickSpacing(25);
		saturationSlider.setPaintTicks(true);
		saturationSlider.setPaintLabels(true);

		brightnessSlider.setMajorTickSpacing(25);
		brightnessSlider.setPaintTicks(true);
		brightnessSlider.setPaintLabels(true);

//		sliderPanel.add(labelBlur); // adding labels to slider panel
		sliderPanel.add(blurSlider);
//		sliderPanel.add(labelSaturation);
		sliderPanel.add(saturationSlider);
//		sliderPanel.add(labelBrightness);
		sliderPanel.add(brightnessSlider);
		
		add(sliderPanel, BorderLayout.SOUTH);
		
		change_listeners = new ArrayList<ChangeListener>();
	}

	public Picture blur(Picture p, int blurValue) {
		if (blurValue == 0) {
			return p;
		}
		int width = p.getWidth();
		int height = p.getHeight();
		Picture newBlurPicture = new PictureImpl(width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int numPixels = 0;
				double red = 0.0;
				double green = 0.0;
				double blue = 0.0;
				for (int x = i - blurValue; x <= i + blurValue; x++) {
					for (int y = j - blurValue; y <= j + blurValue; y++) {
						if (x >= width || x < 0 || y >= height || y < 0 || x == i || y == j) {
							continue;
						}
						Pixel pPixel = p.getPixel(x, y);
						red += pPixel.getRed();
						green += pPixel.getGreen();
						blue += pPixel.getBlue();
						numPixels++;
					}
				}
				red /= numPixels;
				green /= numPixels;
				blue /= numPixels;
				Pixel newPixel = new ColorPixel(red, green, blue);
				newBlurPicture.setPixel(i, j, newPixel);
			}
		}
		return newBlurPicture;
	}

	public Picture saturation(Picture p, double saturationValue) {
		int width = p.getWidth();
		int height = p.getHeight();
		double f = saturationValue;
		double red;
		double green;
		double blue;
		Picture newSaturatedPicture = new PictureImpl(width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Pixel pPixel = p.getPixel(i, j);
				double b = p.getPixel(i,j).getIntensity();
				red = pPixel.getRed();
				green = pPixel.getGreen();
				blue = pPixel.getBlue(); 
				
				if (red == 0.0 && green == 0.0 && blue == 0.0){
					red = 0.0;
					green = 0.0;
					blue = 0.0;
				}
				if (f>= -100 && f<= 0) {
					red = red * (1.0 + (f / 100.0) ) - (b * f / 100.0);
					green = green * (1.0 + (f / 100.0) ) - (b * f / 100.0);
					blue = blue * (1.0 + (f / 100.0) ) - (b * f / 100.0);
				}
				if (f>0 && f<=100) {
					double a = 0.0;
					if ( red >= green && red >= blue ) {a = red;}
				      else if ( green >= red && green >= blue ) { a = green;}
				      	else if ( blue >= red && blue >= green ) {a = blue;}
					red = red * ((a + ((1.0 - a) * (f / 100.0))) / a);
					green = green * ((a + ((1.0 - a) * (f / 100.0))) / a);
					blue = blue * ((a + ((1.0 - a) * (f / 100.0))) / a);
				}
				Pixel newPixel = new ColorPixel(red, green, blue);
				newSaturatedPicture.setPixel(i, j, newPixel);
			}
		}
		return newSaturatedPicture;
	}

	public Picture brightness(Picture p, double brightnessValue) {
		int width = p.getWidth();
		int height = p.getHeight();
		Picture newBrightnessPicture = new PictureImpl(width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Pixel pPixel = p.getPixel(i, j);
				Pixel blendPixel = null;
				ColorPixel white = new ColorPixel(1.0, 1.0, 1.0);
				ColorPixel black = new ColorPixel(0.0, 0.0, 0.0);

				if (brightnessValue > 0) {
					blendPixel = pPixel.blend(white, 1 - (brightnessValue / 100.0));
				} else {
					blendPixel = pPixel.blend(black, 1 - Math.abs((brightnessValue / 100.0)));
				}
				newBrightnessPicture.setPixel(i, j, blendPixel);
			}
		}
		return newBrightnessPicture;
	}

	public void stateChanged(ChangeEvent e) {
		int width = picture.getWidth();
		int height = picture.getHeight();

		// creates a new copied picture to change
		Picture changedPicture = new PictureImpl(width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Pixel p = picture.getPixel(i, j);
				changedPicture.setPixel(i, j, p);
			}
		}
		changedPicture = blur(changedPicture, blurSlider.getValue());
		changedPicture = saturation(changedPicture, saturationSlider.getValue());
		changedPicture = brightness(changedPicture, brightnessSlider.getValue());
		pictureView.setPicture(changedPicture.createObservable());
	}

	public void addChangeListener(ChangeListener l) {
		change_listeners.add(l);
	}

	public void removeChangeListener(ChangeListener l) {
		change_listeners.remove(l);
	}
}
