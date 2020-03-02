/// @description Insert description here
// You can write your code in this editor
if(keyboard_check_pressed(ord("R"))) {
	control_state |= $10;
	client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_CONTROL_STATE);
}

if(keyboard_check_released(ord("R"))) {
	control_state &= ~$10;
	client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_CONTROL_STATE);
}

if(keyboard_check_pressed(vk_up)) {
	control_state |= $8;
	client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_CONTROL_STATE);
}

if(keyboard_check_released(vk_up)) {
	control_state &= ~$8;
	client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_CONTROL_STATE);
}

if(keyboard_check_pressed(vk_down)) {
	control_state |= $4;
	client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_CONTROL_STATE);
}

if(keyboard_check_released(vk_down)) {
	control_state &= ~$4;
	client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_CONTROL_STATE);
}

if(keyboard_check_pressed(vk_right)) {
	control_state |= $2;
	client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_CONTROL_STATE);
}

if(keyboard_check_released(vk_right)) {
	control_state &= ~$2;
	client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_CONTROL_STATE);
}

if(keyboard_check_pressed(vk_left)) {
	control_state |= $1;
	client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_CONTROL_STATE);
}

if(keyboard_check_released(vk_left)) {
	control_state &= ~$1;
	client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_CONTROL_STATE);
}