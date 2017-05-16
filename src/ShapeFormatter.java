import java.awt.Color;
import java.awt.Shape;

/**
 * Interface for designing various shapes 
 * Part of Strategy
 * @author Ilia Pant
 */
public interface ShapeFormatter
{
	/**
	 * Format of the color. 
	 * @return the color
	 */
    Color formatPitColor();
    
    /**
     * Format of the PitShape. 
     * @param p - the pit that needs to be formated.
     * @return the shape
     */
    Shape formatPitShape(PitShape p);
}
