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
	
	public Boolean has_intersection_point_with_ray(Ray r) {
		// I've done it pretty ugly in order to avoid sqrt / divide operations.
		// There must be a prettier way to do it...
		Vector normalize_position = new Vector(this.position); 
		normalize_position.substract(r.start); // move start to the origin
		double dot = normalize_position.dot(r.direction);
		if (dot < 0) {
			return false;
		}
		double x = r.direction.x_cor * normalize_position.y_cor * normalize_position.z_cor;
		double y = r.direction.y_cor * normalize_position.x_cor * normalize_position.z_cor;
		double z = r.direction.z_cor * normalize_position.x_cor * normalize_position.y_cor;
		return (x==y && x ==z);
	}

}
