package oldAlgos;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import wrappers.Category;
import wrappers.Day;
import wrappers.DayComparator;
import wrappers.DayComparator2;
import wrappers.Hour;
import wrappers.Slot;
import wrappers.Song;
import wrappers.Tupel;

public class BacktrackingAlgo {

	public static Stack<Day> stack = new Stack<Day>();
	public static Day bestDay = null;

	public Day planNextSong(Day day) {

		if (day.remainingSlots == day.calNumberOfSlots()) {
			bestDay = day;
		}

		if (day.remainingSlots == 0) {
			return day;
		} else {
			Tupel slotToPlan = calculateSlotToPlan(day);
			setSongOnLocation(day, slotToPlan);

			try {
				Day nextNode = stack.pop();

				return planNextSong(nextNode);

			} catch (Exception e) {
				// return bestDay;
				System.out.println("best day: ----------");
				System.out.println(bestDay);
				GreedyAlgo greedy = new GreedyAlgo();
				return greedy.planNextSong(bestDay);
			}

		}
	}

	private void printOutInfo(Day day, List<Day> newNodes) {

		System.out.println(day.remainingSlots + " present Day: ----------------------");
		System.out.println(day);
		System.out.println(day.remainingSlots + " New Nodes from here: ------------------");
		for (Day newNode : newNodes) {
			System.out.println(newNode);
		}

		System.out.println(day.remainingSlots + " Currently In the Que: ------------------");
		System.out.println(stack);
		System.out.println(day.remainingSlots + " Next Node To Plan: ----------------------");
		System.out.println(stack.peek());
		System.out.println(day.remainingSlots + " Best Day: ----------------------");
		System.out.println(bestDay);

	}

	private void setSongOnLocation(Day oldDay, Tupel slotToPlan) {

		Slot slot = oldDay.getListOfHours().get(slotToPlan.x).getHourSlots().get(slotToPlan.y);
		Category cat = slot.getCategory();
		List<Song> songsOfCat = cat.getSongList();
		int numSongsOfCat = oldDay.getListOfHours().get(slotToPlan.x).getHourSlots().get(slotToPlan.y).getCategory()
				.getSongList().size();

		for (int i = 0; i < numSongsOfCat; i++) {
			Song song = songsOfCat.get(i);
			int violations = checkConstraints(oldDay, song, cat, slotToPlan.x, slotToPlan.y);

			if (violations == 0) {
				Day newDay = Day.deepCopyDay(oldDay);
				Slot newSlot = newDay.getListOfHours().get(slotToPlan.x).getHourSlots().get(slotToPlan.y);
				Song newSong = newSlot.getCategory().getSongList().get(i);

				newSlot.setSong(newSong);
				newDay.setTotalViolations(newDay.getTotalViolations() + violations);
				newDay.remainingSlots--;
				stack.push(newDay);
				if (newDay.remainingSlots < bestDay.remainingSlots) {
					bestDay = newDay;
					System.out.println(newDay.remainingSlots);
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

	private Tupel calculateSlotToPlan(Day day) {

		Tupel slotToPlan = new Tupel(0, 0);
		int currentMin = Integer.MAX_VALUE;
		ArrayList<List<Integer>> possibilityOverview = new ArrayList<List<Integer>>();

		int hourIndex = 0;
		List<Hour> listOfHours = day.getListOfHours();

		for (Hour hour : listOfHours) {

			List<Slot> slotList = hour.getHourSlots();
			List<Integer> possibilitiesForHour = new ArrayList<Integer>();
			int slotIndex = 0;

			for (Slot slot : slotList) {
				if (slot.getSong() == null) {
					Category cat = slot.getCategory();
					int numberOfPossibleSongs = calculateNumberOfPossibleSongs(day, cat, hourIndex, slotIndex);
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
			possibilityOverview.add(possibilitiesForHour);
			hourIndex++;
		}
	//	System.out.println(possibilityOverview);
		return slotToPlan;
	}

	private int calculateNumberOfPossibleSongs(Day day, Category cat, int hourIndex, int slotIndex) {

		int counter = 0;
		List<Song> songList = day.getListOfHours().get(hourIndex).getHourSlots().get(slotIndex).getCategory()
				.getSongList();

		for (Song song : songList) {
			int violations = checkConstraints(day, song, cat, hourIndex, slotIndex);
			if (violations == 0) {
				counter++;
			}

		}

		return counter;
	}
}
