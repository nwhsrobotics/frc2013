/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.northwestrobotics.frc2013;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.MotorSafetyHelper;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

/**
 * This class manages the shooting system.
 *
 * @author soggy.potato
 * @author SilverX
 * @author AgentOrange
 */
public class Shooter {

    private Joystick aimingStick;
    // Joystick buttons
    /**
     * The state of the button, which is pressed to active launching.
     */
    private boolean shootButton = false;//creates and indicator for the pressing of button
    // Motors
    /**
     * The motor used to adjust the shooter's vertical aiming.
     *
     * @author soggy.potato
     * @author SilverX
     */
    private Talon pitchMotor;
    /**
     * The motor used to launch a frisbee at targets
     *
     * @author soggy.potato
     * @author SilverX
     * @author AgentOrange
     */
    private Talon shootMotor;
    /**
     * Used to adjust pitchMotor (vertical aiming)
     * @author AgentOrange
     */
    private Encoder pitchChanger = null; // TODO: yet to initialize

    /**
     * Pneumatic arm to push frisbees into shooter
     * @author AgentOrange
     */
    private Solenoid feeder;
    
    public Shooter(Joystick aimingStick) {
        this.aimingStick = aimingStick;
        initializeMotors();
        feeder = new Solenoid(RobotConstants.Shooting.FEEDER_CHANNEL);
    }

    /**
     * @author AgentOrange
     * @author soggy.potato
     * Adjusts vertical aiming in accordance to user input.
     */
    public void respondToUserInput() {
        // User uses controller to aim. Read in this user input.
        // If the y is zero pitch arm stops moving
        /*
         * TODO: Find out the sign for the Y value, and adjust the pitch factor
         * from testing. Perform a range check before calling the set method.
         * May have to change sign.
         */
        double pitchAdjustment = readUserInput() * RobotConstants.Shooting.PITCH_FACTOR;
        pitchMotor.set(pitchAdjustment);
        
    }

    /**
     * Determines whether the driver has commanded the robot to shoot a
     * frisbee.
     *
     * @author soggy.potato
     */
    public boolean shootButtonPressed() {
        return shootButton;
    }

    /**
     * Fires a frisbee.
     */
    public void shoot() {
        // 1. Start shooter motor
        // 2. Trigger the pneumatic arm to move frisbee into shooter
        feeder.set(true);
        
        Timer.delay(RobotConstants.Shooting.FEEDER_WAIT_TIME);
        
        
        // 3. Retract arm (maybe automatic)
        feeder.set(false);
        
        // 4. Turn off shooting motor.

    }

    /**
     * Reads in the user's commands for the robot.
     *
     * @return The pitch adjustment from the aiming joystick.
     */
    private double readUserInput() {
        shootButton = aimingStick.getRawButton(RobotConstants.Shooting.SHOOT_BUTTON); // Determine whether the shoot button is pressed or not
        return aimingStick.getY(); // Get the input for adjustment to the pitch, which is the only thing we can control.
    }

    /**
     * Sets up the motors for use.
     */
    private void initializeMotors() {
        pitchMotor = new Talon(RobotConstants.Shooting.PITCH_MOTOR);
        shootMotor = new Talon(RobotConstants.Shooting.SHOOT_MOTOR);
    }
}
