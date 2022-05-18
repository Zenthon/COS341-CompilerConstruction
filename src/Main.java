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
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Please enter the the input file name with the extension (.txt): ");
            String inputFile = sc.nextLine();
            System.out.print("Please enter the output file name with the extension (.xml): ");
            String outputFile = sc.nextLine();

            // Start of the lexer
            Lexer lexer = new Lexer(Files.readString(Path.of(inputFile)));
            lexer.process();

            // Check if lexer has errors. If not, get the tokens
            if (!lexer.isHasErrors()) {
                Parser parser = new Parser(lexer.tokens);
                parser.parseGrammar();
                treeToXML(parser.getXmlString(), outputFile);
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

    public static void treeToXML(String tree, String fileName) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(tree)));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);

        StreamResult result = new StreamResult(new File(fileName));
        transformer.transform(source, result);
    }
}
