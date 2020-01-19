package org.firstinspires.ftc.teamcode.hardware.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class EncoderServo {
    // For Continuous Rotation servos using an external encoder connected to a motor

    private static final double TWO_PI = 2.0*Math.PI;

    // Hardware devices
    private CRServo servo;
    private DcMotor encoder;

    // The number of encoder ticks it takes this motor to rotate 360 degrees once.
    private final int ENCODER_TICKS_PER_REVOLUTION;

    // The PID controller which this servo uses to stabilize itself.
    private ErrorResponder errorResponder;


    // The desired velocity of the motor in meters per second.
    private double targetTurn = 0;
    private double observedTurn = 0;
    private double currentPower = 0;
    private double lastRadiansMoved = 0;
    private long lastAdjustmentTime = 0;

    public EncoderServo(CRServo servo, DcMotor encoder,
                        ErrorResponder servoErrorResponse, int encoderTicksPerRevolution) {
        this.encoder = encoder;
        this.servo = servo;
        this.servo.setDirection(CRServo.Direction.FORWARD);

        errorResponder = servoErrorResponse;
        errorResponder.setContinuous();
        errorResponder.setInputRange(0, TWO_PI);
        errorResponder.setTolerance(0.01);

        ENCODER_TICKS_PER_REVOLUTION = encoderTicksPerRevolution;
    }

    // -------------------- ANGLE FUNCTIONS --------------------

    // Constrains radians to an angle from 0 to TWO_PI
    private double constrainAngle(double a) {
        return a % TWO_PI;
    }


    // -------------------- GETTER METHODS --------------------

    // Distance moved, in radians
    public double currentRadiansMoved() {
        return ((double)encoder.getCurrentPosition() / ENCODER_TICKS_PER_REVOLUTION) * TWO_PI;
    }
    public double currentAngle() {
        return constrainAngle(currentRadiansMoved());
    }

    public boolean onTargetBy(double threshold) {
        return errorResponder.getError() <= threshold;
    }


    // -------------------- SETTER METHODS --------------------

    private void setPower() {
        currentPower = Range.clip(currentPower, -1, 1);
        servo.setPower(currentPower);
        lastAdjustmentTime = System.nanoTime();
    }
    private void setPower(double power) {
        currentPower = power;
        setPower();
    }


    public void setTargetTurn(double turn) {
        targetTurn = turn;
    }


    // -------------------- UPDATE METHOD --------------------

    // Calculate PID by finding the number of ticks the servo SHOULD have gone minus the amount it actually went.
    public void update() {
        errorResponder.setTarget(targetTurn);

        double timeElapsed = System.nanoTime() - lastAdjustmentTime;
        double changeInRadiansMoved = currentRadiansMoved() - lastRadiansMoved;
        observedTurn = changeInRadiansMoved / timeElapsed;

        currentPower += errorResponder.performPID(observedTurn);
        setPower();

        lastRadiansMoved = currentRadiansMoved();
    }

}
