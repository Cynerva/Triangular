import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Sound;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Ship extends Entity
{
    protected List<Turret> turrets;
    private int maxHealth;
    private int health;
    private int lives;
    private int invincibilityDuration;
    private int invincibilityTimer;
    private Sound hitSound;
    private Sound deathSound;
    private Sound reactivateSound;
    private float hitVolume;
    private float deathVolume;
    private float reactivateVolume;
    
    public Ship()
    {
        super();
        turrets = new ArrayList<Turret>();
    }
    
    public void getTurrets()
    {
        List<Entity> entities = getEntities();
        for(Entity e : entities)
        {
            if(e instanceof Turret)
                turrets.add((Turret)e);
        }
    }
    
    public int health()
    {
        return health;
    }
    
    public void hit(int damage)
    {
        if(!isInvincible())
            health -= damage;
        /*if(health <= 0)
        {
            lives--;
            invincibilityTimer = invincibilityDuration;
            health = maxHealth;
            if(isDead() && deathSound != null)
                deathSound.play(1.0f, deathVolume);
            else if(!isDead() && hitSound != null)
                hitSound.play(1.0f, hitVolume);
        }*/
    }
    
    public boolean isDead()
    {
        return lives < 0;
    }
    
    public boolean isInvincible()
    {
        return invincibilityTimer > 0;
    }
    
    public int getLives()
    {
        return lives;
    }
    
    public void setDeathSound(Sound sound, float volume)
    {
        deathSound = sound;
        deathVolume = volume;
    }
    
    public void setHealth(int health)
    {
        this.health = health;
    }
    
    public void setHitSound(Sound sound, float volume)
    {
        hitSound = sound;
        hitVolume = volume;
    }
    
    public void setInvincibilityDuration(int duration)
    {
        invincibilityDuration = duration;
    }
    
    public void setLives(int lives)
    {
        this.lives = lives;
    }
    
    public void setMaxHealth(int maxHealth)
    {
        this.maxHealth = maxHealth;
    }
    
    public void setReactivateSound(Sound sound, float volume)
    {
        reactivateSound = sound;
        reactivateVolume = volume;
    }
    
    public void setTarget(Entity entity)
    {
        for(Turret t : turrets)
            t.setTarget(entity);
    }
    
    public void update(int delta, MainGameState game, Transform transform, float parentAngle)
    {
        super.update(delta, game, transform, parentAngle);
        if(health <= 0)
        {
            lives--;
            invincibilityTimer = invincibilityDuration;
            health = maxHealth;
            if(isDead())
            {
                // Spawn debris!
                Random r = new Random();
                List<Entity> debris = getEntities();
                for(Entity obj : debris)
                {
                    Entity d = new Entity(obj);
                    Vector2f worldPos = d.worldTransform.transform(new Vector2f());
                    d.children.clear();
                    d.setPosX(worldPos.getX());
                    d.setPosY(worldPos.getY());
                    d.setAngle(d.worldAngle);
                    Vector2f vel = new Vector2f(0.0001f + r.nextFloat()*0.0001f, 0.0f);
                    vel.add(r.nextDouble()*360.0);
                    d.setVelX(vel.getX());
                    d.setVelY(vel.getY());
                    d.setVelAngle(d.velAngle() + (r.nextFloat()-0.5f)*0.1f);
                    Color targetColor = new Color(d.color);
                    targetColor.a = 0.1f;
                    d.changeColor(targetColor, 0.0003f);
                    game.addDebris(d);
                }
                
                // Make an explosion!
                for(int a = 0; a < 100; a++)
                {
                    Shape shape = new Triangle(0.02f, 0.02f);
                    Color color = new Color(this.color);
                    Color targetColor = new Color(color);
                    targetColor.a = 0.0f;
                    Entity e = new Entity(shape, color);
                    Vector2f worldPos = worldTransform.transform(new Vector2f());
                    e.setPosX(worldPos.getX());
                    e.setPosY(worldPos.getY());
                    e.setAngle(r.nextFloat()*360.0f);
                    Vector2f vel = new Vector2f(0.0002f + r.nextFloat()*0.0002f, 0.0f);
                    vel.add(r.nextDouble()*360.0);
                    e.setVelX(vel.getX());
                    e.setVelY(vel.getY());
                    e.setVelAngle(r.nextFloat()-0.5f);
                    e.changeColor(targetColor, 0.005f);
                    e.setSolidness(1.0f);
                    game.addDebris(e);
                }
                
                if(deathSound != null)
                    deathSound.play(1.0f, deathVolume);
            }
            else if(!isDead() && hitSound != null)
                hitSound.play(1.0f, hitVolume);
        }
        if(isInvincible())
        {
            invincibilityTimer -= delta;
            if(!isInvincible() && reactivateSound != null)
                reactivateSound.play(1.0f, reactivateVolume);
        }
    }
}
