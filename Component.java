package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

abstract class Component {
    abstract void go();
    abstract void addData(Telemetry telemetry);

}


class StackerComponent extends Component {

    double power = 0;

    DcMotor motor;

    StackerComponent(DcMotor motor) {
        this.motor = motor;

        this.motor.setDirection(DcMotor.Direction.FORWARD);
    }

    void update(boolean up, boolean down) {
        power = 0;
        if (up)
            power += 1;
        if (down)
            power -= 1;
    }

    void go() {
        motor.setPower(power);
    }

    void addData(Telemetry telemetry) {
        telemetry.addData("Stacker Component",
                "power: (%.2f)",
                power);
    }
}


class Vector {
    double foot;
    double wheel;
    Vector(double foot, double wheel) {
        this.foot = foot;
        this.wheel = wheel;
    }
}


class StiltComponent extends Component {

    final Vector ZERO_MODE = new Vector(0, 0);
    final Vector INTAKE_MODE = new Vector(0,  -600);
    final Vector STACK_MODE = new Vector(-13000,  2700);
    final Vector STACK0_MODE = new Vector(-13000,  950);
    final Vector STACK1_MODE = new Vector(-7000,  950);
    final Vector STACK2_MODE = new Vector(-12000,  950);

    Vector currentMode = ZERO_MODE;
    boolean atMode = true;

    double footPower = 0;
    double wheelPower = 0;

    CRServo foot_servo;
    DcMotor foot_encoder;
    CRServo wheel_servo;
    DcMotor wheel_encoder;

    StiltComponent(CRServo foot_servo, DcMotor foot_encoder, CRServo wheel_servo, DcMotor wheel_encoder) {
        this.foot_servo = foot_servo;
        this.foot_encoder = foot_encoder;
        this.wheel_servo = wheel_servo;
        this.wheel_encoder = wheel_encoder;

        this.foot_servo.setDirection(DcMotor.Direction.FORWARD);
        this.wheel_servo.setDirection(DcMotor.Direction.FORWARD);
    }

    public void setCurrentMode(Vector currentMode) {
        this.currentMode = currentMode;
    }

    private double getEncoderHeight(DcMotor encoder) {
        return encoder.getCurrentPosition();
    }
    private Vector currentHeightVector() {
        return new Vector(getEncoderHeight(this.foot_encoder), getEncoderHeight(this.wheel_encoder));
    }

    void adjustPower() {
        double toleranceFoot = 100;
        double slowdownFactorFoot = 200.;
        double toleranceWheel = 100;
        double slowdownFactorWheel = 200.;

        Vector current_vector = currentHeightVector();
        Vector target_vector = currentMode;

        double current_foot = current_vector.foot;
        double target_foot = target_vector.foot;
        double current_wheel = current_vector.wheel;
        double target_wheel = target_vector.wheel;

        double foot_diff = target_foot-current_foot;
        double wheel_diff = target_wheel-current_wheel;
        if (Math.abs(foot_diff) < toleranceFoot) {
            footPower = 0;
        } else {
            footPower = Range.clip(foot_diff / slowdownFactorFoot, -1, 1);
        }
        if (Math.abs(wheel_diff) < toleranceWheel) {
            wheelPower = 0;
        } else {
            wheelPower = Range.clip(wheel_diff / slowdownFactorWheel, -1, 1);
        }
        atMode = (wheelPower == 0 && footPower == 0);
    }

    void update(boolean stackButton, boolean stack0Button, boolean stack1Button, boolean stack2Button) {
        if (stackButton)
            setCurrentMode(STACK_MODE);
        if (stack0Button)
            setCurrentMode(STACK0_MODE);
        if (stack1Button)
            setCurrentMode(STACK1_MODE);
        if (stack2Button)
            setCurrentMode(STACK2_MODE);
        adjustPower();
    }

    void go() {
        foot_servo.setPower(footPower);
        wheel_servo.setPower(wheelPower);
    }

    void addData(Telemetry telemetry) {
        Vector height_vector = currentHeightVector();
        telemetry.addData("Stilt Component",
                "footPower: (%.2f), footHeight: (%.2f), rightPower: (%.2f), rightHeight: (%.2f), \nMODE: %s, atMode: %s",
                footPower, height_vector.foot, wheelPower, height_vector.wheel, currentMode.toString(), atMode);
    }

}


class IntakeComponent extends Component {

    final double LOW_POSITION = 0;
    final double MID_POSITION = 200;
    final double HIGH_POSITION = 400;

    double targetPosition = LOW_POSITION;
    boolean atTarget = false;

    double intakePower = 0;
    double verticalPower = 0;

    DcMotor motor1;  // Left intake motor
    DcMotor motor2;  // Right intake motor
    DcMotor motor3;  // Vertical movement motor
    DcMotor encoder;

    IntakeComponent(DcMotor motor1, DcMotor motor2, DcMotor motor3, DcMotor encoder_motor) {
        this.motor1 = motor1;
        this.motor2 = motor2;
        this.motor3 = motor3;
        this.encoder = encoder_motor;

        this.motor1.setDirection(DcMotor.Direction.FORWARD);
        this.motor2.setDirection(DcMotor.Direction.FORWARD);
        this.motor3.setDirection(CRServo.Direction.FORWARD);
    }

    public void setTargetPosition(double targetPosition) {
        this.targetPosition = targetPosition;
    }

    private double getCurrentPosition() {
        return encoder.getCurrentPosition();
    }

    private void adjustVerticalPower() {
        double tolerance = 10;
        double slowdownFactor = 20.;

        double currentPosition = getCurrentPosition();

        double position_diff = targetPosition-currentPosition;
        if (Math.abs(position_diff) < tolerance) {
            verticalPower = 0;
        } else {
            verticalPower = Range.clip(position_diff / slowdownFactor, -1, 1);
        }
        atTarget = verticalPower == 0;
    }

    void update(boolean inButton, boolean outButton, boolean lowButton, boolean midButton, boolean highButton, double tweakStickY) {

        // Intake in/out

        intakePower = 0;
        if (inButton)
            intakePower = -1;
        if (outButton)
            intakePower = 1;

        // Vertical movement

        final double TWEAK_FACTOR = 1;

        if (lowButton)
            setTargetPosition(LOW_POSITION);
        if (midButton)
            setTargetPosition(MID_POSITION);
        if (highButton)
            setTargetPosition(HIGH_POSITION);

        if (tweakStickY < 0)
            targetPosition += TWEAK_FACTOR;
        else if (tweakStickY > 0)
            targetPosition -= TWEAK_FACTOR;
        adjustVerticalPower();
    }

    void go() {
        motor1.setPower(intakePower);
        motor2.setPower(-intakePower);
        motor3.setPower(verticalPower);
    }

    void addData(Telemetry telemetry) {
        telemetry.addData("Intake Component",
                "intakePower: (%f), verticalPower: (%.2f), currentPosition: (%.2f), targetPosition: (%.2f), atTarget %s",
                intakePower, verticalPower, getCurrentPosition(), targetPosition, atTarget);
    }

}


class SwerveDrive extends Component {

    private final double TWO_PI = 2*Math.PI;
    private final int FORWARD = 0;
    private final int REVERSE = 1;


    DcMotor motor1;  // Motor with encoder
    DcMotor motor2;
    DcMotor encoder_motor;
    CRServo servo;


    double drivePower;
    double servoPower;
    int motorDirection = FORWARD;

    SwerveDrive(DcMotor motor1, DcMotor motor2, CRServo servo, DcMotor encoder_motor) {
        this.motor1  = motor1;
        this.motor2 = motor2;
        this.encoder_motor = encoder_motor;
        this.servo = servo;

        this.servo.setDirection(CRServo.Direction.FORWARD);
        this.motor1.setDirection(DcMotor.Direction.FORWARD);
        this.motor2.setDirection(DcMotor.Direction.FORWARD);
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
        double rot = constrainRad(TWO_PI * this.encoder_motor.getCurrentPosition()/8192.);
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

    void update(double driveX, double driveY, double rotate) {

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

    void go() {
        this.motor1.setPower(drivePower*0.75);
        this.motor2.setPower(drivePower*0.75);
        this.servo.setPower(servoPower);
    }

    void addData(Telemetry telemetry) {
        String drc = "null";
        if (motorDirection == FORWARD) drc = "forward";
        else if (motorDirection == REVERSE) drc = "reverse";

        telemetry.addData("Swerve Drive",
                "currentRotation: (%f), direction: (%s), servoPower: (%.2f), drivePower: (%.2f)",
                currentRotation(), drc, servoPower, drivePower);
    }
}


