ArrayList<Particle> particles;

PVector location;
float t;
int noOfParticles = 80;

//vars to find nearest neighbour

float neighbourDist;
float closestDist;
float pClosest = 99999999;
int[] closestNeighbourAry = new int[noOfParticles];

//vars for connecting line animation

PVector maxLength;
ArrayList<PVector> lines;
PVector incr;

void setup() {
  size(800, 800, P3D);
  particles = new ArrayList<Particle>();
  lines = new ArrayList<PVector>();

  for (int i = 0; i < noOfParticles; i++) {
    location = new PVector(random(width), random(height));
    float t = random(0, 10000);

    particles.add(new Particle(location, t));

    lines.add(new PVector(0, 0));

    closestNeighbourAry[i] = -1;
  }
}

void draw() {
  background(255);

  for (int i = 0; i < particles.size(); i++) {
    Particle p = particles.get(i);
    for (int j = 0; j < particles.size(); j++) {
      Particle pNear = particles.get(j);
      if (i != j) {
        neighbourDist = dist(p.location.x, p.location.y, pNear.location.x, pNear.location.y);
        if (neighbourDist < pClosest) {
          closestDist = neighbourDist;
          closestNeighbourAry[i] = j;
        }
        pClosest = closestDist;
        //PVector gForce = q.attract(p);
        //p.applyForce(gForce);
      }
    }
    pClosest = 99999999;

    Particle closestNeighbour = particles.get(closestNeighbourAry[i]);
    float mouseOvrCheck = dist(p.location.x, p.location.y, mouseX, mouseY);

    if (mouseOvrCheck <= p.mass + 6) {

      stroke(140);
      drawLine(p.location, closestNeighbour.location, i);
      noStroke();
    } else {
      stroke(140);
      retreatLine(p.location, i);
      noStroke();
    }

    PVector wind = p.setWind();
    p.applyForce(wind);
    p.update();
    p.display();
    p.checkEdges();
  }
}

void drawLine(PVector startPoint, PVector endPoint, int i_) {


  PVector line = lines.get(i_);
  maxLength = PVector.sub(endPoint, startPoint);
  incr = PVector.sub(maxLength, line);
  incr.div(8);
  pushMatrix();
  translate(startPoint.x, startPoint.y);
  line(0, 0, line.x, line.y);
  popMatrix();
  if (line.mag() < maxLength.mag()) {

    line.add(incr);
  }
}


void retreatLine(PVector startPoint, int i_) {
  PVector noLine = new PVector(0, 0);
  PVector line = lines.get(i_);
  incr = PVector.sub(noLine, line);
  incr.div(8);
  pushMatrix();
  translate(startPoint.x, startPoint.y);
  line(0, 0, line.x, line.y);
  popMatrix();
  if (line.mag() > noLine.mag()) {

    line.add(incr);
  }

  //line.x = 0;
  //line.y = 0;
}