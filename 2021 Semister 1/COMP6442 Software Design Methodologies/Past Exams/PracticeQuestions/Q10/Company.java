import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 *
The goal of this task is to write a program that loads/stores a college in JSON format. The `Company.java` has a company name and a
list of `Employee` instances. Each employee has an employee name and a list of skills. There are some duplicates of the employees in
the `company.json` file. Your job is to implement the three methods in the `Company` class:

* `loadFromJsonFile`
* `mergeEmployees`
* `saveToJsonFile`

in order to load from the given `company.json` file, merge the skills of the employees with the same names, and write the processed
data into a JSON file, which should be the same as the given `company_processed.json` file. A `CompanyTest.java` is given to help you
test your solutions. You are allowed to add additional asserts and test cases to test your solutions. **Please upload `Company.java`
file to wattle!**
 */
public class Company {

	private String name;
	private List<Employee> employees;

	/**
	 * Implement this method to load json data from file to create a company
	 *
	 * @param file
	 * @return
	 */
	public static Company loadFromJsonFile(File file) {

		// START YOUR CODE

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuilder text = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				text.append(line);
			}
			reader.close();
			if (!text.toString().isEmpty()) {
				Gson gson = new GsonBuilder().create();
				return gson.fromJson(text.toString(), Company.class);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("File io exception!");
			e.printStackTrace();
		}

		// END YOUR CODE

		return null;
	}

	/**
	 * Implement this method to merge the skills of the employees with the same
	 * names. Please refer to the given `company_processed.json`, which presents the
	 * data after merging.
	 */
	public void mergeEmployees() {

		// START YOUR CODE
		boolean flag = false;
		for (int i = 0; i < employees.size(); i ++) {
			Employee currentEmployee = employees.get(i);
			System.out.println("\n" + i + "th i current: " + currentEmployee.getName());

			for (int j = 0; j < employees.size(); j ++) {
				Employee savedEmployee = employees.get(j);
				System.out.println(j + "th j current: " + savedEmployee.getName());
				if (i != j && currentEmployee.getName().equals(savedEmployee.getName())) {
					System.out.println("Find: " + currentEmployee.getName());
					List<String> currentSkills = currentEmployee.getSkills();
					for (String skill : savedEmployee.getSkills()) {
						if (!currentSkills.contains(skill)) {
							currentSkills.add(skill);
							currentEmployee.setSkills(currentSkills);
						}
					}
					employees.remove(savedEmployee);
					flag = true;
					break;
				}
			}
		}
		if (flag) {
			mergeEmployees();
		}

//		for (Employee employee : this.employees) {
//			List<String> skills = employee.getSkills();
//			System.out.println("\nName: " + employee.getName());
//			for (int i = 0; i < skills.size(); i++) {
//				String skill = skills.get(i);
//				for (int j = 0; j < skills.size(); j++) {
//					if (i != j) {
//						String savedSkill = skills.get(j);
//						if (savedSkill.equals(skill)) {
//							skills.remove(j);
//							System.out.println("Removed: " + skill);
//							break;
//						}
//					}
//				}
//			}
//			employee.setSkills(skills);
//		}

		// END YOUR CODE
	}

	/**
	 * Implement this method to save this company object into a JSON file
	 *
	 * @param file
	 */
	public void saveToJsonFile(File file) {

		// START YOUR CODE

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        Gson gson = new Gson();
		this.mergeEmployees();
		String jsonStr = gson.toJson(this);
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

		// END YOUR CODE
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o instanceof Company) {
			Company company = (Company) o;

			return this.name.equals(company.name) && this.employees.equals(company.employees);
		}

		return false;
	}
}
