package dssc.exam.draughts;

import dssc.exam.draughts.exceptions.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MoveRules {

    public static void throwExceptionIfPositionsAreInvalid(Board board, Point source, Point destination) throws DraughtsException {
        if (!(board.isPositionInsideTheBoard(source) || board.isPositionInsideTheBoard(destination)))
            throw new IndexException("Position is not valid! Index must be between 1 and 8 for each axis!");
        isBlackTile(board, source);
        isBlackTile(board, destination);
        isMovingInDiagonal(source, destination);
        isMoveByOneOrTwoTiles(source, destination);
        MoveRules.throwExceptionIfTileIsEmpty(board, source);
        MoveRules.throwExceptionIfTileIsNonEmpty(board, destination);
        isCorrectDirection(board, source, destination);
    }

    private static void isBlackTile(Board board, Point position) throws TileException {
        if (board.getTile(position).isWhite())
            throw new TileException("Cannot play on white tiles, only black ones, please change position!");
    }

    private static void isCorrectDirection(Board board, Point source, Point destination) throws MoveException {
        if (board.getPieceAtTile(source.x, source.y).isKing())
            return;

        var movingPieceColor = board.getColorOfPieceAtTile(source);
        var direction = destination.x - source.x;
        if (movingPieceColor.associatedDirection() * direction < 0)
            throw new MoveException("You are moving in the opposite rowOffset!");
    }

    private static void isMovingInDiagonal(Point source, Point destination) throws MoveException {
        if (Math.abs(destination.x - source.x) != Math.abs(destination.y - source.y))
            throw new MoveException("Checker can only move diagonally!");
    }

    private static void isMoveByOneOrTwoTiles(Point source, Point destination) throws MoveException {
        var distance = Math.abs(destination.x - source.x);
        if (distance != 1 && distance != 2)
            throw new MoveException(("Checker can move only by one or two tiles!"));
    }

    private static void throwExceptionIfTileIsNonEmpty(Board board, Point destination) throws TileException {
        Tile destinationTile = board.getTile(destination);
        if (destinationTile.isNotEmpty())
            throw new TileException("Cannot move since tile at (" + (destinationTile.getColumn() + 1)
                    + "," + (destinationTile.getRow() + 1) + ") is not empty");
    }

    private static void throwExceptionIfTileIsEmpty(Board board, Point source) throws TileException {
        Tile sourceTile = board.getTile(source);

        if (sourceTile.isEmpty())
            throw new TileException("Cannot move since tile at (" + (sourceTile.getColumn() + 1)
                    + "," + (sourceTile.getRow() + 1) + ") is empty");
    }

    static HashMap<Tile, Path> candidatePathsForSkipMove(Board board, Color movingPieceColor) {
        ArrayList<Tile> tilesContainingPieceOfSameColor = board.getTilesContainingPieceOfColor(movingPieceColor);
        HashMap<Tile, Path> tilesToStartSkippingFrom = new HashMap<>();
        for (Tile tile : tilesContainingPieceOfSameColor) {
            var skipPath = new Path(tile);
            buildPath(board, tile, skipPath);
            if (skipPath.getWeight() > 0)
                tilesToStartSkippingFrom.put(tile, skipPath);
        }
        return tilesToStartSkippingFrom;
    }

    private static void buildPath(Board board, Tile currentTile, Path path) {
        path.addTile(currentTile);
        if (path.getNumberOfSkips() < 3) {
            var sourcePieceColor = path.getPieceContainedInSource().getColor();
            var movingDirection = sourcePieceColor.associatedDirection();

            ArrayList<SkipMoveRules> candidateSkipMoves = getListOfSameDirectionSkipMove(currentTile, movingDirection, board);
            if (path.startsFromKing()) {
                candidateSkipMoves.addAll(getListOfSameDirectionSkipMove(currentTile, -movingDirection, board));
            }
            candidateSkipMoves.forEach(move -> move.evaluateIfCanSkip(path));
            extendPathIfPossible(board, path, candidateSkipMoves);
        }
    }

    private static ArrayList<SkipMoveRules> getListOfSameDirectionSkipMove(Tile currentTile, int Direction, Board board) {
        var rightMove = new SkipMoveRules(currentTile, new Point(Direction, 1), board);
        var leftMove = new SkipMoveRules(currentTile, new Point(Direction, -1), board);
        return new ArrayList<>(Arrays.asList(rightMove, leftMove));

    }

    private static void extendPathIfPossible(Board board, Path path,
                                             List<SkipMoveRules> candidateSkipMoves) {
        var candidatesPaths = new ArrayList<Path>();
        boolean atLeastOneMoveIsPossible = false;
        for (SkipMoveRules move : candidateSkipMoves) {
            if (move.getSkipCheck()) {
                continueToBuildPath(board, path, move, candidatesPaths);
                atLeastOneMoveIsPossible = true;
            }
        }
        if (atLeastOneMoveIsPossible) {
            updateBestPath(path, candidatesPaths);
        }
    }

    private static void continueToBuildPath(Board board, Path path, SkipMoveRules diagonalMove, ArrayList<Path> candidatesPaths) {
        var nextPath = Path.copy(path);
        int newWeight;
        if (path.startsFromKing()) {
            newWeight = getCurrentWeight(path.getNumberOfSkips(), diagonalMove.getFirstTile().containsAKing());
        } else {
            newWeight = 10 * (path.getNumberOfSkips() + 1);
        }
        nextPath.setWeight(newWeight);
        buildPath(board, diagonalMove.getSecondTile(), nextPath);
        candidatesPaths.add(nextPath);
    }

    private static void updateBestPath(Path currentPath, ArrayList<Path> candidatesPaths) {
        Path bestPath = currentPath;
        for (var candidatePath : candidatesPaths) {
            if (candidatePath.getWeight() > bestPath.getWeight())
                bestPath = candidatePath;
        }
        currentPath.setPath(bestPath.getPath());
        currentPath.setWeight(currentPath.getWeight() + bestPath.getWeight());
    }

    private static int getCurrentWeight(int skips, boolean skippedAKing) {
        int currentWeight = 10 * (skips + 1);
        if (skippedAKing)
            currentWeight += 5 + (3 - skips);
        return currentWeight;
    }
}