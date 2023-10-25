import javax.swing.*;
import java.awt.*;

public class ColorDropsPanel extends JPanel implements SharedConstants
{

    private ColorCell[][] myGrid;
    private int[] levels;
    private SoundPlayer soundPlayer;
    private Stroke dottedLine;
    private int gameState;
    private boolean firstReset;

    public ColorDropsPanel()
    {
        super();
        myGrid = new ColorCell[NUM_ROWS][NUM_COLS];
        levels = new int[NUM_COLS];
        setBackground(Color.BLACK);
        float[] dashes = {5, 5};
        dottedLine = new BasicStroke(CELL_BORDER, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, dashes,0);
        loadSounds();
        firstReset = true; // don't play the reset sound the first time...
        resetAllCells();
        firstReset = false; // ok, now if you reset, you can play the sound...
    }

    /**
     * preload sound files for more responsive sound playback
     */
    public void loadSounds()
    {
        soundPlayer = new SoundPlayer();
        soundPlayer.loadSound("EnergyBounce.wav"); // Energy Bounce by "magnuswalker" at https://freesound.org/s/523088/ shared via Creative Commons
        soundPlayer.loadSound("Punch.wav"); // "Martial arts fast punch" at https://mixkit.co/free-sound-effects/
        soundPlayer.loadSound("Chirp.wav"); // "Retro game notification" at https://mixkit.co/free-sound-effects/
        soundPlayer.loadSound("Hmm.wav"); // Hmm sound by "DAN2008" at https://freesound.org/s/165011/ shared via Creative Commons
        soundPlayer.loadSound("Reveal.wav"); // Reveal sound by "GameAudio" at https://freesound.org/s/220171/ shared via Creative Commons
        soundPlayer.loadSound("Reset.wav"); // Reset sound by "Wdomino" at https://freesound.org/s/508575/ shared via Creative Commons
    }

    /**
     * Fills the grid with all new ColorCells.
     */
    public void resetAllCells()
    {
        for (int r = 0; r < NUM_ROWS; r++)
        {
            for (int c = 0; c < NUM_COLS; c++)
            {
                myGrid[r][c] = new ColorCell((int)(Math.random()*NUM_COLORS_USED));
            }
        }
        pickLevels();
        gameState = STATUS_KEEP_GOING;
        repaint();
        if (!firstReset)
            soundPlayer.playSound("Reset.wav");
    }

    public int getGameState()
    {
        return gameState;
    }

    /**
     * Selects what the target height should be for each column... which translates to the height of the dotted lines.
     */
    public void pickLevels()
    {
        Color[] targetColors = new Color[NUM_COLORS_USED-1];
        for (int i=0; i<NUM_COLORS_USED -1; i++)
            targetColors[i] = COLOR_LIST[(int)(Math.random()*NUM_COLORS_USED)];

        for (int c=0; c<NUM_COLS; c++)
        {
            int count = 0;
            for (ColorCell[] row: myGrid)
            {
                boolean matched = false;
                for (Color col: targetColors)
                    if (row[c].getMyColor() == col)
                    {
                        matched = true;
                        break;
                    }
                if (matched)
                    count++;
            }
            levels[c] = count;
        }
    }

    /**
     * this is what actually draws this screen. You should never call this directly; instead, call "repaint()" -- which
     * will tell the computer to call this method at its next convenience.
     * @param g the <code>Graphics</code> object that represents this portion of the window and the tools to draw in it.
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        int size = Math.min((int)(getWidth()/NUM_COLS), (int)(getHeight()/NUM_ROWS));
        drawCells(g, size);
        drawLevels(g, size);
    }

    /**
     * Draw all the cells in the grid.
     * @param g - the graphics element that represents the visual area of this panel and the tools to draw in it.
     * @param size - the spacing of the cells. Also the size of the drawn Cell+border in width and height.
     */
    private void drawCells(Graphics g, int size)
    {
        for (int r = 0; r < NUM_ROWS; r++)
        {
            for (int c = 0; c < NUM_COLS; c++)
            {
                myGrid[r][c].drawSelfAt(g,
                        c * size + 2 * CELL_BORDER,
                        r * size + 2 * CELL_BORDER,
                        size - 2 * CELL_BORDER);
            }
        }
    }

    /**
     * draws the dotted lines indicating the target levels for the blocks.
     * @param g - the graphics element that represents the visual area of this panel and the tools to draw in it.
     * @param size - the spacing of the cells. Also the size of the drawn Cell+border in width and height.
     */
    public void drawLevels(Graphics g, int size)
    {
        ((Graphics2D)g).setStroke(dottedLine);
        g.setColor(Color.WHITE);
        for (int c = 0; c < NUM_COLS; c++)
        {
            int y = (NUM_ROWS - levels[c]) * size+CELL_BORDER;
            g.drawLine(c*size+CELL_BORDER, y, (c*size+size)+CELL_BORDER, y);
            if (c<NUM_COLS - 1)
            {
                int y2 = (NUM_ROWS - levels[c+1])*size+CELL_BORDER;
                g.drawLine(c*size+size+CELL_BORDER, y, c*size+size+CELL_BORDER, y2);
            }
        }
    }

    /**
     * loops through the entire grid and tells all cells with a color matching colorToKill to die(), then tells the
     * remaining cells to drop down to fill empty spaces and notifies the program that the screen needs an update.
     * @param colorToKill - the Color that we wish to eliminate.
     */
    public void killAllCellsOfColor(Color colorToKill)
    {
        for (int r = 0; r < NUM_ROWS; r++)
        {
            for (int c = 0; c < NUM_COLS; c++)
            {
                if (myGrid[r][c].getMyColor() == colorToKill)
                    myGrid[r][c].die();
            }
        }
        dropCells();
        repaint();
    }

    /**
     * for each column, shuffles any live cells downwards to fill any holes left by dead cells. (Actually swaps the
     * dead ones with the "falling" cell.)
     */
    public void dropCells()
    {
        for (int r = NUM_ROWS-1; r >0; r--)
            for (int c = 0; c < NUM_COLS; c++)
                if (!myGrid[r][c].isAlive())
                {
                    for (int r2 = r-1; r2 >=0; r2--)
                    {
                        if (myGrid[r2][c].isAlive())
                        {
                            ColorCell temp = myGrid[r][c];
                            myGrid[r][c] = myGrid[r2][c];
                            myGrid[r2][c] = temp;
                            break;
                        }
                    }
                }
        checkLevels();
    }

    public void checkLevels()
    {
        // TODO: consider the number of live cells in each column. Are there still more cells to remove, have we matched
        //  the target, or did the user overshoot and now there are too few somewhere?
        //  Alter gameState to be either STATUS_KEEP_GOING, STATUS_WIN, or STATUS_LOSE, accordingly.

        int[] counts = new int[NUM_COLS];
        for (int c = 0; c < NUM_COLS; c++)
            for (int r = 0; r < NUM_ROWS; r++)
                if (myGrid[r][c].isAlive())
                    counts[c] ++;

        for (int c = 0; c < NUM_COLS; c++)
            if (counts[c] < levels[c])
                gameState = STATUS_LOSE;
        if (gameState == STATUS_KEEP_GOING)
        {
            gameState = STATUS_WIN;
            for (int c = 0; c < NUM_COLS; c++)
                if (counts[c] > levels[c])
                {
                    gameState = STATUS_KEEP_GOING;
                    break;
                }
        }

        // after you have done that, we'll play a sound:
        if (gameState == STATUS_KEEP_GOING)
            soundPlayer.playSound("Hmm.wav");
        else if (gameState == STATUS_WIN)
            soundPlayer.playSound("Chirp.wav");
        else
            soundPlayer.playSound("EnergyBounce.wav");
    }

}
