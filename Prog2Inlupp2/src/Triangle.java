import javafx.scene.shape.Polygon;

public class Triangle extends Polygon {
	public Triangle(double x, double y) {
		super(x, y, x - 15, y - 30, x + 15, y - 30);

	}
}