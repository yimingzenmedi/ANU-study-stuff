// carousel:
var carouselList = [
    {
        image: "./asset/carousel1.jpg",
        link: "./blog_1.html",
        id: "carousel1",
        title: "carousel1"
    }, {
        image: "./asset/carousel2.jpg",
        link: "./destination_7.html",
        id: "carousel2",
        title: "carousel2"
    }, {
        image: "./asset/carousel3.jpg",
        link: "./blog_2.html",
        id: "carousel3",
        title: "carousel3"
    }
];

// image map area list:
var areasList1 = [
    {
        title: "Wellington",
        href: "./destination_1.html",
        shape: "circle",
        coords: [380, 1350, 13]
    }, {
        title: "Palmerston North",
        href: "./destination_2.html",
        shape: "circle",
        coords: [500, 1170, 13]
    }, {
        title: "New Plymouth",
        href: "./destination_3.html",
        shape: "circle",
        coords: [270, 925, 13]
    }, {
        title: "Taupo",
        href: "./destination_4.html",
        shape: "circle",
        coords: [573,847,13]
    }, {
        title: "Hamilton",
        href: "./destination_5.html",
        shape: "circle",
        coords: [450,677,13]
    }, {
        title: "Auckland",
        href: "./destination_6.html",
        shape: "circle",
        coords: [380,517,13]
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
        const img = document.getElementById("image_map_1");
        const width = img.width;
        const height = img.height;
        const naturalWidth = img.naturalWidth;
        const naturalHeight = img.naturalHeight;
        const scaleX = width / naturalWidth;
        const scaleY = height / naturalHeight;
        // console.log(img.width, img.height, img.naturalWidth);
        areaEle.coords = `${areaData.coords[0] * scaleX},${areaData.coords[1] * scaleY},${areaData.coords[2]}`;
        target.appendChild(areaEle);
    } 
}