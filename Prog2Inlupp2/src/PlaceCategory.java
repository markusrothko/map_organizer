/*
Markus Bowie, 19841205-0075
Carl Sunnberg 19990330-3395
*/

import javafx.scene.paint.Color;

public enum PlaceCategory {
	BUS("Bus", Color.RED), 
	TRAIN("Train", Color.GREEN), 
	UNDERGROUND("Underground", Color.BLUE),
	NONE("None", Color.BLACK);

	private final String name;
	private final Color color;

	PlaceCategory(String name, Color color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public String toString() {
		return getName();
	}
}