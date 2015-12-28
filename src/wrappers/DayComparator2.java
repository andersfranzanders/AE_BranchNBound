package wrappers;

import java.util.Comparator;

public class DayComparator2 implements Comparator<Day> {

	@Override
	public int compare(Day day1, Day day2) {

		return day1.remainingSlots - day2.remainingSlots;

	}

}
