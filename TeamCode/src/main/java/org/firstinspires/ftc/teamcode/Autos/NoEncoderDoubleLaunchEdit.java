package org.firstinspires.ftc.teamcode.Autos;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="Final Auto ???????????????????")
//@Disabled
public class NoEncoderDoubleLaunchEdit extends OpMode
{

    final double FEED_TIME = 1.20; //The feeder servos run this long when a shot is requested.
    final double LAUNCHER_TARGET_VELOCITY = 1200;
    final double LAUNCHER_MIN_VELOCITY = 900;
    final double TIME_BETWEEN_SHOTS = 2;
    final double DRIVE_SPEED = 0.5;
    final double ROTATE_SPEED = 0.2;
    final double WHEEL_DIAMETER_MM = 96;
    final double ENCODER_TICKS_PER_REV = 537.7;
    final double TICKS_PER_MM = (ENCODER_TICKS_PER_REV / (WHEEL_DIAMETER_MM * Math.PI));
    final double TRACK_WIDTH_MM = 404;
    protected DcMotor intake1_2 = null;

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
        SECOND_LAUNCH,
        LETS_WAIT_AGAIN,
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
        //hardware map
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "leftFront");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFront");
        leftRearDrive  = hardwareMap.get(DcMotor.class, "leftRear");
        rightRearDrive = hardwareMap.get(DcMotor.class, "rightRear");
        flyWheelLeft = hardwareMap.get(DcMotorEx.class,"flyWheelLeft");
        flyWheelRight = hardwareMap.get(DcMotorEx.class,"flyWheelRight");
        intake1_2 = hardwareMap.get(DcMotor.class,"intake1_2");
        feeder = hardwareMap.get(CRServo.class, "feeder");
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftRearDrive.setDirection(DcMotor.Direction.REVERSE);
        rightRearDrive.setDirection(DcMotor.Direction.FORWARD);
        flyWheelRight.setDirection(DcMotor.Direction.FORWARD);
        flyWheelLeft.setDirection(DcMotor.Direction.REVERSE);
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
                if (driveTimer.milliseconds() > 1500){
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
                if (launch(true)) {
                    shotsToFire -= 1;
                    if (shotsToFire > 0) {
                        autonomousState = AutonomousState.LAUNCH;
                    } else {
                        //TODO: delte suspicious suggestion ?????????
                        //flyWheelLeft.setVelocity(0);
                        //flyWheelRight.setVelocity(0);

                        // reset timer before strafing
                        driveTimer.reset();
                        //go to strafe
                        autonomousState = AutonomousState.SECOND_LAUNCH;
                    }
                }
                break;
                //TODO: make sure ts actually freaking functions *shrug*
            case SECOND_LAUNCH:
                driveTimer.reset();
                launch(true);
                runIntake(.5);
                if(driveTimer.milliseconds() > 500){
                    runIntake(0);
                }
                autonomousState = AutonomousState.LETS_WAIT_AGAIN;
                break;

            case LETS_WAIT_AGAIN:
                if (launch(true)) {
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

            case STRAFE_RIGHT:
                //maybe mess with ts
                if(alliance == Alliance.RED){
                    drive(0, .5, 0);
                }else if(alliance == Alliance.BLUE){
                    drive(0, -.5, 0);
                }

                if (driveTimer.milliseconds() > 1000) {
                    drive(0, 0 , 0);
                    autonomousState = AutonomousState.COMPLETE;
                }
                break;
        }

        telemetry.addData("AutoState", autonomousState);
        telemetry.addData("LauncherState", launchState);
        telemetry.addData("Shots to fire", shotsToFire);
        telemetry.addData(
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
        double targetMm = angleUnit.toRadians(angle)*(TRACK_WIDTH_MM/2);
        double leftTargetPosition = -(targetMm*TICKS_PER_MM);
        double rightTargetPosition = targetMm*TICKS_PER_MM;

        leftFrontDrive.setTargetPosition((int) leftTargetPosition);
        rightFrontDrive.setTargetPosition((int) rightTargetPosition);
        leftFrontDrive.setTargetPosition((int) leftTargetPosition);
        rightFrontDrive.setTargetPosition((int) rightTargetPosition);
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
        telemetry.addData("Right Rear Motor Power", rightRearDrive.getPower());
        telemetry.addData("Left Rear Motor Power", leftRearDrive.getPower());
        telemetry.addData("Right Front Motor Power", rightFrontDrive.getPower());
        telemetry.addData("Left Front Motor Power", leftFrontDrive.getPower());

    }

    void runIntake(double power)
    {
        intake1_2.setPower(power);

    }

//
}
