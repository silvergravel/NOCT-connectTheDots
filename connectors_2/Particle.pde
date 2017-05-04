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
  color c;
  String index = "";

  Particle(PVector location_, float t_, String index_) {

    index = index_;
    location = location_;
    velocity = new PVector(0, 0);
    acceleration = new PVector(0, 0);
    mass = 3;
    damping = 0.99;
    t = t_;
    G = 0.005;
    c = color(140);
  }

  PVector setWind() {

    xWind = noise((t + frameCount)*0.001);
    xWind = map(xWind, 0, 1, -0.01, 0.01);
    yWind = noise((t + 10000 + frameCount)*0.001);
    yWind = map(yWind, 0, 1, -0.01, 0.01);
    PVector wind = new PVector(xWind, yWind);

    return wind;
  }

  PVector attract(Particle p) {

    PVector direction = PVector.sub(location, p.location);
    float distance = direction.mag();
    distance = constrain(distance, 5, 25);
    direction.normalize();
    float gravity = (G * mass * p.mass)/ sq(distance);
    PVector gForce = PVector.mult(direction, gravity);
    return gForce;
  }

  void applyForce(PVector force) {
    PVector dA = PVector.div(force, mass);
    acceleration.add(dA);
  }

  void update() {
    velocity.add(acceleration);
    location.add(velocity);
    velocity.mult(damping);
    acceleration.mult(0);
  }

  void display() {

    fill(c);
    ellipse(location.x, location.y, mass*2, mass*2);
    text(index,location.x+3,location.y+3);
  }

  void checkEdges() {

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