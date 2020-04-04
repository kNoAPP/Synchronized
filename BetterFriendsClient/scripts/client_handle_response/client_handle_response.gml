var msg_id = argument0;
var buffer = argument1;

with(obj_client) {
	switch(msg_id) {
	
		case MSG_USER_ID:
			var uuid = buffer_read(buffer, buffer_string);
			obj_game.uuid = uuid;
			show_debug_message("Got UUID: " + string(obj_game.uuid));
			return true;
		
		case MSG_UPDATE_NAME:
			obj_game.name = buffer_read(buffer, buffer_string);
			show_debug_message("Got Name: " + string(obj_game.name));
			return true;
		
		case MSG_CREATE_GAME:
			obj_game.max_players = buffer_read(buffer, buffer_u8);
			obj_game.room_code = buffer_read(buffer, buffer_string);
			show_debug_message("Got Room Code: " + string(obj_game.room_code));
			return true;
		
		case MSG_JOIN_GAME:
			var successCode = buffer_read(buffer, buffer_u8);
			switch(successCode) {
				case 0:
					show_message("That room does not exist. Try a different room code!");
					obj_text_room_code.image_index = 2;
					break;
				
				case 1:
					show_message("This room is full.");
					obj_text_room_code.image_index = 2;
					break;
				
				case 2:
					show_message("Someone already is using that name! Choose another.");
					obj_text_player_name.image_index = 2;
					break;
			
				case 3:
					// show_message("You're in! Waiting for host...");
					room_goto(rm_player_waiting);
					break;
			}
			obj_send_button.image_index = 0;
			return true;
		
		case MSG_HOST_ADD_PLAYER:
			var name = buffer_read(buffer, buffer_string);
			players_add_player(name);	
			return true;
		
		case MSG_HOST_REMOVE_PLAYER:
			var name = buffer_read(buffer, buffer_string);
			players_remove_player(name);	
			return true;
		
		case MSG_PLAYER_GAME_ENDED:
			if(room != rm_title_screen) {
				room_goto(rm_title_screen);
			}
			return true;
			
		case MSG_PROGRESS_GAME:
			show_debug_message("Progressing room... ");
			var state = buffer_read(buffer, buffer_u8);
			switch(state) {
				case 0:
					room_goto(rm_player_waiting);
					break;
					
				case 1:
					room_goto(rm_player_instructions);	
					break;
					
				case 2:
					room_goto(rm_player_role_assignment);
					break;
					
				case 3:
					room_goto(rm_player_game);
					break;
					
				case 4:
					room_goto(rm_player_waiting);
					break;
			}
			return true;
			
		case MSG_ASSIGN_ROLE:
			show_debug_message("Got role... ");
			if(obj_game.host) {
				var name = buffer_read(buffer, buffer_string);
				var inst = obj_game.player_name_to_obj[? name];
				inst.role = buffer_read(buffer, buffer_u8);
			} else
				obj_game.role = buffer_read(buffer, buffer_u8);
			return true;
			
		case MSG_CONTROL_STATE:
			show_debug_message("Got control state... ");
			var name = buffer_read(buffer, buffer_string);
			var control_state = buffer_read(buffer, buffer_u8);
			var inst = obj_game.player_name_to_obj[? name];
			if(inst.control_state & $10 != control_state & $10) {
				if(control_state & $10)
					audio_play_sound(snd_sneeze, 1, false);
			} else
				audio_play_sound(snd_click, 1, false);
			inst.control_state = control_state;
			return true;
		
		
		default:
	        show_debug_message("Received Unknown Request ID");
	        return false;
	}
}