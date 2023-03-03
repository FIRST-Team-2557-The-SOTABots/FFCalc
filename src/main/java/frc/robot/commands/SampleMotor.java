// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Characterizable;

public class SampleMotor extends WaitCommand {

  private final Characterizable mMotor;
  private final double mVoltage;

  private ArrayList<Double> speeds = new ArrayList<>();  
  /** Creates a new SampleMotor. */
  public SampleMotor(Characterizable motor, double voltage, double seconds) {
    // Use addRequirements() here to declare subsystem dependencies.
    super(seconds);
    mMotor = motor;
    mVoltage = voltage;
    SmartDashboard.putNumber("Sample Voltage", mVoltage);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    SmartDashboard.putNumber("Sample Voltage Execute", mVoltage);
    mMotor.setVoltage(mVoltage);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);    
    mMotor.setVoltage(0.0);
    int size = speeds.size();
    double avg = 0.0;
    while (!speeds.isEmpty()) {
      avg += speeds.remove(0);
    }
    avg = avg / size;
    SmartDashboard.putNumber("" + mVoltage, avg);
  }

}
