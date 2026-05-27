import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class json {
    public static void main (String[] args) {

    }
    public static int Json(String Times ,String Name ,String icon) {

        //Duration Time = processus();

        String Date = date();
        String iconPath = Main.GetIconAndSave();
        String Path = Main.GetPath();
        String Process_Name = Main.GetProcessus();

        // Arboressance du json
        Map<String, Map<String, Map<String, String>>> m = new HashMap<>();
        File file = new File("db/User.json");

        Map<String,String>details = new HashMap<>();
        Map<String, Map<String, String>>App = new HashMap<>();

        int nb = 0;


        //int time = Integer.parseInt(details.get("Time"));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (file.exists()) {
            try( FileReader reader = new FileReader(file)){
                Type type = new TypeToken<Map<String, Map<String, Map<String, String>>>>(){}.getType();
                Map<String, Map<String, Map<String, String>>> existing = gson.fromJson(reader, type);

                if (existing != null){
                    m.putAll(existing);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (m.containsKey(Date)){
            App = m.get(Date);
        }

        if (App.containsKey(Process_Name)) {
            Map<String, String> existingDetails = App.get(Process_Name);
            int oldTime = Integer.parseInt(existingDetails.get("Time"));
            int newTime = Integer.parseInt(Times);
            details.put("Time", String.valueOf(oldTime + newTime));
        } else {
            details.put("Time", String.valueOf(Times));
        }

        details.put("IconPath",iconPath);
        details.put("Path",Path);
        App.put(Process_Name,details);
        m.put(Date,App);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        }catch (IOException e) {
            e.printStackTrace();
            System.out.print("Erreur lors de la creation du Fichier User.json");
        };
        if (file.exists()) {
            System.out.println(Path);
        }

        try( FileWriter writer = new FileWriter("db/User.json")) {
            writer.write(gson.toJson(m));


        }catch (IOException a){
            a.printStackTrace();
            System.out.print("Impossible D'ecrire dans le json");
        }
        for (Map<String, Map<String, String>> apps : m.values()) {
            nb += apps.size();
        }
        int nb_total = nb;
        return nb_total;
    };

    public static String date() {

        return java.time.LocalDate.now().toString();
    }


}
