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

// auto run:
window.onload = function() {
    carousel();
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