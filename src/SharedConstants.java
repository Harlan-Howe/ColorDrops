import java.awt.*;

/**
 * This is a file full of constants that will be shared by the other classes. It is an "interface," which means that
 * the other classes say they will "implement SharedConstants" on the class declaration line at the top of their files.
 */
public interface SharedConstants
{
    // the available colors of the cells and buttons.
    public final static Color[] COLOR_LIST = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA,
            Color.CYAN, Color.PINK, Color.WHITE};

    // the corresponding icons that might be drawn on the colors.
    public final static String[] CELL_ICONS =  {"•", "+", "x", "∆", "*", "◊", "≈", ":"};
    // the font used to draw the icons.
    public final static Font CELL_ICON_FONT = new Font("Arial", Font.BOLD, 24);
    // whether or not to draw the icons. (Might be handy for color-blind users... otherwise I find it noisy.)
    public final static boolean SHOW_ICONS = false;

    // How many colors will actually be used in the game... this should be ≤ length of the COLOR_LIST.
    public final static int NUM_COLORS_USED = 6;

    // Size of the grid
    public final static int NUM_ROWS = 15;
    public final static int NUM_COLS = 15;

    // How large the box of color in the buttons is
    public final static int BUTTON_SIZE = 30;

    // The spacing between cells and the thickness of the dotted line.
    public final static int CELL_BORDER = 2;

    // game states
    public final static int STATUS_KEEP_GOING = 0;
    public final static int STATUS_WIN = 1;
    public final static int STATUS_LOSE = 2;
}
