import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * World for Apocalypse Escape
 * 
 * @author Andrei Doroteo 
 * @version 2023-06-16
 */
public class Dungeon extends World
{
    // Screen Dimensions
    private static final int SCREEN_HEIGHT = 480; // fits 10 tiles
    private static final int SCREEN_WIDTH = 480; // fits 10 tiles
    
    Tile[] existingTiles = new Tile[127]; // array for all tiles in world. Length of 127 to account for the wall behind the door and the floors under the side walls

    // Player Variables
    private Player player = new Player(); // new player object
    private int currentLevel = 1; // current level the player is on
    private Heart[] hearts; // array for the shown heart objects
    private int initialHealth; // initial health of the player
    private int currentHealth; // health of the player at a given time
    
    // Map Variables
    private int objectsNum = 1; // number of objects currently in world. Set to 1 because player is always in world
    private int zombiesNum = 0; // number of zombies placed in world
    private ClosedDoor lockedDoor; // stores the door that needs to be unlocked when all zombies are defeated

    // array of objects that players and enemies cannot pass
    private java.lang.Class<?>[] barrierObjects = {Wall.class, WallBL.class, WallBR.class, WallCL.class, WallCM.class,
            WallCR.class, WallLE.class, WallLS.class, WallMB.class, WallML.class, WallMM.class, WallMR.class, WallMT.class,
            WallMiddle.class, WallRE.class, WallRS.class, WallTL.class, WallTR.class, Empty.class};
            
    // 2D arrays that represent the level's 10x10 tile grid
    private String[][] level1 = {
            {"E","E","TL","W","W","C","W","W","TR","E"},
            {"E","E","LE"," "," "," "," "," ","RE","E"},
            {"E","E","CL","ML"," "," "," ","MR","CR","E"},
            {"E","E","E","LS"," "," "," ","RS","E","E"},
            {"E","E","E","LS"," "," "," ","RS","E","E"},
            {"E","E","E","LS"," "," "," ","RS","E","E"},
            {"E","E","E","LS"," "," "," ","RS","E","E"},
            {"E","E","E","LS"," "," "," ","RS","E","E"},
            {"E","E","E","LS"," "," "," ","RS","E","E"},
            {"E","E","E","LS"," "," "," ","RS","E","E"}
        };

    private String[][] level2 = {
            {"E","E","E","E","TL","W","C","W","TR","E"}, 
            {"E","E","E","E","LE"," "," "," ","RE","E"},
            {"E","E","E","E","CL","ML"," "," ","RE","E"},
            {"TL","W","W","W","W","BL"," "," ","RE","E"},
            {"LE"," "," "," "," "," "," "," ","RE","E"},
            {"LE"," "," "," "," "," "," "," ","RE","E"},
            {"LE"," ","W","W","W","W","MR","CM","CR","E"},
            {"LE"," "," "," "," "," ","RS","E","E","E"},
            {"CL","CM","CM","ML"," "," ","RS","E","E","E"},
            {"W","W","W","BL"," "," ","BR","W","W","W"}
        };

    private String[][] level3 = {
            {"W","W","W","W","W","W","W","W","W","C"}, 
            {" "," "," "," "," "," "," "," "," "," "},
            {" ","W","W","W","W","W","W","TR","E","E"},
            {" "," "," "," "," "," "," ","RE","E","E"},
            {"CM","CM","CM","CM","ML"," "," ","RE","E","E"},
            {"E","E","TL","W","BL"," "," ","RE","E","E"},
            {"E","E","LE"," "," "," ","MR","CR","E","E"},
            {"E","E","CL","ML"," "," ","RS","E","E","E"},
            {"E","E","E","LS"," "," ","RS","E","E","E"},
            {"W","W","W","BL"," "," ","BR","W","W","W"}
        };

    private String[][] level4 = {
            {"E","E","LS","C","W","W","W","W","RS","E"}, 
            {"E","E","LS"," "," "," "," "," ","RS","E"},
            {"E","E","LS"," ","MT"," ","MT"," ","RS","E"},
            {"E","E","LS"," ","MM"," ","MM"," ","RS","E"},
            {"E","E","LS"," ","MM"," ","MM"," ","RS","E"},
            {"E","E","LS"," ","MM"," ","MM"," ","RS","E"},
            {"E","E","LS"," ","MM"," ","MM"," ","RS","E"},
            {"E","E","LS"," ","MM"," ","MM"," ","RS","E"},
            {"E","E","LS"," ","MB"," ","MB"," ","RS","E"},
            {"W","W","BL"," "," "," "," "," ","BR","W"}
        };

    private String[][] level5 = {
            {"W","W","W","W","W","C","W","W","W","W"}, 
            {" "," "," "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "},
            {" "," ","M"," ","MT"," "," "," ","M"," "},
            {" "," "," "," ","MM"," "," "," "," "," "},
            {" "," "," "," ","MM"," "," "," "," "," "},
            {" "," "," "," ","MM"," "," "," "," "," "},
            {" "," ","M"," ","MB"," "," "," ","M"," "},
            {" "," "," "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "}
        };

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public Dungeon()
    {    
        // Create a new world with 480x480 cells with a cell size of 1x1 pixels.
        super(480, 480, 1);

        setPaintOrder(Win.class, GameOver.class, Heart.class, Player.class, Bullet.class, Zombie.class, OpenedDoor.class); // setting paint order
        
        prepare();
    }

    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        loadLevel(level1); // creating tiles for the first level
        
        addObject(player,240,450); // adding player object at start point
        
        // setting health variables
        initialHealth = player.getHealth();
        currentHealth = player.getHealth();
        
        // adding hearts to top left of screen
        hearts = new Heart[player.getHealth()]; // new array the same size of the player's health
        for (int i = 0 ; i < player.getHealth() ; i++) // executes the same amount of time as the player's health
        {
            Heart heart = new Heart(); // creating new heart object
            hearts[i] = heart; // adding heart object to hearts array
            addObject(heart, i * 35 + 30, 30); // adding heart object to world at top left of screen
            objectsNum ++; // incrementing objectsNum
        }
        
        // creating zombies for level 1
        spawnZombies(3);
    }

    public void act()
    {
        checkHealth();
        
        checkForCompletedLevel();
        
        checkWalkingThroughDoor();
    }
    
    /**
     * Checks the content of a level 2D array and adds the corresponding objects to the world
     */
    private void loadLevel(String[][] level)
    {
        int i = 0;// to keep track of index of existingTiles
        for(int row = 0 ; row < level.length ; row++) // iterates through level rows
        {
            for (int col = 0 ; col < level[row].length ; col++) // iterates through level collums
            {
                if (level[row][col] == " ") // adding floors
                {
                    Floor floor = new Floor(); // creating new floor object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = floor;
                    i ++;

                    addObject(floor, col * 48 + 24, row * 48 + 24); // adding floor to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "W") // adding walls
                {
                    Wall wall = new Wall(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "C") // adding closed doors
                {
                    // creating new wall and door objects
                    Wall wall = new Wall();
                    ClosedDoor closedDoor = new ClosedDoor();

                    // adding new tiles to array of existing tiles
                    existingTiles[i] = wall;
                    i++;
                    existingTiles[i] = closedDoor;
                    i++;

                    lockedDoor = closedDoor; // setting the door that needs to be opened

                    // adding objects to world
                    addObject(wall, col * 48 + 24, row * 48 + 24);
                    addObject(closedDoor, col * 48 + 24, row * 48 + 24);
                    objectsNum += 2; // adding 2 to objectsNum
                }

                else if (level[row][col] == "E") // adding empty space
                {
                    Empty empty = new Empty(); // creating new empty object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = empty;
                    i++;

                    addObject(empty, col * 48 + 24, row * 48 + 24); // adding empty to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "BL") // adding bottom left wall
                {
                    WallBL wall = new WallBL(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "BR") // adding bottom right wall
                {
                    WallBR wall = new WallBR(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "CL") // adding connecting left wall
                {
                    WallCL wall = new WallCL(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "CM") // adding connecting middle wall
                {
                    WallCM wall = new WallCM(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "CR") // adding connecting right wall
                {
                    WallCR wall = new WallCR(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "LE") // adding left edge walls
                {
                    // creating new wall and floor objects
                    WallLE wall = new WallLE();
                    Floor floor = new Floor();

                    // adding new tiles to array of existing tiles
                    existingTiles[i] = wall;
                    i++;
                    existingTiles[i] = floor;
                    i++;

                    // adding objects to world
                    addObject(floor, col * 48 + 24, row * 48 + 24);
                    addObject(wall, col * 48 + 8, row * 48 + 24);

                    objectsNum += 2; // adding 2 to objectsNum
                }

                else if (level[row][col] == "LS") // adding left side wall
                {
                    WallLS wall = new WallLS(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "MB") // adding middle wall bottom part
                {
                    WallMB wall = new WallMB(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "ML") // adding middle left wall
                {
                    WallML wall = new WallML(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "MM") // adding middle wall middle part
                {
                    WallMM wall = new WallMM(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "MR") // adding middle right wall
                {
                    WallMR wall = new WallMR(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "MT") // adding middle wall top part
                {
                    WallMT wall = new WallMT(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "M") // adding middle stand alone wall
                {
                    WallMiddle wall = new WallMiddle(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "RE") // adding right edge walls
                {
                    // creating new wall and floor objects
                    WallRE wall = new WallRE();
                    Floor floor = new Floor();

                    // adding new tiles to array of existing tiles
                    existingTiles[i] = wall;
                    i++;
                    existingTiles[i] = floor;
                    i++;

                    // adding objects to world
                    addObject(floor, col * 48 + 24, row * 48 + 24);
                    addObject(wall, col * 48 + 40, row * 48 + 24);

                    objectsNum += 2; // adding 2 to objectsNum
                }

                else if (level[row][col] == "RS") // adding right side wall
                {
                    WallRS wall = new WallRS(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "TL") // adding top left wall
                {
                    WallTL wall = new WallTL(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }

                else if (level[row][col] == "TR") // adding top right wall
                {
                    WallTR wall = new WallTR(); // creating new wall object

                    // adding new tile to array of existing tiles
                    existingTiles[i] = wall;
                    i++;

                    addObject(wall, col * 48 + 24, row * 48 + 24); // adding wall to world
                    objectsNum ++; // incrementing objectsNum
                }
            }
        }
    }
    
    /**
     * Handles updating the number of hearts on screen and ending the game if player dies
     */
    private void checkHealth()
    {
        if (currentHealth > player.getHealth()) // if the World's variable for player health is bigger than the player's actual health
        {
            removeObject(hearts[player.getHealth()]); // remove heart from screen
            objectsNum -= 1; // decrease total number of objects in world
            currentHealth = player.getHealth(); // updating the World's variable for player health
            
            if (currentHealth <= 0) // if player dies
            {
                addObject(new GameOver(), getWidth()/2, getHeight()/2); // add game over object in middle of screen
                Greenfoot.playSound("lose.mp3"); // lose sound effect
                Greenfoot.stop(); // stopping game
            }
        }
    }
    
    /**
     * Checks if all zombies have been killed, therefore completing the level
     */
    private void checkForCompletedLevel()
    {
        if (lockedDoor != null)
        {
            if(numberOfObjects() <= objectsNum - zombiesNum) // if all zombie actors have been removed
            {
                // getting coordinates of closed door
                int doorX = lockedDoor.getX();
                int doorY = lockedDoor.getY();

                // adding an opened door on top of closed door
                OpenedDoor openedDoor = new OpenedDoor();
                addObject(openedDoor, doorX, doorY); // adding the door to the world
                
                // stores the opened door object at the end of existingTiles array to ensure it doesn't overide any other tiles
                existingTiles[existingTiles.length-1] = openedDoor;
                
                objectsNum ++; // incrementing objectsNum

                lockedDoor = null; // resetting lockedDoor
            }
        }
    }
    
    /**
     * Checks if player has walked through an opened door
     * If so, the level is changed
     */
    private void checkWalkingThroughDoor()
    {
        if (player.walkingThroughDoor()) // if player walks through opened door
        {
            currentLevel ++; // increment level
            objectsNum = 1 + currentHealth; // resetting number of objects. set to 1 + currentHealth to account for player and heart objects
            zombiesNum = 0; // resetting number of zombies
            player.setLocation(240, 477); // reset player location

            // this switches from level to level
            switch(currentLevel)
            {
                case 2:
                    nextLevel(level2); // load level 2
                    spawnZombies(5);
                    break;

                case 3:
                    nextLevel(level3); // load level 3
                    spawnZombies(8);
                    break;

                case 4:
                    nextLevel(level4); // load level 4
                    spawnZombies(12);
                    break;
                
                case 5:
                    nextLevel(level5); // load level 5
                    spawnZombies(15);
                    break;
                    
                case 6:
                    addObject(new Win(), getWidth()/2, getHeight()/2); // adding win object
                    Greenfoot.playSound("win.mp3"); // win sound effect
                    Greenfoot.stop(); // stopping game
                    break;
            }
        }
    }
    
    /**
     * Removes all tiles currently in the world
     * then loads in the next level
     */
    public void nextLevel(String[][] level)
    {
        for(int i = 0 ; i < existingTiles.length ; i ++) // iterates through objects in existingTiles and removes them
        {
            removeObject(existingTiles[i]);
        }

        loadLevel(level); // loads specified level
    }

    private void spawnZombies(int number)
    {
        for (int n = 0 ; n < number ; n++) // executes the same amount of time as the number of zombies specified
        {
            // these x and y coords will spawn all zombies at least 68 pixels from the edge of screen
            int x = Greenfoot.getRandomNumber(345) + 68;
            int y = Greenfoot.getRandomNumber(345) + 68;

            //makes sure the zombie is not intersecting any walls by checking corners around the zombies image for walls
            for(int dx = x - 15 ; dx <= x + 15 ; dx += 30) // 15 pixels to the left and right of zombie center
            {
                for(int dy = y - 20 ; dy <= y + 20 ; dy += 40) // 20 pixels up and down of the zombie center
                {
                    for (int i = 0 ; i < barrierObjects.length ; i++) // checks all barrier objects
                    {
                        if (getObjectsAt(dx, dy, barrierObjects[i]).size() != 0) // if there is a one of the barrier objects
                        {
                            n -= 1; // allows main loop to pick another x and y coord
                            dx = x + 16; // ends the first subloop
                            dy = y + 16; // ends second subloop
                            break;// ends this loop
                        }
                    }
                    
                    if (dx == x + 15 && dy == y + 20) // if it gets to end of loop, meaning there are no barriers intersecting
                        {
                            // add a new zombie at these coords
                            Actor zombie = new Zombie(Greenfoot.getRandomNumber(3) + 1, 0, 480, 0, 480); // randomly picks zombie movement speed from 1-4
                            addObject(zombie, x, y); // adding zombie to world

                            // incrementing zombiesNum and objectsNum
                            zombiesNum ++;
                            objectsNum ++;
                        }
                }
            }
        }
    }
}
