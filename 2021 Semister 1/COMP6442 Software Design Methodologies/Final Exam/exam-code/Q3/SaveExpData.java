
/**
 * The given code is provided to assist you to complete the required tasks. But the
 * given code is often incomplete. You have to read and understand the given code
 * carefully, before you can apply the code properly. You might need to implement
 * additional procedures, such as error checking and handling, in order to apply the
 * code properly.
 */

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SaveExpData {

	private final Exp root;

	public SaveExpData(Exp root) {
		this.root = root;
	}

	public Exp getRoot() {
		return root;
	}

	/**
	 * This is an example to show how to save one-level parse tree. You are required
	 * to implement your own saveToFile() method.
	 *
	 * @param doc
	 * @param rootExp
	 * @return
	 */
	private Element oneLevelParsing(Document doc, Exp rootExp) {
		if (rootExp == null) {
			return null;
		}

		Element rootElem = doc.createElement(rootExp.getType().name());

		for (Exp child : rootExp.getSubExps()) {
			if (child.getType() == ExpType.IntExp) {
				IntExp intexp = (IntExp) child;
				Element childElem = doc.createElement(child.getType().name());
				childElem.setTextContent(intexp.getValue() + "");
				rootElem.appendChild(childElem);
			}
		}

		return rootElem;
	}

	/*
	 * This is an example to show how to save one-level parse tree. You are required
	 * to implement your own saveToFile() method.
	 *
	 * @param file
	 */
	public void save1LevelExpToFile(File file) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();

			doc.appendChild(oneLevelParsing(doc, this.root));

			// save the xml file
			Transformer transformer = TransformerFactory.newInstance().newTransformer();

			// set xml encoding to utf-8
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			// pretty print
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Implement this method to save a multi-level parse tree to an XML file
	 *
	 * @param file
	 */
	public void saveToFile(File file) {

		// ########## YOUR CODE STARTS HERE ##########

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = documentBuilderFactory.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element rootEle = doc.createElement("exp");
			Exp exp = this.root;
			rootEle.appendChild(multiLevelParsing(doc, exp));

			doc.appendChild(rootEle);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();

			// set xml encoding to utf-8
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			// pretty print
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
		} catch (ParserConfigurationException | TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		// ########## YOUR CODE ENDS HERE ##########
	}


	private Element multiLevelParsing(Document doc, Exp rootExp) {
		if (rootExp == null) {
			return null;
		}

		Element rootElem = doc.createElement(rootExp.getType().name());
//		System.out.println("\nsaving: " + rootElem.getTagName());
		if (rootExp.getType() == ExpType.IntExp) {
			IntExp intexp = (IntExp) rootExp;
//			System.out.println("root: IntExp " + intexp.getValue());
			rootElem.setTextContent(intexp.getValue() + "");
			return rootElem;
		}
		for (Exp child : rootExp.getSubExps()) {
			if (child.getType() == ExpType.IntExp) {
//				System.out.println("saving IntExp");
//				rootElem.appendChild(oneLevelParsing(doc, child));

				IntExp intexp = (IntExp) child;
//				System.out.println("child:: IntExp " + intexp.getValue());
				Element childElem = doc.createElement(child.getType().name());
				childElem.setTextContent(intexp.getValue() + "");
				rootElem.appendChild(childElem);
			} else {
//				System.out.println("saving others");

//				System.out.println("child:  " + child.getType());
				Element childElem = doc.createElement(child.getType().name());
				for (Exp grandChild : child.getSubExps()) {
//					System.out.println("sub child:  " + grandChild.getType());
					Element grandChildEle = multiLevelParsing(doc, grandChild);
					childElem.appendChild(grandChildEle);
				}
//				childElem = multiLevelParsing(doc, child);
				rootElem.appendChild(childElem);
			}
		}

		return rootElem;
	}

}
