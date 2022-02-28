package dssc.exam.draughts;

import dssc.exam.draughts.IOInterfaces.OutInterface;
import dssc.exam.draughts.IOInterfaces.OutInterfaceStdout;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TestIfCandidateSkipPathBuilder {

    @Test
    void findsCandidateTilesForSkipMove() {
        var newBoard = new Board();
        new Move(newBoard, new Point(5, 4), new Point(3, 2)).movePiece();
        assertEquals(2, CandidateSkipPathBuilder.build(newBoard, Color.WHITE).size());
        assertEquals(0, CandidateSkipPathBuilder.build(newBoard, Color.BLACK).size());
    }

    @Test
    void doesntFindAnyCandidateTileForSkipMoveAtBeginning() {
        var newBoard = new Board();
        assertEquals(0, CandidateSkipPathBuilder.build(newBoard, Color.WHITE).size());
        assertEquals(0, CandidateSkipPathBuilder.build(newBoard, Color.BLACK).size());
    }

    @Test
    void findsAPathWithMoreThanOneSkip() {
        var newBoard = new Board();
        new Move(newBoard, new Point(5, 4), new Point(3, 2)).movePiece();
        new Move(newBoard, new Point(6, 1), new Point(5, 4)).movePiece();
        new Move(newBoard, new Point(6, 5), new Point(3, 6)).movePiece();
        assertEquals(2, Collections.max((CandidateSkipPathBuilder.build(newBoard, Color.WHITE).values().stream()
                .map(Path::getNumberOfSkips).collect(Collectors.toList()))));
    }

//    potrebbe essere rimosso
    @Test
    void checksSkipsForKingMove() {
        var newBoard = new Board();
        newBoard.getPieceAtTile(2, 1).upgradeToKing();
        new Move(newBoard, new Point(5, 4), new Point(3, 2)).movePiece();
        new Move(newBoard, new Point(6, 1), new Point(5, 4)).movePiece();
        new Move(newBoard, new Point(6, 5), new Point(3, 6)).movePiece();
        assertEquals(60, Collections.max((CandidateSkipPathBuilder.build(newBoard, Color.WHITE).values().stream()
                                                                                        .map(Path::getWeight)
                                                                                        .collect(Collectors.toList()))));
    }

    @Test
    void stopsMoveAfterThreeCompletedSkips() {
        var newBoard = new Board();
        newBoard.getPieceAtTile(2, 1).upgradeToKing();
        new Move(newBoard, new Point(5, 4), new Point(3, 2)).movePiece();
        new Move(newBoard, new Point(6, 1), new Point(5, 4)).movePiece();
        new Move(newBoard, new Point(6, 5), new Point(3, 6)).movePiece();
        new Move(newBoard, new Point(2, 5), new Point(3, 4)).movePiece();
        assertEquals(3, Collections.max(CandidateSkipPathBuilder.build(newBoard, Color.WHITE).values().stream()
                                                                                        .map(Path::getNumberOfSkips)
                                                                                        .collect(Collectors.toList())));
    }

    @Test
    void doesNotAllowAManToSkipAKing() {
        var newBoard = new Board();
        new Move(newBoard, new Point(6, 5), new Point(3, 2)).movePiece();
        newBoard.getPieceAtTile(5, 4).upgradeToKing();
        var candidateTilesToStartASkip = CandidateSkipPathBuilder.build(newBoard, Color.WHITE);
        var pathOfTileThatCannotSkipAKing = candidateTilesToStartASkip.get(newBoard.getTile(2, 1));
        assertEquals(1, pathOfTileThatCannotSkipAKing.getNumberOfSkips());
        assertEquals(10, pathOfTileThatCannotSkipAKing.getWeight());
    }

    @Test
    void givesHigherScoreToPathWithMostKingsToEat() {
        CustomizableBoard board = new CustomizableBoard()
                .popPiecesAt(Arrays.asList(46, 49, 53))
                .setMultipleManAt(Arrays.asList(26, 28, 32, 40), Color.BLACK)
                .upgradeToKing(Arrays.asList(17, 21, 26, 44));
        var pathValues = CandidateSkipPathBuilder.build(board, Color.WHITE).values();
        assertEquals(45, Collections.max((pathValues.stream()
                                                            .map(Path::getWeight)
                                                            .collect(Collectors.toList()))));
        assertEquals(board.getTile(2, 1), pathValues.stream()
                                                            .filter(path -> path.getWeight() == 45)
                                                            .map(Path::getSource)
                                                            .collect(Collectors.toList())
                                                            .get(0));
    }

    @Test
    void givesHigherScoreToPathWithFirstOccurrenceOfAKing() {
        var newBoard = new Board();
        newBoard.getPieceAtTile(2, 1).upgradeToKing();
        newBoard.getPieceAtTile(2, 3).upgradeToKing();
        new Move(newBoard, new Point(5, 6), new Point(3, 4)).movePiece();
        new Move(newBoard, new Point(6, 1), new Point(3, 2)).movePiece();
        new Move(newBoard, new Point(6, 3), new Point(4, 7)).movePiece();
        new Move(newBoard, new Point(5, 0), new Point(4, 1)).movePiece();
        newBoard.getPieceAtTile(3, 2).upgradeToKing();
        newBoard.getPieceAtTile(5, 4).upgradeToKing();
        var pathValues = CandidateSkipPathBuilder.build(newBoard, Color.WHITE).values();
        assertEquals(38, Collections.max((pathValues.stream()
                                                            .map(Path::getWeight)
                                                            .collect(Collectors.toList()))));
        assertEquals(newBoard.getTile(2, 1), pathValues.stream()
                                                            .filter(path -> path.getWeight() == 38)
                                                            .map(Path::getSource)
                                                            .collect(Collectors.toList())
                                                            .get(0));
    }

}