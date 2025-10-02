import java.util.ArrayList;
import java.util.HashSet;

class King extends Piece{

    private boolean hasMoved = false;

    public King(int row, int column, String imagePath, int color){
        super(row, column, imagePath, color);
        
    }

    @Override
    public void updatePosition(int row, int column){
        this.hasMoved = true;
        super.updatePosition(row, column);
    }

    @Override
    public HashSet<Tile> getAttackRadius() {
       HashSet<Tile> PossibleTile = new HashSet<>();

        int[] offset = {-1, 0, 1};

        for (int dy : offset){
            for (int dx : offset){
                if (!(dy == 0 && dx == 0)) {
                    if (row() + dy <= 7 && row() + dy >= 0 && column() + dx <= 7 && column() + dx >= 0){
                        PossibleTile.add(Main.board[row() + dy][column() + dx]);
                    }
                }
            }
        }
        return PossibleTile;
    }
    //override the CanBeMovedTo to work with castling

    @Override
    public boolean CanBeMovedTo(int rowTo, int columnTo){


        ArrayList<Piece> EnemyList;

        boolean leftCastle = true;
        boolean rightCastle = true;

        if (this.color == -1){
            EnemyList =  Main.BlackPieces;    
        } else{
            EnemyList = Main.WhitePieces;
        }


        // check if the king can be castled

        Rook leftRook = null;
        Rook rightRook = null;
        
        if (Main.board[row][0].getPiece() instanceof Rook Piece){
            leftRook = Piece;
        }
        if (Main.board[row][7].getPiece() instanceof Rook Piece){
            rightRook = Piece;
        }

        if (leftRook != null && !leftRook.hasMoved && !this.hasMoved){

           
            HashSet<Tile> leftPath = new HashSet<>();

            int cols = leftRook.column + 1;

            while (cols < this.column){

                if (Main.board[row][cols].getPiece() != null){
                    leftCastle = false;
                    break;
                }
                leftPath.add(Main.board[row][cols]);
                cols++;
            }
            if (leftCastle){
                for (Piece eachPiece : EnemyList){
                    for (Tile eachTile : eachPiece.getAttackRadius()){
                        if (leftPath.contains(eachTile)){
                            leftCastle = false;
                            break;
                        }
                    }
                }
            }
        } else{
            leftCastle = false;
        }

        if (rightRook != null && !rightRook.hasMoved && !this.hasMoved){
            
            HashSet<Tile> rightPath = new HashSet<>();

            int cols = rightRook.column - 1;

            while (cols > this.column){

                if (Main.board[row][cols].getPiece() != null){
                    rightCastle = false;
                    break;
                }
                rightPath.add(Main.board[row][cols]);
                cols--;
            }
            if (rightCastle){
                for (Piece eachPiece : EnemyList){
                    for (Tile eachTile : eachPiece.getAttackRadius()){
                        if (rightPath.contains(eachTile)){
                            rightCastle = false;
                            break;
                        }
                    }
                }
            }
        } else{
            rightCastle = false;
        }

        //If you can left castle

        if (leftCastle && columnTo == 2){
            Main.AllowMove(Main.board[this.row][leftRook.column], Main.board[this.row][3], true);
            
            return true;
        }

        if (rightCastle && columnTo == 6){
            Main.AllowMove(Main.board[this.row][rightRook.column], Main.board[this.row][5], true);
            return true;
        }

        // normal move

        return super.CanBeMovedTo(rowTo, columnTo);
        
    }
    
}