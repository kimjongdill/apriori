import java.util.Comparator;

public class Item {
    private String name;
    private Integer count;
    private Double minsup;

    public Item(String name)
    {
        this.name = name;
        count = 0;
        minsup = 0.0;
    }

    public Item set_minsup(Double minsup)
    {
        this.minsup = minsup;
        return this;
    }

    public Item incr_count()
    {
        count++;
        return this;
    }

    public String get_name()
    {
        return this.name;
    }

    public Integer get_count()
    {
        return this.count;
    }

    public Double get_minsup()
    {
        return this.minsup;
    }

    public Integer compare(Item b)
    {
        return 0;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("Item: ");
        sb.append(this.name)
                .append(" Count: ")
                .append(this.count)
                .append(" Minsup: ")
                .append(this.minsup);
        return sb.toString();
    }

    public boolean equals(Item other)
    {
        if(this.name.equals(other.name) && this.count.equals(other.count) && this.minsup.equals(other.minsup) )
            return true;

        return false;
    }

    public int compareTo(Item other)
    {
        return this.minsup.compareTo(other.minsup);
    }
}
