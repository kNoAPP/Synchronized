/// @description Insert description here
// You can write your code in this editor
step += 1;

if(host && room != rm_host_pregame_lobby && players_get_size() <= 0) {
	obj_client.room_update = rm_host_pregame_lobby;
	client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_PROGRESS_GAME);
	show_message("You don't have enough players!");
}