package grid.creation;

import entity.Combatant;
import entity.Player;
import grid.CompositeGrid;
import grid.Direction;
import grid.Point;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GridLoaderRectangles implements GridLoader {

    private static final Point DIMENSIONS = new Point(80, 80);
    private Set<Point> validAttachments;
    private Set<Point> invalidAttachments;
    private Set<Point> roomTiles;
    private final CompositeGrid grid;
    private final Random rng;

    public GridLoaderRectangles() {
        grid = new CompositeGrid("Test!", DIMENSIONS.x(), DIMENSIONS.y());
        rng = new Random();

        validAttachments = new HashSet<>();
        invalidAttachments = new HashSet<>();
        roomTiles = new HashSet<>();
    }

    @Override
    public CompositeGrid loadGrid() {
        Room r = new Hallway(10, 15);
        r.addToGrid();

        for(int x = 0; x < 200; x++) {
            int randomIndex = rng.nextInt(validAttachments.size());
            Point[] pointArray = validAttachments.toArray(new Point[0]);
            Point p = pointArray[randomIndex];
            Room s = new SmallRoom(p.x(), p.y());
            s.addToGrid();
        }

        for(int x = 0; x < DIMENSIONS.x(); x++) {
            for(int y = 0; y < DIMENSIONS.y(); y++) {
                Point current = new Point(x, y);
                if(!roomTiles.contains(current)) {
                    grid.setTileAt('#', current);
                }
            }
        }

        Combatant p1 = new Player("Test", "Test", 608, 202);
        p1.setId(0);
        grid.addCombatant(p1, new Point(11, 16));

        return grid;
    }

    private interface Room {
        void addToGrid();
    }

    private class Hallway implements Room {
        private final Point origin;
        private final Point dimensions;

        private Hallway(int x, int y) {
            origin = new Point(x, y);
            int shortEnd = rng.nextInt(3) + 5;
            int longEnd;
            if(x < y) {
                int maxLength = DIMENSIONS.x() - x;
                int baseRange = (int)Math.floor(maxLength * 0.8d);
                int varRange = (int)Math.floor(maxLength * 0.2d);
                longEnd = rng.nextInt(varRange) + baseRange;
                dimensions = new Point(longEnd, shortEnd);
            } else {
                int maxLength = DIMENSIONS.y() - y;
                int baseRange = (int)Math.floor(maxLength * 0.8d);
                int varRange = (int)Math.floor(maxLength * 0.2d);
                longEnd = rng.nextInt(varRange) + baseRange;
                dimensions = new Point(shortEnd, longEnd);
            }
        }

        public void addToGrid() {
            Point end = new Point(origin.x() + dimensions.x(), origin.y() + dimensions.y());
            for(int x = origin.x(); x < end.x(); x++) {
                for(int y = origin.y(); y < end.y(); y++) {
                    Point current = new Point(x, y);

                    if(x == origin.x() || x == end.x() - 1 || y == origin.y() || y == end.y() - 1) {
                        grid.setTileAt('#', current);
                        validAttachments.add(current);
                    } else {
                        roomTiles.add(current);
                    }
                }
            }
        }
    }

    private class SmallRoom implements Room {
        private final Point origin;
        private final Point dimensions;
        private final Point roomSize;
        private final Point doorPoint;
        private Direction expansionDirection;

        private SmallRoom(int x, int y) {
            origin = new Point(x, y);
            doorPoint = new Point(origin);
            dimensions = new Point(1, 1);
            roomSize = new Point(rng.nextInt(5) + 7, rng.nextInt(5) + 7);

            if(roomTiles.contains(new Point(origin.x() + 1, origin.y()))) {
                expansionDirection = Direction.WEST;
            } else if(roomTiles.contains(new Point(origin.x() - 1, origin.y()))) {
                expansionDirection = Direction.EAST;
            } else if(roomTiles.contains(new Point(origin.x(), origin.y() + 1))) {
                expansionDirection = Direction.NORTH;
            } else {
                expansionDirection = Direction.SOUTH;
            }
        }

        public void addToGrid() {

            int expansionPointer = 0;
            int sproutBounds;
            int sweepBounds;
            Point tracingPoint = new Point(origin);

            if(expansionDirection == Direction.EAST || expansionDirection == Direction.WEST) {
                sproutBounds = roomSize.x();
                sweepBounds = roomSize.y();
            } else {
                sproutBounds = roomSize.y();
                sweepBounds = roomSize.x();
            }

            while(expansionPointer < sproutBounds) {
                tracingPoint.moveStepsInDirection(expansionDirection, 1);
                if(tracingPoint.isValid(DIMENSIONS)) {
                    expandBoundsInDirection();
                    expansionPointer++;
                    if(!grid.getTileAt(tracingPoint).canOccupy()) {
                        break;
                    }
                } else {
                    tracingPoint.moveStepsInDirection(expansionDirection, -1);
                    break;
                }
            }

            int pctLeft = (int)(Math.round(rng.nextDouble() * 0.7 + 0.3 * sweepBounds));
            int pctRight = sweepBounds - pctLeft;
            expansionPointer = 0;

            if(expansionDirection == Direction.EAST) {
                expansionDirection = Direction.SOUTH;
            } else if(expansionDirection == Direction.SOUTH) {
                expansionDirection = Direction.WEST;
            } else if(expansionDirection == Direction.WEST) {
                expansionDirection = Direction.NORTH;
            } else {
                expansionDirection = Direction.EAST;
            }

            while(expansionPointer < pctLeft) {
                tracingPoint.moveStepsInDirection(expansionDirection, 1);
                if(tracingPoint.isValid(DIMENSIONS)) {
                    expandBoundsInDirection();
                    expansionPointer++;
                    if(!grid.getTileAt(tracingPoint).canOccupy()) {
                        break;
                    }
                } else {
                    break;
                }
            }

            if(expansionDirection == Direction.EAST) {
                expansionDirection = Direction.WEST;
            } else if(expansionDirection == Direction.SOUTH) {
                expansionDirection = Direction.NORTH;
            } else if(expansionDirection == Direction.WEST) {
                expansionDirection = Direction.EAST;
            } else {
                expansionDirection = Direction.SOUTH;
            }
            tracingPoint.moveStepsInDirection(expansionDirection, expansionPointer);
            expansionPointer = 0;

            while(expansionPointer < pctRight) {
                tracingPoint.moveStepsInDirection(expansionDirection, 1);
                if(tracingPoint.isValid(DIMENSIONS)) {
                    expandBoundsInDirection();
                    expansionPointer++;
                    if(!grid.getTileAt(tracingPoint).canOccupy()) {
                        break;
                    }
                } else {
                    break;
                }
            }

            if(dimensions.x() > 2 && dimensions.y() > 2) {
                Point end = new Point(origin.x() + dimensions.x(), origin.y() + dimensions.y());
                for(int x = origin.x(); x < end.x(); x++) {
                    for(int y = origin.y(); y < end.y(); y++) {
                        Point current = new Point(x, y);

                        if(x == origin.x() || x == end.x() - 1 || y == origin.y() || y == end.y() - 1) {
                            if(!roomTiles.contains(current)) {
                                if(current.equals(doorPoint)) {
                                    grid.setTileAt('.', current);
                                    roomTiles.add(current);
                                } else {
                                    grid.setTileAt('#', current);
                                }
                            }

                            if(validAttachments.contains(current) || invalidAttachments.contains(current)) {
                                validAttachments.remove(current);
                                invalidAttachments.add(current);
                            } else {
                                validAttachments.add(current);
                            }
                        } else {
                            roomTiles.add(current);
                            invalidAttachments.add(current);
                        }
                    }
                }

                invalidAttachments.add(origin);
                invalidAttachments.add(end);
                invalidAttachments.add(new Point(origin.x(), end.y()));
                invalidAttachments.add(new Point(end.x(), origin.y()));
                validAttachments.remove(origin);
                validAttachments.remove(end);
                validAttachments.remove(new Point(origin.x(), end.y()));
                validAttachments.remove(new Point(end.x(), origin.y()));
            }
        }

        private void expandBoundsInDirection() {
            if(expansionDirection == Direction.EAST) {
                dimensions.moveStepsInDirection(Direction.EAST, 1);
            } else if(expansionDirection == Direction.SOUTH) {
                dimensions.moveStepsInDirection(Direction.SOUTH, 1);
            } else if(expansionDirection == Direction.WEST) {
                origin.moveStepsInDirection(Direction.WEST, 1);
                dimensions.moveStepsInDirection(Direction.EAST, 1);
            } else {
                origin.moveStepsInDirection(Direction.NORTH, 1);
                dimensions.moveStepsInDirection(Direction.SOUTH, 1);
            }
        }
    }
}
