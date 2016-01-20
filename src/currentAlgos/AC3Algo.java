package currentAlgos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import wrappers.Arc;
import wrappers.Category;
import wrappers.ConnectedNode;
import wrappers.Day;
import wrappers.Hour;
import wrappers.Slot;
import wrappers.Song;

public class AC3Algo {

	Set<Arc> queue = new HashSet<Arc>();
	
	

	public Day planDay(Day day) {

	//	initializeDay(day);
		while (!queue.isEmpty()) {
			Arc arc = queue.iterator().next();
			queue.remove(arc);
			if (eraseInconsistentValues(arc)) {
				addArcsToX(day, arc.getX(), arc.getY());
			}

		}
		//System.out.println("Okay, prepared that shit. Thats left: ---------------");
		//printRemainingSongs(day);
		//System.out.println("Okay, Time for sum Greeedy. -----------------------");
		
		GraphGreedy gGreedy = new GraphGreedy();

		return gGreedy.planDay(day);
	}

	private void printRemainingSongs(Day day) {
		for (Hour hour : day.getListOfHours()) {
			for (Slot slot : hour.getHourSlots()) {
				System.out.println(slot.getCategory().getName());
				for (Song song : slot.getRemainingSongs()) {
					System.out.println(song);
				}
			}
		}

	}

	private void addArcsToX(Day day, Slot xSlot, Slot ySlot) {
		for (ConnectedNode node : xSlot.getConnectedNodes()) {
			if (node.getSlot() != ySlot) {
				Arc arc = new Arc(node.getSlot(), xSlot, node.getType());
				queue.add(arc);
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

	public void initializeDay(Day day) {

		for (Hour hour : day.getListOfHours()) {
			for (Slot slot : hour.getHourSlots()) {
				for (ConnectedNode node : slot.getConnectedNodes()) {
					queue.add(new Arc(slot, node.getSlot(), node.getType()));
				}
			}
		}
	}
}
