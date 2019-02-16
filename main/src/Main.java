import java.io.*;
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

    public static void main(String args[]) throws IOException{
        String sequence_file = null;
        String parameter_file = null;
        File sequences;
        File parameters;
        PrintWriter writer;

        // Parse command line arguments for file names
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--d")) {
                sequence_file = args[++i];
            }
            if (args[i].equals("--p")) {
                parameter_file = args[++i];
            }
        }
        // Error check the command line arguments
        if (sequence_file == null || parameter_file == null) {
            System.err.println("Usage: java ms_gsp --d <Data File> --p <Parameter File>");
            return;
        }
        // Attempt to open files
        sequences = new File(sequence_file);
        parameters = new File(parameter_file);

        writer = new PrintWriter(new FileWriter("output.txt"));
        check_file(sequences);
        check_file(parameters);

        // Load all given items and Minimum Support
        Items items = new Items(parameters);
        // Cycle through sequences file, build a list of Items and frequency.
        Sequences sequence_set = new Sequences(sequences, items);

        // Begin MS-GSP Algorithm
        Candidates previous_candidates = new Candidates(items, sequence_set.get_transaction_count());
        Candidates current_candidates = new Candidates(items, previous_candidates);

        // Print the stage 1 and stage 2 stuff here
        Sequence ref;
        while ((ref = sequence_set.get_next()) != null){
            current_candidates.count_substrings(ref);
            previous_candidates.count_substrings(ref);
        }
        current_candidates.trim_infrequent();
        previous_candidates.trim_infrequent();
        writer.println("Number of Length 1 Frequency Sentences: " + previous_candidates.size());
        writer.println(previous_candidates.toString());
        writer.println("Number of Length 2 Frequency Sentences: " + current_candidates.size());
        writer.println(current_candidates.toString());

        previous_candidates = current_candidates;

        Integer length = 3;
        while (previous_candidates.size() > 0)
        {
            current_candidates = new Candidates(items, previous_candidates);

            while ((ref = sequence_set.get_next()) != null) {
                current_candidates.count_substrings(ref);
            }

            current_candidates.trim_infrequent();
            if(current_candidates.size() > 0) {
                writer.println("Number of Length " + length + " Frequency Sentences: " + current_candidates.size());
                writer.println(current_candidates.toString());
            }

            length++;
            previous_candidates = current_candidates;
        }

        writer.close();
    }

}
