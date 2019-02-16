import java.util.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Sequence implements Comparable<Sequence>{
    public  List<List<Item>> sequence;
    private final Integer length;
    private  Integer size;
    private Integer count;

    // Parse a formatted string into a sequence.
   public Sequence(String s, Items i) {

        this.sequence = new ArrayList<List<Item>>();
        this.count = 0;
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
            this.sequence.add(set);
        }
        this.length = length;
    }

    // Join 2 sequences with min minsup in first position
    public Sequence(Sequence a, Sequence b, boolean append_itemset){///Joining Process
        this.sequence = new ArrayList<List<Item>>();
        this.length = a.getLength()+1;
        this.count = 0;
        List<List<Item>> s1 = deep_copy(a);
        List<List<Item>> s2 = deep_copy(b);

        if(append_itemset)
        {
            this.size = a.get_Size() + 1;
            s1.add(s2.get(s2.size() - 1));
        }
        else
        {
            this.size = a.get_Size();
            List<Item> last_itemset = s2.get(s2.size() - 1);
            Item last_item = last_itemset.get(last_itemset.size() - 1);
            s1.get(s1.size() - 1).add(last_item);
            s1.get(s1.size() - 1).sort(null);
        }

        this.sequence = s1;
    }

    private static List<List<Item>> add_to_last_element(List<List<Item>> s1, List<List<Item>> s2)
    {
        List<Item> last_itemset = s2.get(s2.size() - 1);
        Item last_item = last_itemset.get(last_itemset.size() - 1);
        last_itemset = new ArrayList<Item>();
        last_itemset.add(last_item);
        s1.add(last_itemset);
        return s1;
    }

    // Join 2 sequences with min minsup in last position
    public Sequence(Sequence a, Sequence b, boolean prepend_itemset, boolean reverse){//Constructor for Reverse join
        this.sequence = deep_copy(b);
        Item i = a.sequence.get(0).get(0);
        this.size = b.get_Size();
        this.length=b.getLength()+1;
        this.count = 0;

        if (prepend_itemset){//Adds it at the beginning, its own separate list--> first set!!
            this.size++;
            List <Item> temp = new ArrayList<Item>() ;
            temp.add(i);
            this.sequence.add(0,temp);
        }
        else{//Adds it to the first set that already exists

            this.sequence.get(0).add(i);
            this.sequence.get(0).sort(null);
        }
    }

    // Join 2 sequences general case
    public Sequence(Sequence a, Sequence b)
    {
        List<List<Item>> s1 = deep_copy(a);
        List<List<Item>> s2 = deep_copy(b);

        if(s2.get(s2.size() - 1).size() == 1){
            s1.add(s2.get(s2.size() - 1));
            this.size = a.size + 1;
        }
        else {
            List<Item> s1_last = s1.get(s1.size() - 1);
            List<Item> s2_last = s2.get(s2.size() - 1);
            s1_last.add(s2_last.get(s2_last.size() - 1));
            s1_last.sort(null);
            this.size = a.size;
        }
        this.sequence = s1;
        this.length = a.getLength() + 1;
        this.count = 0;

    }

    private Sequence(List<List<Item>> l)
    {
        int my_length = 0;
        int my_count = 0;
        sequence = l;

        for( List<Item> subsequence : l) {
            my_count++;
            for (Item item : subsequence) {
                my_length++;
            }
        }
        length = my_length;
        size = my_count;
        count = 0;
        return;
    }
    private List<Item> add_unique(List<Item> a, List<Item> b)
    {
        for(Item i : b)
        {
            if (a.contains(i))
                continue;
            a.add(i);
        }
        a.sort(null);
        return a;
    }

    public Boolean can_merge(Sequence other, double sdc, Integer count) {
        List<List<Item>> a, b;
        // Check minimum support distance
        if (!this.meets_sdc(other, sdc, count))
            return FALSE;

        a = this.without_first();
        b = other.without_last();

        if (this.unique_minsup_in_first() && !other.unique_minsup_in_last()) {
            a = this.without_n(1);
        } else if (other.unique_minsup_in_last() && !this.unique_minsup_in_first()) {
            b = other.without_n(other.getLength() - 2);
        }

        return a.equals(b);
//        for (List<Item> s1 : a){
//            for (List<Item> s2 : b) {
//                if (!a.equals(b)){
//                    for(Item i1 : s1){
//                        for(Item i2 : s2)
//                        {
//                            if(! i1.equals(i2))
//                                return false;
//                        }
//                    }
//                }
//            }
//        }
//
//        return true;
    }

    private static List<List<Item>> deep_copy(Sequence a)
    {
        List<List<Item>> l = new ArrayList<>();
        for(List<Item> j : a.sequence)
        {
            List<Item> i = new ArrayList<>();
            for(Item k : j)
            {
                i.add(k);
            }
            l.add(i);
        }
        return l;
    }

    private List<List<Item>> without_first()
    {
        List<List<Item>> l = deep_copy(this);
        l.get(0).remove(0);
        if(l.get(0).size() == 0)
            l.remove(0);

        return l;
    }

    private List<List<Item>> without_last()
    {
        List<List<Item>> l = deep_copy(this);
        List<Item> last = l.get(l.size() -1);
        last.remove(last.size() - 1);
        if(last.size() == 0)
        {
            l.remove(l.size() - 1);
        }
        return l;
    }

    private List<List<Item>> without_n(int n)
    {
        List<List<Item>> l = deep_copy(this);
        int count = -1;
        int setCount = 0;

        List<Item> last_set = null;
        Item last_item = null;

        for(List<Item> set : l) {
            for (Item i : set) {
                count++;
                if (count == n)
                {
                    last_set = set;
                    last_item = i;
                    break;
                }
            }
            if(count == n)
                break;
        }
        if(last_set == null || last_item == null)
            return l;

        last_set.remove(last_item);
        if(last_set.size() == 0)
            l.remove(last_set);

        return l;
    }

    public Boolean unique_minsup_in_first()
    {
        double minsup = this.get_minsup();
        int count = 0;
        if(this.get_firstItem().get_minsup() > minsup)
            return FALSE;

        for(Item i : this.as_list_of_items())
        {
            if(i.get_minsup() == minsup)
                count++;

            if(count > 1)
                return FALSE;
        }

        return TRUE;
    }

    public Boolean unique_minsup_in_last()
    {
        double minsup = this.get_minsup();
        int count = 0;
        List<Item> last_set = this.sequence.get(this.sequence.size() - 1);
        if(last_set.size() > 1)
            return FALSE;
        if(last_set.get(0).get_minsup() != minsup)
            return FALSE;

        for(Item i : this.as_list_of_items())
        {
            if(i.get_minsup() == minsup)
                count++;

            if(count > 1)
                return FALSE;
        }
        return TRUE;
    }

    public Set<Item> get_unique_items()
    {
        HashSet<Item> h = new HashSet<>();
        sequence.forEach(sequence -> sequence.forEach(item -> h.add(item)));
        return h;
    }


    public String get_first() {
        if(this.length > 0)
        {
            return this.sequence.get(0).get(0).get_name();
        }
        return "";
    }
    public Item get_firstItem(){

        return this.sequence.get(0).get(0);

    }

    public int get_Size(){

        return this.size.intValue();
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("<");
        for(List<Item> sequence: this.sequence)
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

    public List<Item> as_list_of_items()
    {
        List<Item> l = new ArrayList<>();
        this.sequence.forEach(sub -> sub.forEach(item -> l.add(item)));
        return l;
    }

    public int compareTo(Sequence b)
    {
        if(b == null)
            return 1;

        String s1 = this.toString();
        String s2 = b.toString();

        return s1.compareTo(s2);
    }

    public Boolean equals(Sequence other)
    {
        if(other == null)
            return FALSE;
        if(this == other)
            return TRUE;

        String s1 = this.toString();
        String s2 = other.toString();
        return s1.equals(s2);
    }

    public int hashCode()
    {
        int hash_code = this.toString().hashCode();
        //System.out.println(this.toString() + " " + hash_code);
        return hash_code;
    }

    public int compareTo_2(Sequence b){
        List<Item> a_list = this.as_list_of_items();
        List<Item> b_list  = b.as_list_of_items();
        // System.out.println("Alist"+a_list+" "+"Blist"+" "+b_list);
        Integer compare = 0;
        for(int i = 0; i < a_list.size(); i++)
        {
            //compare = a_list.get(i).toString()

            //System.out.println("Digit Sequences"+ a_list.get(i).toString()+b_list.get(i).toString());
            compare = a_list.get(i).toString().compareTo(b_list.get(i).toString());

            if(compare != 0) break;

        }
        // System.out.println("Comparison returning..."+compare);
        return compare;
    }
    public Integer compare(Sequence a, Sequence b)
    {
        return a.compareTo(b);
    }

    public void incr_count()
    {
        this.count++;
    }


    public Boolean is_subsequence(Sequence other)
    {
        if(other.length > this.length) return FALSE;
        if(other.size > this.size) return FALSE;
        List<Item> super_seq;
        List<Item> sub_seq;
        Integer super_ind = 0;
        Integer sub_ind = 0;
        for(int i=0; i < (this.size - other.size + 1); i++)
        {
            super_ind = i;
            sub_ind = 0;
            while(super_ind < this.size && sub_ind < other.size)
            {
                super_seq = this.sequence.get(super_ind);
                sub_seq = other.sequence.get(sub_ind);
                if(super_seq.containsAll(sub_seq))
                    sub_ind++;
                super_ind++;
            }
            if(sub_ind == other.size)
                return TRUE;
        }
        return FALSE;
    }

    private double get_minsup()
    {
        return Collections.min(this.get_unique_items()).get_minsup();
    }
    private double get_maxsup() { return Collections.max(this.get_unique_items()).get_minsup(); }

    private Integer get_max_actual_count()
    {
        List<Integer> counts = new ArrayList<Integer>();
        this.as_list_of_items().forEach( i -> counts.add(i.get_count()));
        return Collections.max(counts);
    }

    private Integer get_min_actual_count()
    {
        List<Integer> counts = new ArrayList<Integer>();
        this.as_list_of_items().forEach( i -> counts.add(i.get_count()));
        return Collections.min(counts);
    }

    public Boolean meets_sdc(Sequence other, double sdc, Integer trans_count)
    {
        Integer a_max_count = this.get_max_actual_count();
        Integer a_min_count = this.get_min_actual_count();
        Integer b_max_count = other.get_max_actual_count();
        Integer b_min_count = other.get_min_actual_count();

        Double min_count = sdc * trans_count.doubleValue();


        return ((Math.abs(a_max_count.doubleValue() - b_min_count.doubleValue()) <= min_count) &&
                (Math.abs(b_max_count.doubleValue() - a_min_count.doubleValue()) <= min_count));
    }

    public Boolean meets_minimum_support(int total_records)
    {
        double minsup = this.get_minsup();
        double support = minsup * total_records;
        double count = this.count;
        return count >= support;
    }

    public Boolean is_valid_sequence()
    {

        for(List<Item> last_itemset : sequence ) {
            for (int i = 0; i < last_itemset.size() - 1; i++) {
                if (last_itemset.get(i) == last_itemset.get(i + 1))
                    return FALSE;
            }
        }
        return TRUE;
    }



    public List<Sequence> get_all_subsequences_with_min_minsup() {
       List<Sequence> l = new ArrayList<Sequence>();
       double min_minsup = this.get_minsup();
       for(int pos_to_remove = 0; pos_to_remove < length; pos_to_remove++)
       {
           List<List<Item>> sub = without_n(pos_to_remove);
           Sequence subsequence = new Sequence(sub);
           if(subsequence.get_minsup() == min_minsup) {
               l.add(subsequence);
           }
       }
       return l;
    }
}
