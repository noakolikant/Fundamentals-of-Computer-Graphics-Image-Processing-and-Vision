package RayTracing;

public class LightSourceIntersection {
	public LightSource light_source;
	public Vector intersection;
	public double distance;
	
	public LightSourceIntersection(LightSource light_source, Vector intersection, double distance) {
		this.light_source = light_source;
		this.intersection = intersection;
		this.distance = distance;
	}
}