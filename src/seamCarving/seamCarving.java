package seamCarving;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import RayTracing.RayTracer.RayTracerException;
public class seamCarving {

	public enum EnergyTypes {
	    REGULAR_WITHOUT_ENTROPY, REGULAR_WITH_ENTROPY, FORWARD_ENERGY
	}
	
	public static void main(String[] args) {
		
		String image_file = args[0];
		int cols_num_after = Integer.parseInt(args[1]);
		int rows_num_after = Integer.parseInt(args[2]);
		int energy_type = Integer.parseInt(args[3]);
		String output_image_file = args[4];
		
		try
	    {
	      BufferedImage input_image = ImageIO.read(new File(image_file));
	      int delta_cols = input_image.getHeight() - cols_num_after;
	      int delta_rows = input_image.getWidth() - rows_num_after;
	      
	      // work with the image here :
	      //TODO: first understand how many rows and cols should be added or removed
	    } 
	    catch (IOException e)
	    {
			System.out.println(e.getMessage());
	    }
		

	}

}
