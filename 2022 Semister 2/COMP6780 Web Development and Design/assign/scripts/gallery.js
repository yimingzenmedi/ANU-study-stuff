// Mock data, this should from backend server.

var imgList = [
    {
        id: 1,
        title: "Canberra Night",
        src: "./asset/gallery/black_mountain_1.png",
        thumbnail: "./asset/gallery/black_mountain_1_thumb.png",
        tags: [
            "night", "city"
        ],
        time: "15-06-2021",
        location: "Black Mountain, Canberra, ACT, Australia",
        description: "An image of the Canberra City shot on Black Mountain after the final exam.",
    }, {
        id: 2,
        title: "Frame on State HWY 25",
        src: "./asset/gallery/coromandel_1.png",
        thumbnail: "./asset/gallery/coromandel_1_thumb.png",
        tags: [
            "dusk", "road", "destination"
        ],
        time: "05-01-2022",
        location: "Te Mata Bay, Coromabdel, New Zealand",
        description: "A frame on the beach near the State HWY 25.",
    }, {
        id: 3,
        title: "Star trails",
        src: "./asset/gallery/hamilton_1.png",
        thumbnail: "./asset/gallery/hamilton_1_thumb.png",
        tags: [
            "night", "star trails"
        ],
        time: "06-09-2022",
        location: "Tills Lookout, Hamilton, New Zealand",
        description: "First attempt of taking star trails.",
    }, {
        id: 4,
        title: "Manukau Heads Lighthouse",
        src: "./asset/gallery/lighthouse_1.png",
        thumbnail: "./asset/gallery/lighthouse_1_thumb.png",
        tags: [
            "lighthouse", "destination"
        ],
        time: "11-01-2020",
        location: "Manukau Heads Lighthouse, Manukau Heads, Auckland, New Zealand",
        description: "",
    }, {
        id: 5,
        title: "Mission Bay Sunset",
        src: "./asset/gallery/mission_bay_1.png",
        thumbnail: "./asset/gallery/mission_bay_1_thumb.png",
        tags: [
            "sunset", "destination"
        ],
        time: "03-10-2020",
        location: "Okahu Bay Wharf, Orakei, Auckland, New Zealand",
        description: "One of the best places to see Auckland City view.",
    }, {
        id: 6, 
        title: "Mt Eden",
        src: "./asset/gallery/mt_eden_1.png",
        thumbnail: "./asset/gallery/mt_eden_1_thumb.png",
        tags: [
            "destination", "dusk"
        ],
        time: "14-10-2020",
        location: "Mount Eden, Auckland, New Zealand",
        description: "",
    }, {
        id: 7, 
        title: "The Tree and the Mountain",
        src: "./asset/gallery/mt_taranaki_1.png",
        thumbnail: "./asset/gallery/mt_taranaki_1_thumb.png",
        tags: [
            "destination"
        ],
        time: "10-09-2022",
        location: "Hollard Gardens, Kaponga, New Zealand",
        description: "Which one is taller?",
    }, {
        id: 8, 
        title: "Sunrise of Mt Taranaki",
        src: "./asset/gallery/mt_taranaki_2.png",
        thumbnail: "./asset/gallery/mt_taranaki_2_thumb.png",
        tags: [
            "destination", "sunrise"
        ],
        time: "10-09-2022",
        location: "Hollard Gardens, Kaponga, New Zealand",
        description: "Beautiful sunrise in front of Mt Taranaki",
    }, {
        id: 9, 
        title: "Milkyway on Piha Beach",
        src: "./asset/gallery/piha_galaxy_1.png",
        thumbnail: "./asset/gallery/piha_galaxy_1_thumb.png",
        tags: [
            "milkyway", "destination"
        ],
        time: "04-09-2020",
        location: "Piha Beach, Auckland, New Zealand",
        description: "First time to use a fish-eye lens.",
    }, {
        id: 10, 
        title: "Tiantan",
        src: "./asset/gallery/tiantan_1.png",
        thumbnail: "./asset/gallery/tiantan_1_thumb.png",
        tags: [
            "destination"
        ],
        time: "03-04-2017",
        location: "Tiantan Park, Beijing, China",
        description: "One of the most famous ancient Chinese buildings.",
    }, {
        id: 11, 
        title: "Umupuia Beach Milkyway",
        src: "./asset/gallery/umupuia_1.png",
        thumbnail: "./asset/gallery/umupuia_1_thumb.png",
        tags: [],
        time: "30-04-2022",
        location: "Umupuia Beach, Auckland, New Zealand",
        description: "Unpopular location but very suitable for shooting starry sky.",
    }, {
        id: 12, 
        title: "Father and Son",
        src: "./asset/gallery/waitawa_1.png",
        thumbnail: "./asset/gallery/waitawa_1_thumb.png",
        tags: [
            "wharf", "street photography"
        ],
        time: "03-07-2021",
        location: "Waitawa Regional Park, Auckland, New Zealand",
        description: "Let's go fishing, daddy!",
    }, {
        id: 13, 
        title: "Mataitai Wharf",
        src: "./asset/gallery/waitawa_2.png",
        thumbnail: "./asset/gallery/waitawa_2_thumb.png",
        tags: [
            "wharf", "destination"
        ],
        time: "03-07-2021",
        location: "Waitawa Regional Park, Auckland, New Zealand",
        description: "",
    }
];

// tag colors to choose:
var colors = ["#FA9494", "#C3FF99", "#ABC9FF", "#F9F9C5"]
var tagCounter = 0;

// auto run:

window.onload = function() {
    renderImageThumbs();
}

function renderImageThumbs() {
    const targetEle = document.getElementById("gallery");
    for (const imgIndex in imgList) {
        const img = imgList[imgIndex];
        const picDivEle = document.createElement("div");
        picDivEle.className = "pic_div";

        // thumbnail image:
        const picFrameEle = document.createElement("div");
        picFrameEle.className = "pic_frame";
        const picImgEle = document.createElement("img");
        picImgEle.className = "pic";
        picImgEle.src = img.thumbnail;
        picImgEle.alt = img.title;
        picFrameEle.appendChild(picImgEle);
        picDivEle.appendChild(picFrameEle);

        // title:
        const titleH3Ele = document.createElement("h3");
        titleH3Ele.className = "title";
        const titleTextEle = document.createTextNode(img.title);
        titleH3Ele.appendChild(titleTextEle);
        picDivEle.appendChild(titleH3Ele);

        // location:
        const locationPEle = document.createElement("p");
        locationPEle.className = "location";
        locationPEle.title = "location";
        const locationIconImgEle = document.createElement("img");
        locationIconImgEle.className = "location_icon";
        locationIconImgEle.src = "./asset/location.png";
        locationIconImgEle.alt = "location icon";
        locationPEle.appendChild(locationIconImgEle);
        const locationTextEle = document.createTextNode(img.location);
        locationPEle.appendChild(locationTextEle);
        picDivEle.appendChild(locationPEle);

        // time:
        const timePEle = document.createElement("p");
        timePEle.className = "time";
        const timeTextEle = document.createTextNode(img.time);
        timePEle.appendChild(timeTextEle);
        picDivEle.appendChild(timePEle);

        // tags:
        const tagsDivEle = document.createElement("div");
        tagsDivEle.className = "tags";
        for (const tag of img.tags) {
            const tagSpanEle = document.createElement("span");
            tagSpanEle.className = "tag";
            tagSpanEle.style.backgroundColor = colors[tagCounter++ % colors.length];     // pick a color from the list
            const iconEleImg = document.createElement("img");
            iconEleImg.src = "./asset/tag_icon.png";
            iconEleImg.className = "tag_icon";
            tagSpanEle.appendChild(iconEleImg);
            const tagTextEle = document.createTextNode(tag);
            tagSpanEle.appendChild(tagTextEle);
            tagsDivEle.appendChild(tagSpanEle);
        }
        picDivEle.appendChild(tagsDivEle);

        // desctiption:
        const descPEle = document.createElement("p");
        descPEle.className = "desc";
        const descTextEle = document.createTextNode(img.description);
        descPEle.appendChild(descTextEle);
        picDivEle.appendChild(descPEle);

        // bond click function:
        picDivEle.onclick = function() {
            onThumbClick(imgIndex);
        };

        targetEle.appendChild(picDivEle);
    }
    
}

function renderLargeImg(imgIndex) {
    const img = imgList[imgIndex];
    const targetEle = document.getElementById("largeImgDiv");

    // large img mask div:
    const largeImgMaskDivEle = document.createElement("div");
    largeImgMaskDivEle.id = "large_img_mask";

    // close btn:
    const closeBtnEle = document.createElement("button");
    closeBtnEle.className = "close_btn";
    closeBtnEle.title = "Close";
    closeBtnEle.appendChild(document.createTextNode("X"));
    closeBtnEle.onclick = function() {
        onCloseClick();
    }
    largeImgMaskDivEle.appendChild(closeBtnEle);

    // last image btn:
    const lastbtnEle = document.createElement("button");
    lastbtnEle.className = "last_btn";
    lastbtnEle.appendChild(document.createTextNode("<"));
    lastbtnEle.onclick = function() {
        onLastBtnClick(imgIndex);
    }
    largeImgMaskDivEle.appendChild(lastbtnEle);

    // next image btn:
    const nextBtnEle = document.createElement("button");
    nextBtnEle.className = "next_btn";
    nextBtnEle.appendChild(document.createTextNode(">"));
    nextBtnEle.onclick = function() {
        onNextBtnClick(imgIndex);
    }
    largeImgMaskDivEle.appendChild(nextBtnEle);

    // large img frame div:
    const largeImgFrameDivEle = document.createElement("div");
    largeImgFrameDivEle.className = "large_img_frame";
    
    // large img:
    const largeImgEle = document.createElement("img");
    largeImgEle.className = "large_img";
    largeImgEle.src = img.src;
    largeImgEle.alt = img.title;
    largeImgFrameDivEle.appendChild(largeImgEle);
    largeImgMaskDivEle.appendChild(largeImgFrameDivEle);

    targetEle.appendChild(largeImgMaskDivEle);
}


// on thumb image click:
function onThumbClick(imgIndex) {
    const img = imgList[imgIndex];
    console.log(img.title);
    const bodyEle = document.getElementsByTagName("body")[0];
    bodyEle.style.overflowX = "hidden";
    bodyEle.style.overflowY = "hidden";

    renderLargeImg(imgIndex);
}

// on close btn click:
function onCloseClick() {
    const bodyEle = document.getElementsByTagName("body")[0];
    bodyEle.style.overflowX = "scroll";
    bodyEle.style.overflowY = "scroll";

    const targetEle = document.getElementById("large_img_mask");
    targetEle.remove();
}

// on last btn click:
function onLastBtnClick(imgIndex) {
    const lastIndex = Number(imgIndex) - 1;
    console.log("current:", imgIndex, ", last:", lastIndex);

    if (imgIndex > 0) {
        onCloseClick();
        renderLargeImg(lastIndex);
    }
}

// on next btn click:
function onNextBtnClick(imgIndex) {
    const nextIndex = Number(imgIndex) + 1;
    const imgListSize = imgList.length;
    console.log("current:", imgIndex, ", next:", nextIndex);

    if (imgIndex < imgListSize - 1) {
        onCloseClick();
        renderLargeImg(nextIndex);
    }
}