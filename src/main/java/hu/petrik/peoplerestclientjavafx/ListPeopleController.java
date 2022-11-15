package hu.petrik.peoplerestclientjavafx;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class ListPeopleController {

    @FXML
    private Button insertButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<Person> peopleTable;
    @FXML
    private TableColumn<Person, String> nameCol;
    @FXML
    private TableColumn<Person, String> emailCol;
    @FXML
    private TableColumn<Person, Integer> ageCol;

    @FXML
    private void initialize() {
        // getName() függvény eredményét írja ki
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        Platform.runLater(() -> {
            try {
                Response response = RequestHandler.get(App.BASE_URL);
                String content = response.getContent();
                Gson converter = new Gson();
                Person[] people = converter.fromJson(content, Person[].class);
                for (Person person : people) {
                    peopleTable.getItems().add(person);
                }
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("Couldn't get data from server");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                Platform.exit();
            }
        });
    }

    @FXML
    public void insertClick(ActionEvent actionEvent) {
    }

    @FXML
    public void updateClick(ActionEvent actionEvent) {
    }

    @FXML
    public void deleteClick(ActionEvent actionEvent) {
    }
}