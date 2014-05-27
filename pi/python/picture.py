#!/usr/bin/python

import picamera
import time, os, ConfigParser, sys
from ftplib import FTP
from PIL import Image, ImageOps

tag = sys.argv[1]
uuid = sys.argv[2].split('\xfe')[0]

local_folder = "/home/pi/py/"
if (tag == 'pic'):
	if (uuid != ''):
		config = ConfigParser.ConfigParser()
		config.read(local_folder + 'config.ini')
		host = config.get('connection', 'host')
		user = config.get('connection', 'user')
		password = config.get('connection','password')
		folder = config.get('connection','folder')

		camera = picamera.PiCamera()

		profile_img = uuid + ".png"
		profile_marker = uuid + "_marker.png"
		mask_img = "mask.png"
	
		ftp = FTP()
		ftp.connect(host)
		ftp.login(user, password)
		ftp.cwd(folder)
	
		camera.resolution = (512, 512)
		camera.capture(local_folder + profile_img)

		mask = Image.open(local_folder + mask_img).convert('L')
		im = Image.open(local_folder + profile_img)
		output = ImageOps.fit(im, mask.size, centering=(0.5, 0.5))
		output.putalpha(mask)
		output.save(local_folder + profile_marker)
		f = open(local_folder + profile_img, 'r')
		ftp.storbinary('STOR ' + folder + profile_img, f)
		f = open(local_folder + profile_marker, 'r')
		ftp.storbinary('STOR ' + folder + profile_marker, f)
	
		ftp.close()
	else:
		print 'No arguments'
elif (tag == 'item'):
	print 'Pick up item'


else:
	print 'Incorrect tag'
