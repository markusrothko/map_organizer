/*
Markus Bowie, 19841205-0075
Carl Sunnberg 19990330-3395
*/

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class DescribedPlaceHandler extends Alert {
	
	private TextField nameField = new TextField();
	private TextField descriptionField = new TextField();
	
	public DescribedPlaceHandler() {
		super(AlertType.CONFIRMATION);
		
		GridPane grid = new GridPane();
		grid.addRow(0, new Label("Name of location:"), nameField);
		grid.addRow(1, new Label("Description:"), descriptionField);
		
		getDialogPane().setContent(grid);
	}
	
	// return the name & description
	public String getName() {
		return nameField.getText();
	}

	public String getDescription() {
		return descriptionField.getText();
	}

}