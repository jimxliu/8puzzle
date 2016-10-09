import java.util.ArrayDeque;
import edu.princeton.cs.algs4.MinPQ;
import java.util.Comparator;
import edu.princeton.cs.algs4.Stopwatch;

public class Solver{

	private MinPQ<SNode> pq; // priority queue for the initial board
	private ArrayDeque<Board> steps; // sequence of solution steps
	private Board ori;  // initial board
	private Board oriTwin;
	private SNode head;

    public Solver(Board initial){    // find a solution to the initial board (using the A* algorithm)
		if(initial == null)
			throw new NullPointerException();
		Stopwatch watch = new Stopwatch();
		head = new SNode(null, null, 0);
		ori = initial;
		oriTwin = initial.twin();
		pq = new MinPQ<>(keyOrder());
		steps = new ArrayDeque<>();
		pq.insert(new SNode(ori,head, 0));
		pq.insert(new SNode(oriTwin, head, 0));
		while(pq.min().goalBoard() != true){
			//System.out.println("Goal board yet? "+pq.min().goalBoard());
			SNode sn = pq.delMin();			
			Board b = sn.getBoard();
			//System.out.println("Board \n" + b); // print check
			//System.out.println("Moves:" + sn.getMoves()); // print check
			//System.out.println("Priority: " + sn.pFunction()); // print check
			//System.out.println("Manhattan: " + (sn.pFunction() - sn.getMoves())); // print check
			for(Board n: b.neighbors()){
				if(!n.equals(sn.getPrev().getBoard())){
					pq.insert(new SNode(n, sn, sn.getMoves()+1));			
				}
			}
		}
		SNode snGoal = pq.min();
		//System.out.println("Board \n" + snGoal.getBoard()); // print check
		//System.out.println("Moves:" + snGoal.getMoves()); // print check
		//System.out.println("Priority: " + snGoal.pFunction()); // print check
		//System.out.println("Manhattan: " + (snGoal.pFunction() - snGoal.getMoves())); // print check

		SNode cursor = snGoal;
		//System.out.println("Reversed steps:");	// print check	 
		while(cursor != head){
			//System.out.println(cursor.getBoard()); // print check
			steps.addFirst(cursor.getBoard());
			cursor = cursor.getPrev();		
		}
		//System.out.println("Elapsed time: "+ watch.elapsedTime());
	}
    public boolean isSolvable(){  // is the initial board solvable?
		//System.out.println("source Board : " + steps.peekFirst()); // print check		
		//System.out.println("Initial Board : " + ori); // print check		
		//System.out.println("Equals : " + steps.peekFirst().equals(ori)); // print check				
		return steps.peekFirst().equals(ori);    	
	}
	public int moves(){        // min number of moves to solve initial board; -1 if unsolvable
		if(!isSolvable())
			return -1;
		return steps.size() - 1;
	}
    public Iterable<Board> solution(){    // sequence of boards in a shortest solution; null if unsolvable
		if(!isSolvable())
			return null;
		return steps;
		
	}
	private Comparator<SNode> keyOrder(){
		return new pComparator();
	}

	private class pComparator implements Comparator<SNode>{
		public int compare(SNode sn1, SNode sn2){
			if(sn1.pFunction() > sn2.pFunction() || (sn1.pFunction() == sn2.pFunction() && sn1.getMan() > sn2.getMan()))
				return 1;
			if(sn1.pFunction() == sn2.pFunction() && sn1.getMan() == sn2.getMan() && sn1.getHam() > sn2.getHam())
				return 1;
			if(sn1.pFunction() < sn2.pFunction() || (sn1.pFunction() == sn2.pFunction() && sn1.getMan() < sn2.getMan()))
				return -1;
			if(sn1.pFunction() == sn2.pFunction() && sn1.getMan() == sn2.getMan() && sn1.getHam() < sn2.getHam())
				return -1;
			return 0;
			
		}
	}
	private class SNode{   // Key class keeps information of a board and its associated moves, manhattan value, and priority function
		private Board b;
		private int moves;
		private int man;
		private int ham;
		private int priority;
		private SNode prev;
		public SNode(Board b, SNode prev, int moves){
			this.b = b;
			this.moves = moves;
			this.prev = prev;
			if(b != null){
				man = b.manhattan();
				ham = b.hamming();
				priority = moves + man;
			}
		}
		public int getMoves(){
			return moves;		
		}
		public int pFunction(){
			return priority;
		}
		public int getMan(){
			return man;
		}
		public int getHam(){
			return ham;
		}
		public Board getBoard(){
			return b;		
		}
		public SNode getPrev(){
			return prev;
		}
		public boolean goalBoard(){
			return b.isGoal();
		}
	} 
    public static void main(String[] args){ // solve a slider puzzle (given below)
			
	}
}

