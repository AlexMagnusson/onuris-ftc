package org.firstinspires.ftc.teamcode.components;

import org.firstinspires.ftc.robotcore.external.Telemetry;

abstract class Component {
    public abstract void update();
    public abstract void go();
    public abstract void addData(Telemetry telemetry);

}

