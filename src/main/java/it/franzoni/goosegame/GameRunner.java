package it.franzoni.goosegame;

import it.franzoni.goosegame.core.GooseGame;
import java.util.Scanner;

public class GameRunner {
    
    public static void main(String[] args) {
        final GooseGame game = new GooseGame();

        System.out.println("Welcome to GooseGame!");
        System.out.println(game.istruzioni);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String input = scanner.nextLine();
            String outcome = game.executeCommand(input);
            System.out.println(outcome);
        }
    }
    
}
