package wrappers;

import java.util.Comparator;

public class DayComparator implements Comparator<Day>{

	@Override
	public int compare(Day day1, Day day2) {
		int value = day1.getTotalViolations() - day2.getTotalViolations();

		if(value == 0){
			return day1.remainingSlots - day2.remainingSlots;
		}
		
		return value;
	}

}
