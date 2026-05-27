import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MainController {

  @FXML private Label labelDate;
  @FXML private Button refresh;
  @FXML private Label mainApp;
  @FXML private Label application;
  @FXML private Label totalTime;

  @FXML private Label appName1, appName2, appName3, appName4, appName5;
  @FXML private Label appTime1, appTime2, appTime3, appTime4, appTime5;
  @FXML private ImageView appIcon1, appIcon2, appIcon3, appIcon4, appIcon5;

  @FXML private StackedBarChart<String, Number> barChart;

  private static final String JSON_PATH = "db/User.json";

  @FXML
  public void initialize() {
    labelDate.setText(LocalDate.now().toString());
    onRefresh();
  }

  @FXML
  public void onRefresh() {
    Label[] names = { appName1, appName2, appName3, appName4, appName5 };
    Label[] times = { appTime1, appTime2, appTime3, appTime4, appTime5 };
    ImageView[] icons = { appIcon1, appIcon2, appIcon3, appIcon4, appIcon5 };

    File file = new File(JSON_PATH);
    if (!file.exists()) {
      resetSlots(names, times, icons);
      totalTime.setText("0h 0m 0s");
      application.setText("0");
      mainApp.setText("-");
      return;
    }

    Map<String, Map<String, Map<String, String>>> data;
    Gson gson = new Gson();
    Type type = new TypeToken<Map<String, Map<String, Map<String, String>>>>() {}.getType();
    try (FileReader reader = new FileReader(file)) {
      data = gson.fromJson(reader, type);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    String today = LocalDate.now().toString();
    Map<String, Map<String, String>> apps = (data == null) ? null : data.get(today);
    if (apps == null || apps.isEmpty()) {
      resetSlots(names, times, icons);
      totalTime.setText("0h 0m 0s");
      application.setText("0");
      mainApp.setText("-");
      return;
    }

    List<Map.Entry<String, Map<String, String>>> sorted = new ArrayList<>(apps.entrySet());
    sorted.sort(Comparator.comparingLong(
        (Map.Entry<String, Map<String, String>> e) -> parseSeconds(e.getValue().get("Time"))
    ).reversed());

    long total = 0;
    for (Map<String, String> v : apps.values()) {
      total += parseSeconds(v.get("Time"));
    }

    for (int i = 0; i < 5; i++) {
      if (i < sorted.size()) {
        Map.Entry<String, Map<String, String>> entry = sorted.get(i);
        names[i].setText(entry.getKey());
        times[i].setText(formatDuration(parseSeconds(entry.getValue().get("Time"))));
        String iconPath = entry.getValue().get("IconPath");
        if (iconPath != null && new File(iconPath).exists()) {
          icons[i].setImage(new Image(new File(iconPath).toURI().toString()));
        } else {
          icons[i].setImage(null);
        }
      } else {
        names[i].setText("");
        times[i].setText("");
        icons[i].setImage(null);
      }
    }

    totalTime.setText(formatDuration(total));
    application.setText(String.valueOf(apps.size()));
    mainApp.setText(sorted.get(0).getKey());
  }

  private static void resetSlots(Label[] names, Label[] times, ImageView[] icons) {
    for (int i = 0; i < names.length; i++) {
      names[i].setText("");
      times[i].setText("");
      icons[i].setImage(null);
    }
  }

  private static long parseSeconds(String s) {
    if (s == null) return 0;
    try {
      return Long.parseLong(s.trim());
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  private static String formatDuration(long seconds) {
    long h = seconds / 3600;
    long m = (seconds % 3600) / 60;
    long s = seconds % 60;
    return h + "h " + m + "m" + s + "s";
  }

}
