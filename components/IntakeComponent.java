package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeComponent extends Component {

    public static final double LOW_POSITION = 0;
    public static final double MID_POSITION = 200;
    public static final double HIGH_POSITION = 400;

    double targetPosition = LOW_POSITION;
    public boolean atTarget = false;

    double intakePower = 0;
    double verticalPower = 0;

    DcMotor motor1;  // Left intake motor
    DcMotor motor2;  // Right intake motor
    DcMotor motor3;  // Vertical movement motor
    DcMotor encoder;

    public IntakeComponent(DcMotor motor1, DcMotor motor2, DcMotor motor3, DcMotor encoder_motor) {
        this.motor1 = motor1;
        this.motor2 = motor2;
        this.motor3 = motor3;
        this.encoder = encoder_motor;

        this.motor1.setDirection(DcMotor.Direction.FORWARD);
        this.motor2.setDirection(DcMotor.Direction.FORWARD);
        this.motor3.setDirection(CRServo.Direction.FORWARD);
    }

    public void setTargetPosition(double targetPosition) {
        this.targetPosition = targetPosition;
    }

    private double getCurrentPosition() {
        return encoder.getCurrentPosition();
    }

    public void update() {
        double tolerance = 10;
        double slowdownFactor = 20.;

        double currentPosition = getCurrentPosition();

        double position_diff = targetPosition-currentPosition;
        if (Math.abs(position_diff) < tolerance) {
            verticalPower = 0;
        } else {
            verticalPower = Range.clip(position_diff / slowdownFactor, -1, 1);
        }
        atTarget = verticalPower == 0;
    }

    public void setIntakeOff() {
        intakePower = 0;
    }
    public void setIntakeIn() {
        intakePower = -1;
    }
    public void setIntakeOut() {
        intakePower = 1;
    }

    public void tweakPosition(double tweak) {
        if (tweak < 0)
            targetPosition += 1;
        else if (tweak > 0)
            targetPosition -= 1;
    }

    public void go() {
        motor1.setPower(intakePower);
        motor2.setPower(-intakePower);
        motor3.setPower(verticalPower);
    }

    public void addData(Telemetry telemetry) {
        telemetry.addData("Intake Component",
                "intakePower: (%f), verticalPower: (%.2f), currentPosition: (%.2f), targetPosition: (%.2f), atTarget %s",
                intakePower, verticalPower, getCurrentPosition(), targetPosition, atTarget);
    }

}