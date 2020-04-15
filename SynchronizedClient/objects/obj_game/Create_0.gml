/// @description Insert description here
// You can write your code in this editor

// Server variables
room_code = "";
level = 0;
max_players = 0;
positive_score = 0;
negative_score = 0;
player_name_to_obj = ds_map_create();
player_objs = ds_list_create();
step = 0;

// Client/server variables
host = false;
this_uuid = "";

// Roles
ROLE_POSITIVE = 0;
ROLE_NEGATIVE = 1;

// Client variables
this_name = "";
role = -1;

randomize();