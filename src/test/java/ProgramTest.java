import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProgramTest {

    @Test
    void gameConstructorInitializesDefaults() {
        Game game = new Game();

        assertEquals(State.PLAYING, game.state);
        assertNotNull(game.player1);
        assertNotNull(game.player2);
        assertEquals('X', game.player1.symbol);
        assertEquals('O', game.player2.symbol);
        assertEquals(9, game.board.length);
        for (char c : game.board) {
            assertEquals(' ', c);
        }
    }

    @Test
    void checkStateDetectsAllTerminalAndPlayingStates() {
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

        char[] playing = {'X','O','X',' ',' ','O',' ','X',' '};
        game.symbol = 'O';
        assertEquals(State.PLAYING, game.checkState(playing));
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
    void generateMovesReturnsEmptyListOnFullBoard() {
        Game game = new Game();
        char[] fullBoard = {'X','O','X','X','O','O','O','X','X'};
        ArrayList<Integer> moves = new ArrayList<>();

        game.generateMoves(fullBoard, moves);

        assertTrue(moves.isEmpty());
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
    void minMoveAndMaxMoveReturnTerminalScores() {
        Game game = new Game();
        Player x = new Player();
        x.symbol = 'X';

        char[] xAlreadyWon = {'X','X','X','O','O',' ',' ',' ',' '};
        game.symbol = 'X';
        assertEquals(Game.INF, game.MinMove(xAlreadyWon, x));
        assertEquals(Game.INF, game.MaxMove(xAlreadyWon, x));

        Player o = new Player();
        o.symbol = 'O';
        game.symbol = 'X';
        assertEquals(-Game.INF, game.MinMove(xAlreadyWon, o));
        assertEquals(-Game.INF, game.MaxMove(xAlreadyWon, o));
    }

    @Test
    void utilityPrintMethodsProduceOutput() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(output));
        try {
            Utility.print(new char[]{'X','O',' ',' ',' ',' ',' ',' ',' '});
            Utility.print(new int[]{1,2,3,4,5,6,7,8,9});
            ArrayList<Integer> moves = new ArrayList<>(java.util.List.of(0, 4, 8));
            Utility.print(moves);
        } finally {
            System.setOut(oldOut);
        }

        String text = output.toString();
        assertTrue(text.contains("X-O-"));
        assertTrue(text.contains("1-2-3"));
        assertTrue(text.contains("0-4-8"));
    }



    @Test
    void panelCreatesNineCellsWithExpectedCoordinates() throws Exception {
        TicTacToePanel panel = new TicTacToePanel(new java.awt.GridLayout(3, 3));

        java.lang.reflect.Field cellsField = TicTacToePanel.class.getDeclaredField("cells");
        cellsField.setAccessible(true);
        TicTacToeCell[] cells = (TicTacToeCell[]) cellsField.get(panel);

        assertEquals(9, cells.length);
        assertEquals(0, cells[0].getRow());
        assertEquals(0, cells[0].getCol());
        assertEquals(2, cells[8].getRow());
        assertEquals(2, cells[8].getCol());
    }

    @Test
    void panelClickMakesHumanAndAiMovesWithoutFinishingGame() throws Exception {
        TicTacToePanel panel = new TicTacToePanel(new java.awt.GridLayout(3, 3));

        java.lang.reflect.Field cellsField = TicTacToePanel.class.getDeclaredField("cells");
        cellsField.setAccessible(true);
        TicTacToeCell[] cells = (TicTacToeCell[]) cellsField.get(panel);

        cells[0].doClick();

        java.lang.reflect.Field gameField = TicTacToePanel.class.getDeclaredField("game");
        gameField.setAccessible(true);
        Game game = (Game) gameField.get(panel);

        int filled = 0;
        for (char c : game.board) {
            if (c != ' ') filled++;
        }
        assertTrue(filled >= 2);
        assertEquals(State.PLAYING, game.state);
        assertEquals(game.player1, game.cplayer);
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
