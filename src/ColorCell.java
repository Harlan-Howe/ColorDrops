import java.awt.*;

/**
 * Represents one color block on the grid of color blocks.
 */
public class ColorCell implements SharedConstants
{
    private Color myColor;
    private String myIcon;
    private boolean isAlive;

    public ColorCell(int cellTypeNum)
    {
        myColor = COLOR_LIST[cellTypeNum];
        myIcon = ICON_LIST[cellTypeNum];
        isAlive = true;
    }

    public Color getMyColor()
    {
        return myColor;
    }

    public boolean isAlive()
    {
        return isAlive;
    }

    public void die()
    {
        isAlive = false;
    }

    public void drawSelfAt(Graphics g, int x, int y, int boxSize)
    {
        if (isAlive)
        {
            g.setColor(myColor);
            g.fillRect(x, y, boxSize, boxSize);
            if (SHOW_ICONS)
            {
                g.setColor(Color.DARK_GRAY);
                g.setFont(CELL_ICON_FONT);
                g.drawString(myIcon, x + boxSize / 2 - 6, y + boxSize / 2 + 6);
            }
        }
    }
}
