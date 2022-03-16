let lastX, lastY, turn;
let birdX, birdY;
let birds;

function setup() {
  createCanvas(windowWidth, windowHeight);
  // put setup code here
  birdX = random(windowWidth);
  birdY = random(windowHeight);
  initBirds(10);
}

function initBirds(n) {
  birds = [];
  for (let i = 0; i < n; i) {
    birds.push({
      x: random(windowWidth),
      y: random(windowHeight),
      s: random(0.5, 1.5),
      c: color(random(0, 255), random(0, 255), random(0, 255))
    });
  }
}


function draw() {
  background(255);
  // put drawing code here

  // fill("#E76F51");
  // background("white");

  // translate(100, 0);
  // rotate(9, 0);


  // rect(-50, -50, 100, 100);

  // translate(windowWidth / 2, windowHeight / 2);
  // for (let i = 0; i < mouseX; i++) {

  //   drawBird(i, i, color(random(0, 255), random(0, 255), random(0, 255)));
  //   // push();
  //   // drawShape(10, 10, 1 + i * random(0.05), color(random(0, 255), random(0, 255), random(0, 255)));
  //   // drawShape(10, 10, 1 + i * (0.025), color((frameCount) % 256, (frameCount + 100) % 256, (frameCount + 200) % 256));
  //   // drawShape(i + 100, i + 100, 1, "green");


  //   rotate(i * 2);
  //   // pop();

  //   // push();
  //   // drawShape(10 + i, 10 + i, i, "red");
  //   // rotate(-i * 0.3);
  //   // pop();
  // }

  // for (let i = 0; i < mouseX; i++) {
  //   // drawShape(10, 10, 1 + i * random(0.05), color(random(0, 255), random(0, 255), random(0, 255)));
  //   drawShape(10, 10, 1 + i * (0.035), color((frameCount) % 256, (frameCount + 60) % 256, (frameCount + 230) % 256));

  //   rotate(-i);
  // }

  // noLoop();

  const birdSpeed = 2;

  // if (lastX > mouseX) {
  //   turn = true;
  //   birdX -= 1;
  // } else if (lastX < mouseX) {
  //   turn = false;
  // }
  
    
  if (lastY > mouseY) {
    birdY -= birdSpeed;
  } else if (lastY < mouseY) {
    birdY += birdSpeed;
  }

  if (lastX > mouseX + 5) {
    turn = true;
    birdX -= birdSpeed;
  } else if (lastX < mouseX - 5) {
    birdX += birdSpeed;
    turn = false;
  }

  // for (let i = 0; i < 10; i++) {

  //   drawBird(i, i, color(random(0, 255), random(0, 255), random(0, 255)));
  //   // push();
  //   // drawShape(10, 10, 1 + i * random(0.05), color(random(0, 255), random(0, 255), random(0, 255)));
  //   // drawShape(10, 10, 1 + i * (0.025), color((frameCount) % 256, (frameCount + 100) % 256, (frameCount + 200) % 256));
  //   // drawShape(i + 100, i + 100, 1, "green");


  //   rotate(i * 2);
    // pop();

    // push();
    // drawShape(10 + i, 10 + i, i, "red");
    // rotate(-i * 0.3);
    // pop();
  // }

  // drawBird(birdX, birdY, "red");
  for (const index in birds) {
    // if (lastY > mouseY) {
    //   birds[index].y -= birds[index].s;
    // } else if (lastY < mouseY) {
    //   birds[index].y += birds[index].s;
    // }
  
    // if (lastX > mouseX + 5) {
    //   turn = true;
    //   birds[index].x -= birds[index].s;
    // } else if (lastX < mouseX - 5) {
    //   birds[index].x += birds[index].s;
    //   turn = false;
    // }
  }
  // lastX = mouseX > lastX + 1 ? mouseX - 10 : mouseX < lastX - 1 ? mouseX + 10 : mouseX;
  // lastY = mouseY > lastY + 1 ? mouseY - 10 : mouseY < lastY - 1 ? mouseY + 10 : mouseY;
  lastX = mouseX > lastX + 1 ? mouseX - 10 : mouseX + 10;
  lastY = mouseY > lastY + 1 ? mouseY - 10 : mouseY + 10;
}

function drawShape(x, y, scaler, c) {
  if (!scaler) {
    scaler = 1;
  }

  if (!c) {
    c = color(255);
  }
  

  const l = 200, h = 50;
  push();

  scale(scaler);
  fill(c);
  // noStroke();
  rect(x, y, l, h);
  beginShape();
  vertex(x + l * 0.25, y);
  vertex(x + l * 0.25 + h, y - h);
  vertex(x + l * 0.8, y - h);
  vertex(x + l, y);
  endShape(CLOSE);

  ellipse(x + 0.2 * l, y + h, 50);
  ellipse(x + 0.8 * l, y + h, 50);

  pop();
}


function drawBird(x, y, c) {
  if (!c) {
    c = 255;
  }

  const l = 50, s = 25;
  const dir = turn ? -1 : 1;
  push();
  fill(c);
  noStroke();

  // tail:
  triangle(x - dir * 0.4 * l, y, x - dir * l * 0.7, y + s * 0.4, x - dir * l * 0.7, y - s * 0.4);
  // body:
  ellipse(x, y, l, s);
  // beak:
  triangle(x + dir * l * 0.5 + dir * s * 1.4, y, x + dir * l * 0.5 + dir * s * 0.4, y - s * 0.1, x + dir * l * 0.5 + dir * s * 0.4, y + s * 0.1);
  // head:
  ellipse(x + dir * l * 0.5 + dir * s * 0.4, y, s);
  // ellipse(x + l * 0.5 + s * 0.4, y - s * 0.1, s);
  // wings:
  triangle(x - l * 0.3, y, x, y + (-1) ** parseInt(frameCount / 5) * s * 1.5, x + s * 0.5, y);
  // eyes:
  fill(0);
  ellipse(x + dir * l * 0.5 + dir * s * 0.4, y, 2);
  // ellipse(x + l * 0.5 + s * 0.4, y - s * 0.1, 2);


  pop();
}