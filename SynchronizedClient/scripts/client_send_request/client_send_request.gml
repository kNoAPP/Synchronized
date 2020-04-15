/// @description client_send_request(socket, buffer, request)
var socket = argument0;
var buffer = argument1;
var request = argument2;

with(obj_client) {
	switch(request) {

	    case MSG_USER_ID:
	        show_debug_message("User ID Requested");
			client_prepare_buffer(buffer, request, 0);
	        network_send_packet(socket, buffer, buffer_tell(buffer));
	        break;
		
		case MSG_UPDATE_NAME:
			show_debug_message("Sent Update Name");
			var name = obj_text_player_name.text;
			client_prepare_buffer(buffer, request, string_length(name));
			buffer_write(buffer, buffer_string, name);
			network_send_packet(socket, buffer, buffer_tell(buffer));
			break;
		
		case MSG_CREATE_GAME:
			show_debug_message("Create Game Requested");
			client_prepare_buffer(buffer, request, 0);
			network_send_packet(socket, buffer, buffer_tell(buffer));
			break;
		
		case MSG_JOIN_GAME:
			show_debug_message("Sent Join Game");
			var code = obj_text_room_code.text;
			client_prepare_buffer(buffer, request, string_length(code));
			buffer_write(buffer, buffer_string, code);
			network_send_packet(socket, buffer, buffer_tell(buffer));
			break;
			
		case MSG_PROGRESS_GAME:
			show_debug_message("Sent Progress Game");
			client_prepare_buffer(buffer, request, 1);
			switch(room_update) {
				case rm_host_pregame_lobby:
					buffer_write(buffer, buffer_u8, 0);
					break;
					
				case rm_host_instructions:
					buffer_write(buffer, buffer_u8, 1);
					break;
					
				case rm_host_level_select:
					buffer_write(buffer, buffer_u8, 2);
					break;
					
				case rm_host_game:
					buffer_write(buffer, buffer_u8, 3);
					break;
					
				case rm_host_win:
				case rm_host_lose:
					buffer_write(buffer, buffer_u8, 4);
					break;
			}
			room_goto(room_update);
	        network_send_packet(socket, buffer, buffer_tell(buffer));
			break;
			
		case MSG_CONTROL_STATE:
			show_debug_message("Sent Control State");
			client_prepare_buffer(buffer, request, 1);
			buffer_write(buffer, buffer_u8, obj_player_movement.control_state);
	        network_send_packet(socket, buffer, buffer_tell(buffer));
			break;
			
		case MSG_QUIT_GAME:
			show_debug_message("Sent Quit Game");
			client_prepare_buffer(buffer, request, 0);
			network_send_packet(socket, buffer, buffer_tell(buffer));
			break;

	    default:
	        show_debug_message("Unknown ID trying to send");
	        break;
	}
}