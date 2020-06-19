package eu.matteoformenti.robespierre;

import eu.matteoformenti.robespierre.geometry.Point;
import eu.matteoformenti.robespierre.geometry.Segment;
import eu.matteoformenti.robespierre.geometry.Triangle;
import org.apache.commons.cli.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class Robespierre {

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "path to the ASCII stl file");
        options.addOption("o", "output-dir", true, "Output directory, default: layers");
        options.addOption("l", "layer-height", true, "Layer height, default: 0.5");
        options.addOption("s", "scale-factor", true, "Scale factor (pixel per mm), default: 10");

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine line = parser.parse(options, args);
            STLLoader loader = new STLLoader(line.getOptionValue("i"));

            double minZ = 0, maxZ = 0, minX = 0, maxX = 0, minY = 0, maxY = 0;
            double layer_height = Double.parseDouble(line.getOptionValue("l", "0.5"));
            int output_scaling = Integer.parseInt(line.getOptionValue("s", "10"));

            minZ = loader.getTriangles().get(0).getMinZ();
            maxZ = loader.getTriangles().get(0).getMaxZ();
            minX = loader.getTriangles().get(0).getMinX();
            maxX = loader.getTriangles().get(0).getMaxX();
            minY = loader.getTriangles().get(0).getMinY();
            maxY = loader.getTriangles().get(0).getMaxY();

            for (Triangle t : loader.getTriangles()) {
                minZ = Math.min(t.getMinZ(), minZ);
                maxZ = Math.max(t.getMaxZ(), maxZ);
                minX = Math.min(t.getMinX(), minX);
                maxX = Math.max(t.getMaxX(), maxX);
                minY = Math.min(t.getMinY(), minY);
                maxY = Math.max(t.getMaxY(), maxY);
            }

            int layer_count = (int) Math.ceil(maxZ / layer_height);
            ArrayList<ArrayList<Segment>> layers = new ArrayList<>();

            for (int i = 0; i <= layer_count; i++) {
                System.out.print("\rSlicing layer " + i + " of " + layer_count);
                ArrayList<Segment> layer = new ArrayList<>();
                double z0 = i * layer_height;
                double z1 = (i + 1) * layer_height;
                for (Triangle t : loader.getTriangles(z0, z1)) {
                    if (t.onPlane(z0, z1)) {
                        layers.get(i).addAll(Arrays.asList(t.getSegments()));
                    } else {
                        ArrayList<Point> uniquePoints = new ArrayList<>();
                        for (Segment s : t.getSegments()) {
                            Point intersection = s.intersectionPoint(z0);
                            if (intersection != null && uniquePoints.stream().noneMatch(point -> point.getX() == intersection.getX() && point.getY() == intersection.getY())) {
                                uniquePoints.add(intersection);
                            }
                        }
                        if (uniquePoints.size() > 2) {
                            throw new Exception("More than two points intersects the cutting plane");
                        } else {
                            try {
                                layer.add(new Segment(uniquePoints.get(0), (uniquePoints.size() == 2) ? uniquePoints.get(1) : uniquePoints.get(0)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                layers.add(layer);
            }
            System.out.println("\nLayer generation complete");

            new File(line.getOptionValue("o", "layers")).mkdir();
            for (ArrayList<Segment> layer : layers) {
                System.out.print("\rPrinting layer " + layers.indexOf(layer) + " of " + layer_count);
                BufferedImage layer_image = new BufferedImage((int) Math.ceil((Math.abs(minX) + Math.abs(maxX) + 10) * output_scaling), (int) Math.ceil((Math.abs(minY) + Math.abs(maxY) + 10) * output_scaling), BufferedImage.TYPE_INT_RGB);
                for (Segment s : layer) {
                    layer_image.getGraphics().drawLine(
                            (int) (s.getP1().getX() + Math.abs(minX) + 5) * output_scaling,
                            (int) (s.getP1().getY() + Math.abs(minY) + 5) * output_scaling,
                            (int) (s.getP2().getX() + Math.abs(minX) + 5) * output_scaling,
                            (int) (s.getP2().getY() + Math.abs(minY) + 5) * output_scaling);
                }
                ImageIO.write(layer_image, "bmp", new File(line.getOptionValue("o", "layers") + "/layer" + layers.indexOf(layer) + ".bmp"));
            }
            System.out.println("\n");
        } catch (Exception e) {
            e.printStackTrace();
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar Robespierre -i [input-file]",
                    "Robespierre is a simple slicing software, it accepts ASCII encoded STL files and outputs a scaled pixel representation of each layer as bmp images", options, "You will lose your mind");
        }
    }
}
