package RayTracing;

public class LightSource {
	Vector position;
	ColorAttribute color;
	ColorAttribute specular_intnesity;
	double shadow_intensity;
	double light_radius;
	
	public LightSource(Vector position, ColorAttribute color, 
			double specular_intnesity, double shadow_intensity,
			double light_radius) {
		this.position = position;
		this.color = color;
		this.specular_intnesity = new ColorAttribute(this.color, specular_intnesity);
		this.shadow_intensity = shadow_intensity;
		this.light_radius = light_radius;
	}

}
