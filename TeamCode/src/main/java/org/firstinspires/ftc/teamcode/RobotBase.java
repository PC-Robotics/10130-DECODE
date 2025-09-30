package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

public class RobotBase extends DriveBase{
    protected DcMotor intake3 = null;
    protected DcMotor flyWheel = null;
    protected CRServo intake1_2 = null;
    public RobotBase(LinearOpMode opMode, boolean isFc)
    {
        super(opMode,isFc);
    }
    public void init()
    {
        intake3 = myOpMode.hardwareMap.get(DcMotor.class, "intake3");
        flyWheel = myOpMode.hardwareMap.get(DcMotor.class, "flyWheel");
        intake1_2 = myOpMode.hardwareMap.get(CRServo.class,"intake1_2");

        intake3.setDirection(DcMotor.Direction.REVERSE);
        flyWheel.setDirection(DcMotor.Direction.REVERSE);
        intake1_2.setDirection(CRServo.Direction.FORWARD);
    }

    public void power(double intakePower)
    {
        //double intake3Power = .7;
        //double flyWheelPower = .7;
        //double intake1_2Power = .7;
        intake1_2.setPower(intakePower);
    }

}
