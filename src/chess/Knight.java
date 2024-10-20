package chess;

/**
 * Provides definition of the Knight and its moves.
 * @author Mateusz Chojnowski
 */

public class Knight extends ChessPiece {
	
	/**
	 * Knight Constructor
	 * 
	 * @param type Color of the piece.
	 * @param file The current file of the piece 
	 * @param rank The current rank of the piece.
	 * @param moved Whether or not the piece has moved.
	 */

	public Knight(String type, int file, int rank,Boolean moved) {
		super(type, file, rank,moved);
		name="Night";
	}
	
	public boolean test(int targetFile, int targetRank, ChessPiece[][] Board) {
		ChessPiece tmp = Board[targetRank][targetFile];
		if(tmp!=null) {
			if(tmp.type.equals(type)) {
				return false;
			}
		}
		if(targetFile>7 || targetFile<0||targetRank>7 || targetRank<0) {
			return false;
		}
		if((targetFile==(file-1) || targetFile==(file+1)) && ((targetRank==rank+2) || (targetRank==rank-2))) {
			return true;
		}
		if((targetFile==(file-2) || targetFile==(file+2)) && ((targetRank==rank+1) || (targetRank==rank-1))) {
			return true;
		}
		
		return false;
	}

}
