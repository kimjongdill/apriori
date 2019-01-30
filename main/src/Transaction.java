import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Transaction {
    private final List<List<String>> transaction;
    private final Integer length;
    private final Integer size;

    Transaction(String s) {
        this.transaction = new ArrayList<List<String>>();
        Integer length = 0;
        // Parse a text line
        String sequence_delims = "[{}]+";
        String item_delims = "[ ,]+";
        String[] sequences = s.substring(2, s.length() - 2).split(sequence_delims);
        this.size = sequences.length;
        for (String sequence : sequences)
        {
            String[] items = sequence.split(item_delims);
            length += items.length;
            ArrayList<String> set = new ArrayList<>(Arrays.asList(items));
            this.transaction.add(set);
        }
        this.length = length;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("<");
        for(List<String> sequence: this.transaction)
        {
            sb.append(sequence.toString().replace('[', '{').replace(']', '}'));
        }
        sb.append(">").append(" Length: ").append(this.length).append(" Size: ").append(this.size);
        return sb.toString();
    }
}
