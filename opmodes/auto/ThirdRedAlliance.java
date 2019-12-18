
package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.components.Stilt;


@Autonomous(name="Julian's Third Red Mode")
public class ThirdRedAlliance extends AbstractAutoMode
{

    public ThirdRedAlliance() {
        super();
        waiting = 200;
    }

    private void updateSwerves() {
        robot.swerveDrive.drive(driveX, driveY, rotate, robot.gyro.getAdjustedHeading());
        robot.swerveDrive.addData(telemetry);
    }

    private void updateStilts() {
        robot.stilt.setTargetMode(stiltMode);
        robot.stilt.update();
        robot.stilt.go();
        robot.stilt.addData(telemetry);
    }

    private void update() {
        updateSwerves();
        updateStilts();
    }

    private double rotationTowardsTarget(double max) {
        double diff = constrainRad(targetHeading)-constrainRad(robot.gyro.getAdjustedHeading());
        return Range.clip(diff,-max, max);
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
        stiltMode = Stilt.ZERO_MODE;

        if (waiting > 0) {
            waiting -= 1;
            updateSwerves();
        } else {
            if (currentStage == 1) {  // Move to face west
                driveX = .1;
                driveY = 0;
                targetHeading = Math.PI;
                rotate = rotationTowardsTarget(.1);

                update();

                waiting -= 1;

                if (waiting < -40) {
                    nextStage();
                }
            } else if (currentStage == 2) {  // Move west towards foundation
                driveX = .5;
                driveY = 0;
                targetHeading = Math.PI;
                rotate = rotationTowardsTarget(.1);

                update();

                if (currentOffsetRight() >= 400) {
                    nextStage();
                }
            } else {
                update();
            }
        }

        telemetry.update();
    }

}
