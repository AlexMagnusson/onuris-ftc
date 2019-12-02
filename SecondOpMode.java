
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.IntakeComponent;
import org.firstinspires.ftc.teamcode.components.StiltComponent;


@TeleOp(name="Julian's Second Op Mode", group="Iterative Opmode")
public class SecondOpMode extends AbstractOpMode
{
    final int INTAKE_MODE = 0;   // intake mode (front)
    final int STACK_MODE = 1;  // stilt/stacking mode (back)

    int operationMode = INTAKE_MODE;

    @Override
    public void loop() {

        if (gamepad1.left_bumper) {
            operationMode = INTAKE_MODE;

            stiltComponent.setTargetMode(StiltComponent.INTAKE_MODE);
        }
        if (gamepad1.right_bumper) {
            operationMode = STACK_MODE;

            stiltComponent.setTargetMode(StiltComponent.STACK_MODE);
        }


        boolean stackerUp;
        boolean stackerDown;
        if (operationMode == STACK_MODE) {
            stackerUp = gamepad1.dpad_up;
            stackerDown = gamepad1.dpad_down;
        } else {
            stackerUp = false;
            stackerDown = false;
        }

        boolean intakeIn;
        boolean intakeOut;

        boolean intakeLow;
        boolean intakeMid;
        boolean intakeHigh;
        double intakeTweak;
        if (operationMode == INTAKE_MODE) {
            intakeLow = gamepad1.a;
            intakeMid = gamepad1.b;
            intakeHigh = gamepad1.y;
            intakeIn = gamepad1.dpad_down;
            intakeOut = gamepad1.dpad_up;
            intakeTweak = gamepad1.right_stick_y;
        } else {
            intakeIn = false;
            intakeOut = false;
            intakeLow = false;
            intakeMid = false;
            intakeHigh = false;
            intakeTweak = 0;
        }

        boolean stiltMode;
        boolean stiltMode0;
        boolean stiltMode1;
        boolean stiltMode2;
        if (operationMode == STACK_MODE) {
            stiltMode = gamepad1.x;
            stiltMode0 = gamepad1.a;
            stiltMode1 = gamepad1.b;
            stiltMode2 = gamepad1.y;
        } else {
            stiltMode = false;
            stiltMode0 = false;
            stiltMode1 = false;
            stiltMode2 = false;
        }

        double driveStick_X = gamepad2.right_stick_x;
        double driveStick_Y = gamepad2.right_stick_y;
        double rotateStick_X = gamepad2.left_stick_x;

        // Stacker Component

        if (stackerComponent != null) {
            if (stackerUp)
                stackerComponent.setUp();
            if (stackerDown)
                stackerComponent.setDown();
            stackerComponent.update();
            stackerComponent.go();
            stackerComponent.addData(telemetry);
        }

        // Intake Component

        if (intakeComponent != null) {

            intakeComponent.setIntakeOff();
            if (intakeIn)
                intakeComponent.setIntakeIn();
            else if (intakeOut)
                intakeComponent.setIntakeOut();

            if (intakeLow)
                intakeComponent.setTargetPosition(IntakeComponent.LOW_POSITION);
            if (intakeMid)
                intakeComponent.setTargetPosition(IntakeComponent.MID_POSITION);
            if (intakeHigh)
                intakeComponent.setTargetPosition(IntakeComponent.HIGH_POSITION);

            intakeComponent.tweakPosition(intakeTweak);

            intakeComponent.update();
            intakeComponent.go();
            intakeComponent.addData(telemetry);
        }

        // Stilt Component

        if (stiltComponent != null) {
            if (stiltMode)
                stiltComponent.setTargetMode(StiltComponent.STACK_MODE);
            if (stiltMode0)
                stiltComponent.setTargetMode(StiltComponent.STACK0_MODE);
            if (stiltMode1)
                stiltComponent.setTargetMode(StiltComponent.STACK1_MODE);
            if (stiltMode2)
                stiltComponent.setTargetMode(StiltComponent.STACK2_MODE);
            stiltComponent.update();
            stiltComponent.go();
            stiltComponent.addData(telemetry);
        }

        // Swerve Drives

        if (leftSwerveDrive != null) {
            leftSwerveDrive.update(driveStick_X, driveStick_Y, -rotateStick_X);
            leftSwerveDrive.go();
            leftSwerveDrive.addData(telemetry);
        }

        if (rightSwerveDrive != null) {
            rightSwerveDrive.update(driveStick_X, driveStick_Y, rotateStick_X);
            rightSwerveDrive.go();
            rightSwerveDrive.addData(telemetry);
        }


        // Update telemetry

        telemetry.update();

    }

}
