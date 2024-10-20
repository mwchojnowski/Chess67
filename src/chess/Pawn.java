package chess;

/**
 * Provides definition of the Pawn and its moves.
 * @author Mateusz Chojnowski
 */


public class Pawn extends ChessPiece {
	
	/**
	 * Pawn Constructor
	 * 
	 * @param type Color of the piece.
	 * @param file The current file of the piece 
	 * @param rank The current rank of the piece.
	 * @param moved Whether or not the piece has moved.
	 */
	
	public Pawn(String type,int file,int rank,Boolean moved) {
		super(type,file,rank,moved);
		name="pawn";
	}
	public boolean test(int targetFile, int targetRank, ChessPiece[][] Board) { //checking whether the target square is legal for this piece->Rook
		//make sure to add this later that a piece cannot move to the same position where it is right now
		ChessPiece tmp = Board[targetRank][targetFile];

		if(tmp!=null) {
			if(tmp.type.equals(type)) {
				return false;
			}

			//white  capture
			if((file+1==targetFile || file-1==targetFile) && rank-1==targetRank && type=="white" && tmp.type=="black") {
				return true;
			}
			//black capture
			else if((file+1==targetFile || file-1==targetFile) && rank+1==targetRank && type=="black" && tmp.type=="white") {
				return true;
			}
			return false;
		}
		if(targetFile==file) {

			if(moved==false) {
				if((targetRank==rank+2 && type.equals("black"))) {
					return true;
				}
				else if(targetRank==rank-2 && type.equals("white")) {
					return true;
				}
			}
			if((targetRank==rank+1 && type.equals("black"))) {
				return true;
			}
			else if(targetRank==rank-1 && type.equals("white")) {
				return true;
			}
		}
		return false;
	}
	 

}
