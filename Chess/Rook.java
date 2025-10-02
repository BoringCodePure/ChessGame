import java.util.HashSet;

class Rook extends Piece{

    protected boolean hasMoved = false;

    public Rook(int row, int column, String imagePath, int color){
        super(row, column, imagePath, color);
    }

    @Override
    public void updatePosition(int newrow, int newcolumn){
        hasMoved = true;
        super.updatePosition(newrow, newcolumn);
    }

    public HashSet<Tile> getAttackRadius(){
        HashSet<Tile> PossibleTile = new HashSet<>();
        PossibleTile.addAll(helperRadius(1,0));
        PossibleTile.addAll(helperRadius(-1, 0));
        PossibleTile.addAll(helperRadius(0, 1));
        PossibleTile.addAll(helperRadius(0, -1));

        return PossibleTile;
    }

}