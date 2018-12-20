import com.jpro.webapi.JProApplication;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class JProWebUIMain extends JProApplication {

    /* Parameters to add series in right order */
    private TableView tableView = new TableView();
    private ObservableList<TVSeries> TVlist;
    private int selectedID = 0;
    private TMDBController TMDB = new TMDBController();
    private JSONArray SearchArr;
    private ArrayList<TVSeries> searchResult = new ArrayList<TVSeries>();
    private final TVSeriesService service = new TVSeriesService();
    private VBox vbox = new VBox(0);

    // Parameters to add after selection
    private ObservableList<String> options =
            FXCollections.observableArrayList();
    private ComboBox resultComboBox = new ComboBox(options);
    private Button buttonAdd = new Button("Add");
    // Textfields & Labels
    private final TextField searchTextField = new TextField();
    private final TextField seasonTextField = new TextField();
    private final TextField episodeTextField = new TextField();
    private Label note = new Label("Please select an entry");
    private Button confirmEditButton = new Button("Confirm");
    private Button refreshButton = new Button("Refresh");
    private Button showButton = new Button("Edit");
    private Button buttonSearch = new Button("Search");
    private Label episodeLabel = new Label("Please select an entry");
    private Label TVName = new Label();
    private Label seasonLabel = new Label("Season");
    private String lastUpdate = "No information";
    private String nextEpisode = "No information";


    /**
     * Main
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start stage
     *
     * @param stage
     */
    @Override
    public void start(Stage stage) {

        // Initiate
        stage.setTitle("JProWebUI");
        Group root = new Group();
        stage.setScene(new Scene(root));
        stage.setMaximized(true);

        // Set size of vbox as big as stage
        vbox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        vbox.prefHeightProperty().bind(stage.heightProperty().multiply(1.00));
        vbox.prefWidthProperty().bind(stage.widthProperty().multiply(1.00));

        // Set size of tableview  as width as vbox
        tableView.prefWidthProperty().bind(vbox.widthProperty().multiply(0.97));
        tableView.prefHeightProperty().bind(vbox.heightProperty().multiply(0.60));

        // Refresh button
        addButtonRefresh(refreshButton);

        // Edit button
        addShowButton(showButton);

        // ConfirmEditbutton
        addButtonConfirm(confirmEditButton);

        // Setup add TextField
        searchTextField.setPromptText("TV Series");
        searchTextField.setMinWidth(25);

        // Search button
        addButtonSearch(buttonSearch);

        // Add button
        addButtonAdd(buttonAdd);

        /* Add to vbox & set indicators */
        vbox.getChildren().addAll(tableView, refreshButton, searchTextField, buttonSearch, showButton);

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(150, 150);

        // Add colums to tableview
        addColumnsToTableView(tableView);

        // Set bindings
        progressIndicator.progressProperty().bind(service.progressProperty());
        progressIndicator.visibleProperty().bind(service.runningProperty());
        tableView.itemsProperty().bind(service.valueProperty());

        // Add stages
        StackPane stack = new StackPane();
        stack.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

        // Put Vbox in ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.prefHeightProperty().bind(stage.heightProperty().multiply(1.00));
        scrollPane.prefWidthProperty().bind(stage.widthProperty().multiply(1.00));
        scrollPane.setContent(vbox);

        // Add childrens to pane
        stack.getChildren().addAll(scrollPane, progressIndicator);
        root.getChildren().add(stack);
        service.start();
        //Show JavaFX window
        stage.show();
    }

    /**
     * Using microservice
     *
     * @param searchQuery
     * @return
     */
    private JSONArray getJsonArrayFromTmdbMicroservice(String searchQuery) {
        try {
            // TV Objects
            String URL = "http://localhost:8761/";
            URL query = new URL(URL + searchQuery);
            HttpURLConnection connection = (HttpURLConnection) query.openConnection();

            // Connection
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            //Buffered Reader
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            String output;

            while ((output = br.readLine()) != null) {
                // To JSONObjects
                JSONArray obj = new JSONArray(output);
                System.out.println("getJsonArrayFromTmdbMicroservice Objekt:" + obj);
                return obj;
            }
        } catch (Exception e) {
            System.out.println("Error during getJsonArrayFromTmdbMicroservice");
            System.out.println(e);
        }
        return null;
    }

    private void addButtonSearch(Button button) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!searchTextField.getText().trim().isEmpty()) {
                    // Form correct syntax of query
                    String query = searchTextField.getText().replaceAll(" ", "+");

                    // Download JSON with query
                    SearchArr = TMDB.resultJSONArray(query);

                    // Hier MICROSERVICE einfügen<--------------------------------------------------------------------------
                    //JSONArray test = getJsonArrayFromTmdbMicroservice(query);
                    //SearchArr = getJsonArrayFromTmdbMicroservice(query);
                    //System.out.println("TestMicroService:" + test);
                    //

                    // Add results to combobox
                    for (int i = 0; i < SearchArr.length(); i++) {
                        TVSeries currentResult = new TVSeries(SearchArr.getJSONObject(i).getInt("id"),
                                SearchArr.getJSONObject(i).getString("name"));
                        searchResult.add(currentResult);
                        resultComboBox.getItems().add(currentResult.getName());
                    }

                    // Add to select from reults
                    vbox.getChildren().addAll(resultComboBox, buttonAdd);
                    //service.restart();
                }
            }
        });
    }

    private void addShowButton(Button button) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                if (!tableView.getSelectionModel().getSelectedItems().isEmpty()) {
                    TVlist = tableView.getSelectionModel().getSelectedItems();
                    TVName = new Label(TVlist.get(0).getName());
                    seasonTextField.setPromptText("Season");
                    seasonTextField.setMinWidth(25);

                    episodeTextField.setPromptText("Episode");
                    episodeTextField.setMinWidth(25);

                    vbox.getChildren().addAll(TVName, seasonLabel, seasonTextField, episodeLabel, episodeTextField, confirmEditButton);
                    note.setVisible(false);
                } else {
                    vbox.getChildren().addAll(note);
                }

            }
        });
    }

    private void addButtonConfirm(Button button) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                seasonTextField.getText();
                String sqlSeason = "Update seriesTicker.TVSeries set current_season = '" + seasonTextField.getText() + "' where id = '" + TVlist.get(0).getId() + "'";
                String sqlEpisode = "Update seriesTicker.TVSeries set current_episode = '" + episodeTextField.getText() + "' where id = '" + TVlist.get(0).getId() + "'";
                DBConnector.executeSQL(sqlSeason);
                DBConnector.executeSQL(sqlEpisode);
                vbox.getChildren().removeAll(confirmEditButton, episodeTextField, episodeLabel, seasonTextField, seasonLabel, TVName);
                service.restart();
            }
        });
    }

    private void
    addButtonAdd(Button button) {
        /* Listener on add Button (add to database) */
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                // Retrieve selected TV series from combobox
                for (int i = 0; i < searchResult.size(); i++) {
                    if (resultComboBox.getValue().toString().replaceAll("\\*", "").matches(searchResult.get(i).getName().replaceAll("\\*", ""))) {
                        selectedID = searchResult.get(i).getId();
                        break;
                    }
                }

                // Add to database
                String sql = null;

                // Getting all information from JSON
                try {
                    lastUpdate = TMDB.getNextEpAr(selectedID).getString("air_date");
                } catch (Exception e3) {
                    System.out.println("No last Update");
                    System.out.println(e3);
                }
                try {
                    nextEpisode = TMDB.downloadResultJSON(selectedID).getString("air_date");
                } catch (Exception e4) {
                    System.out.println("No episode");
                    System.out.println(e4);
                }


                sql = "INSERT INTO SeriesTicker.TVSeries " + "VALUES (" + selectedID + ", '" + resultComboBox.getValue().toString() + "', 1, 1, '" + lastUpdate + "','" + nextEpisode + "')";
                DBConnector.executeSQL(sql);

                // Reset search
                searchTextField.clear();
                vbox.getChildren().removeAll(resultComboBox, buttonAdd);
                resultComboBox.valueProperty().set(null);
                options.clear();
                searchResult.clear();
                SearchArr = null;
                selectedID = 0;
                lastUpdate = "No information";
                nextEpisode = "No information";
                service.restart();
            }
        });
    }

    private void addButtonRefresh(Button button) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                service.restart();
            }
        });
    }


    private void addColumnsToTableView(TableView tableView) {
        /*  Define table columns: */
        // ID column
        TableColumn IDCol = new TableColumn();
        IDCol.setText("id");
        IDCol.setCellValueFactory(new PropertyValueFactory("id"));
//        IDCol.setPrefWidth(70);
        IDCol.prefWidthProperty().bind(tableView.prefWidthProperty().multiply(0.05));
        tableView.getColumns().add(IDCol);
        // name column
        TableColumn nameCol = new TableColumn();
        nameCol.setText("name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        nameCol.prefWidthProperty().bind(tableView.prefWidthProperty().multiply(0.29));
//        nameCol.setPrefWidth(200);
        tableView.getColumns().add(nameCol);
        // current_season column
        TableColumn current_seasonCol = new TableColumn();
        current_seasonCol.setText("current_season");
        current_seasonCol.setCellValueFactory(new PropertyValueFactory("current_season"));
        current_seasonCol.prefWidthProperty().bind(tableView.prefWidthProperty().multiply(0.13));
//        current_seasonCol.setPrefWidth(100);
        tableView.getColumns().add(current_seasonCol);
        // current_episode column
        TableColumn current_episodeCol = new TableColumn();
        current_episodeCol.setText("current_episode");
        current_episodeCol.setCellValueFactory(new PropertyValueFactory("current_episode"));
        current_episodeCol.prefWidthProperty().bind(tableView.prefWidthProperty().multiply(0.13));
//        current_episodeCol.setPrefWidth(100);
        tableView.getColumns().add(current_episodeCol);
        // nextEpisodeDate column
        TableColumn nextEpisodeDateCol = new TableColumn();
        nextEpisodeDateCol.setText("nextEpisodeDate");
        nextEpisodeDateCol.setCellValueFactory(new PropertyValueFactory("nextEpisodeDate"));
        nextEpisodeDateCol.prefWidthProperty().bind(tableView.prefWidthProperty().multiply(0.20));
//        nextEpisodeDateCol.setPrefWidth(100);
        tableView.getColumns().add(nextEpisodeDateCol);
        // lastUpdate column
        TableColumn lastUpdateCol = new TableColumn();
        lastUpdateCol.setText("lastUpdate");
        lastUpdateCol.setCellValueFactory(new PropertyValueFactory("lastUpdate"));
        lastUpdateCol.prefWidthProperty().bind(tableView.prefWidthProperty().multiply(0.20));
//        lastUpdateCol.setPrefWidth(100);
        tableView.getColumns().add(lastUpdateCol);

    }


}
