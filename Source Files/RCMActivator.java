package ecoRe.recyclingSystem;


public class RCMActivator {
	private final RCMUI machine;

	public RCMActivator(RCMUI machine) {
		this.machine = machine;
	}

	public RCMUI getMachine() {
		return machine;
	}

	// Activates machine
	public void activateMachine() {
		machine.setEnabled(true);
	}

	// De - Activates machine
	void deactivateMachine() {
		machine.setEnabled(false);
	}

	public String toString() {
		return "Machine Activator \nMachineID=" + machine.toString();
	}
}
