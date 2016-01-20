package wrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day implements Serializable{

	private int hoursOfDay;
	private List<Hour> listOfHours;
	public int remainingSlots = 0;
	private int totalViolations = 0;

	public Day(int hoursOfDay) {
		this.hoursOfDay = hoursOfDay;
		this.listOfHours = new ArrayList<Hour>();
	}

	public int getHoursOfDay() {
		return hoursOfDay;
	}

	public void setHoursOfDay(int hoursOfDay) {
		this.hoursOfDay = hoursOfDay;
	}

	public List<Hour> getListOfHours() {
		return listOfHours;
	}

	public void setListOfHours(List<Hour> listOfHours) {
		this.listOfHours = listOfHours;
	}

	public void addHourToList(Hour hour) {
		if (listOfHours.size() <= hoursOfDay) {
			this.listOfHours.add(hour);
		}
	}

	public String toString() {
		String s = "";
		s += "Planned Day: \n";
		s += "Total Violations " + this.totalViolations + " \n";
		int i = 0;
		for (Hour hour : listOfHours) {
			s += "hour: " + i + "\n";
			s += hour.toString();
			s += "\n";
			i++;
		}

		return s;

	}

	public int calNumberOfSlots() {
		int counter = 0;
		for (Hour hour : listOfHours) {
			counter += hour.getHourSlots().size();
		}
		return counter;
	}

	public int getTotalViolations() {
		return totalViolations;
	}

	public void setTotalViolations(int totalViolations) {
		this.totalViolations = totalViolations;
	}

	public double getRelativeViolations() {
		return (double) totalViolations / calNumberOfSlots();
	}

	public static Day deepCopyDay(Day oldDay) {

		Day newDay = new Day(oldDay.getHoursOfDay());
		newDay.setTotalViolations(oldDay.getTotalViolations());
		newDay.remainingSlots = oldDay.remainingSlots;

		List<Hour> newListOfHours = new ArrayList<Hour>();
		for (Hour oldHour : oldDay.getListOfHours()) {

			List<Slot> newSlotList = new ArrayList<Slot>();
			for (Slot oldSlot : oldHour.getHourSlots()) {
				Slot newSlot = new Slot(oldSlot.getCategory());
				newSlot.setSong(oldSlot.getSong());
				newSlot.setCurrentViolations(oldSlot.getCurrentViolations());
				newSlotList.add(newSlot);

			}
			Hour newHour = new Hour(oldHour.getName());
			newHour.setHourSlots(newSlotList);
			newListOfHours.add(newHour);
		}
		newDay.setListOfHours(newListOfHours);
		newDay.setTotalViolations(oldDay.getTotalViolations());

		return newDay;
	}

	public Set<Category> getAllUsedCategories() {
		Set<Category> setOfCats = new HashSet<Category>();
		for (Hour hour : listOfHours) {
			for (Slot slot : hour.getHourSlots()) {
				setOfCats.add(slot.getCategory());
			}
		}

		return setOfCats;
	}

	public int checkConstraints(Song song, Category cat, int hourIndex, int slotIndex) {

		int violations = 0;

		List<Slot> songsOfHour = this.getListOfHours().get(hourIndex).getHourSlots();
		Song songBefore = null;
		Song songAfter = null;
		if (slotIndex > 0) {
			songBefore = songsOfHour.get(slotIndex - 1).getSong();
		}
		if (slotIndex < (songsOfHour.size() - 1)) {
			songAfter = songsOfHour.get(slotIndex + 1).getSong();
		}
		// Check Song before
		if (songBefore != null) {
			violations += checkNeigbourConstraints(song, songBefore);
		}
		// Check Song after
		if (songAfter != null) {
			violations += checkNeigbourConstraints(song, songAfter);
		}
		// Check categories rotation Time
		// Check categories rotation Time
		int rotTime = cat.getMaxRot();
		for (int i = 0; i < rotTime; i++) {
			if (hourIndex - i >= 0) {
				Hour hourBefore = this.getListOfHours().get(hourIndex - i);
				List<Slot> slotList = hourBefore.getHourSlots();
				for (Slot rotSlot : slotList) {
					Song songToCheck = rotSlot.getSong();
					if (songToCheck != null) {
						if (songToCheck.equals(song)) {
							violations = violations + rotTime - i;

						}
					}

				}
			}
			if (i != 0) {
				if (hourIndex + i < this.getListOfHours().size()) {
					Hour hourAfter = this.getListOfHours().get(hourIndex + i);
					List<Slot> slotList = hourAfter.getHourSlots();
					for (Slot rotSlot : slotList) {
						Song songToCheck = rotSlot.getSong();
						if (songToCheck != null) {
							if (songToCheck.equals(song)) {
								violations = violations + rotTime - i;
							}
						}

					}
				}
			}
		}

		return violations;
	}

	public int checkConstraints(Song song, Slot slot) {

		int violations = 0;

		for (int hourIndex = 0; hourIndex < this.getListOfHours().size(); hourIndex++) {
			Hour hour = this.getListOfHours().get(hourIndex);
			for (int slotIndex = 0; slotIndex < hour.getHourSlots().size(); slotIndex++) {
				if (hour.getHourSlots().get(slotIndex) == slot) {

					List<Slot> songsOfHour = this.getListOfHours().get(hourIndex).getHourSlots();
					Song songBefore = null;
					Song songAfter = null;
					if (slotIndex > 0) {
						songBefore = songsOfHour.get(slotIndex - 1).getSong();
					}
					if (slotIndex < (songsOfHour.size() - 1)) {
						songAfter = songsOfHour.get(slotIndex + 1).getSong();
					}
					// Check Song before
					if (songBefore != null) {
						violations += checkNeigbourConstraints(song, songBefore);
					}
					// Check Song after
					if (songAfter != null) {
						violations += checkNeigbourConstraints(song, songAfter);
					}
					// Check categories rotation Time
					int rotTime = slot.getCategory().getMaxRot();
					for (int i = 0; i < rotTime; i++) {
						if (hourIndex - i >= 0) {
							Hour hourBefore = this.getListOfHours().get(hourIndex - i);
							List<Slot> slotList = hourBefore.getHourSlots();
							for (Slot rotSlot : slotList) {
								if (rotSlot != slot) {
									Song songToCheck = rotSlot.getSong();
									if (songToCheck != null) {
										if (songToCheck.equals(song)) {
											violations = violations + rotTime - i;

										}
									}
								}
							}
						}
						if (i != 0) {
							if (hourIndex + i < this.getListOfHours().size()) {
								Hour hourAfter = this.getListOfHours().get(hourIndex + i);
								List<Slot> slotList = hourAfter.getHourSlots();
								for (Slot rotSlot : slotList) {
									if (rotSlot != slot) {
										Song songToCheck = rotSlot.getSong();
										if (songToCheck != null) {
											if (songToCheck.equals(song)) {
												violations = violations + rotTime - i;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return violations;
	}

	private int checkNeigbourConstraints(Song song, Song otherSong) {

		int violations = 0;

		if (song.getArtist().equals(otherSong.getArtist())) {
			violations++;
		}
		if (song.getEnergy() == otherSong.getEnergy()) {
			violations++;
		}
		if (song.getGenre() == otherSong.getGenre()) {
			violations++;
		}
		if (song.getTempo() == otherSong.getTempo()) {
			violations++;
		}

		return violations;
	}

	public int getNumOfArcs() {
		int counter = 0;
		for(Hour hour : listOfHours){
			for(Slot slot: hour.getHourSlots()){
				for(ConnectedNode node: slot.getConnectedNodes()){
					counter++;
				}
			}
			
		}
		
		return counter;
	}

	public int getLargestDomain() {
		int maxDomainSize = 0;
		for(Hour hour : listOfHours){
			for(Slot slot: hour.getHourSlots()){
				int domainSize = slot.getCategory().getSongList().size();
				if(domainSize > maxDomainSize){
					maxDomainSize = domainSize;
				}
			}
			
		}
		
		
		return maxDomainSize;
	}
	
	public double calZeroes(){
		double zeroes = 0;
		double pos = 0;
		for(Hour hour: this.getListOfHours()){
			for(Slot slot: hour.getHourSlots()){
				pos++;
				if(slot.getSong() == null){
					zeroes++;
				}	
			}
		}
		return zeroes / pos;
	}
}
