class User < ActiveRecord::Base
  attr_accessible :email, :gcm
  has_many	:provide, 	:dependent => 	:destroy
  has_many 	:wifis, 	:through => 	:provide
  
  
  validates :gcm,			:presence 	=> true,
							:uniqueness	=> true
  
  validates :email,			:presence 	=> true
  validates_format_of 		:email, :with => /\A[^@]+@([^@\.]+\.)+[^@\.]+\z/
end
