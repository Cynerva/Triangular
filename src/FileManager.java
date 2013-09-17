import org.newdawn.slick.geom.Polygon;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class FileManager
{
    public static void savePolygon(Polygon input, String filename)
    {
        float[] points = input.getPoints();
        saveFloatArray(points, filename);
    }
    
    public static Polygon loadPolygon(String filename)
    {
        float[] points = loadFloatArray(filename);
        return new Polygon(points);
    }
    
    public static void saveFloatArray(float[] input, String filename)
    {
        try
        {
            FileOutputStream fout = new FileOutputStream(filename);
            
            ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE/Byte.SIZE);
            bb.putInt(input.length);
            fout.write(bb.array());
            
            bb = ByteBuffer.allocate(Float.SIZE/Byte.SIZE);
            for(float a : input)
            {
                bb.putFloat(0, a);
                fout.write(bb.array());
            }
            
            fout.close();
        }
        catch(IOException E)
        {
            System.out.println("Failed to save file: " + filename);
        }
    }
    
    public static float[] loadFloatArray(String filename)
    {
        try
        {
            FileInputStream fin = new FileInputStream(filename);
            
            ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE/Byte.SIZE);
            fin.read(bb.array());
            float[] output = new float[bb.getInt(0)];
            
            bb = ByteBuffer.allocate(Float.SIZE/Byte.SIZE);
            for(int a = 0; a < output.length; a++)
            {
                fin.read(bb.array());
                output[a] = bb.getFloat(0);
            }
            
            return output;
        }
        catch(IOException E)
        {
            System.out.println("Failed to open file: " + filename);
            return null;
        }
    }
}