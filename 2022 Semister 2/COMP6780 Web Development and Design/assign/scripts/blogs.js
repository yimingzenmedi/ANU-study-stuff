// Mock data, this should from backend server in practical applications.
var data = [
    {
        href: "./blog_1.html",
        coverImg: "./asset/blog1_cover.jpg",
        title: "The BEST places to see Auckland City",
        abstract: "Where is the best places to see Auckland City?",
        tags: [
            "trip", "city", "destinations"
        ]
    }, {
        href: "./blog_2.html",
        coverImg: "./asset/blog2_cover.jpg",
        title: "How to: Astrophotography",
        abstract: "How to photograph the starry sky?",
        tags: [
            "tutorial", "photography", "stars"
        ]
    }, {
        href: "./blog_3.html",
        coverImg: "./asset/blog3_cover.jpg",
        title: "All the way north: Auckland to Cape Reinga",
        abstract: "From New Zealand's busiest city to the northernmost point",
        tags: [
            "destinations", "travel notes", "road"
        ]
    }
];

// tag colors to choose:
var colors = ["#FA9494", "#C3FF99", "#ABC9FF", "#F9F9C5"]

// auto run:
window.onload = function() {
    renderArticals();
}

// Render article list
function renderArticals () {
    let tagCounter = 0;
    const targetEle = document.getElementById("articals");
    for (const artical of data) {
        // create element a:
        const articalEleA = document.createElement("a");
        articalEleA.href = artical.href;
        articalEleA.className = "artical_link";
        // create element div.artical:
        const articalEleDiv = document.createElement("div");
        articalEleDiv.className = "artical";
        // create element img.cover_img:
        const coverImgEleImg = document.createElement("img");
        coverImgEleImg.className = "cover_img";
        coverImgEleImg.src = artical.coverImg;
        coverImgEleImg.alt = artical.coverImg;
        articalEleDiv.appendChild(coverImgEleImg);
        // create element div.right_content:
        const rightContentEleDiv = document.createElement("div");
        rightContentEleDiv.className = "right_content";
        // create element img.cover_img
        const articalTitleEleH3 = document.createElement("h3");
        articalTitleEleH3.textContent = artical.title;
        articalTitleEleH3.className = "artical_title";
        rightContentEleDiv.appendChild(articalTitleEleH3);
        // create element p.artical_abs
        const articalAbsEleP = document.createElement("p");
        articalAbsEleP.className = "artical_abs";
        articalAbsEleP.innerText = artical.abstract;
        rightContentEleDiv.appendChild(articalAbsEleP);
        // create element div.artical_tags
        const articalTagsEleDiv = document.createElement("div");
        articalTagsEleDiv.className = "artical_tags";
        // craete tag elements:
        for (const tag of artical.tags) {
            const articalTagEleSpan = document.createElement("span");
            articalTagEleSpan.className = "artical_tag";
            const iconEleImg = document.createElement("img");
            iconEleImg.src = "./asset/tag_icon.png";
            iconEleImg.className = "tag_icon";
            articalTagEleSpan.appendChild(iconEleImg);
            const articalTagTextEleTxt = document.createTextNode(tag);
            articalTagEleSpan.appendChild(articalTagTextEleTxt);
            const color = colors[tagCounter++ % colors.length];     // pick a color from the list
            articalTagEleSpan.style.backgroundColor = color;
            articalTagsEleDiv.appendChild(articalTagEleSpan);
        }
        rightContentEleDiv.appendChild(articalTagsEleDiv);

        articalEleDiv.appendChild(rightContentEleDiv);
        articalEleA.appendChild(articalEleDiv);
        targetEle.appendChild(articalEleA);
    }
}