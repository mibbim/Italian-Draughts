package dssc.exam.draughts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class TestIfBoard {
    Board board = new Board();

    @Test
    void hasSizeOf64Tiles() {
        assertEquals(64, board.getSizeOfBoard());
    }

    @Test
    void has24Pieces() {
        assertEquals(24, board.getTotalNumberOfPieces());
    }

    @ParameterizedTest
    @CsvSource({"BLACK, 12", "WHITE, 12"})
    void has12PiecesOfColor(Color color, int number) {
        assertEquals(number, board.getPiecesOfColor(color));
    }

    @ParameterizedTest
    @CsvSource({"BLACK, 16", "BLACK, 18", "BLACK, 20", "BLACK, 22",
            "BLACK, 9", "BLACK, 11", "BLACK, 13", "BLACK, 15",
            "BLACK, 0", "BLACK, 2", "BLACK, 4", "BLACK, 6"})
    void has12BlackPiecesInFirstThreeRows(Color color, int position) {
        assertEquals(board.getTile(position).getPiece().getColor(), color);
    }

    @ParameterizedTest
    @CsvSource({"WHITE, 57", "WHITE, 59", "WHITE, 61", "WHITE, 63",
            "WHITE, 48", "WHITE, 50", "WHITE, 52", "WHITE, 54",
            "WHITE, 41", "WHITE, 43", "WHITE, 45", "WHITE, 47"})
    void has12WhitePiecesInLastThreeRows(Color color, int position) {
        assertEquals(board.getTile(position).getPiece().getColor(), color);
    }

    @ParameterizedTest
    @ValueSource(ints = {24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39})
    void hasEmptyTilesInTwoMiddleRows(int position) {
        assertTrue(board.getTile(position).isTileEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {24, 25, 26, 27, 28, 29, 30, 31})
    void isColorSymmetricInTwoMiddleRows(int position) {
        assertSame(board.getTile(position).getTileColor(), board.getSymmetricTile(position).getTileColor());
    }

    @Test
    void printBoard() {
        String expected = """
                   1  2  3  4  5  6  7  8
                1 [b][ ][b][ ][B][ ][b][ ] 1
                2 [ ][b][ ][b][ ][b][ ][b] 2
                3 [b][ ][b][ ][b][ ][b][ ] 3
                4 [ ][ ][ ][ ][ ][ ][ ][ ] 4
                5 [ ][ ][ ][ ][ ][ ][ ][ ] 5
                6 [ ][w][ ][w][ ][w][ ][w] 6
                7 [W][ ][w][ ][w][ ][w][ ] 7
                8 [ ][w][ ][w][ ][w][ ][w] 8
                   1  2  3  4  5  6  7  8
                    """;
        Board board = new Board();
        board.getTile(4).piece.upgradePieceToKing();
        board.getTile(6 * 8).piece.upgradePieceToKing();

        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));

        board.display();
        assertEquals(expected, fakeStandardOutput.toString());
    }
}
