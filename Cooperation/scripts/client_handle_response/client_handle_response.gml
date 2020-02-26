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
		
	default:
        show_debug_message("Received Unknown Request ID");
        return false;
}