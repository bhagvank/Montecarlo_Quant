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
 * Servlet implementation class AsianOption
 */
@WebServlet("/AsianOption")
public class AsianOption extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AsianOption ao = new AsianOption();
		double T = Double.parseDouble(request.getParameter("T")); 
        double r = Double.parseDouble(request.getParameter("r"));
        double vol = Double.parseDouble(request.getParameter("vol"));
        double dt = Double.parseDouble(request.getParameter("dt")); 
        double S0 = Double.parseDouble(request.getParameter("S0")); 
        double K = Double.parseDouble(request.getParameter("K")); 
        int sims = Integer.parseInt(request.getParameter("sims"));
        double result = ao.asianOption(T,r,vol,dt,S0,K,sims);
        PrintWriter out =  response.getWriter();
        out.println("Result is "+result);
	}
	
	public double asianOption(double T, double r, double vol, double dt, double S0, double K, double sims)
	{
		Random random = new Random();
        double a = (r - (0.5*Math.pow(vol, 2)))*dt;
        double b = vol*Math.sqrt(dt);
        double result = 0.0;
        for (int i = 0; i < sims; i++) 
        {
            double avgPrice = 0.0;
            int steps = 0;
            double lastPrice = S0;
            for (double t = dt; t <= T; t += dt) 
            {
                lastPrice = lastPrice*Math.exp(a + (b*random.nextGaussian()));
                avgPrice += lastPrice;
                steps++;
            }
            avgPrice = avgPrice/steps; 
            result += Math.max(0, avgPrice-K);
        }
        result = result/sims; 
        result = result*Math.exp(-r*T);
        return result;
	}

}
