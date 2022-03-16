var sally; // declare a variable called sally

function setup() {
  createCanvas(windowWidth, windowHeight);

  sally = {
    x: windowWidth / 2,
    y: windowHeight / 2,
    isCaptured: false,
    captureSound: loadSound("assets/pokeball-catch.mp3")
  }

  // initialise the sally variable here
  // ...
}

function draw() {
  background(60, 128, 25);


  // what function(s) do you need to call in here, and with what parameters?
  // ...

  drawPokemon(sally);

  updatePokemon(sally);
}

function drawPokemon(aPokemon) {
  noStroke();
  fill(0);

  if (aPokemon.isCaptured) {
    drawBall(aPokemon);
  } else {
    ellipse(aPokemon.x, aPokemon.y, 50, 50);
  }
}

function drawBall(aPokemon) {
  const {x, y} = aPokemon;

  push();
  noStroke();
  fill(255);
  ellipse(x, y, 50, 50);
  fill("red");
  arc(x, y, 50, 50, PI, 0);
  stroke(0);
  fill(255);
  ellipse(x, y, 10, 10);
  pop();
}

function updatePokemon(aPokemon) {
  // this is just a bit of controlled randomness to make the pokemon move around the canvas

  if (!aPokemon.isCaptured) {
    aPokemon.x += 0.05*width*cos(frameCount*0.01)*randomGaussian(0, 0.1);
    aPokemon.y += 0.05*height*cos(frameCount*0.011)*randomGaussian(0, 0.1);
  }
}


function mousePressed() {
  print("!!")
  if (mouseX < sally.x + 25 && mouseX > sally.x - 25 && mouseY < sally.y + 25 && mouseY > sally.y - 25) {
    sally.isCaptured = true;
    sally.captureSound.play();
  }
}



// ===========================================================================

// // an array for our brushes
// var brushes = [];

// function setup() {
//     createCanvas(windowWidth, windowHeight);

//     // create colour palette for the painting
//     var palette = [
//         color(237,176,5), 
//         color(187,16,6),
//         color(26,82,164),
//         color(236,126,3)
//     ];

//     // push 100 objects to the brushes array
//     for(var i=1; i<=102; i++) {
//         brushes.push({
//             color: 
//                 // pick two colours from the palette
//                 // and mix them together randomly
//                 lerpColor(
//                         palette[floor(random(palette.length))],
//                         palette[floor(random(palette.length))],
//                         random(1)),
//             // brush size
//             size: randomGaussian(6,3),
//             // position
//             pos: createVector(random(width),random(height)),
//             // velocity
//             vel: createVector(0,0)
//         });
//     }

//     noStroke();

//     // set mouse position to centre
//     mouseX = width/2;
//     mouseY = height/2;

//     background(0);
// }

// function draw() {
//     // draw all the brushes
//     brushes.map(drawBrush);

//     // move all the brushes around
//     brushes.map(moveBrush);
// }

// function drawBrush(b, i) {
//     fill(b.color);
//     const {pos} = b;
//     let k = random(1);
//     if (k < 0.25) {
//       rect(pos.x - b.size / 2, pos.y - b.size / 2, b.size);
//     } else if (k < 0.5) {
//       ellipse(b.pos.x, b.pos.y, b.size);
//     } else if (k < 0.75) {
//       triangle(b.pos.x - b.size / 2, b.pos.y, b.pos.x + b.size / 2, b.pos.y, b.pos.x, b.pos.y - b.size);
//     } else {
//       triangle(b.pos.x - b.size / 2, b.pos.y, b.pos.x + b.size / 2, b.pos.y, b.pos.x, b.pos.y + b.size);
//     }
    
// }

// function moveBrush(b) {
//     // add brush velocity to brush position
//     b.pos.add(b.vel)
//     for (var i=0; i<brushes.length; i++) {
//         if(brushes[i] == b) continue;

//         // gravity
//         var g = b.pos.copy();
//         g.sub(brushes[i].pos);
//         g.normalize();

//         if(b.pos.dist(brushes[i].pos) > 90) {
//             // move toward other brushes
//             g.div(4);
//             b.vel.add(g);
//         } else {
//             // move away from other brushes
//             b.vel.add(g.mult(100 - b.pos.dist(brushes[i].pos)));
//         }

//     }

//     // move towards the mouse
//     var m = createVector(mouseX, mouseY);
//     m.sub(b.pos);
//     m.normalize();
//     m.mult(100 - max(b.pos.dist(createVector(mouseX,mouseY)),100));
//     b.vel.sub(m);


//     // add some noise to the movement
//     var n = noise(b.pos.x/40,b.pos.y/40);
//     b.vel.add(createVector(sin(n*TWO_PI*400),cos(n*TWO_PI*400)).mult(10));


//     // change size
//     b.size += (random(1) - 0.6) * random(1, 3);
//     if (b.size < 0) {
//       b.size += random(1) * random(1, 3);
//     }

//     // try tweaking this value
//     b.vel.limit(1);


// }