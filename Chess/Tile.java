import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;

class Tile extends JButton implements ComponentListener, MouseListener{
    private final int row;
    private final int column;
    private Piece piece;
    private Color backGroundColor = null;

    public Tile(int row, int column){
        this.row = row;
        this.column = column;

        if (row % 2 == column % 2){
            backGroundColor = new Color(234, 221, 202);

        } else{
            backGroundColor = new Color(92, 64, 51);
        }

        this.setBackground(backGroundColor);
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

    public Color getOriginalBackground(){
        return this.backGroundColor;
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
            }
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
                Main.Repaint();
                return;
            }
            if (Mouse.PlayerPiece != null && Mouse.TargetTile != null){

                // move piece
                if (Mouse.PlayerPiece.CanBeMovedTo(Mouse.TargetTile.row(), Mouse.TargetTile.column())){
                    Main.AllowMove(this, Mouse.TargetTile, false);
                    Mouse.PlayerPiece = null;
                    //   System.out.println("MOve");

                
                }
            }
            Main.Repaint();
            Mouse.PlayerPiece = null;
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