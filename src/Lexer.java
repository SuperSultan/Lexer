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

        boolean comment_mode = false;

        String complete_block_comment = "(\\/\\*).*(\\*\\/)|(\\/\\*).*";
        String incomplete_block_comment = "(\\/\\*).*";
        String closing_block_comment = "^(.*?)(\\*\\/)";
        String line_comment = "(\\/\\/).*";

        String special_symbol = "(==)|(!=)|(<=)|(>=)|(\\+)|(\\-)|(\\*)|(\\/)|(\\<)|(\\>)|(\\=)|(\\;)|(\\,)|(\\()|(\\))|(\\[)|(\\])|(\\{)|(\\})|(\\,)";
        String keyword = "(else)+|(if)+|(int)+|(return)+|(void)+|(while)+|(main)+";
        String identifier = "\\b(?!(else)|(if)|(int)|(return)|(void)|(while)|(main))\\b[a-zA-Z]+";
        String number = "[\\d]+";

        for (String line : lines) {
            System.out.println("INPUT: " + line);

            if ( line.contains("//") ) {
                line = line.replaceAll(line_comment, "");
            }
            if ( comment_mode && line.contains("*/") && !line.contains("/*") ) {
                line = line.replaceAll(closing_block_comment, "");
                comment_mode = false;
            } else if ( line.contains("/*") && line.contains("*/") ) {
                line = line.replaceAll(complete_block_comment, "");
                comment_mode = false;
            } else if ( line.contains("/*") ) {
                line = line.replaceAll(incomplete_block_comment, "");
                comment_mode = true;
            }

            findTokens(line, special_symbol);
            findTokens(line, keyword);
            findTokens(line, identifier);
            //findTokens(line, NUM);
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

    public static void findTokens(String str, String regex) {

        String special_symbol = "(==)|(!=)|(<=)|(>=)|(\\+)|(\\-)|(\\*)|(\\/)|(\\<)|(\\>)|(\\=)|(\\;)|(\\,)|(\\()|(\\))|(\\[)|(\\])|(\\{)|(\\})|(\\,)";
        String keyword = "(else)+|(if)+|(int)+|(return)+|(void)+|(while)+|(main)+";
        String identifier = "\\b(?!(else)|(if)|(int)|(return)|(void)|(while)|(main))\\b[a-zA-Z]+";
        String number = "[\\d]+";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        if (regex.equals(keyword) ) {
            while ( matcher.find() ) { System.out.println("Keyword: " + matcher.group()); }
        } else if ( regex.equals(identifier) ) {
            while ( matcher.find() ) { System.out.println("ID: " + matcher.group()); }
        } else if ( regex.equals(number) ) {
            while ( matcher.find() ) { System.out.println("NUM: " + matcher.group()); }
        }
        while ( matcher.find() ) {
            System.out.println(matcher.group());
        }
    }
}