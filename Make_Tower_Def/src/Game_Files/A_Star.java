package Game_Files;

import tileengine.TERenderer;
import tileengine.Tileset;

import java.util.*;

public class A_Star {
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    private boolean[][] visited;

    private DifferentMaps curMap;

    int[] up = {0, 1};
    int[] down = {0, -1};
    int[] left = {-1, 0};
    int[] right = {1, 0};

    public int[][] allDirections = {up, down, left, right};
    TERenderer worldRender = new TERenderer();

    public A_Star(int startX, int startY, int endX, int endY, DifferentMaps curMap) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        this.curWorld = curWorld;

        this.visited = new boolean[curWorld.WIDTH][curWorld.HEIGHT];
        for (int i = 0; i < curWorld.WIDTH; i++) {
            for (int j = 0; j < curWorld.HEIGHT; j++) {
                visited[i][j] = false;
            }
        }
    }

    public static class Node {
        int x;
        int y;
        int cost;
        int heuristic;
        Node path;

        Node(int x, int y, int cost, int heuristic, Node path) {
            this.x = x;
            this.y = y;
            this.cost = cost;
            this.heuristic = heuristic;
            this.path = path;
        }

        int total_Cost() {
            return heuristic + cost;
        }
    }

    public int manhattanDistance(int curX, int curY) {
        return Math.abs(curX - endX) + Math.abs(curY - endY);
    }

    public List<Node> findShortestPath() {
        Comparator<Node> compareHeuristic = new Comparator<Node>() {

            @Override
            public int compare(Node o1, Node o2) {
                return Integer.compare(o1.total_Cost(), o2.total_Cost());
            }
        };

        PriorityQueue<Node> checkHeuristic = new PriorityQueue<>(compareHeuristic);
        Node startNode = new Node(startX, startY, 0, manhattanDistance(startX, startY), null);
        checkHeuristic.add(startNode);

        while (!checkHeuristic.isEmpty()) {
            Node currentNode = checkHeuristic.poll();
//            System.out.println("this is my startX: " + startX + "; this is my startY: " + startY);
//            System.out.println("this is my curX: " + currentNode.x + "; this is my curY: " + currentNode.y);

//            System.out.println("this is my endX: " + endX + "; this is my endY: " + endY);
            if (visited[currentNode.x][currentNode.y]) {
                continue;
            }
            visited[currentNode.x][currentNode.y] = true;
//            curWorld.finalWorld[currentNode.x][currentNode.y] = Tileset.FLOWER;
//            worldRender.renderFrame(curWorld.finalWorld);

            //INSERT BREAK CONDITION
            if (currentNode.x == endX && currentNode.y == endY) {
                return returnPath(currentNode);
            }

            for (int[] direction : allDirections) {
                int newX = currentNode.x + direction[0];
                int newY = currentNode.y + direction[1];

                if (curWorld.inBounds(newX, newY) && !visited[newX][newY] &&
                        (curWorld.finalWorld[newX][newY] != Tileset.WALL) &&
                        (curWorld.finalWorld[newX][newY] != Tileset.PAPER)) {
                    int nextCost = currentNode.cost + 1;
                    int dist = manhattanDistance(newX, newY);
                    Node nextNode = new Node(newX, newY, nextCost, dist, currentNode);
                    checkHeuristic.add(nextNode);
                }
            }
        }
        return null;
    }

    public List<Node> returnPath(Node lastNode) {
        List<Node> finalPath = new ArrayList<>();
        Node curNode = lastNode;

        while (curNode != null) {
            finalPath.add(curNode);
            curNode = curNode.path;
        }
        Collections.reverse(finalPath);
        return finalPath;
    }
}
