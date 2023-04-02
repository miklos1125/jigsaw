package jigsaw;

import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;

public class GroupOfPieces {
    
    static int PIECESIZE_X, PIECESIZE_Y;  //meant to be unchangeble during one game
                                          //equals the standard size, without the "ears".
    private ArrayList<Piece> myPieces;

    GroupOfPieces(Image img, int firstPieceIndex_X, int firstPieceIndex_Y, int psx, int psy){
        this(img, firstPieceIndex_X, firstPieceIndex_Y);
        PIECESIZE_X = psx;
        PIECESIZE_Y = psy;
              
    }
    
    GroupOfPieces(Image img, int firstPieceIndex_X, int firstPieceIndex_Y){
        myPieces = new ArrayList<>();
        myPieces.add(new Piece(img, firstPieceIndex_X, firstPieceIndex_Y));
    }
    
    static Dimension getPieceSize(){
        return new Dimension(PIECESIZE_X, PIECESIZE_Y);
    }
    
    public void addGroupMember(Piece p){
            myPieces.add(p);
    }
    
    public void addGroupMembersList(Piece[] pArray){
        myPieces.addAll(myPieces.size(), Arrays.asList(pArray));
    }
    
    public Piece[] getGroupMembers(){
        Piece[] p = new Piece[myPieces.size()];
        return myPieces.toArray(p);
    }
    
    public void removeGroupMember(Piece p){
        myPieces.remove(p);
    }
    
    public void removeAllGroupMembers(){
        myPieces.clear();
    }
       
}
