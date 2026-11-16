package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class ChessBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String[] COLUMN_HEADERS = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private static final String[] ROW_HEADERS = {"1", "2", "3", "4", "5", "6", "7", "8"};


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawBoard(out, ChessGame.TeamColor.WHITE);
        out.println();
        drawBoard(out, ChessGame.TeamColor.BLACK);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawBoard(PrintStream out, ChessGame.TeamColor teamColor) {
        drawColHeaders(out, teamColor);

        for (int i = 0;  i < BOARD_SIZE_IN_SQUARES; i++) {
            int row = i;
            if (teamColor == ChessGame.TeamColor.WHITE) {
                row = BOARD_SIZE_IN_SQUARES - 1 - i;
            }
            drawRowHeaders(out, row);
            drawSquares(out, i);
            drawRowHeaders(out, row);
            setBlack(out);
            out.println();
        }
        drawColHeaders(out, teamColor);
    }

    private static void drawSquares(PrintStream out, int row) {
        for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {
            if ((row + col) % 2 == 0) {
                setWhite(out);
            } else {
                setMagenta(out);
            }
            out.print(EMPTY);
        }
    }

    private static void drawColHeaders(PrintStream out, ChessGame.TeamColor teamColor) {
        setHeaders(out);
        out.print(EMPTY);

        for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col ++) {
            if (teamColor == ChessGame.TeamColor.WHITE) {
                out.print(" " + COLUMN_HEADERS[col] + " ");
            } else {
                out.print(" " + COLUMN_HEADERS[BOARD_SIZE_IN_SQUARES - 1 - col] + " ");
            }
        }
        out.print(EMPTY);
        setBlack(out);
        out.println();
    }

    private static void drawRowHeaders(PrintStream out, int row) {
        setHeaders(out);
        out.print(" " + ROW_HEADERS[row] + " ");
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
    private static void setHeaders(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_MAGENTA);
    }



}
