import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;


/**
 * This is concrete strategy 
 * Round Pits with lightGray background.
 * @author Ilia Pant
 */

public class RoundFormatter implements ShapeFormatter
{
	@Override
    public Color formatPitColor()
    {
        return Color.lightGray;
    }

    @Override
    public Shape formatPitShape(PitShape p)
    {
       return new Ellipse2D.Double(p.getX(), p.getY(), p.getWidth(), p.getHeight());
    }
}
