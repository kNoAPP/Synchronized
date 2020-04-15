/// @description Insert description here
// You can write your code in this editor
if(global.in_focus != id) {
	return;
}

if(keyboard_check_pressed(vk_enter)) {
	check_room_and_name();
	audio_play_sound(snd_bell, 1, false);
	return;
}

if(keyboard_check(vk_anykey) && string_length(text) < 16) {
	text += string_lettersdigits(keyboard_string);
	keyboard_string = "";
}

if(keyboard_check_pressed(vk_space) && string_length(text) < 16 
	&& string_length(text) > 0 && string_char_at(text, string_length(text)) != " ") {
	text += " ";
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