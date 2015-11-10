package com.ts.quad;

import java.util.ArrayList;
import java.util.HashMap;

import com.ts.trajectory.Trajectory;
import com.ts.trajectory.TrajectorySamplePoint;

/**
 * 
 * @author Qinger
 *
 */
public class QuadInternalNode extends QuadNode {
	
	/**
	 * The count of trajectory stored in the quad-node
	 */
	private int trjCountInNode;
	
	/**
	 * Flag of whether this node is leaf node
	 */
	private boolean isLeafNode; 
	
	/**
	 * Trajectories in this quad-node
	 */
	//private ArrayList<Trajectory> trjInNode;
	
	private Rectangle mbr;
	
	private int nodeID;
	
	private QuadInternalNode fatherNode = null;
	
	private int treeDepth = 0;
	
	private HashMap<Integer, ArrayList<Pair<Integer, Integer>>> intersectingSegments = new HashMap<Integer, ArrayList<Pair<Integer, Integer>>>();
	
	private static HashMap<Integer, Trajectory> trjInQuadTree = new HashMap<Integer, Trajectory>();
	
	public QuadInternalNode(ArrayList<Double> coordinates, int trjCount) {
		this(new Rectangle(coordinates), trjCount);
		this.coordinates.addAll(coordinates);		
	}
	
	public QuadInternalNode(ArrayList<Double> coordinates) {
		this(coordinates, 0);
	}
	
	public QuadInternalNode(Rectangle rectangle, int trjCount) {
		this.mbr = new Rectangle(rectangle.getLeftBottom(), rectangle.getRightUpper(), true);
		this.trjCountInNode = trjCount;
		this.isLeafNode = true;
		//this.trjInNode = new ArrayList<Trajectory>();
		this.nodeID = IdCreator.getIdCreator().getNextId();
	}
	
	public QuadInternalNode(Rectangle rectangle) {
		this(rectangle, 0);
	}
	
	public void setIsLeafNode(boolean isLeafNode) {
		this.isLeafNode = isLeafNode;
	}
	
	public void setTreeDepth(int val) {
		this.treeDepth = val;
	}
	
	/**
	 * Judge whether the mbr of current node intersects the trajectory
	 */
	@Override
	public Boolean isIntersect(Trajectory traj) {
		ArrayList<TrajectorySamplePoint> samplePoints = traj.getPointList();
		
		int containCount = 1;
		//int segPointBeginIndex = 1;
		if (samplePoints.isEmpty())
			throw new IllegalArgumentException("The trajectory is empty!");
		
		if (samplePoints.size() == containCount)
			return this.mbr.containPoint(samplePoints.get(0).getPoint());
	
		int pointCount = samplePoints.size();
		
		ArrayList<Pair<Integer, Integer>> intersectSegments = null;
		if (this.fatherNode != null) {
			intersectSegments = this.fatherNode.getIntersectSegmentById(traj);
		} else {
			intersectSegments = new ArrayList<Pair<Integer, Integer>>();
			intersectSegments.add(new Pair<Integer, Integer>(0, pointCount - 1));
		}
		
		if (intersectSegments == null)
			return false;
		
		for (Pair<Integer, Integer> segment : intersectSegments) {
			int preIndex = segment.first;
			int curIndex = segment.first + 1;
			while(curIndex <= segment.second) {
				if(this.mbr.isIntersectSegment(samplePoints.get(preIndex).getPoint(), samplePoints.get(curIndex).getPoint())) {
					return true;
				}
				preIndex = curIndex;
				curIndex++;
			}
		}
		
		/**
		for (int index = segPointBeginIndex; index < pointCount; index++) {
			if (this.mbr.isIntersectSegment(samplePoints.get(index - 1).getPoint(), samplePoints.get(index).getPoint())) {
				return true;
			}
		}
		**/
		
		return false;
	}

	/**
	 * Insert a trajectory into this Quad-tree. Store it into some certain leaf-node in quad-tree.
	 * If this trajectory doesn't satisfy criteria, just ignore it. And then, 
	 * 1. this node is a leaf node, add this trajectory into this node.
	 * 2. this node is a common internal node, add this trajectory to its list, and insert it into its child node. 
	 * @param traj The trajectory 
	 */

	public void insertTrajectory(Trajectory traj) {
		//if trajectory has been added into this tree, or doesn't intersect the treeNode;
		if (this.treeDepth == ConfigParams.ROOTNODEDEPTH) {
			if (QuadInternalNode.trjInQuadTree.containsKey(traj.getTrajectoryID()) || this.isIntersect(traj) == false) {
				return; 
			} else {
				QuadInternalNode.trjInQuadTree.put(traj.getTrajectoryID(),  traj);
			}
		}
			
		ArrayList<Pair<Integer, Integer>> intersectSegments = null;
		if (this.treeDepth == ConfigParams.ROOTNODEDEPTH) {
			intersectSegments = findIntersectSegments(traj);
		} else
			intersectSegments = findIntersectSegments(traj, this.fatherNode.getIntersectSegmentById(traj));
		
		if(intersectSegments.isEmpty())
			return;
		else {
			this.intersectingSegments.put(traj.getTrajectoryID(), intersectSegments);
			this.trjCountInNode++;
		}
		
		if (this.isLeafNode == false) {
			int childIndex = 1;
			for (QuadNode node : this.children) {
				QuadInternalNode quadNode = (QuadInternalNode)node;
				//System.out.println("Trajectory " + traj.getTrajectoryID() + "Into: " + this.treeDepth + ", child: " + childIndex);
				quadNode.insertTrajectory(traj);
				childIndex++;
			}
		} else {
			if (shouldSplit()) {
				splitInternalNode();
				this.isLeafNode = false;
				allocateTrajectories();
			}
		}
	}
	
	/**
	 * 
	 * @param traj
	 * @return
	 */
	public ArrayList<Trajectory> getCandidate(Trajectory traj) {
		if (traj == null)
			throw new NullPointerException("Argument Trajectory can't be null");
		
		ArrayList<Trajectory> ret = new ArrayList<Trajectory>();
		
		if(this.isIntersect(traj) == false) {
			return ret; 
		} else {
			//this.insertTrajectory(traj);
		}
		
		ArrayList<Pair<Integer, Integer>> intersectSegments = findIntersectSegments(traj);
		//this.intersectingSegments.put(traj.getTrajectoryID(), intersectSegments);
		
		if (this.isLeafNode) {
			for (Integer inTrajId : this.intersectingSegments.keySet()) {
				Trajectory inTraj = QuadInternalNode.trjInQuadTree.get(inTrajId);
				if (isTrajSegmentIntersect(inTraj, traj)) {
					ret.add(inTraj);
				}
			}
		} else {
			for (QuadNode quadNode : this.children) {
				QuadInternalNode quadInternalNode = (QuadInternalNode)quadNode;
				ArrayList<Trajectory> trajs = quadInternalNode.getCandidate(traj);
				ret.addAll(trajs);
			}
		}
		//System.out.println("Node " + Integer.valueOf(nodeID).toString() + " intersect  Candidate Traj");
		/**
		if (this.isLeafNode) {
			for (Trajectory inTraj : this.trjInNode) {
				if (inTraj.intersectTraj(traj)) {
					ret.add(inTraj);
				}
			}
		} else {
			for (QuadNode quadNode : this.children) {
				QuadInternalNode quadInternalNode = (QuadInternalNode)quadNode;
				ArrayList<Trajectory> trajs = quadInternalNode.getCandidate(traj);
				ret.addAll(trajs);
			}
		}
		**/
		return ret;
	}
	
	private boolean isTrajSegmentIntersect(Trajectory intraj, Trajectory traj) {
		return isTrajSegmentIntersect(intraj.getTrajectoryID(), traj.getTrajectoryID());
	}
	
	private boolean isTrajSegmentIntersect(int inTrajId, int trajId) {
		ArrayList<Pair<Integer, Integer>> inTrajSeg = this.intersectingSegments.get(inTrajId);
		ArrayList<Pair<Integer, Integer>> trajSeg = this.intersectingSegments.get(trajId);
		ArrayList<TrajectorySamplePoint> inTrajPointList = QuadInternalNode.trjInQuadTree.get(inTrajId).getPointList();
		ArrayList<TrajectorySamplePoint> trajPointList = QuadInternalNode.trjInQuadTree.get(trajId).getPointList();
		for (Pair<Integer, Integer> inTrajPair : inTrajSeg) {
			for (Pair<Integer, Integer> trajPair : trajSeg) {
				for (int inTrajIndex = inTrajPair.first; inTrajIndex < inTrajPair.second; inTrajIndex++) {
					for (int trajIndex = trajPair.first; trajIndex < trajPair.second; trajIndex++) {
						boolean isIntersect = GeoCalculate.segIntersectSeg(inTrajPointList.get(inTrajIndex).getPoint(), inTrajPointList.get(inTrajIndex + 1).getPoint(), trajPointList.get(trajIndex).getPoint(), trajPointList.get(trajIndex + 1).getPoint());
						if (isIntersect)
							return true;
					} 
				}
			}
		}
		return false;
	}

	private ArrayList<Pair<Integer, Integer>> findIntersectSegments(
			Trajectory traj) {
		ArrayList<Pair<Integer, Integer>> fatherSegments = new ArrayList<Pair<Integer, Integer>>();
		fatherSegments.add(new Pair<Integer, Integer>(0, traj.getPointList().size() - 1));
		ArrayList<Pair<Integer, Integer>> intersectSegments = findIntersectSegments(traj, fatherSegments);
		return intersectSegments;
	}

	private ArrayList<Pair<Integer, Integer>> findIntersectSegments(Trajectory traj, ArrayList<Pair<Integer, Integer>> segments) {
		ArrayList<Pair<Integer, Integer>> intersectSegments = new ArrayList<Pair<Integer, Integer>>();
		
		ArrayList<TrajectorySamplePoint> pointList = traj.getPointList();
		for (Pair<Integer, Integer> seg : segments) {
			int beginIndex = seg.first;
			for (int curIndex = seg.first + 1; curIndex <= seg.second; curIndex++) {
				boolean isIntersect = this.mbr.isIntersectSegment(pointList.get(curIndex - 1).getPoint(), pointList.get(curIndex).getPoint());
				if (!isIntersect) {
					if (curIndex > beginIndex + 1) {
						intersectSegments.add(new Pair<Integer, Integer>(beginIndex, curIndex - 1));
					}
					beginIndex = curIndex;
					continue;
				}
				if (curIndex == seg.second) {
					intersectSegments.add(new Pair<Integer, Integer>(beginIndex, curIndex));
				}
			}
		}
		
		return intersectSegments;
	}
	
	public int getQuadNodeCount() {
		return IdCreator.getIdCreator().getIdCount();
	}
	
	public HashMap<Integer, ArrayList<Pair<Integer, Integer>>> getIntersectSegments() {
		return this.intersectingSegments;
	}
	
	public ArrayList<Pair<Integer, Integer>> getIntersectSegmentById(Trajectory traj) {
		return this.intersectingSegments.get(traj.getTrajectoryID());
	}
	
	/**
	private void addTrajIntersectSegment(Trajectory traj) {
		ArrayList<TrajectorySamplePoint> pointList = traj.getPointList();
		
		ArrayList<Pair<Integer, Integer>> intersectSegment = null;
		if (this.fatherNode == null) {
			intersectSegment = new ArrayList<Pair<Integer, Integer>>();
			intersectSegment.add(new Pair<Integer, Integer>(0, pointList.size() - 1));
		} else {
			intersectSegment = this.fatherNode.getIntersectSegmentById(traj);
		}
		
		for (Pair<Integer, Integer> segment : intersectSegment) {
			int beginIndex = segment.first;
			TrajectorySamplePoint prePoint = pointList.get(0);
			TrajectorySamplePoint curPoint = pointList.get(0);
			
			for (int curIndex = beginIndex; curIndex <= segment.second; curIndex++) {
				curPoint = pointList.get(curIndex);
				boolean isIntersect = this.mbr.isIntersectSegment(prePoint.getPoint(), curPoint.getPoint());
				if (!isIntersect) {
					if (curIndex > beginIndex + 1) {
						this.intersectingSegments.get(traj.getTrajectoryID()).add(new Pair<Integer, Integer>(beginIndex, curIndex - 1));
					}
					beginIndex = curIndex;
				} else if (curIndex == pointList.size() - 1) {
					this.intersectingSegments.get(traj.getTrajectoryID()).add(new Pair<Integer, Integer>(beginIndex, curIndex));
				}
				prePoint = curPoint;
			}
		}
	}
	*/
	
	/**
	 * This node is a leaf node and the trajectory count exceeds the trajectory-count upper
	 * @return
	 */
	private Boolean shouldSplit() {
		return (this.isLeafNode && this.trjCountInNode > QuadNode.getTrjInNodeUpBd());
	} 
	
	/**
	 * Split current quad leaf node. Firstly, choose the center and create the new 
	 * four children leaf node.  
	 */
	private void splitInternalNode() {
		ArrayList<Rectangle> rectangles = this.mbr.getSplitRectangles();
		ArrayList<QuadNode> childrenLeafNodes = new ArrayList<QuadNode>();
		
		for (int index = 0; index < rectangles.size(); index++) {
			QuadInternalNode childNode = new QuadInternalNode(rectangles.get(index));
			childNode.setIsLeafNode(true);
			childNode.setFatherNode(this);
			childNode.setTreeDepth(this.treeDepth + 1);
			childrenLeafNodes.add(childNode);
		}
		
		this.children = childrenLeafNodes;
		
	}
	
	/**
	 * Allocate the trajectories stored in this internal node into four child node.
	 * For each trajectory, intersect it into each child node. 
	 */
	private void allocateTrajectories() {
		for (int trajId : this.intersectingSegments.keySet()) {
			for(QuadNode quadNode : this.children) {
				QuadInternalNode leafNode = (QuadInternalNode)quadNode;
				leafNode.insertTrajectory(QuadInternalNode.trjInQuadTree.get(trajId));
			}
		}
	}

	public Rectangle getMbr() {
		return mbr;
	}

	public void setMbr(Rectangle mbr) {
		this.mbr = mbr;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("QuadInternalNode :" + this.nodeID + ",\n Rectangle:" + this.mbr.toString());
		return sb.toString();
	}
	
	public void setFatherNode(QuadInternalNode quadNode) {
		this.fatherNode = quadNode;
	}
	
	static class IdCreator {
		private IdCreator() {
		}
		
		private static int id = 0;
		
		private static IdCreator idCreator = null;
		
		public static IdCreator getIdCreator() {
			if(idCreator == null)
				idCreator = new IdCreator();
			return idCreator;
		}
		
		public int getNextId() {
			synchronized(this) {
				id++;
			}
			return id;
		}
		
		public int getIdCount() {
			return id;
		}
	}
	
	static class Pair<A, B> {
		public final A first;
		public final B second;
		
		public Pair(A first, B second) {
			this.first = first;
			this.second = second;
		}
	}
}
