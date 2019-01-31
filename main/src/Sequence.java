import java.io.*;
import java.util.*;

public class Sequence implements Comparable<Sequence>{
    private final List<List<Item>> transaction;
    private final Integer length;
    private final Integer size;
    private Integer count;

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

    public String get_all_but_last()
    {
        StringBuilder sb = new StringBuilder();
        this.transaction.forEach( seq -> seq.forEach( i -> sb.append(i.get_name()).append(" ")));
        return sb.toString();
    }

    public String get_all_but_second() {
        Integer counter = 1;
        StringBuilder sb = new StringBuilder();
        for (List<Item> seq : this.transaction) {
            for (Item i : seq) {
                if (counter == 2)
                    continue;
                sb.append(i.get_name()).append(" ");
            }
        }
        return sb.toString();
    }

    public String get_first() {
        if(this.length > 0)
        {
            return this.transaction.get(0).get(0).get_name();
        }
        return "";
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

    private List<Item> as_list_of_items()
    {
        List<Item> l = new ArrayList<>();
        this.transaction.forEach(sub -> sub.forEach( item -> l.add(item)));
        return l;
    }

    public int compareTo(Sequence b)
    {
        List<Item> a_list = this.as_list_of_items();
        List<Item> b_list  = b.as_list_of_items();
        Integer compare = 0;
        for(int i = 0; i < a_list.size(); i++)
        {
            compare = a_list.get(i).compareTo(b_list.get(i));
            if(compare != 0) break;
        }
        return compare;
    }

    public Integer compare(Sequence a, Sequence b)
    {
        return a.compareTo(b);
    }

}
