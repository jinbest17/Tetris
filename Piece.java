/**
 * Piece.java
 * Jinhao Liu
 * October 30th, 2018
 * contains class piece that is used later in the game in Tetris.java
 */

import java.util.*;

/**
 * This class contains definition of Piece object, its shape, 
 * offsets, and tiles; it also allows Piece to move and rotate.
 * */
public class Piece {

  // all possible char representation of a piece
  public static final char[] possibleShapes = 
  {'O', 'I', 'S', 'Z', 'J', 'L', 'T'}; 

  // initial state of all possible shapes, notice that this array's 
  // content shares index with the possibleShapes array 
  public static final int[][][] initialTiles = {
    {{1,1},
      {1,1}}, // O

    {{0,0,0,0},
      {1,1,1,1},
      {0,0,0,0},
      {0,0,0,0}}, // I

    {{0,0,0},
      {0,1,1},
      {1,1,0}}, // S

    {{0,0,0},
      {1,1,0},
      {0,1,1}}, // Z

    {{0,0,0},
      {1,1,1},
      {0,0,1}}, // J

    {{0,0,0},
      {1,1,1},
      {1,0,0}}, // L

    {{0,0,0},
      {1,1,1},
      {0,1,0}} // T
  };  

  // random object used to generate a random shape
  public static Random random = new Random(); 

  // char representation of shape of the piece, I, J, L, O, S, Z, T
  public char shape;

  // the position of the upper-left corner of the tiles array 
  // relative to the Tetris grid
  public int rowOffset;
  public int colOffset;

  // used to determine 2-state-rotations for shapes S, Z, I
  // set to true to indicate the next call to rotate() should
  // rotate clockwise
  public boolean rotateClockwiseNext = false;

  // an array marking where the visible tiles are
  // a 1 indicates there is a visible tile in that position
  // a 0 indicates there is no visible tile in that position
  public int[][] tiles;


/** Construct a random piece object.
 * @param none
 * @return none
 * */
  public Piece(){
    int n = random.nextInt(7);
    shape = possibleShapes[n];
    tiles = initialTiles[n].clone();
    if(shape != 'O'){
      rowOffset = -1;
      colOffset = 3;
    } else {
      rowOffset = 0;
      colOffset = 4;
    }
  }

  /** Construct a piece object given shape.
   * @param shape the piece's shape
   * @return none
   * */
  public Piece(char shape) {
    for (int i = 0; i < 7; i++){
      if (possibleShapes[i] == shape) {
        this.shape = shape;
        this.tiles = initialTiles[i].clone();
        if(shape != 'O'){
          rowOffset = -1;
          colOffset = 3;
        } else {
          rowOffset = 0;
          colOffset = 4;
        }
      }
    }
  }


  /** Construct piece object copying from another piece's properties.
   * @param oldpiece the source piece
   * @return none
   * */
  public Piece(Piece other){
    this.shape = other.shape;
    this.tiles = other.tiles.clone();
    this.rowOffset = other.rowOffset;
    this.colOffset = other.colOffset;
    this.rotateClockwiseNext = other.rotateClockwiseNext;
  }


  /** rotate the piece once(direction dependent on shape)
   * @param none
   * @return void
   * */
  public void rotate(){
    if (shape == 'O' || shape == 'T' || shape == 'L' || shape == 'J'){
      rotateClockwise();
    }
    else if (shape == 'I' ||shape == 'S' ||shape == 'Z') {
      if (rotateClockwiseNext) {
        rotateClockwise();
      } else {
        rotateCounterClockwise();
      }
      rotateClockwiseNext = !rotateClockwiseNext;
    }
  }

  /** rotate the piece clockwise once.
   * @param none
   * @return void
   * */
  public void rotateClockwise() {
    int n = tiles.length;
    int result[][] = new int[n][n];
    for (int i = 0; i < n; i++){
      for (int j = 0; j < n; j++) {
        result[i][j] = tiles[n-1-j][i];
      }
    }
    tiles = result;
  }

   /** rotate the piece counterclockwise once.
   * @param none
   * @return void
   * */
  public void rotateCounterClockwise () {
    int n = tiles.length;
    int result[][] = new int[n][n]; 
    for (int i = 0; i < n; i++){
      for (int j = 0; j < n; j++) {
        result[i][j] = tiles[j][n-1-i];
      }
    }
    tiles = result;
  }


  /** move the piece in given direction by one space.
   * @param direction the moving direction
   * @return void
   * */
  public void movePiece(Direction direction) {
    switch (direction)
    {
      case DOWN: 
        rowOffset++;
        break;

      case LEFT: 
        colOffset--;
        break;

      case RIGHT: 
        colOffset++;
        break;
    }
  }



  /** main method to test the functionality of above method. 
   * @param String[] args
   * @return void
   * */
  public static void main(String[] args) {
    Piece a = new Piece();
    Piece b = new Piece('Z');
    Piece c = new Piece('T');
    Piece d = new Piece(b);
    System.out.println("Is d deep copy: " + !(b == d));
    System.out.println(a.tiles.toString());
    a.rotate();
    System.out.println(a.tiles.toString());
  }
}
