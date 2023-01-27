public class Pair <U, V>{
    private final U u;
    private final V v;

    public U getU() {
        return u;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "u=" + u +
                ", v=" + v +
                '}';
    }

    public V getV() {
        return v;
    }

    public Pair(U u, V v) {
        this.u = u;
        this.v = v;
    }
}
