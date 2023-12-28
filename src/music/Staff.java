package music;

import graphics.G;
import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;

import static sandbox.Music.PAGE;

public class Staff extends Mass {
    public Sys sys;
    public int iStaff;
    public Staff.Fmt fmt;

    public Staff(Sys sys, int iStaff, Staff.Fmt fmt) {
        super("BACK");
        this.sys = sys;
        this.iStaff = iStaff;
        this.fmt = fmt;

        addReaction(new Reaction("S-S") { // Draw a BarLine

            public int bid(Gesture gest) {
                int x = gest.vs.xM(), y1 = gest.vs.yL(), y2 = gest.vs.yH();
                if (x < PAGE.margins.left || x > PAGE.margins.right + UC.BarTwoMarginSnap){
                    return UC.noBid;
                }
                System.out.println("Top" + y1 + " " + Staff.this.yTop());
                int d = Math.abs(y1 - Staff.this.yTop()) + Math.abs(y2 - Staff.this.yBot());
                return (d < 50)? d + UC.BarTwoMarginSnap : UC.noBid; // d always >= UC.Bar2
            }


            public void act(Gesture gest) {
                int x = gest.vs.xM();
                if (Math.abs(x - PAGE.margins.right) < UC.BarTwoMarginSnap){
                    x = PAGE.margins.right;
                }
                new Bar(Staff.this.sys, x);

            }
        });

        addReaction(new Reaction("S-S") { // this is a set bar continue
            //
            public int bid(Gesture gest) {
                if (Staff.this.sys.iSys !=  0){ // Only change barContinue in first sys;
                    return UC.noBid;
                }

                int y1 =gest.vs.yL(), y2 = gest.vs.yH(), iStaff = Staff.this.iStaff;
                if (iStaff == PAGE.sysFmt.size() - 1){ // this is the last one
                    return UC.noBid; // last staff cannot continue
                }

                if (Math.abs(y1 - Staff.this.yBot()) > 20){
                    return  UC.noBid;
                }
                Staff nextStaff = Staff.this.sys.staffs.get(iStaff + 1);
                if (Math.abs(y2 - nextStaff.yTop()) > 20) {
                    return UC.noBid;
                }
                return 10;
            }


            public void act(Gesture gest) {
                Staff.this.fmt.toggleBarContinues();
            }
        });
        addReaction(new Reaction("S-W") { // adding the Quarter note

            public int bid(Gesture gest) {
                int x = gest.vs.xM(), y = gest.vs.yM();
                if(x < PAGE.margins.left || x > PAGE.margins.right){
                    return UC.noBid;
                }
                int H = Staff.this.fmt.H, top = Staff.this.yTop() - H, bot = Staff.this.yBot() + H;
                if (y < top || y > bot){
                    return UC.noBid;
                }
                return 10;
            }


            public void act(Gesture gest) {
                new Head(Staff.this, gest.vs.xM(), gest.vs.yM());

            }
        });


        addReaction(new Reaction("W-S") {//Add a quarter rest
            @Override
            public int bid(Gesture gest) {
                int x = gest.vs.xL(),y = gest.vs.yL();
                if (x < PAGE.margins.left || x > PAGE.margins.right){return UC.noBid;}
                int H = Staff.this.fmt.H, top = Staff.this.yTop(), bot = Staff.this.yBot();
                if (y < top || y > bot){return UC.noBid;}
                return 10;
            }

            @Override
            public void act(Gesture gest) {
                Time t = Staff.this.sys.getTime(gest.vs.xL());
                new Rest(Staff.this, t);


            }
        });
        addReaction(new Reaction("E-W") {//Add a quarter rest
            @Override
            public int bid(Gesture gest) {
                int x = gest.vs.xL(),y = gest.vs.yL();
                if (x < PAGE.margins.left || x > PAGE.margins.right){return UC.noBid;}
                int H = Staff.this.fmt.H, top = Staff.this.yTop(), bot = Staff.this.yBot();
                if (y < top || y > bot){return UC.noBid;}
                return 10;
            }

            @Override
            public void act(Gesture gest) {
                Time t = Staff.this.sys.getTime(gest.vs.xL());
                new Rest(Staff.this, t);

            }
        });


}

    public int sysOff(){
        return sys.fmt.staffOffSet.get(iStaff);
    }
    public int yTop(){
        return sys.yTop() + sysOff();
    }
    public int yBot(){
        return yTop() + fmt.height();
    }

    public int yLine(int n){return yTop() + n * fmt.H;}

    public int lineOfY(int y){
        int bias = 100;
        int top = yTop() - bias * fmt.H;
        return((y - top + fmt.H/2)/fmt.H - bias);
    }


    // ----------------------------FMT---------------------//
    public static class Fmt{
        public int nLines = 5;
        public int H = UC.defaultStaffSpace;
        public boolean barContinues = false;

        public int height(){
            return 2 * H * (nLines - 1);
        }

        public void toggleBarContinues (){
            barContinues = !barContinues;
        }
        public void showAt(Graphics g, int y){
            int LEFT = PAGE.margins.left, RIGHT = PAGE.margins.right;
            for (int i = 0; i < nLines; i ++){
                g.drawLine(LEFT, y + 2 * H * i,RIGHT, y + 2 * H * i );
            }
        }
    }

}
