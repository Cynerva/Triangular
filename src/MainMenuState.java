import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.Input;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainMenuState extends BasicGameState
{
    private int stateID = -1;
    private int cursorPos;
    private Entity cursor;
    private Music music;
    private Text title;
    private Text start;
    private Text exit;
    private List<Entity> background;
    
    public MainMenuState(int stateID)
    {
        this.stateID = stateID;
    }
    
    public int getID()
    {
        return stateID;
    }
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
    {
        Random r = new Random();
        music = new Music("data/music/Menu.ogg", true);
        background = new ArrayList<Entity>();
        for(int a = 0; a < 100; a++)
        {
            Color color = new Color(0.0f, 0.0f, 0.5f, 0.5f);
            Shape shape = new Triangle(r.nextFloat(), r.nextFloat());
            Entity entity = new Entity(shape, color);
            entity.setPosX(r.nextFloat());
            entity.setPosY(r.nextFloat());
            entity.setAngle(r.nextFloat()*360.0f);
            entity.setVelAngle((r.nextFloat()-0.5f)*0.01f);
            background.add(entity);
        }
        title = new Text("TRIANGULAR", 0.075f, new Color(1.0f, 1.0f, 1.0f));
        title.setPosX(0.5f);
        title.setPosY(0.25f);
        start = new Text("START", 0.025f, new Color(1.0f, 1.0f, 1.0f, 0.75f));
        start.setPosX(0.5f);
        start.setPosY(0.5f);
        exit = new Text("EXIT", 0.025f, new Color(1.0f, 1.0f, 1.0f, 0.75f));
        exit.setPosX(0.5f);
        exit.setPosY(0.575f);
        
        cursor = new Entity(new Triangle(0.03f, 0.03f), new Color(1.0f, 1.0f, 1.0f, 1.0f));
        cursor.setPosX(0.375f);
        cursor.setPosY(0.5f);
        cursor.setAngle(-90.0f);
    }
    
    public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException
    {
        music.loop();
        music.setVolume(0.0f);
        music.fade(1000, 1.0f, false);
        gc.getInput().clearKeyPressedRecord();
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
    {
        float scale = Math.min(gc.getWidth(), gc.getHeight());
        g.translate((gc.getWidth() - scale)/2, (gc.getHeight() - scale)/2);
        g.scale(scale, scale);
        g.setWorldClip(0.0f, 0.0f, 1.0f, 1.0f);
        
        for(Entity e : background)
            e.render(g);
        title.render(g);
        start.render(g);
        exit.render(g);
        cursor.render(g);
        
        g.clearWorldClip();
    }
    
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
    {
        Input input = gc.getInput();
        if(input.isKeyPressed(Input.KEY_UP))
        {
            if(cursorPos > 0)
            {
                cursorPos--;
                cursor.setPosY(0.5f + cursorPos*0.075f);
            }
        }
        if(input.isKeyPressed(Input.KEY_DOWN))
        {
            if(cursorPos < 1)
            {
                cursorPos++;
                cursor.setPosY(0.5f + cursorPos*0.075f);
            }
        }
        if(input.isKeyPressed(Input.KEY_ENTER) || input.isKeyPressed(Input.KEY_Z))
        {
            if(cursorPos == 0)
                sbg.enterState(TriangularGame.MAINGAMESTATE);
            else
                gc.exit();
        }
        else if(input.isKeyPressed(Input.KEY_ESCAPE))
        {
            gc.exit();
        }
        
        for(Entity e : background)
            e.update(delta, null);
    }
}
