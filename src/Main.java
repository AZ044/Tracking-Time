import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;
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

import java.io.*;
import java.util.logging.Handler;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;

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
                System.out.printf("\nApp: " + NomApp + "\n");
            }
            else{
                System.out.printf("\nApp: " + new_window + "\n");

            };

            LocalDateTime heure_depart = LocalDateTime.now();
            System.out.printf("\n Depart : "+heure_depart.toString());

            while (new_window.equals(old_window)) {
                old_window = new_window;
                new_window = get_processus();
                // recupere le dernier element si il y a -

                try{
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    System.out.printf("error" + e);
                }
            }
            // Heure d'arrivé
            LocalDateTime heure_fin = LocalDateTime.now();
            System.out.printf("\n Fin : "+heure_fin.toString());

            Duration duration = Duration.between(heure_depart,heure_fin);
            System.out.printf("\n Temps : " + duration.toSeconds() + " Second\n");
            old_window = get_processus();
            new_window = old_window;

            System.out.printf("\n path : "+ get_path() + "\n");
            geticon();

            System.out.printf("\n----------------------\n");

        }

    }

    private static String get_processus() {
        int nMax_Count = 100;
        char[] window_char = new char[nMax_Count];
        // recupere l'adresse memoire
        HWND hWnd = User32.INSTANCE.GetForegroundWindow();
        // recupere le nmb de caractre et les stock dans lpstring
        int window = User32.INSTANCE.GetWindowText( hWnd, window_char, nMax_Count);


        // convertie lpstring en string
        return String.valueOf(window_char,0,window);
    }


    private static  String get_path() {
       HWND hwnd = User32.INSTANCE.GetForegroundWindow();

       //recupere le pid du processus
        IntByReference pid = new IntByReference();
        User32.INSTANCE.GetWindowThreadProcessId(hwnd,pid);

        //ouvre le processus
        WinNT.HANDLE hProcess = Kernel32.INSTANCE.OpenProcess(0x0400 | 0x0010, false,pid.getValue());

        // obtenir le chemin complet
        char [] path = new char[1024];
        IntByReference size = new IntByReference(1024);
        Kernel32.INSTANCE.QueryFullProcessImageName(hProcess,0,path,size);
        Kernel32.INSTANCE.CloseHandle(hProcess);

        return Native.toString(path);
    }
    public static void  geticon (){
        Icon icon = FileSystemView.getFileSystemView()
                .getSystemIcon(new File(get_path()));
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel(icon));
        frame.pack();
        frame.setVisible(true);
    }

}

