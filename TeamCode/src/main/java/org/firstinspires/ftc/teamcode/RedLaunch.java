package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.opMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous(name="Red Auto Launch")

public class RedLaunch extends LinearOpMode {

    RobotBase robot;
    RedLaunch.AutonomousState autonomousState;
    protected LinearOpMode myOpMode = null;

    protected boolean fieldCentric = false;
    protected IMU imu = null;

    public RedLaunch(LinearOpMode opMode, boolean isFC)
    {
        robot = new RobotBase(this, false);
        myOpMode = opMode;
        fieldCentric = isFC;

    }

    protected DcMotor leftFrontDrive = null;
    protected DcMotor leftRearDrive = null;
    protected DcMotor rightFrontDrive = null;
    protected DcMotor rightRearDrive = null;

    protected DcMotorEx flyWheelLeft = null;
    protected DcMotorEx flyWheelRight = null;


    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        waitForStart();

        robot.drive(.5, 0, 0);

        sleep(1500);

        robot.drive(0, 0, 0);

        //TODO: type can be changed depending on needs
        //robot.launch(true);

    }
    /*void exitLaunchZone ()
    {

        robot.drive(0, 0, -.5);

        sleep(1500);

        robot.drive(0,0,0);

        robot.drive(0, -.5, 0);

        sleep(1500);

        robot.drive(0, 0, 0);
    }*/

    double shotsToFire = 2;

    private enum AutonomousState {
        LAUNCH,
        WAIT_FOR_LAUNCH,
        DRIVING_AWAY_FROM_GOAL,
        ROTATING,
        DRIVING_OFF_LINE,
        COMPLETE;
    }

    @Override
    public void loop() {
        /*
         * TECH TIP: Switch Statements
         * switch statements are an excellent way to take advantage of an enum. They work very
         * similarly to a series of "if" statements, but allow for cleaner and more readable code.
         * We switch between each enum member and write the code that should run when our enum
         * reflects that state. We end each case with "break" to skip out of checking the rest
         * of the members of the enum for a match, since if we find the "break" line in one case,
         * we know our enum isn't reflecting a different state.
         */
        switch (autonomousState){
            /*
             * Since the first state of our auto is LAUNCH, this is the first "case" we encounter.
             * This case is very simple. We call our .launch() function with "true" in the parameter.
             * This "true" value informs our launch function that we'd like to start the process of
             * firing a shot. We will call this function with a "false" in the next case. This
             * "false" condition means that we are continuing to call the function every loop,
             * allowing it to cycle through and continue the process of launching the first ball.
             */
            case LAUNCH:
                robot.launch(true);
                autonomousState = RedLaunch.AutonomousState.WAIT_FOR_LAUNCH;
                break;

            case WAIT_FOR_LAUNCH:
                /*
                 * A technique we leverage frequently in this code are functions which return a
                 * boolean. We are using this function in two ways. This function actually moves the
                 * motors and servos in a way that launches the ball, but it also "talks back" to
                 * our main loop by returning either "true" or "false". We've written it so that
                 * after the shot we requested has been fired, the function will return "true" for
                 * one cycle. Once the launch function returns "true", we proceed in the code, removing
                 * one from the shotsToFire variable. If shots remain, we move back to the LAUNCH
                 * state on our state machine. Otherwise, we reset the encoders on our drive motors
                 * and move onto the next state.
                 */
                if(robot.launch(false)) {
                    shotsToFire -= 1;
                    if(shotsToFire > 0) {
                        autonomousState = RedLaunch.AutonomousState.LAUNCH;
                    } else {
                        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        flyWheelLeft.setVelocity(0);
                        leftRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        rightRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        flyWheelRight.setVelocity(0);
                        autonomousState = RedLaunch.AutonomousState.DRIVING_AWAY_FROM_GOAL;
                    }
                }
}
