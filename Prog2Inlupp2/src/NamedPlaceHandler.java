/*
Markus Bowie, 19841205-0075
Carl Sunnberg 19990330-3395
*/

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class NamedPlaceHandler extends Alert {
	
	private TextField nameField = new TextField();
	
	public NamedPlaceHandler(double x, double y) {
		super(AlertType.CONFIRMATION);
		
		GridPane grid = new GridPane();
		grid.addRow(0, new Label("Name of location:"), nameField);
		
		getDialogPane().setContent(grid);
	}
	
	// return the name & description
	public String getName() {
		return nameField.getText();
	}

}