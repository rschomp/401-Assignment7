package a7;

public interface Pixel {

	public double getRed();
	public double getBlue();
	public double getGreen();
	public double getIntensity();
	public char getChar();	
	public Pixel blend(Pixel p, double weight);
}
