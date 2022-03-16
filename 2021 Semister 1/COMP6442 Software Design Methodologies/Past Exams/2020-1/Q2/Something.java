
public class Something {

	private static final int VALUE1 = 36;
	private static final int VALUE2 = 15;

	private static int method1(int n1, int n2) {
		if (n1 >= n2) {
			System.out.println("1");
			while (n1 >= n2) {
				n1 -= n2;
			}
			System.out.println("method1 -> " + n1);
			return n1;
		} else {
			System.out.println("2");
			while (n2 >= n1) {
				n2 -= n1;
			}
			System.out.println("method1 -> " + n2);
			return n2;
		}
	}

	private static int method2(int n1, int n2) {
		while (n1 != n2) {
			System.out.println("3");
			if (n1 > n2) {
				System.out.println("4");
				n1 -= n2;
			}
			else {
				System.out.println("5");
				n2 -= n1;
			}
		}
		System.out.println("6");
		System.out.println("method2 -> " + n2);
		return n2;
	}

	private static int method3(int n1, int n2) {
		System.out.println("7");
		System.out.println("method3 -> " + n1 * n2 / method2(n1, n2));
		return n1 * n2 / method2(n1, n2);
	}

	public static int someMethod(int n1, int n2, int n3, int n4) {
		int sum = 0;
		if (method2(n1, n2) == VALUE1) {
			System.out.println("8");
			if (method3(n1, n3) == method3(n2, n4)) {
				System.out.println("9");
				sum += n1 + n4;
			} else {
				System.out.println("10");
			}
			sum += n1 + n2;
		} else {
			System.out.println("11");
		}

		if (method1(n3, n4) == VALUE2) {
			System.out.println("12");
			sum += n3 + n4;
		} else {
			System.out.println("13");
		}

		return sum;
	}
}
