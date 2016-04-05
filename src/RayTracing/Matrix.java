package RayTracing;

public class Matrix {
	    public double[][] data;

	    public Matrix() {
	        this.data = new double[4][4]; //Matrix in Ray Tracing 3D are always 4X4
	    }
	    
	    /*
	     * Creates rotation matrix with axis and degree parameters.
	     */
	    public Matrix(char axis, double degree)
	    {
	    	this.data = new double[4][4];
	    	for (int i =0 ; i < 4; i ++)
	    	{
	    		for(int j = 0; j < 4; j++)
	    		{
	    			if(i == j)
	    			{
	    				this.data[i][j] = 1;
	    			}
	    			else
	    			{
	    				this.data[i][j] = 0;
	    			}
	    		}
	    	}
	    	if('x' == axis)
	    	{
	    		this.data[1][1] = Math.cos(degree);
	    		this.data[1][2] = -Math.sin(degree);
	    		this.data[2][1] = Math.sin(degree);
	    		this.data[2][2] = Math.cos(degree);
	    	}
	    	else if('y' == axis)
	    	{
	    		this.data[0][0] = Math.cos(degree);
	    		this.data[0][2] = Math.sin(degree);
	    		this.data[2][0] = -Math.sin(degree);
	    		this.data[3][3] = Math.cos(degree);
	    	}
	    	else if('z' == axis)
	    	{
	    		this.data[0][0] = Math.cos(degree);
	    		this.data[0][1] = -Math.sin(degree);
	    		this.data[1][0] = Math.sin(degree);
	    		this.data[1][1] = Math.cos(degree);
	    	}
	    }
}
