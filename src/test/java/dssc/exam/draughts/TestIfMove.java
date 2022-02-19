package dssc.exam.draughts;

import dssc.exam.draughts.exceptions.*;
import org.junit.jupiter.api.Test;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestIfMove {

    @Test
    void canMoveDiagonallyToEmptySpace() throws Exception {
        var board = new Board();
        assertTrue(board.getTile(3, 2).isEmpty());
        assertTrue(board.getTile(2, 1).isNotEmpty());
        try {
            Move.diagonalMove(board, new Point(2, 1), new Point(3, 2));
            assertTrue(board.getTile(2, 1).isEmpty());
            assertTrue(board.getTile(3, 2).isNotEmpty());
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    void doesntMoveDiagonallyIfTileIsOccupied() throws Exception {
        var board = new Board();
        assertTrue(board.getTile(1, 2).isNotEmpty());
        assertTrue(board.getTile(2, 1).isNotEmpty());
        Exception exception = assertThrows(Exception.class, () -> Move.diagonalMove(board, new Point(1, 2), new Point(2, 1)));
        assertEquals("Cannot move since tile at (2,3) is not empty", exception.getMessage());
    }

    @Test
    void canSkipMove() throws Exception {
        var board = new Board();
        assertTrue(board.getTile(2, 1).isNotEmpty());
        Move.movePiece(board, new Point(5, 2), new Point(3, 2));
        assertTrue(board.getTile(3, 2).isNotEmpty());
        assertTrue(board.getTile(5, 2).isEmpty());
        try {
            Move.skipMove(board, new Point(2, 1), new Point(4, 3));
            assertTrue(board.getTile(2, 1).isEmpty());
            assertTrue(board.getTile(3, 2).isEmpty());
            assertTrue(board.getTile(4, 3).isNotEmpty());
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    void doesASimpleMove() throws Exception{
        var newBoard = new Board();
        Move.moveDecider(newBoard, new Point(2, 1), new Point(3, 2));
        assertEquals(17, newBoard.getPieceAtTile(3,2).getId());
    }

    @Test
    void doesASkipMove() throws Exception{
        var newBoard = new Board();
        Move.movePiece(newBoard, new Point(5, 4), new Point(3, 2));
        Move.moveDecider(newBoard, new Point(2, 1), new Point(4, 3));
        assertTrue(newBoard.getTile(new Point(3,2)).isEmpty());
        assertEquals(17, newBoard.getPieceAtTile(4,3).getId());
    }

    @Test
    void suggestsOptimalMove() throws Exception {
        var newBoard = new Board();
        newBoard.getPieceAtTile(2, 1).upgradeToKing();
        Move.movePiece(newBoard, new Point(5, 4), new Point(3, 2));
        Move.movePiece(newBoard, new Point(6, 1), new Point(5, 4));
        Move.movePiece(newBoard, new Point(6, 5), new Point(3, 6));
        Exception exception = assertThrows(InvalidMoveException.class, () -> Move.moveDecider(newBoard, new Point(2, 1), new Point(3, 0)));
        assertEquals("There are pieces that must capture, try these positions: (2,3)", exception.getMessage());
    }

    @Test
    void suggestsOptimalMoveEvenIfOriginalIsASkip() throws Exception {
        var newBoard = new Board();
        newBoard.getPieceAtTile(2, 1).upgradeToKing();
        Move.movePiece(newBoard, new Point(5, 4), new Point(3, 2));
        Move.movePiece(newBoard, new Point(6, 1), new Point(5, 4));
        Move.movePiece(newBoard, new Point(6, 5), new Point(3, 6));
        Exception exception = assertThrows(InvalidMoveException.class, () -> Move.moveDecider(newBoard, new Point(2, 3), new Point(4, 1)));
        assertEquals("You can select a better skip! Choose one of the tiles at these positions: (2,3)", exception.getMessage());
    }

    @Test
    void stopsAfterThreeCompletedSkips() throws Exception {
        var newBoard = new Board();
        newBoard.getPieceAtTile(2, 1).upgradeToKing();
        Move.movePiece(newBoard, new Point(5, 4), new Point(3, 2));
        Move.movePiece(newBoard, new Point(6, 1), new Point(5, 4));
        Move.movePiece(newBoard, new Point(6, 5), new Point(3, 6));
        Move.movePiece(newBoard, new Point(2, 5), new Point(3, 4));
        IncompleteMoveException exception = assertThrows(IncompleteMoveException.class, () -> Move.moveDecider(newBoard, new Point(2, 1), new Point(4, 3)));
        assertEquals(60, exception.getWeight());
    }

    @Test
    void skipsWithAKingInsteadOfAMan() throws Exception {
        var newBoard = new Board();
        newBoard.getPieceAtTile(2, 1).upgradeToKing();
        Move.movePiece(newBoard, new Point(5, 4), new Point(3, 2));
        newBoard.display();
        Exception exception = assertThrows(InvalidMoveException.class, () -> Move.moveDecider(newBoard, new Point(2, 3), new Point(4, 1)));
        assertEquals("You should skip with a King instead of a Man! Choose one of these positions: (2,3)", exception.getMessage());
    }
}
