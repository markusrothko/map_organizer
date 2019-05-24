/*
Markus Bowie, 19841205-0075
Carl Sunnberg 19990330-3395
*/

import javafx.scene.control.Alert;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class CoordinateHandler extends Alert {
	
	private TextField xCordField = new TextField();
	private TextField yCordField = new TextField();

	
	public CoordinateHandler() {
		super(AlertType.CONFIRMATION);
		
		GridPane grid = new GridPane();
		grid.addRow(0, new Label("x:"), xCordField);
		grid.addRow(1, new Label("y:"), yCordField);
		
		getDialogPane().setContent(grid);
	}
	
	// return the entered x & y coordinate
	public String getXCord() {
		return xCordField.getText();
	}
	
	public String getYCord() {
		return yCordField.getText();
	}

}