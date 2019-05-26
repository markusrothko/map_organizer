import java.io.*;
import java.util.*;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableSet;
import javafx.scene.paint.Color;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MapSystem extends Application {

    private Stage primaryStage;
    private TextField searchField;
    
    private ImageView image;
    private Image map;
    private Pane mapHolder;    
    private List<Place> places = new ArrayList<>();

    //Hashmap för sökning genom position(x & y)
    private Map<Position, Place> positionList = new HashMap<>();
    // Hashmap för att söka genom namn
    private TreeMap<String, HashSet<Place>> nameList = new TreeMap<>();
 // Hashmap för att söka genom kategori
    private TreeMap<String, HashSet<Place>> searchList = new TreeMap<>();
    //datastuktur för alla markerade platser
    //ArrayList<Place> markedPlaces = new ArrayList<>();

    //RENSA SENARE, omskrivning av ovanstående
    private ObservableSet<Place> markedPlaces = FXCollections.observableSet();

    
    
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
//    private boolean undergroundSelected = false;
//    private boolean busSelected = false;
//    private boolean trainSelected = false;
    private BorderPane root;
    Button newButton = new Button("New");
    //private ClickHandler clickHandler = new ClickHandler();
    private ObservableList<String> categories;
    private ListView<String> cat;
    //private Pane display;

    private SimpleBooleanProperty hasChanged = new SimpleBooleanProperty(false);
    
    
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        root = new BorderPane();
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Map System");
        primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new ExitApplicationHandler());
        primaryStage.show();
        image = new ImageView();
		mapHolder = new Pane();
		
		mapHolder.getChildren().add(image);
        
        root.setCenter(mapHolder);
        //((ScrollPane) root.getCenter()).setPadding(new Insets(10));

        // TOP SECTION

        //  FILE MENU
        //root = new BorderPane();
        VBox vbox = new VBox();
        dropDownMenu = new MenuBar();
        vbox.getChildren().add(dropDownMenu);
        Menu archiveMenu = new Menu("File");
        dropDownMenu.getMenus().add(archiveMenu);
        loadMapItem = new MenuItem("Load Map");
        archiveMenu.getItems().add(loadMapItem);
        loadMapItem.setOnAction(new LoadMapHandler());
        loadPlaces = new MenuItem("Load Places");
        archiveMenu.getItems().add(loadPlaces);
        loadPlaces.setOnAction(new LoadPlacesHandler());
        saveItem = new MenuItem("Save");
        archiveMenu.getItems().add(saveItem);
        saveItem.setOnAction(new SavePlacesHandler());
        exitChoiceItem = new MenuItem("Exit");
        archiveMenu.getItems().add(exitChoiceItem);
        exitChoiceItem.setOnAction(action -> primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST)));


        // TOP INPUT

        HBox hboxTop = new HBox();
        vbox.getChildren().add(hboxTop);
        hboxTop.setPadding(new Insets(10));
        hboxTop.setSpacing(10);
        
        hboxTop.setAlignment(Pos.CENTER);
        // Button newButton = new Button("New");
        //newButton.setOnAction(new newButtonHandler());
        newButton.setOnAction(new NewLocation());
        
        VBox vbs = new VBox();
        vbs.setSpacing(10);
        vbs.setPadding(new Insets (10));
        namedPlace = new RadioButton("Named");
        describedPlace = new RadioButton("Described");
        vbs.getChildren().addAll(namedPlace, describedPlace);
        namedPlace.setToggleGroup(group);
        describedPlace.setToggleGroup(group);
        namedPlace.setSelected((true));
        Button searchButton = new Button("Search");
        searchField = new TextField();
        searchField.setPromptText("Enter search:");
        searchButton.setOnAction(new SearchForPlace());
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
        
        //CENTER SECTION
        
       
        // listan.setPadding(new Insets(5));
        // listan.setPrefSize(100, 400);
//        listan.getSelectionModel().selectedItemProperty().addListener(
//         new ListHandler());

        // RIGHT SECTION

        VBox listan = new VBox();
        listan.setPadding(new Insets(5));
        cat = new ListView<String>();
        categories = FXCollections.observableArrayList("Bus", "Train", "Underground", "None");
        
        listan.getChildren().add(new Label("Categories"));
        listan.getChildren().add(cat);
		cat.setItems(categories);
		
        //cat.getSelectionModel().selectedItemProperty().addListener(new ListHandler());
        listan.setAlignment(Pos.CENTER);
        Button hideCategoryButton = new Button("Hide Category");
        hideCategoryButton.setAlignment(Pos.CENTER);
        listan.getChildren().add(hideCategoryButton);
       // listan.setPrefSize(200, 200);
        
        //OBS ändra storlkep på prefsize
        
        listan.setPrefSize(130, 94);
        
        
        
        hideCategoryButton.setOnAction(new HideCategoryButtonHandler());
        hboxTop.getChildren().addAll(newButton, vbs, searchField, searchButton, hideButton, removeButton, coordinatesButton);
        root.setTop(vbox);
        // root.setCenter(imageView);
        // display.setWrapText(true);
		//display.getChildren().add(map);
        root.setRight(listan);
       

    }
    private String getSelectedCategory() {
		// Just in case nothing is selected
		if (cat.getSelectionModel().getSelectedItem() == null) 
			return "None";
		return cat.getSelectionModel().getSelectedItem();
	}
    
    private void storePlace(Place newPlace) {
		markedPlaces.add(newPlace);
		newPlace.getBool().addListener((obs, old, nevv) -> {
			if (nevv == true)
				markedPlaces.add(newPlace);
			else if (nevv == false) 
				markedPlaces.remove(newPlace);
		});

		nameList.putIfAbsent(newPlace.getName(), new HashSet<Place>());
		nameList.get(newPlace.getName()).add(newPlace);

		searchList.putIfAbsent(newPlace.getCategory(), new HashSet<Place>());
		searchList.get(newPlace.getCategory()).add(newPlace);

		positionList.put(newPlace.getPosi(), newPlace);
		
		//Root kan vara display eller liknande
		root.getChildren().add(newPlace);
	}
/*
                  // EXIT-HANDLER   (vår gamla)

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
*/

// exit-handler, väldigt lik! putsa upp

    class ExitApplicationHandler implements EventHandler<WindowEvent> {
        @Override
        public void handle(WindowEvent event) {
            if (hasChanged.get()) {
                Optional<ButtonType> response = new Alert(AlertType.CONFIRMATION,
                        "There are unsaved changes.\nQuit?")
                        .showAndWait();
                if (response.isPresent() && response.get() == ButtonType.CANCEL || response.get() == ButtonType.CLOSE)
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

//    class ListHandler implements ChangeListener<String> {
//        @Override
//        public void changed(ObservableValue obs, String old, String nev) {
//            switch (nev) {
//                case "Underground":
//                    //gör alla undergroundplatser synliga
//                    System.out.println(nev);
//                    undergroundSelected = true;
//                    trainSelected = false;
//                    busSelected = false;
//                    break;
//                case "Train":
//                    //gör alla trainplatser synliga
//                    System.out.println(nev);
//                    undergroundSelected = false;
//                    trainSelected = true;
//                    busSelected = false;
//                    break;
//                case "Bus":
//                    //gör alla busplatser synliga
//                    System.out.println(nev);
//                    undergroundSelected = false;
//                    trainSelected = false;
//                    busSelected = true;
//                    break;
//                default:
//                    //gör category null
//            }
//
//        }
//    }

            //  NY NEWBUTTONHANDLER

//    class newButtonHandler implements EventHandler<ActionEvent> {
//        @Override
//        public void handle(ActionEvent event) {
//            root.addEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
//            root.setCursor(Cursor.CROSSHAIR);
//            newButton.setDisable(true);
//        }
//    }
//                     // CLICKHANDLER
//
//    class ClickHandler implements EventHandler<MouseEvent> {
//        @Override
//        public void handle(MouseEvent event) {
//            
//            
//            //PostItLapp lapp = new PostItLapp(x, y);
//            NamedPlace p = new NamedPlace(null, null, event.getX(), event.getY());
//            root.getChildren().add(p);
//            root.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
//            root.setCursor(Cursor.DEFAULT);
//            newButton.setDisable(false);
//        }
//    }
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

    //class NewPlace implements EventHandler<MouseEvent> {

        //@Override
       // public void handle(MouseEvent event) {
//            double x = event.getX();
//            double y = event.getY();
            //new NewNamePlace();
           
           // }
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
        
   // }
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

            // LOAD MAP HANDLER

    @SuppressWarnings("unchecked")
    class LoadMapHandler implements EventHandler<ActionEvent> {
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
                map = new Image(new FileInputStream(filnamn));
                image.setImage(map);
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


    // LOAD PLACES HANDLER (direkt kopia)

    class LoadPlacesHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            if (unsaved()) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File("C:\\Prog2Inlupp2"));
                fileChooser.setTitle("Choose places file");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Places files", "*.places"));
                File choosenFile = fileChooser.showOpenDialog(primaryStage);

                if (choosenFile != null) {
                    deleteAll();
                    updateMap();
                    hasChanged.set(false);
                    try {
                        FileReader file = new FileReader(choosenFile);
                        BufferedReader bufferedFile = new BufferedReader(file);
                        String line;
                        try {
                            while ((line = bufferedFile.readLine()) != null)
                                createPlace(line.split(","));
                            unmarkAll();
                            new Alert(AlertType.INFORMATION, "Places loaded.").showAndWait();
                        } catch (Exception e) {
                            new Alert(AlertType.ERROR, "Unknown file format.").showAndWait();
                        }
                        file.close();
                        bufferedFile.close();
                    } catch (IOException e) {
                        new Alert(AlertType.ERROR, "Could not open Place file").showAndWait();
                    }
                }
            }
        }

        // så här ser ett place ut i filen: Named,None,191,218,A mitt i
        // det är alltså: kategori, x coord, y coord, platsnamn
        // arg 0 är named eller described (typ)
        // arg 1 är kategori
        // arg 2 .... nåt fuffens här
// testar att justera
        private void createPlace(String[] arg) {
            if (arg[0].equals("Named"))
                storePlace(new NamedPlace(arg[4], arg[1], Double.parseDouble(arg[2]), Double.parseDouble(arg[3])));
            else if (arg[0].equals("Described"))
                storePlace(new DescribedPlace(arg[4], arg[1], Double.parseDouble(arg[2]), Double.parseDouble(arg[3]),
                        arg[5]));
        }

    }

    // SAVE PLACES HANDLER (direkt kopia)

    class SavePlacesHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("C:\\Prog2Inlupp2"));
            fileChooser.setTitle("Choose places file");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Places files", "*.places"));
            File choosenFile = fileChooser.showSaveDialog(primaryStage);

            if (choosenFile != null) {
                hasChanged.set(false);
                try {
                    FileWriter file = new FileWriter(choosenFile);
                    PrintWriter outBound = new PrintWriter(file);
                    for (Map.Entry<Position, Place> p : positionList.entrySet())
                        outBound.println(p.getValue().toString());
                    file.close();
                    outBound.close();
                    new Alert(AlertType.INFORMATION, "Places saved.").showAndWait();
                } catch (IOException e) {
                    new Alert(AlertType.ERROR, "Place file could not be saved.").showAndWait();
                }
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
        	mapHolder.setCursor(Cursor.CROSSHAIR);
        	if (namedPlace.isSelected()) {
        		mapHolder.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						createNamedPlace(event.getX(), event.getY());
					}
				});
			} else  {
				mapHolder.setOnMouseClicked(new EventHandler<MouseEvent>() {
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
					newP = new NamedPlace(named.getName(), getSelectedCategory(), x, y);
					storePlace(newP);
					hasChanged.set(true);
				}
			} else
				error("Could not create place here!");
			restoreMouse();
		}

		private void createDescribedPlace(double x, double y) {
			if (!positionList.containsKey(new Position(x, y))) {
				DescribedPlaceHandler described = new DescribedPlaceHandler(x, y);
				Optional<ButtonType> result = described.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					newP = new DescribedPlace(described.getName(), getSelectedCategory(), x, y, described.getDescription());
					storePlace(newP);
					hasChanged.set(true);
				}
			} else
				error("Could not create place here!");
			restoreMouse();
		}

		private void restoreMouse() {
			mapHolder.setOnMouseClicked(null);
			mapHolder.setCursor(Cursor.DEFAULT);
		}

		private void error(String text) {
			new Alert(AlertType.ERROR, text).showAndWait();
			mapHolder.setOnMouseClicked(null);
			mapHolder.setCursor(Cursor.DEFAULT);
		}
        
        
        
        
    }

                // DIVERSE HANDLERS

    //      VÅR GAMLA SKISS
//    class SearchHandler implements EventHandler<ActionEvent> {
//        public void handle(ActionEvent event) {
//            SearchButtonAction();
//        }
//    }

//    public void SearchButtonAction() {
//        System.out.println("Search button clicked");
//        // avmarkera platser som evt är markerade
//        // hämtar det som är i sökfältet
//        // jämför den textsträngen med nyckeln i den datastrukturen som innehåller alla platser (kategorier spelar ingen roll här)
//        // om den hittar en eller flera matches, ska de göras:
//        // först synliga
//        // sedan markerade
//    }

    class SearchForPlace implements EventHandler<ActionEvent> {
        HashSet<Place> searchOutput;

        @Override
        public void handle(ActionEvent event) {
            unmarkAll();
            if ((searchOutput = nameList.get(searchField.getText())) != null)
                showResults();
        }

        private void showResults() {
            for (Place p : searchOutput)
                if (p.getName().equals(searchField.getText())) {
                    p.setMarkedProperty(true);
                    p.setVisible(true);
                }
        }
    }

    class HideHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            hideButtonAction();
        }
    }

    private void hideButtonAction() {
        Iterator<Place> iterator = markedPlaces.iterator();

        while (iterator.hasNext()) {
            Place p = iterator.next();
            if (p.isMarked()) {
                p.setVisible(false);
                iterator.remove();
                p.setMarkedProperty(false);
            }
        }
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

    // VÅR GAMLA

//    public void HideCategoryButtonAction() {
//        System.out.println("Hide category button clicked");
//        // kollar vilken kategori som är vald, vilket ger en sträng
//        // en if-sats som kollar strängen, som itererar genom arraylisten för den specifika kategorin
//        // gör dem osynliga, genom att setVisible = false
//    }


        public void HideCategoryButtonAction() {
            for (Place p : searchList.get(getSelectedCategory())) {
                p.setVisible(false);
                p.setMarkedProperty(false);
            }
        }

        //// FIXA!!!!!!

    private void unmarkAll() {
        Iterator<Place> iterator = markedPlaces.iterator();
        while (iterator.hasNext()) {
            Place p = iterator.next();
            if (p.isMarked()) {
                iterator.remove();
                p.setMarkedProperty(false);
            }
        }
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

////// UNSAVED METHOD CHANGE.... TYP IDENTISK, ändra allt

    private boolean unsaved() {
        Optional<ButtonType> response = null;
        if (hasChanged.get())
            response = new Alert(AlertType.CONFIRMATION,
                    "You have unsaved changes.\nThis action will delete your places.").showAndWait();
        if (!hasChanged.get() || response.isPresent() && response.get() == ButtonType.OK)
            return true;
        return false;
    }

    // REMOVE ALL METHOD, direkt kopia pretty much

    private void deleteAll() {

        markedPlaces.clear();
        nameList.clear();
        positionList.clear();
        searchList.clear();
    }


    // DIREKT KOPIA PRETTY MUCH


    private void updateMap() {
        mapHolder.getChildren().clear();
        mapHolder.getChildren().add(image);
        for (Map.Entry<Position, Place> p : positionList.entrySet()) {
            mapHolder.getChildren().add(p.getValue());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}