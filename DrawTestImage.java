/**
* Draws the image of a circle made up of differently colored circles. The circles of the middle ring form
* a C of a different color, which can face either up, down, left, or right as specified in the parameters. 
* The overall image is 2 degrees wide when viewed from a specified distance. 
* 
* @author Caroline Danzi
* @version 2017-04-01
*/

import processing.core.PApplet;
import java.util.ArrayList;

public class DrawTestImage {
	
	PApplet parent;
	
	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	private final double RADIANS_PER_DEGREE = 0.0174533;
	private final double CUT_SIZE = 1/6.0;
	
	/**
	* Constructor for use with PApplet
	* @param p the PApplet for displaying the image
	*/
	public DrawTestImage(PApplet p) {
		parent = p;
	}
	
	/**
	* Takes in a list of circles, a list of center colors, a list of test colors, a direction for the opening in the C,
	* the distance the observer is from the screen, and the number of pixels per meter for the display. 
	* Draws a circle made of smaller circles, with a C made of different colored circles. 
	* The cut of the C can face up, down, left, or right and is 1/6 of a degree when viewed at the 
	* specified distance. The overall image is 2 degrees wide when viewed at the specified distance. 
	* 
	* @param circles An ArrayList of Circle objects that gives the location and dimension of all circles in the image
	* @param center An array of colors for the center and outer rings of circles in the image
	* @param test An array of colors for the circles that make up the C 
	* @param dir The direction for the cut in the C 
	* @param distFromScreen The distance the observer is from the screen, in meters 
	* @param pixelsPerMeter The number of pixels in one meter for this screen - take the width in pixels and divide it by the width in meters of the screen 
	*/
	public void drawTestImage(ArrayList<Circle> circles, int[] center, int[] test, Direction dir, double distFromScreen, double pixelsPerMeter) {		
		// To find the width of the image, we use a 2 degree field of view 
		// Width in meters: distance observer is from the screen * tan(1 degree) 
		int pixelsInOneDegree = (int)(distFromScreen*Math.tan(RADIANS_PER_DEGREE)*pixelsPerMeter);
		int imgWidthInPixels = 2*pixelsInOneDegree; 
		parent.size(imgWidthInPixels, imgWidthInPixels);
		
		// Set background color to black
		parent.background(0);

		// Puts the origin at the center of the window rather than
		// the upper-left corner
		parent.scale(parent.width/2);
		parent.translate(1, 1);
		
		// No line, and set ellipses to draw using coordinates for the center
		parent.noStroke();
		parent.ellipseMode(parent.CENTER);
		
		// Use the direction the C opens to figure out the coordinate constraints
		// for the cut, keeping in mind that the cut will be 1/6 of a degree
		// Note: By translating the origin from the upper-left corner to the middle, 
		// y increases down and decreases up from the origin, so that the negative
		// y values are at the top half of the image and the positive y values are at the bottom 
		double xMin, xMax, yMin, yMax; 
		switch(dir) {
			case UP: xMin = -CUT_SIZE; xMax = CUT_SIZE; yMin = -1; yMax = 0; 
			break;
			
			case DOWN: xMin = -CUT_SIZE; xMax = CUT_SIZE; yMin = 0; yMax = 1;
			break;
			
			case LEFT: xMin = -1; xMax = 0; yMin = -CUT_SIZE; yMax = CUT_SIZE;
			break;
			
			case RIGHT: xMin = 0; xMax = 1; yMin = -CUT_SIZE; yMax = CUT_SIZE; 
			break;
			
			default: xMin = 0; xMax = 0; yMin = 0; yMax = 0;
		}
		
		// For each circle, calculate its distance from the center of the image
		// to determine which color to use. The center ring in the image will be
		// colored using randomly selected colors from the test array, and the 
		// other circles will use randomly selected colors from the center array 
		for(Circle c : circles) {
			double d = Math.sqrt(c.x*c.x + c.y*c.y);
			int index;
			// Inner and outer rings should be the center color
			if (d > 2/3.0 || d < 1/3.0) {
				index = (int)(Math.random()*center.length);
				parent.fill(center[index]);
			// The cut of the C should be the center color
			} else if(c.x >= xMin && c.x <= xMax && c.y >= yMin && c.y <= yMax) {
				index = (int)(Math.random()*center.length);
				parent.fill(center[index]);
			// The actual circle should be the test color 
			} else {
				index = (int)(Math.random()*test.length);
				parent.fill(test[index]);
			}
			// Draw the circle 
			parent.ellipse((float)c.x, (float)c.y, 2*(float)c.r, 2*(float)c.r);
		}
	}
}