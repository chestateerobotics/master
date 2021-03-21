package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Intake Drive")

public class IntakeDrive extends LinearOpMode{
    private Servo arm_servo;
    private CRServo intake_servo;
    private DcMotor leftLauncher, rightLauncher;
    private DcMotor arm, intake;
    private Servo handServo;
    private MecanumDrive mecanumDrive = new MecanumDrive();

    public void runOpMode() {
        mecanumDrive.init(hardwareMap);
        mecanumDrive.setMaxSpeed(0.4);
        arm = hardwareMap.get(DcMotor.class, "arm_motor");
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        handServo = hardwareMap.get(Servo.class, "arm_servo");

        leftLauncher = hardwareMap.get(DcMotor.class, "leftLauncher");
        rightLauncher = hardwareMap.get(DcMotor.class, "rightLauncher");
        leftLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake_servo = hardwareMap.get(Servo.class, "intake_servo";
        
        waitForStart();
        while (opModeIsActive()){
            double forward = gamepad1.left_stick_y * -1; //The y direction on the gamepad is reversed idk why
            double strafe = gamepad1.left_stick_x;
            double rotate = gamepad1.right_stick_x;
            
            mecanumDrive.driveMecanum(forward, strafe, rotate);
            
            if (gamepad1.x){
                mecanumDrive.setMaxSpeed(0.4);
            }
            else if (gamepad1.y){
                mecanumDrive.setMaxSpeed(0.1);
            }
            if (gamepad1.left_bumper){
                mecanumDrive.moveArm(-1);
            }
            else if (gamepad1.right_bumper){
                mecanumDrive.moveArm(1);
            }
            else{
                mecanumDrive.moveArm(0);
            }
            
            if (gamepad1.dpad_right){
                moveArm();
            }
            
            if (gamepad1.a){
                mecanumDrive.moveServo(1);
            }
            else if (gamepad1.b){
                mecanumDrive.moveServo(0);
            }

            if (gamepad1.dpad_up){
                intake.setPower(-0.5);
            }
            else if(gamepad1.dpad_down){
                intake.setPower(0.5);
            }
            else if(gamepad1.dpad_left){
                intake_servo.setPower(1);
            }
            else{
                intake.setPower(0);
                intake_servo.setPower(0);
            }

            if (gamepad1.left_trigger > 0){
                leftLauncher.setPower(-1);
                rightLauncher.setPower(1);
            }
            else{
                leftLauncher.setPower(0);
                rightLauncher.setPower(0);
            }



    
            telemetry.addData("Gamepad Left Stick", gamepad1.left_stick_x);
            telemetry.addData("Max Speed", mecanumDrive.getMaxSpeed());
            telemetry.addData("Encoder Arm", arm.getCurrentPosition());
            telemetry.addData("Arm Power", arm.getPower());
            telemetry.update();
        }
    }

    private void moveArm(){
        arm.setTargetPosition(-260);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.2);
        while (arm.isBusy()){
            telemetry.addData("Encoder Arm", arm.getCurrentPosition());
            telemetry.addData("Arm Power", arm.getPower());
            telemetry.update();
        }
        arm.setPower(0);
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
}