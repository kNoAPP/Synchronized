buffer_resize(argument0, argument2);
buffer_seek(argument0, buffer_seek_start, 0);

// Write handshake
buffer_write(argument0, buffer_s16, obj_client.MSG_HANDSHAKE);

// Write the request type that is to be sent to the
// buffer.
buffer_write(argument0, buffer_s8, argument1);