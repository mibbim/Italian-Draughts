package dssc.exam.draughts;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestIfGame {

    void setFakeStdInput(String fakeInput) {
        ByteArrayInputStream fakeStandardInput = new ByteArrayInputStream(fakeInput.getBytes());
        System.setIn(fakeStandardInput);
    }

    @Test
    void testChangePlayer() {
        Game game = new Game();
        assertEquals(game.whitePlayer, game.currentPlayer);
        game.changePlayer();
        assertEquals(game.blackPlayer, game.currentPlayer);
        game.changePlayer();
        assertEquals(game.whitePlayer, game.currentPlayer);
    }

    @Test
    void testTurnBehaviour() throws Exception {
        String fakeInput = "4 3" + System.lineSeparator() + "5 4" + System.lineSeparator();
        setFakeStdInput(fakeInput);

        Game game = new Game();
        Board board = new Board();
        game.loadGame(board, 0);

        assertTrue(board.getTile(2, 3).isNotEmpty());
        assertTrue(board.getTile(3, 4).isEmpty());
        assertEquals(Color.WHITE, game.getCurrentPlayer().getColor());
        assertEquals(0, game.getRound());

        game.playRound();

        assertTrue(board.getTile(2, 3).isEmpty());
        assertTrue(board.getTile(3, 4).isNotEmpty());
        assertEquals(Color.BLACK, game.getCurrentPlayer().getColor());
        assertEquals(1, game.getRound());
    }

    @Test
    void testInvalidEmptyTileInput() throws Exception {
        String fakeInput = "1 3" + System.lineSeparator() +
                "5 4" + System.lineSeparator() +
                "2 3" + System.lineSeparator() +
                "5 4" + System.lineSeparator();
        setFakeStdInput(fakeInput);

        String expectedOut = "Invalid move: The first Tile you selected contains no Piece";

        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));

        Board board = new Board();
        Game game = new Game();
        game.loadGame(board, 0);
        assertEquals(Color.WHITE, game.currentPlayer.getColor());
        game.playRound();
        String actualOut = fakeStandardOutput.toString();
        String[] actualLines = actualOut.split(System.lineSeparator());

        assertEquals(expectedOut, actualLines[actualLines.length - 3]);
    }

    @Test
    void testInvalidPieceColorInput() throws Exception {
        String fakeInput = "1 6" + System.lineSeparator() +
                "5 4" + System.lineSeparator() +
                "2 3" + System.lineSeparator() +
                "5 4" + System.lineSeparator();
        setFakeStdInput(fakeInput);

        String expectedOut = "Invalid move: The piece you intend to move belongs to your opponent";

        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));

        Board board = new Board();
        Game game = new Game();
        game.loadGame(board, 0);
        assertEquals(Color.WHITE, game.currentPlayer.getColor());
        game.playRound();
        String actualOut = fakeStandardOutput.toString();
        String[] actualLines = actualOut.split(System.lineSeparator());

        assertEquals(expectedOut, actualLines[actualLines.length - 3]);
    }
}
