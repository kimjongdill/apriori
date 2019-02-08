import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static void check_file(File f)
    {
        if(!f.isFile())
        {
            StringBuilder sb = new StringBuilder("File: ").append(f.getName()).append(" Not found!");
            System.err.println(sb.toString());
            System.exit(1);
        }
        return;
    }

    private static List<Sequence> initial_frequent_itemset(Items i, Integer n)
    {
        List<Item> l = i.toList();
        List<Sequence> t = new ArrayList<>();
        for(Item item : l)
        {
            Double support = item.get_count().doubleValue() / n.doubleValue();
            if(support > item.get_minsup())
                t.add(new Sequence(new String("<{" + item.get_name() + "}>"), i));
        }

        return t;
    }

    private static String format_output(List<Sequence> t)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Number of length ")
                .append(t.get(0).getLength())
                .append(" Sequences: ")
                .append(t.size())
                .append("\n");
        t.forEach( e -> sb.append(e.toString()).append("\n"));
        return sb.toString();
    }

    public static void main(String args[])
    {
        String sequence_file = "C:\\Data_Mining_2\\resources\\data-1.txt";
        String parameter_file = "C:\\Data_Mining_2\\resources\\para1-1.txt";
        File sequences;
        File parameters;
        //System.out.println("Args"+args);

        sequences = new File(sequence_file);
        parameters = new File(parameter_file);
        check_file(sequences);
        check_file(parameters);

        // Load all given items and Minimum Support
        Items items = new Items(parameters);
        // Cycle through sequences file, build a list of Items and frequency.
        Sequences sequence_set = new Sequences(sequences, items);

        // Begin MS-GSP Algorithm
        List<Candidates> candidate_rounds = new ArrayList<>();
        candidate_rounds.add(new Candidates(items, sequence_set.get_transaction_count()));

        System.out.println(candidate_rounds.get(0).toString());


        candidate_rounds.add(new Candidates(items, candidate_rounds.get(0)));
        System.out.println(candidate_rounds.get(1).toString());
        candidate_rounds.add(new Candidates(items, candidate_rounds.get(1)));


        //candidate_rounds.add(new Candidates(items, candidate_rounds.get(2).getFinalCandidates()));
        //candidate_rounds.add(new Candidates(items, candidate_rounds.get(3).getFinalCandidates()));
    }

}