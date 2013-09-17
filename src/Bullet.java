import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Color;

public class Bullet extends Entity
{
    public Bullet()
    {
        super();
        generate();
    }
    
    public Bullet(Shape shape, Color color)
    {
        super(shape, color);
    }
    
    protected Bullet(Bullet other)
    {
        super(other);
    }
    
    public Bullet clone()
    {
        return new Bullet(this);
    }
    
    public void generate()
    {
        float height = ((float)Math.random()*0.5f+0.5f)*0.03f;
        float width = ((float)Math.random()*0.5f+0.5f)*height;
        Shape shape = new Triangle(width, height);
        Color color = new Color((float)Math.random()*0.5f+0.5f, (float)Math.random()*0.5f+0.5f, (float)Math.random()*0.5f+0.5f);
        this.shape = shape;
        this.color = color;
        setVelY(((float)Math.random()*0.9f+0.1f)*0.0004f);
        setCollidable(true);
    }
}
