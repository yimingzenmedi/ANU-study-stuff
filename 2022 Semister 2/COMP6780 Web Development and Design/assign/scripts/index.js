// carousel:
var carouselList = [
    {
        image: "./asset/carousel1.jpg",
        link: "./blog_1.html",
        id: "carousel1",
        title: "carousel1"
    }, {
        image: "./asset/carousel2.jpg",
        link: "./destination_1.html",
        id: "carousel2",
        title: "carousel2"
    }, {
        image: "./asset/carousel3.jpg",
        link: "./blog_2.html",
        id: "carousel3",
        title: "carousel3"
    }
];

var areasList1 = [
    {
        title: "Wellington",
        href: "./destination_1.html",
        shape: "circle",
        coords: [396, 1412, 30]
    }, {
        title: "Palmerston North",
        href: "./destination_1.html",
        shape: "circle",
        coords: [541, 1230, 30]
    }, {
        title: "New Plymouth",
        href: "./destination_1.html",
        shape: "circle",
        coords: [295, 975, 30]
    }, {
        title: "Taupo",
        href: "./destination_1.html",
        shape: "circle",
        coords: [600,892,30]
    }, {
        title: "Hamilton",
        href: "./destination_1.html",
        shape: "circle",
        coords: [480,714,30]
    }, {
        title: "Auckland",
        href: "./destination_1.html",
        shape: "circle",
        coords: [406,547,30]
    }
];

// auto run:
window.onload = function() {
    carousel();
    renderAreas1(areasList1);
}

// carousel:
function carousel() {
    const carouselParentEle = document.getElementById("carousel");
    for (const carouselIndex in carouselList) {
        const carousel = carouselList[carouselIndex];
        const carouselEleA = document.createElement("a");
        carouselEleA.href = carousel.link;
        carouselEleA.setAttribute("id", carousel.id);
        const carouselEleDiv = document.createElement("div");
        carouselEleDiv.setAttribute("class", "carouselFrame");
        carouselEleDiv.style.backgroundImage = `url(${carousel.image})`;
        carouselEleA.insertAdjacentElement("afterbegin", carouselEleDiv);
        carouselParentEle.insertAdjacentElement("beforeend", carouselEleA);
    }

    let index = 0;
    index = setCarouselDisplay(index);
    self.setInterval(function() {
        index = setCarouselDisplay(index);
    }, 5000);
}

// set the img to display and return the next index
function setCarouselDisplay(index) {
    for (const carouselIndex in carouselList) {
        const carousel = carouselList[carouselIndex];
        const carouselId = carousel.id;
        const carouselEle = document.getElementById(carouselId);
        if (carouselIndex == index) {
            carouselEle.style.display = "block";
            // carouselEle.style.opacity = 1;
        } else {
            carouselEle.style.display = "none";
            // carouselEle.style.opacity = 0;
        }
    }
    index = (index + 1) % 3;
    return index;
}

// render areas for image maps:

function renderAreas1(areasList) {
    const target = document.getElementById("image-map1");

    for (const areaData of areasList) {
        const areaEle = document.createElement("area");
        areaEle.alt = areaData.title;
        areaEle.title = areaData.title;
        areaEle.href = areaData.href;
        areaEle.coords = areaData.coords;
        areaEle.shape = areaData.shape;
        // calculate coordinate:
        areaEle.coords = `${areaData.coords[0] * 0.6},${areaData.coords[1] * 0.6},${areaData.coords[2] * 0.6}`;
        target.appendChild(areaEle);
    } 
}