package dssc.exam.draughts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestTile {

    Tile emptyTile = new Tile();
    Tile black_tile_black_piece = new Tile(new Piece(1, Color.BLACK), Color.BLACK);

    @Test
    void createNonEmptyTile() {
        assertEquals(black_tile_black_piece.piece.id, 1);
        assertEquals(black_tile_black_piece.piece.pieceColor, Color.BLACK);
    }

    @Test
    void createEmptyTile() {
        assertNull(emptyTile.piece);
    }

    @Test
    void emptinessOfTile() {
        assertTrue(emptyTile.isTileEmpty());
    }

    @Test
    void nonEmptinessOfTile() {
        assertFalse(black_tile_black_piece.isTileEmpty());
    }

    @Test
    void emptyTileDisplay(){
        assertEquals(emptyTile.display(), "[ ]");
    }

}
