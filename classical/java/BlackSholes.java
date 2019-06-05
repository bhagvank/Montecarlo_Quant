import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class BlackSholes {

    public BlackSholes()
    {

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

    public static void main(String[] args) throws Exception 
    {
        double s     = 0.0;
        double x     = 0.0;
        double r     = 0.0;
        double sigma = 0.0;
        double t     = 0.0;

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
                if(node.getNodeName().equals("BlackSholes"))
                {
                    Element elem = (Element) node;
                     
                    s = Double.parseDouble(elem.getElementsByTagName("s").item(0).getChildNodes().item(0).getNodeValue());
                     
                    x = Double.parseDouble(elem.getElementsByTagName("x").item(0).getChildNodes().item(0).getNodeValue());
                     
                    r = Double.parseDouble(elem.getElementsByTagName("r").item(0).getChildNodes().item(0).getNodeValue());
                     
                    sigma = Double.parseDouble(elem.getElementsByTagName("sigma").item(0).getChildNodes().item(0).getNodeValue());
                     
                    t = Double.parseDouble(elem.getElementsByTagName("t").item(0).getChildNodes().item(0).getNodeValue());                     
                }
            }
        }

        BlackSholes bs = new BlackSholes();
        System.out.println(bs.callPrice(s, x, r, sigma, t));
        System.out.println(bs.call(s, x, r, sigma, t));
        System.out.println(bs.call2(s, x, r, sigma, t));
    }
}
