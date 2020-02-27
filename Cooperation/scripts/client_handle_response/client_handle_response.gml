var msg_id = argument0;
var buffer = argument1;

switch(msg_id) {
	
	case MSG_USER_ID:
		var uuid = buffer_read(buffer, buffer_string);
		if(uuid != 0 && string_length(uuid) == 36) {
			obj_game.uuid = uuid;
			show_debug_message("Got UUID: " + string(obj_game.uuid));
			return true;
		}
		return false;
		
	case MSG_UPDATE_NAME:
		return true;
		
	case MSG_CREATE_GAME:
		obj_game.room_code = buffer_read(buffer, buffer_string);
		obj_game.host = true;
		show_debug_message("Got Room Code: " + string(obj_game.room_code));
		return true;
		
	case MSG_JOIN_GAME:
		return true;
		
	default:
        show_debug_message("Received Unknown Request ID");
        return false;
}