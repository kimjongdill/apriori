import java.io.File;

class Sequences {
    private Parser p;
    private static boolean first;
    private int transaction_count;
    private Items items;

    public Sequences(File f, Items i)
    {
        transaction_count = 0;
        this.p = new Parser(f);
        items = i;
        init();
    }

    private void init()
    {
        String s;
        Sequence t;
        while((s = p.get_next_line()) != null)
        {
            if(s.length() < 4)
                continue;
            transaction_count++;
            t = new Sequence(s, items);
            t.get_unique_items().forEach( i -> i.incr_count());
        }
    }


    public Sequence get_next()
    {
        while(true) {
            String line = p.get_next_line();

            if (line == null)
                return null;

            if (line.length() < 4)
                continue;

            return new Sequence(line, items);
        }
    }

    public int get_transaction_count()
    {
        return transaction_count;
    }


}
