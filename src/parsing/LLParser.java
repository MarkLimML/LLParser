package parsing;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import parsing.Scanner.*;

public class LLParser {
    public static Stack<String> rStack = new Stack<>();

    public static ParserTable lltable = new ParserTable();

    public static List<String> symbols = Arrays.asList("expr","symbol","union","id");
    public static List<String> terminals = Arrays.asList("TERMINAL","(",")","*","+","?","U","E");

    public static void initializeTable() {
        lltable.put("expr","TERMINAL","id union expr");
        lltable.put("expr","(","( expr ) symbol union expr");
        lltable.put("expr",")","");
        lltable.put("expr","E","id union expr");
        lltable.put("expr","$","");
        lltable.put("symbol","TERMINAL","");
        lltable.put("symbol","(","");
        lltable.put("symbol",")","");
        lltable.put("symbol","*","*");
        lltable.put("symbol","+","+");
        lltable.put("symbol","?","?");
        lltable.put("symbol","U","");
        lltable.put("symbol","E","");
        lltable.put("symbol","$","");
        lltable.put("union","TERMINAL","");
        lltable.put("union","(","");
        lltable.put("union",")","");
        lltable.put("union","U","U");
        lltable.put("union","E","");
        lltable.put("union","$","");
        lltable.put("id","TERMINAL","TERMINAL symbol");
        lltable.put("id","E","E");
    }

    public static void printList(List<Scanner.Token> tkList) {
        for(Scanner.Token t : tkList)
            System.out.print(t.s+" ");
        System.out.println();
        for(Scanner.Token t : tkList)
            System.out.print(t.t+" ");
        System.out.println();
    }

    public static Boolean pushRule(String k1, String k2) {
        Object set = lltable.get(k1,k2);
        if(set==null)
            return false;
        String[] tks = set.toString().split(" ");
        for (int i = tks.length - 1; i >= 0; i--)
            rStack.push(tks[i]);
        return true;
    }

    public static boolean isMatch(List<Scanner.Token> tokens) {
        Boolean matched = false;
        int idx = 0;
        //printList(tokens);
        while(!matched) {
            //System.out.println(Arrays.toString(rStack.toArray()));
            //System.out.println(rStack.peek());
            if(isSymbol(rStack.peek())) {
                //System.out.println(rStack.peek()+" sym");
                Boolean isnull = false;
                if(tokens.get(idx).t == Scanner.Type.TERMINAL) {
                    isnull = pushRule(rStack.pop(), tokens.get(idx).t.toString());
                }
                else {
                    isnull = pushRule(rStack.pop(), tokens.get(idx).s);
                }
                if(!isnull) {
                    //System.out.println("No Match");
                    break;
                }
                if(rStack.peek().equals(""))
                    rStack.pop();
            }
            else if(isTerminal(rStack.peek())) {
                //System.out.println(rStack.peek()+" term");
                if(rStack.peek().equals("TERMINAL")) {
                    if(rStack.peek().equals(tokens.get(idx).t.toString())) {
                        idx++;
                        rStack.pop();
                    }
                    else
                        break;
                }
                else {
                    if(rStack.peek().equals(tokens.get(idx).s)) {
                        idx++;
                        rStack.pop();
                    }
                    else
                        break;
                }
            }
            else {
                if(rStack.peek().equals("$") && tokens.get(idx).s.equals("$")) {
                    matched = true;
                    break;
                }
                else
                    break;
            }
        }
        return matched;
    }

    public static Boolean isSymbol(String s) { return symbols.contains(s); }
    public static Boolean isTerminal(String s) { return terminals.contains(s); }

    public static void main(String args[]) {
        Scanner s = new Scanner();
        initializeTable();
        try {
            File file = new File("src/input.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            FileWriter myWriter = new FileWriter("src/output.txt");

            while((line = reader.readLine()) != null) {
                List<Token> input = new ArrayList<>();
                input = s.getTokens(line.trim());
                input.add(new Scanner.Token("$"));
                rStack.clear();
                rStack.push("$");
                rStack.push("expr");

                //printList(input);
                if(isMatch(input)) {
                    System.out.println(line + " - ACCEPT");
                    myWriter.write(line+" - ACCEPT\n");
                }
                else {
                    System.out.println(line + " - REJECT");
                    myWriter.write(line+" - REJECT\n");
                }
            }
            System.out.println("Successfully wrote to the file.");
            reader.close();
            myWriter.close();
        } catch(IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
