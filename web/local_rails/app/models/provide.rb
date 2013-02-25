class Provide < ActiveRecord::Base
  belongs_to :user
  belongs_to :wifi
  # attr_accessible :title, :body
  
  validates	:u_id		:presence	=> true, 
						:uniqueness	=> true
						
  validates	:w_id		:presence	=> true, 
						:uniqueness	=> true
end
