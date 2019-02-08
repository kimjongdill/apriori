import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Candidates {

    // The length of sequences in this Candidate set
    private Integer length;
    private Map<String, List<Sequence>> candidates;
    private List<Sequence> final_candidates;
    // Generate the initial list
    public Candidates(Items i, Integer n)
    {
        candidates = new HashMap<>();
        final_candidates=new ArrayList<>();
        this.length = 1;
        List<Item> l = i.toList();

        for(Item item : l)
        {
            System.out.println("List of Items"+item.get_name()+" "+item.get_count()+" "+item.get_minsup());
            Double support = item.get_count().doubleValue() / n.doubleValue();
            if(support > item.get_minsup()) {
                Sequence s = new Sequence(new String("<{" + item.get_name() + "}>"), i);
                List<Sequence> list = new ArrayList<>();
                list.add(s);
                candidates.put(s.get_first(), list);
            }
        }
    }

    public Candidates(Items i, Candidates previous_round)
    {
        candidates = new HashMap<>();
        final_candidates=new ArrayList<>();
        length = previous_round.length + 1;
        List<Sequence> prev_seq = previous_round.get_all_sequence();
        if(length == 2)
        {
            for(Sequence first : prev_seq){

                for(Sequence second : prev_seq){

                    List<Sequence> list = new ArrayList<>();
                    Sequence s = new Sequence(
                            new String("<{" + first.get_first() + "}{" + second.get_first() + "}>"), i);
                    list.add(s);
                    if(! first.equals(second)){
                        Sequence s2 = new Sequence(
                                new String("<{" + first.get_first() + " " + second.get_first() + "}>"), i);
                        list.add(s2);
                    }
                    if(candidates.containsKey(s.get_all_but_last())){
                        candidates.get(s.get_all_but_last()).addAll(list);
                    }
                    else
                    {
                        candidates.put(s.get_all_but_last(), list);
                    }

                }
            }
        }
        else{///From a length of 2 into length of 3
            CandidateGeneration(previous_round);
        }

    }
    public Candidates (Items i,List<Sequence> previous_round){//Used to get cnadidate from a length 3 and above
        final_candidates=new ArrayList<>();
        length = previous_round.get(0).getLength()+1;
        CandidateGeneration(previous_round);
    }
    //Length of 2 produces length 3 candidates
    private void CandidateGeneration(Candidates previous_round){


        //candidates = new HashMap<>();
        length = previous_round.length + 1;
        List<Sequence> prev_seq = previous_round.get_all_sequence();
        System.out.println("INside Candidate Generation "+prev_seq.toString());
        for(int counter_1=0;counter_1<prev_seq.size();++counter_1){//Forward Process of Comaparing Sequences
            int a=1;
            while((counter_1+a)<prev_seq.size()){//Forward Process of Comaparing Sequences
                System.out.println("First Sequence"+prev_seq.get(counter_1).toString()+"Second Sequence"+" "+prev_seq.get(counter_1+a).toString());
                if (MISLeast_First(prev_seq.get(counter_1))&& (prev_seq.get(counter_1).compareTo_2(prev_seq.get(counter_1+a))!=0)){
                    // System.out.println("First has the Least");
                    if(potentialJoin(prev_seq.get(counter_1),prev_seq.get(counter_1+a))){

                        Join(prev_seq.get(counter_1),prev_seq.get(counter_1+a));
                    }
                }
                else if(MISLeast_Second(prev_seq.get(counter_1+a))   &&   (prev_seq.get(counter_1+a).compareTo_2(prev_seq.get(counter_1))!=0)){
                    System.out.println("Second has the Least--Reverse");
                    if(potentialJoin_Reverse(prev_seq.get(counter_1),prev_seq.get(counter_1+a))){
                        Join_Reverse(prev_seq.get(counter_1),prev_seq.get(counter_1+a));

                    }
                }
                else{
                    // oldJoin()
                }

                ++a;
            }

        }
        Reverse_Comparison( prev_seq);


    }

    private void CandidateGeneration(List<Sequence> previous_round){//Length of 3 or above
        List<Sequence> prev_seq=previous_round;
        System.out.println("INside Candidate Generation-Length of 3 "+prev_seq.toString());
        for(int counter_1=0;counter_1<prev_seq.size();++counter_1){//Forward Process of Comaparing Sequences
            int a=1;
            while((counter_1+a)<prev_seq.size()){//Forward Process of Comaparing Sequences
                //System.out.println("First Sequence+3 or above"+prev_seq.get(counter_1).toString()+"Second Sequence"+" "+prev_seq.get(counter_1+a).toString());
                if (MISLeast_First(prev_seq.get(counter_1))&& (prev_seq.get(counter_1).compareTo_2(prev_seq.get(counter_1+a))!=0)){
                    // System.out.println("First has the Least 3 or above");
                    if(potentialJoin(prev_seq.get(counter_1),prev_seq.get(counter_1+a))){
                        //System.out.println("potential join successful");
                        Join(prev_seq.get(counter_1),prev_seq.get(counter_1+a));
                    }
                }
                else if(MISLeast_Second(prev_seq.get(counter_1))   &&   (prev_seq.get(counter_1+a).compareTo_2(prev_seq.get(counter_1))!=0)){
                    System.out.println("Second has the Least--Reverse");
                    if(potentialJoin_Reverse(prev_seq.get(counter_1),prev_seq.get(counter_1+a))){
                        Join_Reverse(prev_seq.get(counter_1),prev_seq.get(counter_1+a));
                    }
                }
                else{
                    // oldJoin()
                }

                ++a;
            }

        }
        Reverse_Comparison( prev_seq);

    }
    private void Reverse_Comparison(List<Sequence> prev_seq){//ITerates backwards for Comparison Process
        System.out.println("INside Reverse Comparison"+prev_seq.toString());
        for (int counter_1=(prev_seq.size()-1);counter_1>0;--counter_1){
            int a=1;
            while((counter_1-a)>=0){
                System.out.println("First Sequence"+prev_seq.get(counter_1).toString()+"Second Sequence"+" "+prev_seq.get(counter_1-a).toString());
                if (MISLeast_First(prev_seq.get(counter_1))&& (prev_seq.get(counter_1).compareTo_2(prev_seq.get(counter_1-a))!=0)){
                    // System.out.println("First has the Least");
                    if(potentialJoin(prev_seq.get(counter_1),prev_seq.get(counter_1-a))){

                        Join(prev_seq.get(counter_1),prev_seq.get(counter_1-a));
                    }
                }
                else if(MISLeast_Second(prev_seq.get(counter_1))   &&   (prev_seq.get(counter_1-a).compareTo_2(prev_seq.get(counter_1))!=0)){
                    System.out.println("Second has the Least--Reverse");
                    if(potentialJoin_Reverse(prev_seq.get(counter_1),prev_seq.get(counter_1-a))){
                        Join_Reverse(prev_seq.get(counter_1),prev_seq.get(counter_1-a));
                    }
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

        if (S2.getLastSet().size() == 1) {//last element is a single item


            System.out.println("Last elemet is a single element");
            Sequence S_Joined=new Sequence(S1,S2,true);

            System.out.println("Joined Sequence"+" "+S_Joined.toString());
            this.final_candidates.add(S_Joined);
          //PrintFinalCandidates();
            if(S1.getLength()==2 && S1.get_Size()==2 &&
                    (S2.getlastItem().get_minsup()>S1.getlastItem().get_minsup())){
                Sequence S_Joined_1=new Sequence(S1,S2,false);
                System.out.println("Also Join-Joined Sequence"+" "+S_Joined_1.toString());
                this.final_candidates.add(S_Joined_1);
               //PrintFinalCandidates();

            }
                //the length and the size of s1 are both 2) AND (the last item of s2 is
                    //greater than the last item of s1


        } else {//last elememt contains more than one item
            System.out.println("Last element contains several items");
            System.out.println("COMPARING Last Items" +" "+S2.getlastItem().get_name() +" "+S1.getlastItem().get_name());
            if (((S1.get_Size() == 1 && S1.getLength() == 2) && (S2.getlastItem().get_minsup()>S1.getlastItem().get_minsup()))

                    || (S1.getLength() > 2)) {
                    Sequence S_Joined_3=new Sequence(S1,S2,false);
                this.final_candidates.add(S_Joined_3);
                System.out.println("Joined Sequence-PROBLEM"+" "+S_Joined_3.toString());
                //PrintFinalCandidates();

            }

            //candidates.put(s.get_all_but_last(), list)
        }

    }
    private void Join_Reverse(Sequence S1,Sequence S2){
        System.out.println("JoiningReverse" + S1.toString());
        System.out.println("JoiningReverse" + S2.toString() );

        Item i=S1.get_firstItem();
         if (S1.getFirstSet().size()==1){

             Sequence S_Joined=new Sequence(i,S2,true);

             System.out.println("Joined SequenceReverse"+" "+S_Joined.toString());
             this.final_candidates.add(S_Joined);
             if(S2.getLength()==2 && S2.get_Size()==2 && S1.get_firstItem().get_minsup()> S2.get_firstItem().get_minsup()){

                 Sequence S_Joined_1=new Sequence(i,S2,false);
                 System.out.println("Joined SequenceReverse 2"+" "+S_Joined_1.toString());
                 this.final_candidates.add(S_Joined_1);
             }
         }
         else{
             if(S2.getLength()==2 && S2.get_Size()==1 && S1.get_firstItem().get_minsup()> S2.get_firstItem().get_minsup()){
                // Sequence c2 = s2.getClone();
                // c2.m_ItemSets.get(0).addItem(l);
                // newFS.AddSequenceWithoutDup(c2);
                 Sequence S_Joined_3=new Sequence(i,S2,false);
                 System.out.println("Joined SequenceReverse 3"+" "+S_Joined_3.toString());
                 this.final_candidates.add(S_Joined_3);
             }

         }
    }
    public List<Sequence> getFinalCandidates(){
        return this.final_candidates;
    }
    private void PrintFinalCandidates(){
        System.out.println("INSIDE PRINTING UPDATED FINAL CAIDDIATES.");

        for (Sequence seq:this.final_candidates){
            System.out.println(seq.toString());
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
        //System.out.println("First Sequence being compared"+Seq_1.toString()+" "+Seq_1.get_firstItem().get_minsup());
        //System.out.println("Second Sequence being compared"+Seq_2.toString()+" "+Seq_2.getlastItem().get_minsup());
       // System.out.println("Same one after dropping?"+" "+Seq_1.get_all_but_second()+" "+ Seq_2.get_all_but_last());
        if( Seq_2.get_all_but_last().compareTo(Seq_1.get_all_but_second())==0){
            //System.out.println("Same one after bing dropped");
            if(Seq_1.get_firstItem().get_minsup().compareTo(Seq_2.getlastItem().get_minsup())<0){
                //System.out.println("Minsup comparisions"+Seq_1.get_firstItem().get_minsup()+" "+Seq_2.getlastItem().get_minsup());
                // System.out.println("Last item of second is greater than the first");
                return true;
            }

        }
        return false;
    }
    private boolean potentialJoin_Reverse(Sequence Seq_1, Sequence Seq_2){
       //Second seq ignores second to last item
              //  first seq ignorees first item
        if(Seq_2.get_all_but_secondtoLast().compareTo(Seq_1.get_all_butFirst())==0){

            if (Seq_1.get_firstItem().get_minsup()>Seq_2.getlastItem().get_minsup()){
                        return true;
            }
        }
        return false;

    }
    public List<Sequence> get_all_sequence()
    {
        List<Sequence> list = new ArrayList<>();
        this.candidates.forEach((k, v) -> v.forEach( s -> list.add(s)));
        list.sort(null);
        return list;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        this.candidates.forEach((k, v) -> v.forEach( s -> sb.append(s.toString()).append("\n")));
        return sb.toString();
    }


}
