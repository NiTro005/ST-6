import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProgramTest {

    @Test
    void checkStateDetectsXWinAndOWinAndDraw() {
        Game game = new Game();

        char[] xWin = {'X','X','X',' ','O',' ',' ',' ','O'};
        game.symbol = 'X';
        assertEquals(State.XWIN, game.checkState(xWin));

        char[] oWin = {'O','X','X',' ','O',' ','X',' ','O'};
        game.symbol = 'O';
        assertEquals(State.OWIN, game.checkState(oWin));

        char[] draw = {'X','O','X','X','O','O','O','X','X'};
        game.symbol = 'X';
        assertEquals(State.DRAW, game.checkState(draw));
    }

    @Test
    void generateMovesReturnsAllEmptyPositions() {
        Game game = new Game();
        char[] board = {'X',' ','O',' ',' ','X','O',' ',' '};
        ArrayList<Integer> moves = new ArrayList<>();

        game.generateMoves(board, moves);

        assertEquals(5, moves.size());
        assertIterableEquals(java.util.List.of(1,3,4,7,8), moves);
    }

    @Test
    void evaluatePositionReturnsExpectedScores() {
        Game game = new Game();
        Player x = new Player();
        x.symbol = 'X';
        Player o = new Player();
        o.symbol = 'O';

        char[] xWin = {'X','X','X',' ',' ',' ',' ',' ',' '};
        game.symbol = 'X';
        assertEquals(Game.INF, game.evaluatePosition(xWin, x));
        assertEquals(-Game.INF, game.evaluatePosition(xWin, o));

        char[] draw = {'X','O','X','X','O','O','O','X','X'};
        game.symbol = 'X';
        assertEquals(0, game.evaluatePosition(draw, x));

        char[] inPlay = {'X','O','X',' ',' ','O',' ','X',' '};
        game.symbol = 'X';
        assertEquals(-1, game.evaluatePosition(inPlay, x));
    }

    @Test
    void minimaxChoosesWinningMoveWhenAvailable() {
        Game game = new Game();
        Player o = new Player();
        o.symbol = 'O';

        char[] board = {'O','O',' ','X','X',' ',' ',' ',' '};

        int move = game.MiniMax(board, o);

        assertEquals(3, move);
    }

    @Test
    void ticTacToeCellStoresCoordinatesAndMarker() {
        TicTacToeCell cell = new TicTacToeCell(5, 2, 1);

        assertEquals(5, cell.getNum());
        assertEquals(1, cell.getRow());
        assertEquals(2, cell.getCol());
        assertEquals(' ', cell.getMarker());

        cell.setMarker("X");
        assertEquals('X', cell.getMarker());
        assertFalse(cell.isEnabled());
    }
}
