/* Afnan Sultan (N01154597)
   COP4991 - Dr. Roger Eggen
   Lexical Analyzer (p1): Generates sequence of tokens for the Parser
   Date Last Modified: 9/4/19
 */
import java.io.File;
import java.io.IOException;
import java.lang.String;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;

class Token {

    private String lexeme;
    private String category;
    private String token;
    private int scope;
    private boolean validity;
    private static Map<String, String> tokens =  new LinkedHashMap<String, String>();

    Token (String category, String lexeme) {
        this.category = category; // KEYWORD, ID, NUM, ERROR, SPECIAL_SYMBOL, ERROR
        this.lexeme = lexeme; // the actual token itself
        tokens.put(lexeme, category);
        //System.out.println(category);
    }

    public static void printTokens() {
        for(Map.Entry<String, String> entry: tokens.entrySet() ) {
            System.out.println(entry.getKey() + " = " + entry.getValue() + " ");
        }
    }

    public Map<String, String> getTokenList() {
        return tokens;
    }

} // class Token

public class Lexer {

    public static void main(String[] args) throws IOException {

        Scanner printer = new Scanner(new File(args[0])); // used to print sample input
        Scanner scanner = new Scanner(new File(args[0])); // used to parse tokens
        if ( args.length != 1 ) {
            System.out.println("Usage: java Lexer filename");
            System.exit(0);
        }

        //List<String> tokens = new ArrayList<String>();

        System.out.println("SAMPLE INPUT: ");
        while ( printer.hasNext() ) { System.out.println(printer.nextLine()); } // print source code

        boolean comment_mode = false;

        String complete_block_comment = "(\\/\\*).*(\\*\\/)|(\\/\\*).*";
        String incomplete_block_comment = "(\\/\\*).*";
        String closing_block_comment = "^(.*?)(\\*\\/)";

        while ( scanner.hasNext() ) {
            String line = scanner.nextLine().trim();
            if ( line.length() == 0 ) continue; // skip empty line in stream

            System.out.println("INPUT: " + line);
            line = line.replaceAll("(\\/\\/).*", ""); // replace everything after line comments with ""

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
            findTokens(line); // powered by regex
        }

        System.out.println();
        System.out.println("The tokens: ");
        Token.printTokens();

    }//main

    public static void findTokens(String str) {

        String keyword = "\\b(?:else|if|int|return|void|while)\\b";
        String identifier = "\\b[a-zA-Z]+\\b";
        String number = "\\b[\\d]+\\b";
        String special_symbol = "==|!=|<=|>=|[+\\-*/<>=;,()\\[\\]{}]";
        String error = "\\S+";
        String regex = "(" + keyword + ")|(" + identifier + ")|(" + number + ")|(" + special_symbol + ")|(" + error + ")";

        Pattern pattern = Pattern.compile(regex);

        for( Matcher matcher = pattern.matcher(str); matcher.find(); ) { // Attempt to match each capture group against the regex
            if ( matcher.start(1) != -1 ) {
                System.out.println("keyword: " + matcher.group() );
                new Token("KEYWORD", matcher.group() );
            } else if ( matcher.start(2) != -1 ) {
                System.out.println("identifier: " + matcher.group() );
                new Token ("ID", matcher.group() );
            } else if ( matcher.start(3) != -1 ) {
                System.out.println("number: " + matcher.group());
                new Token("NUM", matcher.group() );
            } else if ( matcher.start(4) != -1 ) {
                System.out.println( matcher.group() );
                new Token("SPECIAL", matcher.group() );
            } else if ( matcher.start(5) != -1 ) {
                System.out.println("error: " + matcher.group() );
                new Token("ERROR", matcher.group());
            }
        }

    }
} // class Lexer