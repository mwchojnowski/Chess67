package chess;

/**
 * Provides definition of the General ChessPiece and its methods
 * @author Mateusz Chojnowski
 */

public class ChessPiece {
	String name; //name is useful for printing the chess board
	String type; //Black(B) or White(W) Piece
	int file; //the current file of the chess piece e.g 'e'(5)
	int rank; //the current integer position of chess piece e.g 2
	Boolean moved;//Useful to check if King can castle or whether pawn can move up 2 squares

	/**
	 * ChessPiece constructor
	 * 
	 * 
	 * @param type Color of the piece.
	 * @param file The current file of the piece 
	 * @param rank The current rank of the piece.
	 * @param moved Whether or not the piece has moved.
	 */
	
	public ChessPiece(String type,int file, int rank, Boolean moved) {
		this.file=file;
		this.rank=rank;
		this.type=type;
		this.moved=moved;
	}
	
	public void move(int file, int rank) { //changing the square of this Piece after it has moved
		this.file=file;
		this.rank=rank;
	}
	
	public boolean test(int targetFile, int targetRank, ChessPiece[][] Board) {
		return false;//This is to avoid typecasting which is not possible(atleast to my knowledge) without knowing the cast!!
		//dyanmic binding will be applied here in order to use the subclass' test method instead of this one
	}
	
	
	

}
