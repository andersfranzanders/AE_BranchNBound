package wrappers;

import java.util.ArrayList;
import java.util.List;

public class Day {

	private int hoursOfDay;
	private List<Hour> listOfHours;
	public int remainingSlots = 0;
	private int totalViolations = 0;

	public Day(int hoursOfDay) {
		this.hoursOfDay = hoursOfDay;
		this.listOfHours = new ArrayList<Hour>();
	}

	public int getHoursOfDay() {
		return hoursOfDay;
	}

	public void setHoursOfDay(int hoursOfDay) {
		this.hoursOfDay = hoursOfDay;
	}

	public List<Hour> getListOfHours() {
		return listOfHours;
	}

	public void setListOfHours(List<Hour> listOfHours) {
		this.listOfHours = listOfHours;
	}

	public void addHourToList(Hour hour) {
		if (listOfHours.size() <= hoursOfDay) {
			this.listOfHours.add(hour);
		}
	}

	public String toString() {
		String s = "";
		s += "Planned Day: \n";
		s += "Total Violations " + this.totalViolations + " \n";
		int i = 0;
		for (Hour hour : listOfHours) {
			s += "hour: " + i + "\n";
			s += hour.toString();
			s += "\n";
			i++;
		}
		
		return s;

	}

	public int calNumberOfSlots() {
		int counter = 0;
		for (Hour hour : listOfHours) {
			counter += hour.getHourSlots().size();
		}
		return counter;
	}

	public int getTotalViolations() {
		return totalViolations;
	}

	public void setTotalViolations(int totalViolations) {
		this.totalViolations = totalViolations;
	}

	public double getRelativeViolations() {
		return (double) totalViolations / calNumberOfSlots();
	}

	public static Day deepCopyDay(Day oldDay) {

		Day newDay = new Day(oldDay.getHoursOfDay());
		newDay.setTotalViolations(oldDay.getTotalViolations());
		newDay.remainingSlots = oldDay.remainingSlots;

		List<Hour> newListOfHours = new ArrayList<Hour>();
		for (Hour oldHour : oldDay.getListOfHours()) {
			
			List<Slot> newSlotList = new ArrayList<Slot>();
			for (Slot oldSlot : oldHour.getHourSlots()) {
				Slot newSlot = new Slot(oldSlot.getCategory());
				newSlot.setSong(oldSlot.getSong());
				newSlotList.add(newSlot);
			}
			Hour newHour = new Hour(oldHour.getName());
			newHour.setHourSlots(newSlotList);
			newListOfHours.add(newHour);
		}
		newDay.setListOfHours(newListOfHours);

		return newDay;
	}
}
