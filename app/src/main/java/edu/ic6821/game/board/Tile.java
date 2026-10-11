package edu.ic6821.game.board;

import java.util.Optional;

public final class Tile {
    private Optional<Tile> up;
    private Optional<Tile> down;
    private Optional<Tile> left;
    private Optional<Tile> right;

    private final Integer nodeId;
    private Integer value;

    public Tile(final Integer nodeId) {
        this.nodeId = nodeId;
        this.up = Optional.empty();
        this.down = Optional.empty();
        this.left = Optional.empty();
        this.right = Optional.empty();
        this.value = 0;
    }

    public Optional<Tile> getUp() {
        return up;
    }

    public void setUp(Tile upTile) {
        this.up = Optional.ofNullable(upTile);
    }

    public Optional<Tile> getDown() {
        return down;
    }

    public void setDown(Tile downTile) {
        this.down = Optional.ofNullable(downTile);
    }

    public Optional<Tile> getLeft() {
        return left;
    }

    public void setLeft(Tile leftTile) {
        this.left = Optional.ofNullable(leftTile);
    }

    public Optional<Tile> getRight() {
        return right;
    }

    public void setRight(Tile rightTile) {
        this.right = Optional.ofNullable(rightTile);
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer valueTile) {
        this.value = valueTile;
    }

    public Integer getNodeId() {
        return nodeId;
    }
}
