package it.franzoni.goosegame.core;

import org.apache.commons.lang3.StringUtils;

public class GooseGame {

    public final String istruzioni = "To add a player: add player Player\nTo move a player: move Player\nTo quit the game: press Ctrl-c\n";
    
    private GameManager manager;

    public GooseGame() {
        this(new GameManager());
    }
    
    public GooseGame(GameManager manager) {
        this.manager = manager;
    }

    public String executeCommand(String input) {
        String outcome = this.istruzioni;

        String[] command = input.split(" ");
        if (isAddPlayer(command)) {
            outcome = manager.addPlayer(command[2]);
        } else if (isMovePlayerAuto(command)) {
            outcome = manager.movePlayer(command[1]);
        } else if (isMovePlayer(command)) {
            int dice1 = Integer.valueOf(StringUtils.chop(command[2]));
            int dice2 = Integer.valueOf(command[3]);
            outcome = manager.movePlayer(command[1], dice1, dice2);
        }

        return outcome;
    }
    
    private boolean isAddPlayer(String[] tokens) {
        return tokens.length == 3 && StringUtils.equals(tokens[0], "add") && StringUtils.equals(tokens[1], "player");
    }
    
    private boolean isMovePlayerAuto(String[] tokens) {
        return tokens.length == 2 && StringUtils.equals(tokens[0], "move");
    }
    
    private boolean isMovePlayer(String[] tokens) {
        return tokens.length == 4 && StringUtils.equals(tokens[0], "move");
    }
    
}
