#!/usr/bin/env python
#
# Copyright 2007 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
import webapp2
import urllib

from google.appengine.ext.webapp import template


class index(webapp2.RequestHandler):
	def get(self):
		self.error(404)

class register(webapp2.RequestHandler):	
	def get(self):
		self.error(404)
		
	def post(self):
		import json, sys, re
		from google.appengine.api import urlfetch
		from config import config
		
		sys.path.append('..')
		SERVER_URL = config.USER_URL
		try:
			reg 			= self.request.get('reg')
			reg_arr			= json.loads(reg)
			reg_arr['gps'] 	= json.loads(reg_arr['gps']) 

			if len(reg_arr['email']) > 0 and re.match(r"[^@]+@[^@]+\.[^@]+", reg_arr['email']) and len(reg_arr['registrarId']) > 0 and len(reg_arr['gps']) > 0 and len(reg_arr['gps']['lat']) > 0 and len(reg_arr['gps']['lon']) > 0:
				form_data 	= 'reg=%s&k=reg' % (reg)
				result = urlfetch.fetch(url=SERVER_URL, payload=form_data, method=urlfetch.POST)
				
				# if result.status_code == 200:
				# self.response.out.write(SERVER_URL)
				self.response.out.write(result.content)
		except Exception, e: 
			self.response.out.write(str(e))
			# self.error(404)

		
class wifi(webapp2.RequestHandler):	
	def get(self):
		self.error(404)
		
	def post(self):
		import json, sys, re
		from google.appengine.api import urlfetch
		from config import config

		sys.path.append('..')
		SERVER_URL = config.WIFI_URL
		try:
			wifi 			= self.request.get('wifi')
			wifi_arr		= json.loads(wifi)
			wifi_arr['gps'] = json.loads(wifi_arr['gps']) 
			
			
			if len(wifi_arr['MAC']) > 0 and re.match(r"([a-fA-F0-9]{2}[:|\-]?){6}", wifi_arr['MAC']):
				form_data = 'wifi=%s&k=save' % (wifi)
				result = urlfetch.fetch(url=SERVER_URL, payload=form_data, method=urlfetch.POST)
				
				# if result.status_code == 200:
				self.response.out.write(result.content)
		except Exception, e: 
			self.response.out.write(str(e))
			# self.error(404)
		

class renew(webapp2.RequestHandler):	
	def get(self):
		self.error(404)
		
	def post(self):
		import json, sys, re
		from google.appengine.api import urlfetch
		from config import config

		sys.path.append('..')
		SERVER_URL = config.WIFI_URL
		try:
			user 			= self.request.get('user')
			user_arr		= json.loads(user)
			
			if len(user_arr['email']) > 0 and re.match(r"[^@]+@[^@]+\.[^@]+", user_arr['email']):
				form_data = 'user=%s&k=renew' % (user)
				result = urlfetch.fetch(url=SERVER_URL, payload=form_data, method=urlfetch.POST)
				
				# if result.status_code == 200:
				self.response.out.write(result.content)
		except Exception, e: 
			self.response.out.write(str(e))
			# self.error(404)		
		
class publicWifi(webapp2.RequestHandler):	
	def get(self):
		self.error(404)	
		
	def post(self):
		import json, sys
		from google.appengine.api import urlfetch
		from config import config	
		
		sys.path.append('..')
		SERVER_URL = config.WIFI_URL
		try:
			publicWifi 		= self.request.get('publicWifi')
			publicWifi_arr 	= json.loads(publicWifi)
			
			form_data = 'publicWifi=%s&k=publicWifi' % (publicWifi)
			result = urlfetch.fetch(url=SERVER_URL, payload=form_data, method=urlfetch.POST)
			
			self.response.out.write(result.content)
		except Exception, e: 
			self.response.out.write(str(e))
			# self.error(404)	
		
		