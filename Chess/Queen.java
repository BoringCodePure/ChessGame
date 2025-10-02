import java.util.HashSet;

class Queen extends Piece{

    public Queen(int row, int column, String imagePath, int color) {
        super(row, column, imagePath, color);
    }

    @Override
    public HashSet<Tile> getAttackRadius() {
        HashSet<Tile> PossibleTile = new HashSet<Tile>();
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