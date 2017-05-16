import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * This is concrete strategy
 * Square pits with gray background
 * @author Ilia Pant
 */

public class SquareFormatter implements ShapeFormatter
{
    @Override
    public Color formatPitColor()
    {
        return Color.GRAY;
    }

   @Override
    public Shape formatPitShape(PitShape p)
    {
        return new Rectangle2D.Double(p.getX(), p.getY(), p.getWidth(), p.getHeight());
    }    
}
