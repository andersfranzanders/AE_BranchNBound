package wrappers;

import java.util.ArrayList;
import java.util.List;

public class Hour {

	//Commentar =)!
	
	String name;
	List<Slot> hourSlots;

	public Hour(String name, List<Category> listOfCategories) {
		this.name = name;
		hourSlots = new ArrayList<Slot>();

		for (int i = 0; i < listOfCategories.size(); i++) {
			Category categoryForSlot = listOfCategories.get(i);
			hourSlots.add(new Slot(categoryForSlot));
		}
	}
	
	public Hour(String name){
		this.name = name;
	}
	
	

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public List<Slot> getHourSlots() {
		return hourSlots;
	}



	public void setHourSlots(List<Slot> hourSlots) {
		this.hourSlots = hourSlots;
	}



	public String toString() {
		String s = "";
		s += "Name: " + name + " \n ";
		for (int i = 0; i < hourSlots.size(); i++) {
			s += i + ". Kategorie: " + hourSlots.get(i).getCategory().getName();
			if (hourSlots.get(i).getSong() != null) {
				s += ". Filled With: " + hourSlots.get(i).getSong().toString();
			}
			s += " \n ";
		}
		return s;
	}

	public boolean addSongToHour(Song song, String categoryOfSong, int index) {

		if (categoryOfSong.equals(hourSlots.get(index).getCategory().getName())) {
			hourSlots.get(index).setSong(song);
			return true;
		}
		return false;

	}

}
