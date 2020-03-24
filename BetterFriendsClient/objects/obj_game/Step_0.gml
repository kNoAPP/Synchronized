/// @description Insert description here
// You can write your code in this editor
step += 1;

if(host && room != rm_host_pregame_lobby && players_get_size() <= 0) {
	// Tell server host quit and goto title_screen;
	game_restart();
	//room_goto(rm_title_screen);
}