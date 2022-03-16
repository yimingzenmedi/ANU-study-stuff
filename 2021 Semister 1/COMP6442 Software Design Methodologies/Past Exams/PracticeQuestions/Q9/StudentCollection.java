import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
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
The goal of this task is to write a program that loads/stores a list of students in XML format. `StudentCollection.java` class contains
a list of `Student` instances. Each student has his/her `age` and `name`, which need to be saved as attributes of XML node. Additionally,
each student can have three possible properties: `height`, `weight` and `courses`. `courses` property contains a list of `course` elements.
Each course has a course `name` attribute and a `grade` value. Note that not every student has all three properties. Some properties of students may be missing
(for example, see the test cases in StudentsTest.java). You job is to implement the below two methods in `StudentCollection.java`:

* `saveToFile`
* `loadFromFile`

Note that these methods should take into account the available properties of a given student. You are allowed to add additional asserts
and test cases to test your solutions. You can use `getAttribute(String name)` and `setAttribute(String name, String value)` of `Element`
class to get and set the attributes of XML node. **Please upload `StudentCollection.java` to wattle!**
 */
public class StudentCollection {

	private final List<Student> students;

	public StudentCollection(List<Student> students) {
		this.students = students;
	}

	public List<Student> getStudents() {
		return students;
	}

	/**
	 * Implement this method to save the list of students to XML file
	 * HINT: `setAttribute(String name, String value)` in `Element` can be used to set `name` and `age` properties
	 * @param file
	 */
	public void saveToFile(File file) {
		//START YOUR CODE

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
//		System.out.println("\nSaving: " + this.toString());
		try {
			builder = documentBuilderFactory.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element rootEle = doc.createElement("StudentCollection");
			for (Student student : students) {
				System.out.println("\nSaving: " + student.getName());

				Element studentEle = doc.createElement("student");
				studentEle.setAttribute("name", student.getName());
				studentEle.setAttribute("age", student.getAge().toString());
				Element heightEle = doc.createElement("height");
				if (student.getHeight() != null) {
					Text heightText = doc.createTextNode(String.valueOf(student.getHeight()));
					System.out.println("2");
					heightEle.appendChild(heightText);
				}

//				heightEle.setNodeValue(student.getHeight() == null ? null : student.getHeight().toString());
				studentEle.appendChild(heightEle);
				Element weightEle = doc.createElement("weight");
				studentEle.appendChild(heightEle);
				if (student.getWeight() != null) {
					Text weightText = doc.createTextNode(String.valueOf(student.getWeight()));
					System.out.println("4");
					weightEle.appendChild(weightText);
				}
//				weightEle.setNodeValue(student.getWeight() == null ? null : student.getWeight().toString());
				studentEle.appendChild(weightEle);
				System.out.println("Processing courses...");

				Element coursesEle = doc.createElement("courses");
				if (student.getCourses() == null) {
					coursesEle.setNodeValue(null);
				} else {
					for (Course course : student.getCourses()) {
						Element courseEle = doc.createElement("course");
						courseEle.setAttribute("name", course.getName());
						courseEle.setAttribute("grade", course.getGrade() == null ? null : course.getGrade().toString());
						coursesEle.appendChild(courseEle);
					}
				}
				studentEle.appendChild(coursesEle);

				rootEle.appendChild(studentEle);
			}
			doc.appendChild(rootEle);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			StreamResult output = new StreamResult(file);
			transformer.transform(new DOMSource(doc), output);
		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}

		//END YOUR CODE
	}

	/**
	 * Implement this method to load from the XML file to create a `StudentCollection`
	 * HINT: `getAttribute(String name)`in `Element` can be used to get `name` and `age` properties
	 * @param file
	 * @return
	 */
	public static StudentCollection loadFromFile(File file) {
		//START YOUR CODE

		ArrayList<Student> students = new ArrayList<>();

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			Document doc = documentBuilder.parse(file);
			NodeList list = doc.getElementsByTagName("student");
			for (int i = 0; i < list.getLength(); i++) {
				Element studentEle = (Element) list.item(i);
				String name = studentEle.getAttribute("name");
//				Integer age = Integer.valueOf(studentEle.getAttribute("age"));
				Integer age = studentEle.getAttribute("age") == null ? null : Integer.valueOf(studentEle.getAttribute("age"));
//				Integer weight = Integer.valueOf(studentEle.getElementsByTagName("weight").item(0).getNodeValue());
				System.out.println(studentEle.getElementsByTagName("height").item(0).getNodeValue());
				Integer height = studentEle.getElementsByTagName("height").item(0).getTextContent() == null || studentEle.getElementsByTagName("height").item(0).getTextContent().isEmpty() ? null : Integer.valueOf(studentEle.getElementsByTagName("height").item(0).getTextContent());
				Integer weight = studentEle.getElementsByTagName("weight").item(0).getTextContent() == null || studentEle.getElementsByTagName("weight").item(0).getTextContent().isEmpty() ? null : Integer.valueOf(studentEle.getElementsByTagName("weight").item(0).getTextContent());

				Student student = new Student();
				student.withName(name).withAge(age).withHeight(height).withWeight(weight);
				if (studentEle.getElementsByTagName("course") != null && studentEle.getElementsByTagName("course").getLength() > 0) {
					for (int j = 0; j < studentEle.getElementsByTagName("course").getLength(); j++) {
						Element courseEle = (Element) studentEle.getElementsByTagName("course").item(j);
						String courseName = courseEle.getAttribute("name");
						Integer grade = courseEle.getAttribute("grade") == null ? null : Integer.valueOf(courseEle.getAttribute("grade"));
						Course course = new Course(courseName, grade);
						student.addCourse(course);
					}
				}
				students.add(student);
			}
			return new StudentCollection(students);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return null;
		//END YOUR CODE
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o instanceof StudentCollection) {
			StudentCollection studentCollection = (StudentCollection) o;
			return this.students.equals(studentCollection.students);
		}

		return false;
	}

	public String toString() {
		StringBuilder s = new StringBuilder("\n===============\nCollection: ");
		for (Student student : students) {
			s.append(student == null ? "null" : student.toString());
		}
		return s.toString();
	}
}
