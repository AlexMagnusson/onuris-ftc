package org.firstinspires.ftc.teamcode.components;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SwerveDrive extends Component {

    public WheelDrive left;
    public WheelDrive right;

    public SwerveDrive(WheelDrive right, WheelDrive left) {
        this.right = right;
        this.left = left;
    }

    public void drive(double driveX, double driveY, double rotateX) {
        driveY *= -1;

        // -1 (left) <= driveX <= 1 (right)
        // -1 (backward) <= driveY <= 1 (forward)
        // -1 (turn left) <= rotateX <= 1 (turn right)

        double right_Y = driveY - rotateX;
        double left_Y = driveY + rotateX;

        double right_speed = Math.sqrt(Math.pow(driveX, 2) + Math.pow(right_Y, 2));
        double right_angle = Math.atan2(right_Y, driveX);

        double left_speed = Math.sqrt(Math.pow(driveX, 2) + Math.pow(left_Y, 2));
        double left_angle = Math.atan2(left_Y, driveX);

        right.drive(right_speed, right_angle);
        left.drive(left_speed, left_angle);
    }

    public void addData(Telemetry telemetry) {
        telemetry.addData("Swerve Drives",
                "LEFT: %s\nRIGHT: %s",
                left.toString(), right.toString());
    }

}
