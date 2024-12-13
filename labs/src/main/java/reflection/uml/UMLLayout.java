package reflection.uml;

import java.util.*;
import reflection.uml.ReflectionData.*;

public class UMLLayout {

    // Record to store the layout of each class
    public record ClassLayout(double centerX, double centerY, double width, double height) {}

    private final double classWidth = 200.0;        // Base width for class boxes
    private final double heightFac = 50.0;          // Height factor for each member
    private final double verticalSpacing = 300.0;   // Much larger vertical gap
    private final double horizontalSpacing = 400.0; // Much larger horizontal gap

    public Map<String, ClassLayout> calculateLayout(DiagramData diagram) {
        Map<String, ClassLayout> layout = new HashMap<>();
        List<String> topologicalSortedClasses = topologicalSort(diagram);
        Map<String, Integer> depthMap = calculateDepths(topologicalSortedClasses, diagram);

        // Calculate width requirements for each level
        Map<Integer, List<String>> levelMap = new HashMap<>();
        for (String className : topologicalSortedClasses) {
            int depth = depthMap.get(className);
            levelMap.computeIfAbsent(depth, k -> new ArrayList<>()).add(className);
        }

        // Find the maximum depth
        int maxDepth = levelMap.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);

        // Calculate total width needed for each level
        Map<Integer, Double> levelWidths = new HashMap<>();
        for (Map.Entry<Integer, List<String>> entry : levelMap.entrySet()) {
            double width = entry.getValue().size() * (classWidth + horizontalSpacing);
            levelWidths.put(entry.getKey(), width);
        }

        // Calculate the maximum level width
        double maxWidth = levelWidths.values().stream().mapToDouble(Double::doubleValue).max().orElse(0);

        // Position classes
        Map<String, ClassData> classDataMap = new HashMap<>();
        for (ClassData classData : diagram.classes()) {
            classDataMap.put(classData.className(), classData);
        }

        for (int depth = 0; depth <= maxDepth; depth++) {
            List<String> classesAtDepth = levelMap.get(depth);
            if (classesAtDepth == null) continue;

            // Center the classes at this depth
            double totalWidthAtDepth = classesAtDepth.size() * classWidth +
                    (classesAtDepth.size() - 1) * horizontalSpacing;
            double startX = (maxWidth - totalWidthAtDepth) / 2;

            for (int i = 0; i < classesAtDepth.size(); i++) {
                String className = classesAtDepth.get(i);
                ClassData classData = classDataMap.get(className);

                double x = startX + i * (classWidth + horizontalSpacing);
                double y = depth * (verticalSpacing + heightFac);
                double height = heightFac * (1 + classData.fields().size() + classData.methods().size());

                layout.put(className, new ClassLayout(
                        x + classWidth / 2,
                        y + height / 2,
                        classWidth,
                        Math.max(height, 100.0)  // Ensure minimum height
                ));
            }
        }

        return layout;
    }


    // Perform topological sort to order classes based on the hierarchy
    private List<String> topologicalSort(DiagramData diagram) {
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, List<String>> adjList = new HashMap<>();

        // Initialize the adjacency list and in-degree count
        for (ClassData classData : diagram.classes()) {
            inDegree.put(classData.className(), 0);
            adjList.put(classData.className(), new ArrayList<>());
        }

        for (Link link : diagram.links()) {
            if (link.type() == LinkType.SUPERCLASS) {
                adjList.get(link.from()).add(link.to());
                inDegree.put(link.to(), inDegree.get(link.to()) + 1);
            }
        }

        // Kahn's algorithm for topological sorting
        Queue<String> queue = new LinkedList<>();
        for (String className : inDegree.keySet()) {
            if (inDegree.get(className) == 0) {
                queue.add(className);
            }
        }

        List<String> sortedClasses = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            sortedClasses.add(current);

            for (String neighbor : adjList.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        return sortedClasses;
    }

    // Build a map from classes to their direct subclasses
    private Map<String, List<String>> buildChildrenMap(DiagramData diagram) {
        Map<String, List<String>> childrenMap = new HashMap<>();

        for (ClassData classData : diagram.classes()) {
            childrenMap.put(classData.className(), new ArrayList<>());
        }

        for (Link link : diagram.links()) {
            if (link.type() == LinkType.SUPERCLASS) {
                childrenMap.get(link.to()).add(link.from());
            }
        }

        return childrenMap;
    }

    // Calculate the depth (vertical level) for each class in the hierarchy
    private Map<String, Integer> calculateDepths(List<String> sortedClasses, DiagramData diagram) {
        Map<String, Integer> depthMap = new HashMap<>();

        for (String className : sortedClasses) {
            depthMap.put(className, 0); // Root classes have depth 0
        }

        for (String className : sortedClasses) {
            for (Link link : diagram.links()) {
                if (link.type() == LinkType.SUPERCLASS && link.from().equals(className)) {
                    depthMap.put(link.to(), depthMap.get(className) + 1);
                }
            }
        }

        return depthMap;
    }

    public static void main(String[] args) {
        // Create a list of classes to generate UML for
        List<Class<?>> classes = new ArrayList<>();
        // add in all the classes we wish to generate UML for
        classes.add(MyShape.class);
        classes.add(MyCircle.class);
        classes.add(MyCircle.InnerStatic.class);
        classes.add(MyEllipse.class);
        classes.add(Connector.class);
        System.out.println(classes);
        System.out.println();
        DiagramData dd = new ProcessClasses().process(classes);

        UMLLayout umlLayout = new UMLLayout();
        Map<String, ClassLayout> layout = umlLayout.calculateLayout(dd);
        for (Map.Entry<String, ClassLayout> entry : layout.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}