package creator.utils;

import java.util.ArrayList;
import java.util.List;

public class StringBuherator {

    private StringBuherator() {
    }

    public static String makeSQLName(String name) {
        List<Integer> indexesOfUpperCase = new ArrayList<>();
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (Character.isUpperCase(ch)) {
                indexesOfUpperCase.add(i);
            }
        }
        String lowerCasename = name.toLowerCase();
        StringBuilder result = new StringBuilder();
        if (indexesOfUpperCase.isEmpty() || (indexesOfUpperCase.size() == 1 && indexesOfUpperCase.get(0) == 0)) {
            result.append(lowerCasename);
        } else {
            int previousIndex = 0;
            for (int i = 0; i < indexesOfUpperCase.size(); i++) {
                if (indexesOfUpperCase.get(i) == 0) {
                    continue;
                }
                if (i == indexesOfUpperCase.size() - 1) {
                    result.append(lowerCasename.substring(previousIndex, indexesOfUpperCase.get(i)));
                    result.append("_");
                    result.append(lowerCasename.substring(indexesOfUpperCase.get(i)));
                } else {
                    result.append(lowerCasename.substring(previousIndex, indexesOfUpperCase.get(i)));
                }
                if (i != indexesOfUpperCase.size() - 1) {
                    result.append("_");
                }
                previousIndex = indexesOfUpperCase.get(i);
            }
        }
        return result.toString();
    }

    public static String makeSentence(String name) {
        List<Integer> indexesOfUpperCase = new ArrayList<>();
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (Character.isUpperCase(ch)) {
                indexesOfUpperCase.add(i);
            }
        }
        String lowerCasename = name.toLowerCase();
        StringBuilder result = new StringBuilder();
        if (indexesOfUpperCase.isEmpty() || (indexesOfUpperCase.size() == 1 && indexesOfUpperCase.get(0) == 0)) {
            result.append(lowerCasename);
        } else {
            int previousIndex = 0;
            for (int i = 0; i < indexesOfUpperCase.size(); i++) {
                if (indexesOfUpperCase.get(i) == 0) {
                    continue;
                }
                if (i == indexesOfUpperCase.size() - 1) {
                    result.append(lowerCasename.substring(previousIndex, indexesOfUpperCase.get(i)));
                    result.append(" ");
                    result.append(lowerCasename.substring(indexesOfUpperCase.get(i)));
                } else {
                    result.append(lowerCasename.substring(previousIndex, indexesOfUpperCase.get(i)));
                }
                if (i != indexesOfUpperCase.size() - 1) {
                    result.append(" ");
                }
                previousIndex = indexesOfUpperCase.get(i);
            }
        }
        return makeCapital(result.toString());
    }

    public static String makeEnumName(String name) {
        StringBuilder result = new StringBuilder();
        for (String word : name.split(" ")) {
            word = word.toUpperCase();
            result.append(word);
        }
        return result.toString();
    }

    public static String makeCapital(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    public static String makeUncapital(String name) {
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

}
