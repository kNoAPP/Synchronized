/// @description Insert description here
// You can write your code in this editor
draw_self();

var self_x = x;
var self_y = y;
with(obj_title_bubble) {
	var dist = point_distance(x, y, self_x, self_y);
	if(dist != 0 && dist < 400) {
		var alpha = 1 - (dist/400);
		draw_set_alpha(alpha);
		draw_line_width_color(x, y, self_x, self_y, 2, c_white, c_white);
		draw_set_alpha(1);
	}
}