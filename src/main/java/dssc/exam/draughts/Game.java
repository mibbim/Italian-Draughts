package dssc.exam.draughts;

import dssc.exam.draughts.exceptions.EmptyTileException;
import dssc.exam.draughts.exceptions.IncompleteMoveException;
import dssc.exam.draughts.exceptions.InvalidColorException;

import java.awt.*;
import java.util.ArrayList;

public class Game {
    Player whitePlayer = new Player(Color.WHITE);
    Player blackPlayer = new Player(Color.BLACK);
    Player currentPlayer = whitePlayer;
    private Board board = new Board();
    public int round = 0;


    void loadGame(Board board, int round) {
        this.board = board;
        this.round = round;
        if ((round % 2) == 0)
            this.currentPlayer = this.whitePlayer;
        else
            this.currentPlayer = this.blackPlayer;
    }

    void play() {
        while (whitePlayerHasPieces() & blackPlayerHasPieces()) {
            playRound();
        }
        changePlayer();
        System.out.println("The winner is " + currentPlayer.name);
    }

    void initPlayers() {
        whitePlayer.initializePlayerName(1);
        blackPlayer.initializePlayerName(2);
    }

    void startGame() {
        initPlayers();
        play();
    }

    private void performAction() throws Exception {
        Point source = currentPlayer.readSource();
        TestSourceValidity(source);

        Point destination = currentPlayer.readDestination();
        Move.moveDecider(board, source, destination);
    }


    void playRound() {
        giveInitialRoundInformationToThePlayer();
        readAndPerformMove();
        changePlayer();
        ++round;
    }

    private void readAndPerformMove() {
        while (true) {
            try {
                performAction();
                break;
            } catch (IncompleteMoveException e) {
                continueTheRound(e);
                break;
            } catch (Exception e) {
                signalInvalidMoveToPlayer(e);
            }
        }
    }

    private void giveInitialRoundInformationToThePlayer() {
        board.display();
        currentPlayer.displayHolder();
    }

    private void continueTheRound(IncompleteMoveException e) {
        int movesToCompleteTurn = e.getNumberOfSkips();
        Point newSource = e.getNewSource();
        while (movesToCompleteTurn > 1) {
            board.display();
            System.out.println(e.getMessage());
            newSource = makeAStepInMultipleSkip(e.getSkipPath(), newSource);
            --movesToCompleteTurn;
        }
    }

    private Point makeAStepInMultipleSkip(ArrayList<Tile> skipPath, Point source) {
        while (true) {
            try {
                Point destination = currentPlayer.readDestination();
                Move.continueToSkip(board, source, destination, skipPath);
                source = destination;
                break;
            } catch (Exception e) {
                signalInvalidMoveToPlayer(e);
            }
        }
        return source;
    }

    private void signalInvalidMoveToPlayer(Exception exception) {
        System.out.print("Invalid move: ");
        System.out.println(exception.getMessage());
    }


    private void TestSourceValidity(Point source) throws Exception {
        Tile sourceTile = board.getTile(source);
        if (sourceTile.isEmpty())
            throw new EmptyTileException("The first Tile you selected contains no Piece");
        if (sourceTile.getPiece().getColor() != currentPlayer.getColor())
            throw new InvalidColorException("The piece you intend to move belongs to your opponent");
    }

    void changePlayer() {
        if (currentPlayer == blackPlayer) {
            currentPlayer = whitePlayer;
            return;
        }
        currentPlayer = blackPlayer;
    }

    boolean blackPlayerHasPieces() {
        return board.getNumberOfPiecesOfColor(Color.BLACK) != 0;
    }

    boolean whitePlayerHasPieces() {
        return board.getNumberOfPiecesOfColor(Color.WHITE) != 0;
    }

    int getRound() {
        return round;
    }

    Player getCurrentPlayer() {
        return currentPlayer;
    }
}
