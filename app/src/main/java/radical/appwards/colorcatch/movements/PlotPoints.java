package radical.appwards.colorcatch.movements;

import android.graphics.Point;

public class PlotPoints {

    /**
     * This class is used to get an array of Point radical.appwards.colorcatch.objects that are the line
     * from a to b;
     */
    Point a, b, point;
    Point[] pointArr;

    /**
     * Takes as input two points and returns a Point[] that contains every point
     * to make as close to a straight line as possible.
     *
     * @param a
     *            Point 1
     * @param b
     *            Point 2
     * @return Point[] of all points to traverse from point a to point b.
     */
    public Point[] plotLine(Point a, Point b) {
        this.a = a;
        this.b = b;
        // create array of points based on longer x or y distance
        if (Math.abs(a.y - b.y) > Math.abs(a.x - b.x)) {
            pointArr = new Point[Math.abs(a.y - b.y)];
        } else {
            pointArr = new Point[Math.abs(a.x - b.x)];
        }

        for (int i = 0; i < pointArr.length; i++) {
            point = new Point();
            pointArr[i] = point;
        }

        PlotPointsToLocation();
        return pointArr;
    }

    /***
     * To help organize plotting get the general direction first for up or down
     * and then left or right and cardinal directions from there.
     */
    private void PlotPointsToLocation() {
        // move object up wards
        if (a.y > b.y) {
            if (a.x < b.x) {
                plotPointsUpRight();
            } else if (a.x > b.x) {
                plotPointsUpLeft();
            } else {
                plotPointsUp();
            }
            // move radical.appwards.colorcatch.objects downwards
        } else if (a.y < b.y) {
            if (a.x < b.x) {
                plotPointsDownRight();
            } else if (a.x > b.x) {
                plotPointsDownLeft();
            } else {
                plotPointsDown();
            }
            // move left or right
        } else if (a.x > b.x) {
            plotPointsLeft();
        } else {
            plotPointsRight();
        }
    }

    private void plotPointsRight() {
        double horizontalPixels = Math.abs(a.x - b.x);
        pointArr = new Point[(int) horizontalPixels];

        // create new points in array with y values filled
        for (int i = 0; i < horizontalPixels; i++) {
            point = new Point();
            point.x = a.x + i + 1;
            point.y = a.y;
            pointArr[i] = point;
        }

    }

    private void plotPointsLeft() {
        double horizontalPixels = Math.abs(a.x - b.x);
        pointArr = new Point[(int) horizontalPixels];

        // create new points in array with y values filled
        for (int i = 0; i < horizontalPixels; i++) {
            point = new Point();
            point.x = a.x - i - 1;
            point.y = a.y;
            pointArr[i] = point;
        }
    }

    private void plotPointsUp() {
        int tempY = a.y;
        int tempX = a.x;
        // given points to the top create the list of points to traverse
        int pixelsToTop = Math.abs(a.y - b.y);
        for (int i = 0; i < pixelsToTop; i++) {
            point = new Point();
            // every step move up a point
            tempY--;
            point.x = tempX;
            point.y = tempY;
            // add point to list
            pointArr[i] = point;
        }
    }

    private void plotPointsUpRight() {
        // get the number of vertical moves
        double verticalPixels = Math.abs(a.y - b.y);

        // get the number of horizontal moves
        double horizontalPixels = Math.abs(a.x - b.x);

        // case of equal up and right moves, a perfect diagonal line
        if (horizontalPixels == verticalPixels) {

            // cycle through the array and fill the points
            for (int i = 0; i < horizontalPixels; i++) {
                point = new Point();
                pointArr[i] = point;
                pointArr[i].x = a.x + i + 1;
                pointArr[i].y = a.y - i - 1;
            }
        } else if (horizontalPixels >= verticalPixels) {
            // more horizontal moves in the line
            pointArr = new Point[(int) horizontalPixels];

            // create new points in array with x values filled
            // we have a certainty that the line will move along the horizontal
            // axis in its direction
            for (int i = 0; i < horizontalPixels; i++) {
                point = new Point();
                pointArr[i] = point;
                pointArr[i].x = a.x + i + 1;
            }

            // start filling in missing values
            int startValue = a.y;
            double distance = Math.abs(a.y - b.y);

            // every x steps we need to move up or down a step as well
            float distancePerMove = (float) distance / (float) (pointArr.length - 1);

            if (distancePerMove == 1.0) {
                float acc = startValue;
                int i = 0;
                while (i < (pointArr.length)) {
                    if (i != pointArr.length / 2) {
                        acc -= distancePerMove;
                    }
                    pointArr[i].y = Math.round(acc);
                    i++;
                }
            } else {

                // set first and last y this is the starting point and
                // the end point
                pointArr[0].y = a.y;
                pointArr[pointArr.length - 1].y = b.y;

                // since we have a decimal it needs to round to find the best
                // pixel
                // to move
                // this means that it will move down if possible or stay in
                // line if needed.
                float acc = startValue;
                int i = 1;
                while (i < (pointArr.length - 1)) {
                    acc -= distancePerMove;
                    pointArr[i].y = Math.round(acc);
                    i++;
                }
            }
        } else {
            // more vertical moves is similar but we are basing the line off
            // the fact that the vertical pixels move evenly
            pointArr = new Point[(int) verticalPixels];

            // create new points in array with y values filled
            for (int i = 0; i < verticalPixels; i++) {
                point = new Point();
                pointArr[i] = point;
                pointArr[i].y = a.y - i - 1;
            }

            // start filling in missing values
            int startValue = a.x;
            double distance = Math.abs(a.x - b.x);

            // every N steps down we move 1 step right
            // N = distancePerMove
            float distancePerMove = (float) distance / (float) (pointArr.length - 1);

            if (distancePerMove == 1.0) {
                float acc = startValue;
                int i = 0;
                while (i < (pointArr.length)) {
                    if (i != pointArr.length / 2) {
                        acc += distancePerMove;
                    }
                    pointArr[i].x = Math.round(acc);
                    i++;
                }

            } else {
                // set first and last x
                pointArr[0].x = a.x;
                pointArr[pointArr.length - 1].x = b.x;

                float acc = startValue;
                int i = 1;
                while (i < (pointArr.length - 1)) {
                    acc += distancePerMove;
                    pointArr[i].x = Math.round(acc);
                    i++;
                }
            }
        }
    }

    private void plotPointsUpLeft() {
        double verticalPixels = Math.abs(a.y - b.y);
        double horizontalPixels = Math.abs(a.x - b.x);

        if (horizontalPixels == verticalPixels) {
            for (int i = 0; i < horizontalPixels; i++) {
                point = new Point();
                pointArr[i] = point;
                pointArr[i].x = a.x - i - 1;
                pointArr[i].y = a.y - i - 1;
            }
        } else if (horizontalPixels >= verticalPixels) {
            // more horizontal moves
            pointArr = new Point[(int) horizontalPixels];
            // create new points in array with x values filled
            for (int i = 0; i < horizontalPixels; i++) {
                point = new Point();
                pointArr[i] = point;
                pointArr[i].x = a.x - i - 1;
            }

            // start filling in missing values
            int startValue = a.y;
            double distance = Math.abs(a.y - b.y);

            float distancePerMove = (float) distance / (float) (pointArr.length - 1);

            if (distancePerMove == 1.0) {
                float acc = startValue;
                int i = 0;
                while (i < (pointArr.length)) {

                    if (i != pointArr.length / 2) {
                        acc -= distancePerMove;
                    }

                    pointArr[i].y = Math.round(acc);
                    i++;
                }
            } else {
                // set first and last y
                pointArr[0].y = a.y;
                pointArr[pointArr.length - 1].y = b.y;

                float acc = startValue;
                int i = 1;
                while (i < (pointArr.length - 1)) {
                    acc -= distancePerMove;
                    pointArr[i].y = Math.round(acc);
                    i++;
                }
            }
        } else {

            pointArr = new Point[(int) verticalPixels];

            // create new points in array with y values filled
            for (int i = 0; i < verticalPixels; i++) {
                point = new Point();
                pointArr[i] = point;
                pointArr[i].y = a.y - 1 - i;
            }

            // start filling in missing values
            int startValue = a.x;
            double distance = Math.abs(a.x - b.x);

            float distancePerMove = (float) distance / (float) (pointArr.length - 1);

            if (distancePerMove == 1.0) {
                float acc = startValue;
                int i = 0;
                while (i < (pointArr.length)) {
                    if (i != pointArr.length / 2) {
                        acc -= distancePerMove;
                    }
                    pointArr[i].x = Math.round(acc);
                    i++;
                }

            } else {
                // set first and last x
                pointArr[0].x = a.x;
                pointArr[pointArr.length - 1].x = b.x;

                float acc = startValue;
                int i = 1;
                while (i < (pointArr.length - 1)) {
                    acc -= distancePerMove;
                    pointArr[i].x = Math.round(acc);
                    i++;
                }
            }

        }
    }

    private void plotPointsDown() {
        int tempY = a.y;
        int tempX = a.x;
        // given points to the top create the list of points to traverse
        int pixelsToTop = Math.abs(b.y - a.y);
        for (int i = 0; i < pixelsToTop; i++) {
            point = new Point();
            // every step move up a point
            tempY++;
            point.x = tempX;
            point.y = tempY;
            // add point to list
            pointArr[i] = point;
        }
    }

    private void plotPointsDownRight() {

        double verticalPixels = Math.abs(a.y - b.y);
        double horizontalPixels = Math.abs(a.x - b.x);

        if (horizontalPixels == verticalPixels) {
            for (int i = 0; i < horizontalPixels; i++) {
                point = new Point();
                pointArr[i] = point;
                pointArr[i].x = a.x + i + 1;
                pointArr[i].y = a.y + i + 1;
            }
        } else if (horizontalPixels >= verticalPixels) {
            pointArr = new Point[(int) horizontalPixels];

            // create new points in array with y values filled
            for (int i = 0; i < horizontalPixels; i++) {
                point = new Point();
                pointArr[i] = point;
                pointArr[i].x = a.x + i + 1;
            }

            // start filling in missing values
            int startValue = a.y;
            int endValue = b.y;
            int distance = Math.abs(endValue - startValue);
            float distancePerMove = (float) distance / (float) (pointArr.length - 1);

            if (distancePerMove == 1.0) {
                float acc = startValue;
                int i = 0;
                while (i < (pointArr.length)) {

                    if (i != pointArr.length / 2) {
                        acc += distancePerMove;
                    }

                    pointArr[i].y = Math.round(acc);
                    i++;
                }
            } else {
                // set first and last y
                pointArr[0].y = a.y;
                pointArr[pointArr.length - 1].y = b.y;

                float acc = startValue;
                int i = 1;
                while (i < (pointArr.length - 1)) {
                    acc += distancePerMove;
                    pointArr[i].y = Math.round(acc);
                    i++;
                }
            }
        } else {

            pointArr = new Point[(int) verticalPixels];

            // create new points in array with y values filled
            for (int i = 0; i < verticalPixels; i++) {
                point = new Point();
                pointArr[i] = point;
                pointArr[i].y = a.y + i + 1;
            }

            // start filling in missing values
            int startValue = a.x;
            int endValue = b.x;

            int distance = Math.abs(endValue - startValue);

            float distancePerMove = (float) distance / (float) (pointArr.length - 1);
            if (distancePerMove == 1.0) {
                // increment is at the halfway point in the array
                int i = 0;
                float acc = startValue;
                while (i < (pointArr.length)) {
                    if (i != pointArr.length / 2) {
                        acc += distancePerMove;
                    }
                    pointArr[i].x = Math.round(acc);
                    i++;
                }
            } else {
                // set first and last x
                pointArr[0].x = a.x;
                pointArr[pointArr.length - 1].x = b.x;

                float acc = startValue;
                int i = 1;
                while (i < (pointArr.length - 1)) {
                    // if distance per move is 1.0 evenly
                    // we should move this at the halfway point not the first
                    // points
                    acc += distancePerMove;
                    pointArr[i].x = Math.round(acc);
                    i++;
                }
            }

        }
    }

    private void plotPointsDownLeft() {
        double verticalPixels = Math.abs(a.y - b.y);
        double horizontalPixels = Math.abs(a.x - b.x);

        if (horizontalPixels == verticalPixels) {
            for (int i = 0; i < horizontalPixels; i++) {
                point = new Point();
                pointArr[i] = point;
                pointArr[i].x = a.x - i - 1;
                pointArr[i].y = a.y + i + 1;
            }
        } else if (horizontalPixels >= verticalPixels) {
            pointArr = new Point[(int) horizontalPixels];

            // create new points in array with y values filled
            for (int i = 0; i < horizontalPixels; i++) {
                point = new Point();
                pointArr[i] = point;
                pointArr[i].x = a.x - i - 1;
            }

            // start filling in missing values
            int startValue = a.y;
            int endValue = b.y;
            int distance = Math.abs(endValue - startValue);
            float distancePerMove = (float) distance / (float) (pointArr.length - 1);

            if (distancePerMove == 1.0) {
                float acc = startValue;
                int i = 0;
                while (i < (pointArr.length)) {
                    if (i != pointArr.length / 2) {
                        acc += distancePerMove;
                    }
                    pointArr[i].y = Math.round(acc);
                    i++;
                }
            } else {
                // set first and last y
                pointArr[0].y = a.y;
                pointArr[pointArr.length - 1].y = b.y;

                float acc = startValue;
                int i = 1;
                while (i < (pointArr.length - 1)) {
                    acc += distancePerMove;
                    pointArr[i].y = Math.round(acc);
                    i++;
                }
            }

        } else {

            pointArr = new Point[(int) verticalPixels];

            // create new points in array with y values filled
            for (int i = 0; i < verticalPixels; i++) {
                point = new Point();
                pointArr[i] = point;
                pointArr[i].y = a.y + i + 1;
            }

            // start filling in missing values
            int startValue = a.x;
            int endValue = b.x;
            int distance = Math.abs(endValue - startValue);
            float distancePerMove = (float) distance / (float) (pointArr.length - 1);

            if (distancePerMove == 1.0) {
                float acc = startValue;
                int i = 0;
                while (i < (pointArr.length)) {
                    if (i != pointArr.length / 2) {
                        acc -= distancePerMove;
                    }
                    pointArr[i].x = Math.round(acc);
                    i++;
                }
            } else {
                // set first and last x
                pointArr[0].x = a.x;
                pointArr[pointArr.length - 1].x = b.x;

                float acc = startValue;
                int i = 1;
                while (i < (pointArr.length - 1)) {
                    acc -= distancePerMove;
                    pointArr[i].x = Math.round(acc);
                    i++;
                }
            }
        }
    }
}
