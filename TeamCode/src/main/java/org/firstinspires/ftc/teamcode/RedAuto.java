package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Blue Auto Test")
public class RedAuto extends LinearOpMode {

    RobotBase robot;

    public RedAuto()
    {
        robot = new RobotBase(this, false);
    }
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        waitForStart();

        robot.drive(-.5, 0, 0);

        sleep(60);

        robot.drive(0, 0, 0);

        //shoot();

        robot.drive(0, -.5, 0);

        sleep(60);

        robot.drive(0,0,0);

    }
}