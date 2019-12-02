
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;



@TeleOp(name="Julian's Second Op Mode", group="Iterative Opmode")
public class SecondOpMode extends AbstractOpMode
{
    final int FRONT_MODE = 0;   // intake mode
    final int BACK_MODE = 1;  // stilt/stacking mode

    int operationMode = FRONT_MODE;

    @Override
    public void loop() {

        if (gamepad1.left_bumper) {
            operationMode = FRONT_MODE;

            stiltComponent.setCurrentMode(stiltComponent.INTAKE_MODE);
        }
        if (gamepad1.right_bumper) {
            operationMode = BACK_MODE;

            stiltComponent.setCurrentMode(stiltComponent.STACK_MODE);
        }


        boolean stackerUp;
        boolean stackerDown;
        if (operationMode == BACK_MODE) {
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
        if (operationMode == FRONT_MODE) {
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
        if (operationMode == BACK_MODE) {
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
            stackerComponent.update(stackerUp, stackerDown);
            stackerComponent.go();
            stackerComponent.addData(telemetry);
        }

        // Intake Component

        if (intakeComponent != null) {
            intakeComponent.update(intakeIn, intakeOut, intakeLow, intakeMid, intakeHigh, intakeTweak);
            intakeComponent.go();
            intakeComponent.addData(telemetry);
        }

        // Stilt Component

        if (stiltComponent != null) {
            stiltComponent.update(stiltMode, stiltMode0, stiltMode1, stiltMode2);
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
