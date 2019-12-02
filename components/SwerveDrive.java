package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SwerveDrive extends Component {

    private final double TWO_PI = 2*Math.PI;
    private final int FORWARD = 0;
    private final int REVERSE = 1;


    DcMotor motor1;  // Motor with encoder
    DcMotor motor2;
    CRServo servo;
    DcMotor servo_encoder;
    DcMotor motor_encoder;

    double drivePower;
    double servoPower;
    int motorDirection = FORWARD;

    public SwerveDrive(DcMotor motor1, DcMotor motor2, CRServo servo, DcMotor servo_encoder,
                       DcMotor motor_encoder) {
        this.motor1 = motor1;
        this.motor2 = motor2;
        this.servo_encoder = servo_encoder;
        this.motor_encoder = motor_encoder;
        this.servo = servo;

        this.servo.setDirection(CRServo.Direction.FORWARD);
        this.motor1.setDirection(DcMotor.Direction.FORWARD);
        this.motor2.setDirection(DcMotor.Direction.FORWARD);
    }

    public double getMotorPosition() {
        return motor_encoder.getCurrentPosition();
    }

    private void setMotorDirection(int direction) {
        motorDirection = direction;
    }

    private void reverseMotorDirection() {
        if (motorDirection == FORWARD) {
            setMotorDirection(REVERSE);
        } else {
            setMotorDirection(FORWARD);
        }
    }

    /**
     * Constrains radians to an angle from 0 to TWO_PI
     */
    private double constrainRad(double r) {
        return r % TWO_PI;
    }

    /**
     * Calculate rotation of the swerve drive servo, in radians (0 - TWO_PI)
     */
    private double currentRotation() {
        double rot = constrainRad(TWO_PI * this.servo_encoder.getCurrentPosition()/8192.);
        if (motorDirection == FORWARD) {
            return rot;
        } else {
            return constrainRad(rot+Math.PI);
        }
    }

    /**
     * Calculate target rotation based on direction of joystick (0 - TWO_PI)
     */
    private double targetRotation(double y, double x) {
        return constrainRad(Math.atan2(y, x));
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

    public void update() {}

    public void update(double driveX, double driveY, double rotate) {

        // MOTORS

        double driveMagnitude = Math.sqrt(Math.pow(driveX,2) + Math.pow(driveY,2));
        drivePower = driveMagnitude;

        // SERVO

        if (driveMagnitude == 0) {
            servoPower = 0;
            drivePower = rotate;
        } else {
            drivePower = Range.clip(drivePower-(rotate*.5), -1,  1);

            double targetRotation = targetRotation(driveY, driveX);
            double currentRotation = currentRotation();
            double aboutFaceRotation = constrainRad(currentRotation + Math.PI);

            double turnCurrent = calculateTurn(currentRotation, targetRotation);
            double turnAboutFace = calculateTurn(aboutFaceRotation, targetRotation);

            if (Math.abs(turnAboutFace) < Math.abs(turnCurrent)) {
                reverseMotorDirection();
                servoPower = -Range.clip(turnAboutFace, -1, 1);
            } else {
                servoPower = -Range.clip(turnCurrent, -1, 1);
            }
        }

        if (motorDirection == REVERSE)
            drivePower *= -1;
    }

    public void go() {
        this.motor1.setPower(drivePower*0.75);
        this.motor2.setPower(drivePower*0.75);
        this.servo.setPower(servoPower);
    }

    public void addData(Telemetry telemetry) {
        String drc = "null";
        if (motorDirection == FORWARD) drc = "forward";
        else if (motorDirection == REVERSE) drc = "reverse";

        telemetry.addData("Swerve Drive",
                "currentRotation: (%f), direction: (%s), servoPower: (%.2f), drivePower: (%.2f)",
                currentRotation(), drc, servoPower, drivePower);
    }
}