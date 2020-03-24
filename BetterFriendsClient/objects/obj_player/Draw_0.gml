/// @description Insert description here
// You can write your code in this editor
draw_self();

var original_font = draw_get_font();
var original_color = draw_get_color();
var original_halign = draw_get_halign();
draw_set_font(fnt_small);
draw_set_color(c_black);
draw_set_halign(fa_center);
draw_text(x, y-12, name);
draw_set_font(original_font);
draw_set_color(original_color);
draw_set_halign(original_halign);