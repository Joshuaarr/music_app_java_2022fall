//General object
package graphics;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public class G {
    public static Random RND = new Random(); //Constant Value that never change use Upper Class
    public static int rnd (int max){return RND.nextInt(max);}
    public static Color rndColor(){
        return new Color(rnd(256),rnd(256),rnd(256));
    }
    public static void clear(Graphics g){ //Mac default will clear the previous, but windows don't
        g.setColor(Color.white);
        g.fillRect(0,0,5000,5000);
    }
    public static void drawCircle(Graphics g, int x, int y, int r){
        g.drawOval(x - r, y - r, 2 * r, 2 * r);
    }
    // ------------------ v -------------------// Vector
    public static class V implements Serializable{
        public static Transform T = new Transform();
        public int x, y;
        //Upper class for constructors
        public V(int x, int y){this.set(x, y);}
        public void set(int x, int y){this.x = x; this.y = y;}
        public void set(V v){this.x = v.x; this.y = v.y;}
        public void add(V v){x += v.x; y += v.y;} //pass in the vector
        public void setT(V v){set(v.tx(), v.ty());} // Calling helper
        public int tx(){return x * T.n / T.d + T.dx;}
        public int ty(){return y * T.n / T.d + T.dy;}
        public void blend(V v, int k){set((k*x + v.x)/(k+1), (k*y + v.y)/(k+1));}


        // --------------- Transform -----------------//
        public static class Transform{ //坐标系转换？
            public int dx, dy, n, d; // Multi things by fractures, n 分子， d 分母
            public void setScale(int oW, int oH, int nW, int nH){
                n = (nW > nH) ? nW : nH; // Select the bigger one
                d = (oW > oH) ? oW : oH;
            }
            public int offSet(int oX, int oW, int nX, int nW){
                return (- oX - oW / 2) * n / d + nX + nW / 2;
            }
            // pass in 旧/新 坐标系
            public void set(VS oVS, VS nVS){
                setScale(oVS.size.x, oVS.size.y, nVS.size.x, nVS.size.y);
                dx = offSet(oVS.loc.x, oVS.size.x, nVS.loc.x, nVS.size.x);
                dy = offSet(oVS.loc.y, oVS.size.y, nVS.loc.y, nVS.size.y);
            }
            public void set(BBox oB, VS nVS){
                setScale(oB.h.size(), oB.v.size(), nVS.size.x, nVS.size.y);
                dx = offSet(oB.h.lo, oB.h.size(), nVS.loc.x, nVS.size.x);
                dy = offSet(oB.v.lo, oB.v.size(), nVS.loc.y, nVS.size.y);
            }
        }
    }

    // ------------------ vs -------------------// Vector
    public static class VS {
        public V loc,size;
        public VS (int x, int y, int w, int h){loc = new V(x,y); size = new V(w,h);}
        public void fill(Graphics g, Color c){
            g.setColor(c);
            g.fillRect(loc.x, loc.y, size.x, size.y);
        }
        /*
        Hit detector
        compare if the dot is in the rectangle
        2 types of and, & does bit-wise, && does logically(short-cut and)
        */
        public boolean hit(int x, int y){
            return loc.x < x && loc.y < y && x < (loc.x + size.x) && y < (loc.y + size.y);
        }
        public int xL(){return loc.x;}
        public int xM(){return loc.x + size.x / 2;}
        public int xH(){return loc.x + size.x;}
        public int yL(){return loc.y;}
        public int yM(){return loc.y + size.y / 2;}
        public int yH(){return loc.y + size.y;}
        public void resize(int x, int y){
            if(x > loc.x && y > loc.y){
                size.set(x - loc.x, y - loc.y);
            }
        }
    }
    // ------------------ LoHi -------------------// Vector
    public static class LoHi {
        public int lo, hi;
        public LoHi(int min, int max){lo = min; hi = max;}
        public void add(int x){if(x < lo){lo = x;} if(x > hi){hi = x;}}
        public void set(int x){lo = x; hi = x;}
        public int size(){return(hi - lo) == 0 ? 1 : hi - lo;}
        // ? is a conditional expression: if True, return 1, if False(not 0), return hi - lo
    }
    // ------------------ BBox -------------------// Vector
    public static class BBox {
        // bonding box
        public LoHi h, v;
        public BBox(){h = new LoHi(0, 0); v = new LoHi(0, 0);}
        public void set(int x, int y){h.set(x); v.set(y);}
        public void add(V v){h.add(v.x); this.v.add(v.y);} //this.v means the v in this class, and the rest is v from V
        public void add(int x, int y){h.add(x); this.v.add(y);}
        public VS getNewVS(){return new VS(h.lo, v.lo, h.size(),v.size());}
        public void draw(Graphics g){g.drawRect(h.lo, v.lo, h.size(),v.size());}
    }

    // ------------------ PL -------------------// Vector
    public static class PL implements Serializable {
        public V[] points;
        // Constructor
        public PL(int count){
            points = new V[count];//array full of null
            for( int i = 0; i < count; i ++){points[i] = new V(0, 0);}
        }
        public int size(){return points.length;}
        public void transform(){
            for(int i = 0; i < points.length; i++){
                points[i].setT(points[i]);
            }
        }
        public void drawN(Graphics g, int n){
            for(int i = 1; i < n; i++){
                g.drawLine(points[i - 1].x, points[i - 1].y, points[i].x, points[i].y);
            }
        }
        public void drawNDots(Graphics g, int n) {
            for(int i = 0; i < n; i++){
                drawCircle(g, points[i].x, points[i].y, 2);
            }
        }
        public void draw(Graphics g){drawN(g, points.length);}
    }
}
