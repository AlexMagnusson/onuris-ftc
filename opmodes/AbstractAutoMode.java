
package org.firstinspires.ftc.teamcode.opmodes;

abstract public class AbstractAutoMode extends AbstractOpMode
{

    protected int stage = 1;
    protected boolean waiting = false;

    // Time elapsed in seconds when started waiting
    private double waitingStartTime = 0;

    // Distance moved when the current stage started
    private double initialDistanceMoved = 0;
    // Time elapsed in seconds when the current stage started
    private double startTime = 0;


    // -------------------- GETTER METHODS --------------------

    public double getCurrentDistanceMoved() {
        return Math.abs(robot.swerveDrive.currentDistanceMoved() - initialDistanceMoved);
    }

    public double getTimeElapsed() {
        // in seconds
        return getRuntime() - startTime;
    }

    public double timeSpentWaiting() {
        return getRuntime() - waitingStartTime;
    }


    // -------------------- FUNCTIONS --------------------

    public void nextStage() {
        stage += 1;

        initialDistanceMoved = robot.swerveDrive.currentDistanceMoved();
        startTime = getRuntime();

        startWaiting();
    }

    public void startWaiting() {
        waitingStartTime = getRuntime();
        waiting = true;
    }
    public void stopWaiting() {
        waiting = false;
    }



}
