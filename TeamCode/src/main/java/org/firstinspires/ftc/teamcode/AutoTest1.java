package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robots.DriveBasePID;

@Autonomous(name = "AutoTest1")
public class AutoTest1 extends LinearOpMode {

    protected DcMotor leftFrontDrive = null;
    protected DcMotor leftRearDrive = null;
    protected DcMotor rightFrontDrive = null;
    protected DcMotor rightRearDrive = null;
    public void runOpMode() throws InterruptedException
    {
        leftFrontDrive = hardwareMap.get(DcMotor.class, "leftFront");
        leftRearDrive = hardwareMap.get(DcMotor.class, "leftRear");
        rightFrontDrive = hardwareMap.get(DcMotor.class,"rightFront");
        rightRearDrive = hardwareMap.get(DcMotor.class,"rightRear");
        waitForStart();

        while(opModeIsActive())
        {
            ///////
        }
    }
}

}
