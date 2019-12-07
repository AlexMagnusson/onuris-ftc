
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.components.StiltComponent;
import org.firstinspires.ftc.teamcode.hardware.components.Vector;


abstract public class AbstractAutoMode extends AbstractOpMode
{

    static final double TWO_PI = 2*Math.PI;

    int currentStage = 1;
    int waiting = 0;

    double initialRightPos = 0;
    double initialLeftPos = 0;

    double currentRightPos = 0;
    double currentLeftPos = 0;


    double targetHeading = Math.PI;

    double driveX;
    double driveY;
    double rotate;
    Vector stiltMode;

    public double constrainRad(double r) {
        return TWO_PI + (r % TWO_PI);
    }

    public double currentOffsetLeft() {
        return Math.abs(currentLeftPos-initialLeftPos);
    }

    public double currentOffsetRight() {
        return Math.abs(currentRightPos-initialRightPos);
    }

    public void nextStage() {
        initialRightPos = currentRightPos;
        initialLeftPos = currentLeftPos;
        currentStage += 1;
        waiting = 20;
    }


}
