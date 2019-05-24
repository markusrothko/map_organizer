import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class NamedPlace extends Place {
	private String toString;
	
    public NamedPlace(String name, String category, double x, double y) {
    	super(name, category, x, y);
    	toString = "Named," + super.category + "," + (int)x + "," + (int)y +"," + super.name;
    }

    @Override
	public void showPlaceDescription() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(name + " [ x" + posi.getXCoordinate() + " / y" + posi.getYCoordinate() + " ]");
		alert.setTitle(category);
		alert.showAndWait();
	}
    
    @Override
	public String toString() {
		return toString;
	}
}