/// @description Insert description here
// You can write your code in this editor

var h_votes = 0;
with(obj_player) {
	if(control_state & $2)
		h_votes += role > 0 ? 2 : 1;
	if(control_state & $1)
		h_votes -= role > 0 ? 2 : 1;
}

hsp = (h_votes / players_get_size()) * h_speed;
if(place_meeting(x+hsp, y, obj_ground)) {
	while(!place_meeting(x+sign(hsp), y, obj_ground)) {
		x += sign(hsp);
	}
	hsp = 0;
}
x += hsp;

if(vsp < 10)
	vsp += grav;
if(place_meeting(x, y+vsp, obj_ground)) {
	while(!place_meeting(x, y+sign(vsp), obj_ground)) {
		y += sign(vsp);
	}
	vsp = 0;
	with(obj_player) {
		has_jumped = false;
		has_crouched = false;
	}
}
y += vsp;