package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.components.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


abstract public class AbstractOpMode extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();


    // Swerve Drives
    SwerveDrive leftSwerveDrive;
    SwerveDrive rightSwerveDrive;

    // Stacker
    StackerComponent stackerComponent;

    // Intake
    IntakeComponent intakeComponent;

    // Stilts
    StiltComponent stiltComponent;

    @Override
    public void init() {

        // Initialize Stacker Motor

        stackerComponent = new StackerComponent(dcMotor(Config.STACKER));

        // Three Intake Motors

        intakeComponent = new IntakeComponent(
                dcMotor(Config.INTAKE_LEFT), dcMotor(Config.INTAKE_RIGHT),
                dcMotor(Config.INTAKE_LIFT), dcMotor(Config.INTAKE_LIFT_ENCODER));

        // Two Stilt Servos

        stiltComponent = new StiltComponent(
                crServo(Config.LEFT_STILT), dcMotor(Config.LEFT_STILT_ENCODER),
                crServo(Config.RIGHT_STILT), dcMotor(Config.RIGHT_STILT_ENCODER));

        // Initialize Two Swerve Drives

        leftSwerveDrive = new SwerveDrive(
                dcMotor(Config.LEFT_SD1), dcMotor(Config.LEFT_SD2), crServo(Config.LEFT_SD3),
                dcMotor(Config.LEFT_SD_SERVO_ENCODER), dcMotor(Config.LEFT_SD_MOTOR_ENCODER));
        rightSwerveDrive = new SwerveDrive(
                dcMotor(Config.RIGHT_SD1), dcMotor(Config.RIGHT_SD2), crServo(Config.RIGHT_SD3),
                dcMotor(Config.RIGHT_SD_SERVO_ENCODER), dcMotor(Config.RIGHT_SD_MOTOR_ENCODER));

        // Update Telemetry

        telemetry.addData("Status", "Initialized");
    }

    private DcMotor dcMotor(String deviceName) {
        return hardwareMap.dcMotor.get(deviceName);
    }
    private CRServo crServo(String deviceName) {
        return hardwareMap.crservo.get(deviceName);
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

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
