import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Test extends StateBasedGame
{
    public static final int TESTSTATE = 0;
    public static final int MAINGAMESTATE = 1;
    
    public Test()
    {
        super("Test");
        
        //addState(new TestState(TESTSTATE));
        //enterState(TESTSTATE);
        addState(new MainGameState(MAINGAMESTATE));
        enterState(MAINGAMESTATE);
    }
    
    public static void main(String[] args) throws SlickException
    {
        AppGameContainer app = new AppGameContainer(new Test());
        
        app.setDisplayMode(800, 800, false);
        app.start();
    }
    
    public void initStatesList(GameContainer gameContainer) throws SlickException
    {
        //getState(TESTSTATE).init(gameContainer, this);
        getState(MAINGAMESTATE).init(gameContainer, this); 
    }
}
