package seamCarving;

public class DynamicProgrammingTableEnrty {
	public Pixel next_pixel;
	double total_energy;
	
	public DynamicProgrammingTableEnrty(Pixel next_p, double total_energy) {
		this.total_energy = total_energy;
		this.next_pixel = next_p;
	}
	
	public DynamicProgrammingTableEnrty(int row_number, int col_number, double total_energy) {
		Pixel next_pixel = new Pixel(row_number, col_number);
		this.total_energy = total_energy;
		this.next_pixel = next_pixel;
	}
}