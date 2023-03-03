// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Characterizable;

public class RampMotor extends WaitCommand {

  private final Characterizable mMotor;
  private double mVoltage;

  /** Creates a new RampMotor. */
  public RampMotor(Characterizable motor, double voltage, double seconds) {
    super(seconds);
    mMotor = motor;
    mVoltage = voltage;
    SmartDashboard.putNumber("Ramp Voltage", voltage);
  }

  public static double getInstanceVoltage(RampMotor rm) {
    return rm.getVoltage();
  }

  public double getVoltage() {
    return this.mVoltage;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    SmartDashboard.putNumber("Ramp Voltage Execute", getVoltage());
    mMotor.setVoltage(mVoltage);
  }
}
