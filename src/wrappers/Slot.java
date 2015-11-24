package wrappers;

public class Slot {
	
	private Song song;
	private Category category;
	
	public Slot(Category category){
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
