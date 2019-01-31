import java.io.File;
import java.util.*;

public class Items {
    private Map<String, Item> items;
    private Double support_distance_constraint;

    public Items(File f)
    {
        this.items = new HashMap<>();
        Parser p = new Parser(f);
        String s;
        while((s = p.get_next_line()) != null)
        {
            parse_line(s);
        }
    }

    private void parse_line(String s)
    {
        // All cases split on =
        String regex = "=";
        String[] param = s.split(regex);

        if(param[0].contains("SDC"))
        {
            this.support_distance_constraint = new Double(param[1]);
        }
        if(param[0].contains("MIS"))
        {
            String[] item_name = param[0].split("[()]+");
            Item item = new Item(item_name[1]);
            item.set_minsup(new Double(param[1]));
            this.items.putIfAbsent(item.get_name(), item);
        }

    }

    public void update_count(Set<String> s)
    {
        for( String item : s)
        {
            if (items.containsKey(item))
            {
                items.get(item).incr_count();
            }
            else
            {
                items.put(item, (new Item(item)).incr_count());
            }
        }
    }

    private boolean contains_item(String s)
    {
        return items.containsKey(s);
    }

    private void add_item(String s)
    {
        if(!contains_item(s))
            items.put(s, new Item(s));
    }

    Item get_item(String s)
    {
        add_item(s);
        return items.get(s);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("Items: ");
        this.items.forEach( (k, v) -> sb.append(v.toString()).append(" "));
        return sb.toString();
    }

    public List<Item> toList()
    {
        List<Item> list = new ArrayList<Item>(items.values());
        list.sort(null);
        return list;
    }

}
