// let backgroundImage;

// const wallColor = "#90CBFB";
const wallColor = "#153959";
const winColor = 237;
const winBorderColor = 50;
const borderW = 40;
const curtainColor = "#C2B8AD";
const curtainDecorationColor = "#FFFCE6";
const curtainEdgeColor = "#A69668";

const edgeT = 50;
const edgeW = 200;
const edgeB = 100;
const sillOuter = 40;

const loop = 2000;

const birdsNumMin = 0, birdsNumMax = 15;

let noonColor, nightColor, sunsetColor, dayTransColor, bgColor;

let loopFrame;

let winFrameW;
let curtain;
let buildings = [];

let turn, birds, lastX, lastY;

// function preload() {
//   backgroundImage = loadImage('assets/windows-xp-bliss.jpg');
// }

function setup() {
  createCanvas(windowWidth, windowHeight);

  // since we're going to be drawing the background image at the same size each
  // frame, we re-size it once here in setup()
  // backgroundImage.resize(windowWidth, windowHeight);

  loopFrame = parseInt(random(0, loop));

  turn = false;

  lastX = windowWidth / 2;
  lastY = windowHeight / 2;
  
  noonColor = color(66,173,233);
  nightColor = color(10, 5, 40);
  sunsetColor = color(245, 125, 0);
  dayTransColor = color(200);
  nightTransColor = color(100);

  winFrameW = windowWidth - 2 * edgeW + 2 * sillOuter;
  curtain = {
    isClosed: true,
    isMoving: false,
    div: 10,
    color: curtainColor,
    decorationColor: curtainDecorationColor,
    winFrameW: winFrameW,
    speed: 1000,
    currentStep: 0,
    startStep: 0,
    edgeWidth: 0,
    // edgeColor: curtainEdgeColor,
  };

  initBuildings();

  initBirds(parseInt(random(birdsNumMin, birdsNumMax)));

  // print(buildings)
}


function draw() {
  // this `image()` function call is just an example---you should change (or
  // remove) it in your submitted assignment
  // image(backgroundImage, 0, 0);

  drawOutside(edgeT, edgeB, edgeW);

  drawWindow(edgeT, edgeB, edgeW, sillOuter);

  drawCurtain();


  if ((curtain.currentStep < curtain.speed && !curtain.isClosed) || (curtain.currentStep > 0 && curtain.isClosed)) {
    curtain.isMoving = true;
    updateCurtain();
  }

  if (curtain.isClosed && !curtain.isMoving) {
    loopFrame = getLoopFrame();
  } else {
    loopFrame = getLoopFrame(true);
  }

  // print(loopFrame)
}


function initBuildings() {
  buildings = [];
  // back:
  buildings.push(buildingFactory(
    random(0.2, 0.4),
    random(0.7, 0.9),
    random(0.1, 0.15),
    color(93,190,123),
    // parseInt(random(3, 5)),
    1,
    parseInt(random(30, 50))
  ));

  buildings.push(buildingFactory(
    random(0.6, 0.9),
    random(0.7, 0.9),
    random(0.1, 0.15),
    color(252,77,45),
    4,
    parseInt(random(10, 15)),
  ));

  // front:
  buildings.push(buildingFactory(
    0,
    random(0.4, 0.5),
    0.15,
    color(239, 82, 99),
    5,
    8
  ));

  buildings.push(buildingFactory(
    0.12,
    random(0.3, 0.4),
    0.2,
    color(230,198,160),
    parseInt(random(3, 6)),
    parseInt(random(4, 6))
  ));

  buildings.push(buildingFactory(
    0.32,
    random(0.3, 0.6),
    0.1,
    color(75,65,120),
    parseInt(random(3, 4)),
    parseInt(random(5, 7))
  ));

  buildings.push(buildingFactory(
    0.42,
    random(0.2, 0.5),
    0.15,
    color(130,202,40),
    parseInt(random(3, 6)),
    parseInt(random(4, 6))
  ));

  buildings.push(buildingFactory(
    0.57,
    random(0.6, 0.8),
    0.13,
    color(0,168,181),
    4,
    1
  ));

  buildings.push(buildingFactory(
    0.7,
    random(0.2, 0.4),
    0.18,
    color(170,171,102),
    parseInt(random(7, 10)),
    parseInt(random(4, 6))
  ));

  buildings.push(buildingFactory(
    0.8,
    random(0.5, 0.7),
    0.2,
    color(255,200,65),
    parseInt(random(3, 5)),
    parseInt(random(6, 8))
  ));
}

function buildingFactory(x, h, w, c, winCol, winRow) {
  let nightWin = [];
  for (let i = 0; i < winCol; i++) {
    let col = [];
    for (let j = 0; j < winRow; j++) {
      const isLight = getRandomBool();
      if (isLight) {
        const a = random(0.7, 1.3);
        col.push(color(237 * a, 222 * a, 9 * a));
      } else {
        col.push(null);
      }
    }    
    nightWin.push(col);
  }
  return {
    height: h,
    width: w,
    x: x,
    winCol: winCol,
    winRow: winRow,
    c: c,
    nightWin: nightWin
  }
}

function drawOutside(marginT, marginB, marginW) {
  const loopQtr = parseInt(loop / 4);
  const loopMid = parseInt(loop / 2);

  // sky color:
  const k = 0.2;

  if (loopFrame < loopQtr * k) {
    bgColor = lerpColor(dayTransColor, sunsetColor, (loopQtr * k - loopFrame) / (loopQtr * k));
  } else if (loopFrame < loopQtr) {
    bgColor = lerpColor(noonColor, dayTransColor, (loopQtr - loopFrame) / (loopQtr * (1 - k)));
  } else if (loopFrame < loopQtr * (2 - k)) {
    bgColor = lerpColor(dayTransColor, noonColor, (loopQtr * (2 - k) - loopFrame) / (loopQtr * (1 - k)));
  } else if (loopFrame < loopQtr * 2) {
    bgColor = lerpColor(sunsetColor, dayTransColor, (loopQtr * 2 - loopFrame) / (loopQtr * k));
  } else if (loopFrame < loopQtr * 3) {
    bgColor = lerpColor(nightColor, sunsetColor, (loopQtr * 3 - loopFrame) / loopQtr);
  } else {
    bgColor = lerpColor(sunsetColor, nightColor, abs(loopQtr * 4 - loopFrame) / loopQtr);
  }
  background(bgColor);

  // sun and moon:
  const midH = 180;
  const r = windowWidth / 2;
  if (loopFrame < loopMid) {
    const x = windowWidth / 2 + cos((loopFrame / loopMid) * PI) * r;
    const y = midH + windowWidth / 2 - sin((loopMid - loopFrame) / loopMid * PI) * r; 
    drawSun(x, y);
  } else {
    const x = windowWidth / 2 + cos(((loopFrame - loopMid) / loopMid) * PI) * r;
    const y = midH + windowWidth / 2 - sin((2 * loopMid - loopFrame) / loopMid * PI) * r; 
    drawMoon(x, y);
  }

  // buildings:
  drawBuildings(marginT, marginB, marginW);

  drawBirds();
}

function initBirds(n) {
  birds = [];
  for (let i = 0; i < n; i++) {
    birds.push({
      x: random(0, windowWidth),
      y: random(0, windowHeight),
      s: random(0.5, 2),
      c: color(random(0, 255), random(0, 255), random(0, 255)),
      sclr: random(0.2, 0.5)
    });
  }
}

function drawBirds() {
  for (const index in birds) {
    if (lastY > mouseY) {
      birds[index].y -= birds[index].s;
    } else if (lastY < mouseY) {
      birds[index].y += birds[index].s;
    }
  
    if (lastX > mouseX + 5) {
      turn = true;
      birds[index].x -= birds[index].s;
    } else if (lastX < mouseX - 5) {
      birds[index].x += birds[index].s;
      turn = false;
    }
    drawBird(birds[index].x, birds[index].y, birds[index].c, birds[index].sclr);
    // print(birds[index].x, birds[index].y);
    // print(windowWidth, windowHeight);
  }
  lastX = mouseX > lastX + 1 ? mouseX - 10 : mouseX + 10;
  lastY = mouseY > lastY + 1 ? mouseY - 10 : mouseY + 10;
}


function drawBuildings(marginT, marginB, marginW) {
  const winH = windowHeight - marginB - marginT - borderW;
  const winW = windowWidth - 2 * marginW;
  const baseY = windowHeight - marginB;
  // const k = 1 - abs(1 - ((getLoopFrame() + loop / 4) % loop) / loop * 2);
  const k = 1 - abs(1 - ((loopFrame + loop / 4) % loop) / loop * 2);

  // print(k);
  const windowColor = color(193 * k, 234 * k, 237 * k);
  const mT = 10;

  push();

  noStroke();

  for (const building of buildings) {
    const {height, width, x, winCol, winRow, c, nightWin} = building;
    fill(c);

    rect(marginW + x * winW, baseY - winH * height, width * winW, height * winH);
    const wW = width * winW / winCol;
    const mW = wW * 0.15;
    const h = (height * winH - mT) / winRow;
    const mH = 0.2 * h;

    // print("=====\nbuilding w: ", width * winW);
    // print("ww: ", wW, ", m: ", m, ", h:", h, ", winCol: ", winCol)

    for (const i in nightWin) {
      const col = nightWin[i];
      for (const j in col) {
        const win = col[j]
        let winC;
        if (k > 0.5) {
          winC = color(193 * k, 234 * k, 237 * k);
        } else {
          if (!win) {
            winC = color(193 * k, 234 * k, 237 * k);
          } else {
            winC = win;
          }
        }
        fill(winC);
        rect(marginW + x * winW + mW + wW * i, baseY - height  * winH + mT + j * (mH + h), wW * 0.6, h);
      }
    }  
    // print("=====================");  
  }

  pop();
}

function getRandomBool(bias) {
  if (!bias) {
    bias = 0.5;
  }
  return random(1) > bias;
}

function drawSun(x, y) {
  push();

  fill(247,196,8);
  noStroke();
  const r = 25;
  const div = 10;
  ellipse(x, y, r * 2);
  stroke(239,148,51);
  strokeWeight(2);
  for (let i = 0; i < div; i++) {
    const lineX = r * cos(i / div * PI * 2);
    const lineY = r * sin(i / div * PI * 2);
    // print(lineX, lineY)
    line(x + lineX * 1.2, y + lineY * 1.2, x + lineX * 1.5, y + lineY * 1.5);
  }

  pop();
}

function drawMoon(x, y) {
  push();

  const r1 = 25, r2 = 18; 
  noStroke();
  fill(241,217,77);
  ellipse(x, y, r1 * 2);
  fill(bgColor);
  ellipse(x + 10, y - 10, r2 * 2);

  pop();
}


function drawCurtain() {

  push();

  const {
    div, 
    color, 
    winFrameW, 
    speed, 
    currentStep, 
    decorationColor,
    edgeWidth, 
    // edgeColor
  } = curtain;
  const curtainRemain = (windowWidth - winFrameW) / 2 - 10;
  const curtainW = windowWidth / 2 - (windowWidth / 2 - curtainRemain) / speed * currentStep - edgeWidth;
  const divW = curtainW / div;

  const decorationH = 60;
  const decorationGape = windowHeight / 4;

  const ruga = windowWidth / 2 / div;
  
  noStroke();

  let i = 0;
  for (; i < curtain.div; i ++) {
    const posiL = divW * i, posiR = windowWidth - divW * (i + 1);

    fill(color);
    rect(posiL, 0, divW + 1, windowHeight);
    rect(posiR, 0, divW + 1, windowHeight);

    fill(decorationColor);
    rect(posiL, decorationGape, divW + 1, decorationH);
    ellipse(posiL + divW * 0.5, decorationGape, divW * 0.6, ruga * 0.6);
    arc(posiL, decorationGape + decorationH, divW * 0.4, ruga * 0.4, 0, HALF_PI);
    arc(posiL + divW, decorationGape + decorationH, divW * 0.4, ruga * 0.4, HALF_PI, PI);
    
    rect(posiR, decorationGape, divW + 1, decorationH);
    ellipse(posiR + divW * 0.5, decorationGape, divW * 0.6, ruga * 0.6);
    arc(posiR, decorationGape + decorationH, divW * 0.4, ruga * 0.4, 0, HALF_PI);
    arc(posiR + divW + 1, decorationGape + decorationH, divW * 0.4, ruga * 0.4, HALF_PI, PI);

    rect(posiL, decorationGape * 3 - decorationH, divW + 1, decorationH);
    ellipse(posiL + divW * 0.5, decorationGape * 3 - decorationH, divW * 0.6, ruga * 0.6);
    arc(posiL, decorationGape * 3, divW * 0.4, ruga * 0.4, 0, HALF_PI);
    arc(posiL + divW, decorationGape * 3, divW * 0.4, ruga * 0.4, HALF_PI, PI);

    rect(posiR, decorationGape * 3 - decorationH, divW + 1, decorationH);
    ellipse(posiR + divW * 0.5, decorationGape * 3 - decorationH, divW * 0.6, ruga * 0.6);
    arc(posiR, decorationGape * 3, divW * 0.4, ruga * 0.4, 0, HALF_PI);
    arc(posiR + divW + 1, decorationGape * 3, divW * 0.4, ruga * 0.4, HALF_PI, PI);


    fill(color);

    ellipse(posiL + divW * 0.5, decorationGape + decorationH, divW * 0.6, ruga * 0.6);
    arc(posiL, decorationGape, divW * 0.4, ruga * 0.4, 0, HALF_PI);
    arc(posiL + divW, decorationGape, divW * 0.4, ruga * 0.4, HALF_PI, PI);

    ellipse(posiR + divW * 0.5, decorationGape + decorationH, divW * 0.6, ruga * 0.6);
    arc(posiR, decorationGape, divW * 0.4, ruga * 0.4, 0, HALF_PI);
    arc(posiR + divW + 1, decorationGape, divW * 0.4, ruga * 0.4, HALF_PI, PI);

    ellipse(posiL + divW * 0.5, decorationGape * 3, divW * 0.6, ruga * 0.6);
    arc(posiL, decorationGape * 3 - decorationH, divW * 0.4, ruga * 0.4, 0, HALF_PI);
    arc(posiL + divW, decorationGape * 3 - decorationH, divW * 0.4, ruga * 0.4, HALF_PI, PI);

    ellipse(posiR + divW * 0.5, decorationGape * 3, divW * 0.6, ruga * 0.6);
    arc(posiR, decorationGape * 3 - decorationH, divW * 0.4, ruga * 0.4, 0, HALF_PI);
    arc(posiR + divW + 1, decorationGape * 3 - decorationH, divW * 0.4, ruga * 0.4, HALF_PI, PI);



    fill("rgba(0, 0, 0, 0.1)");
    rect(posiL, 0, divW * 0.2, windowHeight);
    rect(posiL + divW - divW * 0.2, 0, divW * 0.2, windowHeight);
    rect(posiR, 0, divW * 0.2, windowHeight);
    rect(posiR + divW - divW * 0.2 + 1, 0, divW * 0.2, windowHeight);
  }

  fill(100);
  rect(divW * i, 0, edgeWidth, windowHeight);
  rect(windowWidth - divW * i - edgeWidth, 0, edgeWidth, windowHeight);

  pop();

  if (isMouseOnCurtain()) {
    cursor(HAND);
  } else {
    cursor(ARROW);
  }
}

function updateCurtain() {
  // print("start frame: " + frameCount);
  if (curtain.isClosed) {
    curtain.currentStep = curtain.currentStep - frameCount + curtain.startStep;
    if (curtain.currentStep <= 0) {
      curtain.isMoving = false;
      initBirds(parseInt(random(birdsNumMin, birdsNumMax)));
    }
    // print("closing: " + curtain.currentStep, curtain.isMoving);
  } else {
    curtain.currentStep = curtain.currentStep + frameCount - curtain.startStep;
    if (curtain.currentStep >= curtain.speed) {
      curtain.isMoving = false;
    }
    // print("openning: " + curtain.currentStep, curtain.isMoving);
  }
}

function isMouseOnCurtain() {
  if (!curtain) {
    return false;
  }
  const {
    speed, 
    currentStep, 
    edgeWidth, 
  } = curtain;

  const curtainRemain = (windowWidth - winFrameW) / 2 - 20;
  const curtainW = windowWidth / 2 - (windowWidth / 2 - curtainRemain) / speed * currentStep - edgeWidth;
  return mouseX <= curtainW || mouseX >= windowWidth - curtainW;
}


function drawWindow(marginT, marginB, marginW, sillOuter) {
  const frameW = windowWidth - marginW * 2, frameH = windowHeight - marginT - marginB;
  // const sillOuter = 40;
  const sillDeep = 15;
  const deep = 0;
  const winBorderW = 6;
  const winH = (frameH - deep - winBorderW * 3) / 4; 
  const winW = (frameW - borderW * 2 - winBorderW) / 4;

  push();
  noStroke();

  // fill the outside:
  push();
  fill(wallColor);
  rect(0, 0, windowWidth, marginT);
  rect(0, 0, marginW, windowHeight);
  rect(frameW + marginW, 0, marginW, windowHeight);
  rect(0, windowHeight - marginB, windowWidth, marginB);
  pop();

  // draw the window frame:
  noFill();
  rect(marginW, marginT, frameW, frameH);
  fill(winColor);
  rect(marginW, marginT, frameW, borderW);
  rect(marginW, marginT, borderW, frameH);
  rect(frameW + marginW - borderW, marginT, borderW, frameH);
  noFill();
  rect(marginW + borderW, marginT + borderW, frameW - borderW * 2, frameH - borderW);
  
  // draw window border:
  push();
  fill(winBorderColor);
  rect(marginW + borderW, marginT + borderW, frameW - 2 * borderW, winBorderW);
  rect(marginW + borderW, marginT + borderW + winH, frameW - 2 * borderW, winBorderW);
  rect(marginW + borderW, marginT + frameH - deep - winBorderW, frameW - 2 * borderW, winBorderW);
  rect(marginW + borderW, marginT + borderW, winBorderW, frameH - borderW - deep);
  rect(marginW + frameW - borderW - winBorderW, marginT + borderW, winBorderW, frameH - borderW - deep);
  rect(marginW + borderW + winW, marginT + borderW, winBorderW, frameH - borderW - deep);
  rect(marginW + borderW + winW * 2, marginT + borderW, winBorderW, frameH - borderW - deep);
  rect(marginW + borderW + winW * 3, marginT + borderW, winBorderW, frameH - borderW - deep);

  pop();

  // draw window:
  drawWindowFrame(marginW + borderW + winBorderW, marginT + borderW + winBorderW, winW - winBorderW, winH - winBorderW);
  drawWindowFrame(marginW + borderW + winBorderW + winW, marginT + borderW + winBorderW, winW - winBorderW, winH - winBorderW);
  drawWindowFrame(marginW + borderW + winBorderW + winW * 2, marginT + borderW + winBorderW, winW - winBorderW, winH - winBorderW);
  drawWindowFrame(marginW + borderW + winBorderW + winW * 3, marginT + borderW + winBorderW, winW - winBorderW, winH - winBorderW);

  drawWindowFrame(marginW + borderW + winBorderW, marginT + borderW + winBorderW + winH, winW - winBorderW, frameH - deep - winH - winBorderW * 2 - borderW, 3, 1);
  drawWindowFrame(marginW + borderW + winBorderW + winW, marginT + borderW + winBorderW + winH, winW - winBorderW, frameH - deep - winH - winBorderW * 2 - borderW, 3, 0);
  drawWindowFrame(marginW + borderW + winBorderW + winW * 2, marginT + borderW + winBorderW + winH, winW - winBorderW, frameH - deep - winH - winBorderW * 2 - borderW, 3, 1);
  drawWindowFrame(marginW + borderW + winBorderW + winW * 3, marginT + borderW + winBorderW + winH, winW - winBorderW, frameH - deep - winH - winBorderW * 2 - borderW, 3, 0);
  

  // draw windowsill:

  fill(winColor + 15);
  beginShape();
  vertex(marginW, marginT + frameH);
  vertex(marginW - sillOuter, marginT + frameH + sillDeep);
  vertex(marginW + frameW + sillOuter, marginT + frameH + sillDeep);
  vertex(marginW + frameW, marginT + frameH);
  endShape(CLOSE);
  fill(winColor - 10);
  rect(marginW - sillOuter, marginT + frameH + sillDeep, frameW + 2 * sillOuter, 10);
  fill(winColor);
  rect(marginW, marginT + frameH + sillDeep + 10, frameW, 25);


  // draw shadow:
  fill("rgba(0, 0, 0, 0.1)");
  beginShape();
  
  vertex(marginW, marginT + frameH + sillDeep + 10);
  vertex(marginW + 80, marginT + frameH + sillDeep + 50);
  vertex(marginW + frameW + 85 + sillOuter, marginT + frameH + sillDeep + 50);
  vertex(marginW + frameW + 85 + sillOuter, marginT + frameH + sillDeep + 40);
  vertex(marginW + frameW + sillOuter, marginT + frameH + sillDeep);

  endShape(CLOSE);
  
  pop();
}

function drawWindowFrame(x, y, w, h, div = 1, handleSide = null) {
  const borderW = 12;
  const dividerW = 8;

  push();

  fill(winColor);
  rect(x, y, w, borderW);
  rect(x, y + h - borderW, w, borderW);
  rect(x, y, borderW, h);
  rect(x + w - borderW, y, borderW, h);
  stroke("rgba(0, 0, 0, 0.15)");
  strokeWeight(1);
  line(x, y, x + borderW, y + borderW);
  line(x + w - borderW, y + h - borderW, x + w, y + h);
  line(x, y + h, x + borderW, y + h - borderW);
  line(x + w, y, x + w - borderW, y + borderW);

  // draw divider:
  push();
  const winH = (h - 2 * borderW) / div;
  stroke(winColor);
  strokeWeight(dividerW);
  for (let i = 0; i < div - 1; i ++) {
    line(x + borderW, y + borderW + winH * (1 + i), x + w - borderW, y + borderW + winH * (1 + i));
    
  }
  stroke("rgba(0, 0, 0, 0.1)");
  strokeWeight(1);
  const d = dividerW / 2;
  for (let i = 0; i < div - 1; i ++) {
    // line(x + borderW, y + borderW + winH * (1 + i), x + w - borderW, y + borderW + winH * (1 + i));
    line(x + borderW - d, y + borderW + winH * (1 + i), x + borderW, y + borderW + winH * (1 + i) + d);
    line(x + borderW - d, y + borderW + winH * (1 + i), x + borderW, y + borderW + winH * (1 + i) - d);
    line(x + w - borderW + d, y + borderW + winH * (1 + i), x + w - borderW, y + borderW + winH * (1 + i) + d);
    line(x + w - borderW + d, y + borderW + winH * (1 + i), x + w - borderW, y + borderW + winH * (1 + i) - d);
  }

  pop();


  if (handleSide != null) {
    fill(100);
    const handleSize = 10;
    if (handleSide === 1) {
      ellipse(x + w - borderW / 2, y + h / 2, handleSize);
    } else if (handleSide === 0) {
      ellipse(x + borderW / 2, y + h / 2, handleSize);
    }
  }

  pop();
}


function drawBird(x, y, c, sclr) {
  if (!c) {
    c = 255;
  }

  if (!sclr) {
    sclr = 1;
  }

  const l = 50, s = 25;
  const dir = turn ? -1 : 1;
  push();
  fill(c);
  noStroke();
  scale(sclr);
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
  triangle(x - l * 0.3, y, x, y + (-1) ** parseInt(frameCount / 8) * s * 1.5, x + s * 0.5, y);
  // eyes:
  fill(0);
  ellipse(x + dir * l * 0.5 + dir * s * 0.4, y, 2);
  // ellipse(x + l * 0.5 + s * 0.4, y - s * 0.1, 2);


  pop();
}


function mouseClicked() {
  if (isMouseOnCurtain()) {
    curtain.isClosed = !curtain.isClosed;
    curtain.startStep = frameCount;
  }
}

function getLoopFrame(isSlow) {
  if (!isSlow) {
    return frameCount % loop;
  }
  if (!loopFrame) {
    return frameCount % 100 > 50 ? 1 : 0;
  }
  return loopFrame + (frameCount % 5 === 0 ? 0.05 : 0);
}


// when you hit the spacebar, what's currently on the canvas will be saved (as a
// "thumbnail.png" file) to your downloads folder

function keyTyped() {
  if (key === " ") {
    saveCanvas("thumbnail.png");
  }
}