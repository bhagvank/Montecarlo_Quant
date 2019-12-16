package com.options;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EuropeanOption
 */
@WebServlet("/EuropeanOption")
public class EuropeanOption extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EuropeanOption eo = new EuropeanOption();
		double stock_price = Double.parseDouble(request.getParameter("stock_price")); 
        double strike_price = Double.parseDouble(request.getParameter("strike_price")); 
        double interest_rate = Double.parseDouble(request.getParameter("interest_rate")); 
        int numberOfSteps = Integer.parseInt(request.getParameter("numberOfSteps")); 
        double timeTillExpiration = Double.parseDouble(request.getParameter("timeTillExpiration")); 
        int numberOfComputations = Integer.parseInt(request.getParameter("numberOfComputations")); 
        double volatility = Double.parseDouble(request.getParameter("volatility")); 
        double totalPayoff = Double.parseDouble(request.getParameter("totalPayoff"));
        double averagePayoff = Double.parseDouble(request.getParameter("averagePayoff"));
        double compundRate = Double.parseDouble(request.getParameter("compundRate"));
		double O = eo.europeanOption(stock_price,strike_price,interest_rate,numberOfSteps,timeTillExpiration,numberOfComputations,volatility,totalPayoff,averagePayoff,compundRate); 
        DecimalFormat df = new DecimalFormat("##.####");
        PrintWriter out =  response.getWriter();
        out.println("Result is "+O);
	}
	
	public double europeanOption(double stock_price, double strike_price, double interest_rate, int numberOfSteps, double timeTillExpiration, int numberOfComputations, double volatility, double totalPayoff, double averagePayoff, double compundRate)
	{
		EuropeanOption eo = new EuropeanOption();
		double O;
		int m = 0;
        while (m < numberOfComputations) {
            totalPayoff += eo.getPayoffs(numberOfSteps, stock_price, interest_rate, timeTillExpiration, volatility, strike_price);
            m++;
            }
        averagePayoff = totalPayoff/numberOfComputations;
        compundRate = eo.retrieveCompundCoefficient(interest_rate, timeTillExpiration, numberOfSteps);
        O = averagePayoff/compundRate;
        return O;
	}

	public double getPayoffs(int N, double S, double r, double T, double sigma, double P){
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
 
    public double retrieveCompundCoefficient(double r, double T, int N) {
        double coefficient = Math.pow(1+r*(T/N), (double) N);
        return coefficient;
    }
}
