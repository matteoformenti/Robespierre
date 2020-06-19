package eu.matteoformenti.robespierre;

import eu.matteoformenti.robespierre.geometry.Point;
import eu.matteoformenti.robespierre.geometry.Triangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class STLLoader {

    private final List<Triangle> triangles;

    public STLLoader(String filename) throws FileNotFoundException {
        System.out.println("Parsing facets");
        this.triangles = new ArrayList<>();
        File stl = new File(filename);
        Scanner scanner = new Scanner(stl);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.matches("^\\s*outer loop")) {
                Point p1 = new Point(scanner.nextLine());
                Point p2 = new Point(scanner.nextLine());
                Point p3 = new Point(scanner.nextLine());
                this.triangles.add(new Triangle(p1, p2, p3));
                System.out.print("\r"+triangles.size()+" facets loaded");
            }
        }
        System.out.println("");
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public List<Triangle> getTriangles(double z0, double z1) {
        return triangles.stream().filter(triangle -> triangle.getMinZ() <= z0 && triangle.getMaxZ() >= z1).collect(Collectors.toList());
    }
}
