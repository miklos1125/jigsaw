package jigsaw;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

class PieceMaker{
    BufferedImage image;
    int xNum, yNum;
    int pic_xSize, pic_ySize;
    int pieceSizeX, pieceSizeY;
    ImageObserver imo;

    public PieceMaker(BufferedImage image, int x, int y, ImageObserver imo){
        this.image = image;
        this.xNum = x;
        this.yNum = y;
        pic_xSize = image.getWidth();
        pic_ySize = image.getHeight();
        pieceSizeX = pic_xSize/xNum;
        pieceSizeY = pic_ySize/yNum;
        this.imo = imo;
    }
       
    public ArrayList<GroupOfPieces> getParts(){
        int[][][] ears = new int[xNum][yNum][4];
        for (int y=0; y<yNum; y++){
            for(int x =0; x<xNum; x++){
                if(y == 0){
                    ears[x][y][0] = 0; //north
                } else{
                    ears[x][y][0] = ears[x][y-1][2];
                }
                if(x == 0){
                    ears[x][y][3] = 0; //west
                } else{
                    ears[x][y][3] =  ears[x-1][y][1];
                }
                if(y == yNum-1){
                    ears[x][y][2] = 0; //south
                } else{
                    ears[x][y][2] = ((int)(Math.random()*10))%2 == 0 ? -1 : 1;
                }
                if(x == xNum-1){
                    ears[x][y][1] = 0; //east
                } else {
                    ears[x][y][1] = ((int)(Math.random()*10))%2 == 0 ? -1 : 1;
                }
            }
        }
        
        ArrayList<GroupOfPieces> parts = new ArrayList<>();
        for (int y = 0; y<yNum; y++){
            for (int x = 0; x<xNum; x++){
                Image temp = pieceMaker(ears[x][y], x, y);
                GroupOfPieces part;
                if (x == 0 && y == 0){
                    part = new GroupOfPieces(temp, x, y, pieceSizeX, pieceSizeY);
                } else{
                    part = new GroupOfPieces(temp, x, y);}
                parts.add(part);
            }
        }
        return parts;
    }
       
    private Image pieceMaker(int[] sides, int xCount, int yCount){
        if(sides.length!=4){
            throw new IllegalArgumentException();
        }
        Shape s = shapeMaker(pathMaker(sides[0], sides[1], sides[2], sides[3]));
        Area a = new Area(s);
        int additionalSizeX = (int)(pieceSizeX*PuzzleMain.ADDITIONAL_SIZE_MOD);
        int additionalSizeY = (int)(pieceSizeY*PuzzleMain.ADDITIONAL_SIZE_MOD);
        int borderX = (int)(pieceSizeX*PuzzleMain.EARS_MODIFIER);
        int borderY = (int)(pieceSizeY*PuzzleMain.EARS_MODIFIER);
        BufferedImage pieceGraph = new BufferedImage(additionalSizeX, additionalSizeY, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) pieceGraph.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(borderX, borderY);
        g.setClip(a);
        g.drawImage(image, -xCount*pieceSizeX, -yCount*pieceSizeY, imo);
        //g.setStroke(new BasicStroke(2f));
        //g.draw(s);
        g.dispose();
        return pieceGraph;
    }
    
    private Shape shapeMaker(GeneralPath p){
        AffineTransform transform = new AffineTransform();
        transform.setToScale(pieceSizeX, pieceSizeY);
        Shape shape = transform.createTransformedShape(p);
        return shape;
    }

    private GeneralPath pathMaker(int north, int east, int south, int west){       
        if (north<-1 || north>1 ||east<-1 || east>1 || 
                south<-1 || south>1 || west<-1 || west>1){
            throw new IllegalArgumentException();
        }
        
        GeneralPath path = new GeneralPath();
        path.moveTo(0, 0);
        if(north!=0){
            path.lineTo(0.375, 0);
            path.lineTo(0.375, north*0.125);
            path.quadTo(0.250, north*0.250, 0.500, north*0.300);
            path.quadTo(0.750, north*0.250, 0.625, north*0.125);
            path.lineTo(0.625, 0);
        }
        path.lineTo(1.000, 0);
        if(east!=0){
            path.lineTo(1.000, 0.375);
            path.lineTo(1.000+east*0.125, 0.375);
            path.quadTo(1.000+east*0.250, 0.250, 1.000+east*0.300, 0.500);
            path.quadTo(1.000+east*0.250, 0.750, 1.000+east*0.125, 0.625);
            path.lineTo(1.000, 0.625);
        }
        path.lineTo(1.000, 1.000);
        if(south!=0){    
            path.lineTo(0.625, 1.000);
            path.lineTo(0.625, 1.000+south*0.125);
            path.quadTo(0.750, 1.000+south*0.250, 0.500, 1.000+south*0.300);
            path.quadTo(0.250, 1.000+south*0.250, 0.375, 1.000+south*0.125);
            path.lineTo(0.375, 1.000);
        }
        path.lineTo(0, 1.000);
        if(west!=0){
            path.lineTo(0, 0.625);
            path.lineTo(west*0.125, 0.625);
            path.quadTo(west*0.250, 0.750, west*0.300, 0.500);
            path.quadTo(west*0.250, 0.250, west*0.125, 0.375);
            path.lineTo(0, 0.375);
        }
        path.closePath();
        return path;
    }
}

