/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P2.turtle;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
    	//throw new RuntimeException("implement me!");
        double degree=90.0;
        for(int i=0;i<4;i++) {
        	turtle.forward(sideLength);
            turtle.turn(degree);
        }
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        if(sides<=2) {
        	throw new RuntimeException("implement me!");
        }
        else {
        	int anglesum=(sides-2)*180;
        	double angle=(double)anglesum/sides;
        	return angle;
        }
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        if(angle<=0||angle>=180) {
        	throw new RuntimeException("implement me!");
        }
        else {
        	int exteriorangle=(int)(180-angle);
        	int side=360/exteriorangle;
        	return side;
        }
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        if(sides<=2) {
        	throw new RuntimeException("implement me!");
        }
        else {
        	double angle=180-calculateRegularPolygonAngle(sides);
        	for(int i=0;i<sides;i++) {
        		turtle.forward(sideLength);
            	turtle.turn(angle);
        	}
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the Bearing
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentBearing. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentBearing current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to Bearing (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY,
                                                 int targetX, int targetY) {
        //throw new RuntimeException("implement me!");
    	double targetpoint=0; //根据现坐标与目标位置的相对位置划分为9个维度，分别讨论
    	if((currentX==targetX)&&(currentY==targetY)) {//重合
    		targetpoint=0;
    	}
    	else if ((targetX<currentX)&&(targetY>currentY)) {//目标位于左上方
			double difX=currentX-targetX;
			double difY=targetY-currentY;
			double tan=difX/difY;
			targetpoint=Math.atan(tan)*180/Math.PI;//利用tan值求出夹角
			targetpoint=360-targetpoint-currentBearing;//求出需旋转的角度，此处的角度有可能为负值
		}
    	else if((targetX<currentX)&&(targetY==currentY)) {//目标在正左方
    		targetpoint=270-currentBearing;
    	}
    	else if((targetX<currentX)&&(targetY<currentY)) {//目标在左下方
    		double difX=currentX-targetX;
    		double difY=currentY-targetY;
    		double tan=difX/difY;
			targetpoint=Math.atan(tan)*180/Math.PI;//利用tan值求出夹角
			targetpoint=targetpoint+180-currentBearing;//求出需旋转的角度，此处的角度有可能为负值
    	}
    	else if((currentX==targetX)&&(targetY<currentY)) {//目标在正下方
    		targetpoint=180-currentBearing;
    	}
    	else if((targetX>currentX)&&(targetY<currentY)) {//目标在右下方
    		double difX=targetX-currentX;
    		double difY=currentY-targetY;
    		double tan=difX/difY;
			targetpoint=Math.atan(tan)*180/Math.PI;//利用tan值求出夹角
			targetpoint=180-targetpoint-currentBearing;//求出需旋转的角度，此处的角度有可能为负值
    	}
    	else if((targetX>currentX)&&(targetY==currentY)) {//目标在正右方
    		targetpoint=90-currentBearing;
    	}
    	else if((targetX>currentX)&&(targetY>currentY)) {//目标在右上方
    		double difX=targetX-currentX;
    		double difY=targetY-currentY;
    		double tan=difX/difY;
			targetpoint=Math.atan(tan)*180/Math.PI;//利用tan值求出夹角
			targetpoint=targetpoint-currentBearing;//求出需旋转的角度，此处的角度有可能为负值
    	}
    	else {	//目标在正上方
    		targetpoint=0-currentBearing;
    	}
    	if(targetpoint<0) {//通过加上360度使负角度调整为满足条件的区间
    		targetpoint+=360;
    	}
    	return targetpoint;
    }

    /**
     * Given a sequence of points, calculate the Bearing adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateBearingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of Bearing adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords) {
    	int xnum=xCoords.size();
    	int ynum=yCoords.size();
    	if(xnum!=ynum) {//若参数中x,y向量列表元素个数不等，则为异常情况
    		throw new RuntimeException("implement me!");
    	}
    	else {//参数正常
    		List<Double> BearingsList=new ArrayList<>();//建立列表，保存每一次的point
    		double bearingpoint=0;//初始bearingpoint角度是0，可由网页中的例子得知
    		int currentX,currentY,targetX,targetY,i;
    		for(i=0;i<xnum-1;i++) {//用n-1次循环遍历，cunrrentX,Y可由xCoords和yCoords列表第i项得到
    			currentX=xCoords.get(i);
    			currentY=yCoords.get(i);
    			targetX=xCoords.get(i+1);//targetX,Y为向量列表中的后一项
    			targetY=yCoords.get(i+1);
    			bearingpoint=calculateBearingToPoint(bearingpoint, currentX, currentY, targetX, targetY);
    			BearingsList.add(bearingpoint);//加入到返回列表中
    		}
    		return BearingsList;
    	}
    }
    
    /**
     * Given a set of points, compute the convex hull, the smallest convex set that contains all the points 
     * in a set of input points. The gift-wrapping algorithm is one simple approach to this problem, and 
     * there are other algorithms too.
     * 
     * @param points a set of points with xCoords and yCoords. It might be empty, contain only 1 point, two points or more.
     * @return minimal subset of the input points that form the vertices of the perimeter of the convex hull
     */
    public static Set<Point> convexHull(Set<Point> points) {
        //throw new RuntimeException("implement me!");
    	if(points.size()<=3) {
    		return points;
    	}
    	else {
    		Set<Point> convexset=new HashSet<Point>();
    		List<Point> convexlist=new ArrayList<Point>();//因为List元素按加入顺序有序，所以先将点存储在List中。
    		Point convexPoint=new Point(0,0);
    		double distance=0;
    		for(Point p:points) {//先根据与原点间的距离找到一个距离最大点，其一定为凸包上点
    			if(p.x()*p.x()+p.y()*p.y()>distance) {
    				distance=p.x()*p.x()+p.y()*p.y();
    				convexPoint=p;
    			}
    		}
    		convexlist.add(convexPoint);
    		double bearingpoint=0;//初始方向为Y轴正向，角度置0
    		double pointbetween1=360;//设置一个比较的初始值，大于所有的转向角度
    		double pointbetween2=0;//保存从凸包上一点到目前凸包集合外一点所需转向角
    		for(int i=0;i<points.size()-1;i++) {//再从该点开始，利用写好的calculateBearingToPoint函数找到顺时针最小转向角度的点，则其余点全在这两点连线的一侧，故该点也为凸包上点
    			int currentX=(int)convexlist.get(convexlist.size()-1).x();//最新加入凸包集合的点
				int currentY=(int)convexlist.get(convexlist.size()-1).y();
				pointbetween1=360;
    			for(Point p:points) {//在点集中逐个寻找所需转向角最小的点
    				pointbetween2=calculateBearingToPoint(bearingpoint, currentX, currentY, (int)p.x(), (int)p.y());//两点间转向角
    				if((pointbetween2<pointbetween1)&&(p!=convexlist.get(convexlist.size()-1))) {//小的保存
    					pointbetween1=pointbetween2;
    					convexPoint=p;
    				}
    			}
    			if(convexPoint==convexlist.get(0)) {//回到起点时凸包已形成，直接退出
    				break;
    			}
    			else {
    				if((pointbetween1==0)&&(bearingpoint==0)) {//已知凸包上一点，它所在直线上还有两点都被认定为转向角最小的点，则删除中间的点，保证凸包的最小性
    					convexlist.remove(convexlist.size()-1);//即凸包上三点共线，则删除中间
    				}
    				bearingpoint=(bearingpoint+pointbetween1)%360;
    				convexlist.add(convexPoint);
    			}
    		}
    		for(int i=0;i<convexlist.size();i++) {//将存储在List中的凸包转到集合中
    			convexset.add(convexlist.get(i));
    		}
    		return convexset;
    	}
    }

    
    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
        //throw new RuntimeException("implement me!");
    	int i=0;
    	int k;
    	for(k=0;k<2;k++) {
    		i=0;
    		while(i<90) {
        		turtle.color(PenColor.RED);
        		turtle.forward(5);
        		turtle.turn(4);
        		i++;
        	}
    		turtle.turn(180);
    	}
    	
    	turtle.turn(90);
    	
    	for(k=0;k<2;k++) {
    		i=0;
    		while(i<90) {
        		turtle.color(PenColor.GREEN);
        		turtle.forward(5);
        		turtle.turn(4);
        		i++;
        	}
    		turtle.turn(180);
    	}
    	
    	turtle.turn(45);
    	for(k=0;k<2;k++) {
    		i=0;
    		while(i<90) {
        		turtle.color(PenColor.CYAN);
        		turtle.forward(5);
        		turtle.turn(4);
        		i++;
        	}
    		turtle.turn(90);
    	}
    	
    	for(k=0;k<2;k++) {
    		i=0;
    		while(i<90) {
        		turtle.color(PenColor.YELLOW);
        		turtle.forward(5);
        		turtle.turn(4);
        		i++;
        	}
    		turtle.turn(90);
    	}
    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();

        //drawSquare(turtle, 40);
        //drawRegularPolygon(turtle, 8, 40);

         //draw the window 
        drawPersonalArt(turtle);
        turtle.draw();
    }

}
