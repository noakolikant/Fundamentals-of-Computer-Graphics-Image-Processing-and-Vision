package RayTracing;

public class Ray {
	
	public Vector start;
	public Vector direction;
	
	public Ray(Vector start, Vector direction) {
		this.start = start;
		this.direction = direction; // TODO: assuming the direction is from start
		this.direction.normalize();
	}

}
