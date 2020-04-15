/// @description Insert description here
// You can write your code in this editor
obj_game.level = 1;
obj_client.room_update = rm_host_game;
client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_PROGRESS_GAME);