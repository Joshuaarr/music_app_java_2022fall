package reaction;

import music.I;

import java.awt.*;

public abstract class Mass extends Reaction.List implements I.Show{
    public Layer layer;
    public Mass(String layerName){
        this.layer = layer.byName.get(layerName);
        if (layer != null){
            layer.add(this);
        } else {
            System.out.println("Bad layer name " + layerName);
        }
    }
    public void deleteMass(){
        clearAll();
        layer.remove(this);
    }
    public void show(Graphics g){}

}
