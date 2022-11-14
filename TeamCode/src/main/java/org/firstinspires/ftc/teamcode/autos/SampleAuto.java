package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.classes.WebcamClass;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

import java.util.List;
import java.util.Objects;

@Autonomous
public class SampleAuto extends LinearOpMode
{
    String objDetect = "";
    @Override
    public void runOpMode()
    {
        WebcamClass camera = new WebcamClass(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(new Pose2d(0, 0, Math.toRadians(90)));

        Trajectory startMove = drive.trajectoryBuilder(new Pose2d(0,0, Math.toRadians(90)))
                .splineToConstantHeading(new Vector2d(0, 30), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(-25, 30, Math.toRadians(104)), Math.toRadians(180))
                .lineToLinearHeading(new Pose2d(10, -55, Math.toRadians(104)))
                .build();

        while(!opModeIsActive() && camera.getTfod() != null)
        {
            List<Recognition> updatedRecognitions = camera.getTfod().getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Objects Detected", updatedRecognitions.size());
                for (Recognition recognition : updatedRecognitions) {
                    if(Objects.equals(recognition.getLabel(), "cargill1"))
                        objDetect = "cargill1";
                    else if(Objects.equals(recognition.getLabel(), "number2"))
                        objDetect = "number2";
                    else
                        objDetect = "eagle3";
                    telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100 );
                }
                telemetry.update();
            }
        }

        waitForStart();
        while(opModeIsActive())
        {
            drive.followTrajectory(startMove);
        }
    }
}
