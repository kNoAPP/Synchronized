/// @description Insert description here
// You can write your code in this editor

if(host && room == rm_host_pregame_lobby) {
	var original_font = draw_get_font();
	var original_color = draw_get_color();
	draw_set_color(c_black);
	draw_rectangle(15, 30, 240, 115, false);
	draw_set_color(c_white);
	draw_rectangle(20, 35, 235, 110, false);
	draw_set_font(fnt_large);
	draw_set_color(c_orange);
	draw_text(20, 20, room_code);
	draw_set_font(original_font);
	draw_set_color(original_color);
}

