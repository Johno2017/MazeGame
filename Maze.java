package mazegame;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Maze extends JFrame{
    public final static int rows = 20;
    public final static int columns = 20;
    public static int panelSize = 25;
    public static int map[][] = new int[columns][rows];
    public static int endMazeX;
    public static int endMazeY;
    Player p;
    
    static String loadURL = "loading.gif";

    /**
     * Creates the maze map and its entirety with functioning playability
     * @param str - name of maze map file
     */
    public Maze(String str){
        loadMap(str);
        this.setResizable(false);
        this.setSize((columns*panelSize)+50, (rows*panelSize)+70);
        this.setTitle("Maze.exe");
        this.setLayout(null);

        this.addKeyListener(new KeyListener(){

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                revalidate();
                repaint();

                // Player movement (WASD and arrow keys)
                if(key == KeyEvent.VK_W || key == KeyEvent.VK_UP){
                    p.moveUp();
                }
                if(key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT){
                    p.moveLeft();
                }
                if(key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN){
                    p.moveDown();
                }
                if(key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT){
                    p.moveRight();
                }

                // Checking for that one green exit tile
                if(p.x == endMazeX && p.y == endMazeY){
                    JOptionPane.showMessageDialog(null, "Congratulations! You escaped the maze!", "End Game", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new MainMenu();
                }
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyTyped(KeyEvent arg0) {
                // TODO Auto-generated method stub
            }

        });

        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        this.setLocationRelativeTo(null);

        // Create player
        p = new Player();
        p.setVisible(true);
        this.add(p);

        // Color map
        for(int y = 0; y < columns; y++){
            for(int x = 0; x < rows; x++){
                Tile tile = new Tile(x, y);
                tile.setSize(panelSize, panelSize);
                tile.setLocation((x*panelSize)+23, (y*panelSize)+25);
                switch (map[x][y]) {
                    case 0: // wall tile
                        tile.setBackground(Color.GRAY);
                        break;
                    case 2: // maze end tile
                        tile.setBackground(Color.GREEN);
                        tile.setWall(false);
                        // save the x and y coordinates for the game end tile
                        endMazeX = x;
                        endMazeY = y;
                        break;
                    default: // path tile
                        tile.setBackground(Color.WHITE);
                        tile.setWall(false);
                        if(x == 0){ // sets the location where the player will appear when the maze first opens (left side)
                            p.setLocation((x*panelSize)+23, (y*panelSize)+25);
                            p.y = y;
                        } break;
                }
                tile.setVisible(true);
                this.add(tile);
            }
        }
        this.setVisible(true);
    }

    public static void main(String args[]){
        Screen loadScreen = new Screen();
        loadScreen.createScreen(loadURL);
        int secondsToSleep = 3;
        try {
            Thread.sleep(secondsToSleep * 1000);
            loadScreen.visibility();
            new MainMenu();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Loads the maze map file
     * @param str - name of the maze map file
     */
    public static void loadMap(String str){
        try{
            BufferedReader br = new BufferedReader(new FileReader(str));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String mapStr = sb.toString();

            int counter = 0;
            for(int y = 0; y < columns; y++){
                for(int x = 0; x < rows; x++){
                    String mapChar = mapStr.substring(counter, counter+1);
                    if(!mapChar.equals("\r\n") && !mapChar.equals("\n")&& !mapChar.equals("\r")){//If it's a number
                        map[x][y] = Integer.parseInt(mapChar);
                    }else{//If it is a line break
                        x--;
                        System.out.print(mapChar);
                    }
                    counter++;
                }
            }
        }catch(IOException | NumberFormatException e){
            System.out.println("Unable to load existing maze(if exists), creating new maze.");
        }
    }
}
