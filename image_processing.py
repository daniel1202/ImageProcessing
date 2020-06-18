from cv2 import cv2
import numpy as np
import os
import math
from matplotlib import pyplot as plt
from PIL import Image, ImageEnhance
from py4j.java_gateway import JavaGateway, CallbackServerParameters
from scipy.interpolate import UnivariateSpline

class ImageProcessing():

    class Java:
        implements = ["sample.ImageProcessor"]

    def __init__(self, gateway):
        self.gateway = gateway

    def histograms(self, path):
        img = cv2.imread(path)
        img_rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        img_lum = cv2.cvtColor(img, cv2.COLOR_BGR2YCR_CB)
        histr = cv2.calcHist([img_rgb],[0],None,[256],[0,256])
        histg = cv2.calcHist([img_rgb],[1],None,[256],[0,256])
        histb = cv2.calcHist([img_rgb],[2],None,[256],[0,256])
        histl = cv2.calcHist([img_lum],[0],None,[256],[0,256])

        plt.xlabel("Pixel value")
        plt.ylabel("Number of pixels")
        plt.plot(histr, color="r", label="Red")
        plt.savefig("swap/red.png")
        plt.clf()
        plt.plot(histg, color="g", label="Green")
        plt.savefig("swap/green.png")
        plt.clf()
        plt.plot(histb, color="b", label="Blue")
        plt.savefig("swap/blue.png")
        plt.clf()
        plt.plot(histl, color="grey", label="Lumi")
        plt.savefig("swap/lum.png")
        plt.clf()
    
    def contrast(self, contrast):
        img = Image.open('swap/image.png')
        cont = ImageEnhance.Contrast(img)
        img = cont.enhance(contrast)
        img.save('swap/processed.png')

    def brightness(self, brightness):
        img = Image.open('swap/image.png')
        bright = ImageEnhance.Brightness(img)
        img = bright.enhance(brightness)
        img.save('swap/processed.png')

    def saturation(self, saturation):
        img = Image.open('swap/image.png')
        sat = ImageEnhance.Color(img)
        img = sat.enhance(saturation)
        img.save('swap/processed.png')

    def sharpness(self, sharpness):
        if sharpness < 1:
	        sharpness = (sharpness + 10)/11
        img = Image.open('swap/image.png')
        enhancer = ImageEnhance.Sharpness(img)
        img = enhancer.enhance(sharpness)
        img.save('swap/processed.png')

    def color_temperature(self, temperature):
        img = Image.open('swap/image.png')
        r, g, b = self.kelvin_rgb(temperature)
        matrix = (r / 255.0, 0.0, 0.0, 0.0, 0.0, g / 255.0, 0.0, 0.0, 0.0, 0.0, b / 255.0, 0.0)
        img = img.convert('RGB', matrix)
        img.save('swap/processed.png')

    def color_balance(self, r_scalar, g_scalar, b_scalar):
        img = Image.open('swap/image.png')
        matrix = (r_scalar, 0.0, 0.0, 0.0, 0.0, g_scalar, 0.0, 0.0, 0.0, 0.0, b_scalar, 0.0)
        img = img.convert('RGB', matrix)
        img.save('swap/processed.png')

    def all_operations(self, contrast, brightness, saturation, sharpness, temperature, r_scalar, g_scalar, b_scalar):
        contrast = (contrast+10)/10
        brightness = (brightness+10)/10
        saturation = (saturation+10)/10
        if sharpness < 1:
	        sharpness = ((sharpness - (-10))/(1- (-10))) * (1- (-2)) - 2
        img = Image.open('swap/image.png')
        # contrast
        cont = ImageEnhance.Contrast(img)
        img = cont.enhance(contrast)
        # brightness
        bright = ImageEnhance.Brightness(img)
        img = bright.enhance(brightness)
        # saturation
        sat = ImageEnhance.Color(img)
        img = sat.enhance(saturation)
        # sharpness
        enhancer = ImageEnhance.Sharpness(img)
        img = enhancer.enhance(sharpness)
        # # color temperature
        r, g, b = self.kelvin_rgb(temperature)
        matrix = (r / 255.0, 0.0, 0.0, 0.0, 0.0, g / 255.0, 0.0, 0.0, 0.0, 0.0, b / 255.0, 0.0)
        img = img.convert("RGB")
        img = img.convert("RGB", matrix)
        # # color balance
        matrix = (r_scalar , 0.0, 0.0, 0.0, 0.0, g_scalar, 0.0, 0.0, 0.0, 0.0, b_scalar, 0.0)
        img = img.convert("RGB", matrix)
        img.save('swap/processed.png')

    def kelvin_rgb(self, temperature):
        # Source: http://www.tannerhelland.com/4435/convert-temperature-rgb-algorithm-code/
        temperature /= 100.0

        if temperature <= 66:
            red = 255
        else:
            red = 329.698727446 * math.pow(temperature - 60, -0.1332047592)
            if red < 0:
                red = 0
            elif red > 255:
                red = 255
            else:
                red = red

        if temperature <=66:
            green = 99.4708025861 * math.log(temperature) - 161.1195681661
            if green < 0:
                green = 0
            elif green > 255:
                green = 255
            else:
                green = green
        else:
            green = 288.1221695283 * math.pow(temperature - 60, -0.0755148492)
            if green < 0:
                green = 0
            elif green > 255:
                green = 255
            else:
                green = green

        if temperature >=66:
            blue = 255
        elif temperature <= 19:
            blue = 0
        else:
            blue = 138.5177312231 * math.log(temperature - 10) - 305.0447927307
            if blue < 0:
                blue = 0
            elif blue > 255:
                blue = 255
            else:
                blue = blue
        
        return red, green, blue

if __name__ == "__main__":
    dirName = "swap"
    if not os.path.exists(dirName):
        os.mkdir(dirName)
    gateway = JavaGateway(
        callback_server_parameters=CallbackServerParameters()
    )
    processor = ImageProcessing(gateway)
    gateway.entry_point.setProcessor(processor)