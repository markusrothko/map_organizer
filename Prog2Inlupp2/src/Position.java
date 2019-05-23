
public class Position {
	private double x;
	private double y;
	private int hashMulti;
	
	
	//HashMap<double, String> positions = new HashMap<Position(), name>();
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
		hashMulti = getHashMulti();	
	}
	
	public double getXCoordinate() {
		return x;
	}
	
	public double getYCoordinate() {
		return y;
	}
	
	private int getHashMulti() {
		int lengthX = String.valueOf(x).length() - 1;
		int lengthY = String.valueOf(y).length() - 1;
		int multiplier = 1;
		
		if(lengthX > lengthY)
			for(int i = 1; i < lengthX; i++) 
				multiplier = multiplier * 10;
		else
			for(int i = 1; i < lengthY; i++) 
				multiplier = multiplier * 10;
		return multiplier;
	}
	
	@Override
	public int hashCode() {
		return (int) ((x * hashMulti) + y);
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