import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Color;

import java.util.List;
import java.util.ArrayList;

public class EnemyShip extends Ship
{
    private int numActiveTurrets;
    private int turretPeriod;
    private int turretTimer;
    
    public EnemyShip()
    {
        super();
        generate(1, 10);
    }
    
    public EnemyShip(int size, int difficulty)
    {
        super();
        generate(size, difficulty);
    }
    
    public void generate(int size, int difficulty)
    {
        Shape shape = new Triangle((float)Math.random()*0.2f, (float)Math.random()*0.2f);
        //Color color = new Color((float)Math.random()*0.5f+0.5f, (float)Math.random()*0.5f+0.5f, (float)Math.random()*0.5f+0.5f);
        Color color = new Color(0.0f, 0.0f, 0.0f, 0.0f);
        this.shape = shape;
        this.color = color;
        color = new Color((float)Math.random()*0.5f+0.5f, (float)Math.random()*0.5f+0.5f, (float)Math.random()*0.5f+0.5f);
        changeColor(color, 0.001f);
        setCollidable(true);
        numActiveTurrets = ((int)(Math.random()*size))*2+2;
        //period = (int)(Math.random()*9000.0)+1000;
        turretPeriod = numActiveTurrets*1000;
        turretTimer = 1000;
        setHealth(1000);
        
        List<Entity> parts = new ArrayList<Entity>();
        parts.add(this);
        for(int a = 0; a < size; a++)
        {
            for(int b = 0; b < parts.size(); b++)
            {
                float offsetX = (float)Math.random()*0.2f-0.1f;
                float offsetAngle = (float)Math.random()*360.0f;
                float offsetVelAngle = (float)Math.random() < 0.5f ? (float)Math.random()*0.2f-0.1f : 0.0f;
                shape = new Triangle((float)Math.random()*0.2f, (float)Math.random()*0.2f);
                
                Turret turret = new Turret(shape, new Color(0.0f, 0.0f, 0.0f, 0.0f));
                turret.generate(difficulty/numActiveTurrets);
                turret.setCollidable(true);
                turret.changeColor(color, 0.001f);
                
                turret.setPosX(offsetX);
                turret.setAngle(offsetAngle);
                turret.setVelAngle(offsetVelAngle);
                addChild((Entity)turret.clone());
                
                turret.setPosX(-offsetX);
                turret.setAngle(-offsetAngle);
                turret.setVelAngle(-offsetVelAngle);
                addChild(turret);
            }
        }
        getTurrets();
    }
    
    public void changeActiveTurrets()
    {
        List<Turret> temp = new ArrayList<Turret>();
        temp.addAll(turrets);
        for(int a = 0; a < numActiveTurrets/2; a++)
        {
            int index = (int)(Math.random()*temp.size()/2)*2;
            temp.get(index+1).setActive(true);
            temp.remove(index+1);
            temp.get(index).setActive(true);
            temp.remove(index);
        }
        for(int a = 0; a < temp.size(); a++)
            temp.get(a).setActive(false);
    }
    
    public void update(int delta, MainGameState game, Transform transform, float parentAngle)
    {
        super.update(delta, game, transform, parentAngle);
        turretTimer -= delta;
        if(turretTimer <= 0)
        {
            turretTimer = turretPeriod;
            changeActiveTurrets();
        }
    }
}
