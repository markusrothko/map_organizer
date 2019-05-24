import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public abstract class Place extends Polygon {
	protected String name;
	protected final String category;
	private final Color color;
	private final Color markedColor;
	private SimpleBooleanProperty isMarked = new SimpleBooleanProperty();
	
	protected final Position posi;

	public Place(String name, String category, double x, double y) {

//		super (p.getXCoordinate(), p.getYCoordinate(), p.getXCoordinate()-15, p.getYCoordinate()-30,
//		p.getXCoordinate()+15, p.getXCoordinate()-30);

		this.name = name;
		posi = new Position(x, y);
		this.category = category;
		
		this.setOnMouseClicked(new MarkerEvent());
		isMarked.set(true);
		

		switch (category.toUpperCase()) {
		case "BUS":
			color = Color.DARKRED;
			markedColor = Color.RED;
			setupMarker();
			break;
		case "TRAIN":
			color = Color.GREEN;
			markedColor = Color.LIMEGREEN;
			setupMarker();
			break;
		case "UNDERGROUND":
			color = Color.DEEPSKYBLUE;
			markedColor = Color.SKYBLUE;
			setupMarker();
			break;
		default:
			color = Color.BLACK;
			markedColor = Color.DIMGRAY;
			setupMarker();
		}

	}
	
	private void setupMarker() {
		getPoints().addAll(new Double[] { 0.0, 0.0, -10.0, -20.0, 10.0, -20.0 });
		setFill(color);
		relocate(getX() - 10, getY() - 20);
		setMarkedProperty();
	}

	private void setMarkedProperty() {
		if (isMarked.getValue()) {
			relocate(getX() - 10, getY() - 23);
			setStroke(Color.BLACK);
			setFill(markedColor);
			setStrokeWidth(3);
		} else {
			setStroke(null);
			setFill(color);
			relocate(getX() - 10, getY() - 20);
		}
	}
	
	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}
	
	public double getX() {
		return posi.getXCoordinate();
	}

	public double getY() {
		return posi.getYCoordinate();
	}

	public Position getPosi() {
		return posi;
	}
	public SimpleBooleanProperty getBool() {
		return isMarked;
	}

	public boolean isMarked() {
		return isMarked.getValue();
	}

	public void setMarkedProperty(boolean bool) {
		isMarked.set(bool);
		setMarkedProperty();
	}

	class MarkerEvent implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			if (event.getButton() == MouseButton.PRIMARY) {
				isMarked.set(!isMarked.getValue());
				setMarkedProperty();
			} else if (event.getButton() == MouseButton.SECONDARY)
				showPlaceDescription();
		}
	}
	
	
	
	

	

	
	
	
	
	
	
	abstract void showPlaceDescription();
	
	@Override
	public abstract String toString();
	
//	public void setShowInfo(boolean show) {
//        this.description = show;
//    }
}