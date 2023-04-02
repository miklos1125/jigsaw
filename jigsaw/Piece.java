package jigsaw;

import java.awt.Dimension;
import java.awt.Image;
import static jigsaw.GroupOfPieces.PIECESIZE_X;
import static jigsaw.GroupOfPieces.PIECESIZE_Y;

public class Piece {
    Image image;
    private final int X_INDEX, Y_INDEX;
    private int placeX, placeY;

    Piece(Image img, int index_X, int index_Y){
        image = img;
        X_INDEX = index_X;
        Y_INDEX = index_Y;
        placeX = (int)(Math.random()*(PuzzleMain.SW-PIECESIZE_X*PuzzleMain.ADDITIONAL_SIZE_MOD));
        placeY = (int)(Math.random()*(PuzzleMain.SH-PIECESIZE_Y*PuzzleMain.ADDITIONAL_SIZE_MOD));
    }

    public int getX_Index(){
        return X_INDEX;
    }

    public int getY_Index(){
        return Y_INDEX;
    }

    public Dimension getPlace(){
        return new Dimension(placeX, placeY);
    }

    public Dimension getUpLeftCornerPlace(){
        return new Dimension((int)(placeX + PuzzleMain.EARS_MODIFIER*PIECESIZE_X), (int)(placeY + PuzzleMain.EARS_MODIFIER*PIECESIZE_Y));
    }

    public void setPlace(int x, int y){
        placeX += x;
        placeY += y;
    } 
}

