//package utils;

public class MyUtil{
	
	/**
	 * Extract the the first double from string.
	 * i.e. "123.456" => 123.456
	 * i.e. "abc123.456efg" => 123.456
	 * i.e. "abc123." => 123
	 * 
	 * 
	 * @param text input text, may be null
	 * @return {@code double} extracted from text
	 */
	public static double parseDouble(String text){
		if(text == null) {
			System.out.println(0);
			return 0;
		}
		
		int pos=0;
		double number;

		System.out.println(1);

		while (pos < text.length() && !Character.isDigit(text.charAt(pos)) ){
			pos++;
			System.out.println(2);
		}
		int start = pos;
		System.out.println(3);
		while (pos < text.length() && Character.isDigit(text.charAt(pos)) ){
			pos++;
			System.out.println(4);
		}

		System.out.println(5);
		// check period in a sequence. Note that valid double has only single period in a seq
		if (pos+1 < text.length() && text.charAt(pos) == '.' && Character.isDigit(text.charAt(pos+1))) {
			System.out.println(6);

			pos++;
			while (pos < text.length() && Character.isDigit(text.charAt(pos))){
				System.out.println(7);

				pos++;
			}
		}

		System.out.println(8);

		if (start == pos){
			// when there is no number in text
			System.out.println(9);
			return 0;
		}

		System.out.println(10);

		System.out.println(text.substring(start, pos));
		number = Double.parseDouble(text.substring(start, pos));

		return number;
	}
}