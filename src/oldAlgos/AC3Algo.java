package oldAlgos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import wrappers.Arc;
import wrappers.Category;
import wrappers.Day;
import wrappers.Hour;
import wrappers.Slot;
import wrappers.Song;

public class AC3Algo {

	Set<Arc> queue = new HashSet<Arc>();

	public Day planDay(Day day) {

		initializeDay(day);
		while (!queue.isEmpty()) {
			Arc arc = queue.iterator().next();
			queue.remove(arc);
			if (eraseInconsistentValues(arc)) {
				addArcsToX(day, arc.getX(), arc.getY());
			}

		}

		System.out.println("-------------------- Finally:");
		for (Hour hour: day.getListOfHours()) {
			for(Slot slot: hour.getHourSlots()){
				System.out.println(slot);
				for(Song song: slot.getRemainingSongs()){
					System.out.println(song);
				}
			}

		}
		GreedyForAC3 gForAc3 = new GreedyForAC3();
		day = gForAc3.planDay(day);

		return day;
	}

	private void addArcsToX(Day day, Slot xSlot, Slot ySlot) {

		for (int hourIndex = 0; hourIndex < day.getListOfHours().size(); hourIndex++) {
			Hour hour = day.getListOfHours().get(hourIndex);
			for (int slotIndex = 0; slotIndex < hour.getHourSlots().size(); slotIndex++) {
				Slot thisSlot = hour.getHourSlots().get(slotIndex);
				if (xSlot == thisSlot) {

					if (slotIndex < (hour.getHourSlots().size() - 1)) {
						Slot postSlot = hour.getHourSlots().get(slotIndex + 1);
						if (postSlot != ySlot) {
							Arc arc = new Arc(postSlot, thisSlot, 0);
							queue.add(arc);
						}
					}
					if (slotIndex > 0) {
						Slot preSlot = hour.getHourSlots().get(slotIndex - 1);
						if (preSlot != ySlot) {
							Arc arc = new Arc(preSlot, thisSlot, 0);
							queue.add(arc);
						}
					}
					Category thisCat = thisSlot.getCategory();
					int rotTime = thisCat.getMaxRot();
					for (int rotIndex = hourIndex - rotTime; rotIndex <= hourIndex + rotTime; rotIndex++) {
						if (rotIndex >= 0 && rotIndex < day.getListOfHours().size()) {
							Hour rotHour = day.getListOfHours().get(rotIndex);
							for (Slot rotSlot : rotHour.getHourSlots()) {
								if (rotSlot != ySlot) {
									if (rotSlot.getCategory().equals(thisCat) && !rotSlot.equals(thisSlot)) {
										Arc arc = new Arc(rotSlot, thisSlot, 1);
										queue.add(arc);
									}
								}
							}
						}
					}
				}
			}
		}

	}

	private boolean eraseInconsistentValues(Arc arc) {
		boolean removed = false;
		List<Song> songsToRemove = new ArrayList<Song>();
		for (Song songOfX : arc.getX().getRemainingSongs()) {
			int numOfSatisfiedY = 0;
			for (Song songOfY : arc.getY().getRemainingSongs()) {
				boolean satisfied = checkConstraints(songOfX, songOfY, arc.getKind());
				if (satisfied) {
					numOfSatisfiedY++;
				}
			}
			if (numOfSatisfiedY == 0) {
				songsToRemove.add(songOfX);
				removed = true;
			}
		}
		for (Song song : songsToRemove) {
			arc.getX().getRemainingSongs().remove(song);
		}

		return removed;
	}

	private boolean checkConstraints(Song songOfX, Song songOfY, int kind) {
		if (kind == 0) {
			if (songOfX.getArtist().equals(songOfY.getArtist())) {
				return false;
			}
			if (songOfX.getName().equals(songOfY.getName())) {
				return false;
			}
			if (songOfX.getEnergy() == songOfY.getEnergy()) {
				return false;
			}
			if (songOfX.getGenre() == songOfY.getGenre()) {
				return false;
			}
			if (songOfX.getTempo() == songOfY.getTempo()) {
				return false;
			}
		}
		if (kind == 1) {
			if (songOfX.equals(songOfY)) {
				return false;
			}
		}

		return true;
	}

	private void initializeDay(Day day) {

		for (int hourIndex = 0; hourIndex < day.getListOfHours().size(); hourIndex++) {
			Hour hour = day.getListOfHours().get(hourIndex);
			for (int slotIndex = 0; slotIndex < hour.getHourSlots().size(); slotIndex++) {
				Slot thisSlot = hour.getHourSlots().get(slotIndex);
				for (Song song : thisSlot.getCategory().getSongList()) {
					// reduziere Domäne
					thisSlot.getRemainingSongs().add(song);
					// füge Kanten zur queue hinZu

				}
				if (slotIndex < (hour.getHourSlots().size() - 1)) {
					Slot postSlot = hour.getHourSlots().get(slotIndex + 1);
					Arc arc = new Arc(thisSlot, postSlot, 0);
					queue.add(arc);
				}
				if (slotIndex > 0) {
					Slot preSlot = hour.getHourSlots().get(slotIndex - 1);
					Arc arc = new Arc(thisSlot, preSlot, 0);
					queue.add(arc);
				}
				Category thisCat = thisSlot.getCategory();
				int rotTime = thisCat.getMaxRot();
				for (int rotIndex = hourIndex - rotTime; rotIndex <= hourIndex + rotTime; rotIndex++) {
					if (rotIndex >= 0 && rotIndex < day.getListOfHours().size()) {
						Hour rotHour = day.getListOfHours().get(rotIndex);
						for (Slot rotSlot : rotHour.getHourSlots()) {
							if (rotSlot.getCategory().equals(thisCat) && !rotSlot.equals(thisSlot)) {
								Arc arc = new Arc(thisSlot, rotSlot, 1);
								queue.add(arc);
							}
						}
					}
				}
			}
		}
	}
}
