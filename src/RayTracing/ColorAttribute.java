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

	public ColorAttribute(Color color, double specular_intensity) {
		this.red = color.red * specular_intensity;
		this.green = color.green * specular_intensity;
		this.blue = color.blue * specular_intensity;
	}
}
