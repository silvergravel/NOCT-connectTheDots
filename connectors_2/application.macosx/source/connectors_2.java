import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class connectors_2 extends PApplet {

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

public void setup() {
  
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

public void draw() {
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

public void drawLine(PVector startPoint, PVector endPoint, int i_) {


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


public void retreatLine(PVector startPoint, int i_) {
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
class Particle {

  PVector location;
  PVector velocity;
  PVector acceleration;
  int mass; //size in practical terms
  float damping;
  float xWind;
  float yWind;
  float t;
  float G;
  int c;

  Particle(PVector location_, float t_) {

    location = location_;
    velocity = new PVector(0, 0);
    acceleration = new PVector(0, 0);
    mass = 3;
    damping = 0.99f;
    t = t_;
    G = 0.005f;
    c = color(140);
  }

  public PVector setWind() {

    xWind = noise((t + frameCount)*0.001f);
    xWind = map(xWind, 0, 1, -0.01f, 0.01f);
    yWind = noise((t + 10000 + frameCount)*0.001f);
    yWind = map(yWind, 0, 1, -0.01f, 0.01f);
    PVector wind = new PVector(xWind, yWind);

    return wind;
  }

  public PVector attract(Particle p) {

    PVector direction = PVector.sub(location, p.location);
    float distance = direction.mag();
    distance = constrain(distance, 5, 25);
    direction.normalize();
    float gravity = (G * mass * p.mass)/ sq(distance);
    PVector gForce = PVector.mult(direction, gravity);
    return gForce;
  }

  public void applyForce(PVector force) {
    PVector dA = PVector.div(force, mass);
    acceleration.add(dA);
  }

  public void update() {
    velocity.add(acceleration);
    location.add(velocity);
    velocity.mult(damping);
    acceleration.mult(0);
  }

  public void display() {

    fill(c);
    ellipse(location.x, location.y, mass*2, mass*2);
  }

  public void checkEdges() {

    if (location.x > width) {
      location.x = 0;
    } else if (location.x <0) {
      location.x = width;
    }
    
    if (location.y > height) {
      location.y = 0;
    } else if (location.y <0) {
      location.y = height;
    }
  }
  
  
}
  public void settings() {  size(800, 800, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "connectors_2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
