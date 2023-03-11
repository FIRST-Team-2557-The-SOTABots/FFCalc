// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveSpeedMotor extends SubsystemBase implements Characterizable {
  private final WPI_TalonFX[] mMotors;

  /** Creates a new SwerveSpeedMotor. */
  public SwerveSpeedMotor(WPI_TalonFX[] motors) {
    mMotors = motors;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void setVoltage(double voltage) {
    for (int i = 0; i < mMotors.length; i++) {
      mMotors[i].setVoltage(voltage);
    }    
  }

  @Override
  public double getVelocity() {
    return mMotors[0].getSelectedSensorVelocity();
  }

  @Override
  public double getAcceleration() {
    return 0;
  }
}
