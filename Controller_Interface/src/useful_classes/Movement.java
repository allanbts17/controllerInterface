package useful_classes;

import javax.swing.JComponent;

public class Movement extends Thread {
	private float initialPosition;
	private float finalPosition;
	private float initialVelocity;
	private float aceleration;
	private float time = 0;
	private float duration;
	private JComponent objetiveElement;
	private float objetiveParameter;
	private String[] movementTypes = {"uniform","accelerated","custom"};
	private String selectedMovementType;
	private char positionComponent;
	private float actualPosition;
	private int timeInterval = 1000;
	private int componentX;
	private int componentY;
	
		
	public Movement(JComponent objetiveElement) {
		this.objetiveElement = objetiveElement;
	}
	
	public void setUniformMovement(int x0, int x1, int duration,char positionComponent) {
		selectedMovementType = movementTypes[1];
		initialPosition = x0;
		finalPosition = x1;
		this.duration = duration;
		this.positionComponent = positionComponent;
		initialVelocity = this.duration/(finalPosition - initialPosition);
		componentX = objetiveElement.getLocation().x;
		componentY = objetiveElement.getLocation().y;
	}
	
	public void setUniformMovement(int x1,int duration,char positionComponent) {
		selectedMovementType = movementTypes[0];
		
		this.duration = duration;
		this.positionComponent = positionComponent;
		componentX = objetiveElement.getLocation().x;
		componentY = objetiveElement.getLocation().y;
		if(positionComponent == 'x')
			initialPosition = componentX;
		else
			initialPosition = componentY;
		finalPosition = x1;
		initialVelocity = (float)(finalPosition - initialPosition)/(float)this.duration;
		System.out.println("Velocity: "+initialVelocity);
		System.out.println("selectedMovementType: "+selectedMovementType);
	}
	
	
	public void setAcceleratedMovement() {
		
	}
	
	@Override
	public void run() {
		switch(selectedMovementType) {
		case "uniform":
			uniformMovement();
			break;
		}
			 
	}
	
	private void uniformMovement() {
		long mil = System.currentTimeMillis();
		while(actualPosition < finalPosition) {
			actualPosition = (actualPosition > finalPosition)? finalPosition:initialVelocity * time + initialPosition;
			actualPosition = Math.round(actualPosition);
			//System.out.println("position: "+actualPosition);
			time += timeInterval;
			if(positionComponent == 'x')
				objetiveElement.setLocation((int)actualPosition,componentY);
			else
				objetiveElement.setLocation(componentX,(int)actualPosition);
			
			try {
				Thread.sleep((long)timeInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		System.out.println(System.currentTimeMillis()-mil);
	}
	
}
