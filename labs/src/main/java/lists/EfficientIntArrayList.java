package lists;

public class EfficientIntArrayList implements IntList {
    // Instance variables
    private int[] values;      // The internal array that stores our elements
    private int len;          // The number of elements actually in the list (not the array capacity)

    // Constants for array management
    private static final int INITIAL_CAPACITY = 10;   // Start with room for 10 elements
    private static final int GROWTH_FACTOR = 2;       // Double the size when we need more room


    public EfficientIntArrayList() {
        values = new int[INITIAL_CAPACITY];  // Allocate initial array
        len = 0;                            // No elements yet
    }


    public boolean contains(int value) {

        for (int i = 0; i < len; i++) {
            if (values[i] == value) {
                return true;
            }
        }
        return false;
    }


    public void append(int value) {

        if (len == values.length) {
            resize();  // Grow the array
        }


        values[len] = value;
        len++;
    }


    private void resize() {

        int newCapacity = values.length == 0 ? INITIAL_CAPACITY : values.length * GROWTH_FACTOR;


        int[] newValues = new int[newCapacity];


        for (int i = 0; i < len; i++) {
            newValues[i] = values[i];
        }


        values = newValues;


    }


    public int length() {
        return len;
    }

    /**
     * Utility method to get the current capacity of the internal array.
     * Useful for testing and understanding the resizing behavior.
     */
    public int capacity() {
        return values.length;
    }

    /**
     * Main method to demonstrate and test the ArrayList behavior
     */
    public static void main(String[] args) {
        EfficientIntArrayList list = new EfficientIntArrayList();

        // Add 20 elements to demonstrate resizing behavior
        for (int i = 0; i < 20; i++) {
            list.append(i);
            // Print current state after each append
            System.out.println("After adding " + i +
                    ": Length = " + list.length() +
                    ", Capacity = " + list.capacity());
        }

        /* Expected output will show:
           - Initial capacity of 10
           - When we hit capacity, it doubles to 20
           - Length grows one at a time
           - Capacity grows in jumps
        */
    }
}