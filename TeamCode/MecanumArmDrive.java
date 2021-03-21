package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name = "Main Drive")
public class MecanumArmDrive extends OpMode {
    private MecanumDrive mecanumDrive = new MecanumDrive();
    private double[] distances;

    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        mecanumDrive.init(hardwareMap);
        mecanumDrive.setMaxSpeed(0.4);
    }

    // Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop() {
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
        if (gamepad1.a){
            mecanumDrive.moveServo(1);
        }
        else if (gamepad1.b){
            mecanumDrive.moveServo(0);
        }
        telemetry.addData("Gamepad Left Stick", gamepad1.left_stick_x);
        telemetry.addData("Max Speed", mecanumDrive.getMaxSpeed());
        telemetry.update();
    }
}
