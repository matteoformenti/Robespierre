package eu.matteoformenti.robespierre.geometry;

public class Segment {
    private final Point p1;
    private final Point p2;

    public Segment(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public boolean onPlane(double z0, double z1) {
        return (p1.getZ() >= z0 && p1.getZ() <= z1) && (p2.getZ() >= z0 && p2.getZ() <= z1);
    }

    public boolean intersects(double z0, double z1) {
        return (p1.getZ() <= z0 && p2.getZ() >= z1) || (p2.getZ() <= z0 && p1.getZ() >= z1);
    }

    public Point intersectionPoint(double z) {
        if (p1.getZ() == p2.getZ())
            return null;
        Point p = new Point(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ());
        double t = (z - this.p1.getZ()) / p.getZ();
        double int_x = this.p1.getX() + (t * p.getX());
        double int_y = this.p1.getY() + (t * p.getY());
        System.out.println(t + "\t" + int_x + "\t" + int_y);
        return (int_x <= Math.max(p1.getX(), p2.getX()) &&
                int_x >= Math.min(p1.getX(), p2.getX()) &&
                int_y <= Math.max(p1.getY(), p2.getY()) &&
                int_y >= Math.min(p1.getY(), p2.getY()))
                ? new Point(int_x, int_y, z) : null;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }
}