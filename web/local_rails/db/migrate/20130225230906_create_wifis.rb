class CreateWifis < ActiveRecord::Migration
  def change
    create_table :wifis do |t|
	  t.string		:SSID
	  t.string		:MAC
	  t.string		:passwd
	  t.string		:gps
      t.timestamps
    end
  end
end
