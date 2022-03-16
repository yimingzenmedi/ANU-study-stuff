const rectX = 50, rectY = 50, rectL = 150;
const elliX = 350, elliY = 75 + 50, elliW = 150;
const arcX = 650 - 75, arcY = 50 + 75, arcW = 150;
var  arcStart, arcStop;
var starry_night, sunset, party;
var img;


function setup() {
  createCanvas(windowWidth, windowHeight);
  img = "white";
  starry_night = loadImage("./assets/starry_night.jpg");
  sunset = loadImage("./assets/sunset.png");
  party = loadImage("./assets/party.jpg");
  arcStart = PI, arcStop = 0;
  // put setup code here
}

function draw() {
  // put drawing code here
  background(img);

  // rectangle:
  // fill("black");
  // noStroke();
  // rect(rectX, rectY, rectL);
  // ellipse(elliX, elliY, elliW);

  // stroke("white");
  // strokeWeight(5);

  // const rectMidX = rectX + rectL / 2;
  // const rectMidY = rectY + rectL / 2;
  // line(rectX, rectMidY, rectX + rectL, rectMidY);
  // line(rectMidX, rectY, rectMidX, rectY + rectL);

  // line(elliX - elliW / 2, elliY, elliX + elliW / 2, elliY);
  // line(elliX, elliY - elliW / 2, elliX, elliY + elliW / 2);


  if (
    mouseIsPressed &&
    mouseX >= rectX &&
    mouseX <= rectX + rectL &&
    mouseY >= rectY &&
    mouseY <= rectY + rectL
  ) {
    img = starry_night;
    // image(starry_night, rectX, rectY, rectL, rectL);
  } else if (mouseIsPressed && dist(mouseX, mouseY, elliX, elliY) <= elliW / 2) {
    img = sunset;
  } else if (mouseIsPressed &&
      (
        dist(mouseX, mouseY, arcX, arcY) <= arcW / 2  || 
        (
          mouseX >= arcX - arcW / 2 &&
          mouseX <= arcX - arcW / 2 + arcW &&
          mouseY >= arcY &&
          mouseY <= arcY + arcW / 2
        )
      )
    ) {
    img = party;
    console.log(111)
  } else {
    img = "white";
    fill("black");
    noStroke();

    rect(rectX, rectY, rectL);

    ellipse(elliX, elliY, elliW);

    arc(arcX, arcY, arcW, arcW, arcStart, arcStop);
    rect(arcX - arcW / 2, arcY, arcW, arcW / 2);

    stroke("white");
    strokeWeight(5);
  
    const rectMidX = rectX + rectL / 2;
    const rectMidY = rectY + rectL / 2;

    line(rectX, rectMidY, rectX + rectL, rectMidY);
    line(rectMidX, rectY, rectMidX, rectY + rectL);
  
    line(elliX - elliW / 2, elliY, elliX + elliW / 2, elliY);
    line(elliX, elliY - elliW / 2, elliX, elliY + elliW / 2);

    line(arcX - arcW / 2, arcY, arcX + arcW / 2, arcY);
    line(arcX, arcY - arcW / 2, arcX, arcY + arcW / 2);
  }
}
