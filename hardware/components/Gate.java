package org.firstinspires.ftc.teamcode.hardware.components;

import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcore.external.Telemetry;


public class Gate extends Component {

    CRServo servo;

    public Gate(CRServo servo) {
        this.servo = servo;
    }

    public double getPower() {
        return servo.getPower();
    }

    public void setPower(double power) {
        servo.setPower(power);
    }
    public void setForward() {
        setPower(0.5);
    }
    public void setBackward() {
        setPower(-0.5);
    }
    public void setOff() {
        setPower(0);
    }

    public void addData(Telemetry telemetry) {
        telemetry.addData("Gate",
                "power: (%.2f)",
                getPower());
    }

}
