import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import javax.imageio.ImageIO;

abstract class Piece implements Serializable {
    transient BufferedImage  PieceImage;
    protected int row;
    protected int column;
    protected int color;
    private final String imagePath;


    public Piece(int row, int column, String imagePath, int color){
        try{
            InputStream input = getClass().getClassLoader().getResourceAsStream(imagePath);
            PieceImage = ImageIO.read(input);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        this.row = row;
        this.column = column;
        this.color = color;
        this.imagePath = imagePath;
    }

    public void updatePosition(int newRow, int newColumn){
        this.row = newRow;
        this.column = newColumn;
        
    }

    public int row(){
        return row;
    }
    public int column(){
        return column;
    }
    public BufferedImage getImage(){

        if (PieceImage == null){
            try{
                InputStream input = getClass().getClassLoader().getResourceAsStream(imagePath);
                PieceImage = ImageIO.read(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return PieceImage;
    }
    public abstract HashSet<Tile> getAttackRadius();

    public ArrayList<Tile> helperRadius(int dr, int dc){
        ArrayList<Tile> PossibleTile = new ArrayList<>();
        int row = this.row() + dr;
        int column = this.column() + dc;

        while (row <= 7 && row >= 0 && column  <= 7 && column  >= 0) {
            if (Main.board[row][column].getPiece() == null){
                PossibleTile.add(Main.board[row ][column ]);
            } else{
                PossibleTile.add(Main.board[row ][column] );
                break;
            }

            row = row + dr;
            column = column + dc;
        }
        return PossibleTile;
    }

    public boolean CanBeMovedTo(int row, int column){

        Tile tileTo = Main.board[row][column];

        if (!getAttackRadius().contains(tileTo)){
            return false;
        }

        if (tileTo.getPiece() != null && tileTo.getPiece().color == this.color){
            return false;
        }

        return Main.pseudoLegalMove(Main.board[this.row()][this.column()], Main.board[row][column]);
    }
}