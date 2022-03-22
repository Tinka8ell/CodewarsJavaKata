public class BouncingBall {

    /**
     * A child is playing with a ball on the nth floor of a tall building.
     * The height of this floor, h, is known.
     * He drops the ball out of the window. The ball bounces (for example),
     * to two-thirds of its height (a bounce of 0.66).
     * His mother looks out of a window 1.5 meters from the ground.
     * How many times will the mother see the ball pass in front of her window (including when it's falling and bouncing?
     *
     * Three conditions must be met for a valid experiment:
     * Float parameter "h" in meters must be greater than 0
     * Float parameter "bounce" must be greater than 0 and less than 1
     * Float parameter "window" must be less than h.
     *
     * If all three conditions above are fulfilled, return a positive integer, otherwise return -1.
     *
     * @param h - height in meters must be greater than 0
     * @param bounce - fraction of height each bounce must be greater than 0 and less than 1
     * @param window - height of viewing position must be less than h
     * @return number of times it will pass the window or -1 if invalid input
     */
    public static int bouncingBall(double h, double bounce, double window) {
        int bounces = -1;
        if ( (h > 0) &&
                (bounce >= 0) &&
                (bounce < 1) &&
                (window < h) ) {
            while (h > window){
                bounces +=2; // makes 1 first time, adds 2 (up and down) after that
                h *= bounce;
            }
        }

        return bounces;
    }
}