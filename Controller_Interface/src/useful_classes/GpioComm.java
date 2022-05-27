package useful_classes;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class GpioComm {
	// crear controlador gpio
	/*final GpioController gpio = GpioFactory.getInstance();
	
	// provisionar pin #01 como output y encenderlo como estado inicial
	final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.HIGH);

	final GpioPinDigitalOutput io4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "MyLED", PinState.HIGH);
	final GpioPinDigitalOutput io5 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "MyLED", PinState.HIGH);
	final GpioPinDigitalOutput io6 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "MyLED", PinState.HIGH);
	final GpioPinDigitalOutput io12 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "MyLED", PinState.HIGH);
	final GpioPinDigitalOutput io17 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_17, "MyLED", PinState.HIGH);
	final GpioPinDigitalOutput io18 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18, "MyLED", PinState.HIGH);
	final GpioPinDigitalOutput io22 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, "MyLED", PinState.HIGH);
	final GpioPinDigitalOutput io23 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "MyLED", PinState.HIGH);
	final GpioPinDigitalOutput io24 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "MyLED", PinState.HIGH);
	final GpioPinDigitalOutput io25 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "MyLED", PinState.HIGH);
	final GpioPinDigitalOutput io27 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "MyLED", PinState.HIGH);
	
	private GpioPinDigitalOutput selectPin(String sys_pin){
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
	}*/
	
	public void setHigh(String sys_pin) {
		//selectPin(sys_pin).high();
	}
	
	public void setLow(String sys_pin) {
		//selectPin(sys_pin).low();
	}
	
	public void setToggle(String sys_pin) {
		//selectPin(sys_pin).toggle();
	}
	
	public void setPulse(String sys_pin, int miliseconds) {
		//selectPin(sys_pin).pulse(miliseconds);
		System.out.println(sys_pin+" "+miliseconds+" pulse");
	}
	
	public void setPulse(String sys_pin, int miliseconds,boolean blocking) {
		//selectPin(sys_pin).pulse(miliseconds,blocking);
		System.out.println(sys_pin+" "+miliseconds+" pulse");
	}
	
	public void shutdown() {
	//	gpio.shutdown();
	}
}
