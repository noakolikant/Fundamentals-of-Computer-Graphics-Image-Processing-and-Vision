package RayTracing;

public class Sphere {
	
	Vector center;
	double radius;
	
	public Sphere(Vector center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	public boolean has_intersection(Ray ray) {
		Vector l = new Vector(this.center);
		l.substract(ray.start);
		return (l.dot(ray.direction) >= 0);
	}
	public Vector findIntersection(Ray ray) {
		// TODO: check it and change params names
		if (!this.has_intersection(ray)) {
			System.out.println("findIntersection assumes there is an intersection");
			return this.center; // exit ?
		}
		// compute the vector between the center of the sphere and the start of the ray
		Vector l = new Vector(this.center);
		l.substract(ray.start); 
		
		Vector v = new Vector(ray.direction); // TODO: direction should be from ray.start !
		// TODO: direction should be normalize !
		v.normalize();
		v.multiplyByScalar(l.dot(ray.direction));
		
		double d = l.lengthSquared() - v.lengthSquared();
		double a = Math.sqrt((this.radius * this.radius) - (d * d));
		
		double t = Math.min(v.length() + a, v.length() - a);
		
		Vector result = new Vector(ray.direction);
		result.multiplyByScalar(t);
		return result;
	}

}
