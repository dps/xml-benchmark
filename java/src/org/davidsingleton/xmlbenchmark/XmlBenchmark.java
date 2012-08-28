/**
 * 
 */
package org.davidsingleton.xmlbenchmark;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 * Main class for the Java XML parsing benchmark.
 * 
 * @author davidsingleton
 */
public class XmlBenchmark {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: XmlBenchmark [inputFile] [outputFile]");
			return;
		}
		File inputFile = new File(args[0]);
    ArticleHandler articleHandler = new ArticleHandler(args[1]);

		try {
	    SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(inputFile, articleHandler);
    } catch (ParserConfigurationException e) {
	    e.printStackTrace();
    } catch (SAXException e) {
	    e.printStackTrace();
    } catch (IOException e) {
	    e.printStackTrace();
    }
		
		articleHandler.close();

	}

}
