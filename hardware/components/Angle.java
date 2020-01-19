package org.firstinspires.ftc.teamcode.hardware.components;

class Angle {

    static final double TWO_PI = 2.0*Math.PI;


    // -------------------- ANGLE FUNCTIONS --------------------

    // Constrains radians to an angle from 0 to TWO_PI
    static double constrain(double a) {
        return a % TWO_PI;
    }
    // Add 180 degrees (pi radians) to an angle
    static double flip(double a) {
        return constrain(a + Math.PI);
    }


    /**
     * The amount of rotation, in radians (-PI to PI), needed to reach a target angle from the current angle
     */
    static double calculateTurn(double current, double target) {
        double ccw = constrain(target - current);  // amount if you go counterclockwise
        double cw = TWO_PI - ccw;  // amount if you go clockwise
        if (ccw < cw) {
            return ccw;
        } else {
            return -cw;
        }
    }

}
