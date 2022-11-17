package hu.petrik.peoplerestclientjavafx;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

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
                loadPeopleFromServer();
            } catch (IOException e) {
                error("Couldn't get data from server", e.getMessage());
                Platform.exit();
            }
        });
    }

    private void loadPeopleFromServer() throws IOException {
        Response response = RequestHandler.get(App.BASE_URL);
        String content = response.getContent();
        Gson converter = new Gson();
        Person[] people = converter.fromJson(content, Person[].class);
        peopleTable.getItems().clear();
        for (Person person : people) {
            peopleTable.getItems().add(person);
        }
    }

    @FXML
    public void insertClick(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("create-people-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            Stage stage = new Stage();
            stage.setTitle("Create People");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            error("Could not load form", e.getMessage());
        }
    }

    @FXML
    public void updateClick(ActionEvent actionEvent) {
    }

    @FXML
    public void deleteClick(ActionEvent actionEvent) {
        int selectedIndex = peopleTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Please select a person from the list first");
            alert.show();
            return;
        }

        Person selected = peopleTable.getSelectionModel().getSelectedItem();
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText(String.format("Are you sure you want to delete %s?", selected.getName()));
        Optional<ButtonType> optionalButtonType = confirmation.showAndWait();
        if (optionalButtonType.isEmpty()) {
            System.err.println("Unknown error occurred");
            return;
        }
        ButtonType clickedButton = optionalButtonType.get();
        if (clickedButton.equals(ButtonType.OK)) {
            String url = App.BASE_URL + "/" + selected.getId();
            try {
                RequestHandler.delete(url);
                loadPeopleFromServer();
            } catch (IOException e) {
                error("An error occured while communicating with the server");
            }
        }
    }

    private void error(String headerText) {
        error(headerText, "");
    }

    private void error(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}