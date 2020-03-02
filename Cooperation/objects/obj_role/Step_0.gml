/// @description Insert description here
// You can write your code in this editor

if(obj_game.role == obj_game.ROLE_POSITIVE) {
	obj_role.sprite_index = spr_player_role_positive;
} else if(obj_game.role == obj_game.ROLE_NEGATIVE) {
	obj_role.sprite_index = spr_player_role_negative;
}