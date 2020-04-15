/// @description Insert description here
// You can write your code in this editor

if(x <= 0 || room_width <= x) {
	if(ignore_collisions <= 0) {
		look_at_dir = (direction - 180) % 360;
		ignore_collisions = 120;
	}
}
	
if(y <= 0 || room_height <= y) {
	if(ignore_collisions <= 0) {
		look_at_dir = (direction - 180) % 360;
		ignore_collisions = 120;
	}
}

if(irandom(200) == 1) {
	look_at_dir = random(359);
}

image_angle += (look_at_dir - image_angle)/16;
direction = image_angle;

if(ignore_collisions > 0)
	ignore_collisions--;
