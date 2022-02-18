package dssc.exam.draughts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class TestIfPiece {

    @ParameterizedTest
    @CsvSource({"BLACK, BLACK", "WHITE, WHITE"})
    void isOfColor(Color colorOfPiece, Color expectedColor){
        Piece piece = new Piece(1, colorOfPiece);
        assertEquals(expectedColor, piece.getColor());
    }

    @Test
    void isNotKing(){
        Piece piece = new Piece(1, Color.WHITE);
        assertFalse(piece.isKing());
    }

    @Test
    void isKing(){
        Piece piece = new Piece(1, Color.WHITE);
        piece.upgradeToKing();
        assertTrue(piece.isKing());
    }

    private static final String piece =  "Piece{" +
            "id=" + 1 +
            ", color=" + "BLACK" +
            ", isKing=" + false +
            "}" + System.lineSeparator();

    @Test
    void printsToStdOutput() {
        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));
        var fakePiece = new Piece(1, Color.BLACK);
        System.out.println(fakePiece);

        assertEquals(TestIfPiece.piece, fakeStandardOutput.toString());
    }
}
