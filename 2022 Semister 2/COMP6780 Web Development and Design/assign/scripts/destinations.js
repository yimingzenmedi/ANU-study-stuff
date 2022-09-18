var data = [
    {
        href: "./destination_1.html",
        coverImg: "./asset/desti1_cover.jpg",
        title: "Piha",
        abstract: "One of Auckland's most famous west coast black sand beaches"
    }
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