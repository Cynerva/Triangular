import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import java.util.List;
import java.util.ArrayList;

public class Entity implements Cloneable
{
    protected List<Entity> children;
    protected Shape shape;
    protected Color color;
    protected Color targetColor;
    protected float solidness;
    protected float colorChangeSpeed;
    protected float posX;
    protected float posY;
    protected float angle;
    protected float velX;
    protected float velY;
    protected float velAngle;
    private boolean collidable;
    protected Transform worldTransform;
    protected float worldAngle;
    
    public Entity()
    {
        children = new ArrayList<Entity>();
        worldTransform = new Transform();
        color = targetColor = new Color(0.0f, 0.0f, 0.0f);
        solidness = 0.5f;
    }
    
    public Entity(Shape shape, Color color)
    {
        this();
        this.shape = shape;
        this.color = this.targetColor = color;
    }
    
    protected Entity(Entity other)
    {
        this(other.shape, other.color);
        for(Entity child : other.children)
            addChild(child.clone());
        posX = other.posX;
        posY = other.posY;
        angle = other.angle;
        velX = other.velX;
        velY = other.velY;
        velAngle = other.velAngle;
        collidable = other.collidable;
        targetColor = other.targetColor;
        solidness = other.solidness;
        colorChangeSpeed = other.colorChangeSpeed;
        worldTransform = other.worldTransform;
        worldAngle = other.worldAngle;
    }
    
    public Entity clone()
    {
        return new Entity(this);
    }
    
    public float posX()
    {
        return posX;
    }
    
    public float posY()
    {
        return posY;
    }
    
    public float angle()
    {
        return angle;
    }
    
    public float velX()
    {
        return velX;
    }
    
    public float velY()
    {
        return velY;
    }
    
    public float velAngle()
    {
        return velAngle;
    }
    
    public boolean collidable()
    {
        return collidable;
    }
    
    public void setPosX(float posX)
    {
        this.posX = posX;
    }
    
    public void setPosY(float posY)
    {
        this.posY = posY;
    }
    
    public void setAngle(float angle)
    {
        this.angle = angle;
    }
    
    public void setVelX(float velX)
    {
        this.velX = velX;
    }
    
    public void setVelY(float velY)
    {
        this.velY = velY;
    }
    
    public void setVelAngle(float velAngle)
    {
        this.velAngle = velAngle;
    }
    
    public void setCollidable(boolean collidable)
    {
        this.collidable = collidable;
    }
    
    public void setSolidness(float solidness)
    {
        this.solidness = solidness;
    }
    
    public void render(Graphics g)
    {
        g.translate(posX, posY);
        g.rotate(0.0f, 0.0f, angle);
        
        if(shape != null && color != null)
        {
            g.setColor(color.scaleCopy(solidness));
            g.fill(shape);
            g.setColor(color);
            g.draw(shape);
        }
        
        for(Entity child : children)
            child.render(g);
        
        g.rotate(0.0f, 0.0f, -angle);
        g.translate(-posX, -posY);
    }
    
    public void update(int delta, MainGameState game)
    {
        update(delta, game, new Transform(), 0.0f);
    }
    
    public void update(int delta, MainGameState game, Transform transform, float parentAngle)
    {
        posX += velX*delta;
        posY += velY*delta;
        angle += velAngle*delta;
        transform.concatenate(Transform.createTranslateTransform(posX, posY));
        transform.concatenate(Transform.createRotateTransform((float)Math.toRadians(angle)));
        worldTransform = transform;
        worldAngle = parentAngle + angle;
        color.add(targetColor.addToCopy(color.scaleCopy(-1.0f)).scaleCopy(colorChangeSpeed*delta));
        for(Entity child : children)
        {
            child.update(delta, game, new Transform(transform), worldAngle);
        }
    }
    
    public boolean checkCollision(Entity other)
    {
        List<Shape> shapes = getCollidableShapes(new Transform());
        List<Shape> otherShapes = other.getCollidableShapes(new Transform());
        for(Shape a : shapes)
        {
            for(Shape b : otherShapes)
            {
                if(a.intersects(b))
                    return true;
            }
        }
        return false;
    }
    
    public List<Shape> getCollidableShapes(Transform transform)
    {
        List<Shape> output = new ArrayList<Shape>();
        if(collidable)
            output.add(shape.transform(worldTransform));
        for(Entity child : children)
        {
            output.addAll(child.getCollidableShapes(worldTransform));
        }
        return output;
    }
    
    public List<Entity> getEntities()
    {
        List<Entity> output = new ArrayList<Entity>();
        output.add(this);
        for(Entity child : children)
            output.addAll(child.getEntities());
        return output;
    }
    
    public List<Entity> getLeaves()
    {
        List<Entity> output = new ArrayList<Entity>();
        if(children.size() == 0)
        {
            output.add(this);
        }
        else
        {
            for(Entity child : children)
            {
                output.addAll(child.getLeaves());
            }
        }
        return output;
    }
    
    public void addChild(Entity entity)
    {
        children.add(entity);
    }
    
    public void changeColor(Color targetColor, float colorChangeSpeed)
    {
        this.targetColor = targetColor;
        this.colorChangeSpeed = colorChangeSpeed;
    }
}
