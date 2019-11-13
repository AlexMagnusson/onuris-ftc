
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


class ElevatorComponent extends Component {

    double power = 0;

    CRServo servo;

    ElevatorComponent(OpMode op, CRServo servo) {
        opMode = op;
        this.servo = servo;

        this.servo.setDirection(DcMotor.Direction.FORWARD);
    }

    void update() {
        power = 0;
        if (opMode.gamepad1.dpad_up)
            power += 1;
        if (opMode.gamepad1.dpad_down)
            power -= 1;
    }

    void go() {
        servo.setPower(power);
    }

    void addData() {
        opMode.telemetry.addData("Elevator Component",
                "power: (%.2f)",
                power);
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


    DcMotor motor1;  // Motor with encoder
    DcMotor motor2;
    CRServo servo;


    double drivePower;
    double servoPower;

    SwerveDrive(OpMode op, DcMotor motor1, DcMotor motor2, CRServo servo) {
        opMode = op;
        this.motor1  = motor1;
        this.motor2 = motor2;
        this.servo = servo;

        this.motor1.setDirection(DcMotor.Direction.FORWARD);
        this.motor2.setDirection(DcMotor.Direction.FORWARD);
        this.servo.setDirection(CRServo.Direction.FORWARD);
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
        return constrainRad(TWO_PI * this.motor1.getCurrentPosition()/8192.);
    }

    /**
     * Calculate target rotation based on direction of joystick (0 - TWO_PI)
     */
    private double targetRotation(double y, double x) {
        return constrainRad(Math.atan2(y, x));
    }

    void update() {
        double x = opMode.gamepad1.right_stick_x;
        double y = -opMode.gamepad1.right_stick_y;

        // MOTORS

        double magnitude = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
        drivePower = Range.clip(magnitude, -1.0, 1.0);

        // SERVO

        double targetRotation = targetRotation(y, x);
        double currentRotation = currentRotation();

        double diff_ccw = constrainRad(targetRotation - currentRotation);
        double diff_cw = TWO_PI - diff_ccw;
        double distanceToTarget;
        if (diff_ccw < diff_cw) {
            distanceToTarget = diff_ccw;
        } else {
            distanceToTarget = -diff_cw;
        }
        servoPower = Range.clip(distanceToTarget, -1, 1);
    }

    void go() {
        this.motor1.setPower(drivePower);
        this.motor2.setPower(drivePower);
        this.servo.setPower(servoPower);
    }

    void addData() {
        opMode.telemetry.addData("Swerve Drive",
                "currentRotation: (%f), servoPower: (%.2f), drivePower: (%.2f)",
                currentRotation(), servoPower, drivePower);
    }
}





@TeleOp(name="Julian's Second Op Mode", group="Iterative Opmode")
public class SecondOpMode extends OpMode
{

    private ElapsedTime runtime = new ElapsedTime();


    // Motor
    private DcMotor testMotor;

    // Swerve Drives
    private SwerveDrive leftSwerveDrive;
    private SwerveDrive rightSwerveDrive;

    // Stacker
    private StackerComponent stackerComponent;

    // Intake
    private IntakeComponent intakeComponent;

    // Elevators
    private ElevatorComponent leftElevator;
    private ElevatorComponent rightElevator;

    @Override
    public void init() {

        // Test Motor

//        testMotor = hardwareMap.dcMotor.get("test_motor");
//        testMotor.setDirection(DcMotor.Direction.FORWARD);

        // Initialize Stacker Motor (1)

        DcMotor stacker = hardwareMap.dcMotor.get("stacker");
        stackerComponent = new StackerComponent(this, stacker);

        // Three Intake Motors

        DcMotor intake_left = hardwareMap.dcMotor.get("intake_left");
        DcMotor intake_right = hardwareMap.dcMotor.get("intake_right");
        DcMotor intake_lift = hardwareMap.dcMotor.get("intake_lift");
        intakeComponent = new IntakeComponent(this, intake_left, intake_right, intake_lift);

        // Two Elevator Servos

//        CRServo left_elevator = hardwareMap.crservo.get("left_elevator");
//        leftElevator = new ElevatorComponent(this, left_elevator);
//
//        CRServo right_elevator = hardwareMap.crservo.get("right_elevator");
//        rightElevator = new ElevatorComponent(this, right_elevator);

        // Initialize Two Swerve Drives

//        DcMotor left_sd1 = hardwareMap.dcMotor.get("left_sd1");
//        DcMotor left_sd2 = hardwareMap.dcMotor.get("left_sd2");
//        CRServo left_sd3 = hardwareMap.crservo.get("left_sd3");
//        leftSwerveDrive = new SwerveDrive(this, left_sd1, left_sd2, left_sd3);
//
//        DcMotor right_sd1 = hardwareMap.dcMotor.get("right_sd1");
//        DcMotor right_sd2 = hardwareMap.dcMotor.get("right_sd2");
//        CRServo right_sd3 = hardwareMap.crservo.get("right_sd3");
//        rightSwerveDrive = new SwerveDrive(this, right_sd1, right_sd2, right_sd3);

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

        // Test Motor

//        double testPower = 0;
//        if (gamepad1.a)
//            testPower += 1;
//        if (gamepad1.b)
//            testPower -= -1;
//        testMotor.setPower(testPower);
//        telemetry.addData("Test Motor", "power (%.2f)", testPower);


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

        // Left Elevator Component

        if (leftElevator != null) {
            leftElevator.update();
            leftElevator.go();
            leftElevator.addData();
        }

        // Right Elevator Component

        if (rightElevator != null) {
            rightElevator.update();
            rightElevator.go();
            rightElevator.addData();
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
