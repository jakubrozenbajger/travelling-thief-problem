package cf.jrozen.mh.ttp.model;

public class Node {
    public final int index;
    public final double x;
    public final double y;

    public Node(int index, double x, double y) {
        this.index = index;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Node{" +
                "index=" + index +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
