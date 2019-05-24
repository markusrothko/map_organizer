import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DescribedPlace extends Place {

	private String description = "null", toString;
	
	public DescribedPlace(String name, String category, double x, double y, String description) {
		super(name, category, x, y);
		if (!description.equals(""))
			this.description = description;
		toString = "Described," + category + "," + (int) x + "," + (int) y + "," + super.name + "," + this.description;
	}
	
	public String getDescription() {
		return description;
	}
	
	
	
	@Override
	public void showPlaceDescription() {
		Alert alert = new Alert(AlertType.INFORMATION, description);
		alert.setHeaderText(name + " [ x" + posi.getXCoordinate() + " / y" + posi.getYCoordinate() + " ]");
		//alert.setTitle(category);
		alert.showAndWait();
	}
	
	@Override
	public String toString() {
		return toString;
	}
}