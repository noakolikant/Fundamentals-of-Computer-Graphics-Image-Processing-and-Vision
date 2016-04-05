package RayTracing;

public class Vector {
	double x_cor;
	double y_cor;
	double z_cor;
	
	public Vector(double x, double y, double z)
	{
		this.x_cor = x;
		this.y_cor = y;
		this.z_cor = z;
	}
	
	public Vector (Vector v) {
		this.x_cor = v.x_cor;
		this.y_cor = v.y_cor;
		this.z_cor = v.z_cor;
	}
	
	/**
	 * Add other vector to self
	 * @param other Another vector
	 */
	public void add(Vector other) {
		this.x_cor += other.x_cor;
		this.y_cor += other.y_cor;
		this.z_cor += other.z_cor;
	}
	
	public void substract(Vector other) {
		this.x_cor -= other.x_cor;
		this.y_cor -= other.y_cor;
		this.z_cor -= other.z_cor;
	}
	
	/**
	 * Return the negative of this vector
	 */
	public void neg() {
		this.x_cor = -this.x_cor;
		this.y_cor = -this.y_cor;
		this.z_cor = -this.z_cor;
	}
	
	/**
	 * Returns the squared length of this vector
	 * @return Squared length of this vector
	 */
	public double lengthSquared() {
		return ((this.x_cor * this.x_cor) + 
				(this.y_cor * this.y_cor) +
				(this.z_cor * this.z_cor));
	}
	
	/**
	 * Returns the length of this vector
	 * @return Length of this vector
	 */
	public double length() {
		return Math.sqrt(lengthSquared());
	}
	
	/**
	 * Returns the destination to point p
	 * @return destination to point p
	 */
	public double destinstion_from_point(Vector p) {
		Vector delta = new Vector(p);
		delta.substract(this);
		return delta.length();
	}
	
	/**
	 * Normalize this vector
	 */
	public void normalize() {
		double length = length();
		assert length != 0;
		this.x_cor /= length;
		this.y_cor /= length;
		this.z_cor /= length;
	}
	
	/**
	 * Multiply this vector by a scalar.
	 * @param scalar A double
	 */
	public void multiplyByScalar(double scalar) {
		this.x_cor *= scalar;
		this.y_cor *= scalar;
		this.z_cor *= scalar;
	}
	
	/**
	 * Returns the dot product of this vector with other
	 * @param other Another vector
	 * @return Dot product of this vector with other
	 */
	public double dot(Vector other) {
		return ((this.x_cor * other.x_cor) + 
				(this.y_cor * other.y_cor) + 
				(this.z_cor * other.z_cor));
	}
	
	/**
	 * Sets self to the cross product of self and other
	 * @param other Another vector
	 */
	public void cross(Vector other) {
		// Compute the vector on temporary variables in order
		// to do a safe computation (don't change coordinates 
		// while computing)
		double x = (this.y_cor * other.z_cor) - (this.z_cor * other.y_cor);
		double y = (this.z_cor * other.x_cor) - (this.x_cor * other.z_cor);
		double z = (this.x_cor * other.y_cor) - (this.y_cor * other.x_cor);
		
		this.x_cor = x;
		this.y_cor = y;
		this.z_cor = z;
	}
	
	/**
	 * Sets self to a component-wise multiplication with other
	 * @param other Another vector
	 */
	public void componentWiseMult(Vector other) {
		this.x_cor *= other.x_cor;
		this.y_cor *= other.y_cor;
		this.z_cor *= other.z_cor;
	}
	
	/**
	 * Sets Vector after multiplying with a Matrix. M * v
	 * @param M - Matrix to multipy from the left
	 */
	public void multiply_with_Matrix(Matrix M)
	{
		double tmp_calc;
		
		for(int i = 0; i < 3; i ++)
		{
			tmp_calc = M.data[i][0] * this.x_cor + M.data[i][1] * this.y_cor + M.data[i][2] * this.z_cor + M.data[i][3] * 1;
			switch (i)
			{
			case 0:
				this.x_cor = tmp_calc;
				break;
			case 1:
				this.y_cor = tmp_calc;
				break;
			case 2:
				this.z_cor = tmp_calc;
				break;
			}
		}
	}
	/**
	 * Sets self new Vector after rotation
	 * @param axis - can be 'x', 'y' or 'z'
	 * @param degree - angle in degree to rotate
	 */
	public void rotate_vector(char axis, double degree) {
		Matrix rotatiom_mat = new Matrix(axis, degree);
		this.multiply_with_Matrix(rotatiom_mat);
	}
}
