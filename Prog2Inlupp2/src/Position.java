
public class Position {
	public double x;
	public double y;
	
	//HashMap<double, String> positions = new HashMap<Position(), name>();
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getXCoordinate() {
		return x;
	}
	
	public double getYCoordinate() {
		return y;
	}
	
	@Override
	public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position other = (Position) obj;
            return (x == other.x && y == other.y);
        }
        return false;
    }
	
	@Override
    public String toString() {
        return "{ " + getXCoordinate() + "," + getYCoordinate() + " }";
    }
	  
}