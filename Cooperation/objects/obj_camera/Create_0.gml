/// @description Insert description here
// You can write your code in this editor
view_camera[0] = camera_create();

var vm = matrix_build_lookat(x, y, -1000, x, y, 0, 0, 1, 0);
var pm = matrix_build_projection_ortho(1024, 768, 1, 10000);

camera_set_view_mat(view_camera[0], vm);
camera_set_proj_mat(view_camera[0], pm);

follow = obj_mpc;

xTo = x;
yTo = y;