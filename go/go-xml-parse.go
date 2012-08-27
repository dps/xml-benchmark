package main

// An example streaming XML parser.

import (
	"bufio"
	"fmt"
	"os"
	"flag"
	"encoding/xml"
)

var inputFile = flag.String("infile", "enwiki-latest-pages-articles.xml", "Input file path")
var outFile = flag.String("outfile", "out/article_list.txt", "article list output file")

// Here is an example article from the Wikipedia XML dump
//
// <page>
// 	<title>Apollo 11</title>
//      <redirect title="Foo bar" />
// 	...
// 	<revision>
// 	...
// 	  <text xml:space="preserve">
// 	  {{Infobox Space mission
// 	  |mission_name=&lt;!--See above--&gt;
// 	  |insignia=Apollo_11_insignia.png
// 	...
// 	  </text>
// 	</revision>
// </page>
//
// Note how the tags on the fields of Page and Redirect below
// describe the XML schema structure.

type Redirect struct {
	Title string `xml:"title,attr"`
}

type Page struct {
	Title string `xml:"title"`
	Redir Redirect `xml:"redirect"`
	Text string `xml:"revision>text"`
}

func main() {
	flag.Parse()

	xmlFile, err := os.Open(*inputFile)
	if err != nil {
		fmt.Println("Error opening file:", err)
		return
	}
	defer xmlFile.Close()
	outFile, err := os.Create(*outFile);
	if err != nil {
		fmt.Println("Error opening file:", err)
		return
	}
	defer outFile.Close()
	writer := bufio.NewWriter(outFile)

	decoder := xml.NewDecoder(xmlFile)
	total := 0
	var inElement string
	for {
		// Read tokens from the XML document in a stream.
		t, _ := decoder.Token()
		if t == nil {
			break
		}
		// Inspect the type of the token just read.
		switch se := t.(type) {
		case xml.StartElement:
			// If we just read a StartElement token
			inElement = se.Name.Local
			// ...and its name is "page"
			if inElement == "page" {
				var p Page
				// decode a whole chunk of following XML into the
				// variable p which is a Page (se above)
				decoder.DecodeElement(&p, &se)
				writer.WriteString(p.Title + "\n")
				total++
			}
		default:
		}
		
	}

	fmt.Printf("Total articles: %d \n", total)
}
