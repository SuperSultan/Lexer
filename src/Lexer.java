import java.io.File;
import java.io.IOException;
import java.lang.String;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(new File(args[0]));
        printTokens(input); // print sample input

        Scanner scanner = new Scanner(new File(args[0]));
        String[] lines = createListOfTokens(scanner);

        Stack<String> blocks = new Stack<>();

        String special_symbol = "(\\/\\*)|(\\*\\/)|(\\+)|(\\-)|(\\*)|(\\/)|(\\<)|(<=)|(>=)|(\\>)|(==)|(!=)|(\\=)|(\\;)|(\\,)|(\\()|(\\))|(\\[)|(\\])|(\\{)|(\\})|(\\,)";
        String keyword = "(else)+|(if)+|(int)+|(return)+|(void)+|(while)+|(main)+";
        String ID = "[a-zA-Z]+";
        String NUM = "[\\d]+";

        for (String line : lines) {

            System.out.println("INPUT: " + line);

            if (isLineComment(line)) { // is this in the right loop?
                line = getStringBeforeLineComment(line); // filter out what is after the line comments
                //continue; (is this line even necessary?)
            }
            if (line.length() < 3) {
                findTokens(line, special_symbol);
                findTokens(line, keyword);
                findTokens(line, ID);
                findTokens(line, NUM);
            }

            for (int i = 0; i < line.length() - 2; i++) {

                if (line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                    blocks.push("/*");
                    i = i + 2;
                } else if (!blocks.isEmpty() && line.charAt(i) != '/' && (line.charAt(i + 1) == '*' && line.charAt(i + 2) == '/')) {
                    blocks.pop();
                    i = i + 2;
                } else if (blocks.isEmpty()) {

                    if (line.lastIndexOf("/*") != -1) { // if there is a last index occurence of /* we replace everything from /* to */ with ""
                        line = line.replaceAll("/\\*.*?\\*/", "");
                    } else {
                        line = line.substring(i); // /* is on some previous line, so just eat the line
                    }
                    findTokens(line, special_symbol);
                    findTokens(line, keyword);
                    findTokens(line, ID);
                    findTokens(line, NUM);
                    break;
                } //else if

            }//for
        }//foreach

    }//main


    public static void printTokens(Scanner reader) {
        System.out.println("SAMPLE INPUT:");
        while (reader.hasNext()) {
            System.out.println(reader.nextLine());
        }
    }

    public static String[] createListOfTokens(Scanner reader) {
        List<String> lines = new ArrayList<>();
        while (reader.hasNext()) {
            lines.add(reader.nextLine()); // feed scanner into ArrayList
        }
        lines.removeAll(Arrays.asList("", null));
        lines.replaceAll(String::trim); // remove leading and trailing \\s+
        String[] str = lines.toArray(new String[0]);
        return str;
    }


    public static boolean isLineComment(String str) {
        if (str.isEmpty()) return false;
        for (int i = 0; i < str.length() - 1; i++)
            if (str.charAt(i) == '/' && str.charAt(i + 1) == '/') return true;
        return false;
    }

    public static String getStringBeforeLineComment(String str) {

        if (str.isEmpty()) return "";
        for (int i = 0; i < str.length() - 1; i++)
            if (str.charAt(i) == '/' && str.charAt(i + 1) == '/') {
                return str.substring(0, i);
            }
        return "";
    }

    public static void findTokens(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(str);
        int i = 0;
        while (m.find()) {
            String theGroup = m.group();
            if (regex == "(\\/\\*)|(\\*\\/)|(\\+)|(\\-)|(\\*)|(\\/)|(\\<)|(<=)|(>=)|(\\>)|(==)|(!=)|(\\=)|(\\;)|(\\,)|(\\()|(\\))|(\\[)|(\\])|(\\{)|(\\})|(\\,)") {
                System.out.format("%s\n", theGroup);
            } else if (regex == "(else)+|(if)+|(int)+|(return)+|(void)+|(while)+|(main)+") {
                System.out.format("KEYWORD:\n", theGroup);
            } else if (regex == "[a-zA-Z]+") {
                System.out.format("ID:\n", theGroup);
            } else if (regex == "[\\d]+") {
                System.out.format("NUM:\n", theGroup);
            }

        }
    }
}