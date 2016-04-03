package RayTracing;

public interface Surface {
	
	/**
	 * Returns the intersection point represented as a Vector. If there is not returns null
	 * @param other Another vector
	 * @return Intersection point if exists
	 */
	public Vector get_intersection_point_with_surface(Vector v);
}
