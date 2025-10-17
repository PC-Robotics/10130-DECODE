package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

public class RobotBase extends DriveBase{

    //TODO: from carter - remember because changes
    final double FEED_TIME_SECONDS = 0.20; //The feeder servos run this long when a shot is requested.
    final double STOP_SPEED = 0.0; //We send this power to the servos when we want them to stop.
    final double FULL_SPEED = 1.0;
    final double LAUNCHDURATION_SECONDS = 2.0; //The amount of time to wait before turning off the flywheel

    /*
     * When we control our launcher motor, we are using encoders. These allow the control system
     * to read the current speed of the motor and apply more or less power to keep it at a constant
     * velocity. Here we are setting the target, and minimum velocity that the launcher should run
     * at. The minimum velocity is a threshold for determining when to fire.
     */
    final double LAUNCHER_TARGET_VELOCITY = 1125;
    final double LAUNCHER_MIN_VELOCITY = 1075;

    ElapsedTime feederTimer = new ElapsedTime();
    ElapsedTime StopTimer = new ElapsedTime();

    //mine again - this is fine
    protected DcMotor feeder = null;
    protected DcMotorEx flyWheel = null;
    protected CRServo intake1_2 = null;
    public RobotBase(LinearOpMode opMode, boolean isFc)
    {
        super(opMode,isFc);
    }
    public void init()
    {
        feeder = myOpMode.hardwareMap.get(DcMotor.class, "feeder");
        flyWheel = myOpMode.hardwareMap.get(DcMotorEx.class, "flyWheel");
        intake1_2 = myOpMode.hardwareMap.get(CRServo.class,"intake1_2");

        feeder.setDirection(DcMotor.Direction.REVERSE);
        flyWheel.setDirection(DcMotor.Direction.REVERSE);
        intake1_2.setDirection(CRServo.Direction.FORWARD);
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

    private LaunchState launchState;


    void launch(boolean shotRequested, int type) { //type 0 for short, 1 for long

        int shotRange = 500*type;
        switch (launchState) {
            case IDLE:
                if (shotRequested) {
                    launchState = LaunchState.SPIN_UP;
                }//if
                else if (StopTimer.seconds() > LAUNCHDURATION_SECONDS) {
                    flyWheel.setVelocity(0);
                }//else if
                break;
            case SPIN_UP:
                flyWheel.setVelocity(LAUNCHER_TARGET_VELOCITY+shotRange);
                if (flyWheel.getVelocity() > LAUNCHER_MIN_VELOCITY+shotRange) {
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
    }
    //
    void runIntake(double power)
    {
        intake1_2.setPower(power);

    }
}
