package oldAlgos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import wrappers.Category;
import wrappers.Day;
import wrappers.Hour;
import wrappers.Slot;
import wrappers.Song;

public class MinConflictsAlgo {

	public Day planDay(Day day) {
		
		calculateAllViolations(day);
//		Scanner sc = new Scanner(System.in);
//		Day copyDay = Day.deepCopyDay(day);

		boolean changedAtLeastOneSong = true;
//		System.out.println(day.getTotalViolations());
//		System.out.println("Using minConflicts ----------------------");
		
		while (changedAtLeastOneSong) {
			changedAtLeastOneSong = betterPlan(day);
//			System.out.println("Try again ?" + changedAtLeastOneSong);
//			System.out.println(day);
			
			//int i = sc.nextInt();
			
		}
//		System.out.println("before-------------");
//		System.out.println(copyDay);
		calculateAllViolations(day);
//		System.out.println("after--------------");
//		

		return day;
	}

	private void calculateAllViolations(Day day) {
		int sum = 0;
		for(Hour hour: day.getListOfHours()){
			for(Slot slot: hour.getHourSlots()){
				int violations = day.checkConstraints(slot.getSong(), slot);
				slot.setCurrentViolations(violations);
				sum += violations;
			}
		}
		day.setTotalViolations(sum);
		
	}

	private boolean betterPlan(Day day) {
		int numOfChangedSongs = 0;

		Set<Category> allCats = day.getAllUsedCategories();
		for (Category cat : allCats) {
		//	System.out.println("Doing cat: " + cat.getName());
			List<Song> songList = cat.getSongList();
			for (int i = 0; i < songList.size(); i++) {
				Song song = songList.get(i);
		//		System.out.println("Doing song: " + song);
				List<Slot> slotsWithSong = getSlotsWithSong(day, cat, song);
				
				int sum1 = calViolationsOfSlots(slotsWithSong);
				for (int j = i + 1; j < songList.size(); j++) {
					Song songToChange = songList.get(j);
		//			System.out.println("Try to Change with Song: " + songToChange);

					List<Slot> slotsWithSongToChange = getSlotsWithSong(day, cat, songToChange);
					int sum2 = calViolationsOfSlots(slotsWithSongToChange);
					
					if (!slotsWithSong.isEmpty() && !slotsWithSongToChange.isEmpty() & (sum1 + sum2 != 0)) {
						int changedThisSong = doChangeStuff(slotsWithSong, slotsWithSongToChange, sum1 + sum2, day);
						numOfChangedSongs += changedThisSong;
						if(changedThisSong >0){
							calculateAllViolations(day);
		//					System.out.println(day);
							sum1 = calViolationsOfSlots(slotsWithSong);
							sum2 = calViolationsOfSlots(slotsWithSongToChange);
						}
					}
				}
			}
		}
		if (numOfChangedSongs > 0) {
			return true;
		}
		return false;
	}

	private int calViolationsOfSlots(List<Slot> slotsWithSong) {
		int sum = 0;
		for (Slot slot : slotsWithSong) {
			sum += slot.getCurrentViolations();
		}
		return sum;
	}

	private int doChangeStuff(List<Slot> slotsWithSong, List<Slot> slotsWithSongToChange, int oldViolationSum, Day day) {
	//	System.out.println("Current violations " + oldViolationSum);
		Song song = slotsWithSong.get(0).getSong();
		Song songToChange = slotsWithSongToChange.get(0).getSong();
		
		//putSongsIntoSlots(slotsWithSong, null, slotsWithSongToChange, null);
		putSongsIntoSlots(slotsWithSong, songToChange, slotsWithSongToChange, song);
		int newViolationsSum = 0;
		for (Slot slot : slotsWithSong) {
			int violations = day.checkConstraints(songToChange, slot);
			newViolationsSum += violations;

		}
		for (Slot slot : slotsWithSongToChange) {
			int violations = day.checkConstraints(song, slot);
			newViolationsSum += violations;
		}
	//	System.out.println("New Violationssum " + newViolationsSum);
		if (newViolationsSum < oldViolationSum) {
			return 1;

		} else {
			putSongsIntoSlots(slotsWithSong, song, slotsWithSongToChange, songToChange);
			return 0;
		}

	}

	private void putSongsIntoSlots(List<Slot> slotsWithSong, Song song, List<Slot> slotsWithSongToChange,
			Song songToChange) {
		for (Slot slot : slotsWithSong) {
			slot.setSong(song);
		}
		for (Slot slot : slotsWithSongToChange) {
			slot.setSong(songToChange);
		}
	}

	private List<Slot> getSlotsWithSong(Day day, Category cat, Song song) {
//		System.out.println("Found in slots:  ----------");
		List<Slot> slotsWithSong = new ArrayList<Slot>();
		for (Hour hour : day.getListOfHours()) {
			for (Slot slot : hour.getHourSlots()) {
				if (slot.getCategory() == cat) {
					if (slot.getSong() == song) {
						slotsWithSong.add(slot);
//						System.out.println(slot);
					}
				}
			}
		}
		return slotsWithSong;
	}

}
