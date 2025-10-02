
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

class GameState implements Serializable{
    private int CurrentRound;
    private final ArrayList<Piece> WhitePieces = new ArrayList<>();
    private final ArrayList<Piece> BlackPieces = new ArrayList<>();
    private Piece WhiteKing;
    private Piece BlackKing;
    @Serial
    private static final long serialVersionUID = 1L;


    public GameState() {
        CurrentRound = -1;

        // Pawns
        for (int row = 0; row <= 7; row++) {
            for (int col = 0; col <= 7; col++) {
                if (row == 1) {
                    BlackPieces.add(new Pawn(row, col, "myPicture/BPawn.png", 1));
                } else if (row == 6) {
                    WhitePieces.add(new Pawn(row, col, "myPicture/WPawn.png", -1));
                }
            }
        }

        // Back ranks
        setupBackRank(0, 1, "B", BlackPieces);
        setupBackRank(7, -1, "W", WhitePieces);
    }

    /**
     * Helper method to set up the back rank pieces using switch-case.
     */
    private void setupBackRank(int row, int direction, String prefix, ArrayList<Piece> targetList) {
        for (int col = 0; col <= 7; col++) {
            Piece piece = null;
            switch (col) {
                case 0, 7 -> piece = new Rook(row, col, "myPicture/" + prefix + "Rook.png", direction);
                case 1, 6 -> piece = new Knight(row, col, "myPicture/" + prefix + "Knight.png", direction);
                case 2, 5 -> piece = new Bishop(row, col, "myPicture/" + prefix + "Bishop.png", direction);
                case 3 -> piece = new Queen(row, col, "myPicture/" + prefix + "Queen.png", direction);
                case 4 -> piece = new King(row, col, "myPicture/" + prefix + "King.png", direction);
            }

            if (piece != null) {
                targetList.add(piece);
                // Store kings separately
                if (piece instanceof King) {
                    if (prefix.equals("W")) {
                        WhiteKing = piece;
                    } else {
                        BlackKing = piece;
                    }
                }
            }
        }
    }

    public ArrayList<Piece> getWhitePieces(){
        return WhitePieces;
    }
    public ArrayList<Piece> getBlackPieces(){
        return BlackPieces;
    }

    public int getCurrentRound(){
        return CurrentRound;
    }
    public Piece getWhiteKing(){
        return WhiteKing;

    }
    public Piece getBlackKing(){
        return BlackKing;
    }

    public void setCurrentRound(int currentRound) {
        this.CurrentRound = currentRound;
    }

}