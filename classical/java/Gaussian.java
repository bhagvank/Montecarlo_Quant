public class Gaussian {

    public Gaussian()
    {

    }

    public double pdf(double x) {
        return Math.exp(-x*x / 2) / Math.sqrt(2 * Math.PI);
    }

    public double pdf(double x, double mu, double sigma) {
        return pdf((x - mu) / sigma) / sigma;
    }

    public double cdf(double z) {
        if (z < -8.0) return 0.0;
        if (z >  8.0) return 1.0;
        double sum = 0.0, term = z;
        for (int i = 3; sum + term != sum; i += 2) {
            sum  = sum + term;
            term = term * z * z / i;
        }
        return 0.5 + sum * pdf(z);
    }

    public double cdf(double z, double mu, double sigma) {
        return cdf((z - mu) / sigma);
    } 

    public double inverseCDF(double y) {
        return inverseCDF(y, 0.00000001, -8, 8);
    } 

    private double inverseCDF(double y, double delta, double lo, double hi) {
        double mid = lo + (hi - lo) / 2;
        if (hi - lo < delta) return mid;
        if (cdf(mid) > y) return inverseCDF(y, delta, lo, mid);
        else              return inverseCDF(y, delta, mid, hi);
    }


    @Deprecated
    public double phi(double x) {
        return pdf(x);
    }

    @Deprecated
    public double phi(double x, double mu, double sigma) {
        return pdf(x, mu, sigma);
    }

    @Deprecated
    public double Phi(double z) {
        return cdf(z);
    }

    @Deprecated
    public double Phi(double z, double mu, double sigma) {
        return cdf(z, mu, sigma);
    } 

    @Deprecated
    public double PhiInverse(double y) {
        return inverseCDF(y);
    } 

    public static void main(String[] args) {
        double z     = Double.parseDouble(args[0]);
        double mu    = Double.parseDouble(args[1]);
        double sigma = Double.parseDouble(args[2]);
        Gaussian g = new Gaussian();
        System.out.println(g.cdf(z, mu, sigma));
        double y = g.cdf(z);
        System.out.println(g.inverseCDF(y));
    }
}