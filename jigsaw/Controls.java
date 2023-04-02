package jigsaw;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class Controls extends MouseAdapter{

    GroupOfPieces activeGroup;
    Move move;
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e){
        if (e.getPreciseWheelRotation()>0 && PuzzleMain.table.resize.getScaleX()>0.8){
            PuzzleMain.table.resize.setToScale(PuzzleMain.table.resize.getScaleX()-0.1, PuzzleMain.table.resize.getScaleY()-0.1);
        } else if (e.getPreciseWheelRotation()<0 && PuzzleMain.table.resize.getScaleX()<1.2){
            PuzzleMain.table.resize.setToScale(PuzzleMain.table.resize.getScaleX()+0.1, PuzzleMain.table.resize.getScaleY()+0.1);
        }
        PuzzleMain.table.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e){
        if (!PuzzleMain.table.inGame || PuzzleMain.table.groups == null || PuzzleMain.table.groups.size()<1) return;
        if (e.getButton()==1){
            outerloop:
            for(int g = PuzzleMain.table.groups.size()-1; g >= 0; g--){
                for(int i = 0; i < PuzzleMain.table.groups.get(g).getGroupMembers().length; i++){
                    int mouseX = (int)(e.getX());
                    int mouseY = (int)(e.getY());
                    //UpLeftCorner arent't the piece's real coordinates, becouse of the "ears" and ARGB Mode(see PieceMaker).
                    int pieceX = (int)((PuzzleMain.table.groups.get(g).getGroupMembers()[i].getUpLeftCornerPlace().width)*PuzzleMain.table.resize.getScaleX());
                    int pieceY = (int)((PuzzleMain.table.groups.get(g).getGroupMembers()[i].getUpLeftCornerPlace().height)*PuzzleMain.table.resize.getScaleY());
                    int pxSize = (int)(GroupOfPieces.getPieceSize().width*PuzzleMain.table.resize.getScaleX());
                    int pySize = (int)(GroupOfPieces.getPieceSize().height*PuzzleMain.table.resize.getScaleY());
                    if(mouseX >= pieceX && mouseX <= pieceX + pxSize && mouseY >= pieceY && mouseY <= pieceY + pySize){
                        activeGroup = PuzzleMain.table.groups.get(g);
                        move = new Move(activeGroup);
                        //set actual group to the top, from here:
                        PuzzleMain.table.groups.add(activeGroup);  
                        PuzzleMain.table.groups.remove(g);
                        move.start();
                        break outerloop; //This is for only one piece/group.
                    }
                }
            }
        } else if(e.getButton()==3){
            PuzzleMain.table.showEntirePicture = true;
            PuzzleMain.table.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
        if(e.getButton()==1){
            if (move != null){
                for(Piece p: activeGroup.getGroupMembers()){
                    neighbourChecker(p);
                }
                move.putDown();
                move = null;
                System.gc();
            }
        }else if(e.getButton()==3){
            PuzzleMain.table.showEntirePicture = false;
            PuzzleMain.table.repaint();
        }
    }


    //to find all nieghbours of the members of the activegroup
    void neighbourChecker(Piece activePiece){
        outerloop:
        for(int g = PuzzleMain.table.groups.size()-1; g>=0; g--){
            if(PuzzleMain.table.groups.get(g).equals(activeGroup)) continue;
            for(Piece p: PuzzleMain.table.groups.get(g).getGroupMembers()){
                if(p.getX_Index() == activePiece.getX_Index()){
                    if(p.getY_Index() == activePiece.getY_Index()-1){
                        if(match(activePiece, p, 0, -1, PuzzleMain.table.groups.get(g)))continue outerloop;
                        } else if(p.getY_Index() == activePiece.getY_Index()+1){
                        if(match(activePiece, p, 0, 1, PuzzleMain.table.groups.get(g)))continue outerloop;
                    }
                }
                if(p.getY_Index() == activePiece.getY_Index()){
                    if(p.getX_Index() == activePiece.getX_Index()-1){
                        if(match(activePiece, p, -1, 0, PuzzleMain.table.groups.get(g)))continue outerloop;
                    } else if(p.getX_Index() == activePiece.getX_Index()+1){
                        if(match(activePiece, p, 1, 0, PuzzleMain.table.groups.get(g)))continue outerloop;
                    }
                }         
            }
        }
    }

    //to find if neighbours fit
    boolean match(Piece a, Piece b, int xDirection, int yDirection, GroupOfPieces otherGroup){
        int difX = b.getPlace().width - (a.getPlace().width + xDirection*GroupOfPieces.getPieceSize().width);
        int difY = b.getPlace().height - (a.getPlace().height + yDirection*GroupOfPieces.getPieceSize().height);
        final int LOOSEX = 7; // one can miss that number of pixels
        final int LOOSEY = 7;
        if(Math.abs(difX)<=LOOSEX && Math.abs(difY)<=LOOSEY){
            for(Piece p: activeGroup.getGroupMembers()){
                p.setPlace(difX, difY); // fit the active group correctly
            }
            activeGroup.addGroupMembersList(otherGroup.getGroupMembers());
            PuzzleMain.table.groups.remove(otherGroup);
            return true;
        }
        return false;
    }
}
