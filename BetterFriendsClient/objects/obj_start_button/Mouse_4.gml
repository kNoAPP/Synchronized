/// @description Insert description here
// You can write your code in this editor
if(room == rm_host_pregame_lobby) {
	obj_client.room_update = rm_host_instructions;
} else if(room == rm_host_role_assignment) {
	obj_client.room_update = rm_host_game;
}
	
client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_PROGRESS_GAME);