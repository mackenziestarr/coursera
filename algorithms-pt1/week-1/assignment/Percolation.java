public class Percolation {

   private int grid_size;              // N in NxN grid
   private WeightedQuickUnionUF grid;
   private int[] states;
   private int virtual_top;            // index N*N in grid
   private int virtual_bottom;         //index N*N + 1 in grid

   //constructor
   public Percolation(int N) {
     if (N <= 0) throw new IllegalArgumentException("argument must be greater than or equal to zero");
     grid_size = N;
     grid = new WeightedQuickUnionUF(N*N + 2);
     states = new int[N*N + 2];
     virtual_top = N*N;
     virtual_bottom = N*N + 1;
   }

   //convert cartesian to 1-dimension using row-major order
   private int To1D(int row, int col){
     return grid_size * (row - 1) + (col - 1);
   }

   //test list for range against 0..N
   private void testRange(int... args) {
     for (int arg : args) {
       if (arg <= 0 || arg > grid_size)
         throw new IndexOutOfBoundsException("row index " + arg +  " out of bounds 1..N");
     }
   }

   // open site (row i, column j) if it is not already
   public void open(int i, int j) {
     testRange(i, j);
     if ( isOpen(i,j) ) return;
     int index = To1D(i,j);
     states[index] = 1;                     //open site

     //union with all neighbors
     if ( i != 1 && isOpen(i-1,j) && !grid.connected(To1D(i-1,j), index) )           //if not in top row connect to upper cell
         grid.union( To1D(i-1,j), index );
     else if (i==1)
       grid.union( virtual_top, index );                                             //it is top row and connect to virtual_top

     if ( i != grid_size && isOpen(i+1,j) && !grid.connected(To1D(i+1,j), index) )   //if not in bottom row connect to lower cell
         grid.union( To1D(i+1,j), index );
     else if (i==grid_size)
       grid.union( virtual_bottom, index );                                          //it is bottom row and connect to virtual_bottom

     if ( j != 1 && isOpen(i,j-1) && !grid.connected(To1D(i,j-1), index) )           //if not on left edge connect to cell to the left
         grid.union( To1D(i,j-1), index );
     if ( j != grid_size && isOpen(i,j+1) && !grid.connected(To1D(i,j+1), index) )   //if not on right edge connect to cell on the right
         grid.union( To1D(i,j+1), index );
   }

   // is site (row i, column j) open?
   public boolean isOpen(int i, int j) {
     testRange(i, j);
     return states[To1D(i, j)] == 1;
   }

   // is site (row i, column j) full?
   public boolean isFull(int i, int j) {
     testRange(i, j);
     return isOpen(i, j) && grid.connected(virtual_top, To1D(i, j));
   }

   // does the system percolate?
   public boolean percolates(){
     return grid.connected(virtual_top, virtual_bottom);
   }

}
