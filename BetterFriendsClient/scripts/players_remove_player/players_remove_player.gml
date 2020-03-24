var name = argument0;

with(obj_game) {
	var inst = player_name_to_obj[? name];
	var i = ds_list_find_index(player_objs, inst);
	ds_list_delete(player_objs, i);
	ds_map_delete(player_name_to_obj, name);
	instance_destroy(inst);
	
	for(var j=i; j<ds_list_size(player_objs); j++) {
		var update = ds_list_find_value(player_objs, j);
		update.index = j;
	}
	
	if(room == rm_host_pregame_lobby) {
		audio_play_sound(snd_quit, 1, false);
	}
}