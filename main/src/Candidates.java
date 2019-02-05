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
