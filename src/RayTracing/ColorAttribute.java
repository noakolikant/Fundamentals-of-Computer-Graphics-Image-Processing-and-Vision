package RayTracing;

public class ColorAttribute {
	double red;
	double green;
	double blue;
	
	public ColorAttribute(double red, double green, double blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public ColorAttribute(double red, double green, double blue, double factor) {
		this.red = red * factor;
		this.green = green * factor;
		this.blue = blue * factor;
	}
	
	public ColorAttribute(ColorAttribute color, double factor) {
		this.red = color.red * factor;
		this.green = color.green * factor;
		this.blue = color.blue * factor;
	}
}
