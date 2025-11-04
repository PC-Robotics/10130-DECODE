package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


public class AutoBase extends LinearOpMode {

    RobotBase robot;

    public AutoBase()
    {
        robot = new RobotBase(this, false);
    }
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        waitForStart();

        robot.drive(0, .5, 0);

        sleep(1500);

        robot.drive(0, 0, 0);

        //TODO: type can be changed depending on needs
        robot.launch(true, 1);

        void exitLaunchZone(int direction)
        {

            robot.drive(0, 0, direction*.5);

            sleep(1500);

            robot.drive(0,0,0);

            robot.drive(0, -.5, 0);

            sleep(1500);

            robot.drive(0, 0, 0);
        }

    }
}
