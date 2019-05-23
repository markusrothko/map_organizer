
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;


public abstract class Place extends Polygon {
	protected String name;
	protected String category;
	private final Color color;
	//private final Color marked;
	//private boolean selected = false;
	
	protected final Position posi;

	public Place(String name, String category, double x, double y) {
		
//		super (p.getXCoordinate(), p.getYCoordinate(), p.getXCoordinate()-15, p.getYCoordinate()-30,
//		p.getXCoordinate()+15, p.getXCoordinate()-30);
		
		this.name = name;
		this.category = category;
		//this.selected = selected;
		
		posi = new Position(x, y);

		
		//setBounds(p.getXCoordinate() - 10, p.getYCoordinate() - 10, 20, 20);
        //setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

	}
	
	public String getName() {
		return name;
	}
	
	public String getCategory() {
		return category;
	}
	
//	public boolean getselected() {
//		return selected;
//	}
	
//	public void setSelected(boolean select){
//        this.selected = select;
//        //selected = true;
//    }

	
	switch(category.toUpperCase()) {
		case "BUS":
		 	color = Color.DARKRED;
			//markedColor = Color.RED;
			setupMarker();
		break;
		case "TRAIN":
			color = Color.GREEN;
			//markedColor = Color.LIMEGREEN;
			setupMarker();
			break;
		case "UNDERGROUND":
			color = Color.DEEPSKYBLUE;
			//markedColor = Color.SKYBLUE;
			setupMarker();
			break;
		default:
			color = Color.BLACK;
			//markedColor = Color.DIMGRAY;
			setupMarker();
		}


	public double getX() {
		return posi.getXCoordinate();
	}

	public double getY() {
		return posi.getYCoordinate();
	}

	public Position getPos() {
		return posi;
	}
	
	abstract void showPlaceDescription();
	
	@Override
	public abstract String toString();
	
//	public void setShowInfo(boolean show) {
//        this.description = show;
//    }
}