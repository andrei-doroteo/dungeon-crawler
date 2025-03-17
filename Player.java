import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The player actor is what the user controls throughout the game
 * 
 * @author Andrei Doroteo 
 * @version 2023-06-16
 */
public class Player extends Actor
{
    private int speed = 2; // movement speed of player
    private String direction = "UP"; // direction the character is facing
    private int health = 3; // number of times player can get hit by zombie before losing
    private int invincibility; // number of frames player is invincible for

    private boolean shooting = false; // Tells us whether player is shooting or not. Used for shooting animation

    // Instance variable for animations
    private int walkAnimationSpeed = 12; // how fast the animation images change
    private int shootAnimationSpeed = 5; // how fast the animation images change
    private int frameCount = 0; // used for animations
    private int stage = 1; // tells us which stage of animation the player is in. first frame is stage 1, etc...

    // array of objects that players and enemies cannot pass
    private java.lang.Class<?>[] barrierObjects = {Wall.class, WallBL.class, WallBR.class, WallCL.class, WallCM.class,
            WallCR.class, WallLE.class, WallLS.class, WallMB.class, WallML.class, WallMM.class, WallMR.class, WallMT.class,
            WallMiddle.class, WallRE.class, WallRS.class, WallTL.class, WallTR.class};

    /**
     * Act - do whatever the Player wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        frameCount ++; // incrementing frame count
        checkKeys();
        animateShooting();
        checkPlayerDamage();
    }

    /**
     * Handles key presses
     */
    private void checkKeys()
    {   
        // moving up
        if (Greenfoot.isKeyDown("W") && !shooting)
        {
            if (!isTouchingBarrier(3)) // if player is not touching a barrier above it
            {
                setLocation(getX(), getY()-speed); // move up
                direction = "UP";
                animateWalk();
            }
        }

        // moving down
        if (Greenfoot.isKeyDown("S") && !shooting)
        {
            if (!isTouchingBarrier(4)) // if player is not touching a barrier below it
            {
                setLocation(getX(), getY()+speed); // move down
                direction = "DOWN";
                animateWalk();
            }
        }

        // moving left
        if (Greenfoot.isKeyDown("A") && !shooting)
        {
            if (!isTouchingBarrier(1)) // if player is not touching a barrier to its left
            {
                setLocation(getX()-speed, getY()); // move left
                direction = "LEFT";
                animateWalk();
            }
        }

        // moving right
        if (Greenfoot.isKeyDown("D") && !shooting)
        {
            if (!isTouchingBarrier(2)) // if player is not touching a barrier to its right
            {
                setLocation(getX()+speed, getY()); // move right
                direction = "RIGHT";
                animateWalk();
            }
        }

        //shooting
        if (Greenfoot.mouseClicked(null) && !shooting)
        {
            MouseInfo mouse = Greenfoot.getMouseInfo(); // getting mouse info
            if (mouse.getButton() == 1) // if left mouse button is clicked
            {
                shooting = true;
                getWorld().addObject(new Bullet(direction), getX(), getY()); // shoot bullet in direction player is facing
                Greenfoot.playSound("shoot.mp3"); // shooting sound effect
            }
        }
    }

    /**
     * Handles player walk animations
     */
    private void animateWalk()
    {
        if(frameCount >= walkAnimationSpeed)
        { 
            if (stage <= 4) // is animation stage is 1,2,3, or 4
            {
                // going through the different animation images
                setImage("walk-" + direction.toLowerCase() + Integer.toString(stage) + ".png"); // using concatenation to get animation image names
                stage ++; // incrementing animation stage

                frameCount = 0; // resetting frame count
            }
            else
            {
                stage = 1; // resetting stage
            }
        }
    }

    /**
     * Handles player shooting animations
     */
    private void animateShooting()
    {
        if(frameCount >= shootAnimationSpeed)
        {
            if (shooting) // if player is currently shooting, play shooting animation
            {
                int i = stage;
                // switch statement to go through shooting animation
                switch(i)
                {
                    case 1:
                        setImage("shoot-" + direction.toLowerCase() + Integer.toString(stage) + ".png");
                        stage ++;
                        break;

                    case 2:
                        setImage("shoot-" + direction.toLowerCase() + Integer.toString(stage) + ".png");
                        stage ++;
                        break;

                    case 3:
                        setImage("shoot-" + direction.toLowerCase() + Integer.toString(stage) + ".png");
                        stage ++;
                        break;

                    case 4:
                        setImage("shoot-" + direction.toLowerCase() + Integer.toString(stage) + ".png");
                        stage = 1;
                        shooting = false;
                        break;

                    default:
                        stage = 1;
                }
                frameCount = 0; // resetting frame count
            }
        }
    }
    
    /**
     * Handles player damage
     */
    private void checkPlayerDamage()
    {
        if(invincibility > 0) // if player is invincible
        {
            invincibility -= 1; // decrease invincibility
        }
        else
        {
            if(getOneObjectAtOffset(0, 0, Zombie.class) != null) // makes player hitbox at center of image
            {
                health -= 1; // reduce health
                Greenfoot.playSound("player-hit.mp3"); // player damage sound effect
                invincibility = 30; // makes player invincible for 30 frames
            }
        }
    }

    /**
     * Checks if player is going through an OpenedDoor object
     */
    public boolean walkingThroughDoor()
    {
        if (getOneObjectAtOffset(0, -15, OpenedDoor.class) != null) // if there is an OpenedDoor above the player
        {
            return true;
        }

        else
        {
            return false;
        }
    }

    /**
     * checks if player image is intersecting a barrier on the up, down, left, or right sides
     * 1 = left
     * 2 = right
     * 3 = up
     * 4 = down
     */
    private boolean isTouchingBarrier(int side)
    {
        int direction = side;
        switch(direction)
        {
            case 1: //checks left
                for(int i = 0 ; i < barrierObjects.length ; i ++)
                {
                    if ((getOneObjectAtOffset(-10, 0, barrierObjects[i]) != null))
                    {
                        return true;
                    }
                }
                break;

            case 2: // checks right
                for(int i = 0 ; i < barrierObjects.length ; i ++)
                {
                    if ((getOneObjectAtOffset(10, 0, barrierObjects[i]) != null))
                    {
                        return true;
                    }
                }
                break;

            case 3: // checks top
                for(int i = 0 ; i < barrierObjects.length ; i ++)
                {
                    if ((getOneObjectAtOffset(0, -15, barrierObjects[i]) != null))
                    {
                        return true;
                    }
                }
                break;

            case 4: // checks bottom
                for(int i = 0 ; i < barrierObjects.length ; i ++)
                {
                    if ((getOneObjectAtOffset(0, 15, barrierObjects[i]) != null))
                    {
                        return true;
                    }
                }
                break;
        }
        return false; // returns false if loop runs and player is not intersecting a barrier
    }
    
    /**
     * Returns player's current health
     */
    public int getHealth()
    {
        return health;
    }
}
