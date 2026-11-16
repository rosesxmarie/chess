package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class ChessBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String[] HEADERS = {"a", "b", "c", "d", "e", "f", "g", "h"};

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawBoard(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawBoard(PrintStream out) {
        for (int row = BOARD_SIZE_IN_SQUARES - 1; row >= 0; row--) {
            drawSquares(out, row);
        }
    }

    private static void drawSquares(PrintStream out, int row) {
        for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {
            if ((row + col) % 2 == 0) {
                setMagenta(out);
            } else {
                setWhite(out);
            }
            out.print(EMPTY);
        }
        setBlack(out);
        out.println();
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }
    private static void setMagenta(PrintStream out) {
        out.print(SET_BG_COLOR_MAGENTA);
        out.print(SET_TEXT_COLOR_MAGENTA);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

}
