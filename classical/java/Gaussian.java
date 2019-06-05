import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

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

    public static void main(String[] args) throws Exception 
    {
        double z     = 0.0;
        double mu    = 0.0;
        double sigma = 0.0;

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
                if(node.getNodeName().equals("Gaussian"))
                {
                    Element elem = (Element) node;
                     
                    z = Double.parseDouble(elem.getElementsByTagName("z").item(0).getChildNodes().item(0).getNodeValue());
                     
                    mu = Double.parseDouble(elem.getElementsByTagName("mu").item(0).getChildNodes().item(0).getNodeValue());
                     
                    sigma = Double.parseDouble(elem.getElementsByTagName("sigma").item(0).getChildNodes().item(0).getNodeValue());                      
                }
            }
        }

        Gaussian g = new Gaussian();
        System.out.println(g.cdf(z, mu, sigma));
        double y = g.cdf(z);
        System.out.println(g.inverseCDF(y));
    }
}