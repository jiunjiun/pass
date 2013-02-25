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
from config import config

class index(webapp2.RequestHandler):
	def get(self):
		self.response.out.write(template.render('template/layout.html',''))

class register(webapp2.RequestHandler):		
	def get(self):
		from django.utils import simplejson as json
		jsonstr = []
		user_email = self.request.get('gcm')
		gcm = self.request.get('gcm')
		
		jsonstr = {'user_email' : user_email, 'gcm' : gcm}
		jsonEncode = json.dumps( jsonstr )

		from google.appengine.api import urlfetch
		url = "http://localhost/"
		form_fields = {
		  "first_name": "Albert",
		  "last_name": "Johnson",
		  "email_address": "Albert.Johnson@example.com"
		}
		form_data = urllib.urlencode(form_fields)
		result = urlfetch.fetch(url=url,
								payload=form_data,
								method=urlfetch.POST,
								headers={'Content-Type': 'application/x-www-form-urlencoded'})
		
		if result.status_code == 200:
			self.response.write(result.content)
		else:
			self.response.write('error')
		
	def post(self):
		# from django.utils import simplejson as json
		# user_email = self.request.get('gcm')
		# gcm = self.request.get('gcm')
		# result = json.dumps( mac )
		self.response.write('')
		
class find(webapp2.RequestHandler):		
	def post(self):
		from django.utils import simplejson as json
		mac = self.request.get('m')
		result = json.dumps( mac )
		self.response.write(result)
		
class save(webapp2.RequestHandler):		
	def post(self):
		from django.utils import simplejson as json
		mac = self.request.get('m')
		result = json.dumps( mac )
		self.response.write(result)

		