import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class AsianOption
{

    public AsianOption()
    {

    } 

    public static void main(String[] args) throws Exception
    {
        double T = 0.0; 
        double r = 0.0;
        double vol = 0.0;
        double dt = 0.0; 
        double S0 = 0.0; 
        double K = 0.0; 
        int sims = 0;
           
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
                if(node.getNodeName().equals("AsianOption"))
                {
                    Element elem = (Element) node;
                     
                    T = Double.parseDouble(elem.getElementsByTagName("T").item(0).getChildNodes().item(0).getNodeValue());
                     
                    r = Double.parseDouble(elem.getElementsByTagName("r").item(0).getChildNodes().item(0).getNodeValue());
                     
                    vol = Double.parseDouble(elem.getElementsByTagName("vol").item(0).getChildNodes().item(0).getNodeValue());
                     
                    dt = Double.parseDouble(elem.getElementsByTagName("dt").item(0).getChildNodes().item(0).getNodeValue());
                     
                    S0 = Double.parseDouble(elem.getElementsByTagName("S0").item(0).getChildNodes().item(0).getNodeValue());

                    K = Double.parseDouble(elem.getElementsByTagName("K").item(0).getChildNodes().item(0).getNodeValue());

                    sims = Integer.parseInt(elem.getElementsByTagName("sims").item(0).getChildNodes().item(0).getNodeValue());                      
                }
            }
        }

        AsianOption ao = new AsianOption();
        ao.calculateAsianOptions(T,r,vol,dt,S0,K,sims); 
    }

    public double calculateAsianOptions(double T, double r, double vol, double dt, double S0, double K, int sims)
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
        System.out.println(result);
        return result;
    }
}