package oldAlgos;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import wrappers.Category;
import wrappers.Day;
import wrappers.DayComparator;
import wrappers.Hour;
import wrappers.Slot;
import wrappers.Song;
import wrappers.Tripel;
import wrappers.Tupel;

public class GreedyAlgo2 {

	public Day planNextSong(Day day) {
		Tripel slotToPlan = calculateSlotToPlan(day);
		while (day.remainingSlots > 0) {
			slotToPlan = setSongOnLocation(day, slotToPlan);
			day.remainingSlots--;

		}
		MinConflictsAlgo minConflits = new MinConflictsAlgo();
		day = minConflits.planDay(day);
		return day;

	}

	private Tripel setSongOnLocation(Day day, Tripel _tripel) {
		Slot slot = day.getListOfHours().get(_tripel.getHourIndex()).getHourSlots().get(_tripel.getSlotIndex());
		Category cat = slot.getCategory();
		List<Song> songsOfCat = cat.getSongList();
		Set<Song> songsToConsider = new HashSet<Song>();

		int minViolations = Integer.MAX_VALUE;
		for (Song song : songsOfCat) {
			int violations = day.checkConstraints(song, cat, _tripel.getHourIndex(), _tripel.getSlotIndex());
			if (violations < minViolations) {
				minViolations = violations;
				songsToConsider = new HashSet<Song>();
				songsToConsider.add(song);
			}
			if (violations == minViolations) {
				songsToConsider.add(song);
			}
		}
		slot.setCurrentViolations(minViolations);

		int lowestZeroViolations = -1;
		Song songToPlan = null;
		Tripel bestTripel = null;
		for (Song song : songsToConsider) {
			
			slot.setSong(song);
			Tripel tripel = calculateSlotToPlan(day);
			
			if (tripel.getTotalZeroViolations() > lowestZeroViolations) {
				songToPlan = song;
				lowestZeroViolations = tripel.getTotalZeroViolations();
				bestTripel = tripel;
			}
		}
	
		slot.setSong(songToPlan);
		day.setTotalViolations(day.getTotalViolations() + minViolations);
		
		return bestTripel;
	}

//	private int checkConstraints(Day day, Song song, Category cat, int hourIndex, int slotIndex) {
//
//		int violations = 0;
//
//		List<Slot> songsOfHour = day.getListOfHours().get(hourIndex).getHourSlots();
//		Song songBefore = null;
//		Song songAfter = null;
//		if (slotIndex > 0) {
//			songBefore = songsOfHour.get(slotIndex - 1).getSong();
//		}
//		if (slotIndex < (songsOfHour.size() - 1)) {
//			songAfter = songsOfHour.get(slotIndex + 1).getSong();
//		}
//		// Check Song before
//		if (songBefore != null) {
//			// check genre
//			if (songBefore.getGenre() == song.getGenre()) {
//				violations++;
//			}
//			if (songBefore.getTempo() == song.getTempo()) {
//				violations++;
//			}
//		}
//		// Check Song after
//		if (songAfter != null) {
//			// check genre
//			if (songAfter.getGenre() == song.getGenre()) {
//				violations++;
//			}
//			if (songAfter.getTempo() == song.getTempo()) {
//				violations++;
//			}
//		}
//		// Check categories rotation Time
//		int rotTime = cat.getMaxRot();
//		for (int i = 0; i < rotTime; i++) {
//			if (hourIndex - i >= 0) {
//				Hour hourBefore = day.getListOfHours().get(hourIndex - i);
//				List<Slot> slotList = hourBefore.getHourSlots();
//				for (Slot slot : slotList) {
//					Song songToCheck = slot.getSong();
//					if (songToCheck != null) {
//						if (songToCheck.equals(song)) {
//							violations = violations + rotTime - i;
//
//						}
//					}
//				}
//			}
//			if (i != 0) {
//				if (hourIndex + i < day.getListOfHours().size()) {
//					Hour hourAfter = day.getListOfHours().get(hourIndex + i);
//					List<Slot> slotList = hourAfter.getHourSlots();
//					for (Slot slot : slotList) {
//						Song songToCheck = slot.getSong();
//						if (songToCheck != null) {
//							if (songToCheck.equals(song)) {
//								violations = violations + rotTime - i;
//							}
//						}
//					}
//				}
//			}
//		}
//
//		return violations;
//	}

	private Tripel calculateSlotToPlan(Day day) {

		Tripel slotToPlan = new Tripel(0, 0, 0);
		int currentMin = Integer.MAX_VALUE;
		ArrayList<List<Integer>> possibilityOverview = new ArrayList<List<Integer>>();

		int hourIndex = 0;
		List<Hour> listOfHours = day.getListOfHours();
		int totalZeroViolations = 0;

		for (Hour hour : listOfHours) {

			List<Slot> slotList = hour.getHourSlots();
			List<Integer> possibilitiesForHour = new ArrayList<Integer>();
			int slotIndex = 0;

			for (Slot slot : slotList) {
				if (slot.getSong() == null) {
					Category cat = slot.getCategory();
					int numberOfPossibleSongs = calculateNumberOfPossibleSongs(day, cat, hourIndex, slotIndex);
					possibilitiesForHour.add(numberOfPossibleSongs);
					totalZeroViolations += numberOfPossibleSongs;
					if (numberOfPossibleSongs < currentMin) {
						currentMin = numberOfPossibleSongs;
						slotToPlan = new Tripel(hourIndex, slotIndex, 0);
					}
				} else {
					possibilitiesForHour.add(99);
				}
				slotIndex++;
			}
			possibilityOverview.add(possibilitiesForHour);
			hourIndex++;
		}
		slotToPlan.setTotalZeroViolations(totalZeroViolations);

	//	System.out.println(possibilityOverview);
		return slotToPlan;
	}

	private int calculateNumberOfPossibleSongs(Day day, Category cat, int hourIndex, int slotIndex) {

		int counter = 0;
		List<Song> songList = cat.getSongList();

		for (Song song : songList) {
			int violations = day.checkConstraints(song, cat, hourIndex, slotIndex);
			if (violations == 0) {
				counter++;
			}

		}

		return counter;
	}

}
