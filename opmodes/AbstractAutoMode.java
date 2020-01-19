
package org.firstinspires.ftc.teamcode.opmodes;

abstract public class AbstractAutoMode extends AbstractOpMode
{

    protected int stage = 1;
    protected boolean waiting = false;

    public void resetInitials() {
        initialDistanceMoved = robot.swerveDrive.currentDistanceMoved();
        initialTimeElapsed = getRuntime();
    }

    public void nextStage() {
        stage += 1;
        startWaiting();
    }

    public void startWaiting() {
        resetInitials();
        waiting = true;
    }
    public void stopWaiting() {
        waiting = false;
        resetInitials();
    }

    // Distance moved since current stage started
    private double initialDistanceMoved = 0;
    // Time elapsed in seconds since current stage started
    private double initialTimeElapsed = 0;

    public double getCurrentDistanceMoved() {
        return Math.abs(robot.swerveDrive.currentDistanceMoved() - initialDistanceMoved);
    }

    public double getTimeElapsed() {
        // in seconds
        return getRuntime() - initialTimeElapsed;
    }



}
