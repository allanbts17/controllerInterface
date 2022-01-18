# Escribe tu código aquí :-)
import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BOARD)
GPIO.setwarnings(False)
GPIO.setup(11,GPIO.OUT)

GPIO.output(11,True)
time.sleep(0.01)
GPIO.output(11,False)
    
