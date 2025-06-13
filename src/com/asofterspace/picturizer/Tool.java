/**
 * Unlicensed code created by A Softer Space, 2023
 * www.asofterspace.com/licenses/unlicense.txt
 */
package com.asofterspace.picturizer;


public enum Tool {

	// pipette to set foreground color
	PIPETTE_FG,

	// pipette to set background color
	PIPETTE_BG,

	// drawing pen with foreground color
	DRAW_PEN_FG,

	// draw connected lines with foreground color
	DRAW_LINES_FG,

	// fill a clicked area with foreground color
	FILL_FG,
	FILL_ROUGHLY_FG,

	// draw rectangle with foreground / background color
	DRAW_RECTANGLE_FG,
	DRAW_RECTANGLE_BG,

	// draw quads (area with one quadrangle after another) with foreground / background color
	DRAW_QUADS_FG,
	DRAW_QUADS_BG,

	// draw area with foreground / background color
	DRAW_AREA_FG,
	DRAW_AREA_BG;
}
