from cv2 import cv2
import numpy as np
import os
from matplotlib import pyplot as plt
from PIL import Image, ImageEnhance
from py4j.java_gateway import JavaGateway, CallbackServerParameters
from scipy.interpolate import UnivariateSpline

class ImageProcessing():
    # def __init__(self, gateway):
    #     self.gateway = gateway

    def __init__(self):
        self.kelvin_table = {
            1000: (255, 56, 0),
            1100: (255, 71, 0),
            1200: (255, 83, 0),
            1300: (255, 93, 0),
            1400: (255, 101, 0),
            1500: (255, 109, 0),
            1600: (255, 115, 0),
            1700: (255, 121, 0),
            1800: (255, 126, 0),
            1900: (255, 131, 0),
            2000: (255, 138, 18),
            2100: (255, 142, 33),
            2200: (255, 147, 44),
            2300: (255, 152, 54),
            2400: (255, 157, 63),
            2500: (255, 161, 72),
            2600: (255, 165, 79),
            2700: (255, 169, 87),
            2800: (255, 173, 94),
            2900: (255, 177, 101),
            3000: (255, 180, 107),
            3100: (255, 184, 114),
            3200: (255, 187, 120),
            3300: (255, 190, 126),
            3400: (255, 193, 132),
            3500: (255, 196, 137),
            3600: (255, 199, 143),
            3700: (255, 201, 148),
            3800: (255, 204, 153),
            3900: (255, 206, 159),
            4000: (255, 209, 163),
            4100: (255, 211, 168),
            4200: (255, 213, 173),
            4300: (255, 215, 177),
            4400: (255, 217, 182),
            4500: (255, 219, 186),
            4600: (255, 221, 190),
            4700: (255, 223, 194),
            4800: (255, 225, 198),
            4900: (255, 227, 202),
            5000: (255, 228, 206),
            5100: (255, 230, 210),
            5200: (255, 232, 213),
            5300: (255, 233, 217),
            5400: (255, 235, 220),
            5500: (255, 236, 224),
            5600: (255, 238, 227),
            5700: (255, 239, 230),
            5800: (255, 240, 233),
            5900: (255, 242, 236),
            6000: (255, 243, 239),
            6100: (255, 244, 242),
            6200: (255, 245, 245),
            6300: (255, 246, 247),
            6400: (255, 248, 251),
            6500: (255, 249, 253),
            6600: (254, 249, 255),
            6700: (252, 247, 255),
            6800: (249, 246, 255),
            6900: (247, 245, 255),
            7000: (245, 243, 255),
            7100: (243, 242, 255),
            7200: (240, 241, 255),
            7300: (239, 240, 255),
            7400: (237, 239, 255),
            7500: (235, 238, 255),
            7600: (233, 237, 255),
            7700: (231, 236, 255),
            7800: (230, 235, 255),
            7900: (228, 234, 255),
            8000: (227, 233, 255),
            8100: (225, 232, 255),
            8200: (224, 231, 255),
            8300: (222, 230, 255),
            8400: (221, 230, 255),
            8500: (220, 229, 255),
            8600: (218, 229, 255),
            8700: (217, 227, 255),
            8800: (216, 227, 255),
            8900: (215, 226, 255),
            9000: (214, 225, 255),
            9100: (212, 225, 255),
            9200: (211, 224, 255),
            9300: (210, 223, 255),
            9400: (209, 223, 255),
            9500: (208, 222, 255),
            9600: (207, 221, 255),
            9700: (207, 221, 255),
            9800: (206, 220, 255),
            9900: (205, 220, 255),
            10000: (207, 218, 255),
            10100: (207, 218, 255),
            10200: (206, 217, 255),
            10300: (205, 217, 255),
            10400: (204, 216, 255),
            10500: (204, 216, 255),
            10600: (203, 215, 255),
            10700: (202, 215, 255),
            10800: (202, 214, 255),
            10900: (201, 214, 255),
            11000: (200, 213, 255),
            11100: (200, 213, 255),
            11200: (199, 212, 255),
            11300: (198, 212, 255),
            11400: (198, 212, 255),
            11500: (197, 211, 255),
            11600: (197, 211, 255),
            11700: (197, 210, 255),
            11800: (196, 210, 255),
            11900: (195, 210, 255),
            12000: (195, 209, 255)} 

    def histograms(self):
        img = cv2.imread('swap/image.jpg')
        img_rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        img_lum = cv2.cvtColor(img, cv2.COLOR_BGR2YCR_CB)
        histr = cv2.calcHist([img_rgb],[0],None,[256],[0,256])
        histg = cv2.calcHist([img_rgb],[1],None,[256],[0,256])
        histb = cv2.calcHist([img_rgb],[2],None,[256],[0,256])
        histl = cv2.calcHist([img_lum],[0],None,[256],[0,256])

        plt.xlabel("Pixel value")
        plt.ylabel("Number of pixels")
        plt.plot(histr, color="r", label="Red")
        plt.savefig("swap/red.jpg")
        plt.clf()
        plt.plot(histg, color="g", label="Green")
        plt.savefig("swap/green.jpg")
        plt.clf()
        plt.plot(histb, color="b", label="Blue")
        plt.savefig("swap/blue.jpg")
        plt.clf()
        plt.plot(histl, color="grey", label="Lumi")
        plt.savefig("swap/lum.jpg")
        plt.clf()
    
    def contrast(self, contrast):
        img = Image.open('swap/image.jpg')
        cont = ImageEnhance.Contrast(img)
        img = cont.enhance(contrast)
        img.save('swap/processed.jpg')

    def brightness(self, brightness):
        img = Image.open('swap/image.jpg')
        bright = ImageEnhance.Brightness(img)
        img = bright.enhance(brightness)
        img.save('swap/processed.jpg')

    def saturation(self, saturation):
        img = Image.open('swap/image.jpg')
        sat = ImageEnhance.Color(img)
        img = sat.enhance(saturation)
        img.save('swap/processed.jpg')

    def smoothing(self):
        image = cv2.imread('swap/image.jpg')
        image = cv2.blur(image,(5,5))
        cv2.imwrite('swap/processed.jpg', image)

    def sharpening(self):
        matrix = np.array([[-1,-1,-1], [-1,9,-1], [-1,-1,-1]])
        image = cv2.imread('swap/image.jpg')
        image = cv2.filter2D(image, -1, matrix)
        cv2.imwrite('swap/processed.jpg', image)

    def color_temperature(self, value):
        image = Image.open('swap/image.jpg')
        r, g, b = self.kelvin_table[value]
        color_matrix = (r / 255.0, 0.0, 0.0, 0.0, 0.0, g / 255.0, 0.0, 0.0, 0.0, 0.0, b / 255.0, 0.0)
        image = image.convert('RGB', color_matrix)
        image.save('swap/processed.jpg')

    class Java:
        implements = ["sample.ImageProcessor"]

if __name__ == "__main__":
    dirName = "swap"
    if not os.path.exists(dirName):
        os.mkdir(dirName)
    gateway = JavaGateway(
        callback_server_parameters=CallbackServerParameters()
    )
    processor = ImageProcessing()
    print("cokolwiek1")
    gateway.entry_point.setProcessor(processor)
    print("cokolwiek2")



