import java.io.File;

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


    public static void main(String args[])
    {
        String transaction_file = null;
        String parameter_file = null;
        File transactions;
        File parameters;

        // Parse command line arguments for file names
        for(int i = 0; i < args.length; i++)
        {
            if(args[i].equals("--d"))
            {
                transaction_file = args[++i];
            }
            if(args[i].equals("--p"))
            {
                parameter_file = args[++i];
            }
        }
        // Error check the command line arguments
        if(transaction_file == null || parameter_file == null)
        {
            System.err.println("Usage: java ms_gsp --d <Data File> --p <Parameter File>");
            return;
        }
        // Attempt to open files
        transactions = new File(transaction_file);
        parameters = new File(parameter_file);
        check_file(transactions);
        check_file(parameters);

        Transactions T = new Transactions(transactions);
        Transaction s;

        System.out.println("First Time");
        while((s = T.get_next_transaction()) != null)
        {
            System.out.println(s.toString());
        }

    }

}
