/*
 * Copyright (c) 2025 Base 10 Assets, LLC
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of NAME nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="ModdedModdedStarterBotAuto")
//@Disabled
public class NoEncoderAhhhhhhhhh extends OpMode
{

    final double FEED_TIME = 1.20; //The feeder servos run this long when a shot is requested.
    final double LAUNCHER_TARGET_VELOCITY = 1400;
    final double LAUNCHER_MIN_VELOCITY = 1075;
    final double TIME_BETWEEN_SHOTS = 2;
    final double DRIVE_SPEED = 0.5;
    final double ROTATE_SPEED = 0.2;
    final double WHEEL_DIAMETER_MM = 96;
    final double ENCODER_TICKS_PER_REV = 537.7;
    final double TICKS_PER_MM = (ENCODER_TICKS_PER_REV / (WHEEL_DIAMETER_MM * Math.PI));
    final double TRACK_WIDTH_MM = 404;

    int shotsToFire = 2; //The number of shots to fire in this auto.

    double robotRotationAngle = 45;
    protected ElapsedTime shotTimer = new ElapsedTime();
    protected ElapsedTime feederTimer = new ElapsedTime();
    protected ElapsedTime driveTimer = new ElapsedTime();

    // Declare OpMode members.
    protected DcMotor leftFrontDrive = null;
    protected DcMotor rightFrontDrive = null;
    protected DcMotor leftRearDrive = null;
    protected DcMotor rightRearDrive = null;
    protected DcMotorEx flyWheelLeft = null;
    protected DcMotorEx flyWheelRight = null;

    protected CRServo feeder = null;

    private enum LaunchState {
        IDLE,
        PREPARE,
        LAUNCH,
    }

    private LaunchState launchState;

    double leftFrontPower;
    double rightFrontPower;
    double leftRearPower;
    double rightRearPower;

    private enum AutonomousState {

        MOVE_FORWARD,
        LAUNCH,
        WAIT_FOR_LAUNCH,
        STRAFE_RIGHT,
        COMPLETE;
    }

    private AutonomousState autonomousState;

    private enum Alliance {
        RED,
        BLUE;
    }

    private Alliance alliance = Alliance.RED;

    /*
     * This code runs ONCE when the driver hits INIT.
     */
    @Override
    public void init() {
        autonomousState = AutonomousState.MOVE_FORWARD;
        launchState = LaunchState.IDLE;

        leftFrontDrive  = hardwareMap.get(DcMotor.class, "leftFront");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFront");
        leftRearDrive  = hardwareMap.get(DcMotor.class, "leftRear");
        rightRearDrive = hardwareMap.get(DcMotor.class, "rightRear");
        flyWheelLeft = hardwareMap.get(DcMotorEx.class,"flyWheelLeft");
        flyWheelRight = hardwareMap.get(DcMotorEx.class,"flyWheelRight");
        feeder = hardwareMap.get(CRServo.class, "feeder");
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftRearDrive.setDirection(DcMotor.Direction.REVERSE);
        rightRearDrive.setDirection(DcMotor.Direction.FORWARD);
        flyWheelRight.setDirection(DcMotor.Direction.FORWARD);
        flyWheelLeft.setDirection(DcMotor.Direction.REVERSE);
        /*leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setZeroPowerBehavior(BRAKE);
        rightFrontDrive.setZeroPowerBehavior(BRAKE);
        leftRearDrive.setZeroPowerBehavior(BRAKE);
        rightRearDrive.setZeroPowerBehavior(BRAKE);
        flyWheelLeft.setZeroPowerBehavior(BRAKE);
        flyWheelRight.setZeroPowerBehavior(BRAKE);
        flyWheelLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flyWheelRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        flyWheelLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,new PIDFCoefficients(300,0,0,10));
        flyWheelRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,new PIDFCoefficients(300,0,0,10));
        */

        feeder.setDirection(DcMotorSimple.Direction.REVERSE);
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
        feeder.setPower(0);
        if (gamepad1.b) {
            alliance = Alliance.RED;
        } else if (gamepad1.x) {
            alliance = Alliance.BLUE;
        }

        telemetry.addData("Press square???", "for BLUE");
        telemetry.addData("Press nothing i think", "for RED");
        telemetry.addData("Selected Alliance", alliance);
    }

    @Override
    public void start() {
        driveTimer.reset();
    }
    @Override
    public void loop() {
        switch (autonomousState) {

            case MOVE_FORWARD:
                drive(.5, 0, 0);
                if (driveTimer.milliseconds() > 1000){
                    drive(0, 0, 0);
                    flyWheelLeft.setZeroPowerBehavior(BRAKE);
                    flyWheelRight.setZeroPowerBehavior(BRAKE);
                    autonomousState = AutonomousState.LAUNCH;
                }
                break;
            case LAUNCH:
                launch(true);
                autonomousState = AutonomousState.WAIT_FOR_LAUNCH;
                break;

            case WAIT_FOR_LAUNCH:
                if (launch(false)) {
                    shotsToFire -= 1;
                    if (shotsToFire > 0) {
                        autonomousState = AutonomousState.LAUNCH;
                    } else {
                        flyWheelLeft.setVelocity(0);
                        flyWheelRight.setVelocity(0);

                        // reset timer before strafing
                        driveTimer.reset();
                        //go to strafe
                        autonomousState = AutonomousState.STRAFE_RIGHT;
                    }
                }
                break;
/*
            case DRIVING_AWAY_FROM_GOAL:

                 * This is another function that returns a boolean. This time we return "true" if
                 * the robot has been within a tolerance of the target position for "holdSeconds."
                 * Once the function returns "true" we reset the encoders again and move on.

                if(drive(DRIVE_SPEED, -4, DistanceUnit.INCH, 1)){
                    leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    autonomousState = AutonomousState.ROTATING;
                }
                break;

            case ROTATING:
                if(alliance == Alliance.RED){
                    robotRotationAngle = 45;
                } else if (alliance == Alliance.BLUE){
                    robotRotationAngle = -45;
                }

                if(rotate(ROTATE_SPEED, robotRotationAngle, AngleUnit.DEGREES,1)){
                    leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    autonomousState = AutonomousState.DRIVING_OFF_LINE;
                }
                break;

            case DRIVING_OFF_LINE:

                if(drive(DRIVE_SPEED, -26, DistanceUnit.INCH, 1)){
                   autonomousState = AutonomousState.COMPLETE;
                }
                break;
        }

 */

            case STRAFE_RIGHT:
                /*leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                leftRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightRearDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);*/
                if(alliance == Alliance.RED){
                    drive(0, .5, 0);
                }else if(alliance == Alliance.BLUE){
                    drive(0, .5, 0);
                }

                if (driveTimer.milliseconds() > 500) {
                    drive(0, 0 , 0);
                    autonomousState = AutonomousState.COMPLETE;
                }
                break;
        }
        /*
         * Here is our telemetry that keeps us informed of what is going on in the robot. Since this
         * part of the code exists outside of our switch statement, it will run once every loop.
         * No matter what state our robot is in. This is the huge advantage of using state machines.
         * We can have code inside of our state machine that runs only when necessary, and code
         * after the last "case" that runs every loop. This means we can avoid a lot of
         * "copy-and-paste" that non-state machine autonomous routines fall into.
         */
        telemetry.addData("AutoState", autonomousState);
        telemetry.addData("LauncherState", launchState);
        /*telemetry.addData("Motor Current Positions", "left (%d), right (%d)",
                leftFrontDrive.getCurrentPosition(), leftRearDrive.getCurrentPosition(), rightFrontDrive.getCurrentPosition(), rightRearDrive.getCurrentPosition());
        */telemetry.addData(
                "Motor Current Positions",
                "LF(%d), LR(%d), RF(%d), RR(%d)",
                leftFrontDrive.getCurrentPosition(),
                leftRearDrive.getCurrentPosition(),
                rightFrontDrive.getCurrentPosition(),
                rightRearDrive.getCurrentPosition());
        telemetry.addData("Motor Target Positions", "left (%d), right (%d)",
                leftFrontDrive.getCurrentPosition(), leftRearDrive.getCurrentPosition(), rightFrontDrive.getCurrentPosition(), rightRearDrive.getCurrentPosition());
        telemetry.update();

    }

    @Override
    public void stop() {
    }

    public boolean launch(boolean shotRequested){
        switch (launchState) {
            case IDLE:
                if (shotRequested) {
                    launchState = LaunchState.PREPARE;
                    shotTimer.reset();
                }
                break;
            case PREPARE:
                flyWheelLeft.setVelocity(LAUNCHER_TARGET_VELOCITY);
                flyWheelRight.setVelocity(LAUNCHER_TARGET_VELOCITY);
                if (flyWheelRight.getVelocity() > LAUNCHER_MIN_VELOCITY && flyWheelLeft.getVelocity() > LAUNCHER_MIN_VELOCITY){
                    launchState = LaunchState.LAUNCH;
                    feeder.setPower(1);
                    feederTimer.reset();
                }
                break;
            case LAUNCH:
                if (feederTimer.seconds() > FEED_TIME) {
                    feeder.setPower(0);

                    if(shotTimer.seconds() > TIME_BETWEEN_SHOTS){
                        launchState = LaunchState.IDLE;
                        return true;
                    }
                }
        }
        return false;
    }

    /**
     * @param speed From 0-1
     * @param distance In specified unit
     * @param distanceUnit the unit of measurement for distance
     * @param holdSeconds the number of seconds to wait at position before returning true.
     * @return "true" if the motors are within tolerance of the target position for more than
     * holdSeconds. "false" otherwise.
     */
    public boolean drive(double speed, double distance, DistanceUnit distanceUnit, double holdSeconds) {
        final double TOLERANCE_MM = 10;
        /*
         * In this function we use a DistanceUnits. This is a class that the FTC SDK implements
         * which allows us to accept different input units depending on the user's preference.
         * To use these, put both a double and a DistanceUnit as parameters in a function and then
         * call distanceUnit.toMm(distance). This will return the number of mm that are equivalent
         * to whatever distance in the unit specified. We are working in mm for this, so that's the
         * unit we request from distanceUnit. But if we want to use inches in our function, we could
         * use distanceUnit.toInches() instead!
         */
        double targetPosition = (distanceUnit.toMm(distance) * TICKS_PER_MM);

        leftFrontDrive.setTargetPosition((int) targetPosition);
        rightFrontDrive.setTargetPosition((int) targetPosition);
        leftRearDrive.setTargetPosition((int) targetPosition);
        rightRearDrive.setTargetPosition((int) targetPosition);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFrontDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
        leftRearDrive.setPower(speed);
        rightRearDrive.setPower(speed);

        /*
         * Here we check if we are within tolerance of our target position or not. We calculate the
         * absolute error (distance from our setpoint regardless of if it is positive or negative)
         * and compare that to our tolerance. If we have not reached our target yet, then we reset
         * the driveTimer. Only after we reach the target can the timer count higher than our
         * holdSeconds variable.
         */
        if(Math.abs(targetPosition - leftFrontDrive.getCurrentPosition()) > (TOLERANCE_MM * TICKS_PER_MM)){
            driveTimer.reset();
        }

        return (driveTimer.seconds() > holdSeconds);
    }

    /**
     * @param speed From 0-1
     * @param angle the amount that the robot should rotate
     * @param angleUnit the unit that angle is in
     * @param holdSeconds the number of seconds to wait at position before returning true.
     * @return True if the motors are within tolerance of the target position for more than
     *         holdSeconds. False otherwise.
     */
    public boolean rotate(double speed, double angle, AngleUnit angleUnit, double holdSeconds){
        final double TOLERANCE_MM = 10;

        /*
         * Here we establish the number of mm that our drive wheels need to cover to create the
         * requested angle. We use radians here because it makes the math much easier.
         * Our robot will have rotated one radian when the wheels of the robot have driven
         * 1/2 of the track width of our robot in a circle. This is also the radius of the circle
         * that the robot tracks when it is rotating. So, to find the number of mm that our wheels
         * need to travel, we just need to multiply the requested angle in radians by the radius
         * of our turning circle.
         */
        double targetMm = angleUnit.toRadians(angle)*(TRACK_WIDTH_MM/2);

        /*
         * We need to set the left motor to the inverse of the target so that we rotate instead
         * of driving straight.
         */
        double leftTargetPosition = -(targetMm*TICKS_PER_MM);
        double rightTargetPosition = targetMm*TICKS_PER_MM;

        leftFrontDrive.setTargetPosition((int) leftTargetPosition);
        rightFrontDrive.setTargetPosition((int) rightTargetPosition);
        leftFrontDrive.setTargetPosition((int) leftTargetPosition);
        rightFrontDrive.setTargetPosition((int) rightTargetPosition);
/*
        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(speed);
        rightDrive.setPower(speed);

        if((Math.abs(leftTargetPosition - leftDrive.getCurrentPosition())) > (TOLERANCE_MM * TICKS_PER_MM)){
            driveTimer.reset();
        }
        //
*/
        return (driveTimer.seconds() > holdSeconds);

    }
    public void drive(double axial, double lateral, double yaw)
    {
        double max;
        double denominator = Math.max(Math.abs(axial)+Math.abs(lateral)+Math.abs(yaw),1);
        double leftFrontPower  = (axial + lateral + yaw)/denominator;
        double leftRearPower   = (axial - lateral + yaw)/denominator;
        double rightFrontPower = (axial - lateral - yaw)/denominator;
        double rightRearPower  = (axial + lateral - yaw)/denominator;

        // Apply the power to the motors
        leftFrontDrive.setPower(leftFrontPower);
        leftRearDrive.setPower(leftRearPower);
        rightFrontDrive.setPower(rightFrontPower);
        rightRearDrive.setPower(rightRearPower);

        // TODO: Add data to the telemetry for displaying the current motor powers
    }

//
}
