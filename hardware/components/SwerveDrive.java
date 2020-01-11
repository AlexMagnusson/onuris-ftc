package org.firstinspires.ftc.teamcode.hardware.components;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SwerveDrive extends Component {

    private static double RAMP_UP_FACTOR1 = 0.25;  // Percentage of error adjusted for speed
    private static double RAMP_DOWN_FACTOR1 = 0.5;  // Percentage of error adjusted for speed

    private static double RAMP_UP_FACTOR2 = 0.5;  // Percentage of error adjusted for rotation
    private static double RAMP_DOWN_FACTOR2 = 0.8;  // Percentage of error adjusted for rotation

    public WheelDrive left;
    public WheelDrive right;
    public WheelDrive front;

    // Controller inputs
    private double joystickX = 0;
    private double joystickY = 0;
    private double rotateTarget = 0;

    // Actual controls after calculations
    private double driveX = 0;
    private double driveY = 0;
    private double rotate = 0;

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

    public double calcRampUp(double target, double current, double upFactor, double downFactor) {
        double rampUp;
        double error = target-current;
        if (Math.abs(target) < Math.abs(current))
            rampUp = downFactor*error;
        else
            rampUp = upFactor*error;
        return rampUp;
    }

    public void drive() {
        driveX += calcRampUp(joystickX, driveX, RAMP_UP_FACTOR1, RAMP_DOWN_FACTOR1);
        driveY += calcRampUp(joystickY, driveY, RAMP_UP_FACTOR1, RAMP_DOWN_FACTOR1);
        rotate += calcRampUp(rotateTarget, rotate, RAMP_UP_FACTOR2, RAMP_DOWN_FACTOR2);

        // -1 (left) <= driveX <= 1 (right)
        // -1 (backward) <= driveY <= 1 (forward)
        // -1 (turn left) <= rotateX <= 1 (turn right)

        double[] frontXY = {0, -3.75};
        double[] leftXY = {-3.75, 3.75};
        double[] rightXY = {3.75, 3.75};

        double[] frontArr = calcSwerve(frontXY[0], frontXY[1]);
        double[] leftArr = calcSwerve(leftXY[0], leftXY[1]);
        double[] rightArr = calcSwerve(rightXY[0], rightXY[1]);

        right.drive(rightArr[0], rightArr[1]);
        left.drive(leftArr[0], leftArr[1]);
        front.drive(frontArr[0], frontArr[1]);
    }

    // Robot-centric (drive without gyro)
    public void control(double x, double y, double rX) {
        rotateTarget = rX/2;
        joystickX = x;
        joystickY = -y;
    }

    // Field-centric (drive with gyro)
    public void control(double x, double y, double rX, double heading) {
        rotateTarget = rX/2;
        joystickX = x;
        joystickY = -y;

        double temp = joystickY*Math.cos(heading) + joystickX*Math.sin(heading);
        joystickX = -joystickY*Math.sin(heading) + joystickX*Math.cos(heading);
        joystickY = temp;
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
