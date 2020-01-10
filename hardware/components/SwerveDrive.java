package org.firstinspires.ftc.teamcode.hardware.components;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SwerveDrive extends Component {

    public WheelDrive left;
    public WheelDrive right;
    public WheelDrive front;

    public SwerveDrive(WheelDrive right, WheelDrive left, WheelDrive front) {
        this.right = right;
        this.left = left;
        this.front = front;
    }

    private void doDrive(double driveX, double driveY, double rotateX) {
        // -1 (left) <= driveX <= 1 (right)
        // -1 (backward) <= driveY <= 1 (forward)
        // -1 (turn left) <= rotateX <= 1 (turn right)
        rotateX *= 0.5;

        double right_Y = driveY - rotateX;
        double left_Y = driveY + rotateX;

        double right_speed = Math.sqrt(Math.pow(driveX, 2) + Math.pow(right_Y, 2));
        double right_angle = Math.atan2(driveX, right_Y);

        double left_speed = Math.sqrt(Math.pow(driveX, 2) + Math.pow(left_Y, 2));
        double left_angle = Math.atan2(driveX, left_Y);

        double max = Math.abs(Math.abs(left_speed) > Math.abs(right_speed) ? left_speed : right_speed);
        if (max > 1) {
            right_speed /= max;
            left_speed /= max;
        }
        right_speed = right_speed*Math.abs(right_speed);
        left_speed = left_speed*Math.abs(left_speed);

        right.drive(right_speed, right_angle);
        left.drive(left_speed, left_angle);
    }

    // Robot-centric (drive without gyro)
    public void drive(double driveX, double driveY, double rotateX) {
        driveY *= -1;
        doDrive(driveX, driveY, rotateX);
    }

    // Field-centric (drive with gyro)
    public void drive(double driveX, double driveY, double rotateX, double heading) {
        driveY *= -1;

        double temp = driveY*Math.cos(heading) + driveX*Math.sin(heading);
        driveX = -driveY*Math.sin(heading) + driveX*Math.cos(heading);
        driveY = temp;

        doDrive(driveX, driveY, rotateX);
    }

    public void addData(Telemetry telemetry) {
        telemetry.addData("Right Swerve Drive",
                "%s",
                right.toString());
        telemetry.addData("Left Swerve Drive",
                "%s",
                left.toString());
    }

}
