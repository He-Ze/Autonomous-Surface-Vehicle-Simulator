package gebd.games.boat.json.util;

/**
 * Created by CaptainPete on 8/1/2016.
 */
public class ParseNiceJson {
    public static StringBuilder prettyParseJson(String jsonString, int spaces) {
        StringBuilder result = new StringBuilder();
        int depth = 0;
        char nextChar = jsonString.charAt(0);
        for (int charNo = 1; charNo < jsonString.length(); charNo++) {
            char c = nextChar;
            nextChar = jsonString.charAt(charNo);

            boolean appendNewLineBefore = false;
            boolean appendNewLine = false;
            if (c == '{' || c == '[') {
                depth++;
                appendNewLine = true;
            } else if (c == '}' || c == ']') {
                appendNewLineBefore = true;
                depth--;
                if (nextChar == ','|| nextChar == '}' || nextChar == ']') {
                    //Don't create a new line.
                } else {
                    appendNewLine = true;
                }
            } else if (c == ',') {
                appendNewLine = true;
            }
            if (appendNewLineBefore) {
                result.append('\n');
                for (int spaceNo = 0; spaceNo < (depth * spaces); spaceNo++) {
                    result.append(' ');
                }
            }
            result.append(c);
            if (appendNewLine) {
                result.append('\n');
                for (int spaceNo = 0; spaceNo < (depth * spaces); spaceNo++) {
                    result.append(' ');
                }
            }
        }
        result.append('\n');
        result.append(nextChar);
        return result;
    }



}
