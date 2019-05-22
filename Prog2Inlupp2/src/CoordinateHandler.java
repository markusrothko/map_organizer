/*
Markus Bowie, 19841205-0075
Carl Sunnberg 19990330-3395
*/

import javafx.scene.control.Alert;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class CoordinateHandler extends Alert {
	private TextField xField = new TextField();
	private TextField yField = new TextField();

	public CoordinateHandler() {
		super(AlertType.CONFIRMATION);
		
		GridPane grid = new GridPane();
		grid.addRow(0, new Label("x:"), xField);
		grid.addRow(1, new Label("y:"), yField);
		
		getDialogPane().setContent(grid);
	}
	
	// return the entered x & y coordinate
	public double getXCoordinate() {
		return Double.parseDouble(xField.getText());
	}
	
	public double getYCoordinate() {
		return Double.parseDouble(yField.getText());
	}

}