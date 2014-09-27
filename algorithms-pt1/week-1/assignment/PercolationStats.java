public class PercolationStats {
   private double[] thresholds;

   public PercolationStats(int size, int total) {
     if (size <= 0 || total <=0)
       throw new java.lang.IllegalArgumentException("arguments must be greater than 0");
     thresholds = new double[total];
     for (int i = 0; i < total; i++) {
       Percolation sim = new Percolation(size);
       int count = 0;
       while (!sim.percolates()) {
         int x = StdRandom.uniform(size) + 1 ;
         int y = StdRandom.uniform(size) + 1 ;
         if (!sim.isOpen(x,y)) {
           sim.open(x,y);
           count++;
         }
       }
       thresholds[i] = (double)(count / Math.pow(size,2));
     }
   }

   public double mean() {
     return StdStats.mean(thresholds);
   }


   public double stddev() {
     return StdStats.stddev(thresholds);
   }

   public double confidenceLo() {
     return mean() - ( 1.96 * stddev() / Math.sqrt(thresholds.length) );
   }

   public double confidenceHi() {
     return mean() + ( 1.96 * stddev() / Math.sqrt(thresholds.length) );
   }

   public static void main(String[] args){
     int size = Integer.parseInt(args[0]);
     int total = Integer.parseInt(args[1]);
     PercolationStats stat = new PercolationStats(size,total);
     StdOut.printf("%-23s = %f\n", "mean", stat.mean() );
     StdOut.printf("%-23s = %f\n", "stddev", stat.stddev() );
     StdOut.printf("%-23s = %f , %f\n", "95% confidence interval", stat.confidenceLo(), stat.confidenceHi() );
   }
}
