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
	private TextField description = new TextField();
	
	public DescribedPlaceHandler() {
		super(AlertType.CONFIRMATION);
		
		GridPane grid = new GridPane();
		grid.addRow(0, new Label("Name of location:"), nameField);
		grid.addRow(1, new Label("Description:"), description);
		
		getDialogPane().setContent(grid);
	}
	
	// return the name & description
	public String getName() {
		String correctedName = nameField.getText();
		String s1 = correctedName.substring(0, 1).toUpperCase();
		String nameCapitalized = s1 + correctedName.substring(1);
		return nameCapitalized;
	}
	
	public String getDescription() {
		String correctedDescription = nameField.getText();
		String s1 = correctedDescription.substring(0, 1).toUpperCase();
		String descriptionCapitalized = s1 + correctedDescription.substring(1);
		return descriptionCapitalized;
	}

}