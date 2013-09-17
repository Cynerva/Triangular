import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;

import java.io.IOException;

public class PolygonCreator extends BasicGame
{
    private byte character;
    private Polygon polygon;
    private Entity cursor;
    private int counter;
    
    public PolygonCreator()
    {
        super("Polygon Creator");
        getGlyph();
    }
    
    public static void main(String[] args) throws SlickException
    {
        AppGameContainer app = new AppGameContainer(new PolygonCreator());
        
        app.setDisplayMode(800, 800, false);
        app.start();
    }
    
    public void getGlyph()
    {
        try
        {
            System.out.print("Enter glyph: ");
            character = (byte)System.in.read();
        }
        catch(IOException E)
        {
            System.out.println("Failed to retrieve user input.");
        }
    }
    
    public void init(GameContainer gc) throws SlickException
    {
        polygon = new Polygon();
        
        Shape shape = new Triangle(0.1f, 0.1f);
        Color color = new Color(0.5f, 0.0f, 0.5f, 1.0f);
        cursor = new Entity(shape, color);
        cursor.setPosX(-0.5f);
        cursor.setPosY(-0.5f);
    }
    
    public void render(GameContainer gc, Graphics g) throws SlickException
    {
        g.scale(800, 800);
        g.translate(0.5f, 0.5f);
        
        if(counter >= 3)
        {
            g.setColor(new Color(1.0f, 1.0f, 1.0f, 0.5f));
            g.fill(polygon);
            g.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
            g.draw(polygon);
        }
        
        cursor.render(g);
    }
    
    public void update(GameContainer gc, int delta) throws SlickException
    {
        Input input = gc.getInput();
        if(input.isKeyPressed(Input.KEY_UP))
            cursor.setPosY(cursor.posY()-0.2f);
        if(input.isKeyPressed(Input.KEY_DOWN))
            cursor.setPosY(cursor.posY()+0.2f);
        if(input.isKeyPressed(Input.KEY_LEFT))
            cursor.setPosX(cursor.posX()-0.2f);
        if(input.isKeyPressed(Input.KEY_RIGHT))
            cursor.setPosX(cursor.posX()+0.2f);
        if(input.isKeyPressed(Input.KEY_ENTER))
        {
            polygon.addPoint(cursor.posX(), cursor.posY());
            counter++;
        }
        if(input.isKeyPressed(Input.KEY_S))
        {
            FileManager.savePolygon(polygon, "data/glyphs/" + character);
            System.exit(0);
        }
    }
}