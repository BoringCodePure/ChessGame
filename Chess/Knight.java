import java.util.HashSet;

class Knight extends Piece{

    public Knight(int row, int column, String imagePath, int color) {
        super(row, column, imagePath, color);
    }

    @Override
    public HashSet<Tile> getAttackRadius() {
        HashSet<Tile> PossibleTile = new HashSet<Tile>();

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