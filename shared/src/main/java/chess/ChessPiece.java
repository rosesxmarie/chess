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
public class ChessPiece implements Cloneable {

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
        ChessGame.TeamColor myColor = getTeamColor();

        if (piece.getPieceType() == PieceType.KING) {
            int [][] directions = {
                    {1,0}, {-1,0}, {0,1}, {0,-1}, {1,1}, {-1,1}, {1,-1}, {-1,-1}
            };
            stepJumpMoves(board, myPosition, myColor, moves, directions);
        }

        if (piece.getPieceType() == PieceType.KNIGHT) {
            int[][] directions = {
                    {2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}
            };
            stepJumpMoves(board, myPosition, myColor, moves, directions);
        }

        if (piece.getPieceType() == PieceType.ROOK) {
            int[][] directions = {
                    {1, 0}, {-1, 0}, {0, 1}, {0, -1}
            };
            slidingMoves(board, myPosition, myColor, moves, directions);
        }

        if (piece.getPieceType() == PieceType.BISHOP) {
            int[][] directions = {
                    {1, -1}, {1, 1}, {-1, 1}, {-1, -1}
            };
            slidingMoves(board, myPosition, myColor, moves, directions);
        }

        if (piece.getPieceType() == PieceType.QUEEN) {
            int[][] directions = {
                    {1, -1}, {1, 1}, {-1, 1}, {-1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}
            };
            slidingMoves(board, myPosition, myColor, moves, directions);
        }

        if (piece.getPieceType() == PieceType.PAWN) {
            pawnMoves(board, myPosition, myColor, moves);
        }
        return moves;
    }

    private boolean isInBounds (int targetRow, int targetCol) {
        return (targetRow >= 1 && targetRow <= 8 && targetCol >= 1 && targetCol <= 8);
    }

    private void stepJumpMoves (ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor, List<ChessMove> moves, int[][] directions) {
        // Try each possible directions
        for (int[] dir : directions) {
            int targetRow = myPosition.getRow() + dir[0];
            int targetCol = myPosition.getColumn() + dir[1];

            // Check if in-bounds
            if (!isInBounds(targetRow, targetCol)) {
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

    private void slidingMoves (ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor, List<ChessMove> moves, int[][] directions) {
        // Try each possible directions
        for (int[] dir : directions) {
            int targetRow = myPosition.getRow() + dir[0];
            int targetCol = myPosition.getColumn() + dir[1];

            // Check if in-bounds
            while (isInBounds(targetRow, targetCol)) {
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


    private void pawnForwardMoves (ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor, List<ChessMove> moves, int[][] directions, int initialRow, int endRow, int direction) {
        for (int[] dir : directions) {
            if (dir[0] == 2 && myPosition.getRow() != initialRow) {
                continue;
            }

            int targetRow = myPosition.getRow() + dir[0] * direction;
            int targetCol = myPosition.getColumn() + dir[1];

            // Check if in-bounds
            if (!isInBounds(targetRow, targetCol)) {
                continue;
            }

            // Get the position and piece
            ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
            ChessPiece targetPiece = board.getPiece(targetPosition);

            if (targetPiece != null) {
                break;
            } else {
                if (targetPosition.getRow() == endRow) {
                    for (PieceType promotion : promotionPiece) {
                        moves.add(new ChessMove(myPosition, targetPosition, promotion));
                    }
                } else {
                    moves.add(new ChessMove(myPosition, targetPosition, null));
                }
            }
        }
    }

    private void pawnCaptureMoves (ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor, List<ChessMove> moves, int[][] directions, int initialRow, int endRow, int direction) {
        for (int[] dir : directions) {
            int targetRow = myPosition.getRow() + dir[0] * direction;
            int targetCol = myPosition.getColumn() + dir[1];

            // Check if in-bounds
            if (!isInBounds(targetRow, targetCol)) {
                continue;
            }

            // Get the position and piece
            ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
            ChessPiece targetPiece = board.getPiece(targetPosition);

            // Empty target or enemy, add move
            if (targetPiece != null && targetPiece.getTeamColor() != myColor) {
                if (targetPosition.getRow() == endRow) {
                    for (PieceType promotion : promotionPiece) {
                        moves.add(new ChessMove(myPosition, targetPosition, promotion));
                    }
                } else {
                    moves.add(new ChessMove(myPosition, targetPosition, null));
                }
            }
        }
    }

    private static final PieceType[] promotionPiece = {
            PieceType.KNIGHT,
            PieceType.ROOK,
            PieceType.BISHOP,
            PieceType.QUEEN
    };

    private void pawnMoves (ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor, List<ChessMove> moves) {
        int[][] diagonalDirections = {
                {1, 1}, {1, -1}
        };

        int[][] forwardDirections = {
                {1, 0}, {2, 0}
        };

        int initialRow = (myColor == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int direction = (myColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int endRow = (myColor == ChessGame.TeamColor.WHITE) ? 8 : 1;

        pawnForwardMoves(board, myPosition, myColor, moves, forwardDirections, initialRow, endRow, direction);
        pawnCaptureMoves(board, myPosition, myColor, moves, diagonalDirections, initialRow, endRow, direction);
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

    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
