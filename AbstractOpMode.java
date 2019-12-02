package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


abstract public class AbstractOpMode extends OpMode {

    ElapsedTime runtime = new ElapsedTime();


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

        DcMotor stacker = hardwareMap.dcMotor.get(Config.STACKER);
        stackerComponent = new StackerComponent(stacker);

        // Three Intake Motors

        DcMotor intake_left = hardwareMap.dcMotor.get(Config.INTAKE_LEFT);
        DcMotor intake_right = hardwareMap.dcMotor.get(Config.INTAKE_RIGHT);
        DcMotor intake_lift = hardwareMap.dcMotor.get(Config.INTAKE_LIFT);
        intakeComponent = new IntakeComponent(intake_left, intake_right, intake_lift, intake_lift);

        // Two Stilt Servos

        CRServo left_stilt = hardwareMap.crservo.get(Config.LEFT_STILT);
        DcMotor left_encoder = hardwareMap.dcMotor.get(Config.LEFT_STILT_ENCODER);
        CRServo right_stilt = hardwareMap.crservo.get(Config.RIGHT_STILT);
        DcMotor right_encoder = hardwareMap.dcMotor.get(Config.RIGHT_STILT_ENCODER);
        stiltComponent = new StiltComponent(left_stilt, left_encoder, right_stilt, right_encoder);

        // Initialize Two Swerve Drives

        DcMotor left_sd1 = hardwareMap.dcMotor.get(Config.LEFT_SD1);
        DcMotor left_sd2 = hardwareMap.dcMotor.get(Config.LEFT_SD2);
        CRServo left_sd3 = hardwareMap.crservo.get(Config.LEFT_SD3);
        DcMotor right_sd1 = hardwareMap.dcMotor.get(Config.RIGHT_SD1);
        DcMotor right_sd2 = hardwareMap.dcMotor.get(Config.RIGHT_SD2);
        CRServo right_sd3 = hardwareMap.crservo.get(Config.RIGHT_SD3);

        leftSwerveDrive = new SwerveDrive(left_sd1, left_sd2, left_sd3, right_sd1);
        rightSwerveDrive = new SwerveDrive(right_sd1, right_sd2, right_sd3, left_sd1);

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

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
