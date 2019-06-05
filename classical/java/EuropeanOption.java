import java.text.DecimalFormat;
import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
 
public class EuropeanOption {

    public EuropeanOption()
    {

    }
 
    public static void main(String[] args) throws Exception
    {
        double stock_price = 0.0; 
        int strike_price = 0; 
        double interest_rate = 0.0; 
        int numberOfSteps = 0; 
        double timeTillExpiration = 0.0; 
        int numberOfComputations = 0; 
        double volatility = 0.0; 
        double totalPayoff = 0.0;
        double averagePayoff = 0.0;
        double compundRate = 0.0;
        double O;

        File fXmlFile = new File("input.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(fXmlFile);

        document.getDocumentElement().normalize();

        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) 
        {         
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                if(node.getNodeName().equals("EuropeanOption"))
                {
                    Element elem = (Element) node;
                     
                    stock_price = Double.parseDouble(elem.getElementsByTagName("stock_price").item(0).getChildNodes().item(0).getNodeValue());
                     
                    strike_price = Integer.parseInt(elem.getElementsByTagName("strike_price").item(0).getChildNodes().item(0).getNodeValue());
                     
                    interest_rate = Double.parseDouble(elem.getElementsByTagName("interest_rate").item(0).getChildNodes().item(0).getNodeValue());
                     
                    numberOfSteps = Integer.parseInt(elem.getElementsByTagName("numberOfSteps").item(0).getChildNodes().item(0).getNodeValue());
                     
                    timeTillExpiration = Double.parseDouble(elem.getElementsByTagName("timeTillExpiration").item(0).getChildNodes().item(0).getNodeValue());

                    numberOfComputations = Integer.parseInt(elem.getElementsByTagName("numberOfComputations").item(0).getChildNodes().item(0).getNodeValue());

                    volatility = Double.parseDouble(elem.getElementsByTagName("volatility").item(0).getChildNodes().item(0).getNodeValue());

                    totalPayoff = Double.parseDouble(elem.getElementsByTagName("totalPayoff").item(0).getChildNodes().item(0).getNodeValue());

                    averagePayoff = Double.parseDouble(elem.getElementsByTagName("averagePayoff").item(0).getChildNodes().item(0).getNodeValue());

                    compundRate = Double.parseDouble(elem.getElementsByTagName("compundRate").item(0).getChildNodes().item(0).getNodeValue());                      
                }
            }
        }

        EuropeanOption eo = new EuropeanOption();
        eo.calculateEuropeanOption(stock_price,strike_price,interest_rate,numberOfSteps,timeTillExpiration,numberOfComputations,volatility,totalPayoff,averagePayoff,compundRate);
    }

    public double calculateEuropeanOption(double stock_price, int strike_price, double interest_rate, int numberOfSteps, double timeTillExpiration,int numberOfComputations,  double volatility, double totalPayoff, double averagePayoff, double compundRate)
    {
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
        return O;
    }
 
    public double getPayoffs(int N, double S, double r, double T, double sigma, int P){
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