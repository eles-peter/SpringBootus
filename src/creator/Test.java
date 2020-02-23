package creator;

import static creator.utils.StringBuherator.makeSQLName;

public class Test {

    public static void main(String[] args) {


        String test = "ezEgyHulyeNevuString";

        String test2 = "Abigel";
        String test3 = "jozsi";

        String test4 = "EzEgyMasikHulyeNevuString";

        System.out.println(makeSQLName(test));
        System.out.println(makeSQLName(test2));
        System.out.println(makeSQLName(test3));
        System.out.println(makeSQLName(test4));

    }
}
