import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

// ******THIS IS NOT REALLY PART OF MY GAME. JUST NEEDED TO VISUALIZE COORDINATES********

/**
 * TestSquare is used to test the coordinates of a spawn area
 * 
 * @author Andrei Doroteo
 * @version 2023-06-16
 */
public class TestSquare extends Actor
{
    private int length = 345;
    private int width = 345;

    private int x = 480 / 2;
    private int y = 480 / 2;

    public TestSquare()
    {
        GreenfootImage image = new GreenfootImage(length, width);
        image.fill();
        
        setImage(image);

    }

    public void act()
    {
        setLocation(x, y);
    }
}
