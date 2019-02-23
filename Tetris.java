/**
 * Piece.java
 * Jinhao Liu
 * October 30th, 2018
 * This is the program that displays a grid in console to allow
 * user play the tetris game. 
 */

import java.util.*;
import java.io.*;

/** This class gives the game Tetris its main functionalities
 * for game setup, begining, palying and ending.
 * */
public class Tetris {

  public int linesCleared; // how many lines cleared so far

  public boolean isGameover;  // true if the game is over
  // false if the game is not over

  public Piece activePiece;   // holds a Piece object that can be moved
  // or rotated by the player

  public Piece nextPiece; // holds a Piece object that will become the new 
  // activePiece when the activePiece consolidates

  // The following 2 variables are used for the extra credit 
  public boolean usedHold = false;    // set to true if the player already 
  // used hold once since last piece 
  // consolidated

  public Piece storedPiece = null;    // holds the stored piece 
  public char[][] grid;   // contains all consolidated pieces, each tile  
  // represented by a char of the piece's shape
  // a position stores a space char if it is empty


  /** Construct Tetris game with random pieces.
   * @param none
   * @return none
   * */
  public Tetris(){
    linesCleared = 0;
    isGameover = false;

    //set up an empty grid
    grid = new char[20][10];
    for(int i = 0; i < grid.length; i++){
      for(int j = 0; j < grid[i].length; j++){
        grid[i][j] = ' ';
      }
    }

    activePiece = new Piece();
    nextPiece = new Piece();
  }


  /** Construct Tetris game from a saved file.
   * @param filename the name of the game data file
   * @return none
   * */
  public Tetris (String filename) throws IOException {
    //initialize scanner
    BufferedReader input = new BufferedReader(new FileReader(filename));
    linesCleared = Integer.parseInt(input.readLine());
    activePiece = new Piece(input.readLine().charAt(0));
    nextPiece = new Piece(input.readLine().charAt(0));

    //fill up grid from input
    String line = "";
    grid = new char[20][10];

    for (int i = 0; i < grid.length; i++){
      line = input.readLine();
      for (int j = 0; j < grid[i].length; j++){
        grid[i][j] = line.charAt(j);
      }
    }
    input.close();

    //check isGameover
    isGameover = hasConflict(activePiece);
  }


  /** Check whether the piece causes a conflict(out of grid or overlap).
   * @param piece the piece under check
   * @return whether piece causes conflict
   * */
  public boolean hasConflict(Piece piece) {
    //check left boundary
    if (piece.colOffset + findLeftmost(piece.tiles) < 0)
      return true;
    //check right
    if (piece.colOffset + findRightmost(piece.tiles) > grid[0].length - 1)
      return true;
    //check down
    if (piece.rowOffset + findLowest(piece.tiles) > grid.length -1)
      return true;
    if (piece.rowOffset + findTop(piece.tiles) < 0)
      return true;
    //check overlap
    int len = piece.tiles.length;
    for (int i = 0; i < len; i++) {
      for (int j = 0; j < len; j++) {
        if (piece.tiles[i][j] == 1){
          if (grid[i+piece.rowOffset][j+piece.colOffset] != ' ')
            return true;
        }
      }
    }
    return false;
  }

  /** helper method to find leftmost 1 in array
   * @param array checked array
   * @return the index of leftmost value 1
   * */
  private int findLeftmost(int[][] arr) {
    for(int i = 0; i<arr.length;i++){
      for(int j = 0; j < arr.length; j++){
        if(arr[j][i] == 1){
          return i;
        }
      }
    }
    return 0;
  }

   /** helper method to find rightmost 1 in array
   * @param array checked array
   * @return the index of rightmost value 1
   * */
  private int findRightmost(int[][] arr){
    for(int i = arr.length - 1; i >= 0; i--){
      for(int j = 0; j<arr.length; j++){
        if (arr[j][i] == 1){
          return i;
        }
      }
    }
    return 0;
  }

   /** helper method to find lowest 1 in array
   * @param array checked array
   * @return the index of lowest value 1
   * */
  private int findLowest(int[][] arr){
    for(int i = arr.length -1; i >= 0; i--){
      for (int j = 0; j < arr.length; j++){
        if(arr[i][j] == 1){
          return i;
        }
      }
    }
    return 0;
  }

   /** helper method to find top 1 in array
   * @param array checked array
   * @return the index of top value 1
   * */
  private int findTop(int[][] arr){ 
    for(int i = 0; i < arr.length; i++) {
      for(int j = 0; j < arr.length; j++) {
        if(arr[i][j] == 1) {
          return i;
        }
      }
    }
    return 0;
  }


  /** consolidate the piece at the bottom of the grid.
   * @param none
   * @return void
   * */
  public void consolidate() {
    for (int i = 0; i < activePiece.tiles.length; i++) {
      for (int j = 0; j < activePiece.tiles.length; j++) {
        if(activePiece.tiles[i][j] == 1) {
          grid[i + activePiece.rowOffset][j + activePiece.colOffset] = activePiece.shape;
        }
      }
    }
  }


  /** Check if any line is full then clear it, dropping above lines down
   * fill top line with spaces.
   * @param none
   * @return void
   * */
  public void clearLines() {
    boolean foundFullLine = false;
    int count = 0;
    for (int i = grid.length - 1; i > 0; i--) {
      if(!foundFullLine) {
        foundFullLine = isLineFull(i);
      } else {
        count++;
        foundFullLine = false;
        for(int n = i; n >= 0; n--){
          for(int j = 0; j < grid[n].length; j++) {
            grid[n+1][j] = grid[n][j];
          }
        }
        for(int k = 0; k < grid[i].length; k++) {
          grid[0][k] = ' ';
        }
        i--;
      }
    }
    linesCleared += count;
  }

  /** helper method to check if any line is full.
   * @param lineIndex the line checked
   * @return whether the line is full or not
   * */
  private boolean isLineFull(int i) {
    for (int j = 0; j < grid[i].length; j++) {
      if(grid[i][j] == ' ')
        return false;
    }
    return true;
  }

  /** move the piece if no conflict is caused.
   * @param direction the direction moving to
   * @return is the move successful
   * */
  public boolean move(Direction direction) {
    Piece testPiece = new Piece(activePiece);
    if(direction == Direction.DOWN) {
      testPiece.rowOffset += 1;
    } else if(direction == Direction.LEFT) {
      testPiece.colOffset += -1;
    } else if(direction == Direction.RIGHT) {
      testPiece.colOffset += 1;
    }
    if (!hasConflict(testPiece)) {
      activePiece = testPiece;
      return true;
    } else if(direction == Direction.DOWN) {
      consolidate();
      usedHold = false;
      clearLines();
      activePiece = nextPiece;
      nextPiece = new Piece();
      if(hasConflict(activePiece)) {
        isGameover = true;
      }
    }
    return false;
  }

  /** Drop the active piece all the way to the bottom.
   * @param none
   * @return void
   * */
  public void drop() {
    boolean b = true;
    do {
      b = move(Direction.DOWN);
    } while(b);
  }

  /** Rotate the active piece if no conflicted caused
   * @param none
   * @return void
   * */
  public void rotate() {
    Piece test = new Piece(activePiece);
    test.rotate();
    if(!hasConflict(test)) {
      activePiece = test;
    }
  }

  /** Out put the current game state to a file
   * @param none
   * @return void
   * */
  public void outputToFile() throws IOException {
    //intialize printer
    PrintWriter output = new PrintWriter(new FileWriter("output.txt"));
    output.println(linesCleared);
    output.println(activePiece.shape);
    output.println(nextPiece.shape);

    //print grid
    String line = "";
    for (int i = 0; i < grid.length; i++){
      for (int j = 0; j < grid[i].length; j++){
        output.print(grid[i][j]);
      }
      output.println();
    }
    output.close();
    System.out.println("Saved to output.txt");
  }

  /** Keep taking inputs to play the game until active piece
   * immediately causes conflict.
   * @param none
   * @return void
   * */
  public void play () {
    while(!isGameover){
      System.out.println(this.toString());
      System.out.print("> ");
      Scanner sc = new Scanner(System.in);
      String input = sc.nextLine();
      if (input.equals("q")){
        isGameover = true;
        break;
      } else if(input.equals("a")){
        move(Direction.LEFT);
      } else if(input.equals("d")){
        move(Direction.RIGHT);
      } else if(input.equals("s")){
        move(Direction.DOWN);
      } else if(input.equals("w")){
        rotate();
      } else if(input.equals(" ")){
        drop();
      } else if(input.equals("z")){
        hold();
      } else if(input.equals("o")){
        try {
          outputToFile();
        } catch (IOException e) {
          //This prints out an error message!
          System.err.println("SOS please someone help me");
        }
      }
    }
    System.out.println(this.toString());
    System.out.println("Game over! Total lines cleared: " + linesCleared);
  }

  /**
   * returns the string representation of the Tetris game state in the 
   * following format:
   *  Lines cleared: [number]
   *  Next piece: [char]  (Stored piece: [char])
   *  char[20][10]
   * @return string representation of the Tetris game
   */
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();

    str.append("\nLines cleared: " + this.linesCleared + '\n');

    str.append("Next piece: " + this.nextPiece.shape);
    if (this.storedPiece == null) str.append("\n");
    else str.append("  Stored piece: " + this.storedPiece.shape + '\n');

    str.append("| - - - - - - - - - - |\n");

    /*deep copy the grid*/
    char[][] temp_grid = new char[this.grid.length][this.grid[0].length];
    for (int row=0; row<this.grid.length; row++)
      for (int col=0; col<this.grid[0].length; col++)
        temp_grid[row][col] = this.grid[row][col];

    /*put the active piece in the temp grid*/
    for (int row=0; row<this.activePiece.tiles.length; row++)
      for (int col=0; col<this.activePiece.tiles[0].length; col++)
        if (activePiece.tiles[row][col] == 1)
          temp_grid[row+activePiece.rowOffset]
            [col+activePiece.colOffset] = 
            activePiece.shape;

    /*print the temp grid*/
    for (int row=0; row<temp_grid.length; row++){
      str.append('|');
      for (int col=0; col<temp_grid[0].length; col++){
        str.append(' ');
        str.append(temp_grid[row][col]);
      }
      str.append(" |\n");
    }

    str.append("| - - - - - - - - - - |\n");
    return str.toString();        
  }


  /** Go on to next piece and store active piece in stored piece
   * when stored piece is empty; when stored piece is full, swap
   * stored piece and active piece. this method can only be called 
   * once until the piece consolidates.
   * @param none
   * @return void
   * */
  public void hold() {
    activePiece = new Piece(activePiece.shape);
    if(storedPiece == null) {
      //store active piece in stored piece
      storedPiece = activePiece;
      activePiece = nextPiece;
      nextPiece = new Piece();
    } else if(!usedHold){
      //swap active piece and stored piece
      Piece temp1 = new Piece(activePiece);
      Piece temp2 = new Piece(storedPiece);
      activePiece = temp2;
      storedPiece = temp1;
      usedHold = true;
    }
  }

  /**
   * first method called during program execution
   * @param args: an array of String when running the program from the 
   * command line, either empty, or contains a valid filename to load
   * the Tetris game from
   */
  public static void main(String[] args) {
    if (args.length != 0 && args.length != 1) {
      System.err.println("Usage: java Tetris / java Tetris <filename>");
      return;
    }
    try {
      Tetris tetris;
      if (args.length == 0) tetris = new Tetris();
      else tetris = new Tetris(args[0]);
      tetris.play();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
