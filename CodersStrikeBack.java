import java.util.Scanner;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
class Player {

	static class Vector {
		int a;
		int b;

		public Vector(int a, int b) {
			this.a = a;
			this.b = b;
		}
	}

	static class Checkpoint {
		int x;
		int y;

		public Checkpoint(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int thrust;
		boolean[] boosted = new boolean[] { false, false };
		boolean shielded = false;
		boolean opponentInFront = false;
		int distOpAndMe = 0;
		int distOp1AndMe = 0;
		int distOp2AndMe = 0;
		int distOpAndCp = 0;
		int a, b, c;
		double m, p, q;
		int[] startX = new int[] { 0, 0 };
		int[] startY = new int[] { 0, 0 };
		int[] lap = new int[] { 1, 1 };
		int distNCAndStart;
		int dist2ShipsOfMine;
		boolean canIncrease = true;
		int[] prevX = new int[] { 0, 0 };
		int[] prevY = new int[] { 0, 0 };
		int max_velocity = 650;
		int velocity_abs0, velocity_abs1;
		int[] nextX = new int[] { 0, 0 };
		int[] nextY = new int[] { 0, 0 };
		int[] x = new int[4];
		int[] y = new int[4];
		int[] nextCheckpointId = new int[4];
		int[] nextCheckpointX = new int[4];
		int[] nextCheckpointY = new int[4];
		int[] nextCheckpointDist = new int[4];
		int[] vx = new int[4];
		int[] vy = new int[4];
		int[] nextCheckpointAngle = new int[4];
		int[] directionAngle = new int[4];
		Vector[] velocity = new Vector[2];
		Vector[] desired_velocity = new Vector[2];
		Vector[] steering = new Vector[2];
		int[] velocity_abs = new int[2];
		int opponentX = 0;
		int opponentY = 0;
		int goalOp;
		Vector[] vectorToCP = new Vector[2];
		int[] vectorToCP_abs = new int[2];
		Vector[] vectorTo10 = new Vector[2];
		int[] vectorTo10_abs = new int[2];

		Checkpoint[] checkpoints = new Checkpoint[7];

		int maxlap = in.nextInt();
		int checkpointcount = in.nextInt();

		System.err.println("Maxlap: " + maxlap);
		System.err.println("Checkpointcount: " + checkpointcount);

		for (int i = 0; i < checkpointcount; i++) {
			checkpoints[i] = new Checkpoint(in.nextInt(), in.nextInt());
		}

		// game loop
		while (true) {
			// my 1st pod
			x[0] = in.nextInt();
			y[0] = in.nextInt();
			vx[0] = in.nextInt();
			vy[0] = in.nextInt();
			directionAngle[0] = in.nextInt(); // angle of pod right horizontal is 0
			nextCheckpointId[0] = in.nextInt(); // id of the next check point
			nextCheckpointX[0] = checkpoints[nextCheckpointId[0]].x;
			nextCheckpointY[0] = checkpoints[nextCheckpointId[0]].y;
			nextCheckpointDist[0] = (int) Math.sqrt((nextCheckpointX[0] - x[0]) * (nextCheckpointX[0] - x[0])
					+ (nextCheckpointY[0] - y[0]) * (nextCheckpointY[0] - y[0]));

			// my 2nd pod
			x[1] = in.nextInt();
			y[1] = in.nextInt();
			vx[1] = in.nextInt();
			vy[1] = in.nextInt();
			directionAngle[1] = in.nextInt();
			nextCheckpointId[1] = in.nextInt(); // id of the next check point
			nextCheckpointX[1] = checkpoints[nextCheckpointId[1]].x;
			nextCheckpointY[1] = checkpoints[nextCheckpointId[1]].y;
			nextCheckpointDist[1] = (int) Math.sqrt((nextCheckpointX[1] - x[1]) * (nextCheckpointX[1] - x[1])
					+ (nextCheckpointY[1] - y[1]) * (nextCheckpointY[1] - y[1]));

			// enemy 1st pod
			x[2] = in.nextInt();
			y[2] = in.nextInt();
			vx[2] = in.nextInt();
			vy[2] = in.nextInt();
			directionAngle[2] = in.nextInt();
			nextCheckpointId[2] = in.nextInt(); // id of the next check point
			nextCheckpointX[2] = checkpoints[nextCheckpointId[2]].x;
			nextCheckpointY[2] = checkpoints[nextCheckpointId[2]].y;
			nextCheckpointDist[2] = (int) Math.sqrt((nextCheckpointX[2] - x[2]) * (nextCheckpointX[2] - x[2])
					+ (nextCheckpointY[2] - y[2]) * (nextCheckpointY[2] - y[2]));

			// enemy 2nd pod
			x[3] = in.nextInt();
			y[3] = in.nextInt();
			vx[3] = in.nextInt();
			vy[3] = in.nextInt();
			directionAngle[3] = in.nextInt();
			nextCheckpointId[3] = in.nextInt(); // id of the next check point
			nextCheckpointX[3] = checkpoints[nextCheckpointId[3]].x;
			nextCheckpointY[3] = checkpoints[nextCheckpointId[3]].y;
			nextCheckpointDist[3] = (int) Math.sqrt((nextCheckpointX[3] - x[3]) * (nextCheckpointX[3] - x[3])
					+ (nextCheckpointY[3] - y[3]) * (nextCheckpointY[3] - y[3]));

			for (int i = 0; i < 2; i++) {

				if (prevX[i] == 0)
					prevX[i] = x[i];
				if (prevY[i] == 0)
					prevY[i] = y[i];

				velocity[i] = new Vector(x[i] - prevX[i], y[i] - prevY[i]);

				velocity_abs[i] = (int) Math.round(
						Math.sqrt((x[i] - prevX[i]) * (x[i] - prevX[i]) + (y[i] - prevY[i]) * (y[i] - prevY[i])));

				if (velocity_abs[i] == 0) {
					desired_velocity[i] = new Vector(0, 0);
					steering[i] = new Vector(nextCheckpointX[i] - x[i], nextCheckpointY[i] - y[i]);
				} else {
					desired_velocity[i] = new Vector(
							(int) Math.round((nextCheckpointX[i] - x[i]) / velocity_abs[i]) * max_velocity,
							(int) Math.round((nextCheckpointY[i] - y[i]) / velocity_abs[i]) * max_velocity);

					steering[i] = new Vector(desired_velocity[i].a - velocity[i].a,
							desired_velocity[i].b - velocity[i].b);

				}

				if ((startX[i] == 0) && (startY[i] == 0)) {
					startX[i] = x[i];
					startY[i] = y[i];
				}

				vectorToCP[i] = new Vector(nextCheckpointX[i] - x[i], nextCheckpointY[i] - y[i]);
				vectorToCP_abs[i] = (int) Math
						.round(Math.sqrt(vectorToCP[i].a * vectorToCP[i].a + vectorToCP[i].b * vectorToCP[i].b));

				vectorTo10[i] = new Vector(1, 0);
				vectorTo10_abs[i] = (int) Math.round(Math.sqrt(1 * 1 + 0 * 0));

				if (vectorToCP_abs[i] == 0)
					vectorToCP_abs[i] = 1;

				nextCheckpointAngle[i] = (int) Math.round(
						Math.acos((double) (vectorTo10[i].a * vectorToCP[i].a + vectorTo10[i].b * vectorToCP[i].b)
								/ (vectorTo10_abs[i] * vectorToCP_abs[i])) * 180 / Math.PI);

				nextX[i] = x[i] + steering[i].a;
				nextY[i] = y[i] + steering[i].b;
				System.err.println("===POD number i: " + i);
				System.err.println("X,Y: " + x[i] + " " + y[i]);
				System.err.println("NextCP: " + nextCheckpointX[i] + " " + nextCheckpointY[i]);
				// System.err.println("Desired: " + desired_velocity[i].a + " " +
				// desired_velocity[i].b);
				// System.err.println("Steering: " + steering[i].a + " " + steering[i].b);
				// System.err.println("Next: " + nextX[i] + " " + nextY[i]);

				distNCAndStart = (int) Math
						.round(Math.sqrt((nextCheckpointX[i] - startX[i]) * (nextCheckpointX[i] - startX[i])
								+ (nextCheckpointY[i] - startY[i]) * (nextCheckpointY[i] - startY[i])));

				/*
				 * if (distNCAndStart > 600) canIncrease = true;
				 * 
				 * if ((distNCAndStart <= 600) && (canIncrease)) { lap[i]++; canIncrease =
				 * false; }
				 */
				distOp1AndMe = (int) Math
						.round(Math.sqrt(((x[i] - x[2]) * (x[i] - x[2]) + (y[i] - y[2]) * (y[i] - y[2]))));

				distOp2AndMe = (int) Math
						.round(Math.sqrt(((x[i] - x[3]) * (x[i] - x[3]) + (y[i] - y[3]) * (y[i] - y[3]))));

				if (distOp1AndMe < distOp2AndMe) {
					distOpAndMe = distOp1AndMe;
					opponentX = x[2];
					opponentY = y[2];
				} else {
					distOpAndMe = distOp2AndMe;
					opponentX = x[3];
					opponentY = y[3];
				}

				/*
				 * distOpAndCp = (int) Math .round(Math.sqrt(((nextCheckpointX[i] - opponentX) *
				 * (nextCheckpointX[i] - opponentX) + (nextCheckpointY[i] - opponentY) *
				 * (nextCheckpointY[i] - opponentY))));
				 * 
				 * // triangle things a = distOpAndMe; b = distOpAndCp; c =
				 * nextCheckpointDist[i]; p = a * a / c; q = b * b / c; m = Math.sqrt(p * q);
				 * 
				 * // System.err.println("p,q,m: " + p + " " + q + " " + m);
				 * 
				 */
				thrust = 100;

				System.err.println("NextCPAngle: " + nextCheckpointAngle[i]);
				System.err.println("DirAngle: " + directionAngle[i] % 180);
				System.err.println("Dist: " + nextCheckpointDist[i]);
				// System.err.println("Next CP ID: " + nextCheckpointId[i]);
				// System.err.println("DistOM: " + distOpAndMe);
				System.err.println("Thrust: " + thrust);
				System.err.println("Boost: " + boosted[i]);
				System.err.println("Lap: " + lap[i]);
				System.err.println("Velocity abs: " + velocity_abs[i]);

				/*
				 * if ((Math.abs(nextCheckpointAngle[i]) <= 20) && (distOpAndMe < 1000) &&
				 * (nextCheckpointDist[i] < 3000)) if (((nextCheckpointX[i] > x[i] && opponentX
				 * > x[i]) || (nextCheckpointX[i] < x[i] && opponentX < x[i])) &&
				 * ((nextCheckpointY[i] > y[i] && opponentY > y[i]) || (nextCheckpointY[i] <
				 * y[i] && opponentY < y[i]))) opponentInFront = true;
				 * 
				 */
				/*
				 * if ((distOpAndMe < 2000) && (nextCheckpointDist[i] < 3000)) if
				 * (((nextCheckpointX[i] > x[i] && opponentX > x[i]) || (nextCheckpointX[i] <
				 * x[i] && opponentX < x[i])) && ((nextCheckpointY[i] > y[i] && opponentY >
				 * y[i]) || (nextCheckpointY[i] < y[i] && opponentY < y[i])) && m < 2000)
				 * opponentInFront = true;
				 */

				/*
				 * // dodge the opponent if ((boosted[i] == false) && (opponentInFront)) {
				 * System.out.println(nextCheckpointX[i] + " " + nextCheckpointY[i] + " BOOST");
				 * boosted[i] = true; } else if ((boosted[i] == false) && (lap[i] == maxlap - 1)
				 * && (Math.abs(nextCheckpointAngle[i]) <= 10) && (nextCheckpointDist[i] >
				 * 5000)) { System.out.println(nextX[i] + " " + nextY[i] + " BOOST"); boosted[i]
				 * = true; } else System.out.println(nextX[i] + " " + nextY[i] + " " + thrust);
				 * 
				 */

				// dodge the opponent
				// if ((boosted[i] == false) && (opponentInFront)) {
				// System.out.println(nextCheckpointX[i] + " " + nextCheckpointY[i] + " BOOST");
				// boosted[i] = true;
				// } else

				// if ((boosted[i] == false) && (lap[i] == maxlap - 1) && (nextCheckpointDist[i]
				// > 5000)) {

				// the 2 ships of mine are close so not to boost them at once

				dist2ShipsOfMine = (int) Math
						.round(Math.sqrt(((x[1] - x[0]) * (x[1] - x[0]) + (y[1] - y[0]) * (y[1] - y[0]))));

				System.err.println("DistMyShips: " + dist2ShipsOfMine);

				if ((i == 0) && (boosted[0] == false)) {
					System.out.println(nextX[i] + " " + nextY[i] + " BOOST");
					boosted[0] = true;
				} else if ((boosted[i] == false) && (nextCheckpointDist[i] > 6000) && (dist2ShipsOfMine > 3000)
						&& Math.abs(nextCheckpointAngle[i] - directionAngle[i] % 180) < 5) {
					// if (boosted[i] == false) {
					System.out.println(nextX[i] + " " + nextY[i] + " BOOST");
					boosted[i] = true;
				} else {
					// Shield
					// if (opponentInFront)
					// System.out.println(nextX[i] + " " + nextY[i] + " SHIELD");
					// else

					// slave hunt
					/*
					 * if (i == 1) { if (nextCheckpointId[2] > nextCheckpointId[3]) { goalOp = 2; }
					 * else goalOp = 3;
					 * 
					 * nextX[i] = x[goalOp]; nextY[i] = y[goalOp]; System.err.println("GOAL:" +
					 * goalOp + " " + nextX[i] + " " + nextY[i]); }
					 */

					// if (nextCheckpointDist[i] < 700) thrust=10;
					System.out.println(nextX[i] + " " + nextY[i] + " " + thrust);
				}
				prevX[i] = x[i];
				prevY[i] = y[i];
			}
		}
	}
}