/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.northwestrobotics.frc2013;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
/*
 * Jagbot
 * ======
 */

import org.northwestrobotics.frc2013.shooter.Shooter;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Jagbot extends IterativeRobot {
    /*
     * Components
     * ----------
     */

    private Joystick aimingController;
    private Joystick moveController;
    private Driver driver;
    private Shooter shooter;

    private int magazine = 4;
    private boolean isStart = true;

    
    /**
     * Gives access to the lifting subsystem.
     * @author soggy.potato
     */
    private Lifter lifter;


    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        moveController = new Joystick(RobotConstants.Drive.MOVE_CONTROLLER);
        aimingController = new Joystick(RobotConstants.Shooting.AIMING_CONTROLLER);
        
        driver = new Driver(moveController);
        shooter = new Shooter(aimingController);
        lifter = new Lifter(moveController);
    }

    public void autonomousInit() {
        magazine = 4;
        isStart = true;
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        if (magazine > 0) {
            if (isStart) {
                shooter.activateShootMotorForAutonomous();

                // Waiting for two seconds to allow the shoot motor to speed
                // up.
                Timer.delay(2);// second

                isStart = false; // we are done starting the motor
            }

            // Push frisbee into running motor to fire
            shooter.getFeeder().set(true);
            Timer.delay(.5);

            // Retract solenoid to allow next frisbee to be loaded into the
            // chamber.
            shooter.getFeeder().set(false);
            Timer.delay(2);
            // The shooter has launched one frisbee; therefore, there is one
            // less in the magazine.
            magazine--;
        } else {
            // The robot is done shooting; as a result, battery power does 
            // not need to be wasted running the shoot motor.
            shooter.deactivateShootMotorForAutonomous();
        }
        // }
        shooter.updatePressure();


    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        // Drive the robot in response to user input
        driver.drive();

        // Adjust the shooter arm angle based on the user input
        shooter.adjustAim();

        // Check and initiate shooting based on the fire button
        shooter.updateShooting();

        shooter.updatePressure();
        
        // When the user presses the necessary button, activate the climber
        lifter.reactToUserInput();

    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {

    }
}
