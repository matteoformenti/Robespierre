package eu.matteoformenti.robespierre.geometry;

public class Point {
    private double x;
    private double y;
    private double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(String vertex) {
        vertex = vertex.replaceFirst("^\\s*vertex\\s*", "");
        String[] vertices = vertex.split("\\s");
        this.x = Double.parseDouble(vertices[0]);
        this.y = Double.parseDouble(vertices[1]);
        this.z = Double.parseDouble(vertices[2]);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public String toString() {
        return String.format("%.2f\t%.2f\t%.2f", this.x, this.y, this.z);
    }
}
