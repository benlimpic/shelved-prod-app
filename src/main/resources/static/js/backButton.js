document.addEventListener("DOMContentLoaded", function () {
  const backButton = document.getElementById("backButton");
  if (backButton) {
    backButton.addEventListener("click", function () {
      let visitedUrls = JSON.parse(sessionStorage.getItem("visitedUrls")) || [];
      console.log("Visited URLs before back:", visitedUrls);

      visitedUrls.pop(); // Remove the current URL
      const previousUrl = visitedUrls.pop(); // Get the previous URL

      if (previousUrl) {
        console.log("Navigating to:", previousUrl);
        window.location.href = previousUrl;
        sessionStorage.setItem("visitedUrls", JSON.stringify(visitedUrls));
      } else {
        console.error("No previous URL found.");
      }
    });
  }
});