public class NamedPlace extends Place {

    public NamedPlace(String name, String category, boolean selected, boolean hidden, Position p) {
    	super(name, category, selected, hidden, p);
    }

    @Override
    public String toString() {
        return "Named," + getCategory() + "," + p.getXCoordinate() + "," + p.getYCoordinate() + "," + getName();
    }

}