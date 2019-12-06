package org.firstinspires.ftc.teamcode.hardware.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class StiltComponent extends Component {

    // Static variables
    // Modes
    public static final Vector ZERO_MODE = new Vector(0, 0);
    public static final Vector INTAKE_MODE = new Vector(0, 0);
    public static final Vector STACK_MODE = new Vector(-13000,  2700);
    public static final Vector STACK0_MODE = new Vector(-13000,  950);
    public static final Vector STACK1_MODE = new Vector(-7000,  950);
    public static final Vector STACK2_MODE = new Vector(-12000,  950);

    // Hardware devices
    CRServo foot_servo;
    DcMotor foot_encoder;
    CRServo wheel_servo;
    DcMotor wheel_encoder;

    // Instance variables

    Vector targetMode = ZERO_MODE;
    public boolean atTarget = true;

    double footPower = 0;
    double wheelPower = 0;

    public StiltComponent(CRServo foot_servo, DcMotor foot_encoder, CRServo wheel_servo, DcMotor wheel_encoder) {
        this.foot_servo = foot_servo;
        this.foot_encoder = foot_encoder;
        this.wheel_servo = wheel_servo;
        this.wheel_encoder = wheel_encoder;

        this.foot_servo.setDirection(DcMotor.Direction.FORWARD);
        this.wheel_servo.setDirection(DcMotor.Direction.FORWARD);
    }

    public void setTargetMode(Vector targetMode) {
        this.targetMode = targetMode;
    }

    private double getEncoderHeight(DcMotor encoder) {
        return encoder.getCurrentPosition();
    }
    private Vector currentHeightVector() {
        return new Vector(getEncoderHeight(this.foot_encoder), getEncoderHeight(this.wheel_encoder));
    }

    public void update() {
        double toleranceFoot = 100;
        double slowdownFactorFoot = 200.;
        double toleranceWheel = 100;
        double slowdownFactorWheel = 200.;

        Vector current_vector = currentHeightVector();
        Vector target_vector = targetMode;

        double current_foot = current_vector.x;
        double target_foot = target_vector.x;
        double current_wheel = current_vector.y;
        double target_wheel = target_vector.y;

        double foot_diff = target_foot-current_foot;
        double wheel_diff = target_wheel-current_wheel;
        if (Math.abs(foot_diff) < toleranceFoot) {
            footPower = 0;
        } else {
            footPower = Range.clip(foot_diff / slowdownFactorFoot, -1, 1);
        }
        if (Math.abs(wheel_diff) < toleranceWheel) {
            wheelPower = 0;
        } else {
            wheelPower = Range.clip(wheel_diff / slowdownFactorWheel, -1, 1);
        }
        atTarget = (wheelPower == 0 && footPower == 0);
    }

    public void go() {
        foot_servo.setPower(footPower);
        wheel_servo.setPower(wheelPower);
    }

    public void addData(Telemetry telemetry) {
        Vector height_vector = currentHeightVector();
        telemetry.addData("Stilt Component",
                "footPower: (%.2f), footHeight: (%.2f), rightPower: (%.2f), rightHeight: (%.2f), \ntargetMode: (%s,%s), atTarget: %s",
                footPower, height_vector.x, wheelPower, height_vector.y, targetMode.x, targetMode.y, atTarget);
    }

}
