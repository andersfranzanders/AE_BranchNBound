package wrappers;

public class HourSlot {
	
	private Song song;
	private Category category;
	
	public HourSlot(Category category){
		this.category = category;
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	
	
	

}
