package display;

import org.joml.Vector3f;

public enum Direction {
    W (0, new Vector3f(1.0f, 0.0f, 0.0f)),
    NW(1, new Vector3f(sqrt2div2(), -sqrt2div2(), 0.0f)),
    N (2, new Vector3f(0.0f, -1.0f, 0.0f)),
    SW(3, new Vector3f(sqrt2div2(), sqrt2div2(), 0.0f)),
    C (4, new Vector3f(0.0f, 0.0f, 0.0f)),
    NE(5, new Vector3f(-sqrt2div2(), -sqrt2div2(), 0.0f)),
    S (6,new Vector3f(0.0f, 1.0f, 0.0f)),
    SE(7, new Vector3f(-sqrt2div2(), sqrt2div2(), 0.0f)),
    E (8, new Vector3f(-1.0f, 0.0f, 0.0f));
    
    private int mapPosition;
    private Vector3f dir;
    private static final Direction[][] addMap;
    private static final Direction[][] subMap;

    static {
        addMap = new Direction[][] {
                {W , W , NW, W , W , W , SW, W , W },
                {NW, NW, NW, NW, NW, NW, NW, NW, NW},
                {NW, N , N , N , N , N , N , N , NE},
                {SW, SW, SW, SW, SW, SW, SW, SW, SW},
                {W , NW, N , SW, C , NE, S , SE, E },
                {NE, NE, NE, NE, NE, NE, NE, NE, NE},
                {SW, S , S , S , S , S , S , S , SE},
                {SE, SE, SE, SE, SE, SE, SE, SE, SE},
                {E , E , NE, E , E , E , SE, E , E }
        };

        subMap = new Direction[][] {
                {C , W , W , W , W , W , W , W , W },
                {N , C , W , NW, NW, NW, NW, NW, NW},
                {N , N , C , N , N , N , N , N , N },
                {S , SW, SW, C , SW, SW, W , SW, SW},
                {C , C , C , C , C , C , C , C , C },
                {NE, NE, E , NE, NE, C , NE, NE, N },
                {S , S , S , S , S , S , C , S , S },
                {SE, SE, SE, SE, SE, SE, E , C , S },
                {E , E , E , E , E , E , E , E , C }
        };
    }

    Direction(int mapPosition, Vector3f dir) {
        this.mapPosition = mapPosition;
        this.dir = dir;
    }

    private static float sqrt2div2() {
        return (float)Math.sqrt(2.0f)/2.0f;
    }

    public Vector3f getVectorDirection() {
        return dir;
    }

    public static Direction addDirection(Direction originalDirection, Direction directionToAdd) {
        return addMap[originalDirection.mapPosition][directionToAdd.mapPosition];
    }

    public static Direction subtractDirection(Direction originalDirection, Direction directionToRemove) {
        return subMap[originalDirection.mapPosition][directionToRemove.mapPosition];
    }
}
