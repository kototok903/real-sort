import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.awt.Dimension;
import java.util.Arrays;

// Panel where array is displayed
public class Display extends JPanel implements ActionListener, ComponentListener, MouseListener, KeyListener, ChangeListener, FocusListener {
    // Constants
    private final int DEFAULT_FPS = 60;
    private final int MIN_WIDTH = 600, MIN_HEIGHT = 400;
    private final int MAX_ELEMENT_WIDTH = 30;
    private final int MAX_ELEMENT_HEIGHT = 500;
    private final int MARGIN = 40;
    private final int MIN_FPS = 5, MAX_FPS = 600;
    private final int DEFUALT_ARRAY_LENGTH = 110;
    private final int MIN_ARRAY_LENGTH = 4;
    private final int MAX_ARRAY_LENGTH = 4096;
    private final int CONTROLS_PANEL_HEIGHT = 50;
    private static enum DisplayState {
        NOTHING,
        SORT,
        COMPARE,
    }
    
    // Colors
    private Color bgColor; // 0
    private Color inColor; // 1
    private Color outColor; // ...
    private Color inHLColor;
    private Color outHLColor;
    private Color areaColor;
    
    private Color[][] themes = new Color[][] { // theme -> color
        new Color[] { // Light
            new Color(238, 238, 238), // bg
            new Color(165, 255, 214), new Color(132, 220, 198), // element
            new Color(255, 166, 158), new Color(255, 104, 107), // hl
            Color.black, // area
            Color.black, // text
        },
        new Color[] { // Light Green
            new Color(237, 246, 249), // bg
            new Color(166, 213, 208), new Color(98, 181, 173), // element
            new Color(255, 150, 114), new Color(209, 88, 44), // hl
            Color.black, // area
            Color.black, // text
        },
        new Color[] { // Black
            Color.black, // bg
            new Color(248, 248, 248), new Color(210, 210, 210), // element
            new Color(245, 0, 5), new Color(225, 0, 5), // hl
            new Color(248, 248, 248), // area
            Color.white, // text
        },
        new Color[] { // Discord
            new Color(54, 57, 63), // bg
            new Color(160, 166, 232), new Color(89, 99, 207), // element
            new Color(88, 101, 242), new Color(45, 61, 239), // hl
            new Color(88, 101, 242), // area
            Color.white, // text
        },
        new Color[] { // Night
            new Color(7, 8, 46), // bg
            new Color(118, 120, 237), new Color(61, 52, 139), // element
            new Color(247, 184, 1), new Color(241, 135, 1), // hl
            new Color(243, 91, 4), // area
            Color.white, // text
        },
    };
    
    // Variables
    private int sortWidth, sortHeight;
    private int maxElement, maxElementHeight;
    private double elementWidth;
    private int arrayLength, newArrayLength;
    private DisplayState scene;
    private ArrayList<ArrayState> arrayStates;
    private int arrayStatesIndex;
    private ArraySorter sorter;
    private Timer timer; // the update timer
    // States
    private boolean isPaused;
    private boolean isForward;
    private boolean isControlsHidden;
    // Temp
    private char action;
    private Dimension dim;
    private int oldSW;
    
    // GUI elements
    private JFrame frame;
    // Menu
    private JMenuBar menuBar;
    private JMenu sortsMenu, themeMenu, settingsMenu;//, compareMenu;
    private JMenuItem[] sortsItems;
    //private JCheckBoxMenuItem[] compareItems;
    //private JMenuItem compareItem;
    private JRadioButtonMenuItem[] themeItems;
    private JCheckBoxMenuItem hideControlsItem;
    private JMenuItem helpItem;
    // Controls
    private JPanel controlsPanel;
    private JButton pauseButton, reverseButton, forwardButton;
    private JPanel speedGroup, elementsGroup;
    private JLabel speedLabel, elementsLabel;
    private JSlider fpsSlider;
    private JTextField arrayLengthField;
    
    
    public Display(String title, int winW, int winH) {
        // Set Up variables
        scene = DisplayState.NOTHING;
        arrayStates = new ArrayList<ArrayState>();
        isPaused = false;
        newArrayLength = DEFUALT_ARRAY_LENGTH;
        isForward = true;
        isControlsHidden = false;
        
        // Set Up Frame 1
        frame = new JFrame();
        frame.setTitle(title);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(winW, winH));
        
        // Set Up Menu Bar
        menuBar = new JMenuBar();
        
        sortsMenu = new JMenu("Visualize Sorting Algorithms");
        sortsMenu.setMnemonic(KeyEvent.VK_V);
        menuBar.add(sortsMenu);
        //compareMenu = new JMenu("Compare Sorting Algorithms");
        //compareMenu.setMnemonic(KeyEvent.VK_C);
        //menuBar.add(compareMenu);
        themeMenu = new JMenu("Theme");
        themeMenu.setMnemonic(KeyEvent.VK_T);
        menuBar.add(themeMenu);
        settingsMenu = new JMenu("Settings");
        settingsMenu.setMnemonic(KeyEvent.VK_S);
        menuBar.add(settingsMenu);
        
        sortsItems = new JMenuItem[]{
            new JMenuItem("Bubble Sort"),
            new JMenuItem("Selection Sort"),
            new JMenuItem("Insertion Sort"),
            new JMenuItem("Binary Insertion Sort"),
            new JMenuItem("Shell Sort"),
            new JMenuItem("Merge Sort"),
            new JMenuItem("Quick Sort (LL)"),
            new JMenuItem("Quick Sort (LR)"),
            new JMenuItem("Tim Sort"),
            new JMenuItem("Counting Sort"),
            new JMenuItem("Radix Sort (base 10, LSD)"),
            new JMenuItem("Radix Sort (base 10, MSD)"),
        };
        
        sortsItems[0].setActionCommand("vb");
        sortsItems[1].setActionCommand("vs");
        sortsItems[2].setActionCommand("vi");
        sortsItems[3].setActionCommand("vI");
        sortsItems[4].setActionCommand("vS");
        sortsItems[5].setActionCommand("vm");
        sortsItems[6].setActionCommand("vq");
        sortsItems[7].setActionCommand("vQ");
        sortsItems[8].setActionCommand("vt");
        sortsItems[9].setActionCommand("vc");
        sortsItems[10].setActionCommand("vr");
        sortsItems[11].setActionCommand("vR");
        
        for (JMenuItem item : sortsItems) {
            item.addActionListener(this);
            sortsMenu.add(item);
        }
        
        /*compareItems = new JCheckBoxMenuItem[]{
            new JCheckBoxMenuItem("Bubble Sort"),
            new JCheckBoxMenuItem("Selection Sort"),
            new JCheckBoxMenuItem("Insertion Sort"),
            new JCheckBoxMenuItem("Binary Insertion Sort"),
            new JCheckBoxMenuItem("Merge Sort"),
            new JCheckBoxMenuItem("Quick Sort (LL)"),
            new JCheckBoxMenuItem("Quick Sort (LR)"),
            new JCheckBoxMenuItem("Tim Sort"),
        };
        
        compareItem = new JMenuItem("Compare");
        compareItem.setActionCommand("C");
        compareItem.addActionListener(this);
        
        for (JCheckBoxMenuItem item : compareItems) {
            item.setActionCommand("c");
            item.addActionListener(this);
            compareMenu.add(item);
        }
        
        compareMenu.addSeparator();
        compareMenu.add(compareItem);*/
        
        themeItems = new JRadioButtonMenuItem[] {
            new JRadioButtonMenuItem("Light"),
            new JRadioButtonMenuItem("Light Green"),
            new JRadioButtonMenuItem("Black"),
            new JRadioButtonMenuItem("Discord"),
            new JRadioButtonMenuItem("Night"),
        };
        themeItems[0].setSelected(true);
        
        themeItems[0].setActionCommand("t0");
        themeItems[1].setActionCommand("t1");
        themeItems[2].setActionCommand("t2");
        themeItems[3].setActionCommand("t3");
        themeItems[4].setActionCommand("t4");
        
        for (JRadioButtonMenuItem item : themeItems) {
            item.addActionListener(this);
            themeMenu.add(item);
        }
        
        hideControlsItem = new JCheckBoxMenuItem("Hide Controls");
        hideControlsItem.setActionCommand("sh");
        hideControlsItem.addActionListener(this);
        settingsMenu.add(hideControlsItem);
        
        helpItem = new JMenuItem("Help");
        helpItem.setActionCommand("sH");
        helpItem.addActionListener(this);
        settingsMenu.add(helpItem);
        
        frame.setJMenuBar(menuBar);
        
        // Set Up Sort Panel
        this.addMouseListener(this);
        
        frame.add(this);
        
        // Set Up Controls Panel
        controlsPanel = new JPanel();
        controlsPanel.setLayout(new FlowLayout());
        
        reverseButton = new JButton("⏪"); // fast reverse
        reverseButton.setActionCommand("kB");
        reverseButton.addActionListener(this);
        reverseButton.setFocusable(false);
        controlsPanel.add(reverseButton);
        
        pauseButton = new JButton("⏯"); // pause
        pauseButton.setActionCommand("kp");
        pauseButton.addActionListener(this);
        pauseButton.setFocusable(false);
        controlsPanel.add(pauseButton);
        
        forwardButton = new JButton("⏩"); // fast forward
        forwardButton.setActionCommand("kF");
        forwardButton.addActionListener(this);
        forwardButton.setFocusable(false);
        controlsPanel.add(forwardButton);
        
        speedGroup = new JPanel();
        speedGroup.setLayout(new BorderLayout());
        
        speedLabel = new JLabel("   Speed: ");
        speedGroup.add(speedLabel, BorderLayout.WEST);
        
        fpsSlider = new JSlider(MIN_FPS, MAX_FPS, 30);
        fpsSlider.setMinorTickSpacing(5);
        //fpsSlider.setPaintTicks(true);
        fpsSlider.setSnapToTicks(true);
        fpsSlider.addChangeListener(this);
        speedGroup.add(fpsSlider, BorderLayout.EAST);

        controlsPanel.add(speedGroup);
        
        elementsGroup = new JPanel();
        elementsGroup.setLayout(new BorderLayout());
        
        elementsLabel = new JLabel("  Elements:  ");
        elementsGroup.add(elementsLabel, BorderLayout.WEST);
        
        arrayLengthField = new JTextField(newArrayLength + "", 5);
        arrayLengthField.addFocusListener(this);
        elementsGroup.add(arrayLengthField, BorderLayout.EAST);
        
        controlsPanel.add(elementsGroup);
        
        frame.add(controlsPanel, BorderLayout.PAGE_END);
        
        // Set Up Frame 2
        frame.addKeyListener(this);
        frame.addComponentListener(this);
        
        frame.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        
        // Set Up some variables
        updateSize();
        setTheme(0);
        setUpFPS();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == null) { // update screen
            update();
            repaint();
            return;
        }
        
        action = e.getActionCommand().charAt(0);
        
        switch (action) {
            case 'v': // Sort Visulization
                isPaused = false;
                isForward = true;
            
                action = e.getActionCommand().charAt(1);
                
                arrayLength = newArrayLength; // update arrayLength
                
                ArrayState a = new ArrayState(arrayLength);
                a.fillArrayInOrderFrom(1);
                a.shuffle();
                
                arrayStates = new ArrayList<ArrayState>();
                arrayStates.add(a);
                arrayStatesIndex = 0;
                
                sorter = new ArraySorter(arrayStates);
                
                maxElement = a.maxElement();
                maxElementHeight = sortHeight - 2 * MARGIN;
                elementWidth = (double)(sortWidth - 2 * MARGIN) / arrayLength;
                
                scene = DisplayState.SORT;
                
                switch (action) {
                    case 'b':
                        sorter.bubbleSort();
                        break;
                    case 's':
                        sorter.selectionSort();
                        break;
                    case 'i':
                        sorter.insertionSort();
                        break;
                    case 'I':
                        sorter.binaryInsertionSort();
                        break;
                    case 'S':
                        sorter.shellSort();
                        break;
                    case 'm':
                        sorter.mergeSort();
                        break;
                    case 'q':
                        sorter.quickSortLL();
                        break;
                    case 'Q':
                        sorter.quickSortLR();
                        break;
                    case 't':
                        sorter.timSort();
                        break;
                    case 'c':
                        sorter.countingSort();
                        break;
                    case 'r':
                        sorter.radixSort10LSD();
                        break;
                    case 'R':
                        sorter.radixSort10MSD();
                        break;
                }
                break;
                
            /*case 'c': // Sort Comparison element
                compareMenu.doClick(0);
                break;*/
                
            case 't': // Theme
                int ti = (int)e.getActionCommand().charAt(1) - '0';
                
                // Unselect other themes
                for (JRadioButtonMenuItem item : themeItems) {
                    item.setSelected(false);
                }
                themeItems[ti].setSelected(true);
                
                setTheme(ti);
                
                break;
                
            case 's':
                action = e.getActionCommand().charAt(1);
                
                switch (action) {
                    case 'h':
                        isControlsHidden = !isControlsHidden;
                        
                        if (isControlsHidden) {
                            frame.remove(controlsPanel);
                        }
                        else {
                            frame.add(controlsPanel, BorderLayout.PAGE_END);
                        }
                        
                        updateSize();
                        frame.revalidate();
                        frame.repaint();
                        break;
                        
                    case 'H':
                        scene = DisplayState.NOTHING;
                        break;
                }
                break;
                
            case 'k':
                action = e.getActionCommand().charAt(1);
                
                switch (action) {
                    case 'p':
                        isPaused = !isPaused;
                        //pauseButton.setText(isPaused ? "▶" : "⏸");
                        break;
                        
                    case 'F':
                        isForward = true;
                        break;
                        
                    case 'B':
                        isForward = false;
                        break;
                }
                break;
        }
    }
    
    public void setTheme(int ti) {
        bgColor = themes[ti][0];
        inColor = themes[ti][1];
        outColor = themes[ti][2];
        inHLColor = themes[ti][3];
        outHLColor = themes[ti][4];
        areaColor = themes[ti][5];
        speedLabel.setForeground(themes[ti][6]); // text
        elementsLabel.setForeground(themes[ti][6]);
        
        controlsPanel.setBackground(bgColor);
        speedGroup.setBackground(bgColor);
        fpsSlider.setBackground(bgColor);
        elementsGroup.setBackground(bgColor);
    }
    
    
    public void componentHidden(ComponentEvent e) { }
    
    public void componentMoved(ComponentEvent e) { }
    
    public void componentResized(ComponentEvent e) {
        updateSize();
    }
    
    public void componentShown(ComponentEvent e) { }
    
    public void updateSize() {
        oldSW = sortWidth;
        
        sortWidth = frame.getContentPane().getBounds().width;
        sortHeight = frame.getContentPane().getBounds().height;
        if (!isControlsHidden) {
            sortHeight -= controlsPanel.getHeight();
        }
        
        maxElementHeight = sortHeight - 2 * MARGIN;
        elementWidth = (double)(sortWidth - 2 * MARGIN) / arrayLength;
        
        if (sortWidth >= 480 && oldSW < 480) { // change to big
            sortsMenu.setText("Visualize Sorting Algorithms");
            //compareMenu.setText("Compare Sorting Algorithms");
        }
        else if (sortWidth < 470 && oldSW >= 470) { // change to small
            sortsMenu.setText("Visualize");
            //compareMenu.setText("Compare");
        }
    }
    
    
    // Mouse
    public void mouseClicked(MouseEvent e) {
        // for Debuging
        //System.out.println(e.getX() + " " + e.getY());
    }
    
    public void mousePressed(MouseEvent e) { }
    
    public void mouseReleased(MouseEvent e) { }
    
    public void mouseEntered(MouseEvent e) { }
    
    public void mouseExited(MouseEvent e) { }
    
    
    // Keyboard
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                isPaused = !isPaused;
                //pauseButton.setText(isPaused ? "▶" : "⏸");
                break;
        }
    }
   
    public void keyReleased(KeyEvent e) { }
    
    public void keyTyped(KeyEvent e) { }
    
    
    // Slider moved
    public void stateChanged(ChangeEvent e) {
        setFPS(fpsSlider.getValue());
    }
    
    private void setFPS(int fps) {
        if (timer != null) {
            timer.setDelay(1000 / fps);
        }
    }
    
    public void setUpFPS() {
        if(timer != null) {
            timer.stop();
        }
        
        timer = new Timer(1000 / DEFAULT_FPS, this); // roughly FPS frames per second
        timer.start();
    }
    
    
    public void focusGained(FocusEvent e) { }

    public void focusLost(FocusEvent e) {
        try {
            int al = Integer.parseInt(arrayLengthField.getText().trim());
            newArrayLength = Math.max(Math.min(al, MAX_ARRAY_LENGTH), MIN_ARRAY_LENGTH);
            arrayLengthField.setText(newArrayLength + "");
        } 
        catch (Exception ex) {
            arrayLengthField.setText(newArrayLength + "");
        }
    }
    
    
    public void update() {
        switch (scene) {
            case SORT:
                if (isPaused) {
                    break;
                }
                
                if (isForward) {
                    arrayStatesIndex = Math.min(arrayStatesIndex + 1, arrayStates.size() - 1);
                }
                else {
                    arrayStatesIndex = Math.max(arrayStatesIndex - 1, 0);
                }
                break;
                
            case COMPARE:
                
                break;
        }
    }
    
    public void paint(Graphics g) {
        g.setColor(bgColor);
        g.fillRect(0, 0, sortWidth, sortHeight);
        
        switch (scene) {
            case SORT:
                int[] a = arrayStates.get(arrayStatesIndex).getArray();
                int[] hl = arrayStates.get(arrayStatesIndex).getHighlighted();
                int hAmount = arrayStates.get(arrayStatesIndex).getHighlightedAmount();
                int aL = arrayStates.get(arrayStatesIndex).getAreaLeftIndex();
                int aR = arrayStates.get(arrayStatesIndex).getAreaRightIndex();
                
                int posY = sortHeight - MARGIN;
                int leftX = MARGIN;
                
                g.setColor(inColor);
                for (int i = 0; i < a.length; i++) {
                    g.fillRect((int)(leftX + elementWidth * i), posY - (int)((double)maxElementHeight / maxElement * a[i]), 
                        (int)elementWidth, (int)((double)maxElementHeight / maxElement * a[i]));
                }
                
                g.setColor(outColor);
                for (int i = 0; i < a.length; i++) {
                    g.drawRect((int)(leftX + elementWidth * i), posY - (int)((double)maxElementHeight / maxElement * a[i]), 
                        (int)elementWidth, (int)((double)maxElementHeight / maxElement * a[i]));
                }
                
                g.setColor(inHLColor);
                for (int i = 0; i < hAmount; i++) {
                    g.fillRect((int)(leftX + elementWidth * hl[i]), posY - (int)((double)maxElementHeight / maxElement * a[hl[i]]), 
                        (int)elementWidth, (int)((double)maxElementHeight / maxElement * a[hl[i]]));
                }
                
                g.setColor(outHLColor);
                for (int i = 0; i < hAmount; i++) {
                    g.drawRect((int)(leftX + elementWidth * hl[i]), posY - (int)((double)maxElementHeight / maxElement * a[hl[i]]), 
                        (int)elementWidth, (int)((double)maxElementHeight / maxElement * a[hl[i]]));
                }
                
                if (aL != -1) {
                    g.setColor(areaColor);
                    g.fillRect((int)(leftX + elementWidth * aL), posY + (int)Math.max(elementWidth * 2 / 3, 3), 
                        (int)(elementWidth * (aR - aL + 1)), 2);
                }
                break;
                
            case NOTHING:
                g.setColor(Color.black);
                g.setFont(new Font("Arial", Font.PLAIN, 14));
                g.drawString("The sorting algorithms visualizer Real Sort by Stas B", 10, 40);
                g.drawString("Click Visualize Sorting Algorithms to begin.", 10, 75);
                g.drawString("Use controls at the bottom to control sorting process.", 10, 100);
                g.drawString("Controls can be hidden.", 10, 125);
                g.drawString("Window can be resized.", 10, 150);
                break;
        }
    }
}
