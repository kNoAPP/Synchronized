var name = argument0;

with(obj_game) {
	var inst = instance_create_layer(0, 0, "Instances", obj_player);
	ds_list_add(player_objs, inst);
	inst.name = name;
	inst.index = ds_list_find_index(player_objs, inst);
	player_name_to_obj[? name] = inst;
	
	if(room == rm_host_pregame_lobby) {
		inst.visible = true;
	}
}