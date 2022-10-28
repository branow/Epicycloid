package com.kpi.epicycloid;

import java.math.BigDecimal;

import static java.lang.Math.pow;

public class Logic {

    /**
     * @param A the radius of the fixed circle
     * @param a the radius of the moving circle
     * @param fi the angular in radian
     * @param k the accuracy of sine and cosine measurement, the optimal value is 10
     * @return double[] array of coordinate 0 - x, 1 - y
     * */
    public static double[] getPoint(double A, double a, double fi, int k) throws IllegalArgumentException {
        if (a<0 || k<0) throw new IllegalArgumentException();
        double[] d = {getX(A,a,fi,k), getY(A,a,fi,k)};
        return d;
    }

    /**
     * @return double x
     * */
    private static double getX(double A, double a, double fi, int k) {
        return (A + a)*cos(k, fi) - a*cos( k, (A+a)/a*fi);
    }

    /**
     * @return double x
     * */
    private static double getY(double A, double a, double fi, int k) {
        return (A + a)*sin(k, fi) - a*sin( k, (A+a)/a*fi);
    }

    /**
     * @param x the angular in radian
     * @param k the accuracy of sine, the optimal value is 10Ô
     * @return double the value of sinus
     * */
    public static double sin(int k, double x) {
        while (x>=Math.PI) x-=Math.PI*2;
        while (x<=-Math.PI) x+=Math.PI*2;
        return sinOf(k, x);
    }

    /**
     * @param x the angular in radian
     * @param k the accuracy of cosine, the optimal value is 10
     * @return double the value of cosine
     * */
    public static double cos(int k, double x) {
        while (x>=Math.PI*2) x-=Math.PI*2;
        while (x<0) x+=Math.PI*2;
        return cosOf(k, x);
    }

    private static double sinOf(int k, double x) {
        if(k<0) return 0;
        double div = pow(x, 2*k+1)/fact(new BigDecimal(2L *k+1)).doubleValue();
        return pow(-1, k)*div + sinOf(k-1, x);
    }

    private static double cosOf(int k, double x) {
        if(k<0) return 0;
        double div = pow(x, 2*k)/fact(new BigDecimal(2L *k)).doubleValue();
        return pow(-1, k)*div + cosOf(k-1, x);
    }

    /**
     * @param v number
     * @return long the value of factorial
     * */
    public static BigDecimal fact(BigDecimal v) {
        if (v.compareTo(new BigDecimal(1))<=0) return new BigDecimal(1);
        return v.multiply(fact(v.subtract(new BigDecimal(1))));
    }

}
