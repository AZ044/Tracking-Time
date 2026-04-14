import com.sun.jna.platform.win32.Netapi32Util;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Native;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");
        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }
        usr32();
        }
    public static void usr32() {
        try {
            Thread.sleep(3000);
        }
        catch (InterruptedException e){
            System.out.println("interrupted");
        }

        int nMax_Count = 100;
        char[] lpString = new char[nMax_Count] ;
        // Recuperer l'adresse mémoire du processus (fenetre) focus
        HWND hWnd = User32.INSTANCE.GetForegroundWindow();
        // recupere le nmb de caractre et les stock dans lpstring
       int window = User32.INSTANCE.GetWindowText( hWnd, lpString, nMax_Count);
       // convertie lpstring en string
        String lpStrings = String.valueOf(lpString,0,window);
        System.out.printf("App: " + lpStrings);

        if (lpStrings.contains(" - ")){
            String[] parts = lpStrings.split(" - ");
            String NomApp = parts[parts.length-1];
            System.out.printf("App: " + NomApp);
        };
    }

}

