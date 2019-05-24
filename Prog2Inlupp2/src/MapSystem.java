import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MapSystem extends Application {

    private Stage primaryStage;
    private TextField wordField = new TextField();
    private ImageView display = new ImageView();
    List<Place> places = new ArrayList<>();

    //Hashmap för sökning genom position(x & y)
    Map<Position, Place> positionList = new HashMap<>();
    // Hashmap för att söka genom namn
    HashMap<String, List<Place>> nameList = new HashMap<>();
    //datastuktur för alla markerade platser
    ArrayList<Place> markedPlaces = new ArrayList<>();
    //datastruktur för alla underground
    ArrayList<Place> allUnderground = new ArrayList<>();
    //datastruktur för alla train
    ArrayList<Place> allTrain = new ArrayList<>();
    //datastruktur för alla bus
    ArrayList<Place> allBus = new ArrayList<>();

    private boolean changed = false;
    private ToggleGroup group = new ToggleGroup();
    //private RadioButton namedButton = new RadioButton("Named");
    //private RadioButton describedButton = new RadioButton("Described");
    private RadioButton namedPlace, describedPlace;
    private MenuBar dropDownMenu;
    private MenuItem saveItem, exitChoiceItem, loadMapItem, loadPlaces;
    private boolean undergroundSelected = false;
    private boolean busSelected = false;
    private boolean trainSelected = false;
    BorderPane root = new BorderPane();
    Button newButton = new Button("New");
    private ClickHandler clickHandler = new ClickHandler();
    private ObservableList<String> categories;
    private ListView<String> cat;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        //	BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Map System");
        primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new ExitHandler());
        primaryStage.show();

        // TOP SECTION

        //  FILE MENU

        VBox vbox = new VBox();
        dropDownMenu = new MenuBar();
        vbox.getChildren().add(dropDownMenu);
        Menu archiveMenu = new Menu("File");
        dropDownMenu.getMenus().add(archiveMenu);
        loadMapItem = new MenuItem("Load Map");
        archiveMenu.getItems().add(loadMapItem);
        loadMapItem.setOnAction(new OpenHandler());
        loadPlaces = new MenuItem("Load Places");
        archiveMenu.getItems().add(loadPlaces);
        loadPlaces.setOnAction(new OpenHandler());
        saveItem = new MenuItem("Save");
        archiveMenu.getItems().add(saveItem);
        saveItem.setOnAction(new SaveHandler());
        exitChoiceItem = new MenuItem("Exit");
        archiveMenu.getItems().add(exitChoiceItem);

        //exithandler nedan

        // archiveMenu.getItems().add(exitItem);
        // exitItem.setOnAction(e -> primaryStage.fireEvent(
        // new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST)));

        // TOP INPUT

        HBox hboxTop = new HBox(15);
        vbox.getChildren().add(hboxTop);
        hboxTop.setPadding(new Insets(15));
        hboxTop.setAlignment(Pos.CENTER);
        // Button newButton = new Button("New");
        //newButton.setOnAction(new newButtonHandler());
        newButton.setOnAction(new NewLocation());
        
        VBox vbs = new VBox(10);
        namedPlace = new RadioButton("Named");
        describedPlace = new RadioButton("Described");
        vbs.getChildren().addAll(namedPlace, describedPlace);
        namedPlace.setToggleGroup(group);
        describedPlace.setToggleGroup(group);
        namedPlace.setSelected((true));
        Button searchButton = new Button("Search");
        searchButton.setOnAction(new SearchHandler());
        Button hideButton = new Button("Hide");
        hideButton.setOnAction(new HideHandler());
        Button removeButton = new Button("Remove");
        removeButton.setOnAction(new RemoveHandler());
        Button coordinatesButton = new Button("Coordinates");
        coordinatesButton.setOnAction(new CoordinateSearch());

        //tror inte det nedan behövs längre

/*	    group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	        public void changed(ObservableValue<? extends Toggle> ov,
	            Toggle old_toggle, Toggle new_toggle) {
	          if (group.getSelectedToggle() != null) {
	            System.out.println(group.getSelectedToggle().toString());
	          }
	        }
	      });*/


        // RIGHT SECTION

        VBox listan = new VBox();
        listan.setPadding(new Insets(5));
        cat = new ListView<>(categories);
        categories = FXCollections.observableArrayList("Underground", "Bus", "Train", "None");
       
        
        
        
        listan.getChildren().add(new Label("Categories"));
        listan.getChildren().add(cat);
		cat.setItems(categories);

        cat.getSelectionModel().selectedItemProperty().addListener(new ListHandler());
        
        Button hideCategoryButton = new Button("Hide Category");
        hideCategoryButton.setAlignment(Pos.CENTER);
        listan.getChildren().add(hideCategoryButton);
        listan.setPrefSize(200, 200);
        hideCategoryButton.setOnAction(new HideCategoryButtonHandler());
        hboxTop.getChildren().addAll(newButton, vbs, wordField, searchButton, hideButton, removeButton, coordinatesButton);
        wordField.setPromptText("Enter search:");
        root.setTop(vbox);
        // root.setCenter(imageView);
        // display.setWrapText(true);
        root.setCenter(display);
        root.setRight(listan);
        // listan.setPadding(new Insets(5));
        // listan.setPrefSize(100, 400);
//        listan.getSelectionModel().selectedItemProperty().addListener(
//         new ListHandler());

    }
    private String getSelectedCategory() {
		// Just in case nothing is selected
		if (cat.getSelectionModel().getSelectedItem() == null)
			return "None";
		return cat.getSelectionModel().getSelectedItem();
	}
                  // EXIT-HANDLER

    class ExitHandler implements EventHandler<WindowEvent> {
        public void handle(WindowEvent event) {
            if (changed) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setContentText("Osparat. Avsluta ändå?");
                Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent() && res.get() == ButtonType.CANCEL)
                    event.consume();
            }
        }
    }



                // SEARCH-HANDLER
    /*
     * class SearchHandler implements EventHandler<ActionEvent>{
     *
     * @Override public void handle(ActionEvent event) { String word =
     * wordField.getText(); if (word.isEmpty()) word =
     * listan.getSelectionModel().getSelectedItem(); else
     * listan.getSelectionModel().select(word); if (word == null || uppslag == null)
     * return; String def = uppslag.get(word); display.setText(def); } }
     */

                 // CATEGORY-HANDLER

    class ListHandler implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue obs, String old, String nev) {
            switch (nev) {
                case "Underground":
                    //gör alla undergroundplatser synliga
                    System.out.println(nev);
                    undergroundSelected = true;
                    trainSelected = false;
                    busSelected = false;
                    break;
                case "Train":
                    //gör alla trainplatser synliga
                    System.out.println(nev);
                    undergroundSelected = false;
                    trainSelected = true;
                    busSelected = false;
                    break;
                case "Bus":
                    //gör alla busplatser synliga
                    System.out.println(nev);
                    undergroundSelected = false;
                    trainSelected = false;
                    busSelected = true;
                    break;
                default:
                    //gör category null
            }

        }
    }

            //  NY NEWBUTTONHANDLER

    class newButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            root.addEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
            root.setCursor(Cursor.CROSSHAIR);
            newButton.setDisable(true);
        }
    }
                     // CLICKHANDLER

    class ClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            
            
            //PostItLapp lapp = new PostItLapp(x, y);
            NamedPlace p = new NamedPlace(null, null, event.getX(), event.getY());
            root.getChildren().add(p);
            root.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
            root.setCursor(Cursor.DEFAULT);
            newButton.setDisable(false);
        }
    }
             // GAMMAL NEWBUTTONHANDLER
	/*
	public class newButtonHandler implements EventHandler<ActionEvent> {
		boolean new_is_pressed = false;
		public void handle(ActionEvent event) {
			display.setCursor(Cursor.CROSSHAIR);
			//Lyssnar var användare klickar i kartan och hämtar koordinaterna
			new_is_pressed = true;
			display.setOnMouseClicked(new NewPlace());
			*/

//			while (new_is_pressed) {
//				handleMouseClickAction(null);
//				
//			}
    //handleMouseClickAction(null);

    //Stäng av mus lyssnaren
    //Kolla kategoring och radiobutton
    //Startar Named eller Described placehandler beroende på radiobuttons
    //Objectet skapas i dialogen eller här.

    class NewPlace implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
//            double x = event.getX();
//            double y = event.getY();
            //new NewNamePlace();
           
            }
            //	System.out.println(x + " " + y);
/*			if (group.getSelectedToggle() == null)
			{
				System.out.println("Inget valt");
				return;
				//&& namedButton.isSelected()
				//System.out.println(group.getSelectedToggle());
			}
			if (group.getSelectedToggle().toString().contains("Named"))
			{
				System.out.println("Named funkar");
					//&& namedButton.isSelected()
				//System.out.println(group.getSelectedToggle());
			}
			if (group.getSelectedToggle().toString().contains("Described"))
			{
				System.out.println("Described funkar");
				//&& namedButton.isSelected()
				//System.out.println(group.getSelectedToggle());
			}*/
//            if ((group.getSelectedToggle().toString().contains("Described")) && undergroundSelected) {
//                System.out.println("Described funkar tillsammans med underground");
//            } else if ((group.getSelectedToggle().toString().contains("Described")) && busSelected) {
//                System.out.println("Described funkar tillsammans med buss");
//            } else if ((group.getSelectedToggle().toString().contains("Described")) && trainSelected) {
//                System.out.println("Described funkar tillsammans med tåg");
//            } else if ((group.getSelectedToggle().toString().contains("Named")) && undergroundSelected) {
//                System.out.println("Named funkar tillsammans med underground");
//            } else if ((group.getSelectedToggle().toString().contains("Named")) && busSelected) {
//                System.out.println("Named funkar tillsammans med buss");
//            } else if ((group.getSelectedToggle().toString().contains("Named")) && trainSelected) {
//                System.out.println("Named funkar tillsammans med tåg");
//            } else {
//                System.out.println("Inget");
//            }
            //uppdaterings test
            //Kolla radiobutton
            //kolla kategori
            //Skapa plats mha diologruta
            //Avsluta new place function
        
    }
    //private void disableNewPlaceCursor() {
    //display.setCursor(Cursor.DefaultCursor);
    //display.removeMouseListener(mouseLyss);
    //newButton.setEnabled(true);
    // }

    //setOnMouseClicked(EventHandler<MouseEvent>);

//	private void handleMouseClickAction(MouseEvent event) {
//	    double x = event.getX();
//	    double y = event.getY();
//	    if (!isFirstLine && freehandButton.isSelected())
//	        gc.strokeLine(oldX, oldY, x, y);
//
//	    isFirstLine = false;
//	  //  oldX = x;
//	    //oldY = y;
//
//	    System.out.printf("(%f, %f)\n", x, y);
//	}

            // FILE NAVIGATION

    @SuppressWarnings("unchecked")
    class OpenHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Map File");
                fileChooser.setInitialDirectory(new File("C:\\Prog2Inlupp2"));
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file == null)
                    return;
                String filnamn = file.getAbsolutePath();

                FileInputStream fis = new FileInputStream(filnamn);
                Image image = new Image(new FileInputStream(filnamn), 600, 600, true, true);
                display.setImage(image);
                // ois.close();
                fis.close();

                //ObservableList<String> words = FXCollections.observableArrayList(uppslag.keySet());
                //FXCollections.sort(words);
                // listan.setItems(words);
            } catch (FileNotFoundException fnfe) {
                System.err.println("Ingen sån fil!");
                // }catch(ClassNotFoundException cnff){
                System.err.println("Fatalt fel har inträffat!");
            } catch (IOException ioe) {
                System.err.println("IO-fel har inträffat!");
                System.err.println(ioe.getMessage());
            }
        }
    }

    // SAVE HANDLER

    class SaveHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showSaveDialog(primaryStage);
                if (file == null)
                    return;
                String filnamn = file.getAbsolutePath();

                FileOutputStream fos = new FileOutputStream(filnamn);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                //oos.writeObject(uppslag);
                oos.close();
                fos.close();
            } catch (FileNotFoundException fnfe) {
                System.err.println("Filen går ej att skriva!");
            } catch (IOException ioe) {
                System.err.println("Fel har inträffat!");
            }
        }
    }


    // COORDINATE SEARCH

	class CoordinateSearch implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			CoordinateHandler dialog = new CoordinateHandler();
			dialog.setTitle("Input coordinates:");
			Optional<ButtonType> result = dialog.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				if (Double.parseDouble(dialog.getXCord()) >= 0 && Double.parseDouble(dialog.getYCord()) >= 0) {
					if (positionList.containsKey(new Position(Double.parseDouble(dialog.getXCord()),
							Double.parseDouble(dialog.getYCord())))) {
						//unmarkAll();
						Place p = positionList.get(new Position(Double.parseDouble(dialog.getXCord()),
								Double.parseDouble(dialog.getYCord())));
						//p.setMarkedProperty(true);
						p.setVisible(true);
					} //else?
						//noPlaceError();
				} //else?
					//negativeNumError();
			}
//			double x = dialog.getXCord();
//			double y = dialog.getYCoordinate();
			// jämför de mottagna koordinaterna med de koordinater som finns i positionlist
			// om den inte hittar en plats ska det komma ett felmeddelande
			// gör alla (evenutella) markerade platser omarkerade
			// om dem mottagna koord hittar en plats ska den platsen bli markerad
			// och om den platsen är osynlig så ska den bli synlig

		}

	}


               // NEW NAME PLACE

    class NewLocation implements EventHandler<ActionEvent> {
        private Place newP;
        
        @Override
        public void handle(ActionEvent event) {
			display.setCursor(Cursor.CROSSHAIR);
        	if (namedPlace.isSelected()) {
        		display.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						createNamedPlace(event.getX(), event.getY());
					}
				});
			} else  {
				display.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						createDescribedPlace(event.getX(), event.getY());
					}
				});
			}
        }
        private void createNamedPlace(double x, double y) {
			if (!positionList.containsKey(new Position(x, y))) {
				NamedPlaceHandler named = new NamedPlaceHandler(x, y);
				Optional<ButtonType> result = named.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					newP = new NamedPlace(cat.getSelectedCategory(), named.getName(), x, y);
					//storePlace(newP);
					//hasChanged.set(true);
				}
			} else
				error("Could not create place here!");
			restoreMouse();
		}

		private void createDescribedPlace(double x, double y) {
			if (!searchPos.containsKey(new Position(x, y))) {
				CreateDescribedPlace described = new CreateDescribedPlace(x, y);
				Optional<ButtonType> anwser = described.showAndWait();
				if (anwser.isPresent() && anwser.get() == ButtonType.OK) {
					newPlace = new DescribedPlace(getSelectedCategory(), described.getName(), x, y,
							described.getDescription());
					storePlace(newPlace);
					//hasChanged.set(true);
				}
			} else
				error("Could not create place here!");
			restoreMouse();
		}

		private void restoreMouse() {
			display.setOnMouseClicked(null);
			display.setCursor(Cursor.DEFAULT);
		}

		private void error(String text) {
			new Alert(AlertType.ERROR, text).showAndWait();
			display.setOnMouseClicked(null);
			display.setCursor(Cursor.DEFAULT);
		}
        
        
        
        
    }

                // DIVERSE HANDLERS


    class SearchHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            SearchButtonAction();
        }
    }

    public void SearchButtonAction() {
        System.out.println("Search button clicked");
        // avmarkera platser som evt är markerade
        // hämtar det som är i sökfältet
        // jämför den textsträngen med nyckeln i den datastrukturen som innehåller alla platser (kategorier spelar ingen roll här)
        // om den hittar en eller flera matches, ska de göras:
        // först synliga
        // sedan markerade
    }

    class HideHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            hideButtonAction();
        }
    }

    private void hideButtonAction() {

    }


    class RemoveHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            RemoveButtonAction();

        }
    }

    public void RemoveButtonAction() {
        System.out.println("Remove button clicked");
        // tar alla platser i datastrukturen markedPlaces -- den innehåller markerade platser
        // tar bort dem från samtliga datastrukturer

    }

    class HideCategoryButtonHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            HideCategoryButtonAction();
        }
    }

    public void HideCategoryButtonAction() {
        System.out.println("Hide category button clicked");
        // kollar vilken kategori som är vald, vilket ger en sträng
        // en if-sats som kollar strängen, som itererar genom arraylisten för den specifika kategorin
        // gör dem osynliga, genom att setVisible = false
    }

//	class ShowCategoryHandler implements EventHandler<MouseEvent> {
//		public void handle(ActionEvent event) {
//			ShowCategoryAction();
//		}
//	}
//
//	public void ShowCategoryAction() {
//		System.out.println("Hide category button clicked");
//		// kollar vilken kategori som är vald, vilket ger en sträng
//		// en if-sats som kollar strängen, som itererar genom arraylisten för den
//		// specifika kategorin
//		// gör dem osynliga, genom att setVisible = true
//
//	}

    public static void main(String[] args) {
        launch(args);
    }
}