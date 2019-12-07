
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.components.StiltComponent;
import org.firstinspires.ftc.teamcode.hardware.components.Vector;


@Autonomous(name="Julian's Blue Mode")
public class BlueAlliance extends AbstractAutoMode
{

    private void updateSwerves() {
        robot.swerveDrive.drive(-driveX, driveY, rotate, getAdjustedHeading());
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
        double diff = constrainRad(targetHeading)-constrainRad(heading);
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
        stiltMode = StiltComponent.ZERO_MODE;

        Vector clampMode = new Vector(-1600, 1600);
        Vector upMode = new Vector(-3500, 3500);

        if (waiting > 0) {
            waiting -= 1;
            updateSwerves();
        } else {
            if (currentStage == 1) {  // Move backward towards foundation
                driveX = .1;
                driveY = -.4;
                targetHeading = Math.PI;
                rotate = rotationTowardsTarget(.1);
                stiltMode = upMode;

                update();

                if (currentOffsetRight() >= 1750) {
                    nextStage();
                }
            } else if (currentStage == 2) {  // Drop the stilts, clamp
                driveX = 0;
                driveY = 0;
                targetHeading = Math.PI;
                rotate = rotationTowardsTarget(.1);
                stiltMode = clampMode;

                update();

                if (robot.stilt.atTarget) {
                    nextStage();
                }
            } else if (currentStage == 3) {  // Move forward, dragging foundation
                driveX = -.3;
                driveY = .75;
                targetHeading = Math.PI/2;
                rotate = rotationTowardsTarget(1);
                stiltMode = clampMode;

                update();

                if (currentOffsetRight() >= 1700) {
                    nextStage();
                }
            } else if (currentStage == 4) {  // Move backward, turning foundation
                driveX = 0.7;
                driveY = 0;
                targetHeading = Math.PI/2;
                rotate = rotationTowardsTarget(1);
                stiltMode = clampMode;

                update();

                if (currentOffsetRight() >= 200) {
                    nextStage();
                }
            } else if (currentStage == 5) {  // Move stilts up, un-clamp
                driveX = 0;
                driveY = 0;
                targetHeading = Math.PI;
                rotate = rotationTowardsTarget(.1);
                stiltMode = upMode;

                update();

                if (robot.stilt.atTarget) {
                    nextStage();
                }
            } else if (currentStage == 6) {  // Move rightward
                driveX = -.5;
                driveY = 0;
                targetHeading = Math.PI;
                rotate = rotationTowardsTarget(.1);
                stiltMode = StiltComponent.ZERO_MODE;

                update();

                if (currentOffsetRight() >= 400) {
                    nextStage();
                }
            } else if (currentStage == 7) {  // Move rightward
                driveX = -.4;
                driveY = 0;
                targetHeading = Math.PI;
                rotate = rotationTowardsTarget(.1);
                stiltMode = StiltComponent.ZERO_MODE;

                update();

                if (currentOffsetRight() >= 1200) {
                    nextStage();
                }
            } else {
                update();
            }
        }

        telemetry.update();
    }

}
