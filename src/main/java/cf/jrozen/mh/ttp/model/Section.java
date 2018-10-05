package cf.jrozen.mh.ttp.model;

public class Section {

    public final int index;
    public final double profit;
    public final double weight;
    public final int assignedNodeNumber;

    public Section(int index, double profit, double weight, int assignedNodeNumber) {
        this.index = index;
        this.profit = profit;
        this.weight = weight;
        this.assignedNodeNumber = assignedNodeNumber;
    }
}
