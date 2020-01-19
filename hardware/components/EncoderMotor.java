package org.firstinspires.ftc.teamcode.hardware.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class EncoderMotor {

    // The motor reference to which this corresponds.
    private DcMotor motor;

    // The PID controller which this motor uses to stabilize itself.
    public final ErrorResponder errorResponder;

    // The number of encoder ticks it takes this motor to rotate 360 degrees once.
    private final int ENCODER_TICKS_PER_REVOLUTION;

    /**
     * The wheel circumference which this motor drives (public so that SwomniModule
     * can look at this to know how much to correct by)
     */
    public final double WHEEL_CIRCUMFERENCE;


    // The desired velocity of the motor in meters per second.
    private double desiredVelocity = 0;
    private double observedVelocity = 0;
    private double currentPower = 0;
    private double lastDistanceMoved = 0;
    private long lastAdjustmentTime = 0;


    public EncoderMotor(DcMotor motor,
                         ErrorResponder motorErrorResponse,
                         int encoderTicksPerRevolution, double wheelDiameterCM) {
        this.motor = motor;

        errorResponder = motorErrorResponse;

        ENCODER_TICKS_PER_REVOLUTION = encoderTicksPerRevolution;
        WHEEL_CIRCUMFERENCE = wheelDiameterCM * Math.PI;
    }


    // -------------------- GETTER METHODS --------------------

    /**
     * Every motor has a different number of encoder ticks per revolution and a distinct wheel
     * circumference, so this just takes that into account while calculating distance traversed.
     * Returns: distance moved by the motor, in centimeters
     */
    public double currentDistanceMoved() {
        return ((double)motor.getCurrentPosition() / ENCODER_TICKS_PER_REVOLUTION) * WHEEL_CIRCUMFERENCE;
    }

    public double getPower() {
        return currentPower;
    }


    // -------------------- SETTER METHODS --------------------

    private void setPower() {
        currentPower = Range.clip(currentPower, -1, 1);
        motor.setPower(currentPower);
        lastAdjustmentTime = System.nanoTime();
    }
    private void setPower(double power) {
        currentPower = power;
        setPower();
    }

    /**
     * Tells this motor the number of revolutions that it should be moving per second.
     * @param velocity the angular velocity of the motor.
     */
    public void setVelocity(double velocity) {
        // Some really quick adjustments we can make.
        if (Math.abs(velocity) < .0001) {
            setPower(0);
            desiredVelocity = 0;
        } else {
            desiredVelocity = velocity;

            if (desiredVelocity < 0 && currentPower > 0) {
                setPower(-.1);
            } else if (desiredVelocity > 0 && currentPower < 0) {
                setPower(.1);
            }
        }
    }


    // -------------------- UPDATE METHOD --------------------

    public void update() {
        // Calculate PID by finding the number of ticks the motor SHOULD have gone minus the amount it actually went.

        errorResponder.setTarget(desiredVelocity);

        double timeElapsed = System.nanoTime() - lastAdjustmentTime;
        double changeInDistanceMoved = currentDistanceMoved() - lastDistanceMoved;
        observedVelocity = changeInDistanceMoved / timeElapsed * 1e9;

        currentPower += errorResponder.performPID(observedVelocity);
        setPower();

        lastDistanceMoved = currentDistanceMoved();
    }
}
