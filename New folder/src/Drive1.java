
package org.usfirst.frc.team3470.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;

import org.usfirst.frc.team3470.robot.commands.ExampleCommand;
import org.usfirst.frc.team3470.robot.subsystems.ExampleSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	RobotDrive myRobot;
	XboxController controller;
    Timer timer;
	
	@Override
	public void robotInit() {
		oi = new OI();
		chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		myRobot = new RobotDrive(0,2,1,3);
		myRobot.setInvertedMotor(RobotDrive..kFrontRight, true);
		myRobot.setInvertedMotor(RobotDrive.MotorTypMotorTypee.kRearRight, true);
		controller = new XboxController(1);
		timer = new Timer();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	double LX = 0;
	double LY = 0;
	double RX = 0;
	double RY = 0;
	double LDeadZone = 0;
	double RDeadZone = 0;
	double LDist = 0;
	double RDist = 0;
	boolean turning;
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		LDeadZone = SmartDashboard.getNumber("LDeadZone", 0.25);
		RDeadZone = SmartDashboard.getNumber("RDeadZone", 0.25);
		LX = controller.getX(GenericHID.Hand.kLeft);
		LY = controller.getY(GenericHID.Hand.kLeft);
		RX = controller.getX(GenericHID.Hand.kRight);
		RY = controller.getY(GenericHID.Hand.kRight);
		LDist = Math.sqrt( Math.pow(LX, 2) + Math.pow(LY, 2) );
		RDist = Math.sqrt( Math.pow(RX, 2) + Math.pow(RY, 2) );
		if (LDist > RDist && LDist > LDeadZone) {
			myRobot.mecanumDrive_Cartesian(LX, LY, 0, 0);
		}
		else if (RDist > LDist && RDist > RDeadZone) {
			myRobot.mecanumDrive_Cartesian(0, RY, RX, 0);
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
