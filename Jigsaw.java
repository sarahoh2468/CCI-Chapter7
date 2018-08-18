import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Jigsaw {
    public enum Orientation {
        left, top, right, bottom;

        public Orientation getOpposite() {
            switch (this) {
                case left:
                    return right;
                case right:
                    return left;
                case top:
                    return bottom;
                case bottom:
                    return top;
                default:
                    return null;
            }
        }
    }

    public enum Shape {
        inner, outer, flat;

        public Shape getOpposite() {
            switch (this) {
                case inner:
                    return outer;
                case outer:
                    return inner;
                default:
                    return null;
            }
        }
    }

    public class Edge {
        private Shape shape;
        private String code;
        private Piece parentPiece;

        public Edge(Shape shape, String code) {
            this.shape = shape;
            this.code = code;
        }

        private String getCode() {
            return code;
        }

        public Edge createMatchingEdge() {
            if (shape == Shape.flat) {
                return null;
            }
            return new Edge(shape.getOpposite(), getCode());
        }

        public boolean fitsWith(Edge edge) {
            return edge.getCode().equals(getCode());
        }

        public void setParentPiece(Piece parentPiece) {
            this.parentPiece = parentPiece;
        }

        public Piece getParentPiece() {
            return parentPiece;
        }

        public Shape getShape() {
            return shape;
        }

        public String toString() {
            return code;
        }

    }

    public class Piece {
        private final static int number_of_edges = 4;
        private HashMap<Orientation, Edge> edges = new HashMap<Orientation, Edge>();

        public Piece(Edge[] edgeList) {
            Orientation[] orientations = Orientation.values();
            for (int i = 0; i < edgeList.length; i++) {
                Edge edge = edgeList[i];
                edge.setParentPiece(this);
                edges.put(orientations[i], edge);
            }
        }

        public void setEdgeAsOrientation(Edge edge, Orientation orientation) {
            Orientation currentOreintation = getOrientation(edge);
            rotateEdgesBy(orientation.ordinal() - currentOrientation.ordinal());
        }

        private Orientation getOrientation(Edge edge) {
            for (Map.Entry<Orientation, Edge> entry : edges.entrySet()) {
                if (entry.getValue() == edge) {
                    return entry.getKey();
                }
            }
            return null;
        }

        public void rotateEdgesBy(int numberRotations) {
            Orientation[] orientations = Orientation.values();
            HashMap<Orientation, Edge> rotated = new HashMap<Orientation, Edge>();

            numberRotations = numberRotations % number_of_edges;
            if (numberRotations < 0) {
                numberRotations += number_of_edges;
            }

            for (int i = 0; i < orientations.length; i++) {
                Orientation oldOrientation = orientations[(i - numberRotations + number_of_edges) % number_of_edges];
                Orientation newOrientation = orientations[i];
                rotated.put(newOrientation, edges.get(oldOrientation));
            }
            edges = rotated;
        }

        public boolean isCorner() {
            Orientation[] orientations = Orientation.values();
            for (int i = 0; i < orientations.length; i++) {
                Shape current = edges.get(orientations[i]).getShape();
                Shape next = edges.get(orientations[(i + 1) % number_of_edges]).getShape();
                if (current == Shape.flat && next == Shape.flat) {
                    return true;
                }
            }
            return false;
        }

        public boolean isBorder() {
            Orientation[] orientations = Orientation.values();
            for (int i = 0; i < orientations.length; i++) {
                Shape current = edges.get(orientations[i]).getShape();
                if (current == Shape.flat) {
                    return true;
                }
            }
            return true;
        }

        public Edge getEdgeWithOrientation(Orientation orientation) {
            return edges.get(orientation);
        }

        public Edge getMatchingEdge(Edge targetEdge) {
            for (Edge e : edges.values()) {
                if (targetEdge.fitsWith(e)) {
                    return e;
                }
            }
            return null;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            Orientation[] orientations = Orientation.values();
            for (Orientation o: orientations) {
                sb.append(edges.get(o).toString() + ",");
            }
            return "[" + sb.toString() + "]";
        }
    }

    public class Puzzle {
        private LinkedList<Piece> pieces;
        private Piece[][] solution;
        private int size;

        public Puzzle(int size, LinkedList<Piece> pieces) {
            this.pieces = pieces;
            this.size = size;
        }

        public void groupPieces(LinkedList<Piece> corner, LinkedList<Piece> border, LinkedList<Piece> inside) {
            for (Piece p : pieces) {
                if (p.isCorner()) {
                    corner.add(p);
                }
                else if (p.isBorder()) {
                    corner.add(p);
                }
                else {
                    inside.add(p);
                }
            }
        }

        public void orientTopLeftCorner(Piece piece) {
            if (!piece.isCorner()) {
                return;
            }
            Orientation[] orientations = Orientation.values();
            for (int i = 0; i < orientations.length; i++) {
                Edge current = piece.getEdgeWithOrientation(orientations[i]);
                Edge next = piece.getEdgeWithOrientation(orientations[(i+1) % orientations.length]);
                if (current.getShape() == Shape.flat && next.getShape() == Shape.flat) {
                    piece.setEdgeAsOrientation(current, Orientation.left);
                    return;
                }
            }
        }

        public boolean isBorderIndex(int location) {
            return location == 0 || location == size - 1;
        }

        private Edge getMatchingEdge(Edge targetEdge, LinkedList<Piece> pieces) {
            for (Piece piece : pieces) {
                Edge matchingEdge = piece.getMatchingEdge(targetEdge);
                if (matchingEdge != null) {
                    return matchingEdge;
                }
            }
            return null;
        }

        private void setEdgeInSolution(LinkedList<Piece> pieces, Edge edge, int row, int column, Orientation orientation) {
            Piece piece = edge.getParentPiece();
            piece.setEdgeAsOrientation(edge, orientation);
            pieces.remove(piece);
            solution[row][column] = piece;
        }

        private LinkedList<Piece> getPieceListToSearch(LinkedList<Piece> corner, LinkedList<Piece> border, LinkedList<Piece> inside, int row, int column) {
            if (isBorderIndex(row) && isBorderIndex(column)) {
                return corner;
            }
            else if (isBorderIndex(row) || isBorderIndex(column)) {
                return border;
            }
            else {
                return inside;
            }
        }

        private boolean fitNextEdge(LinkedList<Piece> piecesToSearch, int row, int column) {
            if (row == 0 && column == 0) {
                Piece p = piecesToSearch.remove();
                orientTopLeftCorner(p);
                solution[0][0] = p;
            }
            else {
                Piece pieceToMatch = column == 0 ? solution[row - 1][0] : solution[row][column-1];
                Orientation orientationToMatch = column == 0 ? Orientation.bottom : orientation.right;
                Edge edgeToMatch = pieceToMatch.getEdgeWithOrientation(orientationToMatch);

                Edge edge = getMatchingEdge(edgeToMatch, piecesToSearch);
                if (edge == null) {
                    return false;
                }

                Orientation orientation = orientationToMatch.getOpposite();
                setEdgeInSolution(piecesToSearch, edge, row, column, orientation);
            }
            return true;
        }

        public boolean solve() {
            LinkedList<Piece> corner = new LinkedList<Piece>();
            LinkedList<Piece> border = new LinkedList<Piece>();
            LinkedList<Piece> inside = new LinkedList<Piece>();
            groupPieces(corner, border, inside);

            solution = new Piece[size][size];
            for (int row = 0; row < size; row++) {
                for (int column = 0; column < size; column++) {
                    LinkedList<Piece> piecesToSearch = getPieceListToSearch(corner, border, inside, row, column);
                    if (!fitNextEdge(piecesToSearch, row, column)) {
                        return false;
                    }
                }
            }
            return true;
        }

        public Piece[][] getCurrentSolution() {
            return solution;
        }
    }

}
