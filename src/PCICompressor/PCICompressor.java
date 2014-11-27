package pci.compressor;

import static pci.compressor.Algorithms.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class PCICompressor {

    static BufferedImage bi;
    
    static StringBuilder horsb = new StringBuilder();
    static String hor = "";
    static StringBuilder vertsb = new StringBuilder();
    static String vert = "";
    static StringBuilder horswsb = new StringBuilder();
    static String horsw = "";
    static StringBuilder vertswsb = new StringBuilder();
    static String vertsw = "";
    
    static BufferedImage buff;
    static BufferedImage text;
    static BufferedImage cover;
    static Graphics coverg;
    static boolean VISUALMODE = false;
    static boolean SLEEP = false;
    static JFrame frame = null;
    static int mx = 0;
    static int my = 0;
    
    static boolean ADone = false;
    static boolean BDone = false;
    static boolean CDone = false;
    static boolean DDone = false;
    
    public static void main(String[] args) throws Throwable {  
        JFileChooser jfc = new JFileChooser();
        jfc.showOpenDialog(null);
        bi = ImageIO.read(jfc.getSelectedFile());
        
        if(VISUALMODE){
            frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(bi.getWidth(), bi.getHeight());
            frame.setVisible(true);
            buff = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
            text = new BufferedImage(256, 32, BufferedImage.TYPE_INT_RGB);
            cover = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
            coverg = cover.getGraphics();
            coverg.setColor(new Color(0, 0, 0, 0));
            
            new Thread(){
                public void run(){
                    while(true){
                        try{
                            Graphics g = buff.getGraphics();
                            Graphics f = text.getGraphics();
                            g.drawImage(bi, 0, 0, null);
                            g.drawImage(cover, 0, 0, null);
                            g.setColor(Color.WHITE);
                            g.fillRect(mx, my, 1, 1);
                            frame.getGraphics().drawImage(buff, 0, 0, frame.getWidth(), frame.getHeight()-64, null);
                            f.setColor(Color.WHITE);
                            f.fillRect(0, 0, text.getWidth(), text.getHeight());
                            f.setColor(Color.red);
                            f.drawString("H:"+horsb.length()+" V:"+vertsb.length()+" HS:"+horswsb.length()+" VS:"+vertswsb.length(),0,16);
                            frame.getGraphics().drawImage(text, 0, frame.getHeight()-64, frame.getWidth(), 64, null);
                        }catch(Throwable t){break;}
                    }
                }
            }.start();
        }
        
        long start = System.currentTimeMillis();
        
        //TEST HORIZONTAL - A
        new Thread(){
            public void run(){
                AlgA();
            }
        }.start();
        if(VISUALMODE)
            while(!ADone) Thread.yield();
        refreshVisMode();
       
        //TEST VERTICAL - B
        new Thread(){
            public void run(){
                AlgB();
            }
        }.start();
        if(VISUALMODE)
            while(!BDone) Thread.yield();
        refreshVisMode();
        
        //TEST HORIZONTAL SWAPPING - C
        new Thread(){
            public void run(){
                AlgC();
            }
        }.start();
        if(VISUALMODE)
            while(!CDone) Thread.yield();
        refreshVisMode();
        
        //TEST VERTICAL SWAPPING - D
        new Thread(){
            public void run(){
                AlgD();
            }
        }.start();
        if(VISUALMODE)
            while(!DDone) Thread.yield();
        refreshVisMode();

        while(!(ADone&&BDone&&CDone&&DDone)) Thread.yield();
        System.out.println(System.currentTimeMillis() - start);
        
        int[] fina = {vert.length(),hor.length(),vertsw.length(),horsw.length()};
        Arrays.sort(fina);
        String fin = vert.length() == fina[0] ? vert : (hor.length() == fina[0] ? hor : (horsw.length() == fina[0] ? horsw : vertsw));
        PrintWriter pw = new PrintWriter(new File("output.pci"));
        pw.write(fin);
        pw.close();
    }

    public static int[] getPixel(int x, int y){
        int col = bi.getRGB(x, y);
        int[] tmp = new int[3];
        tmp[0] = (col)&0xFF;
        tmp[1] = (col>>8)&0xFF;
        tmp[2] = (col>>16)&0xFF;
        return tmp;
    }
    
    public static char[] IATCA(int[] pixel) {
        char[] ret = new char[3];
        ret[2]=(char)pixel[0];
        ret[1]=(char)pixel[1];
        ret[0]=(char)pixel[2];
        return ret;
    }

    public static boolean PixelEquals(char[] col, char[] pix) {
        if(col.length<3 || pix.length<3)
            return false;
        if(Math.abs(col[0]-pix[0])>3)
            return false;
        if(Math.abs(col[1]-pix[1])>3)
            return false;
        if(Math.abs(col[2]-pix[2])>3)
            return false;
        return true;
    }

    private static void refreshVisMode() {
        if(!VISUALMODE)
            return;
        cover = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    public static int getRevCol(int x, int y) {
        int col = bi.getRGB(x, y);
        int b = col&0xFF;
        int g = (col>>8)&0xFF;
        int r = (col>>16)&0xFF;
        int ncol = 0xFF;
        ncol = (ncol<<8) + r;
        ncol = (ncol<<8) + g + 128;
        ncol = (ncol<<8) + b;
        return ncol;
    }
    
    public static byte numBytes(long a){
        return (byte)Math.ceil(Math.log(a+1)/Math.log(2)/8);
    }

    public static void AppendData(StringBuilder sb, long len) {
        sb.append((char)numBytes(len));
        sb.append(L2CA(len));
    }

    public static char[] L2CA(long len) {
        byte[] bytes = ByteBuffer.allocate(8).putLong(len).array();
        char[] dat = new char[numBytes(len)];
        for(int i = 0; i < dat.length; i++)
            dat[dat.length-i-1]=(char)bytes[bytes.length-i-1];
        return dat;
    }
    
    public static void AppendHeader(StringBuilder s, char c) {
        s.append(c);
        AppendData(s, bi.getWidth());
        AppendData(s, bi.getHeight());
    }

}
