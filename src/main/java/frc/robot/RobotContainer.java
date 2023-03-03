// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.Characterizable;
import frc.robot.subsystems.SwerveAngleMotor;

import java.util.ArrayList;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  private static final double rampDuration = 3; // In seconds
  private static final double sampleDuration = 5;

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  private final SwerveAngleMotor swerveAngleMotor;
  private final Characterizable testSystem;
  // private SequentialCommandGroup sampleCommand;

  private double mVoltage = 0.0;
  private ArrayList<Double> speeds = new ArrayList<>();  

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    CANSparkMax angleMotor = new CANSparkMax(3, MotorType.kBrushless);
    AnalogInput angleEncoder = new AnalogInput(2);
    swerveAngleMotor = new SwerveAngleMotor(angleMotor, angleEncoder);
    testSystem = swerveAngleMotor;

    // updateSampleCommand();

    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    m_driverController.a().onTrue(
      new SequentialCommandGroup(
        new RunCommand(
          () -> testSystem.setVoltage(mVoltage)
        ).withTimeout(rampDuration),
        new RunCommand(
          () -> {
            testSystem.setVoltage(mVoltage);
            speeds.add(testSystem.getVelocity());
          }
        ).withTimeout(sampleDuration),
        new InstantCommand(
          () -> {
            testSystem.setVoltage(0.0);
            int size = speeds.size();
            double avg = 0.0;
            while (!speeds.isEmpty()) {
              avg += speeds.remove(0);
            }
            avg = avg / size;
            SmartDashboard.putNumber("" + mVoltage, avg);
          }
        )
      )
    );

    m_driverController.b().onTrue(
      new InstantCommand(
        () -> {
          mVoltage += 0.5;
          SmartDashboard.putNumber("selected voltage", mVoltage);
        }
      )
    );
    m_driverController.x().onTrue(
      new InstantCommand(
        () -> {
          mVoltage -= 0.5;
          SmartDashboard.putNumber("selected voltage", mVoltage);
        }
      )
    );
    m_driverController.y().onTrue(
      new InstantCommand(
        () -> {
          mVoltage *= -1.0;
          SmartDashboard.putNumber("selected voltage", mVoltage);
        }
      )
    );

  }

  // private void updateSampleCommand() {
  //   SmartDashboard.putNumber("generate voltage", mVoltage);
  //   sampleCommand = new SequentialCommandGroup(

  //   );
  // }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }
}
