package org.davidsingleton.xmlbenchmark;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ArticleHandler extends DefaultHandler {

	String currentElement = null;
	String currentTitle = null;
	StringBuilder currentText = new StringBuilder();
	PrintStream outPrintStream;
	int total;

	public ArticleHandler(String outputFilePath) {
		try {
	    outPrintStream = new PrintStream(outputFilePath);
    } catch (FileNotFoundException e) {
	    e.printStackTrace();
    }
  }
	
	@Override
  public void characters(char[] characters, int start, int length) throws SAXException {
		super.characters(characters, start, length);
		if (currentElement.equals("title")) {
			String chrString = new String(characters, start, length);
			if (currentTitle == null) {
				currentTitle = chrString;
			} else {
				currentTitle = currentTitle + chrString;
			}
		}

		if (currentElement.equals("text")) {
			currentText.append(characters, start, length);
		}

  }

	@Override
  public void endDocument() throws SAXException {
	  super.endDocument();
  }

	@Override
  public void endElement(String uri, String localName, String qName)
      throws SAXException {
		super.endElement(uri, localName, qName);
		if (qName.equals("page")) {
			total++;
			outPrintStream.println(currentTitle);
			currentTitle = null;
		}
		if (qName.equals("text")) {
			// We do nothing with the article text, but the rules state we must parse it out of the
			// XML document.
			currentText = new StringBuilder();
		}
		currentElement = "";
  }

	@Override
  public void startDocument() throws SAXException {
	  super.startDocument();
  }

	@Override
  public void startElement(String uri, String localName, String qName,
      Attributes attributes) throws SAXException {
	  super.startElement(uri, localName, qName, attributes);
	  currentElement = qName;
  }

	public void close() {
		System.out.println("Total: " + total);
		outPrintStream.close();
  }

}
