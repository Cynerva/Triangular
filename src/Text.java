import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Shape;

public class Text extends Entity
{
    public Text()
    {
        super();
    }
    
    public Text(String input, float scale, Color color)
    {
        this();
        set(input, scale, color);
    }
    
    public void set(String input, float scale, Color color)
    {
        children.clear();
        Transform transform = Transform.createScaleTransform(scale, scale);
        float totalLength = (input.length() + (input.length()-1)*0.2f)*scale;
        float currentPos = -totalLength/2.0f + 0.5f*scale;
        for(char c : input.toCharArray())
        {
            Shape glyph = GlyphManager.getGlyph(c);
            if(glyph != null)
            {
                Entity child = new Entity(glyph.transform(transform), color);
                child.setPosX(currentPos);
                addChild(child);
            }
            currentPos += 1.2*scale;
        }
    }
    
    public void setSolidness(float value)
    {
        for(Entity child : children)
            child.setSolidness(value);
    }
    
    public void changeColor(Color targetColor, float colorChangeSpeed)
    {
        for(Entity child : children)
            child.changeColor(targetColor, colorChangeSpeed);
    }
}