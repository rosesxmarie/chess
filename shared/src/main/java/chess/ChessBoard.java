package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    // Make a data sctructure of the board
    ChessPiece[][] squares = new ChessPiece[8][8];
    // This is a constructor for initialization. This will create an empty board
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        // Jave is 0 based, so that's why we need to subtract 1 on the position.
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        // Get the position
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Initialize the new board
        squares = new ChessPiece[8][8];

        // Add all the pieces
        ChessPiece.PieceType[] backRanks = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };

        // WHITE Pieces
        for (int col = 0; col < 8; col++) {
            addPiece(new ChessPosition(1,col +1), new ChessPiece(ChessGame.TeamColor.WHITE, backRanks[col]));
            addPiece(new ChessPosition(2,col +1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }

        // BLACK Pieces
        for (int col = 0; col < 8; col++) {
            addPiece(new ChessPosition(8,col +1), new ChessPiece(ChessGame.TeamColor.BLACK, backRanks[col]));
            addPiece(new ChessPosition(7,col +1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
    }

    // Generated from intelliJ
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
