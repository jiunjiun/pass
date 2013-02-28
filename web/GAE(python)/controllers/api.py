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
		import sys  
		from google.appengine.api import urlfetch
		from django.utils import simplejson
		from config import config
		
		sys.path.append('..')
		SERVER_URL = config.SERVER_URL
		try:
			reg	= simplejson.loads(self.request.get('reg'))			
			form_data = 'test[email]=%s&test[gcm]=%s' % (reg['email'], reg['gcm'])
			result = urlfetch.fetch(url=SERVER_URL, payload=form_data, method=urlfetch.POST)
		except Exception: 
			pass
		
class save(webapp2.RequestHandler):	
	def get(self):
		self.error(404)
		
	def post(self):
		import sys  
		from google.appengine.api import urlfetch
		from django.utils import simplejson
		from config import config

		sys.path.append('..')
		SERVER_URL = config.SERVER_URL
		try:
			reg	= simplejson.loads(self.request.get('reg'))			
			form_data = 'test[email]=%s&test[gcm]=%s' % (reg['email'], reg['gcm'])
			result = urlfetch.fetch(url=SERVER_URL, payload=form_data, method=urlfetch.POST)
		except Exception: 
			pass

		