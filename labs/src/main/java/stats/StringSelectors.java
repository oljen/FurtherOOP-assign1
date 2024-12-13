package stats;

public class StringSelectors {
    // implement convenience methods for selecting strings
    public Selector<String> longestString() {
        return new Selector<>((s1, s2) -> Integer.compare(s1.length(), s2.length()));
    }

    // Method for selecting the shortest string
    public Selector<String> shortestString() {
        return new Selector<>((s1, s2) -> Integer.compare(s2.length(), s1.length()));
    }
}


