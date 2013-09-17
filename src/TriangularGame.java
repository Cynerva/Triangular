import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Input;
import org.lwjgl.opengl.Display;

import java.io.File;


public class TriangularGame extends StateBasedGame
{
    public static final int MAINMENUSTATE = 0;
    public static final int MAINGAMESTATE = 1;

    public TriangularGame() { super("Triangular"); }

    public static void main(String[] args) throws SlickException
    {
        System.setProperty(
            "org.lwjgl.librarypath",
            new File("lib/natives").getAbsolutePath()
        );

        StateBasedGame game = new TriangularGame();
        AppGameContainer app = new AppGameContainer(game);
        app.setDisplayMode(800, 600, false);
        app.setShowFPS(false);
        app.start();
    }

    public void initStatesList(GameContainer gameContainer) throws SlickException
    {
        addState(new MainMenuState(MAINMENUSTATE));
        addState(new MainGameState(MAINGAMESTATE));
        enterState(MAINMENUSTATE);
    }
}
