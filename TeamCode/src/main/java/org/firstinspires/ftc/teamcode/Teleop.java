package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import java.lang.Exception.*;

@TeleOp(name="Teleop", group="Test")
public class Teleop extends LinearOpMode {

    RobotBase robot = new RobotBase(this,false);

    @Override
    public void runOpMode(){ throws InterruptedException
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    public void init()
    {
        double BRAKE = 0;
        flyWheel.setPower(BRAKE);
        intake3.setPower(BRAKE);
        intake1_2.setPower(BRAKE);
    }
}
