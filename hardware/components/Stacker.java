package org.firstinspires.ftc.teamcode.hardware.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Stacker extends Component {

    // Hardware devices
    DcMotor motor;

    public Stacker(DcMotor motor) {
        this.motor = motor;

        this.motor.setDirection(DcMotor.Direction.FORWARD);
    }

    public double getPower() {
        return motor.getPower();
    }

    public void setUp() {
        setPower(1);
    }
    public void setDown() {
        setPower(-1);
    }
    public void setOff() {
        setPower(0);
    }

    public void setPower(double power) {
        motor.setPower(power);
    }

    public void addData(Telemetry telemetry) {
        telemetry.addData("Stacker Component",
                "power: (%.2f)",
                getPower());
    }
}
