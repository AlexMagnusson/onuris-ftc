package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.hardware.components.Gate;
import org.firstinspires.ftc.teamcode.hardware.components.Gyro;
import org.firstinspires.ftc.teamcode.hardware.components.Intake;
import org.firstinspires.ftc.teamcode.hardware.components.Stacker;
import org.firstinspires.ftc.teamcode.hardware.components.Stilt;
import org.firstinspires.ftc.teamcode.hardware.components.SwerveDrive;
import org.firstinspires.ftc.teamcode.hardware.components.WheelDrive;

public class Robot {

    private HardwareMap hardwareMap;


    // Gyro
    public Gyro gyro;

    // Swerve Drives
    public SwerveDrive swerveDrive;

    // Stacker
    public Stacker stacker;

    // Intake
    public Intake intake;

    // Stilts
    public Stilt stilt;

    // Gate
    public Gate gate;

    public Robot(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        // Retrieve gyro
        gyro = new Gyro(hardwareMap.get(BNO055IMU.class, "imu"));

        // Initialize gate
        gate = new Gate(crServo(Config.GATE_SERVO));

        // Initialize Stacker Motor
        stacker = new Stacker(dcMotor(Config.STACKER));

        // Three Intake Motors
        intake = new Intake(
                dcMotor(Config.INTAKE_LEFT), dcMotor(Config.INTAKE_RIGHT),
                dcMotor(Config.INTAKE_LIFT));

        // Two Stilt Servos
        stilt = new Stilt(
                crServo(Config.LEFT_STILT), dcMotor(Config.LEFT_STILT_ENCODER),
                crServo(Config.RIGHT_STILT), dcMotor(Config.RIGHT_STILT_ENCODER));

        // Initialize Swerve Drives

        WheelDrive rightWheelDrive = new WheelDrive(
                dcMotor(Config.RIGHT_SD1), dcMotor(Config.RIGHT_SD2), crServo(Config.RIGHT_SD3),
                dcMotor(Config.RIGHT_SD_SERVO_ENCODER), dcMotor(Config.RIGHT_SD_MOTOR_ENCODER));
        WheelDrive leftWheelDrive = new WheelDrive(
                dcMotor(Config.LEFT_SD1), dcMotor(Config.LEFT_SD2), crServo(Config.LEFT_SD3),
                dcMotor(Config.LEFT_SD_SERVO_ENCODER), dcMotor(Config.LEFT_SD_MOTOR_ENCODER));
        WheelDrive frontWheelDrive = new WheelDrive(
                dcMotor(Config.FRONT_SD1), dcMotor(Config.FRONT_SD2), crServo(Config.FRONT_SD3),
                dcMotor(Config.FRONT_SD_SERVO_ENCODER), dcMotor(Config.FRONT_SD_MOTOR_ENCODER));

        swerveDrive = new SwerveDrive(rightWheelDrive, leftWheelDrive, frontWheelDrive);
    }

    private DcMotor dcMotor(String deviceName) {
        return hardwareMap.dcMotor.get(deviceName);
    }
    private CRServo crServo(String deviceName) {
        return hardwareMap.crservo.get(deviceName);
    }

}

