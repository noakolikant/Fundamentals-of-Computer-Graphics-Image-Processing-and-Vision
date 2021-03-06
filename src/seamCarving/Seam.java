package seamCarving;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Seam {
	
	public int max_length;
	List<Pixel> pixels_list;
	double total_energy;
	
	public Seam(int max_length)
	{
		this.max_length = max_length;
		this.pixels_list = new ArrayList<Pixel>();
		this.total_energy = 0;
	}
	
	public Seam(Seam s)
	{
		this.max_length = s.max_length;
		this.total_energy = s.total_energy;
		this.pixels_list = new ArrayList<Pixel>();
		
		Iterator<Pixel> it = s.pixels_list.iterator();
		
		while(it.hasNext())
		{
			Pixel p = it.next();
			Pixel p_copy = new Pixel(p);
			this.pixels_list.add(p_copy);
		}		
	}
	
	public void insert_pixel(Pixel p, double added_energy)
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
	
	public void add_one_to_row(Seam other) {
		for (int i = 0; i < this.pixels_list.size(); i++) {
			if (other.pixels_list.get(i).row_number <= this.pixels_list.get(i).row_number) {
				// if the seam row number is lower-or-equal than 
				// the current seam it means that its row-coordinate
				// in the original image is higher in one
				this.pixels_list.get(i).row_number += 1;
			} 
		}
	}
	
	public void finalize()
	{
		this.pixels_list.clear();
	}
}
