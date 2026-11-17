package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.WHITE_KING;

public class BoardDisplay {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String[] COLUMN_HEADERS = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private static final String[] ROW_HEADERS = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private static final String[] BLACK_PIECES = {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK};
    private static final String[] WHITE_PIECES = {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK};


    static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawBoard(out, ChessGame.TeamColor.WHITE);
        out.println();
        drawBoard(out, ChessGame.TeamColor.BLACK);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    public static void draw(ChessGame.TeamColor teamColor) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        drawBoard(out, teamColor);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawBoard(PrintStream out, ChessGame.TeamColor teamColor) {
        drawColHeaders(out, teamColor);

        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
            int row = i;
            if (teamColor == ChessGame.TeamColor.WHITE) {
                row = BOARD_SIZE_IN_SQUARES - 1 - i;
            }

            drawRowHeaders(out, row);

            for (int j = 0; j < BOARD_SIZE_IN_SQUARES; j++) {
                int col = j;
                if (teamColor == ChessGame.TeamColor.WHITE) {
                    col = BOARD_SIZE_IN_SQUARES - 1 - j;
                }

                drawSquares(out, i, j);
                drawChessPieces(out, row, col);
            }
                drawRowHeaders(out, row);
                setBlack(out);
                out.println();
            }
            drawColHeaders(out, teamColor);
        }

    private static void drawSquares(PrintStream out, int row, int col) {
            if ((row + col) % 2 == 0) {
                setWhite(out);
            } else {
                setMagenta(out);
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

    private static void drawChessPieces(PrintStream out, int row, int col) {
        if (row == 0) {
            printWhitePiece(out, WHITE_PIECES[col]);
        } else if (row == 1) {
            printWhitePiece(out, WHITE_PAWN);
        } else if (row == 6) {
            printBlackPiece(out, BLACK_PAWN);
        } else if (row == 7) {
            printBlackPiece(out, BLACK_PIECES[col]);
        } else {
            out.print(EMPTY);
        }
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

    private static void printBlackPiece(PrintStream out, String piece) {
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(piece);
    }

    private static void printWhitePiece(PrintStream out, String piece) {
        out.print(SET_TEXT_COLOR_RED);
        out.print(piece);
    }
}
