
package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opmodes.AbstractOpMode;
import org.firstinspires.ftc.teamcode.hardware.components.Intake;
import org.firstinspires.ftc.teamcode.hardware.components.Stilt;


@TeleOp(name="Julian's Second Op Mode", group="Iterative Opmode")
public class TeleOpMode extends AbstractOpMode
{
    boolean useGyro = false;

    @Override
    public void loop() {
        super.loop();

        if (gamepad2.b)
            useGyro = true;
        if (gamepad2.x)
            useGyro = false;
        if (gamepad2.y)
            robot.gyro.resetHeading();

        telemetry.addData("Gyro", "Using gyro? %s", useGyro);

        boolean intakeIn = false;
        boolean intakeOut = false;

        boolean intakeLow = false;
        boolean intakeMid = false;
        boolean intakeHigh = false;
        double intakeTweak = 0;

        intakeLow = gamepad1.a;
        intakeMid = gamepad1.b;
        intakeHigh = gamepad1.y;
        intakeIn = gamepad1.dpad_down;
        intakeOut = gamepad1.dpad_up;
        intakeTweak = gamepad1.right_stick_y;

        double driveStick_X = gamepad2.right_stick_x;
        double driveStick_Y = gamepad2.right_stick_y;
        double rotateStick_X = gamepad2.left_stick_x;

        telemetry.addData("Controller Inputs",
                "driveStick_X: (%f), driveStick_Y: (%f), rotateStick_X: (%f)",
                driveStick_X, driveStick_Y, rotateStick_X);

        // Intake Component

        if (robot.intake != null) {

            robot.intake.setIntakeOff();
            if (intakeIn)
                robot.intake.setIntakeIn();
            else if (intakeOut)
                robot.intake.setIntakeOut();

//            if (intakeLow)
//                robot.intake.setTargetPosition(Intake.LOW_POSITION);
//            if (intakeMid)
//                robot.intake.setTargetPosition(Intake.MID_POSITION);
//            if (intakeHigh)
//                robot.intake.setTargetPosition(Intake.HIGH_POSITION);
//
//            if (intakeTweak < 0) {
//                robot.intake.tweakTargetPositionDown();
//            } else if (intakeTweak > 0) {
//                robot.intake.tweakTargetPositionUp();
//            }

            robot.intake.addData(telemetry);
        }

        // Swerve Drives

        if (robot.swerveDrive != null) {
            if (useGyro)
                robot.swerveDrive.drive(driveStick_X, driveStick_Y, rotateStick_X, robot.gyro.getAdjustedHeading());
            else
                robot.swerveDrive.drive(driveStick_X, driveStick_Y, rotateStick_X);
            robot.swerveDrive.addData(telemetry);
        }

        // Update telemetry

        telemetry.update();

    }

}
