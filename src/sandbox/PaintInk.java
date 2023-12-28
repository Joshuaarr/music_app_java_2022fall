package sandbox;

import graphics.G;
import graphics.Window;
import music.UC;
import reaction.Ink;
import reaction.Shape;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PaintInk extends Window {
    public static String recognized = "";
    public static Ink.List inkList = new Ink.List();
//    static{inkList.add(new Ink());}
    public static Shape.Prototype.List pList = new Shape.Prototype.List();
    public PaintInk(){
        super ("PaintInk", UC.initialWindowWidth, UC.initialWindowHeight);
    }
    public void paintComponent (Graphics g){
        G.clear(g);
/*
        g.setColor(Color.RED);
        g.fillRect(100, 100, 100, 100);
 */
        inkList.show(g);
        g.setColor(Color.RED);
        Ink.BUFFER.show(g);
        if(inkList.size() > 1){
            int last = inkList.size() - 1;
            int dist = inkList.get(last).norm.dist(inkList.get(last - 1).norm);
            g.setColor((dist < UC.noMatchDict ? Color.GREEN : Color.RED));
            g.drawString("dist: " + dist, 600, 60);
        }
        g.drawString("points:" + Ink.BUFFER.n, 500, 30);
        pList.show(g);
        g.drawString(recognized, 700, 40);
    }
    public void mousePressed(MouseEvent me){Ink.BUFFER.dn(me.getX(), me.getY()); repaint();}
    public void mouseDragged(MouseEvent me){Ink.BUFFER.drag(me.getX(), me.getY()); repaint();}
//    public void mouseReleased(MouseEvent me){inkList.add(new Ink()); repaint();}
    public void mouseReleased(MouseEvent me) {
        Ink ink = new Ink();
        Shape s = Shape.recognize(ink);
        recognized = "recognized: " + ((s != null) ? s.name: "unrecognized");
        Shape.Prototype proto = new Shape.Prototype();
        inkList.add(ink);
        if (pList.bestDist(ink.norm) < UC.noMatchDict) {
            proto = Shape.Prototype.List.bestMatch;
            proto.blend(ink.norm);
        } else {
            pList.add(new Shape.Prototype());
            pList.add(proto);
        }
        ink.norm = proto;
        repaint();
    }
}
