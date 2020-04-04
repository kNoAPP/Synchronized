/// @description Insert description here
// You can write your code in this editor
room_goto(rm_title_screen);

client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_QUIT_GAME);