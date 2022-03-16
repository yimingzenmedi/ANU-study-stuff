import java.util.Arrays;

/**
 * IMPORTANT: This class is incomplete. Please look for "TODO" comments.
 * 
 * Implement a parser to extract the columns and values from tokens and execute
 * the SQL command to insert new customers. Do not insert customers if the
 * following errors are found: 
 * 1 - some brackets are missing 
 * 2 - some column names are wrong
 * 
 * Please see the columns names in Customer.java file. Please see test cases in
 * ParserTest.java
 */
public class Parser {

	Tokeniser tokeniser;
	XMLTable table;

	public Parser(Tokeniser tokeniser, XMLTable table) {
		this.tokeniser = tokeniser;
		this.table = table;
	}

	/**
	 * Extract the columns and values from tokens and execute the SQL command to insert new customers
	 */
	public void parseExp() {

		// TODO: Complete this method
		// START YOUR CODE
		
		Token t1 = tokeniser.takeNext();
		Token t2 = tokeniser.takeNext();

		if (t1 == null || t2 == null) {
			return;
		}

		System.out.println("Cols: " + t1.value);

		// check brackets:
		int b1 = 0, b2 = 0;
		for (int i = 0; i < t1.value.length(); i++) {
			char c = t1.value.toCharArray()[i];
			if (c == '(') {
				b1 += 1;
			}
			if (c == ')') {
				b2 += 1;
			}
		}
		if (b1 == 0 || b2 == 0) {
			return;
		}

		// check columns:
		int flagL = t1.value.indexOf("(");
		String colStr = t1.value.substring(flagL + 1);
		int flagR = colStr.indexOf(")");
		colStr = colStr.substring(0, flagR);
		String[] cols = colStr.split(",");
		System.out.println("split cols: " + Arrays.toString(cols));

		if (
				!cols[0].equals(Customer.KEY_ID) ||
						!cols[1].strip().equals(Customer.KEY_NAME) ||
						!cols[2].strip().equals(Customer.KEY_ADDRESS) ||
						!cols[3].strip().equals(Customer.KEY_CITY) ||
						!cols[4].strip().equals(Customer.KEY_POSTCODE) ||
						!cols[5].strip().equals(Customer.KEY_COUNTRY)

		) {
			return;
		}

		// check t2:
		b1 = 0;
		b2 = 0;
		for (int i = 0; i < t2.value.length(); i++) {
			char c = t2.value.toCharArray()[i];
			if (c == '(') {
				b1 += 1;
			}
			if (c == ')') {
				b2 += 1;
			}
		}
		if (b1 == 0 || b2 == 0) {
			return;
		}

		// get values from t2 and insert:
		flagL = t2.value.indexOf("(");
		String valStr = t2.value.substring(flagL + 1);
		flagR = valStr.indexOf(")");
		valStr = valStr.substring(0, flagR);
		valStr = valStr.replace("'", "");
		String[] values = valStr.split(",");
		System.out.println("split values: " + Arrays.toString(values));

		int id = Integer.parseInt(values[0].strip());
		String name = values[1].strip();
		String address = values[2].strip();
		String city = values[3].strip();
		String postCode = values[4].strip();
		String country = values[5].strip();

		Customer customer = new Customer(id, name, address, city, postCode, country);
		table.insert(Customer.TABLE_NAME, customer);

			// END YOUR CODE
	}
}
