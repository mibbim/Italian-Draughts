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
        Piece piece = new Piece(colorOfPiece);
        assertEquals(expectedColor, piece.getColor());
    }

    @Test
    void isNotKing(){
        Piece piece = new Piece(Color.WHITE);
        assertFalse(piece.isKing());
    }

    @Test
    void isKing(){
        Piece piece = new Piece(Color.WHITE);
        piece.upgradeToKing();
        assertTrue(piece.isKing());
    }

    private static final String piece =  "[b]" + System.lineSeparator();

    @Test
    void printsToStdOutput() {
        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));
        var fakePiece = new Piece(Color.BLACK);
        System.out.println(fakePiece.display());

        assertEquals(TestIfPiece.piece, fakeStandardOutput.toString());
    }
}
