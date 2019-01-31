import java.io.*;
import java.util.*;

public class Sequence {
    private final List<List<Item>> transaction;
    private final Integer length;
    private final Integer size;


    Sequence(String s, Items i) {

        this.transaction = new ArrayList<List<Item>>();
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
            ArrayList<Item> set = new ArrayList<>();
            for(String item : items)
            {
                set.add(i.get_item(item));
            }
            set.sort(null);
            this.transaction.add(set);
        }
        this.length = length;
    }

    public Set<Item> get_unique_items()
    {
        HashSet<Item> h = new HashSet<>();
        transaction.forEach( sequence -> sequence.forEach( item -> h.add(item)));
        return h;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("<");
        for(List<Item> sequence: this.transaction)
        {
            sb.append("{");
            for(int j = 0; j < sequence.size(); j++)
            {
                sb.append(sequence.get(j).get_name());
                if( j != (sequence.size() - 1))
                {
                    sb.append(", ");
                }
            }
            sb.append("}");
        }
        sb.append(">");
        return sb.toString();
    }

    public Integer getLength()
    {
        return this.length;
    }
}
