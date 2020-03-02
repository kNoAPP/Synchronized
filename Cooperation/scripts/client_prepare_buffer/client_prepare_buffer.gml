with(obj_client) {
	buffer_resize(argument0, argument2 + 3);
	buffer_seek(argument0, buffer_seek_start, 0);

	// Write handshake
	buffer_write(argument0, buffer_s16, MSG_HANDSHAKE);

	// Write the request type that is to be sent to the
	// buffer.
	buffer_write(argument0, buffer_s8, argument1);
}