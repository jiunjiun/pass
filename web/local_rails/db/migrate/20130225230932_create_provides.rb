class CreateProvides < ActiveRecord::Migration
  def change
    create_table(:provides, :bulk => true) do |t|

      t.timestamps
    end
	add_column :provides, :u_id, :integer
	add_index :provides, :u_id
	add_column :provides, :w_id, :integer
	add_index :provides, :w_id
  end
end
