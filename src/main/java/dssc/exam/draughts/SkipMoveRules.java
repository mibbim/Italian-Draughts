package dssc.exam.draughts;

public class SkipMoveRules {
    private final Tile sourceTile;
    private final int rowOffset;
    private final int columnOffset;
    private Tile firstDiagonalTile;
    private Tile secondDiagonalTile;
    private boolean skipCheck;

    SkipMoveRules(Tile source, int rowOffset, int columnOffset) {
        sourceTile = source;
        this.rowOffset = rowOffset;
        this.columnOffset = columnOffset;
    }

    public void evaluateIfManCanSkip(Board board, Color manColor) {
        firstDiagonalTile = board.getTileInDiagonalOffset(sourceTile, rowOffset, columnOffset);
        secondDiagonalTile = board.getTileInDiagonalOffset(firstDiagonalTile, rowOffset, columnOffset);
        skipCheck = isSkipValid(manColor) && checkThatIsSkippingAMan();
    }

    public void evaluateIfKingCanSkip(Board board, Color kingColor, Path path) {
        firstDiagonalTile = board.getTileInDiagonalOffset(sourceTile, rowOffset, columnOffset);
        secondDiagonalTile = board.getTileInDiagonalOffset(firstDiagonalTile, rowOffset, columnOffset);
        skipCheck = tileWasNotVisitedYet(path) && isSkipValid(kingColor);
    }

    private boolean isSkipValid(Color movingPieceColor) {
        return isTileInsideTheBoard(secondDiagonalTile) &&
                firstDiagonalTile.isNotEmpty() &&
                firstDiagonalTile.getPiece().getColor() != movingPieceColor &&
                secondDiagonalTile.isEmpty();
    }

    private boolean checkThatIsSkippingAMan() {
        if (firstDiagonalTile.isNotEmpty())
            return !firstDiagonalTile.containsAKing();
        return false;
    }

    private boolean tileWasNotVisitedYet(Path path) {
        return !(path.containsTile(secondDiagonalTile));
    }

    private boolean isTileInsideTheBoard(Tile tile) {
        return tile != null;
    }

    public Tile getFirstTile() {
        return firstDiagonalTile;
    }

    public Tile getSecondTile() {
        return secondDiagonalTile;
    }

    public boolean getSkipCheck() {
        return skipCheck;
    }
}