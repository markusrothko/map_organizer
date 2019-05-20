
public class DescribedPlace extends Place {

	private String description;
	
	public DescribedPlace(String name, String category, boolean selected, boolean hidden, Position p, String description) {
		super(name, category, selected, hidden, p);
		this.description = description;
	}
	
	public String getDescription () {
		return description;
	}
	
	@Override
    public String toString() {
        return "Described place," + getCategory() + "," + p.getXCoordinate() + "," + p.getYCoordinate() + "," + getName() + "," + description;
    }
}