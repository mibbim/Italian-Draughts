package dssc.exam.draughts;

import dssc.exam.draughts.IOInterfaces.ScannerPlayerInput;
import net.jqwik.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.util.InputMismatchException;

import static org.junit.jupiter.api.Assertions.*;

public class TestIfScannerPlayerInput {

    void setFakeStdInput(String fakeInput) {
        ByteArrayInputStream fakeStandardInput = new ByteArrayInputStream(fakeInput.getBytes());
        System.setIn(fakeStandardInput);
    }

    @Provide
    Arbitrary<Integer> integerGenerator() {
        return Arbitraries.integers().between(0, 100);
    }

    @Property
    void testGetInt(@ForAll("integerGenerator") Integer input) {
        setFakeStdInput(input.toString());
        ScannerPlayerInput inputInterface = new ScannerPlayerInput();
        assertSame(input, inputInterface.getInt());
    }

    @ParameterizedTest
    @CsvSource({"Michele", "Andres", "Davide", "Alberto", "1", "some"})
    void testGetString(String string) {
        setFakeStdInput(string);
        ScannerPlayerInput inputInterface = new ScannerPlayerInput();
        assertEquals(string, inputInterface.getString());
    }

    @ParameterizedTest
    @CsvSource({"Michele", "a", "b", "@", "some"})
    void testInputMismatchException(String string){
        setFakeStdInput(string);
        ScannerPlayerInput inputInterface = new ScannerPlayerInput();
        assertThrows(InputMismatchException.class, inputInterface::getInt);
    }
}
