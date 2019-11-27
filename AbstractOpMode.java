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
        stackerComponent = new StackerComponent(this, stacker);

        // Three Intake Motors

        DcMotor intake_left = hardwareMap.dcMotor.get(Config.INTAKE_LEFT);
        DcMotor intake_right = hardwareMap.dcMotor.get(Config.INTAKE_RIGHT);
        DcMotor intake_lift = hardwareMap.dcMotor.get(Config.INTAKE_LIFT);
        intakeComponent = new IntakeComponent(this, intake_left, intake_right, intake_lift);

        // Two Stilt Servos

        CRServo left_stilt = hardwareMap.crservo.get(Config.LEFT_STILT);
        DcMotor left_encoder = hardwareMap.dcMotor.get(Config.LEFT_STILT_ENCODER);
        CRServo right_stilt = hardwareMap.crservo.get(Config.RIGHT_STILT);
        DcMotor right_encoder = hardwareMap.dcMotor.get(Config.RIGHT_STILT_ENCODER);
        stiltComponent = new StiltComponent(this, left_stilt, left_encoder, right_stilt, right_encoder);

        // Initialize Two Swerve Drives

        DcMotor left_sd1 = hardwareMap.dcMotor.get(Config.LEFT_SD1);
        DcMotor left_sd2 = hardwareMap.dcMotor.get(Config.LEFT_SD2);
        CRServo left_sd3 = hardwareMap.crservo.get(Config.LEFT_SD3);
        leftSwerveDrive = new SwerveDrive(this, left_sd1, left_sd2, left_sd3);

        DcMotor right_sd1 = hardwareMap.dcMotor.get(Config.RIGHT_SD1);
        DcMotor right_sd2 = hardwareMap.dcMotor.get(Config.RIGHT_SD2);
        CRServo right_sd3 = hardwareMap.crservo.get(Config.RIGHT_SD3);
        rightSwerveDrive = new SwerveDrive(this, right_sd1, right_sd2, right_sd3);

        // Update Telemetry

        telemetry.addData("Status", "Initialized");
    }

}
