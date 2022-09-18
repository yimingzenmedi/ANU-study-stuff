var data = [
    {
        href: "./destination_1.html",
        coverImg: "./asset/desti1_cover.jpg",
        title: "Wellington",
        abstract: "Capital city of new zealand."
    }, {
        href: "./destination_2.html",
        coverImg: "./asset/desti2_cover.jpg",
        title: "Palmerstion North",
        abstract: "University City"
    }, {
        href: "./destination_3.html",
        coverImg: "./asset/desti3_cover.jpg",
        title: "New Plymouth",
        abstract: "One of Auckland's most famous west coast black sand beaches"
    }, {
        href: "./destination_4.html",
        coverImg: "./asset/desti4_cover.jpg",
        title: "Taupo",
        abstract: "One of Auckland's most famous west coast black sand beaches"
    }, {
        href: "./destination_5.html",
        coverImg: "./asset/desti5_cover.jpg",
        title: "Hamilton",
        abstract: "One of Auckland's most famous west coast black sand beaches"
    }, {
        href: "./destination_6.html",
        coverImg: "./asset/desti6_cover.jpg",
        title: "Auckland",
        abstract: "One of Auckland's most famous west coast black sand beaches"
    }, {
        href: "./destination_7.html",
        coverImg: "./asset/desti7_cover.jpg",
        title: "Piha",
        abstract: "One of Auckland's most famous west coast black sand beaches"
    }, 
]

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

        articalEleDiv.appendChild(rightContentEleDiv);
        articalEleA.appendChild(articalEleDiv);
        targetEle.appendChild(articalEleA);
    }
}