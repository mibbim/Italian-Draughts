package dssc.exam.draughts;

import java.awt.*;
import java.util.ArrayList;

public class Board {
    public final int size = 64;
    public final int lastIndex = size - 1;
    public final int maxRows = 8;
    public final int maxColumns = 8;
    public final int piecesPerPlayer = 12;
    public ArrayList<Tile> board = new ArrayList<>(size);

    Board() {
        Color firstColor;
        Color secondColor;
        for (int row = 0; row < maxRows; row++) {
            if (row % 2 == 0) {
                firstColor = Color.BLACK;
                secondColor = Color.WHITE;
            } else {
                firstColor = Color.WHITE;
                secondColor = Color.BLACK;
            }
            for (int column = 0; column < maxColumns; column += 2) {
                board.add(new Tile(firstColor));
                board.add(new Tile(secondColor));
            }
        }
        for (int tileIndex = 0; tileIndex < piecesPerPlayer * 2; ++tileIndex) {
            if (board.get(tileIndex).getTileColor() == Color.BLACK) {
                board.get(tileIndex).setPieceContainedInTile(new Piece(tileIndex, Color.BLACK));
                board.get(lastIndex - tileIndex).setPieceContainedInTile(new Piece(lastIndex - tileIndex, Color.WHITE));
            }
        }
    }

    public int getIndex(int row, int column) {
        return 8 * row + column;
    }

    public int getSizeOfBoard() {
        return board.size();
    }

    public int getPiecesOfColor(Color color) {
        int sum = 0;
        for (int tileIndex = 0; tileIndex < getSizeOfBoard(); ++tileIndex) {
            if (board.get(tileIndex).isTileNotEmpty() && board.get(tileIndex).getTilePiece().getColorOfPiece() == color)
                sum += 1;
        }
        return sum;
    }

    public int getTotalNumberOfPieces() {
        int sum = 0;
        for (int tileIndex = 0; tileIndex < getSizeOfBoard(); ++tileIndex) {
            if (board.get(tileIndex).isTileNotEmpty())
                sum += 1;
        }
        return sum;
    }

    public Tile getTile(int index) {
        return board.get(index);
    }

    public Tile getSymmetricTile(int index) {
        return board.get(lastIndex - index);
    }

    public Tile getTile(int row, int column) {
        return board.get(getIndex(row, column));
    }

    public int getMiddlePosition(Point source, Point destination) {
        return getMiddlePosition(getIndex(source.x, source.y), getIndex(destination.x, destination.y));
    }

    public int getMiddlePosition(int startPosition, int endPosition) {
        int distance = Math.abs(startPosition - endPosition);
        if (!isValidPosition(startPosition) || !isValidPosition(endPosition)) {
            return -1;
        }
        if (getTile(startPosition).getTileColor() == Color.WHITE ||
                getTile(endPosition).getTileColor() == Color.WHITE)
            return -1;
        if (distance != 14 && distance != 18) {
            return -1;
        }
        return Math.min(startPosition, endPosition) + distance / 2;
    }

    public boolean isValidPosition(int position) {
        return position >= 0 && position <= lastIndex;
    }

    public boolean isValidPosition(int row, int column) {
        return isValidPosition(getIndex(row, column));
    }

    public void display() {
        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < maxRows; col++) {
                System.out.print(getTile(row, col).display());
            }
            System.out.print("\n");
        }
    }
}
