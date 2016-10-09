import java.util.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

public class Board {

	private int[][] blocks; // 2d array to contain elements in the board
	private int blank_x;// coordinate track the "0" blank space in the board	
	private int blank_y;// coordinate track the "0" blank space in the board
	private int N; // dimension size
	private int ham; // hamming priority function
	private int man;	// manhattan priority function

	public Board(int[][] b){           // construct a board from an n-by-n array of blocks
		ham = 0;
		man = 0;	
		N = b.length;
		blocks = new int[N][N];   
    	for(int i = 0; i < N; i++)    // (where blocks[i][j] = block in row i, column j)
			for(int j = 0; j < N; j++){
				blocks[i][j] = b[i][j];
				if(blocks[i][j] == 0){  // hamming() and manhattan() don't count blank space represented by "0"
					blank_y = i;
					blank_x = j;
				}else{
					if(blocks[i][j] != (i*N+j+1))
						ham++;                            // calculate hamming function
					int goal_y = (blocks[i][j] - 1) / N;
					int goal_x = (blocks[i][j] - 1) % N;
					man += Math.abs(i-goal_y) + Math.abs(j-goal_x);	 // calculate manhattan function
				}			
     		}
	}    
	public int dimension(){              // board dimension n
		return N;
	}    
	public int hamming(){         // number of blocks out of place
		return ham;
	}
	public int manhattan(){        // sum of Manhattan distances between blocks and goal
		return man;   
	}
	public boolean isGoal() {        // is this board the goal board?
		return manhattan() == 0;
	}
    public Board twin() {                 // a board that is obtained by exchanging any pair of blocks
		int y1 = StdRandom.uniform(0,N);      // make sure neither of the blocks is the blank space
		int x1 = StdRandom.uniform(0,N);
		while(y1 == blank_y && x1 == blank_x){
			y1 = StdRandom.uniform(0,N);
			x1 = StdRandom.uniform(0,N);
		}
		int y2 = StdRandom.uniform(0,N);
		int x2 = StdRandom.uniform(0,N);
		while((y2 == blank_y && x2 == blank_x) || (y2 == y1 && x2 == x1)){
			y2 = StdRandom.uniform(0,N);
			x2 = StdRandom.uniform(0,N);
		}
		
		swap(blocks, x1, y1, x2, y2);
		Board twinBoard = new Board(blocks);
		swap(blocks, x1, y1, x2, y2);
   		return twinBoard;
	}
    public boolean equals(Object y) {     // does this board equal y?
		if(y == null) return false;
		if(y == this) return true;		
		if(y.getClass() != this.getClass()) return false;
		Board that = (Board) y;
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				if(this.blocks[i][j] != that.blocks[i][j])
					return false;
		return true;
	}
    public Iterable<Board> neighbors(){     // all neighboring boards
    	Stack<Board> boardStack = new Stack<Board>();
		if(blank_y > 0){  // blank space goes up
			swap(blocks, blank_x, blank_y, blank_x, blank_y-1);
			Board up = new Board(blocks);
			boardStack.push(up);
			swap(blocks, blank_x, blank_y, blank_x, blank_y-1);
		}
		if(blank_y < dimension()-1){ // blank space goes down
			swap(blocks, blank_x, blank_y, blank_x, blank_y+1);
			Board down = new Board(blocks);
			boardStack.push(down);
			swap(blocks, blank_x, blank_y, blank_x, blank_y+1);
		}
		if(blank_x > 0){   // blank space goes left
			swap(blocks, blank_x, blank_y, blank_x-1, blank_y);
			Board left = new Board(blocks);
			boardStack.push(left);
			swap(blocks, blank_x, blank_y, blank_x-1, blank_y);
		}
		if(blank_x < dimension()-1){ // blank space goes right
			swap(blocks, blank_x, blank_y, blank_x+1, blank_y);
			Board right = new Board(blocks);
			boardStack.push(right);
			swap(blocks, blank_x, blank_y, blank_x+1, blank_y);
		}
		return boardStack;
	}	
	public String toString(){ // string representation of this board (in the output format specified below)
		String str = "";
		str += dimension() + "\n";		
		for(int i=0; i<dimension(); i++){
			for(int j=0; j<dimension(); j++){
				str += " " + blocks[i][j];
			}
			str += "\n"; 
		}
		return str;
	}

	private void swap(int[][] arr, int x1, int y1, int x2, int y2){   // swap the elements in two blocks, not blank space
		int temp = arr[y1][x1];
		arr[y1][x1] = arr[y2][x2];
		arr[y2][x2] = temp;
	}
	
	public static void main(String[] args){ // unit tests (not graded) as you like
		// create initial board from file
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] blocks = new int[n][n];
		for (int i = 0; i < n; i++)
		    for (int j = 0; j < n; j++)
		        blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);
		System.out.println("Initial board:");
		System.out.println(initial);
		System.out.println("Manhattan: "+initial.manhattan());
		System.out.println("Neighbor board:");
		for(Board b: initial.neighbors()){
			System.out.println(b);
			System.out.println("Manhattan: "+b.manhattan());
		}
	}
}
