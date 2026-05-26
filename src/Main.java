import java.io.IOException;
import java.io.FileWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Native;

import java.awt.image.BufferedImage;
import java.lang.reflect.Type;
import java.time.Duration;

import java.time.LocalDateTime;
import java.awt.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;

public class Main {
    public static void main(String[] args) {

        processus();

    }

    public static void processus() {
        String old_window = GetProcessus();
        String new_window = old_window;

        while (true) {
            if (new_window.contains(" - ")) {
                String[] parts = new_window.split(" - ");
                String NomApp = parts[parts.length - 1];
                System.out.printf("\nApp: " + NomApp + "\n");
            } else {
                System.out.printf("\nApp: " + new_window + "\n");

            };

            String Date = java.time.LocalDate.now().toString();
            System.out.printf("\n Date : "+ Date + "\n");

            LocalDateTime heure_depart = LocalDateTime.now();
            System.out.printf("\n Depart : " + heure_depart.toString());

            while (new_window.equals(old_window)) {
                // recupere le dernier element si il y'en a un
                old_window = new_window;
                new_window = GetProcessus();


                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.printf("error" + e);
                }
            }
            ;
            // Heure d'arrivé
            LocalDateTime heure_fin = LocalDateTime.now();
            System.out.printf("\n Fin : " + heure_fin.toString());

            Duration duration = Duration.between(heure_depart, heure_fin);
            System.out.printf("\n Temps : " + duration.toSeconds() + " Second\n");
            long Times = duration.toSeconds();
            int nbApp = json.Json(String.valueOf(Times),new_window,GetIconAndSave());
            System.out.println("Number of app: "+ nbApp);
            old_window = GetProcessus();
            new_window = old_window;

            System.out.printf("\n path : " + GetPath() + "\n");
            GetIcon();
            GetIconAndSave();
            System.out.printf("\n----------------------\n");

        }
    };



    public static String GetProcessus() {
        int nMax_Count = 100;
        char[] window_char = new char[nMax_Count];
        // recupere l'adresse memoire
        HWND hWnd = User32.INSTANCE.GetForegroundWindow();
        // recupere le nmb de caractre et les stock dans lpstring
        int window = User32.INSTANCE.GetWindowText(hWnd, window_char, nMax_Count);


        // convertie lpstring en string
       String ProcessName =  String.valueOf(window_char, 0, window);

        if (ProcessName.contains(" - ")) {
            String[] parts = ProcessName.split(" - ");
            String NomApp = parts[parts.length - 1];
           return NomApp;
        } else {
            return ProcessName;

        }
    }

    ;

    public static String GetIconAndSave() {
        //  Récupérer l'icône
        String exePath = GetPath();

        // Générer un nom unique basé sur le nom de l'exe
        String exeName = new File(exePath).getName().replace(".exe", "");
        String iconPath = "icons/" + exeName + ".png";
        Icon icon = FileSystemView.getFileSystemView()
                .getSystemIcon(new File(exePath));

        if (new File(iconPath).exists()) {
            return iconPath;
        }
        ;

        // Convertir Icon -> BufferedImage
        BufferedImage bufferedImage = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = bufferedImage.createGraphics();
        icon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();

        // 4. Créer le dossier si nécessaire et sauvegarder
        try {
            new File("icons").mkdirs();
            ImageIO.write(bufferedImage, "PNG", new File(iconPath));
            System.out.println("Icône sauvegardée : " + iconPath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        ;

        return iconPath;
    }

    ;

    static String GetPath() {
        HWND hwnd = User32.INSTANCE.GetForegroundWindow();

        // Recupere l'id du (pid) du processus
        IntByReference pid = new IntByReference();
        User32.INSTANCE.GetWindowThreadProcessId(hwnd, pid);

        // ouvre le processus
        WinNT.HANDLE hProcess = Kernel32.INSTANCE.OpenProcess(0x0400 | 0x0010, false, pid.getValue());

        // Obtient le chemin du processus
        char[] path = new char[1024];
        IntByReference size = new IntByReference(1024);
        Kernel32.INSTANCE.QueryFullProcessImageName(hProcess, 0, path, size);
        Kernel32.INSTANCE.CloseHandle(hProcess);

        return Native.toString(path);
    }

    ;

    public static void GetIcon() {
        Icon icon = FileSystemView.getFileSystemView()
                .getSystemIcon(new File(GetPath()));

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel(icon));
        frame.pack();
        frame.setVisible(true);
    }
};

