package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();

        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor myColor = piece.getTeamColor();

        if (piece.getPieceType() == PieceType.KING) {
            kingMoves(board, myPosition, myColor, moves);
        }

        if (piece.getPieceType() == PieceType.KNIGHT) {
            knightMoves(board, myPosition, myColor, moves);
        }

        if (piece.getPieceType() == PieceType.ROOK) {
            rookMoves(board, myPosition, myColor, moves);
        }

        if (piece.getPieceType() == PieceType.BISHOP) {
            bishopMoves(board, myPosition, myColor, moves);
        }

        if (piece.getPieceType() == PieceType.QUEEN) {
            queenMoves(board, myPosition, myColor, moves);
        }

        return moves;
    }

    private void kingMoves (ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor, List<ChessMove> moves) {
        int [][] directions = {
                {1,0}, {-1,0}, {0,1}, {0,-1}, {1,1}, {-1,1}, {1,-1}, {-1,-1}
        };

        // Try each possible directions
        for (int[] dir : directions) {
            int targetRow = myPosition.getRow() + dir[0];
            int targetCol = myPosition.getColumn() + dir[1];

            // Check if in-bounds
            if (targetRow < 1 || targetRow > 8 || targetCol < 1 || targetCol > 8) {
                continue;
            }

            // Get the position and piece
            ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
            ChessPiece targetPiece = board.getPiece(targetPosition);

            // Empty target or enemy, add move
            if (targetPiece == null || targetPiece.getTeamColor() != myColor) {
                moves.add(new ChessMove(myPosition, targetPosition, null));
            }
        }
    }

    private void knightMoves (ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor, List<ChessMove> moves) {
        int[][] directions = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}
        };

        // Try each possible directions
        for (int[] dir : directions) {
            int targetRow = myPosition.getRow() + dir[0];
            int targetCol = myPosition.getColumn() + dir[1];

            // Check if in-bounds
            if (targetRow < 1 || targetRow > 8 || targetCol < 1 || targetCol > 8) {
                continue;
            }

            // Get the position and piece
            ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
            ChessPiece targetPiece = board.getPiece(targetPosition);

            // Empty target or enemy, add move
            if (targetPiece == null || targetPiece.getTeamColor() != myColor) {
                moves.add(new ChessMove(myPosition, targetPosition, null));
            }
        }
    }

    private void rookMoves (ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor, List<ChessMove> moves) {
        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

        // Try each possible directions
        for (int[] dir : directions) {
            int targetRow = myPosition.getRow() + dir[0];
            int targetCol = myPosition.getColumn() + dir[1];

            // Check if in-bounds
            while (targetRow >= 1 && targetRow <= 8 && targetCol >= 1 && targetCol <= 8) {
                ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
                ChessPiece targetPiece = board.getPiece(targetPosition);

                if (targetPiece == null) {
                    // Empty target? Keep moving
                    moves.add(new ChessMove(myPosition, targetPosition, null));
                } else {
                    // Not empty? If it's an enemy, add move
                    if (targetPiece.getTeamColor() != myColor) {
                        moves.add(new ChessMove(myPosition, targetPosition, null));
                    }
                    // Empty or not, stop moving. This piece cannot jump over another piece
                    break;
                }
                targetRow += dir[0];
                targetCol += dir[1];
            }
        }
    }

    private void bishopMoves (ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor, List<ChessMove> moves) {
        int[][] directions = {
                {1, -1}, {1, 1}, {-1, 1}, {-1, -1}
        };

        // Try each possible directions
        for (int[] dir : directions) {
            int targetRow = myPosition.getRow() + dir[0];
            int targetCol = myPosition.getColumn() + dir[1];

            // Check if in-bounds
            while (targetRow >= 1 && targetRow <= 8 && targetCol >= 1 && targetCol <= 8) {
                ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
                ChessPiece targetPiece = board.getPiece(targetPosition);

                if (targetPiece == null) {
                    // Empty target? Keep moving
                    moves.add(new ChessMove(myPosition, targetPosition, null));
                } else {
                    // Not empty? If it's an enemy, add move
                    if (targetPiece.getTeamColor() != myColor) {
                        moves.add(new ChessMove(myPosition, targetPosition, null));
                    }
                    // Empty or not, stop moving. This piece cannot jump over another piece
                    break;
                }
                targetRow += dir[0];
                targetCol += dir[1];
            }
        }
    }

    private void queenMoves (ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor, List<ChessMove> moves) {
        int[][] directions = {
                {1, -1}, {1, 1}, {-1, 1}, {-1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

        // Try each possible directions
        for (int[] dir : directions) {
            int targetRow = myPosition.getRow() + dir[0];
            int targetCol = myPosition.getColumn() + dir[1];

            // Check if in-bounds
            while (targetRow >= 1 && targetRow <= 8 && targetCol >= 1 && targetCol <= 8) {
                ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
                ChessPiece targetPiece = board.getPiece(targetPosition);

                if (targetPiece == null) {
                    // Empty target? Keep moving
                    moves.add(new ChessMove(myPosition, targetPosition, null));
                } else {
                    // Not empty? If it's an enemy, add move
                    if (targetPiece.getTeamColor() != myColor) {
                        moves.add(new ChessMove(myPosition, targetPosition, null));
                    }
                    // Empty or not, stop moving. This piece cannot jump over another piece
                    break;
                }
                targetRow += dir[0];
                targetCol += dir[1];
            }
        }
    }

    // Generated from intelliJ
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
