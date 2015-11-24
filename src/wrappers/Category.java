package wrappers;

import java.util.ArrayList;
import java.util.List;

public class Category {

	private String name;
	int maxSongs;
	int maxRot;
	private List<Song> songList = new ArrayList<Song>();

	public Category(String name, int maxSongs, int maxRot) {
		super();
		this.name = name;
		this.maxSongs = maxSongs;
		this.maxRot = maxRot;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Song> getSongList() {
		return songList;
	}

	public void setSongList(List<Song> songList) {
		this.songList = songList;
	}

	public boolean addSong(Song song) {
		if (songList.size() == maxSongs) {
			return false;
		}
		songList.add(song);
		return true;
	}

	public String toString() {
		String s = "";
		s += "Category: " + name + " | maxSize : " + maxSongs + " | Filled: " + songList.size() + " | Rottime: " + maxRot + " \n ";
		for (Song song : songList) {
			s += "..." + song.toString() + " \n ";	
		}

		return s;
	}

	public int getMaxSongs() {
		return maxSongs;
	}

	public void setMaxSongs(int maxSongs) {
		this.maxSongs = maxSongs;
	}

	public int getMaxRot() {
		return maxRot;
	}

	public void setMaxRot(int maxRot) {
		this.maxRot = maxRot;
	}
	
	public void fillCategoryWithRandomSongs(){
		for(int i = 0; i < maxSongs; i++){
			songList.add(Song.generateRandomSong());
		}
	}
	public float songDeterminism(){
		
		return (maxSongs - 1) / maxRot;
	}
	

}
