/**
 * The given code is provided to assist you to complete the required tasks. But the
 * given code is often incomplete. You have to read and understand the given code
 * carefully, before you can apply the code properly. You might need to implement
 * additional procedures, such as error checking and handling, in order to apply the
 * code properly.
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// you need to import some xml libraries

// import any standard library if needed

/**
 * A book collection holds 0 or more books in a collection.
 */
public class BookCollection {
	private List<Book> books;

	/**
	 * Creates a new collection with no books by default.
	 */
	public BookCollection() {
		this.books = new ArrayList<Book>();
	}

	/**
	 * Creates a new book collection with the specified list of books pre-defined.
	 *
	 * @param books A books list.
	 */
	public BookCollection(List<Book> books) {
		this.books = books;
	}

	/**
	 * Returns the current list of books stored by this collection.
	 *
	 * @return A (mutable) list of books.
	 */
	public List<Book> getList() {
		return books;
	}

	/**
	 * Sets the list of books in this collection to the specified value.
	 */
	public void setList(List<Book> books) {
		this.books = books;
	}

	/**
	 * A simple human-readable toString implementation. Not particularly useful to
	 * save to disk.
	 *
	 * @return A human-readable string for printing
	 */
	@Override
	public String toString() {
		return this.books.stream().map(book -> " - " + book.display() + "\n").collect(Collectors.joining());
	}

	/**
	 * Saves this collection to the specified "bespoke" file.
	 *
	 * @param file The path to a file.
	 */
	public void saveToBespokeFile(File file) {
		// TODO: Implement this function yourself. The specific hierarchy is up to you,
		// but it must be in a bespoke format and should match the
		// load function.

		String divider = "//divider//";
		try {
//			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
//			stream.writeObject(this.books);
//			stream.close();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (Book book : this.books) {
				String text = String.format("%s%s%s%s%d%s%s", book.title, divider, book.authorName, divider, book.yearReleased, divider, book.bookGenre);
				writer.write(text);
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("File IO exception: " + e);
			e.printStackTrace();
		}


	}

	/**
	 * Saves this collection to the specified JSON file.
	 *
	 * @param file The path to a file.
	 */
	public void saveToJSONFile(File file) {
		// TODO: Implement this function yourself. The specific hierarchy is up to you,
		// but it must be in a JSON format and should match the load function.

		Gson gson = new Gson();
		String jsonStr = gson.toJson(this.books);
		System.out.println("saveToJSONFile content: " + jsonStr);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(jsonStr);
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("File IO exception: " + e);
			e.printStackTrace();
		}

	}

	/**
	 * Saves this collection to the specified XML file.
	 *
	 * @param file The path to a file.
	 */
	public void saveToXMLFile(File file) {
		// TODO: Implement this function yourself. The specific hierarchy is up to you,
		// but it must be in an XML format and should match the
		// load function.

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = documentBuilderFactory.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element rootEle = doc.createElement("BookCollection");
			for (Book book : this.books) {
				Element bookEle = doc.createElement("Book");
				bookEle.setAttribute("title", book.title);
				bookEle.setAttribute("authorName", book.authorName);
				bookEle.setAttribute("yearReleased", String.valueOf(book.yearReleased));
				bookEle.setAttribute("bookGenre", String.valueOf(book.bookGenre));

				rootEle.appendChild(bookEle);
			}

			doc.appendChild(rootEle);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			StreamResult output = new StreamResult(file);
			transformer.transform(new DOMSource(doc), output);

		} catch (ParserConfigurationException | TransformerConfigurationException e) {
			System.err.println("Configuration exception: " + e);
			e.printStackTrace();
		} catch (TransformerException e) {
			System.err.println("Transformer exception: " + e);
			e.printStackTrace();
		}


	}

	/**
	 * Load a pre-existing book collection from a "bespoke" file.
	 *
	 * @param file The file to load from. This is guaranteed to exist.
	 * @return An initialised book collection.
	 */
	public static BookCollection loadFromBespokeFile(File file) {
		// TODO: Implement this function yourself.

//		ArrayList books = new ArrayList<>();
		ArrayList<Book> books = new ArrayList<>();
		try {
//			ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file));
//			books = (ArrayList) reader.readObject();

			String divider = "//divider//";
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] info = line.split(divider);
				String title = info[0];
				String authorName = info[1];
				int yearReleased = Integer.parseInt(info[2]);
				BookGenre bookGenre = BookGenre.valueOf(info[3]);
				Book book = new Book(title, authorName, yearReleased, bookGenre);
				books.add(book);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("File io exception: " + e);
			e.printStackTrace();
		}
//		catch (ClassNotFoundException e) {
//			System.err.println("Class not found exception: " + e);
//		}

		return new BookCollection(books);

	}

	/**
	 * Load a pre-existing book collection from a JSON file.
	 *
	 * @param file The file to load from. This is guaranteed to exist.
	 * @return An initialised book collection.
	 */
	public static BookCollection loadFromJSONFile(File file) {
		// TODO: Implement this function yourself.

		ArrayList<Book> books = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuilder text = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				text.append(line);
			}
			reader.close();
			if (!text.toString().equals("")) {
				Gson gson = new Gson();
				books = gson.fromJson(text.toString(), new TypeToken<List<Book>>(){}.getType());
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("File io exception: " + e);
			e.printStackTrace();
		}

		return new BookCollection(books);


	}

	/**
	 * Load a pre-existing book collection from an XML file.
	 *
	 * @param file The file to load from. This is guaranteed to exist.
	 * @return An initialised book collection.
	 */
	public static BookCollection loadFromXMLFile(File file) {
		// TODO: Implement this function yourself.

		ArrayList<Book> books = new ArrayList<>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			Document doc = documentBuilder.parse(file);
			NodeList list = doc.getElementsByTagName("Book");

			for (int i = 0; i < list.getLength(); i++) {
				Element bookEle = (Element) list.item(i);
				String title = bookEle.getAttribute("title");
				String authorName = bookEle.getAttribute("authorName");
				int yearReleased = Integer.parseInt(bookEle.getAttribute("yearReleased"));
				BookGenre bookGenre = BookGenre.valueOf(bookEle.getAttribute("bookGenre"));
				books.add(new Book(title, authorName, yearReleased, bookGenre));
			}

		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("File io exception: " + e);
			e.printStackTrace();
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}

		return new BookCollection(books);



	}
}
