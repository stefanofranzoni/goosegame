package it.franzoni.goosegame.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class GameManagerTest {
    
    //### 1. Add players
    @Test
    public void seGiocatoreNonPresenteLoAggiungo() {
        GameManager gm = new GameManager();
        
        String message = gm.addPlayer("Pippo");
        
        assertThat(message, is("players: Pippo"));
        
        message = gm.addPlayer("Pluto");
        
        assertThat(message, is("players: Pippo, Pluto"));
        
    }
    
    @Test
    public void seGiocatoreGiaPresenteNonLoAggiungo() {
        GameManager gm = new GameManager();
        gm.addPlayer("Pippo");
        String message = gm.addPlayer("Pippo");
        
        assertThat(message, is("players: Pippo"));
    }
    
    //### 2. Move a player
    @Test
    public void laPrimaMossaParteDaStart() {
        GameManager gm = new GameManager();
        gm.addPlayer("Pippo");
        gm.addPlayer("Pluto");
        
        String pippoMove = gm.movePlayer("Pippo",2,1);
        String plutoMove = gm.movePlayer("Pluto",2,2);
        
        assertThat(pippoMove, is("Pippo rolls 2, 1. Pippo moves from Start to 3"));
        assertThat(plutoMove, is("Pluto rolls 2, 2. Pluto moves from Start to 4"));
    }
    
    @Test
    public void leMossePartonoDalPuntoDiArrivoPrecedente() {
        GameManager gm = new GameManager();
        gm.addPlayer("Pippo");
        gm.addPlayer("Pluto");
        
        gm.movePlayer("Pippo",5,2);
        gm.movePlayer("Pluto",2,2);
        
        String pippoMove = gm.movePlayer("Pippo",2,3);
        String plutoMove = gm.movePlayer("Pluto",4,5);
        
        assertThat(pippoMove, is("Pippo rolls 2, 3. Pippo moves from 7 to 12"));
        assertThat(plutoMove, is("Pluto rolls 4, 5. Pluto moves from 4 to 13"));
    }
    
    @Test
    public void nonPossoMuovereUnGiocatoreSconosciuto() {
        GameManager gm = new GameManager();
        
        String pippoMove = gm.movePlayer("Pippo",2,3);
        
        assertThat(pippoMove, is("Player 'Pippo' unknown!"));
    }
    
    //### 3. Win
    @Test
    public void sePlayerRaggiungePosizione63Vince() {
        GameManager gm = new GameManager();
        gm.addPlayer("Pippo");

        gm.movePlayer("Pippo",6,6);
        gm.movePlayer("Pippo",6,6);
        gm.movePlayer("Pippo",6,6);
        gm.movePlayer("Pippo",6,6);
        gm.movePlayer("Pippo",6,6); //player in 60
        
        String msg = gm.movePlayer("Pippo",1,2);
        
        assertThat(msg, is("Pippo rolls 1, 2. Pippo moves from 60 to 63. Pippo Wins!!"));
    }
    
    @Test
    public void sePlayerSuperaPosizione63RimbalzaIndietro() {
        GameManager gm = new GameManager();
        gm.addPlayer("Pippo");

        gm.movePlayer("Pippo",6,6);
        gm.movePlayer("Pippo",6,6);
        gm.movePlayer("Pippo",6,6);
        gm.movePlayer("Pippo",6,6);
        gm.movePlayer("Pippo",6,6); //player in 60
        
        String msg = gm.movePlayer("Pippo",3,2);
        
        assertThat(msg, is("Pippo rolls 3, 2. Pippo moves from 60 to 63. Pippo bounces! Pippo returns to 61"));
    }
    
    //### 4. The game throws the dice
    @Test
    public void ilProgrammaLanciaIDadiPerMe() {
        Dice dado = mock(Dice.class);
        
        //simulo 2 lanci in successione, che restituiscono 3 e 1
        when(dado.roll()).thenAnswer(new Answer<Integer>() {
            private int roll = 0;
            @Override
            public Integer answer(InvocationOnMock iom) { 
                if (roll == 0) {
                    roll++;
                    return 3;
                }
                return 1;
            }
        });
        
        GameManager gm = new GameManager(dado);
        gm.addPlayer("Pippo");
        
        String move = gm.movePlayer("Pippo");
        assertThat(move, is("Pippo rolls 3, 1. Pippo moves from Start to 4"));
        
        verify(dado, times(2)).roll();
    }
    
    //### 5. Space "6" is "The Bridge"
    @Test
    public void sePlayerSuBridgeSaltaFinoA12() {
        GameManager gm = new GameManager();
        gm.addPlayer("Pippo");
        
        gm.movePlayer("Pippo",2,2);
        
        String msg = gm.movePlayer("Pippo",1,1);
        assertThat(msg, is("Pippo rolls 1, 1. Pippo moves from 4 to The Bridge. Pippo jumps to 12"));
    }
    
    //### 6. If you land on "The Goose", move again
    @Test
    public void sePlayerSuGooseAvanzaAncoraDelleStesseMosse() {
        GameManager gm = new GameManager();
        gm.addPlayer("Pippo");
        
        gm.movePlayer("Pippo",2,1);
        
        String msg = gm.movePlayer("Pippo",1,1);
        assertThat(msg, is("Pippo rolls 1, 1. Pippo moves from 3 to 5, The Goose. Pippo moves again and goes to 7"));
    }
    
    @Test
    public void seDoppiaGoosePlayerAvanzaDueVolte() {
        GameManager gm = new GameManager();
        gm.addPlayer("Pippo");
        
        gm.movePlayer("Pippo",6,4);
        
        String msg = gm.movePlayer("Pippo",2,2);
        assertThat(msg, is("Pippo rolls 2, 2. Pippo moves from 10 to 14, The Goose. Pippo moves again and goes to 18, The Goose. Pippo moves again and goes to 22"));
    }
    
    //### 7. Prank
    @Test
    public void sePlayerRaggiungePlayerSiScambianoPosizione() {
        GameManager gm = new GameManager();
        gm.addPlayer("Pippo");
        gm.addPlayer("Pluto");
        
        gm.movePlayer("Pippo",6,6);
        gm.movePlayer("Pippo",2,1);
        
        gm.movePlayer("Pluto",6,6);
        gm.movePlayer("Pluto",3,2);
        
        String pippoMove = gm.movePlayer("Pippo",1,1);
        
        assertThat(pippoMove, is("Pippo rolls 1, 1. Pippo moves from 15 to 17. On 17 there is Pluto, who returns to 15"));
    }
    
    @Test
    public void playerNonScambiaPosizioneConSeStesso() {
        GameManager gm = new GameManager();
        gm.addPlayer("Pippo");
        
        gm.movePlayer("Pippo",6,6);
        gm.movePlayer("Pippo",6,6);
        gm.movePlayer("Pippo",6,6);
        gm.movePlayer("Pippo",6,6);
        gm.movePlayer("Pippo",6,6); //player in 60
        
        String pippoMove = gm.movePlayer("Pippo",4,2); //ritorno a 60
        
        assertThat(pippoMove, is("Pippo rolls 4, 2. Pippo moves from 60 to 63. Pippo bounces! Pippo returns to 60"));
    }
    
}
