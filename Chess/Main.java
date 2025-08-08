import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

class Mouse{
    public static Piece PlayerPiece;
    public static Tile TargetTile;

}

class Tile extends JButton implements ComponentListener, MouseListener{
    private int row;
    private int column;
    private Piece piece;

    public Tile(int row, int column){
        this.row = row;
        this.column = column;

        if (row % 2 == column % 2){

            this.setBackground(new Color(234, 221, 202));
        } else{

            this.setBackground(new Color(92, 64, 51));
        }

        this.addComponentListener(this);
        this.addMouseListener(this);

    }

    public void setPiece(Piece IncomingPiece){
        if (IncomingPiece == null){
            piece = null;
            this.setIcon(null);
        } else{
            piece = IncomingPiece;
            this.setIcon(new ImageIcon(IncomingPiece.getImage()));

        }
    }

    public int row(){
        return row;
    }
    public int column(){
        return column;
    }

    public Piece getPiece() {
        return piece;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (piece != null){
            int width = this.getWidth();
            int height = this.getHeight();

            BufferedImage blankImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = blankImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(blankImage, 0, 0, width, height, null);
            g2d.drawImage(piece.getImage(),0, 0, width, height, null);
            this.setIcon(new ImageIcon(blankImage));
            g2d.dispose();
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (!Main.underPromotion){
            if (piece != null){
                Mouse.PlayerPiece = piece;
                HashMap<Tile, Integer> debugAttackedTile = Main.getTileUnderAttack(-(piece.color));

            }
            Debugger.printBoardState();
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!Main.underPromotion){
            if (piece != null){
                Mouse.PlayerPiece = piece;
                for (Tile eachTile : Mouse.PlayerPiece.getAttackRadius()){
                    eachTile.setBackground(new Color(200, 0, 0));
                }
            } else{
                Mouse.PlayerPiece = null;
            }
        }


    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!Main.underPromotion){
            if (Mouse.TargetTile == this){
                Mouse.TargetTile.mouseClicked(null);
                return;
            }

            if (Mouse.PlayerPiece != null && Mouse.TargetTile != null){

                // move piece
                if (Mouse.PlayerPiece.CanBeMovedTo(Mouse.TargetTile.row(), Mouse.TargetTile.column())){
                    Main.AllowMove(this, Mouse.TargetTile);
                    Mouse.PlayerPiece = null;

                    System.out.println("MOve");

                }
            }
            Main.Repaint();
            Mouse.PlayerPiece = null;
            Debugger.printBoardState();
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

        if (!Main.underPromotion){
            Mouse.TargetTile = this;
        } else{
            Mouse.TargetTile = null;
        }


    }

    @Override
    public void mouseExited(MouseEvent e) {
        Mouse.TargetTile = null;
        


    }
}

public class Main{
    public static boolean underPromotion;
    private static JPanel gridPanel;
    private static int currentRound = -1;
    public static Piece WhiteKing;
    public static Piece BlackKing;
    private static final int ROWS = 8;
    private static final int COLS = 8;
    public static Tile[][] board = new Tile[ROWS][COLS];
    public static ArrayList<Piece> WhitePieces = new ArrayList<>();
    public static ArrayList<Piece> BlackPieces = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        // Always start Swing on the Event Dispatch Thread
        SwingUtilities.invokeLater(Main::createAndShowUI);

    }


    private static void createAndShowUI() {
        JFrame frame = new JFrame("6x6 Button Grid");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        gridPanel = new JPanel(new GridLayout(ROWS, COLS, 0, 0));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));




        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Tile btn = new Tile(r, c);
                board[r][c] = btn;
                gridPanel.add(btn);
            }
        }

       for (int row = 0; row <= 7; row++){
           for (int column = 0; column <= 7; column++){
               if (row == 1){
                   Piece pawn = new Pawn(row, column, "myPicture/BPawn.png", 1);
                   board[row][column].setPiece(pawn);
                   BlackPieces.add(pawn);
               }
               if (row == 6){
                   Piece pawn = new Pawn(row, column, "myPicture/WPawn.png", -1);
                   board[row][column].setPiece(pawn);
                   WhitePieces.add(pawn);
               }
           }
       }


        // White pieces
        Piece WKing = new King(7, 4, "myPicture/WKing.png", -1);
        Piece WRook1 = new Rook(7, 0, "myPicture/WRook.png", -1);
        Piece WRook2 = new Rook(7, 7, "myPicture/WRook.png", -1);
        Piece WBishop1 = new Bishop(7, 2, "myPicture/WBishop.png", -1);
        Piece WBishop2 = new Bishop(7, 5, "myPicture/WBishop.png", -1);
        Piece WKnight1 = new Knight(7, 1, "myPicture/WKnight.png", -1);
        Piece WKnight2 = new Knight(7, 6, "myPicture/WKnight.png", -1);

// Black pieces
        Piece BKing = new King(0, 4, "myPicture/BKing.png", 1);
        Piece BRook1 = new Rook(0, 0, "myPicture/BRook.png", 1);
        Piece BRook2 = new Rook(0, 7, "myPicture/BRook.png", 1);
        Piece BBishop1 = new Bishop(0, 2, "myPicture/BBishop.png", 1);
        Piece BBishop2 = new Bishop(0, 5, "myPicture/BBishop.png", 1);
        Piece BKnight1 = new Knight(0, 1, "myPicture/BKnight.png", 1);
        Piece BKnight2 = new Knight(0, 6, "myPicture/BKnight.png", 1);

// Place pieces on the board
        board[7][4].setPiece(WKing);
        board[7][0].setPiece(WRook1);
        board[7][7].setPiece(WRook2);
        board[7][2].setPiece(WBishop1);
        board[7][5].setPiece(WBishop2);
        board[7][1].setPiece(WKnight1);
        board[7][6].setPiece(WKnight2);

        board[0][4].setPiece(BKing);
        board[0][0].setPiece(BRook1);
        board[0][7].setPiece(BRook2);
        board[0][2].setPiece(BBishop1);
        board[0][5].setPiece(BBishop2);
        board[0][1].setPiece(BKnight1);
        board[0][6].setPiece(BKnight2);

// Save king references
        WhiteKing = WKing;
        BlackKing = BKing;

// Add to piece lists
        WhitePieces.add(WKing);
        WhitePieces.add(WRook1);
        WhitePieces.add(WRook2);
        WhitePieces.add(WBishop1);
        WhitePieces.add(WBishop2);
        WhitePieces.add(WKnight1);
        WhitePieces.add(WKnight2);

        BlackPieces.add(BKing);
        BlackPieces.add(BRook1);
        BlackPieces.add(BRook2);
        BlackPieces.add(BBishop1);
        BlackPieces.add(BBishop2);
        BlackPieces.add(BKnight1);
        BlackPieces.add(BKnight2);
        // Add Queens
        Piece WQueen = new Queen(7, 3, "myPicture/WQueen.png", -1);
        Piece BQueen = new Queen(0, 3, "myPicture/BQueen.png", 1);

// Place Queens on board
        board[7][3].setPiece(WQueen);
        board[0][3].setPiece(BQueen);

// Add Queens to piece lists
        WhitePieces.add(WQueen);
        BlackPieces.add(BQueen);

        frame.setContentPane(gridPanel);
        frame.pack();
        frame.setSize(1100, 1100);// size to fit contents
        frame.setLocationRelativeTo(null); // center on screen

        frame.setVisible(true);
    }



    private static void rotateBoard(JPanel gridpanel, int currentTeam){

        // if currentTeam is white rotate to white face;
        if (currentTeam == -1){
            for (int i = 0 ; i < ROWS; i++){
                for (int j = 0; j < COLS; j++){
                    gridpanel.add(board[i][j]);
                }
            }
        } else{
            for (int i = ROWS - 1; i >= 0; i--){
                for (int j = 0; j < COLS; j++){
                    gridpanel.add(board[i][j]);
                }
            }
        }
    }

    public static ArrayList<Piece> getCheckCount(int kingColor){


        Tile KingTile;

        ArrayList<Piece> EnemyPiece;
        ArrayList<Piece> attackerList = new ArrayList<>();

        if (kingColor == -1){
            KingTile = board[WhiteKing.row][WhiteKing.column];

            EnemyPiece = BlackPieces;
        } else{
            KingTile = board[BlackKing.row][BlackKing.column];

            EnemyPiece = WhitePieces;
        }

        for (Piece eachPiece : EnemyPiece){
            if (eachPiece.getAttackRadius().contains(KingTile)){
                attackerList.add(eachPiece);
            }
        }


        return attackerList;

    }

    public static boolean isCheckMate(King king){

        ArrayList<Piece> myPiece;

        if (king.color == -1){
            myPiece = WhitePieces;

        } else{
            myPiece = BlackPieces;
        }

        // get the check count first
        ArrayList<Piece> Attacker = getCheckCount(king.color);

        int checkCount = Attacker.size();

        if (checkCount == 0){
            return false;
        }

        System.out.println("CHECK COUNT = " + checkCount);

        //is there any safe square for king?
        boolean SafeSquare = false;
        for (Tile eachTile : king.getAttackRadius()){
            if (king.CanBeMovedTo(eachTile.row(), eachTile.column())){
                SafeSquare = true;
            }
        }

        // double check
        if (checkCount > 1) {
            //is there any safe square for king?
            if (SafeSquare){
                return false;
            }
            return true;
        }


        // single check;
        if (SafeSquare){
            return false;
        }

        Piece attack = Attacker.getFirst();
        if (attack instanceof Rook || attack instanceof Queen || attack instanceof Bishop){
            for (Piece eachPiece : myPiece){
                for (Tile eachTile : eachPiece.getAttackRadius()){
                    if (getCheckSight(attack, king).contains(eachTile) && eachPiece.CanBeMovedTo(eachTile.row(), eachTile.column())){
                        return false;
                    }
                }
            }
        }

        // given that king has no safe sqaure and the piece that is checking is knight or pawn, then the only way is to capture the attacking pieces
        if (attack instanceof Knight || attack instanceof Pawn){
            for (Piece eachPiece : myPiece){
                for (Tile eachTile : eachPiece.getAttackRadius()){
                    if (eachPiece.CanBeMovedTo(eachTile.row(), eachTile.column())){
                        return false;
                    }
                }
            }
        }


        return true;
    }

    private static ArrayList<Tile> getCheckSight(Piece attacker, King king){

        ArrayList<Tile> path = new ArrayList<>();

        int dr = 0;
        int dc = 0;

        if (king.row() > attacker.row()){
            dr = 1;
        } else{
            dr = -1;
        }

        if (king.column() > attacker.column()){
            dc = 1;
        } else{
            dc = -1;
        }

        if (king.row() == attacker.row()){
            dr = 0;
        }

        if (king.column() == attacker.column()){
            dc = 0;
        }



        path.addAll(CheckSightHelper(attacker, king, dr,dc));
        return path;



    }

    private static ArrayList<Tile> CheckSightHelper(Piece attacker, Piece king, int dr, int dc){


        ArrayList<Tile> path = new ArrayList<>();
        path.add(board[attacker.row()][attacker.column()]);

        int row = attacker.row() + dr;
        int column = attacker.column() + dc;


        while (row <= 7 && row >= 0 && column <= 7 && column >= 0){
            if (board[row][column].getPiece() == king){
                return path;
            } else if (board[row][column].getPiece() == null){
                path.add(board[row][column]);
            }
            row = row + dr;
            column = column + dc;
        }
        return null;
    }

    public static HashMap<Tile, Integer> getTileUnderAttack(int EnemyColor){
        HashMap<Tile, Integer> AttackerPerTile = new HashMap<>();

        ArrayList<Piece> EnemyPiece;
        // white
        if (EnemyColor == -1){
            EnemyPiece = WhitePieces;
        } else{
            EnemyPiece = BlackPieces;
        }


        for (Piece eachPiece : EnemyPiece){

            ArrayList<Tile> TileUnderAttacked = eachPiece.getAttackRadius();

            for (Tile tile : TileUnderAttacked){
                if (AttackerPerTile.containsKey(tile)){
                    int count = AttackerPerTile.get(tile);
                    AttackerPerTile.put(tile, count + 1);
                } else{
                    AttackerPerTile.put(tile, 1);
                }
            }
        }
        return AttackerPerTile;
    }

    public static void AllowMove(Tile from, Tile to){
        // check for capture
        if (to.getPiece() != null){
            if (to.getPiece().color == -1){
                WhitePieces.remove(to.getPiece());
            } else{
                BlackPieces.remove(to.getPiece());
            }
        }

        from.getPiece().updatePosition(to.row(), to.column());

        to.setPiece(from.getPiece());
        from.setPiece(null);

        nextRound();
        Repaint();

    }

    public static void summonPiece(Piece newPiece){
        int row = newPiece.row();
        int column = newPiece.column();

        board[row][column].setPiece((newPiece));

    }




    public static void Repaint(){
        for (Tile[] TileArray : board){
            for (Tile eachTile : TileArray){
                if (eachTile.getPiece() != null){
                    eachTile.componentResized(null);
                    eachTile.setText(null);
                }
                eachTile.setText(null);
                if (eachTile.row() % 2 == eachTile.column() % 2){

                    eachTile.setBackground(new Color(234, 221, 202));
                } else{

                    eachTile.setBackground(new Color(92, 64, 51));
                }

                eachTile.componentResized(null);
            }
        }
    }

    public static boolean pseudoLegalMove(Tile tileFrom, Tile tileTo) {
        System.out.println("WHITE KING POSITION" + " " + WhiteKing.row() + " " + WhiteKing.column());
        System.out.println("BLACK KING POSITION" + " " + BlackKing.row() + " " + BlackKing.column());
        System.out.println("DEBUG " + tileFrom.row() + "  " + tileFrom.column());
        if (currentRound != tileFrom.getPiece().color){
            return false;
        }

        int FromRow = tileFrom.row();
        int FromColumn = tileFrom.column();

        int ToRow = tileTo.row();
        int ToColumn = tileTo.column();

        Piece movingPiece = tileFrom.getPiece();

        Piece CapturePiece = tileTo.getPiece();

        Tile kingTile;
        ArrayList<Piece> EnemyPiece;



        // if capture Piece is not null

        tileTo.setPiece(movingPiece);
        tileFrom.setPiece(null);
        movingPiece.row = ToRow;
        movingPiece.column = ToColumn;

        if (movingPiece.color == -1){
            kingTile = board[WhiteKing.row][WhiteKing.column];
            EnemyPiece = BlackPieces;
        } else{
            kingTile = board[BlackKing.row][BlackKing.column];
            EnemyPiece = WhitePieces;
        }

        if (CapturePiece != null){
            EnemyPiece.remove(CapturePiece);
        }
        boolean legal = true;
        for (Piece eachPiece : EnemyPiece){
            if (eachPiece.getAttackRadius().contains(kingTile)){
                legal = false;
                break;
            }
        }

        // undo everything
        tileTo.setPiece(CapturePiece);
        if (CapturePiece != null){
            EnemyPiece.add(CapturePiece);
        }


        tileFrom.setPiece(movingPiece);
        movingPiece.row = FromRow;
        movingPiece.column = FromColumn;


        Repaint();
        return legal;


    }

    public static void nextRound(){
        if (currentRound == -1){
            currentRound = 1;
        } else{
            currentRound = -1;
        }

        String color;
        if (currentRound == -1){
            color = "White";
        } else{
            color = "Black";
        }

        if (getCheckCount(currentRound).size() > 0){
            System.out.println(color + " King in check check amount = " + getCheckCount(currentRound).toString());
        } else{
            System.out.println(color + " King not in check");
        }

        Piece king;
        if (currentRound == -1){
            king = WhiteKing;
        } else{
            king = BlackKing;
        }




        if (isCheckMate((King) king)){
            System.out.println("CHECKMATE");
        }

        rotateBoard(gridPanel, currentRound);

    }

}

abstract class Piece{
    BufferedImage PieceImage;
    protected int row;
    protected int column;
    protected int color;


    public Piece(int row, int column, String imagePath, int color){
        try{
            PieceImage = ImageIO.read(new File(imagePath));
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        this.row = row;
        this.column = column;
        this.color = color;
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
        return PieceImage;
    }
    public abstract ArrayList<Tile> getAttackRadius();


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


class King extends Piece{
    public King(int row, int column, String imagePath, int color){
        super(row, column, imagePath, color);
    }

    @Override
    public ArrayList<Tile> getAttackRadius() {

        ArrayList<Tile> PossibleTile = new ArrayList<>();

        int[] offset = {-1, 0, 1};

        for (int dy : offset){
            for (int dx : offset){
                if (dy == 0 && dx == 0){
                    continue;
                } else{
                    if (row() + dy <= 7 && row() + dy >= 0 && column() + dx <= 7 && column() + dx >= 0){
                        Tile tile = Main.board[row() + dy][column() + dx];
                        PossibleTile.add(Main.board[row() + dy][column() + dx]);
                    }

                }
            }
        }
        return PossibleTile;
    }

}


class Rook extends Piece{
    public Rook(int row, int column, String imagePath, int color){
        super(row, column, imagePath, color);
    }


    public ArrayList<Tile> getAttackRadius(){
        ArrayList<Tile> PossibleTile = new ArrayList<>();
        PossibleTile.addAll(helperRadius(1,0));
        PossibleTile.addAll(helperRadius(-1, 0));
        PossibleTile.addAll(helperRadius(0, 1));
        PossibleTile.addAll(helperRadius(0, -1));

        return PossibleTile;
    }
    // make sure to update the piece new position
    @Override
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


class Bishop extends Piece{

    public Bishop(int row, int column, String imagePath, int color) {
        super(row, column, imagePath, color);
    }

    @Override
    public ArrayList<Tile> getAttackRadius() {
        ArrayList<Tile> PossibleTile = new ArrayList<>();

        PossibleTile.addAll(helperRadius(1, 1));
        PossibleTile.addAll(helperRadius(1, -1));
        PossibleTile.addAll(helperRadius(-1, 1));
        PossibleTile.addAll(helperRadius(-1, -1));

        return PossibleTile;
    }

}

class Knight extends Piece{

    public Knight(int row, int column, String imagePath, int color) {
        super(row, column, imagePath, color);
    }

    @Override
    public ArrayList<Tile> getAttackRadius() {
        ArrayList<Tile> PossibleTile = new ArrayList<>();

        int[] diff = {-2, -1, 1, 2};

        for (int dy : diff){
            for (int dx : diff){
                int absX = Math.abs(dx);
                int absY = Math.abs(dy);
                if (absX != absY && row() + dy <= 7 && row() + dy >= 0 && column() + dx <= 7 && column() + dx >= 0){
                    PossibleTile.add(Main.board[row() + dy][column() + dx]);
                }
            }
        }
        return PossibleTile;

    }


}

class Queen extends Piece{

    public Queen(int row, int column, String imagePath, int color) {
        super(row, column, imagePath, color);
    }

    @Override
    public ArrayList<Tile> getAttackRadius() {
        ArrayList<Tile> PossibleTile = new ArrayList<>();

        PossibleTile.addAll(helperRadius(1, 1));
        PossibleTile.addAll(helperRadius(1, -1));
        PossibleTile.addAll(helperRadius(-1, 1));
        PossibleTile.addAll(helperRadius(-1, -1));
        PossibleTile.addAll(helperRadius(1,0));
        PossibleTile.addAll(helperRadius(-1, 0));
        PossibleTile.addAll(helperRadius(0, 1));
        PossibleTile.addAll(helperRadius(0, -1));

        return PossibleTile;
    }

}

class Pawn extends Piece{

    private boolean doubleMove = false;


    public Pawn(int row, int column, String imagePath, int color) {
        super(row, column, imagePath, color);
    }

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
    public ArrayList<Tile> getAttackRadius() {
        ArrayList<Tile> PossiblePath = new ArrayList<>();
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
                if (row <= 7 && row >= 0){
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

class Debugger {

    public static void printBoardState() {
        System.out.println("=== Current Board State ===");
        for (int r = 0; r < Main.board.length; r++) {
            for (int c = 0; c < Main.board[r].length; c++) {
                Piece piece = Main.board[r][c].getPiece();
                if (piece == null) {
                    System.out.print("-- ");
                } else {
                    String colorCode = (piece.color == -1) ? "W" : "B";
                    String typeCode = piece.getClass().getSimpleName().substring(0, 1);
                    System.out.print(colorCode + typeCode + " ");
                }
            }
            System.out.println();
        }
        System.out.println("===========================");
    }
}