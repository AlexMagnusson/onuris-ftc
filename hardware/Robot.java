package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.hardware.components.GateComponent;
import org.firstinspires.ftc.teamcode.hardware.components.IntakeComponent;
import org.firstinspires.ftc.teamcode.hardware.components.StackerComponent;
import org.firstinspires.ftc.teamcode.hardware.components.StiltComponent;
import org.firstinspires.ftc.teamcode.hardware.components.SwerveDrive;
import org.firstinspires.ftc.teamcode.hardware.components.WheelDrive;

public class Robot {

    private HardwareMap hardwareMap;


    // Swerve Drives
    public SwerveDrive swerveDrive;

    // Stacker
    public StackerComponent stacker;

    // Intake
    public IntakeComponent intake;

    // Stilts
    public StiltComponent stilt;

    // Gate
    public GateComponent gate;

    public Robot(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        // Initialize gate

        gate = new GateComponent(crServo(Config.GATE_SERVO));

        // Initialize Stacker Motor

        stacker = new StackerComponent(dcMotor(Config.STACKER));

        // Three Intake Motors

        intake = new IntakeComponent(
                dcMotor(Config.INTAKE_LEFT), dcMotor(Config.INTAKE_RIGHT),
                dcMotor(Config.INTAKE_LIFT));

        // Two Stilt Servos

        stilt = new StiltComponent(
                crServo(Config.LEFT_STILT), dcMotor(Config.LEFT_STILT_ENCODER),
                crServo(Config.RIGHT_STILT), dcMotor(Config.RIGHT_STILT_ENCODER));

        // Initialize Two Swerve Drives

        WheelDrive rightWheelDrive = new WheelDrive(
                dcMotor(Config.RIGHT_SD1), dcMotor(Config.RIGHT_SD2), crServo(Config.RIGHT_SD3),
                dcMotor(Config.RIGHT_SD_SERVO_ENCODER), dcMotor(Config.RIGHT_SD_MOTOR_ENCODER));
        WheelDrive leftWheelDrive = new WheelDrive(
                dcMotor(Config.LEFT_SD1), dcMotor(Config.LEFT_SD2), crServo(Config.LEFT_SD3),
                dcMotor(Config.LEFT_SD_SERVO_ENCODER), dcMotor(Config.LEFT_SD_MOTOR_ENCODER));

        swerveDrive = new SwerveDrive(rightWheelDrive, leftWheelDrive);
    }

    private DcMotor dcMotor(String deviceName) {
        return hardwareMap.dcMotor.get(deviceName);
    }
    private CRServo crServo(String deviceName) {
        return hardwareMap.crservo.get(deviceName);
    }

}

