package RayTracing;

public class Camera {
	Vector position;
	Vector look_at_point;
	Vector direction;
	Vector up_vector;
	double screen_distance;
	double screen_width;
	
	public Camera(Vector position, Vector look_at_point, Vector up_vector,
			double screen_distnace, double screen_width) {
		this.position = new Vector(position);
		this.look_at_point = new Vector(look_at_point);
		// find the direction vector
		this.direction = new Vector(this.look_at_point); // copy the vector
		this.direction.substract(position);
		// fix the up vector to be perpendicular to the direction
		this.up_vector = new Vector(up_vector);
		Vector temp = new Vector(this.direction); // copy this vector
		temp.multiplyByScalar(temp.dot(up_vector));
		this.up_vector.substract(temp);
		assert this.up_vector.dot(this.direction) == 0;
		this.screen_distance = screen_distnace;
		this.screen_width = screen_width;
	}
	
	public Vector get_center_of_screen() {
		Vector pixel_location = new Vector(this.direction);
		pixel_location.normalize();
		pixel_location.multiplyByScalar(this.screen_distance);
		pixel_location.add(this.position); // now pixel_direction is pointing to the middle of the screen
		return pixel_location;
	}
}
