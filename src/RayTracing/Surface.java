package RayTracing;

public interface Surface {
	
	/**
	 * Returns the intersection point represented as a Vector. If there is not returns null
	 * @param v The direction vector, assuming the ray starts at the origin
	 * @return Intersection point if exists
	 */
	public Vector get_intersection_point_with_surface(Vector v);
	
	/**
	 * Returns the intersection point represented as a Vector. If there is not returns null
	 * @param r A ray
	 * @return Intersection point if exists
	 */
	public Vector get_intersection_point_with_surface(Ray r);
}
