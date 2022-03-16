
/**
 * The given code is provided to assist you to complete the required tasks. But the
 * given code is often incomplete. You have to read and understand the given code
 * carefully, before you can apply the code properly. You might need to implement
 * additional procedures, such as error checking and handling, in order to apply the
 * code properly.
 */

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LoadExpData {

	/**
	 * Helper method
	 *
	 * @param node
	 * @param childrenExps
	 * @return
	 */
	private static Exp generateExp(Node node, List<Exp> childrenExps) {

		if (node == null || childrenExps == null) {
			return null;
		}

		if (Arrays.asList("AddExp", "SubExp", "MulExp", "DivExp", "ExpoExp").contains(node.getNodeName())
				&& childrenExps.size() < 2) {
			return null;
		}

		Exp exp = null;

		switch (node.getNodeName()) {
		case "AddExp":
			exp = new AddExp(childrenExps.get(0), childrenExps.get(1));
			break;
		case "SubExp":
			exp = new SubExp(childrenExps.get(0), childrenExps.get(1));
			break;
		case "MulExp":
			exp = new MulExp(childrenExps.get(0), childrenExps.get(1));
			break;
		case "DivExp":
			exp = new DivExp(childrenExps.get(0), childrenExps.get(1));
			break;
		case "ExpoExp":
			exp = new ExpoExp(childrenExps.get(0), childrenExps.get(1));
			break;
		}

		return exp;
	}

	/**
	 * This is an example to show how to load one-level parse tree. You are required
	 * to implement your own loadFromFile() method.
	 *
//	 * @param doc
	 * @param rootType
	 * @return
	 */
	private static Exp oneLevelLoading(Node root, ExpType rootType) {

		if (root == null) {
			return null;
		}

		List<Exp> childrenExps = new LinkedList<>();

		NodeList childrenList = root.getChildNodes();
		for (int i = 0; i < childrenList.getLength(); i++) {
			Node child = childrenList.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals(ExpType.IntExp.name())) {
					childrenExps.add(new IntExp(Integer.valueOf(child.getTextContent())));
				}
			}
		}

		return generateExp(root, childrenExps);
	}

	/**
	 * This is an example to show how to load one-level parse tree. You are required
	 * to implement your own loadFromFile() method.
	 *
	 * @param file
	 * @param rootType
	 * @return
	 */
	public static Exp load1LevelExpFromFile(File file, ExpType rootType) {

		Exp exp = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();

			Document doc = db.parse(file);

			doc.getDocumentElement().normalize();

			NodeList nl = doc.getElementsByTagName(rootType.name());

			exp = oneLevelLoading(nl.item(0), rootType);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return exp;
	}

	/**
	 * Implement this method to load a multi-level parse tree from the XML file.
	 *
	 * @param file
	 * @param rootType
	 * @return
	 */
	public static Exp loadFromFile(File file, ExpType rootType) {

		Exp exp = null;

		// ########## YOUR CODE STARTS HERE ##########

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();

			Document doc = db.parse(file);

			doc.getDocumentElement().normalize();

			NodeList nl = doc.getElementsByTagName(rootType.name());

			exp = multiLevelLoading(nl.item(0), rootType);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// ########## YOUR CODE END HERE ##########

		return exp;
	}

	private static Exp multiLevelLoading(Node root, ExpType rootType) {

		if (root == null) {
			return null;
		}

		List<Exp> childrenExps = new LinkedList<>();

		if (root.getNodeName().equals(ExpType.IntExp.name())) {
			System.out.println("root: IntExp " + root.getTextContent());
			childrenExps.add(new IntExp(Integer.valueOf(root.getTextContent())));
			return generateExp(root, childrenExps);
		}

		NodeList childrenList = root.getChildNodes();
		for (int i = 0; i < childrenList.getLength(); i++) {
			Node child = childrenList.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equals(ExpType.IntExp.name())) {
					childrenExps.add(new IntExp(Integer.valueOf(child.getTextContent())));
				} else {
					Node child1 = child.getChildNodes().item(0);
					Node child2 = child.getChildNodes().item(1);
					ExpType expType1;
					ExpType expType2;
					if (child1.getTextContent() != null) {
						expType1 = ExpType.IntExp;
					} else {
						expType1 = ExpType.valueOf(child1.getNodeName());
					}
					if (child2.getTextContent() != null) {
						expType2 = ExpType.IntExp;
					} else {
						expType2 = ExpType.valueOf(child1.getNodeName());
					}
//					expType2 = ExpType.valueOf(child2.getNodeName());
					Exp exp;
					switch (rootType) {
						case IntExp:
							exp = new IntExp(Integer.valueOf(child1.getTextContent()));
							childrenExps.add(exp);
							exp = new IntExp(Integer.valueOf(child2.getTextContent()));
							childrenExps.add(exp);
							return generateExp(root, childrenExps);
						case MulExp:
							exp = new MulExp(multiLevelLoading(child1, expType1), multiLevelLoading(child2, expType2));
							childrenExps.add(exp);
							break;
						case DivExp:
							exp = new DivExp(multiLevelLoading(child1, expType1), multiLevelLoading(child2, expType2));
							childrenExps.add(exp);
							break;
						case AddExp:
							exp = new AddExp(multiLevelLoading(child1, expType1), multiLevelLoading(child2, expType2));
							childrenExps.add(exp);
							break;
						case SubExp:
							exp = new SubExp(multiLevelLoading(child1, expType1), multiLevelLoading(child2, expType2));
							childrenExps.add(exp);
							break;
						case ExpoExp:
							exp = new ExpoExp(multiLevelLoading(child1, expType1), multiLevelLoading(child2, expType2));
							childrenExps.add(exp);
							break;
						default:
							System.out.println("!!!!");
					}
				}
			}
		}

		return generateExp(root, childrenExps);
	}
}
