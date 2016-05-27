package seamCarving;

import java.util.ArrayList;
import java.util.List;


public class Seam {
	
	public int max_length;
	List<Pixel> pixels_list;
	int total_energy;
	
	public Seam(int max_length)
	{
		this.max_length = max_length;
		this.pixels_list = new ArrayList<Pixel>();
		this.total_energy = 0;
	}
	
	public void insert_pixel(Pixel p, int added_energy)
	{
		if(this.pixels_list.size() < this.max_length)
		{
			this.pixels_list.add(p);
			this.total_energy += added_energy;
		}
		else
		{
			System.out.println("Trying to add another pixel to full seam failed.");
			System.exit(-1);
		}
	}
}
