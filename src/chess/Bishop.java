package chess;

/**
 * Provides definition of the Bishop and its moves.
 * @author Mateusz Chojnowski
 */

public class Bishop extends ChessPiece {

	/**
	 * Bishop Constructor
	 * 
	 * @param type Color of the piece.
	 * @param file The current file of the piece 
	 * @param rank The current rank of the piece.
	 * @param moved Whether or not the piece has moved.
	 */
	public Bishop(String type, int file, int rank,Boolean moved) {
		super(type, file, rank,moved);
		name="Bishop";
	}
	public boolean test(int targetFile, int targetRank, ChessPiece[][] Board) {
		ChessPiece temp = Board[targetRank][targetFile];
		if(temp!=null) {
			if(temp.type.equals(type)) {
				return false;
			}
		}
		
		if(!(Math.abs(targetFile-file) == Math.abs(targetRank-rank))) return false;
		int diffRank = 1;
		int diffFile = 1;
		if(targetRank<rank) diffRank=-1;
		if(targetFile<file) diffFile=-1;
		
		for(int i=1;i<=Math.abs(targetRank-rank);i++) {
			ChessPiece tmp = Board[rank+(diffRank*i)][file+(diffFile*i)];
			if(tmp==null) continue;
			else {
				if((targetRank==(rank+(diffRank*i))) && !(tmp.type.equals(type))) return true;
				return false;
			}
		}
		return true;
	}
}
