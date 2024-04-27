public class OrbitalMechanics {
    private static final double TOLERANCE = 1e-6;
    private static final int MAX_ITERATIONS = 100;

    /**
     * <p>Solving Kepler's Equation: M=E−esin(E) for E</p>
     *
     * @param M <p>mean anomaly is a linear measure of time since the last perihelion, related directly to the orbiting body's motion if its orbit were circular. It increases uniformly from 0 to
     * 2π radians over one orbital period (T)</p>
     * @param e <p>e is the eccentricity of the orbit.</p>
     * @return
     */
    public static double solveKeplersEquation(double M, double e){
        double E = M; //take an initial guess
        double delta = 0;
        int i = 0;


        do{
            delta = (E - e * Math.sin(E) - M) / (1 - e * Math.cos(E));
            System.out.println("Current E: " + E);
            i++;
        }while(Math.abs(delta) > TOLERANCE && i < MAX_ITERATIONS);

        return E;
    }

    public static double trueAnomaly(double M, double e){
        double E = solveKeplersEquation(M, e);
        double trueAnomaly = 2 * Math.atan2(Math.sqrt(1+e) * Math.sin(E/2), Math.sqrt(1-e) * Math.cos(E/2));
        return trueAnomaly;
    }

    public static void main(String[] args){
        double meanAnomaly = Math.toRadians(67);
        double eccentricity = 0.0167; //earth's eccentricity

        double nu = trueAnomaly(meanAnomaly, eccentricity);
        System.out.println("True Anomaly: " + Math.toDegrees(nu) + " degrees");
    }
}
