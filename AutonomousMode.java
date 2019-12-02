
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


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

    private void update() {
        leftSwerveDrive.update(driveX, driveY, rotate);
        leftSwerveDrive.go();
        leftSwerveDrive.addData(telemetry);

        rightSwerveDrive.update(driveX, driveY, rotate);
        rightSwerveDrive.go();
        rightSwerveDrive.addData(telemetry);

        stiltComponent.setCurrentMode(stiltMode);
        stiltComponent.adjustPower();
        stiltComponent.go();
        stiltComponent.addData(telemetry);
    }

    @Override
    public void loop() {
        currentRightPos = rightSwerveDrive.motor2.getCurrentPosition();
        currentLeftPos = leftSwerveDrive.motor2.getCurrentPosition();

        telemetry.addData("STAGE", "Stage %s, rightPosition: (%f), leftPosition: (%f)", currentStage, currentRightPos, currentLeftPos);

        driveX = 0;
        driveY = 0;
        rotate = 0;
        stiltMode = stiltComponent.ZERO_MODE;

        Vector clampMode = new Vector(-1600, 1600);

        if (waiting > 0) {
            waiting -= 1;
        } else {
            if (currentStage == 1) {
                driveX = 0;
                driveY = .8;
                rotate = 0;
                stiltMode = new Vector(-3000, 3000);

                update();

                if (currentOffsetRight() >= 1500) {
                    nextStage();
                }
            } else if (currentStage == 2) {
                driveX = 0;
                driveY = 0;
                rotate = 0;
                stiltMode = clampMode;

                update();

                if (stiltComponent.atMode) {
                    nextStage();
                }
            } else if (currentStage == 3) {
                driveX = 0;
                driveY = -1;
                rotate = 0;
                stiltMode = clampMode;

                update();

                if (currentOffsetLeft() >= 1000) {
                    nextStage();
                }
            } else if (currentStage == 4) {
                driveX = 0;
                driveY = .8;
                rotate = 0;
                stiltMode = clampMode;

                update();

                if (currentOffsetLeft() >= 1000) {
                    nextStage();
                }
            } else if (currentStage == 5) {
                driveX = 0;
                driveY = 0;
                rotate = 0;
                stiltMode = new Vector(-3000, 3000);

                update();

                if (stiltComponent.atMode) {
                    nextStage();
                }
            } else if (currentStage == 6) {
                driveX = 0;
                driveY = -0.8;
                rotate = 0;
                stiltMode = stiltComponent.INTAKE_MODE;

                update();

                if (currentOffsetLeft() >= 2500) {
                    nextStage();
                }
            } else {
                update();
            }
        }
    }

    private double currentOffsetRight() {
        return Math.abs(currentLeftPos-initialLeftPos);
    }

    private double currentOffsetLeft() {
        return Math.abs(currentRightPos-initialRightPos);
    }

    private void nextStage() {
        initialRightPos = currentRightPos;
        initialLeftPos = currentLeftPos;
        currentStage += 1;
        waiting = 500;
    }


}
