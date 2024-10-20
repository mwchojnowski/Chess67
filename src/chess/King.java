package chess;

/**
 * Provides definition of the King and its moves.
 * @author Mateusz Chojnowski
 */


public class King extends ChessPiece {
	
	/**
	 * King Constructor
	 * 
	 * @param type Color of the piece.
	 * @param file The current file of the piece 
	 * @param rank The current rank of the piece.
	 * @param moved Whether or not the piece has moved.
	 */

	public King(String type, int file, int rank,Boolean moved) {
		super(type, file, rank,moved);
		name="King";
	}
	
	public boolean test(int targetFile, int targetRank, ChessPiece[][] Board) { //checking whether the target square is legal for this piece->Rook
		//make sure to add this later that a piece cannot move to the same position where it is right now
		ChessPiece tmp = Board[targetRank][targetFile];
		if(tmp!=null) {
			if(tmp.type.equals(type)) {
				return false;
			}
		}
		if(targetFile-file==2) { //player trying to castle right side
			if(type.equals("white")) { //White's player is trying to castle
				if(moved==true || Board[7][5]!=null ||  Board[7][6]!=null || !(Board[7][7].name.equals("Rook")) || Board[7][7].moved==true) { 
					//Not allowed if either king or rook has moved, or if there are pieces b/w rook and king
					//Or if there is no rook
					return false;
				}
			}
			else { //Black's player is tryng to castle
				if(moved==true || Board[0][5]!=null ||  Board[0][6]!=null || !(Board[0][7].name.equals("Rook")) || Board[0][7].moved==true) { 
					//Not allowed if either king or rook has moved, or if there are pieces b/w rook and king
					//or if there is no rook
					return false;
				}
			}
			if(targetRank!=rank) return false;
			return true;
		}
		if(file-targetFile==2) { //player trying to castle left side
			if(type.equals("white")) { //White's player is trying to castle
				if(moved==true || Board[7][1]!=null ||  Board[7][2]!=null || Board[7][3]!=null || !(Board[7][0].name.equals("Rook")) || Board[7][0].moved==true) { 
					//Not allowed if either king or rook has moved, or if there are pieces b/w rook and king
					//Or if there is no rook
					return false;
				}
			}
			else { //Black's player is trying to castle
				if(moved==true || Board[0][1]!=null ||  Board[0][2]!=null || Board[0][3]!=null || !(Board[0][0].name.equals("Rook")) || Board[0][0].moved==true) { 
					//Not allowed if either king or rook has moved, or if there are pieces b/w rook and king
					//or if there is no rook
					return false;
				}
			}
			if(targetRank!=rank) return false;
			return true;
			
		}
		if(Math.abs(targetFile-file)==1 && Math.abs(targetRank-rank)<=1) {
			return true;
		}
		if(targetFile==file && Math.abs(targetRank-rank)==1) {
			return true;
		}
		return false;
	}

}
