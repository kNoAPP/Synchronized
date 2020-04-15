/// @description Insert description here
// You can write your code in this 
name = "";
role = -1;
next_control_state = $0;
control_state = $0;
has_jumped = false;
has_crouched = false;

center_x = 512;
center_y = 384;
rotation_speed = 1024;
index = 0;

image_angle = irandom_range(0, 359);
depth = -999;