class User < ActiveRecord::Base
  has_many	:provide, 	:dependent => 	:destroy
  has_many 	:wifis, 	:through => 	:provide
  # attr_accessible :title, :body
  
  validates :gcm,			:presence 	=> true,
							:uniqueness	=> true
  
  validates :email,			:presence 	=> true
  validates_format_of 		:email, :with => /\A[^@]+@([^@\.]+\.)+[^@\.]+\z/
end
