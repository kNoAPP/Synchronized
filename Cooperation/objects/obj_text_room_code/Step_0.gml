/// @description Insert description here
// You can write your code in this editor
if(global.in_focus != id) {
	image_index = 0;
	return;
}

if(keyboard_check_pressed(vk_tab)) {
	global.in_focus = obj_text_player_name.id;
	obj_text_player_name.image_index = 1;
	keyboard_string = "";
	image_index = 0;
	return;
}

if(keyboard_check(vk_anykey) && string_length(text) < 4) {
	text += string_upper(string_letters(keyboard_string));
	keyboard_string = "";
}

if(keyboard_check_pressed(vk_backspace)) {
	text = string_delete(text, string_length(text), 1);
	keyboard_string = "";
	delete = 0.5*room_speed;
} else if(keyboard_check(vk_backspace)) {
	--delete;
	if(delete <= 0) {
		delete = 0.05*room_speed;
		text = string_delete(text, string_length(text), 1);
		keyboard_string = "";
	}
}