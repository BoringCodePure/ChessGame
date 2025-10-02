import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import javax.imageio.ImageIO;
import javax.swing.*;



class Main implements Serializable {
    private static GameState gameState = new GameState();
    public static boolean underPromotion;
    public static ArrayList<Piece> WhitePieces = gameState.getWhitePieces();
    public static ArrayList<Piece> BlackPieces = gameState.getBlackPieces();
    public static Piece WhiteKing = gameState.getWhiteKing();
    public static Piece BlackKing = gameState.getBlackKing();
    private static final int ROWS = 8;
    private static final int COLS = 8;
    public static Tile[][] board = new Tile[ROWS][COLS];

    public static void main(String[] args) throws IOException {

        // Always start Swing on the Event Dispatch Thread
        SwingUtilities.invokeLater(Main::createAndShowUI);

    }
    private static void createAndShowUI() {


        JFrame frame = new JFrame("6x6 Button Grid");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel MainPanel = new JPanel(new BorderLayout());
        JPanel gridPanel = new JPanel(new GridLayout(ROWS, COLS, 0, 0));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        MainPanel.add(gridPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        MainPanel.add(buttonPanel, BorderLayout.WEST);
        @SuppressWarnings("unused")
        String[] textArray = {"Save Game", "Reset Game", "Open Game"};
        buttonPanel.add(Box.createVerticalGlue());
        JButton button = new JButton("Restart Game");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                emptyBoard();
                restartGame(new GameState());
            }
        });
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(150, 50)); // width x height
        button.setMaximumSize(new Dimension(150, 50)); // prevent resizing
        button.setBackground(new Color(113, 183, 255));
        button.setFocusPainted(false);
        buttonPanel.add(button);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15))); // space between buttons



        JButton SaveGame = new JButton("Save Game");
        SaveGame.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                if (!underPromotion){
                    JFileChooser fileChooser = new JFileChooser();
                    int state = fileChooser.showSaveDialog(null);

                    if (state == JFileChooser.APPROVE_OPTION) {
                        String selectedFile = fileChooser.getSelectedFile().getAbsolutePath();

                        try (FileOutputStream fileStream = new FileOutputStream(selectedFile + ".ser");
                             ObjectOutputStream outputStream = new ObjectOutputStream(fileStream)) {
                            //    System.out.println("Saving game state to " + selectedFile + ".ser");

                            outputStream.writeObject(gameState);

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
        SaveGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        SaveGame.setPreferredSize(new Dimension(150, 50));
        SaveGame.setMaximumSize(new Dimension(150, 50));
        SaveGame.setBackground(new Color(113, 183, 255));
        SaveGame.setFocusPainted(false);
        buttonPanel.add(SaveGame);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15))); // space between buttons

        JButton OpenGame = new JButton("Open Game");
        OpenGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!underPromotion){
                    JFileChooser fileChooser = new JFileChooser();
                    int state = fileChooser.showOpenDialog(null);

                    if (state == JFileChooser.APPROVE_OPTION) {
                        String selectedFile = fileChooser.getSelectedFile().getAbsolutePath();

                        FileInputStream fileStream = null;
                        try {
                            fileStream = new FileInputStream(selectedFile);
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            ObjectInputStream outputStream = new ObjectInputStream(fileStream);
                            GameState data = (GameState) outputStream.readObject();
                            emptyBoard();
                            restartGame(data);
                        } catch (IOException | ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });
        OpenGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        OpenGame.setPreferredSize(new Dimension(150, 50)); // width x height
        OpenGame.setMaximumSize(new Dimension(150, 50)); // prevent resizing
        OpenGame.setBackground(new Color(113, 183, 255));
        OpenGame.setFocusPainted(false);
        buttonPanel.add(OpenGame);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15))); // space between buttons

        buttonPanel.add(Box.createVerticalGlue());

        startGame(gridPanel);

        frame.add(MainPanel);
        frame.pack();
        frame.setSize(1100, 1000);// size to fit contents
        frame.setLocationRelativeTo(null); // center on screen
        frame.setVisible(true);
    }

    private static void startGame(JPanel gridPanel){
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Tile btn = new Tile(r, c);
                board[r][c] = btn;
                gridPanel.add(btn);
            }
        }


        for (Piece eachPiece : WhitePieces){
            board[eachPiece.row()][eachPiece.column()].setPiece(eachPiece);
        }
        for (Piece eachPiece : BlackPieces){
            board[eachPiece.row()][eachPiece.column()].setPiece(eachPiece);
        }

    }

    private static void restartGame(GameState new_gameState){
        gameState = new_gameState;
        WhitePieces = gameState.getWhitePieces();
        BlackPieces = gameState.getBlackPieces();
        WhiteKing = gameState.getWhiteKing();
        BlackKing  = gameState.getBlackKing();


        for (Piece eachPiece : WhitePieces){
            board[eachPiece.row()][eachPiece.column()].setPiece(eachPiece);
        }
        for (Piece eachPiece : BlackPieces){
            board[eachPiece.row()][eachPiece.column()].setPiece(eachPiece);
        }

        Repaint();
    }


    @SuppressWarnings("unused")
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
    public static boolean isStaletMate(King king){
        ArrayList<Piece> Pieces;
        if (king.color == -1){
            Pieces = WhitePieces;
        } else{
            Pieces = BlackPieces;
        }

        for (Tile eachTile : king.getAttackRadius()){
            if (king.CanBeMovedTo(eachTile.row(), eachTile.column())){
                return false;
            }
        }
        // king has no safe square.
        // Is there any piece that can move in its attack radius?

        for (Piece eachPiece : Pieces){
            for (Tile eachTile : eachPiece.getAttackRadius()){
                if (eachPiece.CanBeMovedTo(eachTile.row(), eachTile.column())){
                    return false;
                }
            }
        }
        return true;
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


        //  System.out.println("CHECK COUNT = " + checkCount);

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
            return !SafeSquare;
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
        // given that king has no safe square and the piece that is checking is knight or pawn, then the only way is to capture the attacking pieces
        if (attack instanceof Knight || attack instanceof Pawn){
            for (Piece eachPiece : myPiece){
                if (eachPiece.CanBeMovedTo(attack.row(), attack.column())){
                    return false;
                }
            }
        }

        return true;
    }

    private static HashSet<Tile> getCheckSight(Piece attacker, King king){

        HashSet<Tile> path = new HashSet<Tile>();

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

    private static HashSet<Tile> CheckSightHelper(Piece attacker, Piece king, int dr, int dc){


        HashSet<Tile> path = new HashSet<Tile>();
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

    public static void AllowMove(Tile from, Tile to, boolean IsCastle){
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

        if (!IsCastle){
            nextRound();
        }
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
                eachTile.setBackground(eachTile.getOriginalBackground());
                eachTile.componentResized(null);
            }
        }
    }

    public static boolean pseudoLegalMove(Tile tileFrom, Tile tileTo) {

        //    System.out.println(gameState.getCurrentRound());
        if (gameState.getCurrentRound() != tileFrom.getPiece().color){
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
        if (gameState.getCurrentRound() == -1){
            gameState.setCurrentRound(1);
        } else{
            gameState.setCurrentRound(-1);
        }

        Piece king;
        if (gameState.getCurrentRound() == -1){
            king = WhiteKing;
        } else{
            king = BlackKing;
        }

        if (isCheckMate((King) king)){
            System.out.println("CHECKMATE");
        } else{
            if (isStaletMate((King) king)){
                System.out.println("StaleMate");
            }
        }

        //   System.out.println(ChessConsolePrinter.printBoard(Main.board));

    }
    public static void emptyBoard(){
        WhitePieces = null;
        BlackPieces = null;
        for (Tile[] eachRow : board){
            for (Tile eachTile : eachRow){
                eachTile.setPiece(null);
            }
        }
    }
}



// try to implement socket programming and multithread for project based

