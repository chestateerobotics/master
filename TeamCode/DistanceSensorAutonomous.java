package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/*
Stuff to test before running on the actual robot:
    1: Color Sensor Values for the white line
    2: Distance sensor values for each of the squares
    3: Change all movement to slower
    (I recomend 0.25 for regular and 0.05 for testing for white line)
    4:
*/


@TeleOp(name = "Distance Sensor Autonomous")
public class DistanceSensorAutonomous extends LinearOpMode{
    private MecanumDrive mecanumDrive = new MecanumDrive();
    // Define harware
    private DcMotor arm;
    private Servo handServo;
    private DistanceSensor frontDistance, rightDistance;
    private ColorSensor colorSensor;

    // Game variables
    private String alliance = "Red";
    private int target;
    private int targetXMin, targetXMax, targetYMin, targetYMax;

    public void runOpMode(){
        Initiate();
        telemetry.addData("Press Start When Ready", "");
        telemetry.update();

        //target = 3;
        target = (int) Math.ceil(Math.random() * 3);
        telemetry.addData("target", target);
        telemetry.update();

        if (target == 1) {
            targetXMin = 0;
            targetYMax = 0;
            targetXMax = 15;
            targetYMax = 15;
        } else if (target == 2) {
            targetXMin = 60;
            targetYMin = 60;
            targetXMax = 75;
            targetYMax = 75;
        } else {
            targetXMin = 0;
            targetYMin = 135;
            targetXMax = 15;
            targetYMax = 150;
        }

        //mecanumDrive.setEndPowerBehaviorBrake();
        waitForStart();
        while(opModeIsActive()){
                //Sets Robot on the line after it looks at the rings
                while(rightDistance.getDistance(DistanceUnit.CM)  <= 95.5){
                    driveLeft(0.4);
                }
                while(rightDistance.getDistance(DistanceUnit.CM) >= 95.5){
                    driveRight(0.2);
                }

                //Robot moves to Area and posts "Dropped the thing"
                if (target == 3) {
                    while (colorSensor.blue() < 200 &&
                            colorSensor.green() < 200 &&
                            colorSensor.red() < 200){
                        driveForward(0.3);
                    }
                }
                else{
                    while (frontDistance.getDistance(DistanceUnit.CM) > targetYMax) {
                        driveForward(1);
                    }
                    while (frontDistance.getDistance(DistanceUnit.CM) < targetYMin) {
                        driveBackwards(1);
                    }
                }
                while (rightDistance.getDistance(DistanceUnit.CM) > targetXMax) {
                    driveRight(1);
                }
                while (rightDistance.getDistance(DistanceUnit.CM) < targetXMin) {
                    driveLeft(1);
                }
                fullStop();
                telemetry.addData("Dropped the thing", "");
                telemetry.update();
                sleep(200);

                if (target == 3){
                    break;
                }

                //Arm Moves
                armSetOut(0.5);
                //Position 0 is Retract
                handServo.setPosition(0);
                sleep(100);
                driveLeft(1);
                sleep(100);
                returnArm();

                //Find actual color sensor values
                while (colorSensor.blue() < 300 &&
                        colorSensor.green() < 300 &&
                        colorSensor.red() < 300) {
                    driveBackwards(.1);
                }

                fullStop();
                telemetry.addData("finished", "");
                telemetry.update();
                break;
            }
        }

    private void Initiate(){
        mecanumDrive.init(hardwareMap);

        arm = hardwareMap.get(DcMotor.class, "arm_motor");
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        handServo = hardwareMap.servo.get("hand_servo");

        frontDistance = hardwareMap.get(DistanceSensor.class, "front_distance");
        rightDistance = hardwareMap.get(DistanceSensor.class, "right_distance");;

        colorSensor = hardwareMap.get(ColorSensor.class, "color_sensor");
    }

    private void driveForward(double speed) {
        telemetry.addData("driving forward", "");
        telemetry.update();
        mecanumDrive.driveMecanum(speed,0,0);
    }

    private void driveBackwards(double speed) {
        telemetry.addData("driving backwards", "");
        telemetry.update();
        mecanumDrive.driveMecanum(-speed,0,0);
    }

    private void driveLeft(double speed) {
        mecanumDrive.driveMecanum(0, -speed, 0);
    }

    private void driveRight(double speed) {
        mecanumDrive.driveMecanum(0, speed, 0);
    }

    private void rotateClockwise(double speed) {
        mecanumDrive.driveMecanum(0, 0, speed);
    }

    private void rotateCounterClockwise(double speed) {
        mecanumDrive.driveMecanum(0, 0, -speed);
    }

    private void fullStop() {
        mecanumDrive.driveMecanum(0,0,0);
    }

    private void armSetOut(double power){
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setTargetPosition(-260);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //Power = 0.2 without WobbleGoal
        arm.setPower(power);
        while (arm.isBusy()){
            telemetry.addData("Encoder Arm", arm.getCurrentPosition());
            telemetry.addData("Arm Power", arm.getPower());
            telemetry.update();
        }
        arm.setPower(0);

        //Possibly comment out the up motion since it doesn't matter for autonomous much
        sleep(50);
        arm.setPower(-0.15);
        while (arm.isBusy()){
            telemetry.addData("Encoder Arm", arm.getCurrentPosition());
            telemetry.addData("Arm Power", arm.getPower());
            telemetry.update();
        }
        arm.setPower(0);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void returnArm(){
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setTargetPosition(-50);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(-0.2);
        while (arm.isBusy()){
            telemetry.addData("Encoder Arm", arm.getCurrentPosition());
            telemetry.addData("Arm Power", arm.getPower());
            telemetry.update();
        }
        arm.setPower(0);
    }
}
