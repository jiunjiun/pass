class RegisterController < ApplicationController
	before_filter :regder_nothing_event, :only => [ :index, :show, :new, :edit, :update, :destroy]
	
	def index
	end
	
	def show
	end
	
	def new
	end
	
	def edit
	end
	
	def create
		render :nothing  => true
		@user = User.new(params[:user]).save
	end
	
	def update
	end
	
	def destroy
	end
	
	protected
	def regder_nothing_event
		render :nothing  => true
	end
end
