package org.usfirst.frc.team20.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	CANTalon tableTalon;
	Joystick joy;
	int ticksPerDegree = 44; // number of ticks per degree of the table
	double desiredPos = 0;
	boolean clockwise = true, counterClockwise = true;	//is the direction disabled?

	@Override
	public void robotInit() {
		tableTalon = new CANTalon(13);
		joy = new Joystick(0);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {

	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {

	}

	/**
	 * This function is called periodically during operator control
	 */

	@Override
	public void teleopInit() {
		tableTalon.setPosition(0);
	}

	@Override
	public void teleopPeriodic() {
		double ticksToTurn = 50 * ticksPerDegree;
		double position = tableTalon.getPosition();
		double ticksLeft = Math.abs(position - desiredPos);
		// remote controls
		if (joy.getRawButton(1)) { // clockwise
			desiredPos = position - ticksToTurn;
		}
		if (joy.getRawButton(2)) { // counter clockwise
			desiredPos = position + ticksToTurn;
		}
		if (joy.getRawButton(3)) {
			tableTalon.set(0);
			desiredPos = position;
		}
		System.out.println(position);
		System.out.println("     " + desiredPos);
		// moving the turret to the desired position
		if (desiredPos < position + 100 && desiredPos > position - 100) { //tolerance ~0.5 degrees
			System.out.println("          Done Turning!!!");
			tableTalon.set(0);
			tableTalon.enableLimitSwitch(true, true);
			clockwise = true; counterClockwise = true;
		} else if (desiredPos < position && clockwise) {
			if (ticksLeft < 300) {
				tableTalon.set(-0.6);
			} else {
				tableTalon.set(-1.0);
				System.out.println("          Clockwise");
			}
		} else if (desiredPos > position && counterClockwise) {
			if (ticksLeft < 300) {
				tableTalon.set(0.6);
			} else {
				tableTalon.set(1.0);
				System.out.println("          Counter Clockwise");
			}
		}
		// limit switches
		if (!tableTalon.isFwdLimitSwitchClosed()) {
			System.out.println("Forward Switch Triggered");
			tableTalon.set(0);
			tableTalon.enableLimitSwitch(false, false);
			desiredPos = position + 200; clockwise = false;
		} else if (!tableTalon.isRevLimitSwitchClosed()) {
			System.out.println("Reverse Switch Triggered");
			tableTalon.set(0);
			tableTalon.enableLimitSwitch(false, false);
			desiredPos = position - 200; counterClockwise = false;
		}
	}

	/**
	 * This function is called periodically during tableTalon mode
	 */
	@Override
	public void testInit() {
		tableTalon.setPosition(0);
	}

	@Override
	public void testPeriodic() {
		System.out.println(tableTalon.getPosition());
	}
}
