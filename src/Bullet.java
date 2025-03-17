import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Bullets damage and kill zombies
 * 
 * @author Andrei Doroteo
 * @version 2023-06-16
 */
public class Bullet extends Actor
{
    // Instance Variables
    private int speed = 10;
    private String trajectory;

    // array of objects that bullets end at
    private java.lang.Class<?>[] barrierObjects = {Wall.class, WallBL.class, WallBR.class, WallCL.class, WallCM.class,
            WallCR.class, WallLE.class, WallLS.class, WallMB.class, WallML.class, WallMM.class, WallMR.class, WallMT.class,
            WallMiddle.class, WallRE.class, WallRS.class, WallTL.class, WallTR.class};

    public Bullet(String direction)
    {
        trajectory = direction; // this is set by the player's movement so it shoots the way the player is facing
    }

    public void act()
    {
        // moving bullet throughout the screen
        if (trajectory == "UP")
        {
            setLocation(getX(), getY() - speed); // moving bullet up
        }
        else if (trajectory == "LEFT")
        {
            setLocation(getX() - speed, getY()); // moving bullet left
        }
        else if (trajectory == "DOWN")
        {
            setLocation(getX(), getY() + speed); // moving bullet down
        }
        else if (trajectory == "RIGHT")
        {
            setLocation(getX() + speed, getY()); // moving bullet right
        }

        // removing bullet when at end of screen or if it hits a wall
        if(isAtEdge() || isTouchingBarrier())
        {
            getWorld().removeObject(this);
        }
    }
    
    /**
     * Checks if bullet is touching a barrier object
     */
    private boolean isTouchingBarrier()
    {
        for(int i = 0 ; i < barrierObjects.length ; i ++) // loops through all the barrier objects
        {
            if (isTouching(barrierObjects[i])) // if bullet is touching that object
            {
                return true;
            }
        }
        return false; // returns false if not touching barrier object
    }
}
