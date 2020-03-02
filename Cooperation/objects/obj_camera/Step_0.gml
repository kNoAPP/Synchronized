/// @description Insert description here
// You can write your code in this editor
if(follow != noone) {
	xTo = follow.x;
	yTo = follow.y;
}

x += (xTo - x)/1;
y += (yTo - y)/1;

var vm = matrix_build_lookat(x, y, -10, x, y, 0, 0, 1, 0);
camera_set_view_mat(view_camera[0], vm);
