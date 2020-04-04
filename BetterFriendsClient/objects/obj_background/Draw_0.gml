/// @description Insert description here
// You can write your code in this editor
var color = draw_get_color();
var r = 127*sin(pi*red / 1024) + 128;
var g = 127*sin(pi*green / 1024) + 128;
var b = 127*sin(pi*blue / 1024) + 128;
draw_set_color(make_color_rgb(r, g, b));
draw_rectangle(0, 0, room_width, room_height, false);
draw_set_color(color);