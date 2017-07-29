package generator;

import grid.Tile;

/**
 * Provides the basic functions for generating a proper grid.
 * Each implementor must implement a grid-generating algorithm that defines
 * the class.
 * @author Matt Imel
 */
public interface GridGenerator {
	Tile[][] generateGrid(int width, int height);
}