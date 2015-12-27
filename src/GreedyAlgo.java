import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import wrappers.Category;
import wrappers.Day;
import wrappers.Hour;
import wrappers.Slot;
import wrappers.Song;
import wrappers.Tupel;

public class GreedyAlgo {

	Day day;
	List<List<Integer>> result;
	int totalViolations = 0;

	public float planDay(Day day) {

		this.day = day;

		int numberOfSlotsToPlan = day.calNumberOfSlots();

		while (numberOfSlotsToPlan != 0) {
			Tupel slotToPlan = calculateSlotToPlan();

			//System.out.println(result);
			//System.out.println("Planning Location: " + slotToPlan);
			setSongOnLocation(slotToPlan);
			numberOfSlotsToPlan--;
		}
		

		return (float) totalViolations / day.calNumberOfSlots();

	}

	private void setSongOnLocation(Tupel slotToPlan) {
		Slot slot = day.getListOfHours().get(slotToPlan.x).getHourSlots().get(slotToPlan.y);
		Category cat = slot.getCategory();
		List<Song> songsOfCat = cat.getSongList();

		List<Integer> violationsList = new ArrayList<Integer>();
		int minViolations = 99;
		int index = 0;
		int minIndex = 0;
		for (Song song : songsOfCat) {
			int violations = checkConstraints(song, cat, slotToPlan.x, slotToPlan.y);
			violationsList.add(violations);
			// System.out.println("Violation of Song " + song + ": " +
			// violations);

			if (violations < minViolations) {
				minViolations = violations;
				minIndex = index;
			}
			index++;

		}
		// System.out.println("Genommener Song: " + minViolations);
		slot.setSong(songsOfCat.get(minIndex));
		totalViolations += minViolations;
	}

	private int checkConstraints(Song song, Category cat, int hourIndex, int slotIndex) {

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

	private Tupel calculateSlotToPlan() {

		Tupel slotToPlan = new Tupel(0, 0);
		int currentMin = Integer.MAX_VALUE;
		result = new ArrayList<List<Integer>>();

		int hourIndex = 0;
		List<Hour> listOfHours = day.getListOfHours();

		for (Hour hour : listOfHours) {

			List<Slot> slotList = hour.getHourSlots();
			List<Integer> possibilitiesForHour = new ArrayList<Integer>();
			int slotIndex = 0;

			for (Slot slot : slotList) {
				if (slot.getSong() == null) {
					Category cat = slot.getCategory();
					int numberOfPossibleSongs = calculateNumberOfPossibleSongs(cat, hourIndex, slotIndex);
					possibilitiesForHour.add(numberOfPossibleSongs);

					if (numberOfPossibleSongs < currentMin) {
						currentMin = numberOfPossibleSongs;
						slotToPlan = new Tupel(hourIndex, slotIndex);
					}
				} else {
					possibilitiesForHour.add(99);
				}
				slotIndex++;
			}
			result.add(possibilitiesForHour);
			hourIndex++;
		}

		return slotToPlan;
	}

	// hahah das is nur ein kommentar =)
	private int calculateNumberOfPossibleSongs(Category cat, int hourIndex, int slotIndex) {

		int counter = 0;
		List<Song> songList = day.getListOfHours().get(hourIndex).getHourSlots().get(slotIndex).getCategory()
				.getSongList();

		for (Song song : songList) {
			int violations = checkConstraints(song, cat, hourIndex, slotIndex);
			if (violations == 0) {
				counter++;
			}

		}

		return counter;
	}
}
