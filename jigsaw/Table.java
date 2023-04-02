package jigsaw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Table extends JPanel{
    
    ArrayList<GroupOfPieces> groups;
    AffineTransform resize, standard;
    boolean inGame, showEntirePicture;
    Controls mouse;
    
    public Table(){
        super(null, true);  //(LayoutManager, DoubleBuffered);
        setBackground(new Color(70,70,70));
        resize = new AffineTransform();
        standard = new AffineTransform();
        resize.setToScale(1, 1);
        standard.setToScale(1,1);
    }
    
    public void startGame(int xNum, int yNum, BufferedImage bim){
        PieceMaker pm = new PieceMaker(bim, xNum, yNum, this);
        groups = pm.getParts();
        inGame = true;
        showEntirePicture = false;
        mouse = new Controls();
        this.addMouseWheelListener(mouse);
        this.addMouseListener(mouse);
        repaint();
    }
    
    public void restart(){
        inGame = false;
        if (groups != null) groups.clear();
        this.removeMouseListener(mouse);
        this.removeMouseWheelListener(mouse);
        setBackground(new Color(70,70,70));
        resize.setToScale(1, 1);
        standard.setToScale(1,1);
        repaint();
    }

    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D graph2 = (Graphics2D) graphics;
        graph2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graph2.setTransform(resize);
        if(inGame && !showEntirePicture){
            for(GroupOfPieces g: groups){
                for(Piece p : g.getGroupMembers()){
                    graph2.translate(p.getPlace().width, p.getPlace().height);
                    graph2.drawImage(p.image, 0, 0, this);
                    graph2.translate(-p.getPlace().width,  -p.getPlace().height);
                }
            }
        } else if (inGame && showEntirePicture){
            graph2.setTransform(standard);
            graph2.drawImage(PuzzleMain.picture, PuzzleMain.SW/2-PuzzleMain.picture.getWidth()/2, this.getHeight()/2-PuzzleMain.picture.getHeight()/2, this);  
        }
    }
}
