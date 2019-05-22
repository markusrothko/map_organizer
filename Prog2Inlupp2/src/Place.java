import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;

public class Place extends Polygon {
	private String name;
	private String category;
	private boolean selected = false;
	private boolean hidden = false;
	protected Position p;


	private TextArea textArea = new TextArea();
	private Pane balk = new Pane();
	
    //Lägg till i konstruktorn senare:  Position p
	public Place(String name, String category, boolean selected, boolean hidden, Position p) {
		super (p.getXCoordinate(), p.getYCoordinate(), p.getXCoordinate()-15, p.getYCoordinate()-30,
		p.getXCoordinate()+15, p.getXCoordinate()-30);
		this.name = name;
		this.category = category;
		this.selected = selected;
		this.hidden = hidden;
		this.p = p;

		
		//setBounds(p.getXCoordinate() - 10, p.getYCoordinate() - 10, 20, 20);
        //setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

	}
	
	public String getName() {
		return name;
	}
	
	public String getCategory() {
		return category;
	}
	
	public boolean getselected() {
		return selected;
	}
	
	public void setSelected(boolean select){
        this.selected = select;
        //selected = true;
    }
	
	public boolean gethidden() {
		return hidden;
	}
	
//	public double getxcoord() {
//		return xcoord;
//	}
//	public double getycoord() {
//		return ycoord;
//	}
	
	@Override
    public String toString() {
        return "Place is " + category + "," + p.getXCoordinate() + "," + p.getYCoordinate() + "," + name;
    }
	
//	public void setShowInfo(boolean show) {
//        this.description = show;
//    }
	
	protected void paintComponent(GraphicsContext Triangle) {
   //     super.paintComponent(Triangle);

        if (category.equals("Bus")) {
        	Triangle.setFill(Color.RED);
        } else if (category.equals("Underground")) {
        	Triangle.setFill(Color.BLUE);
        } else if (category.equals("Train")) {
        	Triangle.setFill(Color.GREEN);
        } else if (category.equals("None")) {
        	Triangle.setFill(Color.BLACK);
        }
        if (selected) {
            System.out.println("Going to selected...");

            requestFocus();
            Triangle.setFill(Color.YELLOW);
        }

  //      g.fillPolygon(xPoint, yPoint, 3);

    }
}