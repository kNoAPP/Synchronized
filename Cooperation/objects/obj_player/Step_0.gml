/// @description Insert description here
// You can write your code in this editor
if(room == rm_host_pregame_lobby || room == rm_host_game) {
	var players = players_get_size();
	var pi_div = players / 2;
	var offset = (index / players) * rotation_speed * 2;
	xTo = center_x + 325*cos((offset+obj_game.step)*pi/rotation_speed);
	yTo = center_y + 350*sin((offset+obj_game.step)*pi/rotation_speed);

	x += (xTo - x)/16;
	y += (yTo - y)/16;
	
	if(room == rm_host_game) {
		image_index = control_state & $F;
		image_angle = 0;
		
		if(control_state & $8 && !has_jumped) {
			has_jumped = true;
			var role_power = role > 0 ? 3 : 1;
			with(obj_mpc) {
				vsp -= (role_power * v_speed) / players_get_size();
			}
		}
		
		if(control_state & $4 && !has_crouched) {
			has_crouched = true;
			var role_power = role > 0 ? 3 : 1;
			with(obj_mpc) {
				vsp += (role_power * v_speed) / players_get_size();
			}
		}
	} else {
		image_index = 0;
		image_angle += 0.5;
	}
}