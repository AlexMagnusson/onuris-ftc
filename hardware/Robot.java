package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.hardware.components.Gyro;
import org.firstinspires.ftc.teamcode.hardware.components.Intake;
import org.firstinspires.ftc.teamcode.hardware.components.SwerveDrive;
import org.firstinspires.ftc.teamcode.hardware.components.WheelDrive;

public class Robot {

    private HardwareMap hardwareMap;


    // Gyro
    public Gyro gyro;

    // Swerve Drives
    public SwerveDrive swerveDrive;

    // Intake
    public Intake intake;

    public Robot(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        // Retrieve gyro
        gyro = new Gyro(hardwareMap.get(BNO055IMU.class, "imu"));

        // Three Intake Motors
        intake = new Intake(
                dcMotor(Config.INTAKE_LEFT), dcMotor(Config.INTAKE_RIGHT),
                crServo(Config.INTAKE_LIFT));

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

