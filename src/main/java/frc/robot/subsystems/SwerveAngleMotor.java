// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveAngleMotor extends SubsystemBase implements Characterizable {

  private static final double countsPerRevolution = 5.0; // In absolute encoder ticks

  private final CANSparkMax mMotor;
  private final AnalogInput mEncoder;

  private double prevPosition;
  private double prevVelocity;
  private double encoderVelocity;

  /** Creates a new AngleMotor. */
  public SwerveAngleMotor(CANSparkMax motor, AnalogInput encoder) {
    mMotor = motor;
    mEncoder = encoder;
    prevPosition = getEncoderPosition();
    prevVelocity = 0.0;
  }

  @Override
  public void setVoltage(double voltage) {
    SmartDashboard.putNumber("set voltage", voltage);
    mMotor.setVoltage(voltage);
  }

  @Override
  public double getVelocity() {
    return encoderVelocity;
  }

  @Override
  public double getAcceleration() {
    return (encoderVelocity - prevVelocity) / 0.02;
  }


  private void updateEncoderVelocity() {
    double shiftedPrevious = prevPosition - getEncoderPosition();
    double positionChange;

    if (shiftedPrevious < 0)
      shiftedPrevious += countsPerRevolution;
    
    if (shiftedPrevious < countsPerRevolution / 2.0)
      positionChange = -1 * shiftedPrevious;
    else
      positionChange = countsPerRevolution - shiftedPrevious;

    encoderVelocity = positionChange / 0.02;
    prevPosition = getEncoderPosition();
  }

  public double getEncoderPosition() {
    return countsPerRevolution - mEncoder.getAverageVoltage();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateEncoderVelocity();
    double encoderVelocity = getVelocity();

    prevVelocity = encoderVelocity;
  }

}
