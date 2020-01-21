package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name="AutoBlueFoundation")
public class AutoBlueFoundation extends AbstractAutoMode
{

    private void updateSwerves() {
        robot.swerveDrive.control(-driveX, driveY, rotate, robot.gyro.getAdjustedHeading());
        robot.swerveDrive.drive();
        robot.swerveDrive.addData(telemetry);
    }

    private void update() {
        updateSwerves();
    }

    private double rotationTowardsTarget(double max) {
        double diff = constrainRad(targetHeading)-constrainRad(robot.gyro.getAdjustedHeading());
        if (diff > 0)
            return max;
        else if (diff < 0)
            return -max;
        else
            return 0;
    }

    @Override
    public void loop() {
        super.loop();

        currentRightPos = robot.swerveDrive.right.getMotorPosition();
        currentLeftPos = robot.swerveDrive.left.getMotorPosition();

        telemetry.addData("STAGE", "Stage %s, rightPosition: (%f), leftPosition: (%f)", currentStage, currentRightPos, currentLeftPos);

        driveX = 0;
        driveY = 0;
        rotate = 0;

        if (waiting > 0) {
            waiting -= 1;
            updateSwerves();
        } else {
            if (currentStage == 1) {  // Move backward towards foundation
                driveX = 0;
                driveY = 0.75;
                targetHeading = Math.PI;
                rotate = rotationTowardsTarget(0);

                update();

                if (currentOffsetRight() >= 1200) {
                    nextStage();
                }
            } else if (currentStage == 2) {
                driveX = 0;
                driveY = 0.5;
                targetHeading = -Math.PI;
                rotate = rotationTowardsTarget(0);

                update();

                if (currentOffsetRight() >= 600) {
                    nextStage();
                }
            } else {
                update();
            }
        }

        telemetry.update();
    }

}