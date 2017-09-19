package suza.project.wackyballs.util;

/**
 * Class representing a vector object.
 *
 * Created by lmark on 12/09/2017.
 */

public class Vector {

    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    public void subtract(Vector vector) {
        this.x = this.x - vector.x;
        this.y = this.x - vector.x;
    }

    public void div(double val) {
        this.x /= val;
        this.y /= val;
    }

    public void mul(double val) {
        this.x *= val;
        this.y *= val;
    }
    public double norm() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public static Vector normalize(Vector vector) {
        double norm = vector.norm();
        return new Vector(vector.x / norm, vector.y / norm);
    }

    public static double scalarProduct(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    public static Vector add(Vector v1, Vector v2) {
        return new Vector(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vector subtract(Vector v1, Vector v2) {
        return new Vector(v1.x - v2.x, v1.y - v2.y);
    }

    public double getAmplitude() {
        return  Math.sqrt(Math.pow(x, 2) + Math.pow(y,2));
    }

    public static double getAmplitude(double x, double y) {
        return  Math.sqrt(Math.pow(x, 2) + Math.pow(y,2));
    }

    public static double getPhase(double x, double y) {
        double phase = Math.atan2(y, x);
        return phase > 0 ? phase : phase + 2* Math.PI;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
