package useful_classes;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.util.Console;

import java.util.concurrent.TimeUnit;

import com.pi4j.*;

public class GpioComm {
	// crear controlador gpio
	Context pi4j = Pi4J.newAutoContext();
	DigitalOutputConfigBuilder config = DigitalOutput.newConfigBuilder(pi4j)
	        .provider("linuxfs-digital-output")
	        .shutdown(DigitalState.LOW)
	        .initial(DigitalState.LOW);

	DigitalOutput io4 = pi4j.dout().create(config.address(4));
	DigitalOutput io5 = pi4j.dout().create(config.address(5));
	DigitalOutput io6 = pi4j.dout().create(config.address(6));
	DigitalOutput io12 = pi4j.dout().create(config.address(12));
	DigitalOutput io17 = pi4j.dout().create(config.address(17));
	DigitalOutput io18 = pi4j.dout().create(config.address(18));
	DigitalOutput io22 = pi4j.dout().create(config.address(22));
	DigitalOutput io23 = pi4j.dout().create(config.address(23));
	DigitalOutput io24 = pi4j.dout().create(config.address(24));
	DigitalOutput io25 = pi4j.dout().create(config.address(25));
	DigitalOutput io27 = pi4j.dout().create(config.address(27));
	
	private DigitalOutput selectPin(String sys_pin){
		switch(sys_pin) {
		case "BACKLIGHT":
			return io4;
		case "RB":
			return io5;
		case "GND":
			return io6;
		case "CARRILLON":
			return io12;
		case "RA":
			return io17;
		case "TC":
			return io18;
		case "TA":
			return io22;
		case "BC":
			return io23;
		case "BB":
			return io24;
		case "BA":
			return io25;
		case "TB":
			return io27;
		default:
			return null;
		}
	}
	
	public void setHigh(String sys_pin) {
		selectPin(sys_pin).high();
	}
	
	public void setLow(String sys_pin) {
		selectPin(sys_pin).low();
	}
	
	public void setToggle(String sys_pin) {
		selectPin(sys_pin).toggle();
	}
	
	public void setPulse(String sys_pin, int miliseconds) {
		selectPin(sys_pin).pulse(miliseconds, TimeUnit.MILLISECONDS);
		System.out.println(sys_pin+" "+miliseconds+" pulse");
	}
	
	public void shutdown() {
		pi4j.shutdown();
	}
}
