import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.time.LocalDate;

public class MainController {
  @FXML
  private Label labelDate;

  @FXML
  private Button refresh;

  @FXML
  private Label mainApp;

  @FXML
  private Label application;

  @FXML
  private Label totalTime;


  @FXML
  private Label appName;

  @FXML
  private VBox applist;

  @FXML
  private StackedBarChart barChart;

  @FXML
  public void initialize() {
    LocalDate date = java.time.LocalDate.now();
    labelDate.setText(date.toString());

    appName.setText(appName.getText());
  }
}

