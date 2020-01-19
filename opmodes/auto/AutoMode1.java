
package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.AbstractAutoMode;


@Autonomous(name="Julian's Auto Mode")
public class AutoMode1 extends AbstractAutoMode
{

    private void setDefaults() {
        robot.swerveDrive.rotate = 0;
        robot.swerveDrive.joystickX = 0;
        robot.swerveDrive.joystickY = 0;
        robot.intake.setLiftOff();
    }

    private void adjustByGyro() {
        robot.swerveDrive.adjustByGyro(robot.gyro.getAdjustedHeading());
    }

    @Override
    public void loop() {
        super.loop();

        setDefaults();

        if (waiting) {
            if (timeSpentWaiting() >= 4) {
                stopWaiting();
            }
        } else {
            if (stage == 1) {  // Move north to grab foundation
                robot.swerveDrive.joystickY = 0.5;
                adjustByGyro();

                if (getCurrentDistanceMoved() >= 1000)
                    nextStage();

            } else if (stage == 2) {  // Move south, pulling foundation
                robot.swerveDrive.joystickY = -0.5;
                adjustByGyro();

                if (getCurrentDistanceMoved() >= 500)
                    nextStage();

            } else if (stage == 3) {  // Turn right 90 degrees with foundation
                robot.swerveDrive.setRotateByTarget(robot.gyro.getAdjustedHeading(), -Math.PI/2);

                if (getTimeElapsed() >= 0.2)
                    nextStage();

            } else if (stage == 4) {  // Move east?, pushing foundation
                robot.swerveDrive.joystickX = -1;
                adjustByGyro();

                if (getCurrentDistanceMoved() >= 500)
                    nextStage();

            } else if (stage == 5) {  // Unlatch from foundation
                robot.intake.setLiftUp();
                if (getTimeElapsed() >= 2)
                    nextStage();
            }
        }
        robot.swerveDrive.drive();

        telemetry.addData("AUTONOMOUS", "stage: (%s), waiting: (%s)", stage, waiting);

        robot.intake.addData(telemetry);
        robot.swerveDrive.addData(telemetry);

        // Update telemetry
        telemetry.update();

    }

}
