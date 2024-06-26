public class OrbitalMechanics {
    private static final double TOLERANCE = 1e-6;
    private static final int MAX_ITERATIONS = 100;

    /**
     * <p>Solving Kepler's Equation: M=E−esin(E) for E</p>
     *
     * @param M <p>mean anomaly is a linear measure of time since the last perihelion, related directly to the orbiting body's motion if its orbit were circular. It increases uniformly from 0 to
     * 2π radians over one orbital period (T)</p>
     * @param e <p>e is the eccentricity of the orbit.</p>
     * @return E <p>eccentric anomaly relates the position of a body moving along a circular orbit to the position
     * of a body moving along an elliptical orbit that has the same mean anomaly. It’s calculated from the mean anomaly using Kepler’s Equation</p>
     */
    public static double solveKeplersEquation(double M, double e){
        double E = M; //take an initial guess
        double delta = 0;
        int i = 0;


        do{
            delta = (E - e * Math.sin(E) - M) / (1 - e * Math.cos(E));
            E = E - delta;
            i++;
            if(Math.abs(delta) < TOLERANCE){System.out.println("FOUND");}
            else{System.out.println("REVISION " + i);}
        }while(Math.abs(delta) > TOLERANCE && i < MAX_ITERATIONS);

        return E;
    }

    public static double trueAnomaly(double M, double e){
        double E = solveKeplersEquation(M, e);
        double trueAnomaly = 2 * Math.atan2(Math.sqrt(1+e) * Math.sin(E/2), Math.sqrt(1-e) * Math.cos(E/2));
        return trueAnomaly;
    }

}
