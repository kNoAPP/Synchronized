var buffer = argument0;
var msg_id = argument1;

switch(msg_id) {
	
	case obj_client.MSG_USER_ID:
		obj_game.uuid = buffer_read(buffer, buffer_string);
		show_debug_message("Got UUID: " + string(obj_game.uuid));
		break;
		
	default:
        show_debug_message("Received Unknown Request ID");
        break;
}