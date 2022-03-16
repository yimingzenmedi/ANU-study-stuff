import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * IMPORTANT: This class is incomplete. Please look for "TODO" comments.
 * 
 * Save() method is already given. Please implement load() method to load XML
 * files and insert() method to insert a new customer to XML files.
 * 
 * Each customer must have an ID value, but may not have all the following column values:
 * Name, Address, City, Postcode, Country. Please see test cases in XMLTableTest.java.
 * 
 */
public class XMLTable {

	/**
	 * Save all records to the XML file
	 */
	public void save(String tableName, List<Customer> customers) {

		File f = new File(FileUtil.getTableFileName(tableName));

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// make the xml tree
			// use factory to get the instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			// Root element <customers>
			Element rootElem = doc.createElement(Customer.KEY_ROOT);
			doc.appendChild(rootElem);

			for (Customer customer : customers) {
				Element customerElem = doc.createElement(Customer.KEY_ELEMENT);
				rootElem.appendChild(customerElem);

				// child element <ID> under <customer>
				Element cid = doc.createElement(Customer.KEY_ID);
				cid.appendChild(doc.createTextNode(Integer.toString(customer.getId())));
				customerElem.appendChild(cid);

				if (customer.getName() != null) {
					Element name = doc.createElement(Customer.KEY_NAME);
					name.appendChild(doc.createTextNode(customer.getName()));
					customerElem.appendChild(name);
				}

				if (customer.getAddress() != null) {
					Element address = doc.createElement(Customer.KEY_ADDRESS);
					address.appendChild(doc.createTextNode(customer.getAddress()));
					customerElem.appendChild(address);
				}

				if (customer.getCity() != null) {
					Element city = doc.createElement(Customer.KEY_CITY);
					city.appendChild(doc.createTextNode(customer.getCity()));
					customerElem.appendChild(city);
				}

				if (customer.getPostCode() != null) {
					Element postCode = doc.createElement(Customer.KEY_POSTCODE);
					postCode.appendChild(doc.createTextNode(customer.getPostCode()));
					customerElem.appendChild(postCode);
				}

				if (customer.getCountry() != null) {
					Element country = doc.createElement(Customer.KEY_COUNTRY);
					country.appendChild(doc.createTextNode(customer.getCountry()));
					customerElem.appendChild(country);
				}
			}
			// save the xml file
			// Transformer is for process XML from a variety of sources and write the
			// transformation
			// output to a variety of sinks
			Transformer transformer = TransformerFactory.newInstance().newTransformer();

			// set xml encoding to utf-8
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			// pretty print
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(f);
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get all records from the XML file
	 */
	public List<Customer> load(String tableName) {

		List<Customer> customers = new ArrayList<>();

		File f = new File(FileUtil.getTableFileName(tableName));
		if (!f.exists()) {
			return customers;
		}

		// TODO: Complete this method
		// START YOUR CODE

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			Document doc = documentBuilder.parse(f);
			Element root = (Element) doc.getElementsByTagName(Customer.KEY_ROOT).item(0);
			NodeList list = root.getElementsByTagName(Customer.KEY_ELEMENT);

			System.out.println("list: " + list.getLength() + "\n");

//
			for (int i = 0; i < list.getLength(); i++) {
				Element customerEle = (Element) list.item(i);

				Element idEle = (Element) customerEle.getElementsByTagName(Customer.KEY_ID).item(0);
				int id = Integer.parseInt(idEle.getFirstChild().getNodeValue());
				System.out.println("id: " + id);

				Element nameEle = (Element) customerEle.getElementsByTagName(Customer.KEY_NAME).item(0);
				String name = nameEle == null ? null : nameEle.getFirstChild().getNodeValue();
				System.out.println("name: " + name);

				Element addressEle = (Element) customerEle.getElementsByTagName(Customer.KEY_ADDRESS).item(0);
				String address = addressEle == null ? null : addressEle.getFirstChild().getNodeValue();
				System.out.println("address: " + address);

				Element cityEle = (Element) customerEle.getElementsByTagName(Customer.KEY_CITY).item(0);
				String city = cityEle == null ? null : cityEle.getFirstChild().getNodeValue();
				System.out.println("city: " + city);

				Element postCodeEle = (Element) customerEle.getElementsByTagName(Customer.KEY_POSTCODE).item(0);
				String postCode = postCodeEle == null ? null : postCodeEle.getFirstChild().getNodeValue();
				System.out.println("postCode: " + postCode);

				Element countryEle = (Element) customerEle.getElementsByTagName(Customer.KEY_COUNTRY).item(0);
				String country = countryEle == null ? null : countryEle.getFirstChild().getNodeValue();
				System.out.println("country: " + country);

				Customer customer = new Customer(id, name, address, city, postCode, country);
				System.out.println(customer.toString());
				System.out.println("---");
				customers.add(customer);
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		// END YOUR CODE
		System.out.println("===========");

		return customers;
	}

	/**
	 * Insert a new customer to XML files. 
	 * 
	 *
	 */
	public void insert(String tableName, Customer customer) {
		
		// TODO: Complete this method
		// START YOU CODE
		// HINT: insert the given customer to the XML file.
		// You can call the load() and save() methods
		
		List<Customer> customers = load(tableName);
		customers.add(customer);
		save(tableName, customers);
		
		// END YOUR CODE
	}
}
