import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.PrintWriter;


public class HandlerDBLP extends DefaultHandler {

    private PrintWriter[] writers;
    
    private String current_element = "no_element_found_yet";
    private JSONObject current_object = new JSONObject();
    private StringBuilder current_value = new StringBuilder();


    public HandlerDBLP(PrintWriter[] writers) {
        super();
        this.writers = writers;
    }


    public void startDocument() {
        System.out.println("[HandlerDBLP]Starting to read document");
    }

    public void endDocument() {
        System.out.println("[HandlerDBLP]Parsing terminated");
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
        if(isMainNode(qName)) {
            current_object = new JSONObject();

            for(int i=0; i<attributes.getLength();i++){
                current_object.accumulate(attributes.getLocalName(i), attributes.getValue(i));
            }
        }
        else {
            current_element = qName;
        }
    }


    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(isMainNode(qName)){
            if(qName.equalsIgnoreCase("article")) {writers[0].println(current_object);}
            else if( qName.equalsIgnoreCase("proceedings")) {writers[1].println(current_object);}
            else if (qName.equalsIgnoreCase("inproceedings")){writers[2].println(current_object);}
            else if (qName.equalsIgnoreCase("book")) {writers[3].println(current_object);}
            else if (qName.equalsIgnoreCase("incollection")) {writers[4].println(current_object);}
            else if (qName.equalsIgnoreCase("phdthesis")) {writers[5].println(current_object);}
            else if (qName.equalsIgnoreCase("mastersthesis")) {writers[6].println(current_object);}
            else if (qName.equalsIgnoreCase("www")) {writers[7].println(current_object);}
            }
        else {
            current_object.accumulate(current_element, current_value);
            current_value = new StringBuilder();
        }
    }

    public void characters(char[] ch,int start,int length) {
        String value = null;
        value = new String(ch, start, length);
        current_value.append(value);
    }

    public void ignorableWhitespace(char[] ch,
                                    int start,
                                    int length)throws SAXException{}

    private boolean isMainNode(String qName){
        if(qName.equalsIgnoreCase("article")
                || qName.equalsIgnoreCase("proceedings")
                || qName.equalsIgnoreCase("inproceedings")
                || qName.equalsIgnoreCase("book")
                || qName.equalsIgnoreCase("incollection")
                || qName.equalsIgnoreCase("phdthesis")
                || qName.equalsIgnoreCase("mastersthesis")
                || qName.equalsIgnoreCase("www")
                || qName.equalsIgnoreCase("dblp")){ return true;}
        else return false;
    }
}
