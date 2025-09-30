package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Starting Teleop")
public class JoulesTeleop extends LinearOpMode {

    DriveBase robot;

    public JoulesTeleop()
    {
        robot = new DriveBase(this, true);
    }

    @Override
    public void runOpMode() throws InterruptedException {

        double drive;
        double turn;

        waitForStart();

        while(opModeIsActive())
        {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            robot.drive(y,x,rx);
        }
    }
}
