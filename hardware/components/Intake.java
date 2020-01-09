package org.firstinspires.ftc.teamcode.hardware.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake extends Component {

    // Static variables
    // Positions
    public static final int LOW_POSITION = 0;
    public static final int MID_POSITION = 200;
    public static final int HIGH_POSITION = 400;

    // Hardware devices
    DcMotor left;  // Left intake motor
    DcMotor right;  // Right intake motor
    CRServo lift;  // Vertical movement motor

    // Instance variables
    private double intakePower = 0;

    public Intake(DcMotor left, DcMotor right, CRServo lift) {
        this.left = left;
        this.right = right;
        this.lift = lift;

        this.left.setDirection(DcMotor.Direction.FORWARD);
        this.right.setDirection(DcMotor.Direction.FORWARD);
    }


    // Lift

//    private int getTargetPosition() {
//        return lift.getTargetPosition();
//    }
//    private int getCurrentPosition() {
//        return lift.getCurrentPosition();
//    }
//
//    public boolean atTarget() {
//        return getCurrentPosition() == getTargetPosition();
//    }
//    public void setTargetPosition(int position) {
//        lift.setTargetPosition(position);
//        if (atTarget()) {
//            lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            lift.setPower(0);
//        } else {
//            lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            lift.setPower(1);
//        }
//    }
//
//    private void tweakTargetPosition(int offset) {
//        setTargetPosition(getTargetPosition()+offset);
//    }
//    public void tweakTargetPositionUp() {
//        tweakTargetPosition(-10);
//    }
//    public void tweakTargetPositionDown() {
//        tweakTargetPosition(10);
//    }


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
                "intakePower: (%f)",
                intakePower);
    }

}