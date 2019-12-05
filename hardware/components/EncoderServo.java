package org.firstinspires.ftc.teamcode.hardware.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

public class EncoderServo extends Component {

    CRServo servo;
    DcMotor encoder;

    double targetPosition;

    public EncoderServo(CRServo servo, DcMotor encoder) {
        this.servo = servo;
        this.encoder = encoder;

        this.servo.setDirection(CRServo.Direction.FORWARD);
    }

    // Position
    public double getCurrentPosition() {
        return this.encoder.getCurrentPosition();
    }
    public void setTargetPosition(double pos) {
        targetPosition = pos;
    }

    // Power
    public double getPower() {
        return this.servo.getPower();
    }
    public void setPower(double power) {
        this.servo.setPower(power);
    }

}
