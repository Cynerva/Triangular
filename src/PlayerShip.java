import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.Sound;

import java.util.List;
import java.util.ArrayList;

public class PlayerShip extends Ship
{
    private SoundEmitter shotSound;
    private boolean fire;
    
    public PlayerShip()
    {
        super();
        generate();
    }
    
    public void generate()
    {
        shape = new Triangle(0.02f, 0.03f);
        color = new Color(1.0f, 1.0f, 1.0f);
        
        Shape bulletShape = new Triangle(0.02f, 0.02f);
        Color bulletColor = new Color(0.0f, 0.5f, 1.0f);
        Bullet bullet = new Bullet(bulletShape, bulletColor);
        bullet.setVelY(0.001f);
        bullet.setCollidable(true);
        Turret turret = new Turret();
        turret.setBullets(bullet, 3, 107, 30.0f, false);
        turret.setPosY(0.035f);
        turret.setActive(true);
        
        Shape newShape = new Triangle(0.015f, 0.05f);
        Entity part = new Entity(newShape, color);
        part.setPosX(0.015f);
        part.addChild(turret);
        addChild(part.clone());
        
        part.setPosX(-0.015f);
        addChild(part);
        
        newShape = new Triangle(0.0075f, 0.0075f);
        Color newColor = new Color(0.0f, 1.0f, 1.0f);
        part = new Entity(newShape, newColor);
        part.setCollidable(true);
        part.setVelAngle(1.0f);
        addChild(part);
        
        shotSound = new SoundEmitter();
        addChild(shotSound);
        
        getTurrets();
        
        setHealth(1);
        setMaxHealth(1);
        setLives(2);
        setInvincibilityDuration(5000);
    }
    
    public void update(int delta, MainGameState game, Transform transform, float parentAngle)
    {
        // handle input
        for(Turret t : turrets)
            t.setActive(fire);
        shotSound.setActive(fire);
        
        if(fire) {
            setVelX(velX() / 2.0f);
            setVelY(velY() / 2.0f);
        }
        
        fire = false;
        
        super.update(delta, game, transform, parentAngle);
        
        setVelX(0.0f);
        setVelY(0.0f);
        
        if(isInvincible())
        {
            changeColor(new Color(0.5f, 0.5f, 0.5f, 0.5f), 0.01f);
            children.get(2).changeColor(new Color(1.0f, 0.0f, 0.0f, 0.5f), 0.01f);
        }
        else
        {
            changeColor(new Color(1.0f, 1.0f, 1.0f), 0.01f);
            children.get(2).changeColor(new Color(0.0f, 1.0f, 1.0f), 0.01f);
        }
    }
    
    public void fire()
    {
        fire = true;
    }
    
    public void moveLeft()
    {
        setVelX(velX() - 0.0003f);
    }
    
    public void moveRight()
    {
        setVelX(velX() + 0.0003f);
    }
    
    public void moveUp()
    {
        setVelY(velY() - 0.0003f);
    }
    
    public void moveDown()
    {
        setVelY(velY() + 0.0003f);
    }
    
    public void setShotSound(Sound sound, float volume)
    {
        shotSound.setSound(sound, volume, 107);
    }
}
