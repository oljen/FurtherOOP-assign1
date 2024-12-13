package reflection.uml;

import java.util.*;
import java.lang.reflect.*;
import reflection.uml.ReflectionData.*;

public class ProcessClasses {

    List<Link> getSuperclasses(Class<?> c, List<Class<?>> javaClasses) {
        List<Link> links = new ArrayList<>();
        Class<?> superClass = c.getSuperclass();

        // Add superclass link if it exists and is in our list of classes
        if (superClass != null && !superClass.equals(Object.class) && javaClasses.contains(superClass)) {
            links.add(new Link(c.getSimpleName(), superClass.getSimpleName(), LinkType.SUPERCLASS));
        }

        // Add interface links
        for (Class<?> iface : c.getInterfaces()) {
            if (javaClasses.contains(iface)) {
                links.add(new Link(c.getSimpleName(), iface.getSimpleName(), LinkType.SUPERCLASS));
            }
        }

        return links;
    }

    ClassType getClassType(Class<?> c) {
        if (c.isInterface()) {
            return ClassType.INTERFACE;
        } else if (Modifier.isAbstract(c.getModifiers())) {
            return ClassType.ABSTRACT;
        } else {
            return ClassType.CLASS;
        }
    }

    List<FieldData> getFields(Class<?> c) {
        List<FieldData> fields = new ArrayList<>();
        for (java.lang.reflect.Field f : c.getDeclaredFields()) {
            fields.add(new FieldData(f.getName(), f.getType().getSimpleName()));
        }
        return fields;
    }

    List<MethodData> getMethods(Class<?> c) {
        List<MethodData> methods = new ArrayList<>();

        for (Method m : c.getDeclaredMethods()) {
            // Skip synthetic methods
            if (m.isSynthetic()) continue;

            // Get return type
            String returnType = m.getReturnType().getSimpleName();

            // Create MethodData with just name and return type
            methods.add(new MethodData(m.getName(), returnType));
        }

        return methods;
    }

    List<Link> getFieldDependencies(Class<?> c, List<Class<?>> javaClasses) {
        List<Link> links = new ArrayList<>();

        for (Field field : c.getDeclaredFields()) {
            Class<?> fieldType = field.getType();

            // Handle array types
            if (fieldType.isArray()) {
                fieldType = fieldType.getComponentType();
            }

            // Add dependency if the field type is in our list of classes
            if (javaClasses.contains(fieldType)) {
                links.add(new Link(c.getSimpleName(), fieldType.getSimpleName(), LinkType.DEPENDENCY));
            }
        }

        return links;
    }

    List<Link> getMethodDependencies(Class<?> c, List<Class<?>> javaClasses) {
        Set<Link> links = new HashSet<>(); // Use Set to avoid duplicates

        for (Method m : c.getDeclaredMethods()) {
            // Check return type
            Class<?> returnType = m.getReturnType();
            if (javaClasses.contains(returnType)) {
                links.add(new Link(c.getSimpleName(), returnType.getSimpleName(), LinkType.DEPENDENCY));
            }

            // Check parameter types
            for (Class<?> paramType : m.getParameterTypes()) {
                if (javaClasses.contains(paramType)) {
                    links.add(new Link(c.getSimpleName(), paramType.getSimpleName(), LinkType.DEPENDENCY));
                }
            }
        }

        return new ArrayList<>(links);
    }

    DiagramData process(List<Class<?>> javaClasses) {
        // we're going to process the classes here to build up the class data
        List<ClassData> classData = new ArrayList<>();
        Set<Link> links = new HashSet<>();

        for (Class<?> c : javaClasses) {
            String className = c.getSimpleName();
            ClassType classType = getClassType(c);
            List<FieldData> fields = getFields(c);
            List<MethodData> methods = getMethods(c);
            classData.add(new ClassData(className, classType, fields, methods));
            links.addAll(getSuperclasses(c, javaClasses));
            links.addAll(getFieldDependencies(c, javaClasses));
            links.addAll(getMethodDependencies(c, javaClasses));
        }
        return new DiagramData(classData, links);
    }


    public static void main(String[] args) {
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
        System.out.println(dd);
        System.out.println();
        for (ClassData cd : dd.classes()) {
            System.out.println(cd);
        }
        System.out.println();
        for (Link l : dd.links()) {
            System.out.println(l);
        }
    }
}
