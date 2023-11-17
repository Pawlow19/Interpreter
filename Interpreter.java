import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Interpreter {

    

    /*Metoda preprocsująca wyrażenia
    - usunięcie whitespace
    - zamiana >= na @
    - zamiana <= na $
    - zamiana != na !
    */
    public static String preprocessExpression(String expression) {
        //Usuwanie białych znaków
        StringBuilder processedExpression = new StringBuilder();
        for (char c : expression.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                processedExpression.append(c);
            }
        }

        //Zmiana probelmatycznych sekwencji
        String processedString = processedExpression.toString()
                .replace("and", "&")
                .replace("or", "|")
                .replace(">=", "@")
                .replace("<=", "$")
                .replace("!=", "!");
        System.err.println("Wyrazenie po preprocesie" + processedString);
        return processedString;
    }

    //Metoda parsująca wyrażenie do RPN
    public List<String> parseToRPN(String expression) {
        expression = preprocessExpression(expression);
        List<String> tokens = tokenize(expression);
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();
        
        System.err.println("output: " + output);
        System.err.println("operators: " + operators);

        Map<String, Integer> precedence = new HashMap<>();
        precedence.put("(", 0);
        precedence.put(")", 1);
        precedence.put("=", 1);
        precedence.put(">", 2);
        precedence.put("<", 2);
        precedence.put("@", 2); // >=
        precedence.put("$", 2); // <=
        precedence.put("!", 2); // !=
        precedence.put("&", 3); // "and"
        precedence.put("|", 4); // "or"
    
        for (String token : tokens) {
            if (token.equals("(")) {
                operators.push(token);
                System.err.println("operators: " + operators);
                System.err.println("output: " + output);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                    System.err.println("operators: " + operators);
                    System.err.println("output: " + output);
                }
                operators.pop(); // Usuń "(" ze stacku
                System.err.println("operators: " + operators);
                System.err.println("output: " + output);
            }else if (isOperator(token)) {
                while (!operators.isEmpty() && precedence.get(token) <= precedence.get(operators.peek())) {
                    output.add(operators.pop());  
                    System.err.println("operators: " + operators);
                    System.err.println("output: " + output);
                }
                operators.push(token);
                System.err.println("operators: " + operators);
                System.err.println("output: " + output);
            } else {
                output.add(token);
                System.err.println("operators: " + operators);
                System.err.println("output: " + output);
            }
        }
    
    
        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }
    
        System.err.println("Test wyrazenia " + output);
        return output;
    }

    //Metoda budująca drzewo z RPN
    public Node buildTreeFromRPN(List<String> rpnTokens) {
        Stack<Node> stack = new Stack<>();

        for (String token : rpnTokens) {
            if (isOperator(token)) {
                Node right = stack.pop();
                Node left = stack.pop();
                Node operatorNode = new Node(token);
                operatorNode.left = left;
                operatorNode.right = right;
                stack.push(operatorNode);
            } else {
                stack.push(new Node(token));
            }
        }

        return stack.pop();
    }

    //Metoda wyświetlajaca drzewo
    public void displayTree(Node root, int level) {
        if (root != null) {
            displayTree(root.right, level + 1);
            if (level != 0) {
                for (int i = 0; i < level - 1; i++)
                    System.out.print("|   ");
                String value = root.value;
                if (value.equals("&")) {
                    System.out.println("|--- and");
                } else if (value.equals("|")) {
                    System.out.println("|--- or");
                } else if (value.equals("!")) {
                    System.out.println("|--- !=");
                } else if (value.equals("@")) {
                    System.out.println("|--- >=");
                } else if (value.equals("$")) {
                    System.out.println("|--- <=");
                } else {
                    System.out.println("|---" + root.value);
                }
            } else {
                System.out.println(root.value);
            }
            displayTree(root.left, level + 1);
        }
        else if(level == 0 && root == null){
            System.err.println("Drzewo ukończone.");
        }
    }

    //Metoda wybierajaca rekordy
    public List<Record> evaluateExpression(Node root, List<Record> records) {
        List<Record> selectedRecords = new ArrayList<>();
    
        for (Record record : records) {
            if (evaluateNode(root, record)) {
                selectedRecords.add(record);
            }
        }
    
        return selectedRecords;
    }

    public boolean evaluateNode(Node node, Record record) {
        if (node != null) {
            String value = node.value;
            if (isOperator(value)) {
                boolean leftValue = evaluateNode(node.left, record);
                boolean rightValue = evaluateNode(node.right, record);
    
                switch (value) {
                    case "=":
                        return evaluateNodeInt(node.left, record) == evaluateNodeInt(node.right, record);
                    case "<":
                        return evaluateNodeInt(node.left, record) < evaluateNodeInt(node.right, record);
                    case ">":
                        return evaluateNodeInt(node.left, record) > evaluateNodeInt(node.right, record);
                    case "<=":
                        return evaluateNodeInt(node.left, record) <= evaluateNodeInt(node.right, record);
                    case ">=":
                        return evaluateNodeInt(node.left, record) >= evaluateNodeInt(node.right, record);
                    case "!=":
                        return evaluateNodeInt(node.left, record) != evaluateNodeInt(node.right, record);
                    case "&":
                        return leftValue && rightValue;
                    case "|":
                        return leftValue || rightValue;
                    default:
                        throw new IllegalArgumentException("Nieznany operator: " + value);
                }
            } else {
                // For operands
                int fieldValue = getFieldValue(record, value);
                int nodeValue = Integer.parseInt(value);
                return fieldValue == nodeValue; // Compare the field value with the node's integer value
                //return false;
            }
        }
        return false; // Placeholder return
    }
    
    public int evaluateNodeInt(Node node, Record record) {
        if (node != null && !isOperator(node.value)) {
            return getFieldValue(record, node.value);
        }
        return 0;
    }
    
    public int getFieldValue(Record record, String fieldName) {
        switch (fieldName) {
            case "a":
                return record.getA();
            case "b":
                return record.getB();
            case "c":
                return record.getC();
            case "d":
                return record.getD();
            case "e":
                return record.getE();
            case "f":
                return record.getF();
            case "g":
                return record.getG();
            case "h":
                return record.getH();
            default:
                throw new IllegalArgumentException("Zla nazwa pola: " + fieldName);
                //return record.getA();
        }
    }

    //Metoda tokenizująca wyrażenia
    public static List<String> tokenize(String expression) {
    List<String> tokens = new ArrayList<>();
    StringBuilder token = new StringBuilder();

    for (int i = 0; i < expression.length(); i++) {
        char c = expression.charAt(i);

        if (isOperator(String.valueOf(c)) || c == '(' || c == ')') {
            if (token.length() > 0) {
                tokens.add(token.toString());
                token = new StringBuilder();
            }

            String possibleOperator = String.valueOf(c);
            if (i < expression.length() - 1) {
                String nextChar = String.valueOf(expression.charAt(i + 1));
                String combinedOperator = possibleOperator + nextChar;
                if (isOperator(combinedOperator)) {
                    tokens.add(combinedOperator);
                    i++;
                    continue;
                }
            }

            tokens.add(possibleOperator);
        } else {
            token.append(c);
        }
    }

    if (token.length() > 0) {
        tokens.add(token.toString());
    }
    System.err.println("tokeny: " + tokens);
    return tokens;
}


    public static boolean isOperator(String token) {
        return token.equals("&") || token.equals("|") || token.equals("=")
                || token.equals("<") || token.equals(">") || token.equals("@")
                || token.equals("$") || token.equals("!");
    }
}
