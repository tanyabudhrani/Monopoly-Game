package mono;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.io.*;

class MonopolyGameTest {

    private Game game;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        List<String> playerNames = Arrays.asList("Player1", "Player2");
        game = new Game(playerNames);
        player1 = game.getCurrentPlayer();
        player2 = game.getNextPlayer();
    }

    @Test
    void testGoSquare() {
        GoSquare goSquare = new GoSquare();
        int initialMoney = player1.getMoney();
        goSquare.landOn(player1, game);
        assertEquals(initialMoney + 1500, player1.getMoney());
    }

    @Test
    void testChanceSquare() {
        ChanceSquare chanceSquare = new ChanceSquare(4);
        int initialMoney = player1.getMoney();
        chanceSquare.landOn(player1, game);
        assertNotEquals(initialMoney, player1.getMoney());
    }

    @Test
    void testIncomeTaxSquare() {
        IncomeTaxSquare taxSquare = new IncomeTaxSquare(6);
        int initialMoney = player1.getMoney();
        taxSquare.landOn(player1, game);
        assertTrue(player1.getMoney() < initialMoney);
    }

    @Test
    void testJailSquare() {
        JailSquare jailSquare = new JailSquare();
        jailSquare.landOn(player1, game);
        assertFalse(player1.isInJail());
    }

    @Test
    void testGoToJailSquare() {
        GoToJailSquare goToJailSquare = new GoToJailSquare(16);
        goToJailSquare.landOn(player1, game);
        assertTrue(player1.isInJail());
        assertEquals(11, player1.getPosition());
    }

    @Test
    void testFreeParkingSquare() {
        FreeParkingSquare freeParkingSquare = new FreeParkingSquare(20);
        int initialMoney = player1.getMoney();
        freeParkingSquare.landOn(player1, game);
        assertEquals(initialMoney, player1.getMoney());
    }

    @Test
    void testPlayerJailOperations() {
        player1.goToJail();
        assertTrue(player1.isInJail());
        
        // Test getting out of jail with doubles
        assertTrue(player1.tryToGetOutOfJail(3, 3));
        assertFalse(player1.isInJail());
        
        player1.goToJail();
        // Test staying in jail without doubles
        assertFalse(player1.tryToGetOutOfJail(2, 3));
        assertTrue(player1.isInJail());
        
        // Test getting out of jail after three turns
        assertFalse(player1.tryToGetOutOfJail(1, 2));
        assertTrue(player1.tryToGetOutOfJail(1, 4));
        assertFalse(player1.isInJail());
    }

    @Test
    void testPlayerPayJailFine() {
        player1.goToJail();
        int initialMoney = player1.getMoney();
        player1.payJailFine();
        assertFalse(player1.isInJail());
        assertEquals(initialMoney - 150, player1.getMoney());
    }


    @Test
    void testGameBoardCustomization() {
        GameBoard board = new GameBoard();
        PropertySquare newProperty = new PropertySquare(5, "New Property", 300, 30);
        board.setSquare(5, newProperty);
        assertEquals(newProperty, board.getSquare(5));
    }


    @Test
    void testFullGameTurn() {
        int initialPosition = player1.getPosition();
        int initialMoney = player1.getMoney();
        
        game.takeTurn(player1);
        
        assertNotEquals(initialPosition, player1.getPosition());
    }

}