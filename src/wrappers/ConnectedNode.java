package wrappers;

public class ConnectedNode {
	
	private Slot slot;
	private int type;
	public ConnectedNode(Slot slot, int type) {
		super();
		this.slot = slot;
		this.type = type;
	}
	public ConnectedNode(){}
	@Override
	public String toString() {
		return "ConnectedNode [slot=" + slot.getCategory().getName() + ", type=" + type + "]";
	}
	public Slot getSlot() {
		return slot;
	}
	public void setSlot(Slot slot) {
		this.slot = slot;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	

}
