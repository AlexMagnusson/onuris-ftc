package org.firstinspires.ftc.teamcode.hardware.components;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Gyro {

    // The IMU sensor object
    private BNO055IMU imu;

    // Gyro variables
    private Orientation angles;
    public double offset = 0;

    private void initIMU() {
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
        imu.initialize(parameters);
    }

    public Gyro(BNO055IMU imu) {
        this.imu = imu;
        initIMU();
    }

    // Update
    public void update() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
    }

    // Heading
    public double getHeading() {
        return -angles.firstAngle + Math.PI;
    }
    public double getAdjustedHeading() {
        return getHeading() - offset;
    }

    // Reset heading
    public void resetHeading() {
        offset = getHeading();
    }

    // Telemetry
    public void addData(Telemetry telemetry) {
        telemetry.addData(
                "Gyro", "heading: (%f); angles: (%f), (%f), (%f)",
                getHeading(), angles.firstAngle, angles.secondAngle, angles.thirdAngle);
    }

}
