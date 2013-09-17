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

public class MainGameState extends BasicGameState
{
    private final int PLAY_STATE = 0;
    private final int PAUSE_STATE = 1;
    private final int GAMEOVER_STATE = 2;
    
    private int stateID = -1;
    private int gameState;
    private Music music;
    private Sound playerHit;
    private Sound playerDeath;
    private Sound playerShot;
    private Sound playerReactivate;
    private List<Sound> enemyAppear;
    private List<Sound> enemyDeath;
    private PlayerShip player;
    private EnemyShip enemy;
    private List<Entity> playerBullets;
    private List<Entity> enemyBullets;
    private List<Entity> debris;
    private int spawnTimer;
    private int enemyCount;
    private boolean playerAllegiance;
    private Text gameOverText;
    private Text livesTitleText;
    private Text livesText;
    private Text scoreTitleText;
    private Text scoreText;
    private Color textColor;
    private int lives;
    private int score;
    
    // Pause menu stuff
    private Text pauseText;
    private Text pauseResumeText;
    private Text pauseExitText;
    private Entity pauseCursor;
    private int pauseCursorPos;
    
    // Background stuff
    private List<Entity> background;
    private final int BACKGROUND_SIZE = 100;
    
    public MainGameState(int stateID)
    {
        this.stateID = stateID;
    }
    
    public int getID()
    {
        return stateID;
    }
    
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
    {
        enemyAppear = new ArrayList<Sound>();
        enemyDeath = new ArrayList<Sound>();
        
        playerShot = new Sound("data/sound/playershot.ogg");
        playerHit = new Sound("data/sound/playerhit.ogg");
        playerDeath = new Sound("data/sound/playerdeath.ogg");
        playerReactivate = new Sound("data/sound/playerreactivate.ogg");
        enemyAppear.add(new Sound("data/sound/appear1.ogg"));
        enemyAppear.add(new Sound("data/sound/appear2.ogg"));
        enemyAppear.add(new Sound("data/sound/appear3.ogg"));
        enemyAppear.add(new Sound("data/sound/appear4.ogg"));
        enemyAppear.add(new Sound("data/sound/appear5.ogg"));
        enemyDeath.add(new Sound("data/sound/explosion1.ogg"));
        enemyDeath.add(new Sound("data/sound/explosion2.ogg"));
        enemyDeath.add(new Sound("data/sound/explosion3.ogg"));
        enemyDeath.add(new Sound("data/sound/explosion4.ogg"));
        enemyDeath.add(new Sound("data/sound/explosion5.ogg"));
        enemyDeath.add(new Sound("data/sound/explosion6.ogg"));
        enemyDeath.add(new Sound("data/sound/explosion7.ogg"));
        enemyDeath.add(new Sound("data/sound/explosion8.ogg"));
        
        music = new Music("data/music/Triangular.ogg", true);
        
        textColor = new Color(1.0f, 1.0f, 1.0f, 0.5f);
        
        livesTitleText = new Text("LIVES", 0.02f, textColor);
        livesTitleText.setPosX(0.078f);
        livesTitleText.setPosY(0.03f);
        
        livesText = new Text("", 0.02f, textColor);
        livesText.setPosX(0.078f);
        livesText.setPosY(0.07f);
        
        scoreTitleText = new Text("SCORE", 0.02f, textColor);
        scoreTitleText.setPosX(0.922f);
        scoreTitleText.setPosY(0.03f);
        
        scoreText = new Text("", 0.02f, textColor);
        scoreText.setPosX(0.922f);
        scoreText.setPosY(0.07f);
        
        gameOverText = new Text("GAME OVER", 0.05f, new Color(1.0f, 1.0f, 1.0f, 0.0f));
        gameOverText.setPosX(0.5f);
        gameOverText.setPosY(0.5f);
        
        pauseText = new Text("PAUSED", 0.05f, new Color(1.0f, 1.0f, 1.0f, 0.75f));
        pauseText.setPosX(0.5f);
        pauseText.setPosY(0.25f);
        
        pauseResumeText = new Text("RESUME", 0.025f, new Color(1.0f, 1.0f, 1.0f, 0.75f));
        pauseResumeText.setPosX(0.5f);
        pauseResumeText.setPosY(0.5f);
        
        pauseExitText = new Text("EXIT", 0.025f, new Color(1.0f, 1.0f, 1.0f, 0.75f));
        pauseExitText.setPosX(0.5f);
        pauseExitText.setPosY(0.575f);
        
        pauseCursor = new Entity(new Triangle(0.03f, 0.03f), new Color(1.0f, 1.0f, 1.0f, 1.0f));
        pauseCursor.setPosX(0.375f);
        pauseCursor.setPosY(0.5f);
        pauseCursor.setAngle(-90.0f);
    }
    
    public void enter(GameContainer gc, StateBasedGame sbg)
    {
        playerBullets = new LinkedList<Entity>();
        enemyBullets = new LinkedList<Entity>();
        debris = new LinkedList<Entity>();
        background = new LinkedList<Entity>();
        
        for(int a = 0; a < BACKGROUND_SIZE; a++)
        {
            Random r = new Random();
            Entity entity = createBackgroundPiece(r.nextFloat()*1.72f-0.36f);
            background.add(entity);
        }
        
        player = new PlayerShip();
        player.setPosX(0.5f);
        player.setPosY(0.75f);
        player.setAngle(180.0f);
        player.setShotSound(playerShot, 0.1f);
        player.setHitSound(playerHit, 0.25f);
        player.setDeathSound(playerDeath, 0.25f);
        player.setReactivateSound(playerReactivate, 0.25f);
        
        enemy = null;
        enemyCount = 1;
        
        music.loop();
        music.fade(0, 1.0f, false);
        
        lives = player.getLives();
        livesText.set(String.valueOf(lives), 0.02f, textColor);
        
        score = 0;
        scoreText.set(String.valueOf(score), 0.02f, textColor);
        
        gameOverText.changeColor(new Color(1.0f, 1.0f, 1.0f, 0.0f), 1.0f);
        
        gameState = PLAY_STATE;
        
        gc.getInput().clearKeyPressedRecord();
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
    {
        float scale = Math.min(gc.getWidth(), gc.getHeight());
        g.translate((gc.getWidth() - scale)/2.0f, (gc.getHeight() - scale)/2.0f);
        g.scale(scale, scale);
        g.setWorldClip(0.0f, 0.0f, 1.0f, 1.0f);
        
        // Draw the background
        g.setColor(new Color(0.05f, 0.0f, 0.05f));
        g.fillRect(0.0f, 0.0f, 1.0f, 1.0f);
        for(Entity e : background)
            e.render(g);
        
        renderWorld(gc, sbg, g);
        
        livesTitleText.render(g);
        livesText.render(g);
        scoreTitleText.render(g);
        scoreText.render(g);
        
        if(gameState == PAUSE_STATE)
            renderPauseMenu(gc, sbg, g);
        
        g.clearWorldClip();
    }
    
    public void renderWorld(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
    {
        for(Entity d : debris)
            d.render(g);
        if(player != null)
            player.render(g);
        if(enemy != null)
            enemy.render(g);
        for(Entity b : playerBullets)
            b.render(g);
        for(Entity b : enemyBullets)
            b.render(g);
        if(player == null)
            gameOverText.render(g);
    }
    
    public void renderPauseMenu(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
    {
        g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
        g.fillRect(0.0f, 0.0f, 1.0f, 1.0f);
        pauseText.render(g);
        pauseResumeText.render(g);
        pauseExitText.render(g);
        pauseCursor.render(g);
    }
    
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
    {
        switch(gameState)
        {
            case PLAY_STATE: updatePlay(gc, sbg, delta); break;
            case PAUSE_STATE: updatePause(gc, sbg, delta); break;
        }
    }
    
    public void updatePlay(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
    {
        int oldScore = score;
        Input input = gc.getInput();
        if(player != null && !player.isDead())
        {
            if(input.isKeyDown(Input.KEY_UP))
                player.moveUp();
            if(input.isKeyDown(Input.KEY_LEFT))
                player.moveLeft();
            if(input.isKeyDown(Input.KEY_DOWN))
                player.moveDown();
            if(input.isKeyDown(Input.KEY_RIGHT))
                player.moveRight();
            if(input.isKeyDown(Input.KEY_Z))
                player.fire();
            if(input.isKeyPressed(Input.KEY_ESCAPE))
            {
                input.clearKeyPressedRecord();
                pauseCursorPos = 0;
                pauseCursor.setPosY(0.5f);
                music.pause();
                gameState = PAUSE_STATE;
            }
            playerAllegiance = true;
            player.update(delta, this);
            if(player.posX() < 0.0f)
                player.setPosX(0.0f);
            else if(player.posX() > 1.0f)
                player.setPosX(1.0f);
            if(player.posY() < 0.0f)
                player.setPosY(0.0f);
            else if(player.posY() > 1.0f)
                player.setPosY(1.0f);
            if(lives != player.getLives() && player.getLives() >= 0)
            {
                lives = player.getLives();
                livesText.set(String.valueOf(lives), 0.02f, textColor);
            }
        }
        else if(player != null)
        {
            music.fade(1000, 0.0f, true);
            player = null;
            gameOverText.changeColor(new Color(1.0f, 1.0f, 1.0f, 1.0f), 0.0001f);
            input.clearKeyPressedRecord();
        }
        else
        {
            if(input.isKeyPressed(Input.KEY_ENTER) || input.isKeyPressed(Input.KEY_Z)
                || input.isKeyPressed(Input.KEY_ESCAPE))
            {
                sbg.enterState(TriangularGame.MAINMENUSTATE);
            }
        }
        
        // update enemy ship
        int difficulty = (int)(20.0*Math.sqrt(enemyCount));
        if(enemy != null)
        {
            spawnTimer = 0;
            playerAllegiance = false;
            enemy.update(delta, this);
            if(enemy.health() <= 0)
            {
                enemy = null;
                enemyCount++;
            }
        }
        else
        {
            spawnTimer += delta;
            if(spawnTimer >= 3000)
            {
                Random r = new Random();
                //System.out.println("Enemy " + enemyCount + ", Difficulty " + difficulty);
                enemy = new EnemyShip(5, difficulty);
                enemy.setPosX(0.5f);
                enemy.setPosY(0.25f);
                enemy.setDeathSound(enemyDeath.get(r.nextInt(enemyDeath.size())), 0.3f);
                enemyAppear.get(r.nextInt(enemyAppear.size())).play(1.0f, 0.2f);
                if(player != null)
                    enemy.setTarget(player);
            }
        }
        
        Iterator<Entity> it = playerBullets.iterator();
        while(it.hasNext())
        {
            Entity b = it.next();
            b.update(delta, this);
            if(b.posX() < -0.01 || b.posY() < -0.01 || b.posX() > 1.01 || b.posY() > 1.01)
                it.remove();
            else if(enemy != null && b.checkCollision(enemy))
            {
                it.remove();
                enemy.hit(1);
                if(enemy.health() >= 0)
                    score += difficulty;
            }
        }
        
        it = enemyBullets.iterator();
        while(it.hasNext())
        {
            Entity b = it.next();
            b.update(delta, this);
            if(b.posX() < -0.01 || b.posY() < -0.01 || b.posX() > 1.01 || b.posY() > 1.01)
                it.remove();
            else if(player != null && !player.isInvincible() && b.checkCollision(player))
            {
                it.remove();
                player.hit(1);
            }
        }
        
        it = debris.iterator();
        while(it.hasNext())
        {
            Entity d = it.next();
            d.update(delta, this);
            if(d.posX() < -0.15 || d.posY() < -0.15 || d.posX() > 1.15 || d.posY() > 1.15)
                it.remove();
        }
        
        if(oldScore/10 != score/10)
            scoreText.set(String.valueOf(score/10), 0.02f, textColor);
        
        // maybe update other text here too
        gameOverText.update(delta, this);
        
        // Background stuff
        it = background.iterator();
        while(it.hasNext())
        {
            Entity e = it.next();
            e.update(delta, this);
            if(e.posY() > 1.36f)
                it.remove();
        }
        while(background.size() < BACKGROUND_SIZE)
        {
            Entity entity = createBackgroundPiece(-0.36f);
            background.add(entity);
        }
    }
    
    public void updatePause(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
    {
        Input input = gc.getInput();
        if(input.isKeyPressed(Input.KEY_UP))
        {
            if(pauseCursorPos > 0)
            {
                pauseCursorPos--;
                pauseCursor.setPosY(0.5f + pauseCursorPos*0.075f);
            }
        }
        if(input.isKeyPressed(Input.KEY_DOWN))
        {
            if(pauseCursorPos < 1)
            {
                pauseCursorPos++;
                pauseCursor.setPosY(0.5f + pauseCursorPos*0.075f);
            }
        }
        if(input.isKeyPressed(Input.KEY_ENTER) || input.isKeyPressed(Input.KEY_Z))
        {
            if(pauseCursorPos == 0)
            {
                input.clearKeyPressedRecord();
                music.resume();
                gameState = PLAY_STATE;
            }
            else
            {
                input.clearKeyPressedRecord();
                sbg.enterState(TriangularGame.MAINMENUSTATE);
            }
        }
        else if(input.isKeyPressed(Input.KEY_ESCAPE))
        {
            input.clearKeyPressedRecord();
            music.resume();
            gameState = PLAY_STATE;
        }
    }
    
    public void addBullet(Bullet bullet)
    {
        if(playerAllegiance)
            playerBullets.add(bullet);
        else
            enemyBullets.add(bullet);
    }
    
    public void addDebris(Entity entity)
    {
        debris.add(entity);
    }
    
    public Entity createBackgroundPiece(float posY)
    {
        Random r = new Random();
        Shape shape = new Triangle(r.nextFloat()*0.5f, r.nextFloat()*0.5f);
        Color color = new Color(0.5f, 0.0f, 0.5f, 0.1f);
        Entity entity = new Entity(shape, color);
        entity.setPosX(r.nextFloat());
        entity.setPosY(posY);
        entity.setAngle(r.nextFloat()*360.0f);
        entity.setVelAngle((r.nextFloat()-0.5f)*0.01f);
        entity.setVelY(r.nextFloat()*0.00005f + 0.00005f);
        return entity;
    }
}
