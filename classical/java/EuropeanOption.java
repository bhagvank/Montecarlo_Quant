import java.text.DecimalFormat;
 
public class EuropeanOption {
 

	public EuropeanOption(){

    }
    
    public static void main(String[] args) {

		EuropeanOption  europeanOption = new EuropeanOption();
		europeanOption.calculateRate();
       
    }

	public static void calculateRate(){
		double stock_price = 85.0d; 
        int strike_price = 90; 
        double interest_rate = 0.08d; 
        int numberOfSteps = 400; 
        double timeTillExpiration = (double) 6/12; 
        int numberOfComputations = 100000; 
        double volatility = 0.2d; 
        double totalPayoff = 0;
        double averagePayoff;
        double compundRate;
        double O; 
        int m = 0;
        while (m < numberOfComputations) {
            totalPayoff += getPayoffs(numberOfSteps, stock_price, interest_rate, timeTillExpiration, volatility, strike_price);
            m++;
            }
        averagePayoff = totalPayoff/numberOfComputations;
        compundRate = retrieveCompundCoefficient(interest_rate, timeTillExpiration, numberOfSteps);
        O = averagePayoff/compundRate;
        DecimalFormat df = new DecimalFormat("##.####");
        System.out.println(df.format(O));
	}
 
    public static double getPayoffs(int N, double S, double r, double T, double sigma, int P){
        int eta;  
        int n = 0;
        double tempS = S;
        while (n < N) {
            double rd = Math.random();
            if (rd > 0.5) {
                eta = 1;
            } else {
                eta = -1;
            }
            tempS = tempS + r*tempS*(T/N) + sigma*eta*tempS*Math.sqrt(T/N);
            n++;
        }
        return Math.max(tempS - P, 0); 
    }
 
    public static double retrieveCompundCoefficient(double r, double T, int N) {
        double coefficient = Math.pow(1+r*(T/N), (double) N);
        return coefficient;
    }
 
}