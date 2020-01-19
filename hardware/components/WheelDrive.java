package org.firstinspires.ftc.teamcode.hardware.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;


public class WheelDrive {

    private static final double TWO_PI = 2.0*Math.PI;

    // The orientation at which it's OK to drive the module.
    private static final double DRIVING_OK_THRESHOLD = Math.PI/4;

    // Hardware devices
    DcMotor motor1;
    DcMotor motor2;

    // motor encoder for measuring motor position;
    // must be one of the swerve drive motors (motor1 and motor2)
    EncoderMotor encoderMotor;

    // servo that includes special encoder for measuring servo position
    EncoderServo servo;


    double driveVelocity;
    double servoTurn;
    int motorDirection = 1;

    public WheelDrive(DcMotor motor1, DcMotor motor2, CRServo servo, DcMotor servoEncoder,
                      DcMotor encoderMotor) {
        this.motor1 = motor1;
        this.motor2 = motor2;
        this.motor1.setDirection(DcMotor.Direction.FORWARD);
        this.motor2.setDirection(DcMotor.Direction.FORWARD);

        this.encoderMotor = new EncoderMotor(
                encoderMotor,
                new ErrorResponder(.05, 0, 0),
                200, 3);
        this.servo = new EncoderServo(
                servo, servoEncoder,
                new ErrorResponder(.003, .00003, 0),
                8192);
    }

    // -------------------- ANGLE FUNCTIONS --------------------

    // Constrains radians to an angle from 0 to TWO_PI
    private double constrainAngle(double a) {
        return a % TWO_PI;
    }
    // Add 180 degrees (pi radians) to an angle
    private double flipAngle(double a) {
        return constrainAngle(a + Math.PI);
    }


    // -------------------- MOTOR FUNCTIONS --------------------

    private void reverseMotorDirection() {
        motorDirection *= -1;
    }

    // Set both drive motors to the power determined by the encoderMotor
    private void setDrivePower() {
        double realPower = encoderMotor.getPower();
        motor1.setPower(realPower);
        motor2.setPower(realPower);
    }


    // -------------------- SERVO FUNCTIONS --------------------

    // Calculate angle of swerve drive servo, in radians (0 - TWO_PI)
    private double currentAngle() {
        double angle = servo.currentAngle();
        if (motorDirection == 1) {
            return angle;
        } else {
            return flipAngle(angle);
        }
    }

    /**
     * The amount of rotation, in radians (-PI to PI), needed to reach a target angle from the current angle
     */
    private double calculateTurn(double current, double target) {
        double ccw = constrainAngle(target - current);  // turn needed if you go counterclockwise
        double cw = TWO_PI - ccw;  // turn needed if you go clockwise
        if (ccw < cw) {
            return ccw;
        } else {
            return -cw;
        }
    }

    // -------------------- DRIVE --------------------

    public void control(double velocity, double angle) {
        driveVelocity = 0;
        // Only start driving when servo has rotated near enough to correct heading
        if (servo.onTargetBy(DRIVING_OK_THRESHOLD))
            driveVelocity = velocity;


        double currentAngle = currentAngle();
        double aboutFaceAngle = flipAngle(currentAngle);

        double turnCurrent = calculateTurn(currentAngle, angle);
        double turnAboutFace = calculateTurn(aboutFaceAngle, angle);

        if (Math.abs(turnAboutFace) < Math.abs(turnCurrent)) {
            reverseMotorDirection();
            servoTurn = -turnAboutFace;
        } else {
            servoTurn = -turnCurrent;
        }
    }

    public void drive() {
        encoderMotor.setVelocity(driveVelocity);
        encoderMotor.update();
        setDrivePower();

        servo.setTargetTurn(servoTurn);
        servo.update();
    }

    public String toString() {
        String drc = "null";
        if (motorDirection == 1) drc = "forward";
        else if (motorDirection == -1) drc = "reverse";
        return String.format(
                "currentAngle: (%f), motorDirection: (%s), servoTurn: (%.2f), driveVelocity: (%.2f)",
                currentAngle(), drc, servoTurn, driveVelocity
        );
    }

}