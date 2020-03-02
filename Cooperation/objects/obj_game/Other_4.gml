/// @description Insert description here
// You can write your code in this editor
if(room == rm_title_screen) {
	room_code = "";
	max_players = 0;
	positive_score = 0;
	negative_score = 0;
	step = 0;
	
	host = false;
	
	role = -1;
	
	ds_map_clear(player_name_to_obj);
	with(obj_player) {
		instance_destroy();
	}
	ds_list_clear(player_objs);
}

if(room == rm_host_instructions) {
	with(obj_player) {
		visible = false;
	}
}

if(room == rm_host_game) {
	with(obj_player) {
		visible = true;
	}
}