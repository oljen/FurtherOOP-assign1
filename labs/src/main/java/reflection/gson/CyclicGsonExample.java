package reflection.gson;

import com.google.gson.Gson;
import java.util.HashSet;
import java.util.Set;

class MyObject {
    MyObject ref;
    String name;

    MyObject(String name) {
        this.name = name;
    }

    void setRef(MyObject ref) {
        this.ref = ref;
    }
}

public class CyclicGsonExample {

    public static class CyclicGraphException extends RuntimeException {
        public CyclicGraphException() {
            super("Gson cannot serialize cyclic graph");
        }
    }

    private static void detectCycle(MyObject obj, Set<MyObject> visited) {
        if (obj == null) return;

        if (visited.contains(obj)) {
            throw new CyclicGraphException();
        }

        visited.add(obj);
        detectCycle(obj.ref, visited);
        visited.remove(obj);
    }

    public static String serializeObject(MyObject obj) {
        // Check for cycles before attempting serialization
        detectCycle(obj, new HashSet<>());

        // If no cycles are detected, proceed with serialization
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static void main(String[] args) {
        // Non-cyclic case
        MyObject obj1 = new MyObject("obj1");
        MyObject obj2 = new MyObject("obj2");
        obj1.setRef(obj2);

        try {
            String json = serializeObject(obj1);
            System.out.println("Non-cyclic serialization: " + json);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Cyclic case
        obj2.setRef(obj1);

        try {
            String json = serializeObject(obj1);
            System.out.println("Cyclic serialization: " + json);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}