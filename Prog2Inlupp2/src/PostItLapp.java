

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class PostItLapp extends BorderPane {
 private TextArea textArea = new TextArea();
    private Pane balk = new Pane();

    private double startX, startY;

    public PostItLapp(double x, double y) {
        relocate(x, y);
        setPrefWidth(30);
        setPrefHeight(30);
        balk.setPrefSize(30, 30);
        setTop(balk);
           setCenter(textArea);
        balk.setStyle("-fx-background-color: orange");
        //      setOnMouseDragged(new DragHandler());
       setOnMousePressed(new StartDrag());
        //      setOnKeyPressed(new PilHandler());
        balk.focusedProperty().addListener((obs, old, nev) -> {
            if (nev) balk.setStyle("-fx-background-color: red");
            else balk.setStyle("-fx-background-color: orange");
            System.out.println("Focus chnaged");
        });
    }


//    class DragHandler implements EventHandler<MouseEvent>{
//        @Override public void handle(MouseEvent event) {
//            double x = getLayoutX() + event.getX() - startX;
//            double y = getLayoutY() + event.getY() - startY;
//            relocate(x,y);
//        }
//    }

    class StartDrag implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            balk.requestFocus();
            startX = event.getX();
            startY = event.getY();
        }
    }
}

//    class PilHandler implements EventHandler<KeyEvent>{
//        @Override public void handle(KeyEvent event) {
//            double x = getLayoutX();
//            double y = getLayoutY();
//            switch(event.getCode()) { //KeyCode
//                case DOWN: y++; break;
//                case UP: y--; break;
//                case LEFT: x--; break;
//                case RIGHT: x ++; break;
//            }
//            relocate(x,y);
//            event.consume();
//        }
//    }
//}
