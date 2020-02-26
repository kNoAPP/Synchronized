var buffer = async_load[? "buffer"];
var in_id = async_load[? "id"];

var size = buffer_get_size(buffer);
show_debug_message("Buffer size: " + string(size));

if(in_id == server) {
	var handshake = buffer_read(buffer, buffer_u16);
	show_debug_message(string(handshake));
	if(handshake == obj_client.MSG_HANDSHAKE) {
		var msg_id = buffer_read(buffer, buffer_u16);
		show_debug_message(string(msg_id));
		client_handle_response(buffer, msg_id);
	}
}