package lists;

class EfficientIntLinkedList implements IntList {
    private IntNode head;
    private IntNode tail;  // Added tail pointer for O(1) append
    private int len;

    public EfficientIntLinkedList() {
        head = null;
        tail = null;
        len = 0;
    }

    public boolean contains(int value) {
        IntNode current = head;
        while (current != null) {
            if (current.value == value) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // O(1) append operation
    public void append(int value) {
        IntNode newNode = new IntNode(value);
        if (head == null) {
            // If list is empty, set both head and tail to new node
            head = newNode;
            tail = newNode;
        } else {
            // Add new node after tail and update tail
            tail.next = newNode;
            tail = newNode;
        }
        len++;
    }

    public int length() {
        return len;
    }

    public static void main(String[] args) {
        EfficientIntLinkedList list = new EfficientIntLinkedList();
        list.append(1);
        list.append(2);
        list.append(3);
        System.out.println(list.contains(2));  // true
        System.out.println(list.contains(4));  // false
        System.out.println(list.length());     // 3
    }
}