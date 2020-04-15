with(obj_send_button) {
	if(image_index != 0)
		return;

	var correct = true;

	if(string_length(obj_text_room_code.text) != 4) {
		obj_text_room_code.image_index = 2;
		correct = false;
	}
	
	if(string_length(obj_text_player_name.text) < 3) {
		obj_text_player_name.image_index = 2;
		correct = false;
	}

	if(correct) {
		image_index = 1;
		client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_UPDATE_NAME);
		client_send_request(obj_client.server, obj_client.buffer, obj_client.MSG_JOIN_GAME);
	}
}