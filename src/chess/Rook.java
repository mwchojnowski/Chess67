package chess;

/**
 * Provides definition of the Rook and its moves.
 * @author Mateusz Chojnowski
 */

public class Rook extends ChessPiece {
	
	/**
	 * Rook Constructor
	 * 
	 * @param type Color of the piece.
	 * @param file The current file of the piece 
	 * @param rank The current rank of the piece.
	 * @param moved Whether or not the piece has moved.
	 */
	public Rook(String type,int file,int rank,Boolean moved) {
		super(type,file,rank,moved);
		name="Rook";
	}
	
	public boolean test(int targetFile, int targetRank, ChessPiece[][] Board) { //checking whether the target square is legal for this piece->Rook
		int diff=1;
		
		ChessPiece temp = Board[targetRank][targetFile];
		if(temp!=null) {
			if(temp.type.equals(type)) {
				return false;
			}
		}
		if(file==targetFile) {
			if(rank>targetRank) diff=-1;
			for(int i=1;i<=Math.abs(rank-targetRank);i++) {
				ChessPiece tmp = Board[rank+diff*i][file];
				if(tmp==null) continue;
				else {
					if(rank+diff*i==targetRank && !(tmp.type.equals(type))) return true;
					return false;
				}
			}
		}
		else if (rank==targetRank){
			if(file>targetFile) diff=-1;
			for(int i=1;i<=Math.abs(file-targetFile);i++) {
				ChessPiece tmp = Board[rank][file+diff*i];
				if(tmp==null) continue;
				else {
					if(file+diff*i==targetFile && !(tmp.type.equals(type))) return true;
					return false;
				}
			}
		}
		else if(rank!=targetRank && file!=targetFile)return false;
		return true;
	}
}
