import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.*;

public class Game {
    public static int ROWS;
    public static int COLS;

    private JFrame frame;
    private JFrame solve;
    private JButton buttons[][];
    private JButton solveButtons[][];

    private int action = 0;
    private State initialState;
    private State currentState;
    private ArrayList<State> solveStates;

    private ArrayList<String> assets;

    private Path solveWinActions;
    private boolean hasWon = false;

    private HashMap<String, ImageIcon> iconMap = new HashMap<String, ImageIcon>();

    private String direction;

    public Game() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter a number");
        ROWS = scan.nextInt();
        COLS = ROWS;

        this.assets = new ArrayList<String>();
        this.setAssets();
        this.direction = "Front";
        this.initializeIconMap();
        this.generateMaze();
        this.initializeUI();
        this.render();
        this.checkWin();
    }
    public void setAssets(){
      this.assets.add("t");
      this.assets.add("W");
    }

    public void generateMaze(){
      String[][] contents = new String[Game.ROWS][Game.COLS];
      int pI = (int)(Math.random() * ROWS);
      int pJ = (int)(Math.random() * ROWS);
      int gI = (int)(Math.random() * ROWS);
      int gJ = (int)(Math.random() * ROWS);
      if((pI == gI) && (pJ == gJ)){
        gI = (int)(Math.random() * ROWS);
        gJ = (int)(Math.random() * ROWS);
      }
      String c;
      for (int i = 0; i < Game.ROWS; i++) {
          for (int j = 0; j < Game.COLS; j++) {
              c = assets.get((int)(Math.random() * assets.size())); //c is random string from assets
              if((i == pI) && (j==pJ)){
                contents[i][j] = "P";
              }else if((i == gI) && (j==gJ)){
                contents[i][j] = "G";
              }else{
                contents[i][j] = c;
              }
          }
      }
      this.currentState = new State(contents);
      this.initialState = new State(contents);
    }

// ceate hashmap if tile -> specific icon
    public void initializeIconMap() {
        String prefix = "PNG/";


        iconMap.put(State.UNVISITED, new ImageIcon(prefix + "GroundGravel_Concrete.png"));

        ArrayList<String> keeperFrontFilenames = new ArrayList<String>(
            Arrays.asList(
                prefix + "GroundGravel_Concrete.png",
                prefix + "Character_Front.png"
            )
        );

        iconMap.put(State.KEEPER + "Front", combineIcon(keeperFrontFilenames));

        ArrayList<String> keeperBackFilenames = new ArrayList<String>(
            Arrays.asList(
                prefix + "GroundGravel_Concrete.png",
                prefix + "Character_Back.png"
            )
        );

        iconMap.put(State.KEEPER + "Back", combineIcon(keeperBackFilenames));

        ArrayList<String> keeperLeftFilenames = new ArrayList<String>(
            Arrays.asList(
                prefix + "GroundGravel_Concrete.png",
                prefix + "Character_Left.png"
            )
        );

        iconMap.put(State.KEEPER + "Left", combineIcon(keeperLeftFilenames));

        ArrayList<String> keeperRightFilenames = new ArrayList<String>(
            Arrays.asList(
                prefix + "GroundGravel_Concrete.png",
                prefix + "Character_Right.png"
            )
        );

        iconMap.put(State.KEEPER + "Right", combineIcon(keeperRightFilenames));

        ArrayList<String> storageFilenames = new ArrayList<String>(
            Arrays.asList(
                prefix + "GroundGravel_Concrete.png",
                prefix + "EndPoint_Blue.png"
            )
        );

        iconMap.put(State.GOAL, combineIcon(storageFilenames));

        ArrayList<String> wallFilenames = new ArrayList<String>(
            Arrays.asList(
                prefix + "GroundGravel_Concrete.png",
                prefix + "Wall_Gray.png"
            )
        );
          iconMap.put(State.WALL, combineIcon(wallFilenames));

        iconMap.put(State.VISITED, new ImageIcon(prefix + "GroundGravel_Grass.png"));

        ArrayList<String> keeperStorageFrontFilenames = new ArrayList<String>(
            Arrays.asList(
                prefix + "GroundGravel_Concrete.png",
                  prefix + "Character_Front.png"
            )
        );
    }

    public void initializeUI() {
        frame = new JFrame("Sokoban");

        buttons = new JButton[Game.ROWS][Game.COLS];

        Container pane = frame.getContentPane();
        JPanel panel = new JPanel(new GridLayout(ROWS+1, COLS));
        pane.add(panel);

        // initialize all grid buttons
        for (int i = 0; i < Game.ROWS; i++) {
            for (int j = 0; j < Game.COLS; j++) {
                JButton button = new JButton();
                button.setFocusable(false);
                button.setPreferredSize(new Dimension(64, 64)); //tile size

                panel.add(button);

                buttons[i][j] = button;
            }
        }

        JButton button = new JButton("S");
        button.setPreferredSize(new Dimension(64, 64)); //tile size
        panel.add(button);
        button.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
              State tempState= currentState;
              solveStates= new ArrayList<State>();
              solveWinActions= (AStar.solve(currentState));

              saveFile();
              for(int i = 0; i<solveWinActions.states.size(); i++){
                solveStates.add(solveWinActions.states.get(i));
              }

              initialState = tempState;
              solveFrame();
              solveRender();
              frame.requestFocus();
            }
        });

        JButton button2 = new JButton("R");
        button2.setPreferredSize(new Dimension(64, 64)); //tile size
        panel.add(button2);
        button2.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
              generateMaze();
              render();
            }
        });

        int WIFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

        panel.getInputMap(WIFW).put(KeyStroke.getKeyStroke("DOWN"),
                                    "goDown");
        panel.getActionMap().put("goDown", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
              moveDown();
              render();

              checkWin();
            }
        });

        panel.getInputMap(WIFW).put(KeyStroke.getKeyStroke("LEFT"),
                                    "goLeft");
        panel.getActionMap().put("goLeft", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
              moveLeft();
              render();

              checkWin();
            }
        });

        panel.getInputMap(WIFW).put(KeyStroke.getKeyStroke("RIGHT"),
                                    "goRight");
        panel.getActionMap().put("goRight", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
              moveRight();
              render();

              checkWin();
            }
        });

        panel.getInputMap(WIFW).put(KeyStroke.getKeyStroke("UP"),
                                    "goUp");
        panel.getActionMap().put("goUp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
              moveUp();
              render();

              checkWin();
            }
        });

        frame.setResizable(false);
        frame.setFocusable(true);
        frame.pack();
        frame.setVisible(true);
    }

    public void solveFrame(){
      solve = new JFrame("Solve!");
      solveButtons = new JButton[Game.ROWS][Game.COLS];

      Container pane = solve.getContentPane();

      pane.setLayout(new GridLayout(ROWS+1, COLS));

      for (int i = 0; i < Game.ROWS; i++) {
          for (int j = 0; j < Game.COLS; j++) {
              JButton button = new JButton();

              button.setPreferredSize(new Dimension(64, 64)); //tile size

              pane.add(button);

              solveButtons[i][j] = button;
          }
      }
      JButton left = new JButton("<");
      left.setPreferredSize(new Dimension(64, 64)); //tile size
      pane.add(left);
      left.addActionListener( new ActionListener()
      {
          public void actionPerformed(ActionEvent e)
          {
            if(action>0){
              action--;
              initialState= solveStates.get(action);
              solveRender();
            }
            else{
              solveRender();
            }
          }
      });

      JButton right = new JButton(">");
      right.setPreferredSize(new Dimension(64, 64)); //tile size
      pane.add(right);
      right.addActionListener( new ActionListener()
      {
          public void actionPerformed(ActionEvent e)
          {
            if(action<solveWinActions.states.size()){
              initialState= solveStates.get(action);
              action++;
              solveRender();
            }
            else{
              solveRender();
            }
          }
      });

      solve.setResizable(false);
      // solve.setFocusable(true);
      solve.pack();
      solve.setVisible(true);
    }

    public void solveRender() {
        for (int i = 0; i < Game.ROWS; i++) {
            for (int j = 0; j < Game.COLS; j++) {
                String currentValue = this.initialState.getValue(i, j);

                if (currentValue.equals(State.KEEPER)) {
                    currentValue += this.direction;
                }

                solveButtons[i][j].setLabel("");

                ImageIcon icon = iconMap.get(currentValue);

                solveButtons[i][j].setIcon(icon);
            }
        }
        checkActions();
    }

    public void render() {
        for (int i = 0; i < Game.ROWS; i++) {
            for (int j = 0; j < Game.COLS; j++) {
                String currentValue = this.currentState.getValue(i, j);

                if (currentValue.equals(State.KEEPER)) {
                    currentValue += this.direction;
                }

                buttons[i][j].setLabel("");

                ImageIcon icon = iconMap.get(currentValue);

                buttons[i][j].setIcon(icon);
            }
        }
        checkActions();
    }

    public void saveFile() {
        String filename = "maze.out";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            for(int i=0; i<solveWinActions.actions.size(); i++){
              writer.write(solveWinActions.actions.get(i)+" ");
            }

            writer.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filename + "'");
        }
        catch(IOException ex) {
            System.out.println("Error writing file '" + filename + "'");
        }
    }

    public void checkActions(){
      this.currentState.getPossibleActions();
    }
    public void moveUp() {
        this.currentState.moveUp();
        this.direction = "Back";

    }

    public void moveDown() {
        this.currentState.moveDown();
        this.direction = "Front";

    }

    public void moveLeft() {
        this.currentState.moveLeft();
        this.direction = "Left";

    }

    public void moveRight() {
        this.currentState.moveRight();
        this.direction = "Right";

    }

    public void checkWin() {
        if (currentState.isWin()) {
            JOptionPane.showMessageDialog(frame, "Win!");
            this.hasWon = true;
        }
    }

    private ImageIcon combineIcon(ArrayList<String> filenames) {
        ArrayList<File> files = new ArrayList<File>();
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

        Image img = null;

        try { // load file
            for (String filename : filenames) {
                files.add(new File(filename));
            }

            for (File file : files) { //get image from each file
                BufferedImage tmp = ImageIO.read(file);

                BufferedImage newImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB); //make new buffered image with correct size

                Graphics g = newImage.getGraphics();
                g.drawImage( // put tmp then draw over with newImage
                    tmp,
                    tmp.getWidth() < 64 ? 32 - tmp.getWidth() / 2 : 0,
                    tmp.getHeight() < 64 ? 32 - tmp.getHeight() / 2 : 0,
                    null
                );
                g.dispose();

                images.add(newImage);// images: list of update buffered images
            }

            img = new BufferedImage( // img : combined all pic per tile
                images.get(0).getWidth(),
                images.get(0).getHeight(),
                BufferedImage.TYPE_INT_RGB
            );

            Graphics g2 = img.getGraphics();

            for (int i = 0; i < images.size(); i++) { //draw the map
                BufferedImage image = images.get(i);

                g2.drawImage(image, 0, 0, null);

            }

            g2.dispose();
        }
        catch(Exception e) {
            System.out.println("Something went wrong.");
        }

        return new ImageIcon(img);
    }
}
