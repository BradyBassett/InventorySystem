module c482performanceassessment {
    requires javafx.controls;
    requires javafx.fxml;


    opens c482performanceassessment to javafx.fxml;
    exports c482performanceassessment;
    exports c482performanceassessment.controllers;
    opens c482performanceassessment.controllers to javafx.fxml;
    exports c482performanceassessment.model;
    opens c482performanceassessment.model to javafx.fxml;
}