import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.Sound;

import java.util.List;
import java.util.ArrayList;

public class SoundEmitter extends Entity
{
    private Sound sound;
    float volume;
    private int period;
    private int timer;
    private boolean active;
    
    public SoundEmitter()
    {
        super();
    }
    
    protected SoundEmitter(SoundEmitter other)
    {
        super(other);
        setSound(other.sound, other.volume, other.period);
        setActive(other.active);
        timer = other.timer;
    }
    
    public SoundEmitter clone()
    {
        return new SoundEmitter(this);
    }
    
    public void setSound(Sound sound, float volume, int period)
    {
        this.sound = sound;
        this.volume = volume;
        this.period = period;
    }
    
    public void update(int delta, MainGameState game, Transform transform, float parentAngle)
    {
        super.update(delta, game, transform, parentAngle);
        timer -= delta;
        if(active && timer <= 0)
        {
            timer = period;
            if(sound != null)
                sound.play(1.0f, volume);
        }
    }
    
    public void setActive(boolean active)
    {
        this.active = active;
    }
}
