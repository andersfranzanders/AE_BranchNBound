package wrappers;

import java.util.List;
import java.util.Random;

public class Slot {
	
	private Song song;
	private Category category;
	private int currentViolations = Integer.MAX_VALUE;
	
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

	public void setRandomSong() {
		Random random = new Random();
		List<Song> songList = this.category.getSongList();
		this.song = songList.get(random.nextInt(songList.size()));
		
	}

	public int getCurrentViolations() {
		return currentViolations;
	}

	public void setCurrentViolations(int currentViolations) {
		this.currentViolations = currentViolations;
	}
	
	
	
	

}
