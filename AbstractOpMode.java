package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.components.*;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


abstract public class AbstractOpMode extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();

    // The IMU sensor object
    BNO055IMU imu;

    // State used for updating telemetry
    double heading;

    // Swerve Drives
    SwerveDrive swerveDrive;

    // Stacker
    StackerComponent stackerComponent;

    // Intake
    IntakeComponent intakeComponent;

    // Stilts
    StiltComponent stiltComponent;

    @Override
    public void init() {

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.RADIANS;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);


        // Initialize Stacker Motor

        stackerComponent = new StackerComponent(dcMotor(Config.STACKER));

        // Three Intake Motors

        intakeComponent = new IntakeComponent(
                dcMotor(Config.INTAKE_LEFT), dcMotor(Config.INTAKE_RIGHT),
                dcMotor(Config.INTAKE_LIFT));

        // Two Stilt Servos

        stiltComponent = new StiltComponent(
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

        // Update Telemetry

        telemetry.addData("Status", "Initialized");
    }

    private DcMotor dcMotor(String deviceName) {
        return hardwareMap.dcMotor.get(deviceName);
    }
    private CRServo crServo(String deviceName) {
        return hardwareMap.crservo.get(deviceName);
    }

    private void updateHeading() {
        heading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle;
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
        updateHeading();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
