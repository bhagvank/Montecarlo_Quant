import java.util.*;

public class AsianOption
{

   public AsianOption(){

   }

   public static void main(String[] args)
   {
    
        AsianOption asianOption = new AsianOption();
        asianOption.calculateOptionPrice();
       
   }

   public static void calculateOptionPrice(){
 
        double T = 1.0; 
        double r = 0.02; 
        double vol = 0.4; 
        double dt = 1.0/250.0; 
        double S0 = 100.0; 
        double K = 100.0; 
        int sims = 100000;

        double a = (r - (0.5*Math.pow(vol, 2)))*dt;
        double b = vol*Math.sqrt(dt);
        
        double result = 0.0;
        for (int i = 0; i < sims; i++) {
            double avgPrice = 0.0;
            int steps = 0;
            double lastPrice = S0;
            for (double t = dt; t <= T; t += dt) {
                Random random= new Random();
                lastPrice = lastPrice*Math.exp(a
                        + (b*random.nextGaussian()));
                avgPrice += lastPrice;
                steps++;
            }
            avgPrice = avgPrice/steps; 
            result += Math.max(0, avgPrice-K);
        }
        result = result/sims;
        result = result*Math.exp(-r*T); 

		System.out.println("result = " + result );
   }
}