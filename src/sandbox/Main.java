package sandbox;

import graphics.Window;

public class Main {
    public static void main(String[] args){
        System.out.println("s");
        Window.PANEL = new ShapeTrainer();
        Window.launch();
    }
}
