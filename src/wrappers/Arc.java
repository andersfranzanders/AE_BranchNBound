package wrappers;

public class Arc {

	private Slot x;
	private Slot y;
	//kind = 0 heißt "Nachbar", kind = 1 heißt "gleiche kategorie und kein Nachbar"
	private int kind;
	
	public Slot getX() {
		return x;
	}
	public void setX(Slot x) {
		this.x = x;
	}
	public Slot getY() {
		return y;
	}
	public void setY(Slot y) {
		this.y = y;
	}
	public Arc(Slot x, Slot y, int kind) {
		super();
		this.x = x;
		this.y = y;
		this.kind = kind;
	}
	
	
	@Override
	public String toString() {
		return "Arc [x=" + x + ", y=" + y + ", kind=" + kind + "]";
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	
	
	
	
}
