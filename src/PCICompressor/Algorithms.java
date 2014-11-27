package pci.compressor;

import java.util.logging.Level;
import java.util.logging.Logger;
import static pci.compressor.PCICompressor.*;

final public class Algorithms {
    public static void AlgA() {
        long streak = 0;
        char[] col = IATCA(getPixel(0, 0));
        char[] pix;
        AppendHeader(horsb, 'A');
        for(int pos = 0; pos<bi.getWidth()*bi.getHeight(); pos++){
            pix = IATCA(getPixel(pos%bi.getWidth(), pos/bi.getWidth()));
            
            if(VISUALMODE){
                cover.setRGB(mx, my, getRevCol(mx,my));
                mx = pos%bi.getWidth();
                my = pos/bi.getWidth();
                if(SLEEP)
                try {
                    Thread.sleep(pos%20/19);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Algorithms.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(!PixelEquals(col,pix) || pos+1==bi.getWidth()*bi.getHeight()){
                if(pos+1==bi.getWidth()*bi.getHeight())
                    streak++;
                
                /*
                hor+=streak+";";
                hor+=col[0];
                hor+=col[1];
                hor+=col[2];*/
                AppendData(horsb,streak);
                horsb.append(col);
                col = pix;
                
                streak = 1;
            }
            else
                streak++;
        }
        
        hor = horsb.toString();
        ADone = true;
    }
    
    public static void AlgB(){
        long streak = 0;
        char[] col = IATCA(getPixel(0, 0));
        char[] pix;
        AppendHeader(vertsb, 'B');
        col = IATCA(getPixel(0, 0));
        for(int pos = 0; pos<bi.getWidth()*bi.getHeight(); pos++){
            pix = IATCA(getPixel(pos/bi.getHeight(), pos%bi.getHeight()));
            
            if(VISUALMODE){
                cover.setRGB(mx, my, getRevCol(mx,my));
                mx = pos/bi.getHeight();
                my = pos%bi.getHeight();
                if(SLEEP)
                try {
                    Thread.sleep(pos%20/19);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Algorithms.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(!PixelEquals(col,pix) || pos+1==bi.getWidth()*bi.getHeight()){
                if(pos+1==bi.getWidth()*bi.getHeight())
                    streak++;
                
                /*
                vert+=streak+";";
                vert+=col[0];
                vert+=col[1];
                vert+=col[2];*/
                //vertsb.append(streak);
                //vertsb.append(';');
                AppendData(vertsb, streak);
                vertsb.append(col);
                col = pix;
                
                streak = 1;
            }
            else
                streak++;
        }
        
        vert = vertsb.toString();
        BDone = true;
    }
    
    public static void AlgC() {
        long streak = 0;
        char[] col = IATCA(getPixel(0, 0));
        char[] pix;
        AppendHeader(horswsb, 'C');
        col = IATCA(getPixel(0, 0));
        for(int pos = 0; pos<bi.getWidth()*bi.getHeight(); pos++){
            int y = pos/bi.getWidth()/2;
            if (pos/bi.getWidth()%2==1)
                y = bi.getHeight() - pos/bi.getWidth()/2 - 1;
            pix = IATCA(getPixel(pos%bi.getWidth(), y));
            
            if(VISUALMODE){
                cover.setRGB(mx, my, getRevCol(mx,my));
                mx = pos%bi.getWidth();
                my = y;
                if(SLEEP)
                try {
                    Thread.sleep(pos%20/19);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Algorithms.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(!PixelEquals(col,pix) || pos+1==bi.getWidth()*bi.getHeight()){
                if(pos+1==bi.getWidth()*bi.getHeight())
                    streak++;
                
                /*
                horsw+=streak+";";
                horsw+=col[0];
                horsw+=col[1];
                horsw+=col[2];*/
                //horswsb.append(streak);
                //horswsb.append(';');
                AppendData(horswsb, streak);
                horswsb.append(col);
                col = pix;
                
                streak = 1;
            }
            else
                streak++;
        }
        
        horsw = horswsb.toString();
        CDone = true;
    }
    
    public static void AlgD() {
        long streak = 0;
        char[] col = IATCA(getPixel(0, 0));
        char[] pix;
        AppendHeader(vertswsb, 'D');
        col = IATCA(getPixel(0, 0));
        for(int pos = 0; pos<bi.getWidth()*bi.getHeight(); pos++){
            int y = pos/bi.getHeight()/2;
            if (pos/bi.getHeight()%2==1)
                y = bi.getWidth() - pos/bi.getHeight()/2 - 1;
            pix = IATCA(getPixel(y, pos%bi.getHeight()));
            
            if(VISUALMODE){
                cover.setRGB(mx, my, getRevCol(mx,my));
                mx = y;
                my = pos%bi.getHeight();
                if(SLEEP)
                try {
                    Thread.sleep(pos%20/19);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Algorithms.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(!PixelEquals(col,pix) || pos+1==bi.getWidth()*bi.getHeight()){
                if(pos+1==bi.getWidth()*bi.getHeight())
                    streak++;
                
                /*
                vertsw+=streak+";";
                vertsw+=col[0];
                vertsw+=col[1];
                vertsw+=col[2];*/
                //vertswsb.append(streak);
                //vertswsb.append(';');
                AppendData(vertswsb, streak);
                vertswsb.append(col);
                col = pix;
                
                streak = 1;
            }
            else
                streak++;
        }
        
        vertsw = vertswsb.toString();
        DDone = true;
    }
}
