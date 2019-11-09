package com.options;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BlackSholes
 */
@WebServlet("/BlackSholes")
public class BlackSholes extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BlackSholes bs = new BlackSholes();
		double s     = Double.parseDouble(request.getParameter("s"));
        double x     = Double.parseDouble(request.getParameter("x"));
        double r     = Double.parseDouble(request.getParameter("r"));
        double sigma = Double.parseDouble(request.getParameter("sigma"));
        double t     = Double.parseDouble(request.getParameter("t"));
        PrintWriter out =  response.getWriter();
        out.println("callPrice: "+bs.callPrice(s, x, r, sigma, t));
        out.println("call: "+bs.call(s, x, r, sigma, t));
        out.println("call2: "+bs.call2(s, x, r, sigma, t));
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

    public double pdf(double x) {
        return Math.exp(-x*x / 2) / Math.sqrt(2 * Math.PI);
    }

    public double callPrice(double s, double x, double r, double sigma, double t) {
        double d1 = (Math.log(s/x) + (r + sigma * sigma/2) * t) / (sigma * Math.sqrt(t));
        double d2 = d1 - sigma * Math.sqrt(t);
        return s * cdf(d1) - x * Math.exp(-r*t) * cdf(d2);
    }

    public double call(double s, double x, double r, double sigma, double t) {
        Random StdRandom = new Random();
        int n = 10000;
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            double eps = StdRandom.nextGaussian();
            double price = s * Math.exp(r*t - 0.5*sigma*sigma*t + sigma*eps*Math.sqrt(t));
            double value = Math.max(price - x, 0);
            sum += value;
        }
        double mean = sum / n;
     
        return Math.exp(-r*t) * mean;
    }

    public double call2(double s, double x, double r, double sigma, double t) {
         Random StdRandom = new Random();
         int n = 10000;
         double sum = 0.0;
         for (int i = 0; i < n; i++) {
             double price = s;
             double dt = t/10000.0;
             for (double time = 0; time <= t; time += dt) {
                 price += r*price*dt +sigma*price*Math.sqrt(dt)*StdRandom.nextGaussian();
             }
             double value = Math.max(price - x, 0);
             sum += value;
         }
         double mean = sum / n;
     
         return Math.exp(-r*t) * mean;
    }

}
