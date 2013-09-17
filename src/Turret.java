import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.Color;

import java.util.List;
import java.util.ArrayList;

public class Turret extends Entity
{
    private Bullet bullet;
    private int numBullets;
    private int period;
    private int timer;
    private float spread;
    private Entity target;
    private boolean aims;
    private boolean active;
    
    public Turret()
    {
        super();
    }
    
    public Turret(Shape shape, Color color)
    {
        super(shape, color);
    }
    
    protected Turret(Turret other)
    {
        super(other);
        setBullets(other.bullet.clone(), other.numBullets, other.period, other.spread, other.aims);
        setTarget(other.target);
        setActive(other.active);
        timer = other.timer;
    }
    
    public Turret clone()
    {
        return new Turret(this);
    }
    
    public void setBullets(Bullet bullet, int numBullets, int period, float spread, boolean aims)
    {
        this.bullet = bullet;
        this.numBullets = numBullets;
        this.period = period;
        this.spread = spread;
        this.aims = aims;
    }
    
    public void update(int delta, MainGameState game, Transform transform, float parentAngle)
    {
        super.update(delta, game, transform, parentAngle);
        timer -= delta;
        
        // spawn bullets
        if(active && timer <= 0)
        {
            timer = period;
            
            float angleToTarget;
            if(aims && target != null)
            {
                Vector2f worldPos = worldTransform.transform(new Vector2f());
                Vector2f targetPos = target.worldTransform.transform(new Vector2f());
                targetPos.sub(worldPos);
                angleToTarget = (float)targetPos.getTheta() - worldAngle - 90.0f;
            }
            else
                angleToTarget = 0.0f;
            
            for(int a = 0; a < numBullets; a++)
            {
                Bullet newBullet = bullet.clone();
                Vector2f newPos = worldTransform.transform(new Vector2f(bullet.posX(), bullet.posY()));
                newBullet.setPosX(newPos.getX());
                newBullet.setPosY(newPos.getY());
                float newAngle = numBullets > 1 ? worldAngle - spread/2.0f + a*spread/(numBullets-1) : worldAngle;
                newAngle += angleToTarget;
                Vector2f newVel = new Vector2f(newBullet.velX(), newBullet.velY()).add(newAngle);
                newBullet.setVelX(newVel.getX());
                newBullet.setVelY(newVel.getY());
                newBullet.setAngle(newAngle);
                game.addBullet(newBullet);
            }
        }
    }
    
    public void setTarget(Entity entity)
    {
        target = entity;
    }
    
    public void generate(int difficulty)
    {
        int newNumBullets = (int)(Math.random()*10.0+1.0);
        int newPeriod = newNumBullets*1000/difficulty;
        //int newPeriod = (int)(Math.random()*2900.0)+100;
        //int newNumBullets = newPeriod*difficulty/1000;
        float newSpread = (float)Math.random()*360.0f;
        boolean newAims = Math.random() < 0.25 ? true : false;
        setBullets(new Bullet(), newNumBullets, newPeriod, newSpread, newAims);
    }
    
    public void setActive(boolean active)
    {
        this.active = active;
    }
}
