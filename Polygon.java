
/**
 * This class represents description a Convex polygon.
 *
 * @author (Hadassa Kazhav)
 * @version (10/12/20)
 */
/**
 * Represents Convex polygon.
 * In a convex polygon the connecting line any two points from the polygon pass only within
 * and all of the interior angles are less than 180 degrees
 * The representation is made by an array that keeps the list of vertices of the polygon in order Their appearance in the polygon.
 */
public class Polygon {

    // Instance variables:
    private Point[]_vertices;
    private int _noOfVertices;

    // Constant:
    final int MAX_VERTICES_POLYGON = 10;

    // Constructor:
    /**
     * Constructor for objects of class Polygon.
     * Initialising an empty array of Points with a default maximum size of 10 points.
     */
    public Polygon() {
        _vertices = new Point[MAX_VERTICES_POLYGON];
        _noOfVertices = 0;
    }

    // Public methods:
    /**
     * Adding a new vertex to the polygon.
     * @param x The X value of the new vertex 
     * @param y The Y value of the new vertex 
     * @return True if the vertex added successfully, 
     * and false if not vertex added,because the number of vertices in the polygon has maximum vertices 
     */
    public boolean addVertex(double x, double y) {
        if (_noOfVertices == MAX_VERTICES_POLYGON) 
            return false; 

        _vertices[_noOfVertices++] = new Point(x, y);//calling point copy constructor to avoid aliasing
        return true;
    }

    /**
     * Returns the highest vertex of the polygon (the vertex which has the biggest Y value).
     * In case of 2 "highest" vertices - the first one to be found is the one to be return.
     * @return new point the highest vertex of the polygon, or null in case the polygon has no vertices.
     */
    public Point highestVertex() {
        if (_noOfVertices == 0)  
            return null; 

        Point highest = new Point(_vertices[0]);//calling point copy constructor to avoid aliasing

        // Find the highest vertex
        for (int i = 1; i < _noOfVertices; i++) {
            if (_vertices[i].isAbove(highest)) {
                highest = new Point(_vertices[i]);
            }
        }
        return new Point(highest);
    }

    /**
     * Returns a string representation of Polygon in the format
     * ((vertex's x, vertex's y),(next vertex's x, next vertex's y)).
     * @return a string representation of the polygon
     */
    public String toString() {
        String base = "The polygon has ";
        if (_noOfVertices == 0) {
            base += _noOfVertices + " vertices.";
            return base;
        }

        base += _noOfVertices + " vertices:\n";
        String verticesStr = "(";

        // For FencePost reasons,which will be printed last, outside the loop, without comma.
        for (int i = 0; i < _noOfVertices - 1; i++) {
            verticesStr += _vertices[i].toString() + ",";
        }
        verticesStr += _vertices[_noOfVertices - 1].toString() + ")";

        return base + verticesStr;
    }

    /**
     * Calculating the perimeter of the polygon.
     * @return The perimeter of the polygon at double number.
     */
    public double calcPerimeter() {
        double perimeter = 0.0;

        if (_noOfVertices == 0 || _noOfVertices == 1) {
            // if there are 0 vertices or 1 vertices , return null
            return 0;
        }
        else if (_noOfVertices == 2) {
            // if there are only 2 vertices, return the distance between them
            return _vertices[0].distance(_vertices[1]);
        }

        // Calculating the distance between every vertex 
        for (int i = 0; i < _noOfVertices - 1; i++) {
            perimeter += _vertices[i].distance(_vertices[i + 1]);
        }
        perimeter += _vertices[_noOfVertices - 1].distance(_vertices[0]);

        return perimeter;
    }

    /**
     * Calculating the area of the Polygon.
     * @return The area of the polygon at double number, or 0 if the polygon has less than 3 vertices.
     */
    public double calcArea() {
        if (_noOfVertices < 3) 
            return 0; 

        // the lengths of the 3 sides of the triangles that is represented by the 3 vertices 
        double area = 0.0;
        for (int i = 2; i <= _noOfVertices-1; i++) {
            double segABLength = _vertices[0].distance(_vertices[i]);
            double segBCLength = _vertices[i].distance(_vertices[i-1]);
            double segCALength = _vertices[i-1].distance(_vertices[0]);
            
            // in order to use Heron formula, use the half of the triangle perimeter
            double triPerimeter = segABLength + segBCLength + segCALength;
            double s = triPerimeter / 2.0;

            // Heron formula:
            double triArea =+ Math.sqrt(s * (s - segABLength) * (s - segBCLength) * (s - segCALength));
            area += triArea;
        }
        return area; 
    }

    /**
     * Check if this polygon is bigger than a reference polygon.
     * @param other the reference polygon
     * @return True if this polygon is bigger than the reference polygon, false if smaller.
     */
    public boolean isBigger(Polygon other) {
        double curArea = this.calcArea();
        double otherArea = other.calcArea();
        if (curArea > otherArea)
        {
            return true;
        }
        return false;
    }

    /**
     * Check if a given point is a vertex of the polygon and return index.
     * @param p The reference point to be checked
     * @return The Point index if it is a vertex of the polygon, and -1 if isn't.
     */
    public int findVertex(Point p) {

        if (_noOfVertices == 0) 
            return -1; 

        for (int i = 0; i < _noOfVertices; i++){
            if (_vertices[i].equals(p)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returning the next vertex of the polygon for to a given vertex if exist.
     * @param p point which represents a vertex of the polygon.
     * @return null if the given point is not a vertex of the polygon. 
     * if the given point is not a vertex of the polygon.
     * if the given point is the only vertex of the polygon return the first (and only) vertex.  
     * if the given point is the last vertex of the polygon - return the first vertex.
     */
    public Point getNextVertex(Point p) {
        
        if (_noOfVertices == 0) 
            return null; 

        //if the given point is not a vertex of the polygon    
        if (findVertex(p) == -1)
            return null; 

        // if the given point is the only vertex of the polygon:
        // returning the first (and only) vertex.    
        if (_noOfVertices == 1 && findVertex(p) == 0) 
            return new Point(_vertices[0]);

        // if the given point is the last vertex of the polygon - return the first vertex.
        if (findVertex(p) == _noOfVertices - 1) 
            return new Point(_vertices[0]); 

        int next = findVertex(p) + 1;
        return new Point(_vertices[next]);
    }

    /**
     * Calculating the bounding box (rectangle) of this polygon, and return it as a bounding box (has 4 vertices).
     * @return The bounding box of this polygon as a Polygon object, or null if this polygon has less than 3 vertices.
     */
    public Polygon getBoundingBox() {
        if (_noOfVertices < 3)  
            return null; 

        //create the bounding box, need values for the four corners:    
        double maxY;
        double minY;
        double maxX;
        double minX;

        maxY = highestVertex().getY();

        //find the lowest vertex,same to the highest method:
        Point lowest = new Point(_vertices[0]);
        for (int i = 1; i < _noOfVertices; i++) {
            if (lowest.isAbove(_vertices[i])) {
                lowest = new Point(_vertices[i]);
            }
        }
        minY = lowest.getY();

        //ind the most right (biggest X value)
        Point biggestX = new Point(_vertices[0]);
        for (int i = 1; i < _noOfVertices; i++) {
            if (biggestX.isLeft(_vertices[i])) {
                biggestX = new Point(_vertices[i]);
            }
        }
        maxX = biggestX.getX();

        //find the most left (smallest X value)
        Point smallestX = new Point(_vertices[0]);
        for (int i = 0; i < _noOfVertices; i++) {
            if (smallestX.isRight(_vertices[i])) {
                smallestX = new Point(_vertices[i]);
            }
        }
        minX = smallestX.getX();

        // Creating the bounding box and adding the vertices
        Polygon boundingBox = new Polygon();
        boundingBox.addVertex(minX, minY); //first - bottom left
        boundingBox.addVertex(maxX, minY); //second - bottom right
        boundingBox.addVertex(maxX, maxY); //third - upper right
        boundingBox.addVertex(minX, maxY); //fourth - upper left 

        return boundingBox;
    }
}