import org.newdawn.slick.geom.Shape;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

public class GlyphManager
{
    private static Map<Character, Shape> map;
    private static boolean initialized;
    
    public static Shape getGlyph(char character)
    {
        if(!initialized)
            init();
        return map.get(character);
    }
    
    private static void init()
    {
        initialized = true;
        map = new HashMap<Character, Shape>();
        String[] files = new File("data/glyphs").list();
        for(String file : files)
        {
            Shape shape = FileManager.loadPolygon("data/glyphs/" + file);
            map.put((char)(Integer.parseInt(file)), shape);
        }
    }
}