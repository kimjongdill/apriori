import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Candidates {

    // The length of sequences in this Candidate set
    private Integer length;
    private Integer transaction_count;
    private List<Sequence> candidates;
    // Generate the initial list11
    public Candidates(Items i, Integer n)
    {
        candidates = new ArrayList<Sequence>();
        this.length = 1;
        this.transaction_count = n;
        List<Item> l = i.toList();

        for(Item item : l)
        {
            System.out.println("List of Items"+item.get_name()+" "+item.get_count()+" "+item.get_minsup());
            Double support = item.get_count().doubleValue() / n.doubleValue();
            if(support > item.get_minsup()) {
                Sequence s = new Sequence(new String("<{" + item.get_name() + "}>"), i);
                candidates.add(s);
            }
        }
    }

    public Candidates(Items i, Candidates previous_round)
    {
        candidates = new ArrayList<Sequence>();

        length = previous_round.length + 1;
        List<Sequence> prev_seq = previous_round.candidates;
        this.transaction_count = previous_round.transaction_count;
        if(length == 2)
        {
            for(Sequence first : prev_seq){

                for(Sequence second : prev_seq){

                    List<Sequence> list = new ArrayList<>();
                    Sequence s = new Sequence(
                            new String("<{" + first.get_first() + "}{" + second.get_first() + "}>"), i);
                    candidates.add(s);
                    if(! first.equals(second)) {
                        Sequence s2 = new Sequence(
                                new String("<{" + first.get_first() + " " + second.get_first() + "}>"), i);
                        candidates.add(s2);
                    }

                }
            }
        }
        else{
            CandidateGeneration(previous_round);
        }

    }
    private void CandidateGeneration(Candidates previous_round){

        System.out.println("INside Candidate Generation "+previous_round.toString());
        //candidates = new HashMap<>();
        length = previous_round.length + 1;
        List<Sequence> prev_seq = previous_round.candidates;

        for(int counter_1=0;counter_1<prev_seq.size();++counter_1){
            int a=1;
            while((counter_1+a)<prev_seq.size()){
                System.out.println("First Sequence"+prev_seq.get(counter_1).toString()+"Second Sequence"+" "+prev_seq.get(counter_1+a).toString());
                if (MISLeast_First(prev_seq.get(counter_1))& (prev_seq.get(counter_1).compareTo_2(prev_seq.get(counter_1+a))!=0)){
                    // System.out.println("First has the Least");
                    if(potentialJoin(prev_seq.get(counter_1),prev_seq.get(counter_1+a))){

                        Join(prev_seq.get(counter_1),prev_seq.get(counter_1+a));
                    }
                }
                else if(MISLeast_Second(prev_seq.get(counter_1+a))   &   (prev_seq.get(counter_1+a).compareTo_2(prev_seq.get(counter_1))!=0)){
                    System.out.println("Second has the Least");
                    // if(potentialJoin_Reverse(prev_seq.get(counter_1),prev_seq.get(counter_1+1)));
                }
                else{
                    // oldJoin()
                }

                ++a;
            }

        }


    }

    private void Join(Sequence S1,Sequence S2) {
        System.out.println("Joining" + S1.toString());
        System.out.println("Joining" + S2.toString() );
        List<Sequence> list = new ArrayList<>();
        if (S2.getLastSet().size() == 1) {//last element is a single item


            // System.out.println("Last elemet is a single element" + S1.get_Copy() + " " + S2.getlastItem());

            S1.add_Item(S2.getlastItem(),true);
            System.out.println("Joined Sequence"+" "+S1.toString());
            this.candidates.add(S1);


        } else {//last elememt contains more than one item
            System.out.println("Last elemet contains several items");
            System.out.println("COMPARING Last Items" +" "+S2.getlastItem().get_name() +" "+S1.getlastItem().get_name());
            if (((S1.get_Size() == 1 & S1.getLength() == 2) & (S2.getlastItem().get_name().compareTo(S1.getlastItem().get_name()) > 0))

                    || (S1.getLength() > 2)) {

                System.out.println("CANDIDATE PRODUCED" + S1.getLastSet().add(S2.getlastItem()));

            }

            //candidates.put(s.get_all_but_last(), list)
        }
    }
    private boolean MISLeast_First(Sequence Seq){
        //System.out.println("First Min Support for:"+Seq.get_first()+" "+Seq.get_firstItem().get_minsup());
        Double min=Seq.get_firstItem().get_minsup();
        for(Item item: Seq.as_list_of_items()){
            //System.out.println("Item:"+item.get_name()+" "+item.get_minsup());
            if(min>item.get_minsup()) {
                return false;
            }

        }
        return true;
    }
    private boolean MISLeast_Second(Sequence Seq){
        int last_index=Seq.as_list_of_items().size()-1;
        Double min=Seq.as_list_of_items().get(last_index).get_minsup();

        for(Item item: Seq.as_list_of_items()){
            //System.out.println("Item:"+item.get_name()+" "+item.get_minsup());
            if(min>item.get_minsup()) {
                return false;
            }

        }
        return true;
    }
    private boolean potentialJoin(Sequence Seq_1, Sequence Seq_2){
        // System.out.println("First Sequence being compared"+Seq_1.toString()+" "+Seq_1.get_firstItem().get_minsup());
        //System.out.println("Second Sequence being compared"+Seq_2.toString()+" "+Seq_2.getlastItem().get_minsup());
        //System.out.println("Same one after dropping?"+" "+ Seq_2.get_all_but_last()+" "+Seq_1.get_all_but_second());
        if( Seq_2.get_all_but_last().compareTo(Seq_1.get_all_but_second())==0){
            // System.out.println("Same one after bing dropped");
            if(Seq_1.get_firstItem().get_minsup().compareTo(Seq_2.getlastItem().get_minsup())<0){
                //System.out.println("Minsup comparisions"+Seq_1.get_firstItem().get_minsup()+" "+Seq_2.getlastItem().get_minsup());
                // System.out.println("Last item of second is greater than the first");
                return true;
            }

        }
        return false;
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        this.candidates.forEach(v -> sb.append(v.toString()).append("\n"));
        return sb.toString();
    }

    public void count_substrings(Sequence obs)
    {
        for(Sequence cand : this.candidates)
        {
            if(obs.is_subsequence(cand))
                cand.incr_count();
        }
    }

    public void prune()
    {
        List<Sequence> pruned_list = new ArrayList<Sequence>();
        for(Sequence s : this.candidates)
        {
            if(s.meets_minimum_support(this.transaction_count))
                pruned_list.add(s);
        }
        this.candidates = pruned_list;
    }
}
