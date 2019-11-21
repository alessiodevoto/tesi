import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.*;
import static com.sun.org.apache.xerces.internal.impl.Constants.JDK_ENTITY_EXPANSION_LIMIT;



public class MongoConverter {
    public static int MAIN_NODES = 8;

    private static String xml_file_path = "/Users/alessiodevoto/tesi/dblp.xml";
    private static String json_file_path = "/Users/alessiodevoto/tesi/json_files/";

    private static String[] main_nodes = {"article", "proceedings", "inproceedings", "book", "incollection", "phdthesis", "mastersthesis", "www"};
    private static File[] output_files = new File[8];
    private static PrintWriter[] printwriters = new PrintWriter[8];

    public static void main(String[] args) throws ParserConfigurationException, SAXException {


        File xml_file = new File(xml_file_path);
        
        InputStream inputStream= null;
        try {
            inputStream = new FileInputStream(xml_file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        Reader reader = null;
        try {
            reader = new InputStreamReader(inputStream,"ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        InputSource is = new InputSource(reader);
        is.setEncoding("ISO-8859-1");
        System.out.println("[MongoConverter]Input file open");

        for(int i=0; i<MAIN_NODES;i++){
            output_files[i] = new File(json_file_path+main_nodes[i]+".json");
            try {
                printwriters[i] = new PrintWriter(new OutputStreamWriter(new FileOutputStream(output_files[i])));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[MongoConverter]Output files open");

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.setProperty(JDK_ENTITY_EXPANSION_LIMIT, 0);
        System.out.println("[MongoConverter]Sax parser ready");

        HandlerDBLP handler = new HandlerDBLP(printwriters);
        System.out.println("[MongoConverter]File handler ready");

        System.out.println("[MongoConverter]Starting parsing process...");
        try {
            parser.parse(is, handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[MongoConverter]Parsing process terminated");

        for(int i=0; i<MAIN_NODES;i++){
            printwriters[i].close();
        }
        System.out.println("[MongoConverter]Printwriters closed");
    }
}
