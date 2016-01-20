package currentAlgos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import wrappers.ConnectedNode;
import wrappers.Day;
import wrappers.Hour;
import wrappers.Slot;
import wrappers.Song;

public class GraphGreedy {

	public Day planDay(Day day) {
		List<Slot> listOfNodes = getAllSlots(day);

		while (!listOfNodes.isEmpty()) {
			//printOutNodes(listOfNodes);
			Slot slot = getNextSlot(listOfNodes);
			listOfNodes.remove(slot);
			if (!slot.getRemainingSongs().isEmpty()) {
				Song song = getValueByLookForward(slot);
				// System.out.println("Will plan Song: " + song);
				slot.setSong(song);
				slot.setRemainingSongs(new ArrayList<Song>());
				// System.out.println(day);
				reduceDomainsOfAffectedNodes(slot);
			} 
//			else {
//				System.out.println("--------------- Will skip this one cuz its empty.");
//			}
		}

		return day;
	}

	private void printOutNodes(List<Slot> listOfNodes) {
		System.out.println("Slots currently in work: ");
		for (Slot slot : listOfNodes) {
			System.out.println(slot);
			for (Song song : slot.getRemainingSongs()) {
				System.out.println(song);
			}
		}

	}

	private void reduceDomainsOfAffectedNodes(Slot slot) {
		for (ConnectedNode node : slot.getConnectedNodes()) {
			for (Iterator<Song> i = node.getSlot().getRemainingSongs().iterator(); i.hasNext();) {
				// for (Song vy : node.getSlot().getRemainingSongs().iterator())
				// {
				if (!checkConstraings(slot.getSong(), i.next(), node.getType())) {
					i.remove();
				}

			}
		}

	}

	private Song getValueByLookForward(Slot slot) {

		Map<Song, Integer> map = new HashMap<Song, Integer>();
		for (Song vx : slot.getRemainingSongs()) {
		//	System.out.println(vx);
			int counter = 0;
			for (ConnectedNode node : slot.getConnectedNodes()) {
				for (Song vy : node.getSlot().getRemainingSongs()) {
		//			System.out.println(checkConstraings(vx, vy, node.getType()) + " " + vy);
					if (!checkConstraings(vx, vy, node.getType())) {

						counter++;
					}
				}
			}
			map.put(vx, counter);
		}
	//	System.out.println("Look Forward said:");
	//	System.out.println(map);

		return getBestValue(map);
	}

	private Song getBestValue(Map<Song, Integer> map) {
		int minViolations = Integer.MAX_VALUE;
		Song bestSong = null;
		for (Song song : map.keySet()) {
			int violations = map.get(song);
			if (violations < minViolations) {
				minViolations = violations;
				bestSong = song;
			}
		}

		return bestSong;
	}

	private boolean checkConstraings(Song songOfX, Song songOfY, int kind) {

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

	private Slot getNextSlot(List<Slot> listOfNodes) {
		Slot nextSlot = null;
		int minDomain = Integer.MAX_VALUE;
		for (Slot slot : listOfNodes) {
			int domainSize = slot.getRemainingSongs().size();
			if (domainSize < minDomain) {
				minDomain = domainSize;
				nextSlot = slot;
			}
		}
		// System.out.println("Next Slot: " + nextSlot + "with Domain Size: " +
		// minDomain);

		return nextSlot;
	}

	private List<Slot> getAllSlots(Day day) {

		List<Slot> slotList = new ArrayList<Slot>();

		for (Hour hour : day.getListOfHours()) {
			for (Slot slot : hour.getHourSlots()) {
				slotList.add(slot);
			}
		}

		return slotList;
	}

}
