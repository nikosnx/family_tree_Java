package com.genTree;

import java.io.IOException;
import java.util.InputMismatchException;

public class Main
{
    public static void main(String[] args) {
        try {
            Actions actions = new Actions();
        } catch (InputMismatchException e) {
            System.out.println("Exiting..");
        }
    }
}
