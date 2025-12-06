package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Starting Teleop")
public class JoulesTeleop extends LinearOpMode {

    RobotBase robot;

    public JoulesTeleop()
    {
        robot = new RobotBase(this, false);
    }

    @Override
    public void runOpMode() throws InterruptedException {

        double drive;
        double turn;

        //actually call the function
        robot.init();

        waitForStart();

        while(opModeIsActive())
        {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            robot.drive(y,x,rx);

            robot.launch(gamepad1.left_bumper);

            robot.runIntake(gamepad1.right_trigger);
            if(gamepad1.dpadDownWasPressed()){
                robot.decreaseVelocity();
            }
            if(gamepad1.dpadUpWasPressed()){
                robot.increaseVelocity();
            }
            telemetry.addData("Right Trigger:: ",gamepad1.right_trigger);

            telemetry.update();
        }
    }
}
