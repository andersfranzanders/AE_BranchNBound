import java.util.ArrayList;
import java.util.List;

import wrappers.Category;
import wrappers.Day;
import wrappers.Hour;
import wrappers.Slot;
import wrappers.Song;

public class EvolutionaryAlgor {

	Day bestDay = null;
	int maxVersuche = 10;

	public Day planDay(Day _day) {
		int versuch = 0;
		System.out.println(_day.getAllUsedCategories());
		
//		while (versuch < maxVersuche) {
//			if (bestDay == null) {
//				bestDay = _day;
//				bestDay.setTotalViolations(Integer.MAX_VALUE);
//			}
//			Day day = Day.deepCopyDay(bestDay);
//
//			planRandomDay(day);
//			int totalViolations = evaluateDay(day);
//			day.setTotalViolations(totalViolations);
//			System.out.println(day);
//			if (day.getTotalViolations() < bestDay.getTotalViolations()) {
//				bestDay = day;
//				versuch = 0;
//			} else {
//				versuch++;
//			}
//		}

		return bestDay;
	}

	private List<List<Integer>> initializeViolationMatrix() {
		List<List<Integer>> violationMatrix = new ArrayList<List<Integer>>();

		return violationMatrix;
	}

	private int evaluateDay(Day day) {
		int totalViolations = 0;
		List<Hour> hoursOfDay = day.getListOfHours();
		for (int hourIndex = 0; hourIndex < hoursOfDay.size(); hourIndex++) {
			Hour hour = hoursOfDay.get(hourIndex);
			List<Slot> slotsOfHour = hour.getHourSlots();
			for (int slotIndex = 0; slotIndex < slotsOfHour.size(); slotIndex++) {
				Slot slot = slotsOfHour.get(slotIndex);
				int violationsOfSlot = checkConstraints(day, slot.getSong(), slot.getCategory(), hourIndex, slotIndex);
				slot.setCurrentViolations(violationsOfSlot);
				System.out.println("Violations of slot: " + violationsOfSlot);
				totalViolations += violationsOfSlot;
			}
		}
		return totalViolations;
	}

	private void planRandomDay(Day day) {

		List<Hour> hoursOfDay = day.getListOfHours();
		for (Hour hour : hoursOfDay) {
			List<Slot> slotsOfHour = hour.getHourSlots();
			for (Slot slot : slotsOfHour) {
				if (slot.getCurrentViolations() > 0) {
					slot.setRandomSong();
				}else{
					System.out.println("Zero violations at " + slot.getSong());
				}
			}
		}

	}

	private int checkConstraints(Day day, Song song, Category cat, int hourIndex, int slotIndex) {

		int violations = 0;

		List<Slot> songsOfHour = day.getListOfHours().get(hourIndex).getHourSlots();
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
			// check genre
			if (songBefore.getGenre() == song.getGenre()) {
				// System.out.println("song didnt fit!" + songBefore + " <-- " +
				// song);
				violations++;
			}
			if (songBefore.getTempo() == song.getTempo()) {
				violations++;
			}
			if (songBefore.getEnergy() == song.getEnergy()) {
				violations++;
			}
			if (songBefore.getGender() == song.getGender()) {
				violations++;
			}
		}
		// Check Song after
		if (songAfter != null) {
			// check genre
			if (songAfter.getGenre() == song.getGenre()) {
				// System.out.println("song didnt fit!" + song + " --> " +
				// songAfter);
				violations++;
			}
			if (songAfter.getTempo() == song.getTempo()) {
				// System.out.println("song didnt fit!" + song + " --> " +
				// songAfter);
				violations++;
			}
			if (songAfter.getEnergy() == song.getEnergy()) {
				// System.out.println("song didnt fit!" + song + " --> " +
				// songAfter);
				violations++;
			}
			if (songAfter.getGender() == song.getGender()) {
				// System.out.println("song didnt fit!" + song + " --> " +
				// songAfter);
				violations++;
			}
		}
		// Check categories rotation Time
		int rotTime = cat.getMaxRot();
		for (int i = 0; i < rotTime; i++) {
			if (hourIndex - i >= 0) {
				Hour hourBefore = day.getListOfHours().get(hourIndex - i);
				List<Slot> slotList = hourBefore.getHourSlots();
				for (Slot slot : slotList) {
					Song songToCheck = slot.getSong();
					if (songToCheck != null) {
						if (songToCheck.equals(song)) {
							violations = violations + rotTime - i;

						}
					}
				}
			}
			if (i != 0) {
				if (hourIndex + i < day.getListOfHours().size()) {
					Hour hourAfter = day.getListOfHours().get(hourIndex + i);
					List<Slot> slotList = hourAfter.getHourSlots();
					for (Slot slot : slotList) {
						Song songToCheck = slot.getSong();
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

}
