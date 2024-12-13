package lists;


import java.util.Iterator;

public class OddRange implements Iterable<Integer> {
    private final int start;
    private final int end;

    public OddRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new OddRangeIterator(start, end);
    }

    private static class OddRangeIterator implements Iterator<Integer> {
        private int current;
        private int end;

        public OddRangeIterator(int start, int end) {
            if (start % 2 == 0) {
                this.current = start + 1;
            } else {
                this.current = start;
            }

            this.end = end;
        }

        @Override
        public boolean hasNext() {
            // implement this properly
            return current < end;
        }

        @Override
        public Integer next() {
            // implement this properly
            int r = current;
            current += 2;
            return r;
        }
    }

    public static void main(String[] args) {
        OddRange range = new OddRange(-6, 5);
        for (int num : range) {
            System.out.println(num);
        }
    }
}
