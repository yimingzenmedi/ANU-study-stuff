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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 *
 The goal of this task it to write a program that loads/stores a list of trees in XML format. `TreeCollection.java` class contains a
 list of `Tree` instances. Each tree has its own `kind`, which needs to be saved as an attribute of XML node. Additionally, each tree
 can have three possible properties: `dimension`, `color` and `types`. `dimension` property has two integer attributes: `diameter` and
 `height`. `types` property has a list of `type` elements. Note that not every tree has all three properties. Some properties of trees
 may be missing (for example, see the test cases in TreesTest.java). You job is to implement the below methods in `TreeCollection.java`:

* `saveToFile`
* `loadFromFile`

Note that these methods should take into account the available properties of a given tree. You are allowed to add additional asserts
and test cases to test your solutions. You can use `getAttribute(String name)` and `setAttribute(String name, String value)` of `Element`
class to get and set the attributes of XML node. **Please upload `TreeCollection.java` file to wattle!**
 *
 */
public class TreeCollection {

	private final List<Tree> trees;

	public TreeCollection(List<Tree> trees) {
		this.trees = trees;
	}

	public List<Tree> getTrees() {
		return trees;
	}

	/**
	 * Implement this method to save the list of trees to XML file
	 * HINT: `setAttribute(String name, String value)` in `Element` can be used to set `kind`, `diameter` and `height` properties
	 * @param file
	 */
	public void saveToFile(File file) {
		//START YOUT CODE
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;

		try {
			builder = documentBuilderFactory.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element rootEle = doc.createElement("TreeCollection");
			for (Tree tree : this.trees) {
				Element treeEle = doc.createElement("tree");
				treeEle.setAttribute("kind", tree.getKind());
				if (tree.getColor() != null) {
					treeEle.setAttribute("color", tree.getColor());
				}

				if (tree.getDimension() != null) {
					Element dimensionEle = doc.createElement("dimension");
					Dimension dimension = tree.getDimension();
					dimensionEle.setAttribute("diameter", dimension.getDiameter() == null ? null : String.valueOf(dimension.getDiameter()));
					dimensionEle.setAttribute("height", dimension.getHeight() == null ? null : String.valueOf(dimension.getHeight()));
					treeEle.appendChild(dimensionEle);
				}

				if (tree.getTypes() != null) {
					List<String> types = tree.getTypes();
					Element typesEle = doc.createElement("types");
					for (String type : types) {
						Element typeEle = doc.createElement("type");
						typeEle.setAttribute("type", type);
						typesEle.appendChild(typeEle);
					}
					treeEle.appendChild(typesEle);
				}
				rootEle.appendChild(treeEle);
			}
			doc.appendChild(rootEle);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			StreamResult output = new StreamResult(file);
			transformer.transform(new DOMSource(doc), output);
		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}

		//END YOUT CODE
	}

	/**color
	 * Implement this method to load from the XML file to create a `TreeCollection`
	 * HINT: `getAttribute(String name)`in `Element` can be used to get `kind`, `diameter` and `height` properties
	 * @param file
	 * @return
	 */
	public static TreeCollection loadFromFile(File file) {
		//START YOUT CODE

		ArrayList<Tree> trees = new ArrayList<>();

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			Document doc = documentBuilder.parse(file);
			NodeList list = doc.getElementsByTagName("tree");
			for (int i = 0; i < list.getLength(); i++) {
				Tree tree = new Tree();

				Element treeEle = (Element) list.item(i);
				String kind = treeEle.getAttribute("kind");
				tree.withKind(kind);
				String color = treeEle.getAttribute("color").isEmpty() ? null : treeEle.getAttribute("color");
				System.out.println(">>> color: " + color);
				tree.withColor(color);

				Dimension dimension = null;
				if (treeEle.getElementsByTagName("dimension").getLength() > 0) {
					Element dimensionEle = (Element) treeEle.getElementsByTagName("dimension").item(0);
					Integer diameter =  dimensionEle.getAttribute("diameter") == null ? null : Integer.valueOf(dimensionEle.getAttribute("diameter"));
					Integer height =  dimensionEle.getAttribute("height") == null ? null : Integer.valueOf(dimensionEle.getAttribute("height"));
					dimension = new Dimension(diameter, height);
				}
				tree.withDimension(dimension);

				if (treeEle.getElementsByTagName("types").getLength() > 0) {
					Element typesEle = (Element) treeEle.getElementsByTagName("types").item(0);
					if (typesEle.getElementsByTagName("type").getLength() > 0) {
						for (int typeI = 0; typeI < typesEle.getElementsByTagName("type").getLength(); typeI ++) {
							Element typeEle = (Element) typesEle.getElementsByTagName("type").item(typeI);
							String type = typeEle.getAttribute("type");
							tree.addType(type);
						}
					}
				}

				trees.add(tree);
			}
			return new TreeCollection(trees);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		return null;

		//END YOUT CODE
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o instanceof TreeCollection) {
			TreeCollection treeCollection = (TreeCollection) o;
			return this.trees.equals(treeCollection.trees);
		}

		return false;
	}

	public String toString() {
		String s = "-----------------------\ntree collection: \n";
		for (Tree tree : trees) {
			s += trees.toString();
		}
		return s;
	}
}
