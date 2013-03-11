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
from controllers.home 	import home
from controllers.api 	import register
from controllers.api 	import wifi
from controllers.api 	import renew

app = webapp2.WSGIApplication([
    ('/', home),
	('/api/register/', register),
	('/api/wifi/', wifi),
	('/api/renew/', renew),
], debug=True)
