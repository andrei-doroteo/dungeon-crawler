import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Zombies can damage and kill the player
 * They move back and forth or up and down indefinitely
 * 
 * @author Andrei Doroteo
 * @version 2023-06-16
 */
public class Zombie extends Actor
{
    private int health = 4; // number of bullets it takes to kill zombie
    private int speed; // zombie's movement speed
    
    // coordinates for zombie movement limits
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    
    // array of objects that zombie cannot pass
    private java.lang.Class<?>[] barrierObjects = {Wall.class, WallBL.class, WallBR.class, WallCL.class, WallCM.class,
            WallCR.class, WallLE.class, WallLS.class, WallMB.class, WallML.class, WallMM.class, WallMR.class, WallMT.class,
            WallMiddle.class, WallRE.class, WallRS.class, WallTL.class, WallTR.class};
    
    int direction; // 0 is left, 1 is right, 2 is up, 3 is down
    
    // Zombie Constructor
    public Zombie(int velocity, int xLimit1, int xLimit2, int yLimit1, int yLimit2)
    {
        // initializing movement variables
        speed = velocity;
        x1 = xLimit1;
        x2 = xLimit2;
        y1 = yLimit1;
        y2 = yLimit2;
        
        direction = Greenfoot.getRandomNumber(4); // sets direction randomly from 0 - 3
    }
    
    public void act()
    {
        checkDamage();
        
        checkDirectionSwitch();
        
        moveDirection(direction);
        
        checkForDeath();
    }
    
    /**
     * Handles zombie damage
     * If damaged, zombie image flashes white
     */
    private void checkDamage()
    {
        Actor bullet = getOneIntersectingObject(Bullet.class); // getting bullet object
        if (bullet != null) // if we get a bullet actor
        {
            setImage("zombie-damage.png"); // white zombie image
            health -= 1; // subtract 1 from health
            Greenfoot.playSound("zombie.mp3"); // zombie damage sound
            getWorld().removeObject(bullet); // remove bullet
        }
        else
        {
            setImage("zombie.png"); // normal zombie image
        }
    }
    
    /**
     * Handles zombie trajectory
     */
    private void checkDirectionSwitch()
    {
        // if zombie has reached it's specified movement limit
        if (getX() <= x1 || getX() >= x2 || getY() <= y1 || getY() >= y2 || isTouchingBarrier() || isAtEdge())
        {
            int d = direction;
            switch (d) // switching directions
            {
                case 0:
                    direction = 1;
                    break;
                    
                case 1:
                    direction = 0;
                    break;
                    
                case 2:
                    direction = 3;
                    break;
                    
                case 3:
                    direction = 2;
                    break;
            }
        }
    }
    
    /**
     * Handles zombie movement
     */
    private void moveDirection(int course) // accepts 0, 1, 2, or 3
    {
        int c = course;
        switch (c) // moving depending on trajectory
        {
            case 0:
                setLocation(getX() - speed, getY());
                break;
            
            case 1:
                setLocation(getX() + speed, getY());
                break;
                
            case 2:
                setLocation(getX(), getY() - speed);
                break;
                
            case 3:
                setLocation(getX(), getY() + speed);
                break;
        }    
    }
    
    /**
     * Handles zombie death
     */
    private void checkForDeath()
    {
        if (health <= 0) // deleting zombie when it has no more health
        {
            getWorld().removeObject(this);
        }
    }
    
    /**
     * Checks if zombie is touching a barrier object
     */
    private boolean isTouchingBarrier()
    {
        for(int i = 0 ; i < barrierObjects.length ; i ++) // loops through all barrier objects
        {
            if (isTouching(barrierObjects[i])) // if zombie is touching that object
            {
                return true;
            }
        }
        return false; // returns false if not touching barrier object
    }
}
