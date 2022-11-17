package engine;

/**
 *
 */

public class Vector {
    public double x;
    public double y;

    // create the zero vector of length N
    public Vector(double _x, double _y) {
        x = _x;
        y = _y;
    }

    // return the inner product of this Vector a and b
    public double dot(Vector that) {
        double sum = 0.0;

        sum = sum + (this.x*that.x);
        sum = sum + (this.y*that.y);
        return sum;
    }

    // return the Euclidean norm of this Vector
    public double magnitude() {
        return Math.sqrt(this.dot(this));
    }

    // return the Euclidean distance between this and that
    public double distanceTo(Vector that) {
        return this.minus(that).magnitude();
    }

    // return this + that
    public Vector plus(Vector that) {
        Vector c = new Vector(x, y);

        c.x = this.x + that.x;
        c.y = this.y + that.y;
        return c;
    }

    // return this - that
    public Vector minus(Vector that) {
        Vector c = new Vector(x, y);

        c.x = this.x - that.x;
        c.y = this.y - that.y;
        return c;
    }

	/* Don't think I need this
	// return the corresponding coordinate
	public double cartesian(int i) {
	    return data[i];
	}
	*/

    // create and return a new object whose value is (this * factor)
    public Vector times(double factor) {
        Vector c = new Vector(x, y);

        c.x = factor * this.x;
        c.y = factor * this.y;
        return c;
    }


    // return the corresponding unit vector
    public Vector direction() {
        if (this.magnitude() == 0.0) throw new RuntimeException("Zero-vector has no direction");
        return this.times(1.0 / this.magnitude());
    }

    public Vector normalize() {
        Vector c = new Vector(x, y);

        c.x = this.x / this.magnitude();
        c.y = this.y / this.magnitude();

        return c;
    }

    public static Vector Normalize(Vector v) {
        Vector c = new Vector(v.x, v.y);

        c.x = v.x / v.magnitude();
        c.y = v.y / v.magnitude();

        return c;
    }

    // return a string representation of the vector
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("(");
        s.append(this.x);
        s.append(", ");
        s.append(this.y);
        s.append(")");
        return s.toString();
    }
}