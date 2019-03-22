package it.franzoni.goosegame.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class GameManager {

    private final Set<String> players;
    private final Map<String, Integer> positions;

    private final int winningPlace = 63;

    private final Dice dice;

    public GameManager(Dice dice) {
        players = new HashSet<>();
        positions = new HashMap<>();

        this.dice = dice;
    }

    public GameManager() {
        this(new Dice());
    }

    public String addPlayer(String player) {
        players.add(player);
        positions.put(player, 0);

        return printPlayersList();
    }

    public String movePlayer(String player) {
        final int dice1 = dice.roll();
        final int dice2 = dice.roll();

        return this.movePlayer(player, dice1, dice2);
    }

    public String movePlayer(String player, int dice1, int dice2) {
        if (!players.contains(player)) {
            return "Player '" + player + "' unknown!";
        }

        final int startingFrom = positions.get(player);
        
        final int steps = dice1 + dice2;

        final String start = chooseStartString(startingFrom);
        
        final StringBuilder message = new StringBuilder(player).append(" rolls ").append(dice1).append(", ").
                append(dice2).append(". ").append(player).append(" moves from ").append(start).append(" to ");
        
        final int goingTo = this.computeDestination(player, startingFrom, steps, message);

        this.handlePrank(player, startingFrom, goingTo, message);

        positions.put(player, goingTo);

        return message.toString();
    }

    private int computeDestination(String player, int startingFrom, int steps, StringBuilder message) {
        int goingTo = startingFrom + steps;

        while (isOnTheGoose(goingTo)) {
            message.append(goingTo).append(", The Goose. ").append(player).append(" moves again and goes to ");
            goingTo += steps;
        }

        if (isOnTheBridge(goingTo)) {
            message.append("The Bridge. ").append(player).append(" jumps to 12");
            goingTo = 12;
        } else if (endReached(goingTo)) {
            message.append(goingTo);
            message.append(". ").append(player).append(" Wins!!");
        } else if (isPastTheEnd(goingTo)) {
            final int rebound = goingTo - winningPlace;
            goingTo = winningPlace - rebound;
            message.append(winningPlace).append(". ").append(player).append(" bounces! ").append(player).append(" returns to ").append(goingTo);
        } else {
            message.append(goingTo);
        }

        return goingTo;
    }

    private void handlePrank(String player, int startingFrom, int goingTo, StringBuilder message) {
        for (String p : players) {
            if (isPlaceTaken(player, goingTo, p, positions.get(p))) {
                positions.put(p, startingFrom);
                message.append(". On ").append(goingTo).append(" there is ").append(p).append(", who returns to ").append(startingFrom);
                break;
            }
        }
    }

    private boolean isOnTheGoose(int place) {
        return (place == 5 || place == 9 || place == 14
                || place == 18 || place == 23 || place == 27);
    }

    private boolean isOnTheBridge(int place) {
        return place == 6;
    }

    private boolean endReached(int place) {
        return place == winningPlace;
    }

    private boolean isPastTheEnd(int place) {
        return place > winningPlace;
    }

    private String chooseStartString(int place) {
        if (place == 0) {
            return "Start";
        }
        return String.valueOf(place);
    }

    private boolean isPlaceTaken(String player1, int place1, String player2, int place2) {
        return !StringUtils.equals(player1, player2) && place1 == place2;
    }

    private String printPlayersList() {
        StringBuilder sb = new StringBuilder("players: ");
        String[] pArray = players.toArray(new String[players.size()]);
        for (String p : pArray) {
            sb.append(p).append(", ");
        }

        return StringUtils.removeEnd(sb.toString(), ", ");
    }

}
