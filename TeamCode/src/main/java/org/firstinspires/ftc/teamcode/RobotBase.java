package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class RobotBase extends DriveBase{

    //TODO: from carter - remember because changes
    final double FEED_TIME_SECONDS = 2.0; //The feeder servos run this long when a shot is requested.
    final double STOP_SPEED = 0.0; //We send this power to the servos when we want them to stop.
    final double FULL_SPEED = 1.0;
    final double LAUNCHDURATION_SECONDS = 2.0; //The amount of time to wait before turning off the flywheel

    /*
     * When we control our launcher motor, we are using encoders. These allow the control system
     * to read the current speed of the motor and apply more or less power to keep it at a constant
     * velocity. Here we are setting the target, and minimum velocity that the launcher should run
     * at. The minimum velocity is a threshold for determining when to fire.
     */

    //TODO: change launch velocities for code to actually work
    final double LAUNCHER_TARGET_VELOCITY = 1525; //flyWheel
    final double LAUNCHER_MIN_VELOCITY = 1425; //starts feeder

    ElapsedTime feederTimer = new ElapsedTime();
    ElapsedTime StopTimer = new ElapsedTime();

    //mine again - this is fine
    protected CRServo feeder = null;
    protected DcMotorEx flyWheelLeft = null;
    protected DcMotorEx flyWheelRight = null;
    protected DcMotor intake1_2 = null;
    public RobotBase(LinearOpMode opMode, boolean isFc)
    {
        super(opMode,isFc);
    }
    public void init()
    {
        super.init();
        feeder = myOpMode.hardwareMap.get(CRServo.class, "feeder");
        flyWheelLeft = myOpMode.hardwareMap.get(DcMotorEx.class, "flyWheelLeft");
        flyWheelRight = myOpMode.hardwareMap.get(DcMotorEx.class, "flyWheelRight");
        intake1_2 = myOpMode.hardwareMap.get(DcMotor.class,"intake1_2");

        feeder.setDirection(CRServo.Direction.REVERSE);
        flyWheelLeft.setDirection(DcMotor.Direction.REVERSE);
        flyWheelRight.setDirection(DcMotor.Direction.FORWARD);
        intake1_2.setDirection(DcMotor.Direction.REVERSE);
    }

    /*public void runIntakes(double intakePower)
    {
        //double feederPower = .7;
        //double flyWheelPower = .7;
        //double intake1_2Power = .7;
        intake1_2.setPower(intakePower);
    }*/
    private enum LaunchState {
        IDLE,
        SPIN_UP,
        LAUNCH,
        LAUNCHING,
    }

    private LaunchState launchState = LaunchState.IDLE;


    boolean launch(boolean shotRequested) { //type 0 for short, 1 for long
        //TODO: play with multiplier number to make code actually work
        switch (launchState) {
            case IDLE:
                if (shotRequested) {
                    launchState = LaunchState.SPIN_UP;
                }//if
                else if (StopTimer.seconds() > LAUNCHDURATION_SECONDS) {
                    flyWheelLeft.setVelocity(0);
                    flyWheelRight.setVelocity(0);
                }//else if
                break;
            case SPIN_UP:
                flyWheelLeft.setVelocity(LAUNCHER_TARGET_VELOCITY);
                flyWheelRight.setVelocity(LAUNCHER_TARGET_VELOCITY);
                if (flyWheelLeft.getVelocity() > LAUNCHER_MIN_VELOCITY && flyWheelRight.getVelocity() > LAUNCHER_MIN_VELOCITY) {
                    launchState = LaunchState.LAUNCH;
                }// if
                break;
            case LAUNCH:
                feeder.setPower(FULL_SPEED);
                feederTimer.reset();
                launchState = LaunchState.LAUNCHING;
                break;
            case LAUNCHING:
                if (feederTimer.seconds() > FEED_TIME_SECONDS) {
                    launchState = LaunchState.IDLE;
                    StopTimer.reset();
                    feeder.setPower(STOP_SPEED);
                }// if
                break;
        }

    /*public void runShooter(boolean isShooting){

    }*/
        myOpMode.telemetry.addData("State", launchState);
        myOpMode.telemetry.addData("Left Flywheel", flyWheelLeft.getVelocity());
        myOpMode.telemetry.addData("Right Flywheel",flyWheelRight.getVelocity());
        return shotRequested;
    }
    //
    void runIntake(double power)
    {
        intake1_2.setPower(power);

    }
}
