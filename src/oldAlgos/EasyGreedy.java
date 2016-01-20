package oldAlgos;

import wrappers.Category;
import wrappers.Day;
import wrappers.Hour;
import wrappers.Slot;
import wrappers.Song;

public class EasyGreedy {

	public Day planDay(Day day) {
	//	System.out.println("Begin new Greey");
		planNextSong(day);
		MinConflictsAlgo minConflicts = new MinConflictsAlgo();
		day = minConflicts.planDay(day);
		return day;
	}

	private void planNextSong(Day day) {

		for (int hourIndex = 0; hourIndex < day.getListOfHours().size(); hourIndex++) {
	//		System.out.println("Hour: -----------------" + hourIndex);
			Hour hour = day.getListOfHours().get(hourIndex);
			for (int slotIndex = 0; slotIndex < hour.getHourSlots().size(); slotIndex++) {
	//			System.out.println("Slot: -----------------" + slotIndex);

				Slot slot = hour.getHourSlots().get(slotIndex);
	//			System.out.println(slot);
				Category cat = slot.getCategory();
				int minViolations = Integer.MAX_VALUE;
				Song songToPlan = null;
				for (Song song : cat.getSongList()) {
	//				System.out.println(song);
					int violations = day.checkConstraints(song, cat, hourIndex, slotIndex);
	//				System.out.println("violations: " + violations);
					if (violations < minViolations) {
						songToPlan = song;
						minViolations = violations;
					}
				}
	//			System.out.println("Will plan Song");
	//			System.out.println(songToPlan);
				slot.setSong(songToPlan);
				slot.setCurrentViolations(minViolations);
	//			System.out.println(day);
			}
		}

	}
}
