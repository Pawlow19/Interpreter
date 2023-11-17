import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //Przykładowe rekordy
        List<Record> records = new ArrayList<>();
        records.add(new Record(1, 2, 3, 4, 5, 6, 7, 8));
        records.add(new Record(2, 1, 3, 4, 5, 6, 7, 8));
        records.add(new Record(3, 2, 3, 4, 5, 6, 7, 8));
        records.add(new Record(1, 1, 3, 4, 5, 6, 7, 8));

        Interpreter interpreter = new Interpreter();

        //Przykładowe wyrażenie
        String expression = "(a = 1 and b = 2) or (a = 2 and b = 1)";

        //Parsuj do RPN
        List<String> rpnTokens = interpreter.parseToRPN(expression);
        System.out.println("Wyrazenie w postaci RPN: " + String.join(" ", rpnTokens));

        //Zbuduj drzewo
        //Node root = interpreter.buildTreeFromRPN(rpnTokens);

        //Wyświetl drzewo
        //interpreter.displayTree(root, 0);

        // Wybór rekordów
        //List<Record> selectedRecords = interpreter.evaluateExpression(root, records);

        //Wyświetlanie rekordów
        //for (Record record : selectedRecords) {
            //record.displayRecord();
        //}
    }
}