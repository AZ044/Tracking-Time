import com.sun.jna.platform.win32.Netapi32Util;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Native;

import java.time.Duration;
import java.time.LocalDate;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import java.util.Timer;

public class Main {
    public static void main(String[] args) {

        processus();
        }

    public static void processus() {
        String old_window = get_processus();
        String new_window = old_window;

        while (true) {
            if (new_window.contains(" - ")){
                String[] parts = new_window.split(" - ");
                String NomApp = parts[parts.length-1];
                System.out.printf("\nApp: " + NomApp);
            }
            else{
                System.out.printf("\nApp: " + new_window);

            };

            LocalDateTime heure_depart = LocalDateTime.now();
            System.out.printf("\n Depart :"+heure_depart.toString());

            while (new_window.equals(old_window)) {
                old_window = new_window;
                new_window = get_processus();
                // recupere le dernier element si il y a -

                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    System.out.printf("error" + e);
                }
            }
            // Heure d'arrivé
            LocalDateTime heure_fin = LocalDateTime.now();
            System.out.printf("\nFin :"+heure_fin.toString());

            Duration duration = Duration.between(heure_depart,heure_fin);
            System.out.printf("\nTemps :" + duration.toSeconds());
            old_window = get_processus();
            new_window = old_window;
        }

    }

    private static String get_processus() {
        int nMax_Count = 100;
        char[] window_char = new char[nMax_Count];

        HWND hWnd = User32.INSTANCE.GetForegroundWindow();
        // recupere le nmb de caractre et les stock dans lpstring
        int window = User32.INSTANCE.GetWindowText( hWnd, window_char, nMax_Count);
        // convertie lpstring en string
        return String.valueOf(window_char,0,window);
    }


}

