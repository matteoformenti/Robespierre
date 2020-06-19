package eu.matteoformenti.robespierre.geometry;

import java.util.Arrays;
import java.util.Collections;

public class Triangle {
    private Point p1;
    private Point p2;
    private Point p3;

    private double minZ = Double.NaN;
    private double maxZ = Double.NaN;
    private double minX = Double.NaN;
    private double maxX = Double.NaN;
    private double minY = Double.NaN;
    private double maxY = Double.NaN;

    public Triangle(Point pointA, Point pointB, Point pointC) {
        this.p1 = pointA;
        this.p2 = pointB;
        this.p3 = pointC;

        this.minZ = Collections.min(Arrays.asList(this.p1.getZ(), this.p2.getZ(), this.p3.getZ()));
        this.maxZ = Collections.max(Arrays.asList(this.p1.getZ(), this.p2.getZ(), this.p3.getZ()));
        this.minX = Collections.min(Arrays.asList(this.p1.getX(), this.p2.getX(), this.p3.getX()));
        this.maxX = Collections.max(Arrays.asList(this.p1.getX(), this.p2.getX(), this.p3.getX()));
        this.minY = Collections.min(Arrays.asList(this.p1.getY(), this.p2.getY(), this.p3.getY()));
        this.maxY = Collections.max(Arrays.asList(this.p1.getY(), this.p2.getY(), this.p3.getY()));
    }

    public Segment[] getSegments() {
        return new Segment[]{
                new Segment(this.p1, this.p2),
                new Segment(this.p1, this.p3),
                new Segment(this.p2, this.p3),
        };
    }

    public boolean onPlane(double z0, double z1) {
        return (p1.getZ() >= z0 && p1.getZ() <= z1) && (p2.getZ() >= z0 && p2.getZ() <= z1) && (p3.getZ() >= z0 && p3.getZ() <= z1);
    }

    public double getMaxZ() {
        return maxZ;
    }

    public double getMinZ() {
        return minZ;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }
}
