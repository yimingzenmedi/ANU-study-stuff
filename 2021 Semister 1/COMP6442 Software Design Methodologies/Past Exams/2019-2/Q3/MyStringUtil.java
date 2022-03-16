//package utils;

public class MyStringUtil{
    /**
     * Checks if the String contains mixed casing of both uppercase and
     * lowercase characters.
     *
     * {@code null} input will return {@code false}. An empty String
     * ({@code length()=0}) will return {@code false}.
     * 
     * @param text    the String to check, may be null
     * @return {@code true} if the String contains both uppercase and
     *         lowercase characters
     */
    public static boolean isMixedCase(String text) {
        if (text == null) {
            System.out.println(0);
            return false;
        } else {
            System.out.println(1);
        }

        boolean containsUppercase = false;
        boolean containsLowercase = false;
        final int sz = text.length();

        if(sz == 0){
            System.out.println(2);
        	return false;
        } else {
            System.out.println(3);
        }

        for (int i = 0; i < sz; i++) {
            if (containsUppercase) {
                System.out.println(4);
            	if (containsLowercase) {
                    System.out.println(5);
            		return true;
            	} else {
                    System.out.println(6);
                }
            } else {
                System.out.println(7);
            }
            if (Character.isUpperCase(text.charAt(i))) {
                System.out.println(8);

                containsUppercase = true;
            }  else {
                System.out.println(9);

            }
            if (Character.isLowerCase(text.charAt(i))) {
                containsLowercase = true;
                System.out.println(10);

            } else {
                System.out.println(11);
            }
        }


        System.out.println(12);
        return containsUppercase && containsLowercase;
    }
}