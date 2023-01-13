package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.classes.WebcamClass;
import org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

import java.util.List;
import java.util.Objects;

@Config
@Autonomous
public class LeftTurnAuto extends LinearOpMode
{
    String objDetect = "";
    public static double MOVE_FORWARD = 52;
    public static double TURN_ALIGN = 45;
    public static int HIGH_VALUE = 1900;
    public static int MIDDLE_VALUE = 1200;
    public static int LOW_VALUE = 1000;
    public static int CUP_VALUE = 350;
    public static double CYCLE_SPEED = 20;
    public static double FORWARD_ALIGN = 3;
    public DcMotor rightLift;
    public DcMotor leftLift;
    public Servo leftServo;
    //public Servo rightServo;
    @Override
    public void runOpMode() {
        rightLift = hardwareMap.get(DcMotor.class, "rightLift");
        rightLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftLift = hardwareMap.get(DcMotor.class, "leftLift");
        leftLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftServo = hardwareMap.get(Servo.class, "leftServo");
        //rightServo = hardwareMap.get(Servo.class, "rightServo");

        rightLift.setTargetPosition(0);
        leftLift.setTargetPosition(0);
        leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        WebcamClass camera = new WebcamClass(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        leftServo.setPosition(1);
        drive.setPoseEstimate(new Pose2d(0, 0, Math.toRadians(90)));

        TrajectorySequence startMove = drive.trajectorySequenceBuilder(new Pose2d(0, 0, Math.toRadians(90)))
                .forward(MOVE_FORWARD, SampleMecanumDrive.getVelocityConstraint(CYCLE_SPEED+10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addTemporalMarker(0, () -> {
                    // Turn on motor
                    encoderArm(3, 0.7);
                })
                .addTemporalMarker(2.5, () -> {
                    // Turn on motor
                    encoderArm(0, 0);
                })
                .turn(Math.toRadians(TURN_ALIGN), 1.5, Math.toRadians(184.02607784577722))
                .forward(0.01)
                .build();
        TrajectorySequence backLower = drive.trajectorySequenceBuilder(startMove.end())
                .waitSeconds(2)
                .lineToLinearHeading(new Pose2d(0, 50, Math.toRadians(0)), SampleMecanumDrive.getVelocityConstraint(CYCLE_SPEED, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))

                .addTemporalMarker(0, () -> {
                    // Turn on motor
                    leftServo.setPosition(-1);
                })
                .addTemporalMarker(1.0, () -> {
                    // Turn on motor
                    encoderArm(4, 0.5);
                })
                .addTemporalMarker(2.5, () -> {
                    // Turn on motor
                    encoderArm(0, 0);
                })
                .forward(19, SampleMecanumDrive.getVelocityConstraint(CYCLE_SPEED, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        TrajectorySequence back1 = drive.trajectorySequenceBuilder(backLower.end())
                .addTemporalMarker(0, () -> {
                    leftServo.setPosition(1);
                })
                .waitSeconds(1)
                .addTemporalMarker(0.5, () -> {
                    encoderArm(3, 0.8);
                })
                .addTemporalMarker(2.5, () -> {
                    // Turn off motor
                    encoderArm(0, 0);
                })
                .forward(-25, SampleMecanumDrive.getVelocityConstraint(CYCLE_SPEED, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .lineToLinearHeading(new Pose2d(0,MOVE_FORWARD, Math.toRadians(0)), SampleMecanumDrive.getVelocityConstraint(CYCLE_SPEED, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .turn(Math.toRadians(135), 1.5, Math.toRadians(184.02607784577722))
                .forward(-1)
                .build();

        TrajectorySequence parkMiddle = drive.trajectorySequenceBuilder(new Pose2d(0, 0, Math.toRadians(90)))
                .addTemporalMarker(0.0, () -> {
                    // Turn on motor
                    encoderArm(0, 0.7);
                })
                .addTemporalMarker(4.5, () -> {
                    // Turn on motor
                    encoderArm(0, 0);
                })
                .waitSeconds(1)
                .turn(Math.toRadians(-130), 1.5, Math.toRadians(184.02607784577722))
                .lineToConstantHeading(new Vector2d(0, 26), SampleMecanumDrive.getVelocityConstraint(10, 10, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(1))
                .build();
        TrajectorySequence parkLeft = drive.trajectorySequenceBuilder(parkMiddle.end())
                .lineToLinearHeading(new Pose2d(24.5, 26, Math.toRadians(90)), SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        TrajectorySequence parkRight = drive.trajectorySequenceBuilder(parkMiddle.end())
                .lineToLinearHeading(new Pose2d(-24.5, 26, Math.toRadians(90)), SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        while (!opModeIsActive() && camera.getTfod() != null) {
            List<Recognition> updatedRecognitions = camera.getTfod().getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Objects Detected", updatedRecognitions.size());
                for (Recognition recognition : updatedRecognitions) {
                    if (Objects.equals(recognition.getLabel(), "cargill1"))
                        objDetect = "cargill1";
                    else if (Objects.equals(recognition.getLabel(), "number2"))
                        objDetect = "number2";
                    else
                        objDetect = "eagle3";
                    telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
                }
                telemetry.update();
            }
        }

        waitForStart();
        while (opModeIsActive()) {
            drive.followTrajectorySequence(startMove);
            drive.followTrajectorySequence(backLower);
            drive.followTrajectorySequence(back1);
            leftServo.setPosition(-1);
            if(objDetect.equals("cargill1"))
            {
                drive.followTrajectorySequence(parkMiddle);
                drive.followTrajectorySequence(parkRight);
                leftServo.setPosition(-1);
            }else if(objDetect.equals("eagle3"))
            {
                drive.followTrajectorySequence(parkMiddle);
                drive.followTrajectorySequence(parkLeft);
                leftServo.setPosition(-1);
            }
            else
            {
                drive.followTrajectorySequence(parkMiddle);
                leftServo.setPosition(-1);
            }
            leftServo.setPosition(-1);

            telemetry.addData("Current Pose", startMove.end());
            telemetry.addData("obj detected", objDetect);
            telemetry.update();

            break;
        }

    }
    private void encoderArm(int level, double power){
        if(power != 0.0) {
            if (level == 0) {
                leftLift.setTargetPosition(0);
                rightLift.setTargetPosition(0);
                rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            } else if (level == 1) {
                rightLift.setTargetPosition(LOW_VALUE);
                leftLift.setTargetPosition(LOW_VALUE);
                rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            } else if (level == 2) {
                rightLift.setTargetPosition(MIDDLE_VALUE);
                leftLift.setTargetPosition(MIDDLE_VALUE);
                rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            } else if (level == 3) {
                rightLift.setTargetPosition(HIGH_VALUE);
                leftLift.setTargetPosition(HIGH_VALUE);
                rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            } else {
                rightLift.setTargetPosition(350);
                leftLift.setTargetPosition(350);
                rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }

        }
        rightLift.setPower(power);
        leftLift.setPower(power);

    }
}