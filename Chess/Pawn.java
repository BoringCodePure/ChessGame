import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import javax.imageio.ImageIO;
import javax.swing.*;



class Pawn extends Piece{

    private boolean doubleMove = false;

    public Pawn(int row, int column, String imagePath, int color) {
        super(row, column, imagePath, color);
    }
    @Override
    public void updatePosition(int newRow, int newColumn){

        if (Math.abs(this.row - newRow) >= 1){
            doubleMove = true;
        }

        super.updatePosition(newRow, newColumn);

        // check for a promotion option;
        if (color == -1){
            if (row == 0) {
                displayPromotionScreen();
            }
        } else{
            if (row == 7){
                displayPromotionScreen();
            }
        }

    }

    private void displayPromotionScreen(){
        Main.underPromotion = true;
        int row = this.row();
        int column = this.column();

        Piece Queen;
        Piece Rook;
        Piece Knight;
        Piece Bishop;

        ArrayList<Piece> PieceSet;
        if (this.color == -1){
            Queen = new Queen(row, column, "myPicture/WQueen.png", -1);
            Rook = new Rook(row, column, "myPicture/WRook.png", - 1);
            Knight = new Knight(row, column, "myPicture/WKnight.png", -1);
            Bishop = new Bishop(row, column, "myPicture/WBishop.png", -1);

            PieceSet = Main.WhitePieces;
        } else{
            Rook = new Rook(row, column, "myPicture/BRook.png", 1);
            Knight = new Knight(row, column, "myPicture/BKnight.png", 1);
            Bishop = new Bishop(row, column, "myPicture/BBishop.png", 1);
            Queen = new Queen(row, column, "myPicture/BQueen.png", 1);
            PieceSet = Main.BlackPieces;
        }
        PieceSet.remove(this);

        Piece[] option = {Queen, Rook, Bishop, Knight};

        JFrame newFrame = new JFrame("Choose Piece");

        JPanel gridPanel = new JPanel(new GridLayout(1, 4, 0, 0));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        for (Piece eachPiece : option){
            JButton PieceOption = new JButton();
            PieceOption.setIcon(new ImageIcon(eachPiece.getImage()));
            PieceOption.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Main.summonPiece(eachPiece);

                    PieceSet.add(eachPiece);
                    newFrame.dispatchEvent(new WindowEvent(newFrame, WindowEvent.WINDOW_CLOSING));
                    Main.underPromotion = false;
                }
            });
            gridPanel.add(PieceOption);
        }
        newFrame.setContentPane(gridPanel);
        newFrame.pack();
        newFrame.setSize(900, 300);
        newFrame.setLocationRelativeTo(null);
        newFrame.setVisible(true);
    }

    @Override
    public HashSet<Tile> getAttackRadius() {
        HashSet<Tile> PossiblePath = new HashSet<Tile>();
        int dr = 0;
        
        if (this.color == -1){
            dr = -1;
        } else{
            dr = 1;
        }

        int row = this.row() + dr;
        int column = this.column();

        // just the straight forward path, not include attacking;
        if (!doubleMove){
            for (int i = 0; i <= 1; i++){
                if (row <= 7 && row >= 0  && Main.board[row][column].getPiece() == null){
                    PossiblePath.add(Main.board[row][column]);
                    row = row + dr;
                } else{
                    break;
                }
            }
        } else{
            if (row <= 7 && row >= 0 && Main.board[row][column].getPiece() == null) {
                PossiblePath.add(Main.board[row][column]);
            }
        }
        row = this.row();
        column = this.column();
        int[] dc = {-1, 1};
        // include attacking;
        // check for adjacent pieces
        for (int dx : dc){
            if (row + dr <= 7 && row + dr >= 0 && column + dx <= 7 && column + dx >= 0){
                Piece opponentPiece = Main.board[row + dr][column + dx].getPiece();
                if (opponentPiece != null){
                    PossiblePath.add(Main.board[row + dr][column + dx]);
                }
            }
        }
        return PossiblePath;
    }
}