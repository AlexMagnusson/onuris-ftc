package org.firstinspires.ftc.teamcode.hardware.components;


public class ErrorResponder {

    private double P;
    private double I;
    private double D;
    private boolean continuous = false;
    private double tolerance = 0.05;  // Percentage error that is considered on target, from 0-1
    private double maximumInput = 0.0;
    private double minimumInput = 0.0;
    private double maximumOutput = -1.0;
    private double minimumOutput = 1.0;

    private double input;
    private double target = 0.0;
    private double error = 0.0;
    private double prevError = 0.0;
    private double totalError = 0.0;
    private double result = 0.0;

    ErrorResponder(double p, double i, double d) {
        this.P = p;
        this.I = i;
        this.D = d;
    }

    private double constrainAbsVal(double val, double min, double max) {
        int sign = (val < 0) ? -1: 1;

        if (Math.abs(val) > max)
            val = max * sign;
        else if (Math.abs(val) < min)
            val = min * sign;

        return val;
    }

    // -------------------- SETTER METHODS --------------------

    // Set input
    public void setInput(double input) {
        if (maximumInput > minimumInput) {
            input = constrainAbsVal(input, minimumInput, maximumInput);
        }
        this.input = input;
    }

    // Set target
    public void setTarget(double target) {
        if (maximumInput > minimumInput) {
            target = constrainAbsVal(target, minimumInput, maximumInput);
        }
        this.target = target;
    }

    // Set input/output ranges
    public void setInputRange(double minimumInput, double maximumInput) {
        this.minimumInput = Math.abs(minimumInput);
        this.maximumInput = Math.abs(maximumInput);
    }
    public void setOutputRange(double minimumOutput, double maximumOutput) {
        this.minimumOutput = Math.abs(minimumOutput);
        this.maximumOutput = Math.abs(maximumOutput);
    }

    // Set tolerance
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    // Set continuous
    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }
    public void setContinuous() {
        this.setContinuous(true);
    }

    // -------------------- PID CONTROL --------------------

    public double getError() {
        return error;
    }

    public boolean onTarget() {
        return (Math.abs(error) < Math.abs(tolerance * (maximumInput - minimumInput)));
    }

    private void calculate() {
        // Calculate the error signal
        error = target - input;

        // If continuous, wrap around
        if (continuous) {
            if (Math.abs(error) > (maximumInput - minimumInput) / 2) {
                if (error > 0)
                    error = error - maximumInput + minimumInput;
                else
                    error = error + maximumInput - minimumInput;
            }
        }

        // Integrate the errors as long as the upcoming integrator does
        // not exceed the minimum and maximum output thresholds.
        if ((Math.abs(totalError + error) * I < maximumOutput)
                && (Math.abs(totalError + error) * I > minimumOutput))
            totalError += error;

        // Perform the primary PID calculation
        result = P * error + I * totalError + D * (error - prevError);

        // Set the current error to the previous error for the next cycle.
        prevError = error;

        // Make sure the final result is within bounds
        result = constrainAbsVal(result, minimumOutput, maximumOutput);
    }

    public double performPID() {
        calculate();
        return result;
    }
    public double performPID(double input) {
        setInput(input);
        return performPID();
    }

}
