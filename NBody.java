/**************************************
*
* compile: javac -cp .:lib/stdlib.jar NBody.java
* run:     java -cp .:lib/stdlib.jar NBody t dt planets.txt (mac/linux)
* windows: replace "-cp .:" with "-cp .;"
* t = 31,500,000 is about equal to 1 earth year
* 
* java -cp .:lib/stdlib.jar NBody 157788000.0 25000.0 planets.txt
* 5
* 2.500E+11
*  1.4925E+11  -1.0467E+10   2.0872E+03   2.9723E+04   5.9740E+24 earth.gif
* -1.1055E+11  -1.9868E+11   2.1060E+04  -1.1827E+04   6.4190E+23 mars.gif
* -1.1708E+10  -5.7384E+10   4.6276E+04  -9.9541E+03   3.3020E+23 mercury.gif
*  2.1709E+05   3.0029E+07   4.5087E-02   5.1823E-02   1.9890E+30 sun.gif
*  6.9283E+10   8.2658E+10  -2.6894E+04   2.2585E+04   4.8690E+24 venus.gif
**************************************/

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class NBody {
	public static void main(String[] args) throws FileNotFoundException {
		// Initialize final time, time increment, current time, and universal constant values
		double t = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		double ct = 0;
		double g = Double.parseDouble("6.67e-11");

		// Allow third input to be the text file from resources folder
		String resourceFolder = "resources/";
		String fileName = resourceFolder + args[2];
		FileInputStream fileInputStream = new FileInputStream(fileName);
		System.setIn(fileInputStream);

		// Read in all values from text file with StdIn
		int numParticles = Integer.parseInt(StdIn.readString());
		double radius = Double.parseDouble(StdIn.readString());

		// Set up array of all planet images
		String[] allPlanets = new String[numParticles];

		// Initialize planetary values arrays
		Double[][] planets = new Double[numParticles][5];
		int arrayCounter = 0;
		int idxCounter = 0;
		int totalPlanets = 0;
		Double[] current = new Double[4];
		Boolean check = true;

		// Recieve planetary values
		while (arrayCounter < numParticles) {
			String next = StdIn.readString();
			if (next.equals(allPlanets[arrayCounter]) || idxCounter > 4) {
				allPlanets[arrayCounter] = next;
				arrayCounter++;
				idxCounter = 0;
				totalPlanets++;
			}
			else {
				planets[arrayCounter][idxCounter] = Double.parseDouble(next);
				idxCounter++;
			}
		}

		// Set up Standard Draw
		StdDraw.setXscale(-1*radius, radius);
		StdDraw.setYscale(-1*radius, radius);
		StdDraw.enableDoubleBuffering();
		StdAudio.play("resources/2001.wav");

		// Execute the program
		while (ct < t) {
			StdDraw.clear();
			StdDraw.picture(0, 0, "resources/starfield.jpg");

			// Loop through planets
			for (int i = 0; i < totalPlanets; i++) {
				double fx = 0;
				double fy = 0;
				// Calculate forces exerted by every other planet on planet i
				for (int j = 0; j < totalPlanets; j++) {
					double dist = Math.sqrt(Math.pow(planets[j][0]-planets[i][0],2) + Math.pow(planets[j][1]-planets[i][1],2));
					double newfx = 0;
					double newfy = 0;
					if (dist != 0) {
						newfx = (planets[j][0]-planets[i][0])*(g * planets[i][4] * planets[j][4]) / Math.pow(dist,3); 
						newfy = (planets[j][1]-planets[i][1])*(g * planets[i][4] * planets[j][4]) / Math.pow(dist,3); 
					}
					else if (dist == 0) {
						newfx = 0;
						newfy = 0;
					}
					else {
						System.out.println("ERROR, DIST IS NOT AN INTEGER");
					}
					fx += newfx;
					fy += newfy;
				}

				// Calculate accelerations
				double ax = (fx / planets[i][4]);
				double ay = (fy / planets[i][4]);

				// Add accelerations to velocities
				planets[i][2] += (ax * dt);
				planets[i][3] += (ay * dt);
			}

			for (int i = 0; i < totalPlanets; i++) { // Change positions in different loop
				// Add velocities to positions
				planets[i][0] += (planets[i][2] * dt);
				planets[i][1] += (planets[i][3] * dt);

				// Draw planet to screen
				StdDraw.picture(planets[i][0], planets[i][1], "resources/"+allPlanets[i]);
			}
			StdDraw.show();

			// Increase ct towards t
			ct += dt;
		}

		// Print out the results	
		StdOut.println(numParticles);
		StdOut.println(String.format("%6.3E",radius));
		for (int i = 0; i < totalPlanets; i++) {
			StdOut.println(String.format("%12.4E",planets[i][0]) + " " + String.format("%12.4E",planets[i][1]) + " " + String.format("%12.4E",planets[i][2]) + " " + String.format("%12.4E",planets[i][3]) + " " + String.format("%12.4E",planets[i][4]) + " " + allPlanets[i]);
		}
	}
}