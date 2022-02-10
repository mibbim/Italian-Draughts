package dssc.exam.draughts;

import dssc.exam.draughts.exceptions.*;

import java.awt.*;
import java.util.ArrayList;

public class MoveRules {

    public static boolean checkIfPositionsAreValid(Board board, Point source, Point destination) throws Exception{
        try {
            board.isValidPosition(source);
            board.isValidPosition(destination);
            board.isBlackTile(board.getTile(source));
            board.isBlackTile(board.getTile(destination));
            isNotSamePosition(source, destination);
        }
        catch (Exception e) {
            throw e;
        }

        return true;
    }

    static void isDiagonal(Point source, Point destination) throws NotDiagonalMoveException {
        if(Math.abs(destination.x - source.x) != Math.abs(destination.y - source.y)) {
            throw new NotDiagonalMoveException("Checker can only move diagonally!");
        }

    }

    static void isNotSamePosition(Point source, Point destination) throws SamePositionException {
        if (source.x == destination.x && source.y == destination.y) {
            throw new SamePositionException("Source and destination position cannot be the same!");
        }
    }

    static void checkTileNonEmptiness(Point destination, Tile destinationTile) throws NonEmptyTileException {
        if (destinationTile.isTileNotEmpty()) {
            throw new NonEmptyTileException("Cannot move since tile at (" + (destination.y + 1) + "," + (destination.x + 1) + ") is not empty");
        }
    }

    static void checkTileEmptiness(Point source, Tile sourceTile) throws EmptyTileException {
        if (sourceTile.isTileEmpty()) {
            throw new EmptyTileException("Cannot move since tile at (" + (source.y + 1) + "," + (source.x + 1) + ") is empty");
        }
    }

    static ArrayList<Tile> isThereASkipMove(Board board, Color color) {
        var listOfTiles = board.getTilesContainingPieceOfColor(color);
        var listOfCandidatesTiles = new ArrayList<Tile>(12);
        for (Tile tile : listOfTiles) {
            if (checkAdjacentDiagonal(board, tile, color))
                listOfCandidatesTiles.add(tile);
        }
        return listOfCandidatesTiles;
    }

    static boolean checkAdjacentDiagonal(Board board, Tile tile, Color color) {
        if (color == Color.WHITE) {
            if(board.getTile(tile.getTilePosition().x + 1, tile.getTilePosition().y - 1).isTileNotEmpty() ||
               board.getTile(tile.getTilePosition().x + 1, tile.getTilePosition().y + 1).isTileNotEmpty())
                return true;
        }
        else if (color == Color.BLACK) {
            if(board.getTile(tile.getTilePosition().x - 1, tile.getTilePosition().y - 1).isTileNotEmpty() ||
               board.getTile(tile.getTilePosition().x - 1, tile.getTilePosition().y + 1).isTileNotEmpty())
                return true;
        }
        return false;
    }
}