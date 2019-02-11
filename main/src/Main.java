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
        String sequence_file = null;
        String parameter_file = null;
        File sequences;
        File parameters;

        // Parse command line arguments for file names
        for(int i = 0; i < args.length; i++)
        {
            if(args[i].equals("--d"))
            {
                sequence_file = args[++i];
            }
            if(args[i].equals("--p"))
            {
                parameter_file = args[++i];
            }
        }
        // Error check the command line arguments
        if(sequence_file == null || parameter_file == null)
        {
            System.err.println("Usage: java ms_gsp --d <Data File> --p <Parameter File>");
            return;
        }
        // Attempt to open files
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

        Sequence ref;
        while((ref = sequence_set.get_next()) != null)
        {
            candidate_rounds.get(1).count_substrings(ref);
        }
        candidate_rounds.get(1).prune();
        System.out.print(candidate_rounds.get(1).toString());

        candidate_rounds.add(new Candidates(items, candidate_rounds.get(1)));
        System.out.print(candidate_rounds.get(2).toString());
    }

}
