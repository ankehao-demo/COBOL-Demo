package com.cobol.demo.subprogram;

import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println();
        System.out.print("Enter value for #1: ");
        String wsItem1 = scanner.nextLine();
        
        System.out.print("Enter value for #2: ");
        String wsItem2 = scanner.nextLine();
        
        displayMessage(wsItem1, wsItem2);
        
        SubApp subApp = new SubApp();
        
        System.out.println("Calling sub program by content:");
        subApp.callByContent(wsItem1, wsItem2);
        displayMessage(wsItem1, wsItem2);
        
        System.out.println("Second call of sub program should retain WS values.");
        System.out.println("Calling sub program by reference:");
        StringBuilder item1 = new StringBuilder(wsItem1);
        StringBuilder item2 = new StringBuilder(wsItem2);
        subApp.callByReference(item1, item2);
        wsItem1 = item1.toString();
        wsItem2 = item2.toString();
        displayMessage(wsItem1, wsItem2);
        
        System.out.println("Cancelling sub program");
        subApp.cancel();
        System.out.println("Calling sub program. WS values should be reset:");
        item1 = new StringBuilder(wsItem1);
        item2 = new StringBuilder(wsItem2);
        subApp.callByReference(item1, item2);
        wsItem1 = item1.toString();
        wsItem2 = item2.toString();
        displayMessage(wsItem1, wsItem2);
        
        scanner.close();
    }

    private void displayMessage(String item1, String item2) {
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println("Main app: " + item1 + item2);
    }
}
