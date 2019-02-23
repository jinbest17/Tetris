/**
 * This program runs tetris on javafx graphic user interface.
 * Jinhao Liu
 * November 20th, 2018
 * */
import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;
import javafx.scene.media.*;

/**
 * This class includes a javafx GUI and a inner class to listen keyboard
 * input and another thread to keep the piece dropping.
 * */
public class GuiTetris extends Application {

  private static final int PADDING = 10;
  private static final int TILE_GAP = 2;
  private static final int GRID_COL = 10;
  private static final int GRID_ROW = 20;
  private static final int SHOW = 4;
  private static final int R1_START_COL = 0;
  private static final int R1_START_ROW = 0;
  private static final int R1_COL_SPAN = 8;
  private static final int R1_ROW_SPAN = 1;
  private static final int R2_START_COL = 8;
  private static final int R2_START_ROW = 0;
  private static final int R2_COL_SPAN = 2;
  private static final int R2_ROW_SPAN = 1;
  private static final int R5_START_COL = 0;
  private static final int R5_START_ROW = 1;
  private static final int R3_START_COL = 6;
  private static final int R3_START_ROW = 1;
  private static final int R4_START_COL = 0;
  private static final int R4_START_ROW = 5;

  private Tetris tetris;
  private GridPane pane;
  private MyKeyHandler myKeyHandler;
  private Text title, count;
  private Rectangle[][] storedPiece;
  private Rectangle[][] nextPiece;
  private Rectangle[][] grid;

  @Override
  public void start(Stage primaryStage) {
    this.tetris = new Tetris();

    // Comment out if needed
    //startMusic();

    // Initialize gridpane
    pane = new GridPane();
    pane.setAlignment(Pos.CENTER);
    pane.setPadding(new Insets(PADDING,PADDING,PADDING,PADDING));
    pane.setStyle("-fx-background-color: rgb(255,255,255)");
    pane.setHgap(TILE_GAP); 
    pane.setVgap(TILE_GAP);




    // Initialize title text
    title =  new Text("Tetris");
    GridPane.setHalignment(title, HPos.CENTER);
    pane.add(title, R1_START_COL, R1_START_ROW, R1_COL_SPAN, R1_ROW_SPAN);

    // Initialize linescleared counter text
    count = new Text("0");
    GridPane.setHalignment(count, HPos.CENTER);
    pane.add(count, R2_START_COL, R2_START_ROW, R2_COL_SPAN, R2_ROW_SPAN);

    // Initialize empty display
    initR5();
    initR3();
    initR4();
    
    // Initialize next piece display
    updatePiece(tetris.nextPiece, "next");

    // Initialize grid with next piece
    updateGrid(tetris.grid, tetris.activePiece);

    Scene scene = new Scene(pane);
    primaryStage.setTitle("Tetris");
    primaryStage.setScene(scene);
    primaryStage.show();

    myKeyHandler = new MyKeyHandler();
    scene.setOnKeyPressed(myKeyHandler);
    MoveDownWorker worker = new MoveDownWorker();
    worker.start();
  }

  /**
   * Initialize Region 5
   * */
  private void initR5() {
    storedPiece = new Rectangle[SHOW][];
    for(int i = 0; i < SHOW; i++) {
      storedPiece[i] = new Rectangle[SHOW];
    }

    for(int i = 0; i < SHOW; i++){
      for(int j = 0; j < SHOW; j++) {
        storedPiece[i][j] = new Rectangle(25,25,Color.PINK);
        pane.add(storedPiece[i][j], j, i+R5_START_ROW, 1, 1);
      } 
    }
  }

  /**
   * Initialize Region 3
   * */
  private void initR3() {
    nextPiece = new Rectangle[SHOW][];
    for(int i = 0; i < SHOW; i++) {
      nextPiece[i] = new Rectangle[SHOW];
    }

    for(int i = 0; i < SHOW; i++){
      for(int j = 0; j < SHOW; j++) {
        nextPiece[i][j] = new Rectangle(25,25,Color.PINK);
        pane.add(nextPiece[i][j], j+R3_START_COL, i+R3_START_ROW, 1, 1);
      } 
    }
  }

  /**
   * Initialize Region 4
   * */
  private void initR4() {
    grid = new Rectangle[GRID_ROW][];
    for(int i = 0; i < GRID_ROW; i++) {
      grid[i] = new Rectangle[GRID_COL];
    }

    for(int i = 0; i < GRID_ROW; i++){
      for(int j = 0; j < GRID_COL; j++) {
        grid[i][j] = new Rectangle(25,25,Color.GRAY);
        pane.add(grid[i][j], j, i+R4_START_ROW, 1, 1);
      } 
    }
  }

  /** 
   * update game play grid with activepiece
   * @param grid the grid from backend tetris game
   * @param piece the piece being put into GUI grid
   * */
  private void updateGrid(char[][] grid, Piece piece) {
    if(piece != null){
      char[][] temp_grid = new char[grid.length][grid[0].length];
      for(int i = 0; i < grid.length; i++)
        for(int j = 0; j < grid[i].length; j++)
          temp_grid[i][j] = grid[i][j];

      // put active piece into temp grid
      for(int i = 0; i < piece.tiles.length; i++)
        for(int j = 0; j < piece.tiles[i].length; j++)
          if(piece.tiles[i][j] == 1) {
            temp_grid[i+piece.rowOffset]
              [j+piece.colOffset] = piece.shape;
          }



      // going through array to update grid
      for(int i = 0; i < temp_grid.length; i++)
        for(int j = 0; j < temp_grid[i].length; j++){
          this.grid[i][j].setFill(getColor(temp_grid[i][j]));
        }
    }
  }


  /**
   * Get the color according to piece shape
   * @param shape char representing shape
   * @return Color of piece with shape c
   * */
  private Color getColor(char shape) {
    switch(shape) {
      case 'O': return Color.RED;
      case 'I': return Color.GREEN;
      case 'S': return Color.BLUE;
      case 'Z': return Color.YELLOW;
      case 'J': return Color.MAGENTA;
      case 'L': return Color.CYAN;
      case 'T': return Color.ORANGE;
      case ' ': return Color.GRAY;
      default:  return Color.BLACK;
    }
  }


  /**
   * update either region 5 or 3 with new piece.
   * @param piece being updated with
   * @param s update 5 or 3
   * */
  private void updatePiece(Piece piece, String s) {
    if(piece != null) {
      Color pieceColor = getColor(piece.shape);
      int tiles[][] = piece.tiles;
      for(int i = 0; i< nextPiece.length; i++) {
        for(int j = 0; j < nextPiece[i].length; j++) {
          nextPiece[i][j].setFill(Color.PINK);
        }
      }
      if(s.equals("next")) {
        for(int i = 0; i< tiles.length; i++) {
          for(int j = 0; j < tiles[i].length; j++) {
            if(tiles[i][j] == 1)
              nextPiece[i][j].setFill(pieceColor);
            else nextPiece[i][j].setFill(Color.PINK);
          }
        }
      } else if(s.equals("stored")){
        for(int i = 0; i< tiles.length; i++) {
          for(int j = 0; j < tiles[i].length; j++) {
            if(tiles[i][j] == 1)
              storedPiece[i][j].setFill(pieceColor);
            else storedPiece[i][j].setFill(Color.PINK);
          }
        }
      }
    } 
  }

  /**
   * Event handler class listening for keyboard input.
   * */
  private class MyKeyHandler implements EventHandler<KeyEvent> {
    @Override
    public void handle(KeyEvent e) {
      if(!(tetris.isGameover)){
        switch(e.getCode()){
          case UP: tetris.rotate();
                   break;
          case DOWN: tetris.move(Direction.DOWN);
                     break;
          case LEFT: tetris.move(Direction.LEFT);
                     break;
          case RIGHT: tetris.move(Direction.RIGHT);
                      break;
          case SPACE: tetris.drop();
                      break;
          case Z: tetris.hold();
                  break;
          case O: try {
                    tetris.outputToFile();
                  } catch (IOException IOE) {
                    System.err.println("Output to File throws exception");
                  }
                  break;
          default: break;
        }
        updateGUI();
      }
    }

    /**
     * update gui display after user input.
     * */
    private void updateGUI(){
      if(tetris.isGameover){
        title.setText("Game Over!");
      } else{
        updateGrid(tetris.grid, tetris.activePiece);
        updatePiece(tetris.nextPiece, "next");
        updatePiece(tetris.storedPiece, "stored");
        count.setText(Integer.toString(tetris.linesCleared));
      }
    }
  }



  /* ---------------- DO NOT EDIT BELOW THIS LINE ---------------- */

  /**
   * private class GuiTetris.MoveDownWorker
   * a thread that simulates a downwards keypress every some interval
   * @author Junshen (Kevin) Chen
   */
  private class MoveDownWorker extends Thread{

    private static final int DROP_INTERVAL = 500; // millisecond
    private int move_down_timer = DROP_INTERVAL; 

    /**
     * method run
     * called when the thread begins, decrements the timer every iteration
     * of a loop, reset the timer and sends a keydown when timer hits 0
     */
    @Override
    public void run(){

      // run forever until returned
      while (true) {
        // stop the thread if the game is over
        if (tetris.isGameover) return; 

        // wait 1ms per iteration
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          break;
        }

        move_down_timer -= 1;
        if (move_down_timer <= 0 ) {

          // simulate one keydown by calling the 
          // handler.handle()
          myKeyHandler.handle(
              new KeyEvent(null, "", "", KeyCode.DOWN, 
                false, false, false, false)
              );

          move_down_timer = DROP_INTERVAL;
        }
      }
    }
  } // end of private class MoveDownWorker

  /**
   * Cheng Shen Nov. 11th 2018
   * This method plays the background music
   */
  private void startMusic(){
    try{
      System.out.println("Playing Music~~~");
      File mp3 = new File("./Tetris.mp3");
      Media bgm = new Media(mp3.toURI().toString());
      MediaPlayer bgmPlayer = new MediaPlayer(bgm);
      bgmPlayer.setCycleCount(bgmPlayer.INDEFINITE);
      bgmPlayer.play();
    }catch (Exception e){
      System.out.println("Exception playing music");
    }
  }

  public static void main(String[] args) {
	  Application.launch(args);
  }
}
