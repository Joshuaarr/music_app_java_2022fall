package music;
import music.Stem.List;
import reaction.Mass;

import java.awt.*;

public class Beam extends Mass {
    public static int mX1, mX2, mY1, mY2; //Master Beam
    public static Polygon poly;
    static {
        int[] foo = {0, 0, 0, 0};
        poly = new Polygon(foo, foo, 4);
    }
    public Stem.List stems = new List();
    public Beam(Stem f, Stem l){
        super("NOTE");
        addStem(f);
        addStem(l);
    }

    public void show(Graphics g){
        g.setColor(Color.BLACK);
        drawBeamGroup(g);
    }

    public void drawBeamGroup(Graphics g){
        setMasterBeam();
        Stem firstStem = first();
        int h = UC.defaultStaffSpace, sH = firstStem.isUp ? h : - h;
        int nPrev = 0, nCurr = firstStem.nFlag, nNext = stems.get(1).nFlag;
        int pX, cX = firstStem.x(), bX = cX + 3 * h;

        // Beam-lets on first stem.
        if(nCurr > nNext){drawBeamStack(g, nNext, nCurr, cX, bX,sH);}
        for(int curr = 1; curr < stems.size(); curr++){
            Stem sCurr = stems.get(curr);
            pX = cX; cX = sCurr.x();
            nPrev = nCurr; nCurr = nNext; nNext = (curr < stems.size() - 1) ? stems.get(curr + 1).nFlag : 0;
            int nBack = Math.min(nPrev, nCurr);
            drawBeamStack(g, 0, nBack, pX, cX, sH);
            if(nCurr > nPrev && nCurr > nNext){ // We need beam-lets
                if(nPrev < nNext){
                    bX = cX + 3 * h;
                    drawBeamStack(g, nNext, nCurr, cX, bX, sH);
                }else{
                    bX = cX - 3 * h;
                    drawBeamStack(g, nPrev, nCurr, cX, bX, sH);
                }
            }
        }
    }


    public void addStem(Stem s){
        if(s.beam == null){
            stems.add(s);
            s.beam = this;
            stems.sort();
            s.nFlag = 1;
        }
    }
    public Stem first(){return stems.get(0);}
    public Stem last(){return stems.get(stems.size() - 1);}
    public void deleteBeam(){
        for (Stem s : stems ){s.beam = null;}
        deleteMass();
    }

    public static int yOfX(int x, int x1, int y1, int x2, int y2){
        int dY = y2 - y1, dX = x2 - x1;
        return (x - x1) * dY / dX + y1;
    }

    public static int yOfX(int x){
        int dY = mY2 - mY1, dX = mX2 - mX1;
        return (x - mX1) * dY / dX + mY1;
    }

    public static void setMasterBeam(int x1, int y1, int x2, int y2){
        mX1 = x1;
        mY1 = y1;
        mX2 = x2;
        mY2 = y2;
    }
    public void setMasterBeam(){
        setMasterBeam(first().x(), first().yBeamEnd(), last().x(), last().yBeamEnd());
    }
    public static void setPoly(int x1, int y1, int x2, int y2, int h){
        int[] a = poly.xpoints;
        a[0] = x1; a[1] = x2; a[2] = x2; a[3] = x1;
        a = poly.ypoints;
        a[0] = y1; a[1] = y2; a[2] = y2 + h; a[3] = y1 + h;
    }

    public static void drawBeamStack(Graphics g, int n1, int n2, int x1, int x2, int h){
        int y1 = yOfX(x1), y2 = yOfX(x2);
        for(int i = n1; i < n2; i++){
            setPoly(x1, y1 + i * 2 * h, x2, y2 + i * 2 * h, h);
            g.fillPolygon(poly);
        }
    }
}
