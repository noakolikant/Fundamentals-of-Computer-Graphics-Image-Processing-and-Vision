package seamCarving;

import java.util.ArrayList;
import java.util.List;


public class Seam {
	
	int seam_length;
	List<Pixel> pixels_list;
	
	public Seam(int seam_length)
	{
		this.seam_length = seam_length;
		this.pixels_list = new ArrayList<Pixel>();
	}
	
	public void insert_pixel(Pixel p)
	{
		if(this.pixels_list.size() < this.seam_length)
		{
			this.pixels_list.add(p);
		}
		else
		{
			System.out.println("Trying to add another pixel to full seam failed.");
			System.exit(-1);
		}
	}
}
