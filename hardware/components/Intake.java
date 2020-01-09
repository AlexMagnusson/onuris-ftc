package org.firstinspires.ftc.teamcode.hardware.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake extends Component {

    // Hardware devices
    DcMotor left;  // Left intake motor
    DcMotor right;  // Right intake motor
    CRServo lift;  // Vertical movement motor

    // Instance variables
    private double intakePower = 0;
    private double liftPower = 0;

    public Intake(DcMotor left, DcMotor right, CRServo lift) {
        this.left = left;
        this.right = right;
        this.lift = lift;

        this.left.setDirection(DcMotor.Direction.FORWARD);
        this.right.setDirection(DcMotor.Direction.FORWARD);
    }


    // Lift

    public void liftUp() {
        liftPower = .4;
    }
    public void liftDown() {
        liftPower = -.4;
    }
    public void setLiftPower() {
        lift.setPower(liftPower);
        liftPower = 0;
    }

    // Intake in/out

    public void setIntakePower(double power) {
        intakePower = power;
        left.setPower(intakePower);
        right.setPower(-intakePower);
    }
    public void setIntakeOff() {
        setIntakePower(0);
    }
    public void setIntakeIn() {
        setIntakePower(-1);
    }
    public void setIntakeOut() {
        setIntakePower(1);
    }


    // Telemetry

    public void addData(Telemetry telemetry) {
        telemetry.addData("Intake Component",
                "intakePower: (%f), liftPower: (%f)",
                intakePower, liftPower);
    }

}