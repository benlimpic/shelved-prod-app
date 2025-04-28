document.addEventListener("DOMContentLoaded", function () {
  let visitedUrls = JSON.parse(sessionStorage.getItem("visitedUrls")) || [];
  if (visitedUrls[visitedUrls.length - 1] !== window.location.href) {
    visitedUrls.push(window.location.href);
    sessionStorage.setItem("visitedUrls", JSON.stringify(visitedUrls));
  }
});