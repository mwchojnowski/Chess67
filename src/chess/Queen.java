package chess;

/**
 * Provides definition of the queen and its moves
 * @author Mateusz Chojnowski
 */
public class Queen extends ChessPiece {
	
	/**
	 * Queen Constructor
	 * 
	 * 
	 * @param type Color of the piece.
	 * @param file The current file of the piece 
	 * @param rank The current rank of the piece.
	 * @param moved Whether or not the piece has moved.
	 */
	public Queen(String type, int file, int rank,Boolean moved) {
		super(type, file, rank,moved);
		name="Queen";
	}
	
	public boolean test(int targetFile, int targetRank, ChessPiece[][] Board) { //checking whether the target square is legal for this piece->Rook
		ChessPiece tmp = Board[targetRank][targetFile];
		if(tmp!=null) {
			if(tmp.type.equals(type)) {
				return false;
			}
		}
		
		ChessPiece r = new Rook(type,file,rank,moved);
		ChessPiece b = new Bishop(type,file,rank,moved);
		Boolean rt = r.test(targetFile, targetRank, Board);
		Boolean bt = b.test(targetFile, targetRank, Board);
		if(rt==true || bt==true) return true;
		else return false;
	}


}
