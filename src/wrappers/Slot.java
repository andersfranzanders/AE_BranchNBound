package wrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Slot implements Serializable{
	
	private Song song;
	private Category category;
	private int currentViolations = Integer.MAX_VALUE;
	private List<Song> remainingSongs = new ArrayList<Song>();
	private List<ConnectedNode> connectedNodes = new ArrayList<ConnectedNode>();
	

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


	public List<Song> getRemainingSongs() {
		return remainingSongs;
	}

	public void setRemainingSongs(List<Song> remainingSongs) {
		this.remainingSongs = remainingSongs;
	}

//	@Override
//	public String toString() {
//		String s = "Slot [category=" + category.getName() + ", song=" + song  + "]  \n";
//		s += "Connected Slots: \n";
//		for(ConnectedNode node: connectedNodes){
//			s+= "... "+ node + "\n";
//		}
//		
//		return s;
//	}
	
	@Override
	public String toString() {
		String s = "Slot [category=" + category.getName() + ", song=" + song  + "] ";
		
		return s;
	}

	public List<ConnectedNode> getConnectedNodes() {
		return connectedNodes;
	}

	public void setConnectedNodes(List<ConnectedNode> connectedNodes) {
		this.connectedNodes = connectedNodes;
	}
	
	
	
	

}
