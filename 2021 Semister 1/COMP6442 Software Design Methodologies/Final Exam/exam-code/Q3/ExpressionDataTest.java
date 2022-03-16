/**
 * These test cases are provided to assist your understanding.
 * But these test cases are not used in the actual marking.
 * Please DO NOT only rely on the given test cases for debugging.
 * You should write your own test cases to debug your code.
 */

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class ExpressionDataTest {

	@Test(timeout = 1000)
	public void testSave1Level() {

		// This is an example to show how to save one-level parse tree in save1LevelExpToFile().
		// You are required to implement your own saveToFile() method.
		Exp addexp = new AddExp(new IntExp(2), new IntExp(3));
		SaveExpData saveExpData = new SaveExpData(addexp);

		File file = new File("expressions.xml");
		file.delete();

		saveExpData.save1LevelExpToFile(file);

		assertTrue(file.exists());

		file.delete();
	}

	@Test(timeout = 1000)
	public void testLoad1Level() {

		// This is an example to show how to load one-level parse tree in load1LevelExpFromFile().
		// You are required to implement your own loadFromFile() method.
		Exp addexp = new AddExp(new IntExp(2), new IntExp(3));
		SaveExpData saveExpData = new SaveExpData(addexp);

		File file = new File("expressions.xml");
		file.delete();

		saveExpData.save1LevelExpToFile(file);

		assertTrue(file.exists());

		Exp actualExp = LoadExpData.loadFromFile(file, addexp.getType());

		assertTrue(checkExpEquality(actualExp, addexp));

		file.delete();
	}

	@Test(timeout = 1000)
	public void testSave2Level() {

		// Two-level parse tree
		Exp addexp1 = new AddExp(new IntExp(2), new IntExp(3));
		Exp addexp2 = new AddExp(new IntExp(1), new IntExp(3));
		Exp multexp = new MulExp(addexp1, addexp2);

		SaveExpData saveExpData = new SaveExpData(multexp);

		File file = new File("expressions.xml");
		file.delete();

		saveExpData.saveToFile(file);

		assertTrue(file.exists());

//		file.delete();
	}

	@Test(timeout = 1000)
	public void testLoad2Level() {

		// Two-level parse tree
		Exp addexp1 = new AddExp(new IntExp(2), new IntExp(3));
		Exp addexp2 = new AddExp(new IntExp(1), new IntExp(3));
		Exp multexp = new MulExp(addexp1, addexp2);

		SaveExpData saveExpData = new SaveExpData(multexp);

		File file = new File("expressions.xml");
		file.delete();

		saveExpData.saveToFile(file);

		assertTrue(file.exists());

		Exp actualExp = LoadExpData.loadFromFile(file, multexp.getType());

		assertTrue(checkExpEquality(actualExp, multexp));

		file.delete();
	}

	@Test(timeout = 1000)
	public void testSave3Level() {

		// Three-level parse tree
		Exp addexp1 = new AddExp(new IntExp(2), new IntExp(3));
		Exp addexp2 = new AddExp(new IntExp(1), new IntExp(3));
		Exp multexp1 = new MulExp(addexp1, addexp2);

		Exp subexp1 = new SubExp(new IntExp(5), new IntExp(2));
		Exp multexp2 = new MulExp(new IntExp(2), new IntExp(2));
		Exp expoexp2 = new ExpoExp(subexp1, multexp2);

		Exp subexp2 = new SubExp(multexp1, expoexp2);

		SaveExpData saveExpData = new SaveExpData(subexp2);

		File file = new File("expressions.xml");
		file.delete();

		saveExpData.saveToFile(file);

		assertTrue(file.exists());

//		file.delete();
	}

	@Test(timeout = 1000)
	public void testLoad3Level() {

		// Three-level parse tree
		Exp addexp1 = new AddExp(new IntExp(2), new IntExp(3));
		Exp addexp2 = new AddExp(new IntExp(1), new IntExp(3));
		Exp multexp1 = new MulExp(addexp1, addexp2);

		Exp subexp1 = new SubExp(new IntExp(5), new IntExp(2));
		Exp multexp2 = new MulExp(new IntExp(2), new IntExp(2));
		Exp expoexp2 = new ExpoExp(subexp1, multexp2);

		Exp subexp2 = new SubExp(multexp1, expoexp2);

		SaveExpData saveExpData = new SaveExpData(subexp2);

		File file = new File("expressions.xml");
		file.delete();

		saveExpData.saveToFile(file);

		assertTrue(file.exists());

		Exp actualExp = LoadExpData.loadFromFile(file, subexp2.getType());

		assertTrue(checkExpEquality(actualExp, subexp2));

		file.delete();
	}

	private static boolean checkExpEquality(Exp actual, Exp expected) {

		if (actual.getType() != expected.getType()) {
			return false;
		}

		if (actual.getType() == ExpType.IntExp) {
			IntExp exp1 = (IntExp) actual;
			IntExp exp2 = (IntExp) expected;

			return exp1.getValue() == exp2.getValue();
		}

		List<Exp> actualSub = actual.getSubExps();
		List<Exp> expectedSub = expected.getSubExps();
		if (actualSub.size() != expectedSub.size()) {
			return false;
		}

		int count = 0;
		while (count < actualSub.size()) {
			if (!checkExpEquality(actualSub.get(count), expectedSub.get(count))) {
				return false;
			}
			count++;
		}

		return true;
	}
}
