
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.components.IntakeComponent;
import org.firstinspires.ftc.teamcode.hardware.components.StiltComponent;


@TeleOp(name="Julian's Second Op Mode", group="Iterative Opmode")
public class SecondOpMode extends AbstractOpMode
{
    final int INTAKE_MODE = 0;   // intake mode (front)
    final int STACK_MODE = 1;  // stilt/stacking mode (back)

    int operationMode = INTAKE_MODE;
    boolean useGyro = false;

    @Override
    public void loop() {
        super.loop();

        if (gamepad2.b)
            useGyro = true;
        if (gamepad2.x)
            useGyro = false;
        if (gamepad2.y)
            offsetGyro = heading;

        telemetry.addData("Gyro", "Using gyro? %s; offsetGyro: (%f)", useGyro, offsetGyro);

        if (gamepad1.left_bumper) {
            operationMode = INTAKE_MODE;

            robot.stilt.setTargetMode(StiltComponent.INTAKE_MODE);
        }
        if (gamepad1.right_bumper) {
            operationMode = STACK_MODE;

            robot.stilt.setTargetMode(StiltComponent.STACK_MODE);
        }

        boolean gateForward = false;
        boolean gateBackward = false;
        if (operationMode == STACK_MODE) {
            if (gamepad1.left_stick_y > 0.1) {
                gateForward = true;
            } else if (gamepad1.left_stick_y < -0.1) {
                gateBackward = true;
            }
        }

        boolean stackerUp = false;
        boolean stackerDown = false;
        if (operationMode == STACK_MODE) {
            stackerUp = gamepad1.dpad_up;
            stackerDown = gamepad1.dpad_down;
        }

        boolean intakeIn = false;
        boolean intakeOut = false;

        boolean intakeLow = false;
        boolean intakeMid = false;
        boolean intakeHigh = false;
        double intakeTweak = 0;
        if (operationMode == INTAKE_MODE) {
            intakeLow = gamepad1.a;
            intakeMid = gamepad1.b;
            intakeHigh = gamepad1.y;
            intakeIn = gamepad1.dpad_down;
            intakeOut = gamepad1.dpad_up;
            intakeTweak = gamepad1.right_stick_y;
        }

        boolean stiltMode = false;
        boolean stiltMode0 = false;
        boolean stiltMode1 = false;
        boolean stiltMode2 = false;
        if (operationMode == STACK_MODE) {
            stiltMode = gamepad1.x;
            stiltMode0 = gamepad1.a;
            stiltMode1 = gamepad1.b;
            stiltMode2 = gamepad1.y;
        }

        double driveStick_X = gamepad2.right_stick_x;
        double driveStick_Y = gamepad2.right_stick_y;
        double rotateStick_X = gamepad2.left_stick_x;

        // Gate Component

        if (robot.gate != null) {
            if (gateForward)
                robot.gate.setForward();
            else if (gateBackward)
                robot.gate.setBackward();
            else
                robot.gate.setOff();
            robot.gate.addData(telemetry);
        }

        // Stacker Component

        if (robot.stacker != null) {
            if (stackerUp)
                robot.stacker.setUp();
            else if (stackerDown)
                robot.stacker.setDown();
            else
                robot.stacker.setOff();
            robot.stacker.addData(telemetry);
        }

        // Intake Component

        if (robot.intake != null) {

            robot.intake.setIntakeOff();
            if (intakeIn)
                robot.intake.setIntakeIn();
            else if (intakeOut)
                robot.intake.setIntakeOut();

            if (intakeLow)
                robot.intake.setTargetPosition(IntakeComponent.LOW_POSITION);
            if (intakeMid)
                robot.intake.setTargetPosition(IntakeComponent.MID_POSITION);
            if (intakeHigh)
                robot.intake.setTargetPosition(IntakeComponent.HIGH_POSITION);

            if (intakeTweak < 0) {
                robot.intake.tweakTargetPositionDown();
            } else if (intakeTweak > 0) {
                robot.intake.tweakTargetPositionUp();
            }

            robot.intake.addData(telemetry);
        }

        // Stilt Component

        if (robot.stilt != null) {
            if (stiltMode)
                robot.stilt.setTargetMode(StiltComponent.STACK_MODE);
            if (stiltMode0)
                robot.stilt.setTargetMode(StiltComponent.STACK0_MODE);
            if (stiltMode1)
                robot.stilt.setTargetMode(StiltComponent.STACK1_MODE);
            if (stiltMode2)
                robot.stilt.setTargetMode(StiltComponent.STACK2_MODE);
            robot.stilt.update();
            robot.stilt.go();
            robot.stilt.addData(telemetry);
        }

        // Swerve Drives

        if (robot.swerveDrive != null) {
            if (useGyro)
                robot.swerveDrive.drive(driveStick_X, driveStick_Y,  rotateStick_X, getAdjustedHeading());
            else
                robot.swerveDrive.drive(driveStick_X, driveStick_Y,  rotateStick_X);
            robot.swerveDrive.addData(telemetry);
        }

        // Update telemetry

        telemetry.update();

    }

}
