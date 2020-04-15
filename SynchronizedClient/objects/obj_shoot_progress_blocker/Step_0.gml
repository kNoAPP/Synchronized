/// @description Insert description here
// You can write your code in this editor
x = obj_shoot_progress_bar.x + (obj_shoot_progress_bar.sprite_width / 2) - 3;
y = obj_shoot_progress_bar.y;

if(progress >= 100) {
	progress = 0;
	var bullet = instance_create_layer(obj_mpc.x, obj_mpc.y - 100, "Instances", obj_bullet);
	audio_play_sound(snd_shoot, 1, false);
}

image_xscale = -124 + (progress * 1.23);

if(progress > 0)
	progress -= 25 / room_speed;