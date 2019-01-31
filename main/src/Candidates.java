import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Candidates {

    // The length of sequences in this Candidate set
    private Integer length;
    private Map<String, List<Sequence>> candidates;

    // Generate the initial list
    public Candidates(Items i, Integer n)
    {
        candidates = new HashMap<>();
        this.length = 1;
        List<Item> l = i.toList();
        for(Item item : l)
        {
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
