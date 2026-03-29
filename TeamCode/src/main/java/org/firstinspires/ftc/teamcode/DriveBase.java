package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Simplistic drive base capable of using mecanum wheels and driving around
 */
public class DriveBase
{
    protected LinearOpMode myOpMode = null;

    protected boolean fieldCentric = false;

    protected DcMotor leftFrontDrive = null;
    protected DcMotor leftRearDrive = null;
    protected DcMotor rightFrontDrive = null;
    protected DcMotor rightRearDrive = null;

    protected IMU imu = null;

    public DriveBase (LinearOpMode opMode, boolean isFC)
    {
        myOpMode = opMode;
        fieldCentric = isFC;
    }

    /**
     * Initializes the motors and sets their direction. Can also add other default settings.
     *  - Set Mode: run with/without encoders
     *  - Zero power behavior
     */
    public void init()
    {//front is intake lol
        leftFrontDrive = myOpMode.hardwareMap.get(DcMotor.class, "leftFront");
        leftRearDrive = myOpMode.hardwareMap.get(DcMotor.class, "leftRear");
        rightFrontDrive = myOpMode.hardwareMap.get(DcMotor.class,"rightFront");
        rightRearDrive = myOpMode.hardwareMap.get(DcMotor.class,"rightRear");
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftRearDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightRearDrive.setDirection(DcMotor.Direction.FORWARD);


        // TODO: Update this based on how the hub is mounted on the robot
        imu = myOpMode.hardwareMap.get(IMU.class,"imu"); //check control hub
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));

        imu.initialize(parameters);

        myOpMode.telemetry.addData("Status","Initialized");
        myOpMode.telemetry.update();

    }
//
    /**
     * Standard POV Mecanum drive code
     * @param axial left joystick y value
     * @param lateral left joystick x value
     * @param yaw right joystick x value
     */
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
        myOpMode.telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
        myOpMode.telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftRearPower, rightRearPower);
        myOpMode.telemetry.addData("Heading",imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
    }
}
