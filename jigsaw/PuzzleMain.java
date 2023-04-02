package jigsaw;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class PuzzleMain {
    
    static int SW = Toolkit.getDefaultToolkit().getScreenSize().width;
    static int SH = Toolkit.getDefaultToolkit().getScreenSize().height;
    static final double EARS_MODIFIER = 0.3;
    static final double ADDITIONAL_SIZE_MOD = 1.6;
    static final double SCALE_DOWN_PICTURE = 0.9;
    static Table table;
    static BufferedImage picture;
    static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame("Jigsaw Puzzle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setLayout(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        setupTable();
        setupMenu();
        frame.setVisible(true);
        setupDialog();
    }
    
    static void setupTable(){
        table = new Table();
        frame.setContentPane(table);
    }
    
    static void setupDialog(){
        JDialog dialog = new JDialog(frame, "setup", true);
        dialog.setLayout(null);
        dialog.setResizable(false);
        dialog.setBounds(SW/2-150,SH/2-100,360,200);
        dialog.getContentPane().setBackground(new Color(170,180,250));
        JTextField field = new JTextField();
        field.setBounds(10, 20, 320, 20);
        field.setEditable(false);
        dialog.add(field);
        JButton openFile = new JButton("Open file");
        openFile.setBounds(30, 50, 80, 40);
        dialog.add(openFile);
        openFile.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                JFileChooser fileChoose = new JFileChooser();
                FileFilter filter = new FileFilter(){
                    @Override
                    public boolean accept(File file){
                        if (file.isDirectory()) return true;
                        int i = file.getName().length();
                        String end = file.getName().substring(i-3, i);
                        switch (end){
                            case "jpg":
                            case "png":
                            case "gif":
                            case "bmp": return true;
                            default: return false;
                        }                        
                    }

                    @Override
                    public String getDescription() {
                        String s = "Image files";
                        return s;
                    }
                };
                fileChoose.setFileFilter(filter);
                int returnValue = fileChoose.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION){
                    File f = fileChoose.getSelectedFile();
                    field.setText(f.getAbsolutePath());
                }
            }
        });
        JButton start = new JButton("Start");
        start.setBounds(120, 110, 120, 40);
        dialog.add(start);

        JLabel label = new JLabel("Size:");
        label.setBounds(170,50,40,40);
        dialog.add(label);
        JLabel labelX = new JLabel("X");
        labelX.setBounds(247,50,10,40);
        dialog.add(labelX);
        SpinnerModel value1 = new SpinnerNumberModel(5,2,12,1);
        JSpinner spinner1 = new JSpinner(value1);
        spinner1.setBounds(200,50,40,40);
        dialog.add(spinner1);
        SpinnerModel value2 = new SpinnerNumberModel(5,2,12,1);
        JSpinner spinner2 = new JSpinner(value2);
        spinner2.setBounds(260,50,40,40);
        dialog.add(spinner2);
        start.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                File f = new File(field.getText());
                loadImage(f);
                    if (picture != null){
                        table.startGame((Integer)spinner1.getValue(), (Integer)spinner2.getValue(), picture);
                        dialog.dispose();
                    }
   
            }
        });
        dialog.setVisible(true);
    }
    
    static void setupMenu(){
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem restart = new JMenuItem("Start new game");
        JMenuItem save = new JMenuItem("Save");
        menu.add(restart);
        restart.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                int selection = JOptionPane.showConfirmDialog(frame, "Are you sure?", "STARTING A NEW GAME", JOptionPane.YES_NO_OPTION);
                if(selection == JOptionPane.YES_OPTION){
                    table.restart();
                    setupDialog();
                }
            }
        });
        menu.add(save);
        save.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                JOptionPane.showMessageDialog(null, "Saving the game is not supported yet.");
            }
        });
        mb.add(menu);
        frame.setJMenuBar(mb);
    }
    
    static void loadImage(File f){
        picture = null;
        try{
            Image image;
            picture = ImageIO.read(f);
            if (picture == null) return;
            double scaleScreen = (double)SW/(double)SH;
            double scalePic = (double)picture.getWidth()/(double)picture.getHeight();
            if (scaleScreen<=scalePic){
                if (picture.getWidth()>=SW*SCALE_DOWN_PICTURE){
                   image = picture.getScaledInstance((int)(SW*SCALE_DOWN_PICTURE), (int)((SW*SCALE_DOWN_PICTURE)/scalePic), Image.SCALE_DEFAULT);
                   picture = fitToScreen(image);
                }
            } else{
                if(picture.getHeight()>=SH*SCALE_DOWN_PICTURE){
                    image = picture.getScaledInstance((int)(SH*SCALE_DOWN_PICTURE*scalePic), (int)(SH*SCALE_DOWN_PICTURE), Image.SCALE_DEFAULT);
                    picture = fitToScreen(image);
                }
            } 
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    static BufferedImage fitToScreen(Image img){
        BufferedImage bimRet;
        bimRet = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bimRet.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return bimRet;
    }
}
