import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.*;

public class Main {

    public static void main(String[] args) {
        try {
            // Start of the lexer
            Lexer lexer = new Lexer(Files.readString(Path.of("input.txt")));
            lexer.process();

            // Check if lexer has errors. If not, get the tokens
            if (!lexer.isHasErrors()) {
                Parser parser = new Parser(lexer.tokens);
                parser.parseGrammar();
                if (!parser.getHasErrors())
                    treeToXML(parser.getXmlString());
            }

        }
        catch (IOException e) {
            System.out.println("Failed to read file to string.");
        } catch (ParserConfigurationException | TransformerException | SAXException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            System.out.println(e.getMessage());;
        }
    }

    public static void treeToXML(String tree) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(tree)));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);

        StreamResult result = new StreamResult(new File("syntaxTree.xml"));
        transformer.transform(source, result);
    }
}
