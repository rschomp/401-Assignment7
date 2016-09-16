package a7;

public class GrayPixel implements Pixel {

	private double intensity;

	private static final char[] PIXEL_CHAR_MAP = {'#', 'M', 'X', 'D', '<', '>', 's', ':', '-', ' '};


	public GrayPixel(double intensity) {
		if (intensity < 0.0 || intensity > 1.0) {
			throw new IllegalArgumentException("Intensity of gray pixel is out of bounds.");
		}
		this.intensity = intensity;
	}

	@Override
	public double getRed() {
		return getIntensity();
	}

	@Override
	public double getBlue() {
		return getIntensity();
	}

	@Override
	public double getGreen() {
		return getIntensity();
	}

	@Override
	public double getIntensity() {
		return intensity;
	}

	@Override
	public char getChar() {
		return PIXEL_CHAR_MAP[(int) (getIntensity()*10.0)];
	}

	@Override
	public Pixel blend(Pixel p, double weight) {
		double blendRed = this.getRed() * (weight) + p.getRed()*(1-weight);
		double blendBlue = this.getBlue() * (weight) + p.getBlue()*(1-weight);
		double blendGreen = this.getBlue() * (weight) + p.getGreen()*(1-weight);

		ColorPixel cp = new ColorPixel(blendRed, blendGreen, blendBlue);
		return cp;
	}	
}
