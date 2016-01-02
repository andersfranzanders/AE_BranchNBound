package oldAlgos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wrappers.Arc;
import wrappers.Category;
import wrappers.Day;
import wrappers.Hour;
import wrappers.Slot;
import wrappers.Song;

public class GreedyForAC3 {

	public Day planDay(Day day) {

		// while (day.remainingSlots > 0) {
		Slot slotToPlan = getSlotToPlan(day);
		setSong(slotToPlan, day);
		day.remainingSlots--;
		// }

		return day;
	}

	private void setSong(Slot slotToPlan, Day day) {

		Set<Song> songsToConsider = getSongsToConsider(slotToPlan, day);
		System.out.println("Songs to Consider are: ");

		for (Song song : songsToConsider) {
			System.out.println(song);
		}
		decideBestSong(songsToConsider, slotToPlan, day);

	}

	private void decideBestSong(Set<Song> songsToConsider, Slot slotToPlan, Day day) {
		int sumOfPossibleSongs = getSumOfPossibleSongs(day);
		for (Song song : songsToConsider) {
			slotToPlan.setSong(song);
			checkNumOfSongsLost(day, slotToPlan);
		}

	}

	private void checkNumOfSongsLost(Day day, Slot slotToPlan) {
		
		int totalLostSongs = 0;
		
		List<Hour> hourList = day.getListOfHours();
		for (int hourIndex = 0; hourIndex < hourList.size(); hourIndex++) {
			Hour hour = hourList.get(hourIndex);
			List<Slot> slotList = hour.getHourSlots();
			for (int slotIndex = 0; slotIndex < slotList.size(); slotIndex++) {
				Slot slot = slotList.get(slotIndex);
				if (slot == slotToPlan) {
					
					
					if (slotIndex > 0) {
						Slot preSlot = slotList.get(slotIndex - 1);
						for(Song song: preSlot.getRemainingSongs()){
							int violations = checkNeighnourConstraints(song, slotToPlan.getSong());
							if(violations > 0){
								totalLostSongs++;
							}
						}
					}
					if(slotIndex < slotList.size() - 1){
					//	Slot postSLot = slotList.get
					}

				}
			}
		}
	}

	private int getSumOfPossibleSongs(Day day) {

		int sum = 0;
		for (Hour hour : day.getListOfHours()) {
			for (Slot slot : hour.getHourSlots()) {
				if (slot.getSong() != null) {
					sum += slot.getRemainingSongs().size();

				}
			}
		}
		return sum;
	}

	private Set<Song> getSongsToConsider(Slot slotToPlan, Day day) {

		System.out.println("Cal which song to consider---------");
		Set<Song> songsToConsider = new HashSet<Song>();

		if (slotToPlan.getRemainingSongs().isEmpty()) {
			System.out.println("List empty, searching for least bad song");
			int minViolations = Integer.MAX_VALUE;
			for (Song song : slotToPlan.getCategory().getSongList()) {
				System.out.println("song" + song);
				int violations = calculateViolationsForSong(song, slotToPlan, day);
				System.out.println("violations: " + violations);

				if (violations < minViolations) {
					songsToConsider = new HashSet<Song>();
					songsToConsider.add(song);
					minViolations = violations;
				}
				if (violations == minViolations) {
					songsToConsider.add(song);
				}
				System.out.println("minViolations: " + minViolations);

			}
			day.setTotalViolations(day.getTotalViolations() + minViolations);

		} else {
			System.out.println("Still pissble songs left!");
			for (Song song : slotToPlan.getRemainingSongs()) {
				songsToConsider.add(song);

			}
		}
		return songsToConsider;
	}

	private int calculateViolationsForSong(Song song, Slot slotToPlan, Day day) {

		int totalViolations = 0;

		List<Hour> hourList = new ArrayList<Hour>();
		for (int hourIndex = 0; hourIndex < hourList.size(); hourIndex++) {
			Hour hour = hourList.get(hourIndex);
			List<Slot> slotList = hour.getHourSlots();
			for (int slotIndex = 0; slotIndex < slotList.size(); slotIndex++) {
				Slot slot = slotList.get(slotIndex);
				if (slotToPlan == slot) {

					Song preSong = null;
					Song postSong = null;
					if (slotIndex > 0) {
						preSong = slotList.get(slotIndex - 1).getSong();

					}
					if (slotIndex < slotList.size() - 1) {
						postSong = slotList.get(slotIndex + 1).getSong();
					}
					if (preSong != null) {
						totalViolations += checkNeighnourConstraints(song, preSong);
					}
					if (postSong != null) {
						totalViolations += checkNeighnourConstraints(song, postSong);
					}
					Category thisCat = slot.getCategory();
					int rotTime = thisCat.getMaxRot();
					for (int rotIndex = hourIndex - rotTime; rotIndex <= hourIndex + rotTime; rotIndex++) {
						if (rotIndex >= 0 && rotIndex < day.getListOfHours().size()) {
							Hour rotHour = day.getListOfHours().get(rotIndex);
							for (Slot rotSlot : rotHour.getHourSlots()) {
								if (rotSlot.getCategory().equals(thisCat) && !rotSlot.equals(slotToPlan)) {
									Song rotSong = rotSlot.getSong();
									if (rotSong != null) {
										totalViolations += checkRotConstraints(song, rotSong, hourIndex, rotIndex,
												rotTime);

									}

								}
							}
						}
					}
				}
			}
		}
		return totalViolations;

	}

	private int checkRotConstraints(Song song, Song rotSong, int hourIndex, int rotIndex, int rotTime) {
		if (song.equals(rotSong)) {
			if (rotIndex >= hourIndex) {
				return rotTime - (rotIndex - hourIndex);
			} else {
				return rotTime - (hourIndex - rotTime);
			}
		}
		return 0;
	}

	private int checkNeighnourConstraints(Song song, Song otherSong) {

		int violations = 0;

		if (song.getArtist().equals(otherSong.getArtist())) {
			violations++;
		}
		if (song.getName().equals(otherSong.getName())) {
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

	private Slot getSlotToPlan(Day day) {

		System.out.println("Cal slot to plan: --------------------");

		int min = Integer.MAX_VALUE;
		Slot slotToPlan = null;
		for (Hour hour : day.getListOfHours()) {
			System.out.print("HOUR  ... ");
			for (Slot slot : hour.getHourSlots()) {
				if (slot.getSong() == null) {
					int size = slot.getRemainingSongs().size();
					System.out.print(size + " ");
					if (size < min) {
						slotToPlan = slot;
						min = size;
					}
				} else {
					System.out.print(" x ");
				}
			}
			System.out.println("");
		}

		return slotToPlan;
	}

}
