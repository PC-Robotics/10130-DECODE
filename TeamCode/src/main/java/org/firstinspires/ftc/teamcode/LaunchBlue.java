package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Blue Auto Launch")

public class LaunchBlue extends LinearOpMode {

    RobotBase robot;

    public LaunchBlue()
    {
        robot = new RobotBase(this, false);
    }
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        waitForStart();

        robot.drive(.5, 0, 0);

        sleep(1500);

        robot.drive(0, 0, 0);

        //TODO: type can be changed depending on needs
        robot.launch(true, 1);

    }
    void exitLaunchZone ()
    {

        robot.drive(0, 0, .5);

        sleep(1500);

        robot.drive(0,0,0);

        robot.drive(0, -.5, 0);

        sleep(1500);

        robot.drive(0, 0, 0);
    }
}

