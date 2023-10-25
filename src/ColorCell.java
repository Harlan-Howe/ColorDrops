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
        myIcon = CELL_ICONS[cellTypeNum];
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

    public void drawSelfAt(Graphics g, int x, int y, int size)
    {
        if (isAlive)
        {
            g.setColor(myColor);
            g.fillRect(x, y, size, size);
            if (SHOW_ICONS)
            {
                g.setColor(Color.DARK_GRAY);
                g.setFont(CELL_ICON_FONT);
                g.drawString(myIcon, x + size / 2 - 6, y + size / 2 + 6);
            }
        }
    }
}
