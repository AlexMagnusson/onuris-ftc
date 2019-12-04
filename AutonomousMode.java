
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.components.StiltComponent;
import org.firstinspires.ftc.teamcode.components.Vector;


@Autonomous(name="Julian's Auto Mode")
public class AutonomousMode extends AbstractOpMode
{

    int currentStage = 1;
    int waiting = 0;

    double initialRightPos = 0;
    double initialLeftPos = 0;

    double currentRightPos = 0;
    double currentLeftPos = 0;

    double driveX;
    double driveY;
    double rotate;
    Vector stiltMode;

    private void updateSwerves() {
        swerveDrive.drive(driveX, driveY, rotate, heading);
        swerveDrive.addData(telemetry);
    }

    private void updateStilts() {
        stiltComponent.setTargetMode(stiltMode);
        stiltComponent.update();
        stiltComponent.go();
        stiltComponent.addData(telemetry);
    }

    private void update() {
        updateSwerves();
        updateStilts();
    }

    @Override
    public void loop() {
        super.loop();

        currentRightPos = swerveDrive.right.getMotorPosition();
        currentLeftPos = swerveDrive.left.getMotorPosition();

        telemetry.addData("STAGE", "Stage %s, rightPosition: (%f), leftPosition: (%f)", currentStage, currentRightPos, currentLeftPos);

        driveX = 0;
        driveY = 0;
        rotate = 0;
        stiltMode = StiltComponent.ZERO_MODE;

        Vector clampMode = new Vector(-1600, 1600);
        Vector upMode = new Vector(-3000, 3000);

        if (waiting > 0) {
            waiting -= 1;
            updateSwerves();
        } else {
            if (currentStage == 1) {  // Move backward towards foundation
                driveX = 0;
                driveY = .8;
                rotate = 0;
                stiltMode = upMode;

                update();

                if (currentOffsetRight() >= 1500) {
                    nextStage();
                }
            } else if (currentStage == 2) {  // Drop the stilts, clamp
                driveX = 0;
                driveY = 0;
                rotate = 0;
                stiltMode = clampMode;

                update();

                if (stiltComponent.atTarget) {
                    nextStage();
                }
            } else if (currentStage == 3) {  // Move forward, dragging foundation
                driveX = 0;
                driveY = -1;
                rotate = 0;
                stiltMode = clampMode;

                update();

                if (currentOffsetRight() >= 1500) {
                    nextStage();
                }
            } else if (currentStage == 4) {  // Move stilts up, un-clamp
                driveX = 0;
                driveY = 0;
                rotate = 0;
                stiltMode = upMode;

                update();

                if (stiltComponent.atTarget) {
                    nextStage();
                }
            } else if (currentStage == 5) {  // Move rightward
                driveX = -.8;
                driveY = 0;
                rotate = 0;
                stiltMode = upMode;

                update();

                if (currentOffsetRight() >= 1500) {
                    nextStage();
                }
            } else {
                update();
            }
        }
    }

    private double currentOffsetLeft() {
        return Math.abs(currentLeftPos-initialLeftPos);
    }

    private double currentOffsetRight() {
        return Math.abs(currentRightPos-initialRightPos);
    }

    private void nextStage() {
        initialRightPos = currentRightPos;
        initialLeftPos = currentLeftPos;
        currentStage += 1;
        waiting = 500;
    }


}
