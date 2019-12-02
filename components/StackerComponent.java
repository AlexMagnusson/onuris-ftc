package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class StackerComponent extends Component {

    double power = 0;

    DcMotor motor;

    public StackerComponent(DcMotor motor) {
        this.motor = motor;

        this.motor.setDirection(DcMotor.Direction.FORWARD);
    }

    public void setUp() {
        power = 1;
    }
    public void setDown() {
        power = -1;
    }

    public void update() {
    }

    public void go() {
        motor.setPower(power);
    }

    public void addData(Telemetry telemetry) {
        telemetry.addData("Stacker Component",
                "power: (%.2f)",
                power);
    }
}
