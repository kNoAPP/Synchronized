/// @description Insert description here
// You can write your code in this editor
draw_self();
var original_font = draw_get_font();
var original_color = draw_get_color();
draw_set_color(c_white);
draw_rectangle(x - 2, y - 6, x + 50, y + 7, false);
draw_set_color(c_black);
draw_text(x, y - 10, "Name:");
draw_set_font(fnt_medium);
draw_set_color(c_black);
draw_text(x, y + 5, text);
draw_set_font(original_font);
draw_set_color(original_color);