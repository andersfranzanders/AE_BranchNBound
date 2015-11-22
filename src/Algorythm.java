import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import wrappers.Category;
import wrappers.Day;
import wrappers.Hour;
import wrappers.HourSlot;
import wrappers.Song;
import wrappers.Tupel;

public class Algorythm {

	Tupel locationOfCurrentMin;

	public void planDay(Day day) {

		Set<Category> usedCategories = getUsedCategories(day);
		int numberOfSlotsToPlan = day.numberOfLosts();
		System.out.println(numberOfSlotsToPlan);

		while (numberOfSlotsToPlan != 0) {
			List<List<Integer>> resultsForDay = calculatePossibilityMatrix(day);

			System.out.println(resultsForDay);
			System.out.println("Planning Location: " + locationOfCurrentMin);
			setSongOnLocation(day);
			numberOfSlotsToPlan--;
		}

		System.out.println(day);

	}

	private void setSongOnLocation(Day day) {
		Hour hour = day.getListOfHours().get(locationOfCurrentMin.x);
		HourSlot slot = hour.getHourSlots().get(locationOfCurrentMin.y);
		Category cat = slot.getCategory();
		List<Song> songsOfCat = cat.getSongList();
		for (Song song : songsOfCat) {
			boolean songCanBePlanned = checkConstraints(song, hour, locationOfCurrentMin.y);
			if (songCanBePlanned) {
				slot.setSong(song);
			}
		}
	}

	private boolean checkConstraints(Song song, Hour hour, int index) {

		List<HourSlot> songsOfHour = hour.getHourSlots();
		int i = index;
		Song songBefore = null;
		Song songAfter = null;
		if (i > 0) {
			songBefore = songsOfHour.get(i - 1).getSong();
		}
		if (i < (songsOfHour.size() - 1)) {
			songAfter = songsOfHour.get(i + 1).getSong();
		}
		// Check Song before
		if (songBefore != null) {
			// check genre
			if (songBefore.getGenre() == song.getGenre()) {
				//System.out.println("song didnt fit!" + songBefore + " <-- " + song);
				return false;
			}
			if (songBefore.getTempo() == song.getTempo()) {
				return false;
			}
		}
		// Check Song after
		if (songAfter != null) {
			// check genre
			if (songAfter.getGenre() == song.getGenre()) {
				//System.out.println("song didnt fit!" + song + " --> " + songAfter);
				return false;
			}
			if (songAfter.getTempo() == song.getTempo()) {
				//System.out.println("song didnt fit!" + song + " --> " + songAfter);
				return false;
			}
		}

		return true;
	}

	private List<List<Integer>> calculatePossibilityMatrix(Day day) {

		int currentMin = 99;

		List<Hour> listOfHours = day.getListOfHours();
		List<List<Integer>> resultsForDay = new ArrayList<List<Integer>>();

		for (int hourIndex = 0; hourIndex < listOfHours.size(); hourIndex++) {
			Hour hour = listOfHours.get(hourIndex);
			int sizeOfSongsInHour = hour.getHourSlots().size();
			List<Integer> possibilitiesForHour = new ArrayList<Integer>();

			for (int slotIndex = 0; slotIndex < sizeOfSongsInHour; slotIndex++) {
				HourSlot slot = hour.getHourSlots().get(slotIndex);
				if (slot.getSong() == null) {
					int numberOfPossibleSongs = calculateNumberOfPossibleSongs(hour, slotIndex);
					possibilitiesForHour.add(numberOfPossibleSongs);

					if (numberOfPossibleSongs < currentMin) {
						currentMin = numberOfPossibleSongs;
						locationOfCurrentMin = new Tupel(hourIndex, slotIndex);
					}
				} else {
					possibilitiesForHour.add(99);
				}
			}
			resultsForDay.add(possibilitiesForHour);
		}
		return resultsForDay;
	}
// hahah das is nur ein kommentar =)
	private int calculateNumberOfPossibleSongs(Hour hour, int slotIndex) {

		int counter = 0;
		List<Song> songList = hour.getHourSlots().get(slotIndex).getCategory().getSongList();
		int songIndex = 0;
		for (Song song : songList) {
			boolean songCanBePlanned = checkConstraints(song, hour, songIndex);
			if (songCanBePlanned) {
				counter++;
			}
			songIndex++;
		}

		return counter;
	}

	private Set<Category> getUsedCategories(Day day) {
		Set<Category> usedCategories = new HashSet<Category>();
		List<Hour> hourList = day.getListOfHours();

		for (Hour hour : hourList) {
			List<HourSlot> hourSlots = hour.getHourSlots();

			for (HourSlot slot : hourSlots) {
				usedCategories.add(slot.getCategory());
			}
		}

		return usedCategories;
	}
}
