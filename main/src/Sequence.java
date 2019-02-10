import java.io.*;
import java.util.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Sequence implements Comparable<Sequence>{
    private final List<List<Item>> transaction;
    private final Integer length;
    private final Integer size;
    private Integer count;

    Sequence(String s, Items i) {

        this.transaction = new ArrayList<List<Item>>();
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
            this.transaction.add(set);
        }
        this.length = length;
    }

//    public Boolean can_merge(Sequence other, double sdc)
//    {
//        List<List<Sequence>> a, b;
//        // Check minimum support distance
//        if(! this.meets_sdc(other, sdc))
//            return FALSE;
//
//
//        if(this.unique_minsup_in_first())
//        {
//            // Remove the second item from s1
//        }
//        if(this.unique_minsup_in_last())
//        {
//            // Remove the second from last item from s2
//        }
//
//    }

    private Boolean unique_minsup_in_first()
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

    private Boolean unique_minsup_in_last()
    {
        double minsup = this.get_minsup();
        int count = 0;
        List<Item> last_set = this.transaction.get(this.transaction.size() - 1);
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
        transaction.forEach( sequence -> sequence.forEach( item -> h.add(item)));
        return h;
    }

    public String get_all_but_last()
    {
        Integer counter = 0;
        StringBuilder sb = new StringBuilder();
        for ( Item item : as_list_of_items()) {

            if (counter == as_list_of_items().size()-1){
                continue;
            }

            else{
                sb.append(item.get_name()).append(" ");
            }
            ++counter;

        }
        return sb.toString();
    }
    public List<List<Item>> get_Copy(){
        List<List<Item>> temp_List= new ArrayList<List<Item>>();
        for (int i=0;i<this.transaction.size();++i){
            temp_List.add(this.transaction.get(i));
        }
        System.out.println("Should be complete First Sequence");
        return temp_List;
    }
    public String get_all_but_second() {
        Integer counter = 0;
        StringBuilder sb = new StringBuilder();
        for ( Item item : as_list_of_items()) {

            if (counter == 1){
                continue;
            }

            else{
                sb.append(item.get_name()).append(" ");
            }
            ++counter;

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
    public Item get_firstItem(){

        return this.transaction.get(0).get(0);

    }
    public Item getlastItem(){
        int initial_Length=this.transaction.size();
        int second_Length=this.transaction.get(initial_Length-1).size();
        return this.transaction.get(initial_Length-1).get(second_Length-1);

    }
    public List<Item> getLastSet(){
        int initial_Length=this.transaction.size();
        //System.out.println("INside last set"+" "+this.transaction.get(initial_Length-1).toString());
        return this.transaction.get(initial_Length-1);
    }
    public void add_Item(Item i,boolean Signal){

        if (Signal){
            //Seperate {}
            List<Item> temp=new ArrayList<>();
            temp.add(i);
            this.transaction.add(temp);

        }
        else{
            //at the end of the last {}
        }
    }
    public int get_Size(){

        return this.size.intValue();
    }
    /////////////
    /////////////

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

    public List<Item> as_list_of_items()
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
                super_seq = this.transaction.get(super_ind);
                sub_seq = other.transaction.get(sub_ind);
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

    public Boolean meets_sdc(Sequence other, double sdc)
    {
        return ((Math.abs(other.get_maxsup() - this.get_minsup()) <= sdc) &&
                (Math.abs(this.get_maxsup() - other.get_minsup()) <= sdc));
    }

    public Boolean meets_minimum_support(int total_records)
    {
        return this.count >= (this.get_minsup() * total_records);
    }
}
