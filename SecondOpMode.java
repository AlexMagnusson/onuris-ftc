
package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


class SwerveDrive {

    OpMode opmode;

    DcMotor motor1;  // Motor with encoder
    DcMotor motor2;
    CRServo servo;

    SwerveDrive(OpMode opmode, DcMotor motor1, DcMotor motor2, CRServo servo) {
        this.opmode = opmode;
        this.motor1  = motor1;
        this.motor2 = motor2;
        this.servo = servo;

        this.motor1.setDirection(DcMotor.Direction.FORWARD);
        this.motor2.setDirection(DcMotor.Direction.FORWARD);
        this.servo.setDirection(CRServo.Direction.FORWARD);
    }

    double currentRotation() {
        return this.motor1.getCurrentPosition();
    }

    void update() {
        double x = this.opmode.gamepad1.right_stick_x;
        double y = this.opmode.gamepad1.right_stick_y;
        double angle = Math.atan2(y, x);
        double magnitude = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));

        double targetRotation = angle/(2*Math.PI);
        double drivePower = Range.clip(magnitude, -1.0, 1.0) ;

        this.motor1.setPower(drivePower);
        this.motor2.setPower(drivePower);

        double error = targetRotation - this.currentRotation();
        double power = Range.clip(error, -1, 1);
        this.servo.setPower(power);

        this.opmode.telemetry.addData("Swerve Drive",
                "currentRotation: %d",
                this.currentRotation());
    }
}


@TeleOp(name="Julian's Second Op Mode", group="Iterative Opmode")
public class SecondOpMode extends OpMode
{

    // CONSTANTS

    private ElapsedTime runtime = new ElapsedTime();


    // Swerve Drive
    private SwerveDrive leftSwerveDrive = null;

    @Override
    public void init() {

        // Initialize devices

        DcMotor motor1 = hardwareMap.dcMotor.get("swerve_motor1");
        DcMotor motor2 = hardwareMap.dcMotor.get("swerve_motor2");
        CRServo servo = hardwareMap.crservo.get("swerve_servo");

        leftSwerveDrive = new SwerveDrive(this, motor1, motor2, servo);

        telemetry.addData("Status", "Initialized");
    }

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

        leftSwerveDrive.update();
        telemetry.update();

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }


}
