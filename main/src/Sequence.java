import java.io.*;
import java.util.*;

public class Sequence implements Comparable<Sequence>{
    public  ArrayList<List<Item>> transaction;
    private final Integer length;
    private  Integer size;
    private Integer count;

   public Sequence(String s, Items i) {

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
    public Sequence(Sequence S1,Sequence S2,boolean bit){///Joining Process
        this.transaction = new ArrayList<List<Item>>();
        this.length=S1.getLength()+1;

        if (bit){//The Item (from S2) gets added as a seperate list
            this.size=S1.get_Size()+1;
            for( List<Item> temp:S1.get_Copy()){
                this.transaction.add(temp);
            }
            List <Item> temp=new ArrayList<Item>() ;
            temp.add(S2.getlastItem());
            this.transaction.add(temp);
        }else{//The item gets added at the ened of the last element of S1
            this.size=S1.get_Size();
            List<List<Item>> Temp_List=S1.get_Copy();
            for(int i=0;i<Temp_List.size();++i){

                //System.out.println("Temp_List"+" "+Temp_List.get(i));
                this.transaction.add(Temp_List.get(i));
            }
          Item temp_item=new Item(S2.getlastItem().get_name());
           temp_item.setCount(S2.getlastItem().get_count());
           temp_item.set_minsup(S2.getlastItem().get_minsup());
            this.transaction.get(this.transaction.size()-1).add(temp_item);
            //System.out.println("Printing Transaction"+this.transaction.toString());
        }


    }
    public Sequence(Item i, Sequence S2,boolean bit){//Constructor for Reverse join
        this.transaction = new ArrayList<List<Item>>();
        this.length=S2.getLength()+1;

        if (bit){//Adds it at the beginnning, its own sepearte list--> first set!!
            this.size=S2.get_Size()+1;
            List <Item> temp=new ArrayList<Item>() ;
            temp.add(i);
            this.transaction.add(0,temp);
            for( List<Item> temp_1:S2.get_Copy()){
                this.transaction.add(temp_1);
            }

        }else{//Adds it to the first set that already exists

            for( List<Item> temp:S2.get_Copy()){
                this.transaction.add(temp);
            }
            this.transaction.get(0).add(i);

        }

    }
    public Set<Item> get_unique_items()
    {
        HashSet<Item> h = new HashSet<>();
        transaction.forEach( sequence -> sequence.forEach( item -> h.add(item)));
        return h;
    }
    public List<List<Item>> get_Copy(){
        List<List<Item>> temp_List= new ArrayList<List<Item>>();
        for (int i=0;i<this.transaction.size();++i){
            List<Item> temp= new ArrayList<>();
            for(Item ii: this.transaction.get(i)){
                temp.add(ii);

            }
            temp_List.add(temp);

        }
      // System.out.println("Should be cCOPY"+temp_List.toString());
        return temp_List;
    }
    public String get_all_butFirst(){
        Integer counter = 0;
        StringBuilder sb = new StringBuilder();
        for ( Item item : as_list_of_items()) {

            if (counter == 0){
                continue;
            }

            else{
                sb.append(item.get_name()).append(" ");
            }
            ++counter;

        }
        return sb.toString();
    }
    public String get_all_but_secondtoLast(){
        Integer counter = 0;
        StringBuilder sb = new StringBuilder();
        for ( Item item : as_list_of_items()) {

            if (counter == as_list_of_items().size()-2){
                continue;
            }

            else{
                sb.append(item.get_name()).append(" ");
            }
            ++counter;

        }
        return sb.toString();
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

    public String get_all_but_second() {
        int counter = 0;
        StringBuilder sb = new StringBuilder();
       //System.out.println("As List of items"+as_list_of_items());
        for ( Item item : as_list_of_items()) {

            if (counter == 1){

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
    public List<Item> getFirstSet(){
       return this.transaction.get(0);
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

}
