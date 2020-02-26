/// @description client_send_request(socket, buffer, request, size)
var socket = argument0;
var buffer = argument1;
var request = argument2;

switch(request) {

    case MSG_USER_ID:
        show_debug_message("User ID Requested");
		client_prepare_buffer(buffer, request, 0);
        network_send_packet(socket, buffer, buffer_tell(buffer));
        break;

    default:
        show_debug_message("Unknown ID trying to send");
        break;
}