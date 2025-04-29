document.addEventListener('DOMContentLoaded', function () {
  // CONSTRUCTOR FOR PAGE ITEM
  class PageItem {
    constructor(url, scrollY, isBack) {
      this.url = url;
      this.scrollY = scrollY;
      this.isBack = isBack;
    }
  }

  // GETTING VISITED PAGES FROM SESSION STORAGE
  let visitedPages = JSON.parse(sessionStorage.getItem('visitedUrls')) || [];

  // ADDING CURRENT PAGE TO VISITED PAGES
  const currentUrl = window.location.href;

  // Check if the navigation was triggered by the back button
  const isNavigatingBack = sessionStorage.getItem('isNavigatingBack') === 'true';

  if (!isNavigatingBack && !visitedPages.some((page) => page.url === currentUrl && page.isBack)) {
    const pageItem = new PageItem(currentUrl, window.scrollY, false); // isBack = false for normal navigation
    visitedPages.push(pageItem);
    sessionStorage.setItem('visitedUrls', JSON.stringify(visitedPages));
    console.log('Added new page to visited pages:', pageItem);
  } else {
    console.log('Page is marked as isBack or navigation was triggered by back button and will not be added:', currentUrl);
  }

  // Clear the isNavigatingBack flag after processing
  sessionStorage.setItem('isNavigatingBack', 'false');

  // BACK BUTTON FUNCTIONALITY
  const backButton = document.getElementById('backButton');
  if (backButton) {
    backButton.addEventListener('click', function () {
      if (visitedPages.length > 1) {
        // Remove the current page
        visitedPages.pop();

        // Get the last visited page
        const lastVisitedPage = visitedPages[visitedPages.length - 1];
        sessionStorage.setItem('visitedUrls', JSON.stringify(visitedPages));

        // Mark the last visited page as isBack = true
        lastVisitedPage.isBack = true;
        console.log('Navigating back to:', lastVisitedPage);

        // Set the isNavigatingBack flag
        sessionStorage.setItem('isNavigatingBack', 'true');

        // Navigate to the last visited page
        window.location.href = lastVisitedPage.url;
      } else {
        console.log('No previous pages to go back to.');
      }
    });
  }

  console.log('Visited Pages:', visitedPages);

  // SAVE SCROLL POSITION ON BEFOREUNLOAD
  window.addEventListener('beforeunload', function () {
    const currentScrollY = window.scrollY;

    // Update the scrollY value for the current page in visitedPages
    const currentPage = visitedPages.find((page) => page.url === currentUrl);
    if (currentPage) {
      currentPage.scrollY = currentScrollY;
      sessionStorage.setItem('visitedUrls', JSON.stringify(visitedPages));
      console.log('Updated scroll position for current page:', currentPage);
    }
  });

  // RESTORE SCROLL POSITION ON PAGE LOAD
  window.addEventListener('load', function () {
    const currentPage = visitedPages.find((page) => page.url === currentUrl);
    if (currentPage && currentPage.scrollY !== undefined) {
      window.scrollTo(0, currentPage.scrollY);
      console.log('Restored scroll position to:', currentPage.scrollY);
    }
  });
});