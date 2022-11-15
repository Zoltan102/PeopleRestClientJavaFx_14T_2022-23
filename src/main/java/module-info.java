module hu.petrik.peoplerestclientjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens hu.petrik.peoplerestclientjavafx to javafx.fxml;
    exports hu.petrik.peoplerestclientjavafx;
}