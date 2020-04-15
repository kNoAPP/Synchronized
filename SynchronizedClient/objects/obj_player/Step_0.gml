/// @description Insert description here
// You can write your code in this editor
if(room == rm_host_pregame_lobby || room == rm_host_game) {
	var players = players_get_size();
	var pi_div = players / 2;
	var offset = (index / players) * rotation_speed * 2;
	
	if(room == rm_host_game) {
		if(control_state != next_control_state) {
			if(next_control_state > control_state) {
				audio_play_sound(snd_click, 1, false);
			}
			
			// They pressed space
			if(next_control_state & $10 && !(control_state & $10)) {
				obj_shoot_progress_blocker.progress += 20 / players_get_size();
			}
			control_state = next_control_state;
		}
		
		image_index = control_state & $F;
		image_angle = 0;
		
		if(control_state & $8 && !has_jumped) {
			has_jumped = true;
			with(obj_mpc) {
				vsp -= v_speed / players_get_size();
			}
		}
		
		if(control_state & $4 && !has_crouched) {
			has_crouched = true;
			with(obj_mpc) {
				vsp += (v_speed * 0.5) / players_get_size();
			}
		}
		
		xTo = obj_mpc.x + 325*cos((offset+obj_game.step)*pi/rotation_speed);
		yTo = obj_mpc.y + 350*sin((offset+obj_game.step)*pi/rotation_speed);
	} else {
		image_index = 0;
		image_angle += 0.5;
		
		xTo = center_x + 325*cos((offset+obj_game.step)*pi/rotation_speed);
		yTo = center_y + 350*sin((offset+obj_game.step)*pi/rotation_speed);
	}
	
	x += (xTo - x)/16;
	y += (yTo - y)/16;
}