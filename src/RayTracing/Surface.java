package RayTracing;

public interface Surface {
	
	/**
	 * Returns the intersection point represented as a Vector. If there is not returns null
	 * @param r A ray
	 * @return Intersection point if exists
	 */
	public Vector get_intersection_point_with_surface(Ray r);
	
	/**
	 * returns the mirror ray after intersection with incoming ray
	 * @param Vector intersection_point - Where does the incoming ray meet the Surface
	 * @param Ray incomming_ray - incoming Ray representation
	 * @return the mirror outgoing ray
	 */
	public Ray get_mirrror_ray(Vector intersection_point, Ray incomming_ray);
}
