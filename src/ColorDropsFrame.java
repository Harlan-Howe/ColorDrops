import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ColorDropsFrame extends JFrame implements SharedConstants, ActionListener
{

    private ColorDropsPanel mainPanel;
    private JButton[] colorButtons;

    public ColorDropsFrame()
    {
        super("Color Drops");
        setSize(900,700);
        buildLayout();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
    }

    /**
     * sets up this window with a Column of buttons on the left and the main ColorDropsPanel on the right.
     */
    public void buildLayout()
    {
        mainPanel = new ColorDropsPanel();
        Box colorButtonsPanel = buildColorButtonPanel();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(colorButtonsPanel,BorderLayout.WEST);
    }

    /**
     * constructs the panel holding one "resetButton" at the top, and a column of color buttons below it.
     * @return the graphical panel holding these elements.
     */
    private Box buildColorButtonPanel()
    {
        Box colorButtonsPanel = Box.createVerticalBox();
        JButton instructionsButton = new JButton("Instructions");
        colorButtonsPanel.add(instructionsButton);
        instructionsButton.addActionListener(this);

        JButton resetButton = new JButton("Reset");
        colorButtonsPanel.add(resetButton);
        resetButton.addActionListener(this);
        colorButtons = new JButton[NUM_COLORS_USED];
        for (int i=0; i<NUM_COLORS_USED; i++)
        {
            colorButtons[i] = new ColorButton(COLOR_LIST[i], CELL_ICONS[i]);
            colorButtonsPanel.add(colorButtons[i]);
            colorButtons[i].setActionCommand("Color "+i);
            colorButtons[i].addActionListener(this);
        }
        return colorButtonsPanel;
    }

    /**
     * respond to a button being pressed. (That is to say, this method is automatically called when the user clicks a
     * button.)
     * @param ae the event to be processed - this has information about which button it was.
     */
    public void actionPerformed(ActionEvent ae)
    {
        String command = ae.getActionCommand();
        if (command.equals("Reset"))
        {
            mainPanel.resetAllCells();  // Make a new, full collection of random cells.
            for (JButton b: colorButtons)  // Enable all the color buttons again.
                b.setEnabled(true);
            return;
        }
        if (command.equals("Instructions"))
        {
            String instructions = "• Click on the colored buttons at left to remove all of that color from the grid at" +
                    " right.\n• Try to remove the correct cells so that the stacks exactly match the heights indicated" +
                    "by the dotted lines.\n• If you need to start over, click the Reset button to try again.";
            JOptionPane.showMessageDialog(this, instructions);
        }
        if(command.length() > 6 && command.substring(0,6).equals("Color "))  // if this is a color button...
        {
            // Tell the main panel to kill all the cells of the corresponding color.
            int whichColor = Integer.parseInt(command.substring(6));
            mainPanel.killAllCellsOfColor(COLOR_LIST[whichColor]);

            // prevent clicking this colorButton again. (automatically turns the button grey.)
            ((JButton)ae.getSource()).setEnabled(false);

            // if the game isn't still running, disable all the color buttons.
            if (mainPanel.getGameState() != STATUS_KEEP_GOING )
                for (JButton b: colorButtons)
                    b.setEnabled(false);
        }
    }


    // The following is an internal class -- it's just used by the ColorDropsFrame class.
    // (You don't need to know how/why to do this.)
    /**
     * A ColorButton is just a variation of JButton that displays a square of solid color and an optional icon.
     */
    public class ColorButton extends JButton
    {
        private Color myColor;

        public ColorButton(Color c, String icon)
        {
            super();
            myColor = c;
            BufferedImage bi = new BufferedImage(BUTTON_SIZE, BUTTON_SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics g2 = bi.getGraphics();
            g2.setColor(myColor);
            g2.fillRect(0,0,BUTTON_SIZE,BUTTON_SIZE);
            if (SHOW_ICONS)
            {
                g2.setColor(Color.DARK_GRAY);
                g2.setFont(CELL_ICON_FONT);
                g2.drawString(icon, 8, 20);
            }
            setIcon(new ImageIcon(bi));
        }

    }


}
