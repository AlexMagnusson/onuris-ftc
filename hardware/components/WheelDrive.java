package org.firstinspires.ftc.teamcode.hardware.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;


public class WheelDrive extends Component {

    // Static variables

    private static final double TWO_PI = 2.0*Math.PI;

    // Hardware devices
    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor_encoder;  // motor encoder for measuring motor position
    EncoderServo servo;  // includes special encoder for measuring servo position

    // Instance variables

    double drivePower;
    int motorDirection = 1;

    public WheelDrive(DcMotor motor1, DcMotor motor2, CRServo servo, DcMotor servo_encoder,
                      DcMotor motor_encoder) {
        this.motor1 = motor1;
        this.motor2 = motor2;
        this.motor_encoder = motor_encoder;
        this.servo = new EncoderServo(servo, servo_encoder);

        this.motor1.setDirection(DcMotor.Direction.FORWARD);
        this.motor2.setDirection(DcMotor.Direction.FORWARD);
    }

    // Helper functions

    /**
     * Constrains radians to an angle from 0 to TWO_PI
     */
    private double constrainRad(double r) {
        return TWO_PI + (r % TWO_PI);
    }


    // Drive Motors

    public double getMotorPosition() {
        return motor_encoder.getCurrentPosition();
    }

    private void reverseMotorDirection() {
        motorDirection *= -1;
    }
    private void setDrivePower(double power) {
        drivePower = power;
        motor1.setPower(power*motorDirection);
        motor2.setPower(power*motorDirection);
    }


    // Servo

    /**
     * Calculate rotation of the swerve drive servo, in radians (0 - TWO_PI)
     */
    private double currentRotation() {
        double rot = constrainRad(TWO_PI * servo.getCurrentPosition()/8192.);
        if (motorDirection == 1) {
            return rot;
        } else {
            return constrainRad(rot+Math.PI);
        }
    }

    /**
     * The amount of rotation, in radians (-PI to PI), needed to reach a target angle from the current angle
     */
    private double calculateTurn(double current, double target) {
        double diff_ccw = constrainRad(target - current);
        double diff_cw = TWO_PI - diff_ccw;
        double turn;
        if (diff_ccw < diff_cw) {
            turn = diff_ccw;
        } else {
            turn = -diff_cw;
        }
        return turn;
    }

    public void drive(double power, double angle) {
        // Motors
        setDrivePower(power);

        // Servo
        double currentAngle = currentRotation();
        double aboutFaceAngle = constrainRad(currentAngle + Math.PI);

        double turnCurrent = calculateTurn(currentAngle, angle);
        double turnAboutFace = calculateTurn(aboutFaceAngle, angle);

        double realTurn;
        if (Math.abs(turnAboutFace) < Math.abs(turnCurrent)) {
            reverseMotorDirection();
            realTurn = -turnAboutFace;
        } else {
            realTurn = -turnCurrent;
        }
        double servoPower;
        if (Math.abs(realTurn) < 0.5) {
            servoPower = realTurn;
        } else {
            servoPower = (realTurn < 0) ? -1 : 1;
        }
        servoPower = Range.clip(servoPower, -1, 1);
        servo.setPower(servoPower);

    }

    public String toString() {
        String drc = "null";
        if (motorDirection == 1) drc = "forward";
        else if (motorDirection == -1) drc = "reverse";
        return String.format(
                "currentRotation: (%f), motorDirection: (%s), servoPower: (%.2f), drivePower: (%.2f)",
                currentRotation(), drc, servo.getPower(), drivePower
        );
    }

}