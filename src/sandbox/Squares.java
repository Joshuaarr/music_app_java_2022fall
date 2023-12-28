package sandbox;

import graphics.G;
import graphics.Window;
import music.I;
import music.UC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

// Let the parent of Square to be Window
// implement the interface
public class Squares extends Window implements ActionListener {
//    public static G.VS theVS = new G.VS(100, 100, 200, 300);
    public static Square.List theList = new Square.List();
    public static Color theColor = Color.BLUE;
    public static Square theSquare;

    public static boolean dragging = false;

    public static G.V mouseDelta = new G.V(0, 0);

    public static Timer timer;

    public static I.Area currentArea;
    public static Square BACKGROUND = new Square(0, 0){
        public void dn(int x, int y){
            dragging = false;
            theSquare = new Square(x, y);
            theList.add(theSquare);
        }
        public void drag(int x, int y){
            theSquare.resize(x, y); // Background behavior
        }
    }; // Anonymous class
    static{
        BACKGROUND.c = Color.WHITE;
        BACKGROUND.size.set(5000, 5000);
        theList.add(BACKGROUND);
    }

    public static final int W = UC.initialWindowWidth;
    public static final int H = UC.initialWindowHeight;
    public Squares() {
        super("Squares", W, H);
        timer = new Timer(30, this);
        timer.setInitialDelay(1000);
        timer.start();
    }
    @Override
    public void paintComponent(Graphics g){
        G.clear(g); //Clear background to color white
//        theVS.fill(g, theColor);
        theList.draw(g);
    }
    public void mousePressed(MouseEvent me){
/*        if(theVS.hit(me.getX(), me.getY())){
            theColor = G.rndColor();
        }
        theList.add(new Square(me.getX(), me.getY()));
        theList.addNew(me.getX(), me.getY());
 */
        int x = me.getX(), y = me.getY();
        currentArea = theList.hit(x,y);
        currentArea.dn(x, y);
        /*
        if (theSquare == null) { //did not take a hit
            dragging = false;
            theSquare = new Square(me.getX(), me.getY());
            theList.add(theSquare);
        }else{
            dragging = true;
            mouseDelta.set(x - theSquare.loc.x, y - theSquare.loc.y);
        }
        */
        repaint();
    }
    public void mouseDragged(MouseEvent me){
        int x = me.getX(), y = me.getY();
        currentArea.drag(x, y);
        /*
        if (dragging){
            theSquare.move(x - mouseDelta.x, y - mouseDelta.y);
        }else{
            theSquare.resize(me.getX(), me.getY());
        }
        */
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // ----------- Square ------------//
    public static class Square extends G.VS implements I.Area {
        public Color c = G.rndColor();
        // Generate random numbers within (-20, 20)
        public G.V dv = new G.V(0,0);//new G.V(G.rnd(40) - 20, G.rnd(40) - 20);
        // move the box
        public void move(int x, int y){
            loc.x = x;
            loc.y = y;
        }
        // prevent the boxes from getting outta screen
        public void moveAndBounce(){
            loc.add(dv);
            if (loc.x < 0 && dv.x < 0){dv.x = - dv.x;}
            if (loc.y < 0 && dv.y < 0){dv.y = - dv.y;}
            if (loc.x + size.x > W && dv.x > 0){dv.x = - dv.x;}
            if (loc.y + size.y > H && dv.y > 0){dv.y = - dv.y;}
        }
        // Making things flying when click
        public void draw(Graphics g){
            fill(g, c);
            //loc.add(dv);
            moveAndBounce();
        }
        public Square(int x, int y) {super(x, y,100, 100);}

        @Override
        public void dn(int x, int y) {
            dragging = true;
            mouseDelta.set(x - theSquare.loc.x, y - theSquare.loc.y);
        }

        @Override
        public void drag(int x, int y) {
            theSquare.move(x - mouseDelta.x, y - mouseDelta.y);
        }

        @Override
        public void up(int x, int y) {

        }

        // ------------ List -------------//
        public static class List extends ArrayList<Square> implements I.Draw{
            public void draw(Graphics g){
                for(Square s: this){
                    //s.fill(g, s.c);
                    s.draw(g);
                    }
                }
            public Square hit(int x, int y){
                Square res = null;
                for(Square s: this){
                    if (s.hit(x,y)){
                        res = s;
                    }
                }
                return res;
            }

            public void addNew(int x, int y){add(new Square(x,y));}
        }
    }
}
