/// @description Insert description here
// You can write your code in this editor
room_goto(rm_host_pregame_lobby);
with(obj_game) {
	host = true;
	room_code = "...";
}

client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_CREATE_GAME);
