package org.firstinspires.ftc.teamcode.hardware.components;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SwerveDrive {

    private static double RAMP_UP_FACTOR = 0.25;  // Percentage of error adjusted for speed
    private static double RAMP_DOWN_FACTOR = 0.5;  // Percentage of error adjusted for speed

    // Wheel components
    public WheelDrive left;
    public WheelDrive right;
    public WheelDrive front;

    // Controller inputs
    public double joystickX = 0;
    public double joystickY = 0;

    // Actual controls after calculations
    private double driveX = 0;
    private double driveY = 0;
    public double rotate = 0;

    public SwerveDrive(WheelDrive right, WheelDrive left, WheelDrive front) {
        this.right = right;
        this.left = left;
        this.front = front;
    }

    private double[] calcSwerve(double wheelX, double wheelY) {
        double Wx = driveX+rotate*wheelY;
        double Wy = driveY-rotate*wheelX;

        double speed = Math.sqrt(Math.pow(Wx, 2) + Math.pow(Wy, 2));
        double angle = Math.atan2(Wx, Wy);

        double[] arr = {speed, angle};
        return arr;
    }

    private double calcRampUp(double target, double current, double upFactor, double downFactor) {
        double error = target-current;
        double factor = (Math.abs(target) < Math.abs(current))? downFactor : upFactor;
        return error*factor;
    }

    public void drive() {
        driveX += calcRampUp(joystickX, driveX, RAMP_UP_FACTOR, RAMP_DOWN_FACTOR);
        driveY += calcRampUp(joystickY, driveY, RAMP_UP_FACTOR, RAMP_DOWN_FACTOR);

        // -1 (left) <= driveX <= 1 (right)
        // -1 (backward) <= driveY <= 1 (forward)
        // -1 (turn left) <= rotateX <= 1 (turn right)

        double[] frontXY = {0, -1};
        double[] leftXY = {-1, 1};
        double[] rightXY = {1, 1};

        double[] frontArr = calcSwerve(frontXY[0], frontXY[1]);
        double[] leftArr = calcSwerve(leftXY[0], leftXY[1]);
        double[] rightArr = calcSwerve(rightXY[0], rightXY[1]);

        right.control(rightArr[0], rightArr[1]);
        left.control(leftArr[0], leftArr[1]);
        front.control(frontArr[0], frontArr[1]);

        right.drive();
        left.drive();
        front.drive();
    }

    // Robot-centric (drive without gyro)
    public void control(double x, double y, double rX) {
        rotate = rX*0.5;
        joystickX = x;
        joystickY = -y;
    }

    // Field-centric (drive with gyro)
    public void control(double x, double y, double rX, double heading) {
        control(x, y, rX);
        adjustByGyro(heading);
    }

    public void adjustByGyro(double heading) {
        double temp = joystickY*Math.cos(heading) + joystickX*Math.sin(heading);
        joystickX = -joystickY*Math.sin(heading) + joystickX*Math.cos(heading);
        joystickY = temp;
    }


    public double currentDistanceMoved() {
        return (left.encoderMotor.currentDistanceMoved() +
                right.encoderMotor.currentDistanceMoved() +
                front.encoderMotor.currentDistanceMoved())/3;
    }


    public void addData(Telemetry telemetry) {
        telemetry.addData("Right Swerve Drive",
                "%s",
                right.toString());
        telemetry.addData("Left Swerve Drive",
                "%s",
                left.toString());
        telemetry.addData("Front Swerve Drive",
                "%s",
                front.toString());
    }

}
