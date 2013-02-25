class Wifi < ActiveRecord::Base
  has_many 	:provide, 	:dependent => 	:destroy
  has_many 	:users, 	:through => 	:provide
  # attr_accessible :title, :body
  
  validates	:SSID		:presence	=> true
  
  validates	:MAC		:presence	=> true, 
						:uniqueness	=> true
end
