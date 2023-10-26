package com.genTree;

import java.io.IOException;
import java.util.Scanner;

public class Actions {
    Scanner sc;
    Relations r;
    String introMsg;
    int input;

    public Actions() {
        do {
            r = new Relations();
            sc = new Scanner(System.in);
            introMsg = "Welcome! \n What do you want to do? \n 1: Read the csv file and load it's data into the system \n 2. Sort family members alphabetically and put their basic data into a text file \n 3. Enter two names and find out their relation \n 4. Save the basic relation data into a dot file \n \t Press any other key to exit";
            System.out.println(introMsg);
            input = sc.nextInt();
            switch(input) {
                case 1:
                    importData();
                    System.out.println("Data loaded Successfully");
                    break;
                case 2:
                    importData();
                    r.writeToTxtSorted();
                    System.out.println("Data loaded Successfully & text file is successfully created");
                    break;
                case 3:
                    importData();
                    r.printRelations(r.getUserInput());
                    break;
                case 4:
                    importData();
                    r.writeToDot();
                    System.out.println("Data loaded Successfully & dot file is successfully created");
                    break;
                default:
                    System.out.println("Exiting.. ");
            }
        } while (input == 1 || input == 2 || input == 3 || input == 4);


    }

    public void importData() {
        try {
            r.ImportDataFromFile("baratheon.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
