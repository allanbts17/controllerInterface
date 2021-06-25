# Escribe tu código aquí :-)
import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BOARD)
GPIO.setwarnings(False)
GPIO.setup(3,GPIO.OUT)


GPIO.output(3,True)
time.sleep(0.01)
GPIO.output(3,False)
    
