package pci.viewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import pci.viewer.Algorithms.*;

public class PCIViewer {

    static WritableRaster r;
    static BufferedImage bi;
    static BufferedReader br;
    static Algorithm alg;
    static char MODE;
    static JFrame frame;
    
    static boolean READING = true;
    static boolean CONCURRENT = false;
    static double DESIREDTIME = 15.0
            * 999;
    static int[] dat = new int[3];
    static int datlen = 0;
    static File file;
    
    public static void main(String[] args) throws Throwable {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFileChooser jfc = new JFileChooser();
        jfc.showOpenDialog(null);
        file = jfc.getSelectedFile();
        br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.defaultCharset()));
        MODE = (char)br.read();
        if(MODE == 'A')
            alg =  Algorithms.AlgA;
        else if (MODE == 'B')
            alg = Algorithms.AlgB;
        else if (MODE == 'C')
            alg = Algorithms.AlgC;
        else
            alg = Algorithms.AlgD;
        
        int w = readCmprsdInt();
        int h = readCmprsdInt();
        
        bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        r = bi.getRaster();
        
        if(CONCURRENT){
            frame.setVisible(true);
            frame.setSize(w, h);
            new Thread(){
                public void run(){
                    while(READING){
                        drawFrame();
                        drawDatText();
                    }
                }
            }.start();
        }
        
        long time = System.currentTimeMillis();
        
        int pos = 0;
        int len;
        int[] col;
        while(br.ready() && pos < w*h){
            len = readCmprsdInt();
            col = readColor();
            if(CONCURRENT){
                datlen = len;
                dat = col;
            }
            while(len>0){
                //setPixel(pos, col);
                alg.set(r, pos, col);
                if(CONCURRENT)
                    smartSleep(DESIREDTIME/(double)(w*h));
                pos++;
                len--;
            }
        }
        READING = false;
        
        System.out.println(System.currentTimeMillis()-time);
        
        //bi.setData(r);
        if(!CONCURRENT){
            frame.setVisible(true);
            frame.setSize(w, h);
        }
        while(true)
            drawFrame();
    }

    /*
    private static void setPixel(int pos, int[] col) {
        if(MODE == 'A')
            r.setPixel(pos%r.getWidth(), pos/r.getWidth(), col);
        else if (MODE == 'B')
            r.setPixel(pos/r.getHeight(), pos%r.getHeight(), col);
        else if (MODE == 'C'){
            int y = pos/r.getWidth()/2;
            if (pos/r.getWidth()%2==1)
                y = r.getHeight() - pos/r.getWidth()/2 - 1;
            r.setPixel(pos%r.getWidth(), y, col);
        }
        else{
            int x = pos/r.getHeight()/2;
            if (pos/r.getHeight()%2==1)
                x = r.getHeight()-1 - pos/r.getHeight()/2;
            r.setPixel(x, pos%r.getHeight(), col);
        }
    }*/

    private static int readCmprsdInt() throws Throwable{
        char len = (char)br.read();
        int num = 0;
        int i;
        while(len>0){
            i = br.read();
            num = (num<<8) + (i&0xFF);
            len--;
        }
        return num;
    }

    private static int[] readColor() throws Throwable {
        int[] dat = new int[3];
        dat[0] = br.read();
        dat[1] = br.read();
        dat[2] = br.read();
        return dat;
    }

    private static void drawFrame() {
        frame.getGraphics().drawImage(bi, 8, 28, frame.getWidth()-16, frame.getHeight()-36, null);
    }
    
    private static void drawDatText() {
        StringBuilder stdat = new StringBuilder();
        stdat.append(datlen);
        stdat.append(' ').append('{');
        stdat.append((char)dat[0]);
        stdat.append((char)dat[1]);
        stdat.append((char)dat[2]);
        stdat.append('}');
        Graphics g = frame.getGraphics();
        g.setColor(Color.WHITE);
        g.drawString(stdat.toString(), frame.getWidth()/2, frame.getHeight()/2);
        g.dispose();
    }

    private static double ssvar = 0;
    private static void smartSleep(double amt) throws Throwable {
        ssvar += amt;
        int tmp = (int)ssvar;
        ssvar -= tmp;
        
        if(tmp!=0)
            Thread.sleep(tmp);
    }

}
