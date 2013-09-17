import org.newdawn.slick.geom.Polygon;

public class Triangle extends Polygon
{
    public Triangle(float scaleX, float scaleY)
    {
        addPoint(0.0f, 0.0f);
        addPoint(scaleX/2.0f, (float)Math.sqrt(scaleX*scaleX*0.75f)*scaleY/scaleX);
        addPoint(scaleX, 0.0f);
        setCenterX(0.0f);
        setCenterY(0.0f);
    }
}
