package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ColorSensor;

@Autonomous

public class MoveForward extends LinearOpMode{
    private Blinker expansion_Hub_1;
    private Blinker expansion_Hub_2;
    private DcMotor backLeft;
    private DcMotor backRight;
    private ColorSensor colorSensor;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private Gyroscope imu;
    private MecanumDrive mecanumDrive = new MecanumDrive();
    // todo: write your code here
    
    public void runOpMode() {
        waitForStart();
        mecanumDrive.init(hardwareMap);
        mecanumDrive.setMaxSpeed(0.4);
        
        while (opModeIsActive()) {
            mecanumDrive.driveMecanum(1, 0, 0);
            sleep(1000);
            mecanumDrive.driveMecanum(0,0,0);
            break;
        }  
    }
}