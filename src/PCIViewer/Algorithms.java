package pci.viewer;

import java.awt.image.WritableRaster;

final public class Algorithms {
    
    public static abstract class Algorithm {
        public void set(WritableRaster r, int pos,  int[] col){
            
        }
    }
    
    static Algorithm AlgA = new Algorithm() {
        @Override
        public void set(WritableRaster r, int pos, int[] col){
            r.setPixel(pos%r.getWidth(), pos/r.getWidth(), col);
        }
    };
    
    static Algorithm AlgB = new Algorithm() {
        @Override
        public void set(WritableRaster r, int pos, int[] col){
            r.setPixel(pos/r.getHeight(), pos%r.getHeight(), col);
        }
    };
    
    static Algorithm AlgC = new Algorithm() {
        @Override
        public void set(WritableRaster r, int pos, int[] col){
            int y = pos/r.getWidth()/2;
            if (pos/r.getWidth()%2==1)
                y = r.getHeight() - pos/r.getWidth()/2 - 1;
            r.setPixel(pos%r.getWidth(), y, col);
        }
    };
    
    static Algorithm AlgD = new Algorithm() {
        @Override
        public void set(WritableRaster r, int pos, int[] col){
            int x = pos/r.getHeight()/2;
            if (pos/r.getHeight()%2==1)
                x = r.getHeight()-1 - pos/r.getHeight()/2;
            r.setPixel(x, pos%r.getHeight(), col);
        }
    };
    /*
    public interface Algorithm {
        public void set(WritableRaster r, int pos,  int[] col);
    }
    static public class AlgA implements Algorithm {
        @Override
        public void set(WritableRaster r, int pos, int[] col){
            r.setPixel(pos%r.getWidth(), pos/r.getWidth(), col);
        }
    }
    
    static public class AlgB implements Algorithm {
        @Override
        public void set(WritableRaster r, int pos, int[] col){
            r.setPixel(pos/r.getHeight(), pos%r.getHeight(), col);
        }
    }
    
    static public class AlgC implements Algorithm {
        @Override
        public void set(WritableRaster r, int pos, int[] col){
            int y = pos/r.getWidth()/2;
            if (pos/r.getWidth()%2==1)
                y = r.getHeight() - pos/r.getWidth()/2 - 1;
            r.setPixel(pos%r.getWidth(), y, col);
        }
    }
    
    static public class AlgD implements Algorithm {
        @Override
        public void set(WritableRaster r, int pos, int[] col){
            int x = pos/r.getHeight()/2;
            if (pos/r.getHeight()%2==1)
                x = r.getHeight()-1 - pos/r.getHeight()/2;
            r.setPixel(x, pos%r.getHeight(), col);
        }
    }*/
    
}
