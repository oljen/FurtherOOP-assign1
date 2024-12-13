package lists;

public class IntArrayList implements IntList {
    int[] values;
    int len;

    public IntArrayList() {
        values = new int[0];
        len = 0;
    }

    // Correct implementation of contains method
    public boolean contains(int value) {
        for (int i = 0; i < len; i++) {
            if (values[i] == value) {
                return true;
            }
        }
        return false;
    }

    // Append method is unchanged
    public void append(int value) {
        int[] newValues = new int[len + 1];
        for (int i = 0; i < len; i++) {
            newValues[i] = values[i];
        }
        newValues[len] = value;
        values = newValues;
        len++;
    }

    // Correct implementation of length method
    public int length() {
        return len;
    }
}
