var w1, w2, w3, p1, p2, p3, p4, p5, p6, r;
let flag = true;

function setup() {
  createCanvas(windowWidth, windowHeight);
  // put setup code here

  w1 = random(1, 20);
  w2 = random(1, 20);
  w3 = random(1, 20);

  p1 = {x: random(1, windowWidth/2), y:  random(10, 50)};
  p2 = {x: random(windowWidth/2, windowWidth), y:  random(10, windowHeight - 10)};
  p3 = {x: random(1, windowWidth/2), y:  random(50, 100)};
  p4 = {x: random(windowWidth/2, windowWidth), y:  random(10, windowHeight - 10)};
  p5 = {x: random(1, windowWidth/2), y:  random(100, 150)};
  p6 = {x: random(windowWidth/2, windowWidth), y:  random(10, windowHeight - 10)};

  r = random(windowWidth /20, windowWidth / 5);

}

function draw() {
  // put drawing code here
  // noLoop();

  background("blue");


  stroke("white");
  strokeWeight(w1);
  line(p1.x, p1.y, p2.x, p2.y);
  strokeWeight(w2);
  line(p3.x, p3.y, p4.x, p4.y);
  strokeWeight(w3);
  line(p5.x, p5.y, p6.x, p6.y);


  if (frameCount % 100 === 0) {
    flag = !flag;
  }

  fill(`rgba(255, 255, 255, ${flag ? frameCount % 100 / 100 : 1 - (frameCount % 100 / 100)})`);
  noStroke();
  ellipse(windowWidth / 2 - r, 200 + r, r);

}
