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
            if (!lexer.isHasErrors())
                for (Token t : lexer.tokens)
                    System.out.println(t.tokenFormat());

            String s = "<SPLProg><A>aa</A></SPLProg>";

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(s)));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);

            StreamResult result = new StreamResult(new File("syntaxTree.xml"));
            transformer.transform(source, result);
        }
        catch (IOException e) {
            System.out.println("Failed to read file to string.");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
