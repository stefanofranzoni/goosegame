package it.franzoni.goosegame.core;

import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

public class GooseGameTest {
    
    @Test
    public void seComandoRiconosciutoLoEseguo() {
        String input = "add player Pippo";
        
        GooseGame game = new GooseGame();
        String result = game.executeCommand(input);
        
        assertThat(result, is("players: Pippo"));
        
        input = "move Pippo";
        result = game.executeCommand(input);
        
        assertTrue(result.startsWith("Pippo rolls"));
        
        input = "move Pippo 4, 2";
        result = game.executeCommand(input);
        
        assertTrue(result.startsWith("Pippo rolls 4, 2. Pippo moves from"));
    }
    
    @Test
    public void seComandoNonRiconosciutoRitornoIstruzioni() {
        String input = "remove Pippo";
        
        GooseGame game = new GooseGame();
        String result = game.executeCommand(input);
        
        assertThat(result, is(game.istruzioni));
    }
    
    @Test
    public void seMancaArgomentoRitornoIstruzioni() {
        String input = "add";
        
        GooseGame game = new GooseGame();
        String result = game.executeCommand(input);
        
        assertThat(result, is(game.istruzioni));
        
        input = "move";
        result = game.executeCommand(input);
        
        assertThat(result, is(game.istruzioni));
    }
    
    @Test
    public void seCiSonoTroppiArgomentiRitornoIstruzioni() {
        String input = "add player pippo pluto";
        
        GooseGame game = new GooseGame();
        String result = game.executeCommand(input);
        
        assertThat(result, is(game.istruzioni));
        
        input = "move pippo pluto";
        result = game.executeCommand(input);
        
        assertThat(result, is(game.istruzioni));
    }
}
