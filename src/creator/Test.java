package creator;

import static creator.utils.StringBuherator.makeSQLName;
import static creator.utils.StringBuherator.makeSentence;

public class Test {

    public static void main(String[] args) {


        String test = "ezEgyHulyeNevuString";

        String test2 = "Abigel";
        String test3 = "jozsi";

        String test4 = "EzEgyMasikHulyeNevuString";

        System.out.println(makeSentence(test));
        System.out.println(makeSentence(test2));
        System.out.println(makeSentence(test3));
        System.out.println(makeSentence(test4));

    }
}
