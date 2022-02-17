package dssc.exam.draughts;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class TestIfPlayer {


    void setFakeStdInput(String fakeInput) {
        ByteArrayInputStream fakeStandardInput = new ByteArrayInputStream(fakeInput.getBytes());
        System.setIn(fakeStandardInput);
    }

    @ParameterizedTest
    @CsvSource({"3, 4, 2, 3", "12, 15, 11, 14"})
    void testReadPosition(String xInput, String yInput,
                          int rowExpected, int columnExpected) {

        String fakeInput = xInput + " " + yInput + "\n";
        setFakeStdInput(fakeInput);

        Point point = new Player(Color.BLACK).readPosition();
        assertEquals(point.x, columnExpected);
        assertEquals(point.y, rowExpected);
    }

    @ParameterizedTest
    @CsvSource({"3, 4, 5, 6, 2, 3, 4, 5", "12, 15, 14, 36, 11, 14, 13, 35"})
    void testGetMove(String sourceXIn, String sourceYIn,
                     String destinationXIn, String destinationYIn,
                     int sourceColumn, int sourceRow,
                     int destinationColumn, int destinationRow) {

        String fakeInput1 = sourceXIn + " " + sourceYIn + "\n ";
        String fakeInput2 = destinationXIn + " " + destinationYIn + "\n";
        String fakeInput = fakeInput1 + fakeInput2;
        setFakeStdInput(fakeInput);

        Player player = new Player(Color.BLACK);
        Point source = player.getSource();
        Point destination = player.getDestination();

        Point sourcePoint = new Point(sourceRow, sourceColumn);
        Point destinationPoint = new Point(destinationRow, destinationColumn);

        assertEquals(source, sourcePoint);
        assertEquals(destination, destinationPoint);
    }

    @ParameterizedTest
    @CsvSource({"1 a, 1 1", "a 1, 2 3"})
    void nonNumericInputException(String source1, String destination1) {
        String fakeInput = source1 + System.lineSeparator() + destination1 + System.lineSeparator();
        setFakeStdInput(fakeInput);

        Player player = new Player(Color.WHITE);
        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));

        player.readPosition();

        String expected = "What are the coordinates (x, y) of the piece you intend to move? (e.g. 3 4)" +
                System.lineSeparator() +
                "Please enter a valid expression" + System.lineSeparator();

        assertEquals(expected, fakeStandardOutput.toString());
    }

    @ParameterizedTest
    @CsvSource({"Michele, Andres, Davide"})
    void testNameGetter(String Name){
        String fakeInput = Name;
        setFakeStdInput(fakeInput);

        Player player = new Player(Color.BLACK);
        player.initializePlayerName(0);

        assertEquals(Name, player.name);
    }

}
