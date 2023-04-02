package jigsaw;

import java.awt.MouseInfo;

//to move the pices dragged by the mouse
    public class Move extends Thread{

        boolean go;
        GroupOfPieces gp;
        
        
        Move (GroupOfPieces gp){
            this.gp = gp;
        }

        @Override
        public void run(){
            go = true;
            int prevMouseX = MouseInfo.getPointerInfo().getLocation().x;
            int prevMouseY = MouseInfo.getPointerInfo().getLocation().y;
            while (go){
                double mouseMotionX = - prevMouseX + (prevMouseX = MouseInfo.getPointerInfo().getLocation().x); //using value, and giving it to prevMouse at the same time
                double mouseMotionY = - prevMouseY + (prevMouseY = MouseInfo.getPointerInfo().getLocation().y); //to prevent delay in motion.
                for(int i = 0; i<gp.getGroupMembers().length; i++){
                    gp.getGroupMembers()[i].setPlace((int)(mouseMotionX/PuzzleMain.table.resize.getScaleX()), (int)(mouseMotionY/PuzzleMain.table.resize.getScaleY()));
                }
                PuzzleMain.table.repaint();
            }
        }

        void putDown(){
            go = false;
        }
    }
