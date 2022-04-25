// Java Program to Extract Content from a PDF

// Importing java input/output classes
import java.io.*;
import java.util.*;
// Importing Apache POI classes
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

// Class
public class pdfHandler {
	private static String fileString = "";
	private static ArrayList<String> courseDesc = new ArrayList<>();

	// Main driver method
	public static void main(String[] args)
	{
		// Reade in PDF File into a text file to be handled 
		createPDFText();
		parseFileIntoDB();

		System.out.println(courseDesc.size());
		for(int i = 0; i < courseDesc.size(); i++)
		{
			System.out.println(courseDesc.get(i));
			System.out.println("BREAK!\n\n");
		}
	}

	public static void parseFileIntoDB()
	{
		try
		{
			int state = 0;
			Scanner input = new Scanner(fileString);
			String read = " ";
			String add = "";

			while(input.hasNextLine())
			{
				switch(state)
				{
					case 0:
						read = input.nextLine();
						if(fits(read))
						{
							state = 1;
						}
						break;
					case 1:
						read = input.nextLine();
						boolean stateChange = false;

						for(int i = 0; i < read.length(); i++)
						{
							if(!Character.isWhitespace(read.charAt(i)))
							{
								stateChange = false;
								break;
							}
							stateChange = true;
						}

						if(stateChange == false)
						{
							add = add + read;
						}
						else
						{
							courseDesc.add(add);
							add = "";
						}
						break;

					default:
						break;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static boolean fits(String read)
	{
		if(read.equals(""))
		{
			return false;
		}
		return Character.isUpperCase(read.charAt(0)) && 
			Character.isUpperCase(read.charAt(1)) && 
			Character.isUpperCase(read.charAt(2)) && 
			Character.isUpperCase(read.charAt(3)) && 
			Character.isWhitespace(read.charAt(4)) && 
			Character.isDigit(read.charAt(5)) && 
			Character.isDigit(read.charAt(6)) && 
			Character.isDigit(read.charAt(7)) && 
			(read.charAt(8) == '.'); 
	}

	public static void createPDFText()
	{
		try 
		{
			// Create a content handler
			BodyContentHandler contenthandler
			= new BodyContentHandler(-1);

			// Create a file in local directory
			File f = new File("Bulletin.pdf");

			// Create a file input stream
			// on specified path with the created file
			FileInputStream fstream = new FileInputStream(f);

			// Create an object of type Metadata to use
			Metadata data = new Metadata();

			// Create a context parser for the pdf document
			ParseContext context = new ParseContext();

			// PDF document can be parsed using the PDFparser
			// class
			PDFParser pdfparser = new PDFParser();

			// Method parse invoked on PDFParser class
			pdfparser.parse(fstream, contenthandler, data, context);


			// Create file to store scraped PDF data
			/*File myObj = new File("pdfOutput.txt");
			if (myObj.createNewFile()) 
			{
				System.out.println("File created: " + myObj.getName());
			} 
			else 
			{
				System.out.println("File already exists.");
			}

			FileWriter myWriter = new FileWriter("pdfOutput.txt");

			// Storing the contents of the pdf document
			// using toString() method in java
      		myWriter.write(contenthandler.toString());
      		myWriter.close();*/

			fileString = contenthandler.toString();
      		System.out.println("Successfully wrote PDF contents.");
		}
		catch (Exception e) 
		{
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}
}
