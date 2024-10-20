package chess;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main class of the project handling the process of the game. 
 * @author Mateusz Chojnowski
 */

public class Chess {

	/**
	 * Prints the current state of chess board in ASCII format
	 * @param chess
	 */
    public static void print(ChessPiece[][] chess) { //Printing the chess board 
        char alt = 'a'; //to print the file positions
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece temp = chess[i][j];


                if (temp != null) { //if the square has a piece on it, then print its details
                    System.out.print(temp.type.substring(0, 1) + temp.name.substring(0, 1) + " ");
                } else { //if the square is empty, then print according to black or white square
                    System.out.print((((i + j) % 2 == 0) ? "  " : "##") + " ");

                }
                if (j == 7) { //This is to print the number positions
                    System.out.print(8 - i + " ");
                }

            }
            System.out.println();
        }
        for (int i = 0; i < 8; i++) { //This is to print the file positions
            System.out.print(" " + alt++ + " ");
        }

        System.out.println();
    }

    /**
     * Creates a chess board for two players
     * Accepts input from players at their turn through command line
     * Checks the validity of the move through various conditions and shows an error message 
     * if the move is illegal
     * game ends when any player resigns by typing 'resign' or offers a draw by typing 'draw?'
     * or through a checkmate
     * All the conditions of a chess game are implemented except
     * stalemate, fifty move rule, threefold repetition and dead position
     * @param args
     */
    public static void main(String[] args) {
    	
        ChessPiece Board[][] = make();
        print(Board);
        Boolean whiteMove = true;
        Boolean draw = false;
        Scanner scan = new Scanner(System.in);

        //Set up both arraylists for check function
        ArrayList<ChessPiece> WhitePieces = new ArrayList<ChessPiece>();
        ArrayList<ChessPiece> BlackPieces = new ArrayList<ChessPiece>();
        for (int i = 0; i < 8; i++) {
            WhitePieces.add(Board[7][i]);
            WhitePieces.add(Board[6][i]);
            BlackPieces.add(Board[0][i]);
            BlackPieces.add(Board[1][i]);
        }
        
        //Store King positions
        int wKingRank = 7;
        int wKingFile = 4;
        int bKingRank = 0;
        int bKingFile = 4;
        
        //For enpassant tracking
        boolean enpassant[] = new boolean[8];
        //initializing all array indexes to be false because the game hasn't started yet and no pawns moved
        makeFalse(enpassant);
        boolean enp=false; //also for enpassant tracking to remove the captured piece

        //for checking if a pawn is being promoted
        boolean promote = false;
        ChessPiece promoted=null;
        
        while (true) {
        	promote=false;
            System.out.println();
            if (whiteMove)
                System.out.print("White's move: ");
            else
                System.out.print("Black's move: ");
            
            //Scan for user move and split at most 3 for possible draw offer or promotion
            String input = scan.nextLine();
            String split[] = input.split(" ", 3);
            

            //Check for draw accepted or resignation
            if (split.length == 1) {
                if (split[0].equals("resign")) {
                    if (whiteMove == true) System.out.println("Black wins");
                    else System.out.println("White wins");
                    break;
                }
                else if(split[0].equals("draw") && draw) {
                	break;
                }
                else {
                	System.out.println("Illegal move, try again");
                	continue;
                }
            }

            if (split.length >= 2) { //if move made

                if (split[0].equals(split[1])) { //Check if the same square
                    System.out.println("Illegal move, try again");
                    continue;
                }
                //Convert moves to all integers for easier use
                int fileStart = (Character.getNumericValue(split[0].charAt(0)) - 10);
                int rankStart = 7 - (Integer.parseInt(String.valueOf(split[0].charAt(1))) - 1);
                int fileDest = (Character.getNumericValue(split[1].charAt(0)) - 10);
                int rankDest = 7 - (Integer.parseInt(String.valueOf(split[1].charAt(1))) - 1);
                
                //Make sure player is moving an actual piece
                ChessPiece tmp = Board[rankStart][fileStart];
                if (tmp == null) {
                    System.out.println("Illegal move, try again");
                    continue;
                }
                
                //If player is moving the wrong color piece
                String color = tmp.type;
                if (whiteMove && (color.equals("black"))) {
                    System.out.println("Illegal move, try again");
                    continue;
                }
                if (!whiteMove && (color.equals("white"))) { 
                    System.out.println("Illegal move, try again");
                    continue;
                }
                
                //Check if piece selected can legally go to its destination
                String name = tmp.name;
                boolean castleShort = false;
                Boolean castleLong = false;
                //Checking if king is trying to castle
                Boolean b = (fileStart-fileDest==2 || fileDest-fileStart==2);
                if(tmp.name.equals("King") && b) {
	                if(whiteMove) {  //white king trying to castle
	                	if(Castlecheck(Board,fileDest,fileStart,rankStart,BlackPieces,"white")) {
	                		System.out.println("Illegal move, try again");
	                		continue;
	                	}
	                }
	                else {  //black king trying to castle
	                	if(Castlecheck(Board,fileDest,fileStart,rankStart,WhitePieces,"black")) {
	                		System.out.println("Illegal move, try again");
	                		continue;
	                	}
	                }
	                if(fileDest-fileStart==2) {
	                	castleShort = true;
	                }
	                else {
	                	castleLong = true;
	                }
                }
                Boolean valid = tmp.test(fileDest, rankDest, Board);
                if (!valid) {
                	//checking enpassant out of pawn class because we cant pass the boolean array
                	if(!(enpassantCheck(fileDest,rankDest,rankStart,fileStart,Board,enpassant))) {
                		System.out.println("Illegal move, try again");
                		continue;
                	}
                	else {
                		//this means that enpassant capture has happened
                		enp=true; //this will help in removing the captured piece
                		//in normal situations, [rankDest][fileDest] is captured but in this case [rank][fileDest] is captured
                	}
                    
                }
                //  --- Make all of the pawn boolean equal to false once previous move's check is done
                //  --- This is because enpassant is valid for one move only
                makeFalse(enpassant);
                
                //  ---Then add this move's boolean
                if(tmp.name=="pawn" && Math.abs(rankDest-rankStart)==2) {
                	//pawn move double squares so it should be added in boolean array
                	enpassant[fileStart]=true; //fileStart or fileDest doesn't matter as they will be same
                }
                
                //Checking for pawn promotion
                if(tmp.name=="pawn" && (rankDest==7 || rankDest==0)) { //checking for both black or white promotion simultaneously
                	promote=true;
                	if(split.length<=2 || split[2].equals("Q")) {
                		//Promoted piece is Queen
                		promoted = new Queen(tmp.type,fileDest,rankDest,false);

                	}
                	else {
                		if(split[2].equals("N")) {
                			//promoted piece is Knight
                    		promoted = new Knight(tmp.type,fileDest,rankDest,false);

                		}
                		else if(split[2].equals("B")) {
                			//promoted piece is Bishop
                    		promoted = new Bishop(tmp.type,fileDest,rankDest,false);

                		}
                		else { 
                			//promoted piece is Rook
                    		promoted = new Rook(tmp.type,fileDest,rankDest,false);

                		}
                	}
                	
                }
                //START
                ChessPiece[][] Testing = new ChessPiece[8][8];
                for(int i=0;i<8;i++) {
                	for(int j=0;j<8;j++) {
                		Testing[i][j]=Board[i][j];
                	}
                }
                ArrayList<ChessPiece> WPieces = new ArrayList<ChessPiece>();
                ArrayList<ChessPiece> BPieces = new ArrayList<ChessPiece>();
                
                for(ChessPiece piece: WhitePieces) {
                	WPieces.add(piece);
            	}
                for(ChessPiece piece: BlackPieces) {
                	BPieces.add(piece);
            	}
                
                int index = 0;
                if(WPieces.contains(tmp)) {
                	index = WPieces.indexOf(tmp);
                }
                else if(BPieces.contains(tmp)) {
                	index = BPieces.indexOf(tmp);
                }
                
                ChessPiece Pawn = Testing[rankStart][fileDest];
                
                int EnPassIndex = 0;
                if(whiteMove && enp) {
                	EnPassIndex = BPieces.indexOf(Pawn);
                }
                else if(!whiteMove && enp) {
                	EnPassIndex = WPieces.indexOf(Pawn);
                }
                
                if(enp) {
                	Testing[rankStart][fileDest] = null; 
                }
                
                
                ChessPiece remove = Testing[rankDest][fileDest];
                if(enp) {
                	remove = Testing[rankStart][fileDest];
                }
                if(promote) { 	
                	
                	tmp=promoted;
                }
                tmp.moved = true;
                tmp.file = fileDest;
                tmp.rank = rankDest;
                
                Testing[rankDest][fileDest] = tmp;
                Testing[rankStart][fileStart] = null;
                
                if(castleShort) {
                	if(whiteMove) {
                		ChessPiece Rook = Testing[7][7];
                		int rookIndex = WPieces.indexOf(Rook);
                		Rook.file=5;
                		Rook.moved = true;
                		Testing[7][5] = Rook;
                		Testing[7][7]=null;
                		WPieces.set(rookIndex, Rook);
                	}
                	else {
                		ChessPiece Rook = Testing[0][7];
                		int rookIndex = BPieces.indexOf(Rook);
                		Rook.file=5;
                		Rook.moved = true;
                		Testing[0][5] = Rook;
                		Testing[0][7]=null;
                		BPieces.set(rookIndex, Rook);
                	}
                }
                else if(castleLong) {
                	if(whiteMove) {
                		ChessPiece Rook = Testing[7][0];
                		int rookIndex = WPieces.indexOf(Rook);
                		Rook.file=3;
                		Rook.moved = true;
                		Testing[7][3] = Rook;
                		Testing[7][0]=null;
                		WPieces.set(rookIndex, Rook);
                	}
                	else {
                		ChessPiece Rook = Testing[0][0];
                		int rookIndex = BPieces.indexOf(Rook);
                		Rook.file=3;
                		Rook.moved = true;
                		Testing[0][3] = Rook;
                		Testing[0][0]=null;
                		BPieces.set(rookIndex, Rook);
                	}
                }
                
                if(tmp.type.equals("white")) {
                	WPieces.set(index,tmp);
                }
                else if(tmp.type.equals("black")){
                	BPieces.set(index,tmp);
                }
                
                if (whiteMove) {
                    if (remove != null) {
                    	BPieces.remove(remove);
                    }
                } else {
                    if (remove != null) {
                    	WPieces.remove(remove);
                    }
                }
                //Now test if own king is in danger and if opposite king is in danger
                
                int[][] wKingAttacked = null;
                int[][] bKingAttacked = null;
                
            	if(whiteMove) {
            		if(name.equals("King")) {
            			wKingAttacked = isAttacked(Testing,rankDest,fileDest,"white",BPieces);
                	}
            		else {
            			wKingAttacked = isAttacked(Testing,wKingRank,wKingFile,"white",BPieces);
            		}
                    bKingAttacked = isAttacked(Testing,bKingRank,bKingFile,"black",WPieces);
            	}
            	else {
            		wKingAttacked = isAttacked(Testing,wKingRank,wKingFile,"white",BPieces);
            		if(name.equals("King")) {
            			bKingAttacked = isAttacked(Testing,rankDest,fileDest,"black",WPieces);
            		}
            		else {
            			bKingAttacked = isAttacked(Testing,bKingRank,bKingFile,"black",WPieces);
            		}
            	}
            	
                if(whiteMove) {
                	if((wKingAttacked!=null)) {
	                	System.out.println("Illegal move, try again");
	                	continue;
                	}
                	if((bKingAttacked!=null)) {//Check for checkmate and check
                		Boolean CheckTest = false;
                		
            			CheckTest = CheckMate(Testing,bKingRank,bKingFile,"white",BPieces,WPieces,bKingAttacked);
            			if(CheckTest) {
            				System.out.println("Checkmate");
            				System.out.println("White wins");
            				break;
            			}
                		System.out.println("Check");
                	}
                }
                if(!whiteMove) {
                	if((bKingAttacked!=null)) {
	                	System.out.println("Illegal move, try again");
	                	continue;
                	}
                	if((wKingAttacked!=null)) {//Check for checkmate and check
                		Boolean CheckTest = false;
                		
            			CheckTest = CheckMate(Testing,wKingRank,wKingFile,"black",BPieces,WPieces,wKingAttacked);
            			if(CheckTest) {
            				System.out.println("Checkmate");
            				System.out.println("Black wins");
            				break;
            			}
                		System.out.println("Check");
                	}
                }
                //If king is moving update position so we can check for check and mate
                if (name.equals("King")) {
                    if (tmp.type.equals("white")) {
                        wKingRank = rankDest;
                        wKingFile = fileDest;
                    } else {
                        bKingRank = rankDest;
                        bKingFile = fileDest;
                    }
                }
                //If all is good update actual board to temp data
                Board = Testing;
                BlackPieces = BPieces;
                WhitePieces = WPieces;
                
                //If a draw is requested
                if(split.length == 3)
                	if(split[2].equals("draw?"))
                		draw=true;

            }//end IF
            whiteMove = !whiteMove; //switch to Black's turn
            System.out.println();
            print(Board);
            
        }
    }
    
    /**
     * Determines which pieces if any are attacking a particular square.
     * 
     * @param Board The current board and its piece arrangement.
     * @param rank The rank of the square requested.
     * @param file The file of the square requested.
     * @param currColor The current color that is being potentially threatened.
     * @param Pieces ArrayList of chess pieces to test on the square.
     * @return result 2D integer matrix containing attackers to square(num attackers=matrix len), storing Rank first then File.
     */
    
    public static int[][] isAttacked(ChessPiece Board[][], int rank, int file, String currColor, ArrayList<ChessPiece> Pieces) { //use this to determine if king is in check
        int x = 0;
        int[][] res = new int[16][2];
        for (ChessPiece piece: Pieces) {
        	if (piece.type.equals(currColor)) continue;
            Boolean Attacker = piece.test(file, rank, Board);
            if (Attacker) {
                res[x][0] = piece.rank;
                res[x][1] = piece.file;
                x++;
            }
        }
        int[][] result = new int[x][2];
        if(x!=0) {
	        for(int y =0;y<x;y++) {
	        	result[y][0]=res[y][0];
	        	result[y][1]=res[y][1];
	        }
        }
        if (x == 0) return null;
        return result;
    }
    
    /**
     * Checkmate determines whether checkmate is on the board.
     * 
     * 
     * 
     * @param Board The current board storing the arrangement of pieces.
     * @param rank The current rank(e.g.,1,2,3...8) of the King.
     * @param file The current file(e.g.,a,b,c...h) of the King.
     * @param currColor The color of the piece that just moved prior to method call.
     * @param BPieces An ArrayList of the black pieces.
     * @param WPieces An ArrayList of the white pieces.
     * @param Attackers A 2d matrix storing indices of pieces attacking the square at the given rank and file.
     * @return checkmate A Boolean value storing data on whether the King is in checkmate.
     * 
     * 
     * {@summary
     * 	Checks for checkmate in 3 total steps. Step 1: checks for any vacant squares the King may run to. 
     *  Step 2: Checks if the attacking piece can be taken(if 2 attackers then it is checkmate because you must
     *  move King in double check). Finally in step 3 it checks if any piece can safely block a square between the king
     *  and the attacking piece.
     * }
     * 
     * 
     */
    public static boolean CheckMate(ChessPiece Board[][],int rank, int file, String currColor,ArrayList<ChessPiece> BPieces,ArrayList<ChessPiece> WPieces,int[][] Attackers) {
    	int res = 0;
    	String color = "";
    	if(currColor.equals("white")) {
    		color = "black";
    	}
    	else {
    		color="white";
    	}
    	ChessPiece King = Board[rank][file];
    	Board[rank][file]=null;
    	
    	Boolean checkmate = true;
    	//Check if the King can move away
    	
    	for(int i = -1;i<2;i++) {
    		for(int j = -1;j<2;j++) {
    			
    			//Make sure i and j are valid
    			if(i==0 && j==0) continue;
    			if(rank+i>7 || rank+i<0 || file+i>7 || file+i<0) continue;
    			
    			//Check if same color piece
    			ChessPiece temp = Board[rank+i][file+j];
    			
    			if(temp!=null && temp.type.equals(color)) continue;
    			
    			if(temp!=null && !temp.type.equals(color)) {
    				Board[rank+i][file+j]=null;
    			}
    			
    			//Check if square attacked by enemy piece
    			if(currColor.equals("white")) {
    				if(isAttacked(Board,rank+i,file+j,"black",WPieces)==null) {
    					checkmate = false;
    					res++;
    				}
    			}
    			else {
    				if(isAttacked(Board,rank+i,file+j,"white",BPieces)==null) {
    					checkmate = false;
    					res++;
    				}
    			}
    			Board[rank+i][file+j]=temp;
        	}
    	}
    	
    	Board[rank][file] = King;
    	if(res>0) return checkmate;
    	
    	//If double check and King can't  move then it is checkmate
    	if(Attackers.length >= 2) {
    		checkmate=true;
    		return checkmate;
    	}
    	int[][] AttackingPieces = null;
    	if(currColor.equals("white"))
    		AttackingPieces = isAttacked(Board,rank,file,color,WPieces);
    	else
    		AttackingPieces = isAttacked(Board,rank,file,color,BPieces);
    	
    	//Check if Attacker can be taken
    	int AttackRank = AttackingPieces[0][0];
    	int AttackFile = AttackingPieces[0][1];
    	
    	
    	ChessPiece AttackingPiece = Board[AttackRank][AttackFile];
    	Board[AttackRank][AttackFile] = null;
    	
    	if(currColor.equals("white")) {
    		int[][] Defenders = isAttacked(Board,AttackRank,AttackFile,"white",BPieces);
    		if(Defenders!=null) {
    			WPieces.remove(AttackingPiece);
    			
    			for(int i=0;i<Defenders.length;i++) {
    				int DRank = Defenders[i][0];
    				int DFile = Defenders[i][1];
    				ChessPiece Defender = Board[DRank][DFile];
    				if(Defender.name.equals("pawn") && (DFile==AttackFile)) continue;
    				
    				Board[AttackRank][AttackFile] = Defender;
    				Board[DRank][DFile] = null;
    				
    				int[][] Attacks = isAttacked(Board,rank,file,"black",WPieces);
    				if(Defender.name.equals("King")) {
    					Attacks = isAttacked(Board,AttackRank,AttackFile,"black",WPieces);
    				}
    				if(Attacks==null) {
    					checkmate = false;
    					res++;
    				}
    				Board[AttackRank][AttackFile] = null;
    				Board[DRank][DFile] = Defender;
    			}
    			WPieces.add(AttackingPiece);
    		}
    	}
    	else {
    		int[][] Defenders = isAttacked(Board,AttackRank,AttackFile,"black",WPieces);
    		if(Defenders!=null) {
    			BPieces.remove(AttackingPiece);
    			for(int i=0;i<Defenders.length;i++) {
    				int DRank = Defenders[i][0];
    				int DFile = Defenders[i][1];
    				ChessPiece Defender = Board[DRank][DFile];
    				if(Defender.name.equals("pawn") && (DFile==AttackFile)) continue;
    				Board[AttackRank][AttackFile] = Defender;
    				Board[DRank][DFile] = null;
    				int[][] Attacks = isAttacked(Board,rank,file,"white",BPieces);
    				if(Defender.name.equals("King")) {
    					Attacks = isAttacked(Board,AttackRank,AttackFile,"black",WPieces);
    				}
    				if(Attacks==null) {
    					checkmate = false;
    					res++;
    				}
    				Board[AttackRank][AttackFile] = null;
    				Board[DRank][DFile] = Defender;
    			}
    			BPieces.add(AttackingPiece);
    		}
    	}
    	Board[AttackRank][AttackFile] = AttackingPiece;
    	
    	if(res>0) return checkmate;
    	//Now if the attacker can't be taken then we check if the attack can be blocked
    	int[][] BlockSquares = new int[6][2];
    	int x = 0;
    	
    	if(rank==AttackRank) {
    		if(file<AttackFile) {
    			for(int i = file+1;i<AttackFile;i++) {
    				BlockSquares[x][0]= AttackRank;
    				BlockSquares[x][1]= i;
    				x++;
    			}
    		} else {
    			for(int i = AttackFile+1;i<file;i++) {
    				BlockSquares[x][0]= AttackRank;
    				BlockSquares[x][1]= i;
    				x++;
    			}
    		}
    	}
    	else if(file==AttackFile) {
    		if(rank<AttackRank) {
    			for(int i=rank+1;i<AttackRank;i++) {
    				BlockSquares[x][0]=i;
    				BlockSquares[x][1]=AttackFile;
    				x++;
    			}
    		} else {
    			for(int i=AttackRank+1;i<rank;i++) {
    				BlockSquares[x][0]=i;
    				BlockSquares[x][1]=AttackFile;
    				x++;
    			}
    		}
    	}
    	else if(Math.abs(file-AttackFile)==Math.abs(rank-AttackRank)) {
    		int diff1 = 1;
    		int diff2 = 1;
    		if(rank>AttackRank) {
    			diff1=-1;
    		}
    		if(file>AttackFile) {
    			diff2=-1;
    		}
    		for(int i=1;i<Math.abs(file-AttackFile);i++) {
    			BlockSquares[x][0]=rank+(diff1*i);
    			BlockSquares[x][1]=file+(diff2*i);
    			x++;
    		}
    	}
    	
    	for(int i=0;i<x;i++) {
    		int BlockRank = BlockSquares[i][0];
    		int BlockFile = BlockSquares[i][1];
    		
    		if(currColor.equals("white")) {
    			int[][] DefPieces = isAttacked(Board,BlockRank,BlockFile,"white",BPieces);
    			if(DefPieces==null) {
    				continue;
    			}
    			else {
    				for(int k=0;k<DefPieces.length;k++) {
    					int DefRank = DefPieces[k][0];
    					int DefFile = DefPieces[k][1];
    					ChessPiece DefPiece = Board[DefRank][DefFile];
    					if(DefPiece.name.equals("King")) {
    						continue;
    					}
    					Board[DefRank][DefFile] = null;
    					Board[BlockRank][BlockFile]=DefPiece;
    					
    					int[][] check = isAttacked(Board,rank,file,"black",WPieces);
    					if(check==null) {
    						checkmate = false;
    						res++;
    					}
    					Board[DefRank][DefFile] = DefPiece;
    					Board[BlockRank][BlockFile] = null;
    					if(res>0) return checkmate;
    				}
    			}
    		}
    		else {
    			int[][] DefPieces = isAttacked(Board,BlockRank,BlockFile,"black",WPieces);
    			if(DefPieces==null) continue;
    			else {
    				for(int k=0;k<DefPieces.length;k++) {
    					int DefRank = DefPieces[k][0];
    					int DefFile = DefPieces[k][1];
    					ChessPiece DefPiece = Board[DefRank][DefFile];
    					if(DefPiece.name.equals("King")) {
    						continue;
    					}
    					Board[DefRank][DefFile] = null;
    					Board[BlockRank][BlockFile]=DefPiece;
    					
    					int[][] check = isAttacked(Board,rank,file,"white",BPieces);
    					if(check==null) {
    						checkmate = false;
    						res++;
    					}
    					Board[DefRank][DefFile] = DefPiece;
    					Board[BlockRank][BlockFile] = null;
    					if(res>0) return checkmate;
    				}
    			}
    		}
    	}
    	Board[AttackRank][AttackFile] = AttackingPiece;
    	return checkmate;
    }

    /**
     * This method checks whether the conditions to castle are met.
     * @param Board The current state of the chess board
     * @param fileDest The file of target square
     * @param fileStart the file of current square that piece is on
     * @param rankStart the rank of current square that piece is on
     * @param pieces Arraylist of current color's chess pieces
     * @param type current move's color --> white or black
     * @return true if the concerned squares are attacked otherwise false
     */
    
    public static boolean Castlecheck(ChessPiece[][] Board, int fileDest,int fileStart, int rankStart, ArrayList<ChessPiece> pieces, String type) {
    	
    	if((fileDest-fileStart==2)) { //checking the "check" restriction of castling
        	//right side castle
        	if(isAttacked(Board,rankStart,fileStart,type,pieces)!=null || isAttacked(Board,rankStart,fileStart+1,type,pieces)!=null || isAttacked(Board,rankStart,fileStart+2,type,pieces)!=null) {
        		//Can't castle if any of the squares that king travels through are attacked
                return true; 
        	}
        }
        else if((fileStart-fileDest==2)) {
        	//left side castle
        	if((isAttacked(Board,rankStart,fileStart,type,pieces)!=null || isAttacked(Board,rankStart,fileStart-1,type,pieces)!=null || isAttacked(Board,rankStart,fileStart-2,type,pieces)!=null || isAttacked(Board,rankStart,fileStart-3,type,pieces)!=null)) {
        		//Can't castle if any of the squares that king travels through are attacked
                return true;
        	}
        }
    	return false;
    	
    	
    }
    /**
     * makes the elements of given array equal to false
     * @param enpassant a boolean array
     */
    
    public static void makeFalse(boolean[] enpassant) {
    	for(int i=0;i<8;i++) {
    		enpassant[i]=false;
    	}
    }
    /**
     * This method checks the conditions of an enpassant capture and returns a boolean result
     * @param targetFile the file of target square
     * @param targetRank the rank of target square
     * @param rank the rank of square which is being moved
     * @param file the file of square which is being moved
     * @param Board the current state of chess board
     * @param enpassant an array which specifies the pawns eligible to be captured through enpassant
     * @return true if the targeted pawn moved twice in the previous move and the target square is empty
     */
    public static boolean enpassantCheck(int targetFile,int targetRank,int rank,int file, ChessPiece[][] Board,boolean[] enpassant) {
    	//checking whether its a pawn and target square is empty
    	if(Board[rank][file].name.equals("pawn") && Board[targetRank][targetFile]==null) { 
    		//checking if white pawn is moving 1 square in diagonal 
	    	if(Math.abs(targetFile-file)==1 && targetRank==rank-1 && Board[rank][file].type.equals("white")) {
				//trying to enpassant capture the black pawn
				if(Board[rank][targetFile].name.equals("pawn") && enpassant[targetFile]==true) {
					return true;
				}
			}
	    	else if(Math.abs(targetFile-file)==1 && targetRank==rank+1 && Board[rank][file].type.equals("black")) { 
	    		//trying to enpassant capture white pawn
	    		if(Board[rank][targetFile].name.equals("pawn") && enpassant[targetFile]==true) {
					return true;
				}
	    	}
    	}
    	return false;
    }
    /**
     * This method sets up the chess board with all the pieces at their default location
     * @return the newly created chessboard
     */
    public static ChessPiece[][] make() {

        ChessPiece Board[][] = new ChessPiece[8][8];
        //Initializing and setting Black pieces on board
        Board[0][4] = new King("black", 4, 0, false);
        Board[0][0] = new Rook("black", 0, 0, false);
        Board[0][7] = new Rook("black", 7, 0, false);
        Board[0][1] = new Knight("black", 1, 0, false);
        Board[0][6] = new Knight("black", 6, 0, false);
        Board[0][2] = new Bishop("black", 2, 0, false);
        Board[0][5] = new Bishop("black", 5, 0, false);
        Board[0][3] = new Queen("black", 3, 0, false);
        char temp = 0; //for pawn file
        for (int j = 0; j < 8; j++) {
            Board[1][j]=new Pawn("black",temp++,1,false); //setting black pawns on the board
        }

        //Initializing and setting White pieces on board
        Board[7][4] = new King("white", 4, 7, false);
        Board[7][0] = new Rook("white", 0, 7, false);
        Board[7][7] = new Rook("white", 7, 7, false);
        Board[7][1] = new Knight("white", 1, 7, false);
        Board[7][6] = new Knight("white", 6, 7, false);
        Board[7][2] = new Bishop("white", 2, 7, false);
        Board[7][5] = new Bishop("white", 5, 7, false);
        Board[7][3] = new Queen("white", 3, 7, false);
        temp = 0;
        for (int j = 0; j < 8; j++) {
            Board[6][j]=new Pawn("white",temp++,6,false); //setting black pawns on the board
        }

        return Board;
    }
}