
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

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        // Stacker Component

        if (stackerComponent != null) {
            stackerComponent.update();
            stackerComponent.go();
            stackerComponent.addData();
        }

        // Intake Component

        if (intakeComponent != null) {
            intakeComponent.update();
            intakeComponent.go();
            intakeComponent.addData();
        }

        // Stilt Component

        if (stiltComponent != null) {
            stiltComponent.update();
            stiltComponent.go();
            stiltComponent.addData();
        }

        // Left Swerve Drive

        if (leftSwerveDrive != null) {
            leftSwerveDrive.update();
            leftSwerveDrive.go();
            leftSwerveDrive.addData();
        }

        // Right Swerve Drive

        if (rightSwerveDrive != null) {
            rightSwerveDrive.update();
            rightSwerveDrive.go();
            rightSwerveDrive.addData();
        }


        // Update telemetry

        telemetry.update();

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }


}
