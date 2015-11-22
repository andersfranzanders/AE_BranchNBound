package wrappers;

import java.util.ArrayList;
import java.util.List;

public class Hour {

	String name;
	List<HourSlot> hourSlots;

	public Hour(String name, List<Category> listOfCategories) {
		this.name = name;
		hourSlots = new ArrayList<HourSlot>();

		for (int i = 0; i < listOfCategories.size(); i++) {
			Category categoryForSlot = listOfCategories.get(i);
			hourSlots.add(new HourSlot(categoryForSlot));
		}
	}
	
	

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public List<HourSlot> getHourSlots() {
		return hourSlots;
	}



	public void setHourSlots(List<HourSlot> hourSlots) {
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
