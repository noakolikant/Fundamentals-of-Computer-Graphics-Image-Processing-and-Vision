package RayTracing;

public class SurfaceIntersection {
	public Surface surface;
	public Vector intersection;
	public double distance;
	
	public SurfaceIntersection(Surface surface, Vector intersection, double distance) {
		this.surface = surface;
		this.intersection = intersection;
		this.distance = distance;
	}
}