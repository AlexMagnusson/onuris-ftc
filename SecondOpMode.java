
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


abstract class Component {
    OpMode opMode;

    abstract void update();
    abstract void go();
    abstract void addData();

}


class StackerComponent extends Component {

    double power = 0;

    DcMotor motor;

    StackerComponent(OpMode op, DcMotor motor) {
        opMode = op;
        this.motor = motor;

        this.motor.setDirection(DcMotor.Direction.FORWARD);
    }

    void update() {
        power = 0;
        if (opMode.gamepad1.right_bumper) {
            power = 1;
        }
        if (opMode.gamepad1.left_bumper) {
            power = -1;
        }
    }

    void go() {
        motor.setPower(power);
    }

    void addData() {
        opMode.telemetry.addData("Stacker Component",
                "power: (%.2f)",
                power);
    }
}


class Vector {
    double left;
    double right;
    Vector(double left, double right) {
        this.left = left;
        this.right = right;
    }
}


class StiltComponent extends Component {

    // Left, right
    Vector[] MODES = {
            new Vector(0, 0),
            new Vector(0, -600),
            new Vector(15000, 2700),
            new Vector(15000, 950),
            new Vector(7000, 950),
            new Vector(14500, 950)
    };


    int currentModeIdx = 0;
    boolean atMode = true;

    double leftPower = 0;
    double rightPower = 0;

    CRServo left_servo;
    DcMotor left_encoder;
    CRServo right_servo;
    DcMotor right_encoder;

    StiltComponent(OpMode op, CRServo left_servo, DcMotor left_encoder, CRServo right_servo, DcMotor right_encoder) {
        opMode = op;
        this.left_servo = left_servo;
        this.left_encoder = left_encoder;
        this.right_servo = right_servo;
        this.right_encoder = right_encoder;

        this.left_servo.setDirection(DcMotor.Direction.FORWARD);
        this.right_servo.setDirection(DcMotor.Direction.FORWARD);
    }

    private double getEncoderHeight(DcMotor encoder) {
        return encoder.getCurrentPosition();
    }
    private Vector currentHeightVector() {
        return new Vector(getEncoderHeight(this.left_encoder), getEncoderHeight(this.right_encoder));
    }

    private void adjustPower() {
        double tolerance = 64;

        Vector current_vector = currentHeightVector();
        Vector target_vector = MODES[currentModeIdx];

        double current_left = current_vector.left;
        double target_left = target_vector.left;
        double current_right = current_vector.right;
        double target_right = target_vector.right;

        double left_diff = target_left-current_left;
        double right_diff = target_right-current_right;
        if (Math.abs(left_diff) < tolerance && Math.abs(right_diff) < tolerance) {
            atMode = true;
            leftPower = 0;
            rightPower = 0;
        } else {
            atMode = false;
            leftPower = Range.clip(left_diff, -1, 1);
            rightPower = Range.clip(right_diff, -1, 1);
        }
    }

    void update() {
        adjustPower();
        if (atMode) {
            if (opMode.gamepad1.dpad_up && currentModeIdx < MODES.length-1)
                currentModeIdx += 1;
            if (opMode.gamepad1.dpad_down && currentModeIdx > 0)
                currentModeIdx -= 1;
        }

    }

    void go() {
        left_servo.setPower(leftPower);
        right_servo.setPower(rightPower);
    }

    void addData() {
        Vector height_vector = currentHeightVector();
        opMode.telemetry.addData("Stilt Component",
                "left power: (%.2f), left height: (%.2f), right power (%.2f), right height: (%.2f)",
                leftPower, height_vector.left, rightPower, height_vector.right);
    }

}


class IntakeComponent extends Component {

    double intakePower = 0;
    double verticalPower = 0;

    DcMotor motor1;  // Left intake motor
    DcMotor motor2;  // Right intake motor
    DcMotor motor3;  // Vertical movement motor

    IntakeComponent(OpMode op, DcMotor motor1, DcMotor motor2, DcMotor motor3) {
        opMode = op;
        this.motor1 = motor1;
        this.motor2 = motor2;
        this.motor3 = motor3;

        this.motor1.setDirection(DcMotor.Direction.FORWARD);
        this.motor2.setDirection(DcMotor.Direction.FORWARD);
        this.motor3.setDirection(CRServo.Direction.FORWARD);
    }

    void update() {
        intakePower = 0;
        if (opMode.gamepad1.x)
            intakePower += 1;
        if (opMode.gamepad1.b)
            intakePower -= 1;

        verticalPower = 0;
        if (opMode.gamepad1.y)
            verticalPower += 1;
        if (opMode.gamepad1.a)
            verticalPower -= 1;
    }

    void go() {
        motor1.setPower(intakePower);
        motor2.setPower(-intakePower);
        motor3.setPower(verticalPower);
    }

    void addData() {
        opMode.telemetry.addData("Intake Component",
                "intakePower: (%f), verticalPower: (%.2f)",
                intakePower, verticalPower);
    }

}


class SwerveDrive extends Component {

    private final double TWO_PI = 2*Math.PI;
    private final int FORWARD = 0;
    private final int REVERSE = 1;


    DcMotor motor1;  // Motor with encoder
    DcMotor motor2;
    CRServo servo;


    double drivePower;
    double servoPower;
    int motorDirection = FORWARD;

    SwerveDrive(OpMode op, DcMotor motor1, DcMotor motor2, CRServo servo) {
        opMode = op;
        this.motor1  = motor1;
        this.motor2 = motor2;
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
        double rot = constrainRad(TWO_PI * this.motor1.getCurrentPosition()/8192.);
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

    void update() {

//        if (opMode.gamepad1.right_stick_button) {
//            // add offset to make current rotation the forward position
//        }

        double x = opMode.gamepad1.right_stick_x;
        double y = opMode.gamepad1.right_stick_y;

        // MOTORS

        double magnitude = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
        drivePower = Range.clip(magnitude, -1.0, 1.0);  // Clip not necessary

        // SERVO

        if (magnitude == 0) {
            servoPower = 0;
        } else {
            double targetRotation = targetRotation(y, x);
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
        this.motor1.setPower(drivePower);
        this.motor2.setPower(drivePower);
        this.servo.setPower(servoPower);
    }

    void addData() {
        String drc = "null";
        if (motorDirection == FORWARD) drc = "forward";
        else if (motorDirection == REVERSE) drc = "reverse";

        opMode.telemetry.addData("Swerve Drive",
                "currentRotation: (%f), direction: (%s), servoPower: (%.2f), drivePower: (%.2f)",
                currentRotation(), drc, servoPower, drivePower);
    }
}





@TeleOp(name="Julian's Second Op Mode", group="Iterative Opmode")
public class SecondOpMode extends OpMode
{

    private ElapsedTime runtime = new ElapsedTime();


    // Swerve Drives
    private SwerveDrive leftSwerveDrive;
    private SwerveDrive rightSwerveDrive;

    // Stacker
    private StackerComponent stackerComponent;

    // Intake
    private IntakeComponent intakeComponent;

    // Stilts
    private StiltComponent stiltComponent;

    @Override
    public void init() {

        // Initialize Stacker Motor

        DcMotor stacker = hardwareMap.dcMotor.get("stacker");
        stackerComponent = new StackerComponent(this, stacker);

        // Three Intake Motors

        DcMotor intake_left = hardwareMap.dcMotor.get("intake_left");
        DcMotor intake_right = hardwareMap.dcMotor.get("intake_right");
        DcMotor intake_lift = hardwareMap.dcMotor.get("intake_lift");
        intakeComponent = new IntakeComponent(this, intake_left, intake_right, intake_lift);

        // Two Stilt Servos

        CRServo left_stilt = hardwareMap.crservo.get("left_stilt");
        CRServo right_stilt = hardwareMap.crservo.get("right_stilt");
        stiltComponent = new StiltComponent(this, left_stilt, intake_left, right_stilt, intake_right);

        // Initialize Two Swerve Drives

        DcMotor left_sd1 = hardwareMap.dcMotor.get("left_sd1");
        DcMotor left_sd2 = hardwareMap.dcMotor.get("left_sd2");
        CRServo left_sd3 = hardwareMap.crservo.get("left_sd3");
        leftSwerveDrive = new SwerveDrive(this, left_sd1, left_sd2, left_sd3);

        DcMotor right_sd1 = hardwareMap.dcMotor.get("right_sd1");
        DcMotor right_sd2 = hardwareMap.dcMotor.get("right_sd2");
        CRServo right_sd3 = hardwareMap.crservo.get("right_sd3");
        rightSwerveDrive = new SwerveDrive(this, right_sd1, right_sd2, right_sd3);

        // Update Telemetry

        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        // Stacker Component

        if (stackerComponent != null) {
            stackerComponent.update();
            stackerComponent.go();
            stackerComponent.addData();
        }

        // Intake Component

        if (intakeComponent != null) {
            intakeComponent.update();
            intakeComponent.go();
            intakeComponent.addData();
        }

        // Stilt Component

        if (stiltComponent != null) {
            stiltComponent.update();
            stiltComponent.go();
            stiltComponent.addData();
        }

        // Left Swerve Drive

        if (leftSwerveDrive != null) {
            leftSwerveDrive.update();
            leftSwerveDrive.go();
            leftSwerveDrive.addData();
        }

        // Right Swerve Drive

        if (rightSwerveDrive != null) {
            rightSwerveDrive.update();
            rightSwerveDrive.go();
            rightSwerveDrive.addData();
        }


        // Update telemetry

        telemetry.update();

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }


}
