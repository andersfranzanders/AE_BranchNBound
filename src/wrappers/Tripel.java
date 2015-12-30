package wrappers;

public class Tripel {
	
	private int hourIndex;
	private int slotIndex;
	private int totalZeroViolations;
	
	public Tripel(int hourIndex, int slotIndex, int totalZeroViolations) {
		super();
		this.hourIndex = hourIndex;
		this.slotIndex = slotIndex;
		this.totalZeroViolations = totalZeroViolations;
	}
	public int getHourIndex() {
		return hourIndex;
	}
	public void setHourIndex(int hourIndex) {
		this.hourIndex = hourIndex;
	}
	public int getSlotIndex() {
		return slotIndex;
	}
	public void setSlotIndex(int slotIndex) {
		this.slotIndex = slotIndex;
	}
	public int getTotalZeroViolations() {
		return totalZeroViolations;
	}
	public void setTotalZeroViolations(int totalZeroViolations) {
		this.totalZeroViolations = totalZeroViolations;
	}
	@Override
	public String toString() {
		return "Tripel [hourIndex=" + hourIndex + ", slotIndex=" + slotIndex + ", totalZeroViolations="
				+ totalZeroViolations + "]";
	}
	
	

}
